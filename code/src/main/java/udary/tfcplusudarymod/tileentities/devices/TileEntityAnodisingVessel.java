package udary.tfcplusudarymod.tileentities.devices;

import java.util.Random;
import java.util.Stack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraftforge.fluids.FluidStack;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.TFCUdaryMod;
import udary.tfcplusudarymod.containers.devices.ContainerAnodisingVessel;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselAnode;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselBattery;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselCathode;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselInput;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselSolute;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.ModTime;
import udary.tfcplusudarymod.core.managers.AnodisingVesselAnodiseManager;
import udary.tfcplusudarymod.core.managers.AnodisingVesselFluidManager;
import udary.tfcplusudarymod.core.recipes.AnodisingVesselAnodiseRecipe;
import udary.tfcplusudarymod.core.recipes.AnodisingVesselFluidRecipe;
import udary.tfcplusudarymod.handlers.GuiHandler;
import udary.tfcplusudarymod.interfaces.IBattery;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.TFCFluids;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class TileEntityAnodisingVessel extends TEBarrel implements IInventory 
{
	protected static AnodisingVesselAnodiseManager anodisingVesselAnodiseManager = AnodisingVesselAnodiseManager.getInstance();
	protected static AnodisingVesselFluidManager anodisingVesselFluidManager = AnodisingVesselFluidManager.getInstance();
	
	public static final int STORAGE_SIZE = 5;
	public static final int TECH_LEVEL = 1000;	

	public static int maxLiquid = ModFluids.AnodisingVesselVolume;
	
	protected int anodisingDelay;
	protected int anodisingTimer;
	protected long anodisingStart;
	protected int evaporationDelay;
	protected int evaporationTemperature;
	protected long evaporationTimer;
	
	public TileEntityAnodisingVessel()
	{
		clearInventory();
		
		anodisingDelay = Math.max(ModOptions.anodisingDelay, 0);
		anodisingTimer = 0;
		anodisingStart = 0;
		
		evaporationDelay = Math.max(ModOptions.evaporationDelay, 0);
		evaporationTemperature = Math.max(ModOptions.evaporationTemperature, 0);
		evaporationTimer = 0;
	}
	
	protected void actionProcess()
	{
		NBTTagCompound nbt = ModTags.createAnodisingVesselProcessTagCompound(anodisingStart);
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	@Override
	public void clearInventory()
	{
		storage = new ItemStack[STORAGE_SIZE];
	}

	@Override
	public ItemStack decrStackSize(int slotIndex, int amount) 
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return null;
		if (storage[slotIndex] == null) return null;
		
		if (storage[slotIndex].stackSize <= amount)
		{
			ItemStack itemstack = storage[slotIndex];
			storage[slotIndex] = null;
			return itemstack;
		}
		
		ItemStack itemstack = storage[slotIndex].splitStack(amount);
		if (storage[slotIndex].stackSize == 0)
			storage[slotIndex] = null;
		return itemstack;
	}
	
	@Override
	public void ejectContents()
	{
		Random rand = new Random();

		// clear the fluid.
		fluid = null;

		// spawn all the storage items into the world.
		for (int slotIndex = 0; slotIndex < storage.length; slotIndex++)
		{
			ejectItem(slotIndex, rand);
		}

		// clear the storage.
		clearInventory();
	}
	
	public boolean ejectItem(int slotIndex)
	{
		Random rand = new Random();

		return ejectItem(slotIndex, rand);
	}
	
	protected boolean ejectItem(int slotIndex, Random rand)
	{
		if (slotIndex < 0 || slotIndex >= storage.length) 
			return false;
		
		float f = rand.nextFloat() * 0.3F + 0.1F;
		float f1 = rand.nextFloat() * 2.0F + 0.4F;
		float f2 = rand.nextFloat() * 0.3F + 0.1F;
		float f3 = 0.05F;

		// spawn the storage item into the world.
		if (storage[slotIndex] != null)
		{
			EntityItem entityitem = new EntityItem(worldObj, xCoord + f, yCoord + f1, zCoord + f2, storage[slotIndex]);
			entityitem.motionX = (float)rand.nextGaussian() * f3;
			entityitem.motionY = (float)rand.nextGaussian() * f3 + 0.2F;
			entityitem.motionZ = (float)rand.nextGaussian() * f3;
			
			if (worldObj.spawnEntityInWorld(entityitem))
				storage[slotIndex] = null;
			
			return true;
		}
		
		return false;
	}
	
	public boolean getCanAnodise()
	{
		if (!getSealed() || anodisingDelay <= 0)
			return false;
		
		if (this.fluid == null)
			return false;

		// get all the item stack objects.
		ItemStack batteryIS = getStackInSlot(ContainerAnodisingVessel.SLOT_BATTERY);
		ItemStack cathodeIS = getStackInSlot(ContainerAnodisingVessel.SLOT_CATHODE);
		ItemStack anodeIS = getStackInSlot(ContainerAnodisingVessel.SLOT_ANODE);
		ItemStack soluteIS = getStackInSlot(ContainerAnodisingVessel.SLOT_SOLUTE);

		// check if all the slots are correctly filled.
		if (batteryIS == null || anodeIS == null || cathodeIS == null || soluteIS == null)
			return false;
			
		// find a recipe that matches the input components.
		AnodisingVesselAnodiseRecipe recipe = anodisingVesselAnodiseManager.findRecipe(this.fluid, soluteIS, EnumRecipeMatchType.MINIMUM);
		if (recipe == null)
			return false;

		// check if the battery has enough charge for the process.
		IBattery battery = (IBattery)batteryIS.getItem();
		if (battery == null || !battery.hasEnoughCharge(batteryIS, recipe.getBatteryChargeAmount()))
			return false;

		// check if the anode and cathode item stacks have enough damage remaining.
		if (anodeIS.getItemDamage() + recipe.getAnodeDamageAmount() > anodeIS.getMaxDamage())
			return false;
		
		if (cathodeIS.getItemDamage() + recipe.getCathodeDamageAmount() > cathodeIS.getMaxDamage())
			return false;
		
		return true;
	}
	
	public boolean getCanCreateFluid()
	{
		if (getSealed())
			return false;
		
		// get the input item stack.
		ItemStack inputIS = getStackInSlot(ContainerAnodisingVessel.SLOT_INPUT);

		// find a recipe that matches the input components.
		AnodisingVesselFluidRecipe recipe = anodisingVesselFluidManager.findRecipe(this.fluid, inputIS, EnumRecipeMatchType.MINIMUM);
		if (recipe == null)
			return false;

		return true;
	}
	
	public boolean getCanEvaporate()
	{
		if (getSealed() || evaporationDelay <= 0)
			return false;
		
		// check if the block above can see the sky, it is daytime and it has fluid.
		if (!worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord) || worldObj.isRaining() || !ModTime.isDayTime() || this.fluid == null)
			return false;

		// get the current temperature.
		float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord+1, zCoord);
		
		// check the current temperature against the evaporation temperature.
		if (temp < evaporationTemperature)
			return false;
		
		return true;
	}
	
	public static int[] getInputSlotIndexes()
	{
		return ContainerAnodisingVessel.getInputSlotIndexes();
	}

	@Override
	public int getInvCount()
	{
		return 0;
	}
	
	@Override
	public String getInventoryName()
	{
		return "Anodising Vessel";
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}	
	
	public boolean getIsProcessing()
	{
		return anodisingStart > 0;
	}
	
	@Override
	public int getMaxLiquid()
	{
		return maxLiquid;
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return ContainerAnodisingVessel.getOutputSlotIndexes();
	}

	public int getProcessPercentage()
	{
		if (anodisingStart == 0)
			return 0;
		
		// get the input item stack.
		ItemStack soluteIS = getStackInSlot(ContainerAnodisingVessel.SLOT_SOLUTE);
		
		// find a recipe that matches the input components.
		AnodisingVesselAnodiseRecipe recipe = anodisingVesselAnodiseManager.findRecipe(this.fluid, soluteIS, EnumRecipeMatchType.BASIC);
		if (recipe == null)
			return 0;
		
		// get the current tick value.
		long currentTotalTicks = TFC_Time.getTotalTicks();
		long tickDifference = currentTotalTicks - anodisingStart;
		
		return (int)Math.min(((float)tickDifference / (float)recipe.getRecipeDuration() * 100F), 100F);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public AxisAlignedBB getRenderBoundingBox()
	{
		return AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord+1, yCoord+1, zCoord+1);
	}
	
	@Override
	public int getSizeInventory()
	{
		return storage.length;
	}
	
	protected Slot getSlot(int slotIndex)
	{
		switch (slotIndex)
		{
			case ContainerAnodisingVessel.SLOT_ANODE:
				return new SlotAnodisingVesselAnode(null, this, slotIndex, 0, 0);
				
			case ContainerAnodisingVessel.SLOT_BATTERY: 
				return new SlotAnodisingVesselBattery(null, this, slotIndex, 0, 0);
				
			case ContainerAnodisingVessel.SLOT_CATHODE: 
				return new SlotAnodisingVesselCathode(null, this, slotIndex, 0, 0);
				
			case ContainerAnodisingVessel.SLOT_SOLUTE: 
				return new SlotAnodisingVesselSolute(null, this, slotIndex, 0, 0);
				
			case ContainerAnodisingVessel.SLOT_INPUT: 
				return new SlotAnodisingVesselInput(null, this, slotIndex, 0, 0);
		}	
		
		return null;		
	}

	@Override
	public ItemStack getStackInSlot(int slotIndex)
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return null;
		return storage[slotIndex];
	}

	@Override
	public ItemStack getStackInSlotOnClosing(int slotIndex)
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return null;
		return storage[slotIndex];
	}

	@Override
	public int getTechLevel()
	{
		return TECH_LEVEL;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack is)
	{		
		Slot slot = getSlot(slotIndex);
		if (slot == null)
			return false;
		
		return slot.isItemValid(is);
	}

	public boolean mergeStackInSlot(int slotIndex, ItemStack is)
	{
		return ModGlobal.mergeItemStack(this, getSlot(slotIndex), is);
	}
	
	protected void performAnodisation()
	{
		if (worldObj.isRemote) return;

		// get the current tick value.
		long currentTotalTicks = TFC_Time.getTotalTicks();
		
		if (this.fluid != null && this.fluid.amount > 0)
		{
			// get all the item stack objects.
			ItemStack batteryIS = getStackInSlot(ContainerAnodisingVessel.SLOT_BATTERY);
			ItemStack cathodeIS = getStackInSlot(ContainerAnodisingVessel.SLOT_CATHODE);
			ItemStack anodeIS = getStackInSlot(ContainerAnodisingVessel.SLOT_ANODE);
			ItemStack soluteIS = getStackInSlot(ContainerAnodisingVessel.SLOT_SOLUTE);

			// check if all the slots are correctly filled.
			if (batteryIS != null && anodeIS != null && cathodeIS != null && soluteIS != null)
			{
				// find a recipe that matches the input components.
				AnodisingVesselAnodiseRecipe recipe = anodisingVesselAnodiseManager.findRecipe(this.fluid, soluteIS, EnumRecipeMatchType.MINIMUM);
				
				// check if a recipe was found.
				if (recipe != null)
				{
					if (this.anodisingStart == 0)
						setAnodisingStart(currentTotalTicks);
					
					while (this.anodisingStart + recipe.getRecipeDuration() <= currentTotalTicks)
					{
						// get the recipe results.
						RecipeResult result = recipe.getRecipeResult(this.fluid, batteryIS, cathodeIS, anodeIS, soluteIS, true);
						
						if (result != null)
						{
							// check if a new fluid was returned.
							if (result.getOutputFluidStack() != null)
							{
								// replace the fluid with the resulting fluid.
								this.fluid = result.getOutputFluidStack();
								
								// check the fluid amount does not exceed the maximum.
								if (this.fluid.amount > getMaxLiquid())
								{
									// to much fluid, reduce to the maximum this container can hold.
									this.fluid.amount = getMaxLiquid();
								}
							}
							
							// check if any output items were returned (should only be the new anode item).
							if (result.getOutputItemCount() > 0)
							{
								Stack<ItemStack> resultISStack = result.getOutputItemStacks();
								
								while (!resultISStack.isEmpty())
								{
									ItemStack resultIS = resultISStack.pop();
									
									// override with the new anode item stack.
									storage[ContainerAnodisingVessel.SLOT_ANODE] = resultIS;
								}
							}
						}						
							
						// check the fluid amount.
						if (this.fluid != null && this.fluid.amount <= 0)
						{
							// fluid amount is 0, clear fluid.
							actionEmpty();
						}
						
						// check the cathode stack size.
						if (cathodeIS.stackSize <= 0)
						{
							// clear the input slot.
							storage[ContainerAnodisingVessel.SLOT_CATHODE] = null;
						}
						
						// check the input stack size.
						if (soluteIS.stackSize <= 0)
						{
							// clear the input slot.
							storage[ContainerAnodisingVessel.SLOT_SOLUTE] = null;
						}
						
						// check if the process can be continued.
						if (!getCanAnodise())
						{
							setAnodisingStart(0);
							break;
						}
						
						setAnodisingStart(this.anodisingStart + recipe.getRecipeDuration());
					}
						
					updateGui();
				}
			}
		}
	}

	protected void performCreateFluid()
	{
		if (worldObj.isRemote) return;

		if (this.fluid != null && this.fluid.amount > 0)
		{
			// get the input item stack.
			ItemStack inputIS = storage[ContainerAnodisingVessel.SLOT_INPUT];

			if (inputIS != null)
			{
				// find a recipe that matches the input components.
				AnodisingVesselFluidRecipe recipe = anodisingVesselFluidManager.findRecipe(this.fluid, inputIS, EnumRecipeMatchType.MINIMUM);
				
				// check if a recipe was found.
				if (recipe != null)
				{
					// get the recipe results.
					RecipeResult result = recipe.getRecipeResult(this.fluid, inputIS, true);
					
					if (result != null)
					{
						// check if a new fluid was returned.
						if (result.getOutputFluidStack() != null)
						{
							// replace the fluid with the resulting fluid.
							this.fluid = result.getOutputFluidStack();
							
							// check the fluid amount does not exceed the maximum.
							if (this.fluid.amount > getMaxLiquid())
							{
								// to much fluid, reduce to the maximum this container can hold.
								this.fluid.amount = getMaxLiquid();
							}
						}
	
						// check the fluid amount
						if (this.fluid != null && this.fluid.amount <= 0)
						{
							// fluid amount is 0, clear fluid.
							actionEmpty();
						}
						
						// check the input stack size.
						if (inputIS.stackSize <= 0)
						{
							// clear the input slot.
							storage[ContainerAnodisingVessel.SLOT_INPUT] = null;
						}
						
						updateGui();
					}
				}
			}
		}
	}
	
	protected void performEvaporation()
	{
		if (worldObj.isRemote) return;

		if (this.fluid != null && this.fluid.amount > 0)
		{
			// reduce the fluid amount.
			this.fluid.amount = Math.max(this.fluid.amount-1, 0);
			
			// check the fluid amount.
			if (this.fluid.amount <= 0)
			{
				// fluid amount is 0, clear fluid.
				actionEmpty();
			}					
			
			updateGui();
		}	
	}
	
	@Override
	public void processItems()
	{
	}
	
	protected void setAnodisingStart(long anodisingStart)
	{
		// record the current value.
		long tempAnodisingStart = this.anodisingStart;
		
		// update with the argument value.
		this.anodisingStart = anodisingStart;
		
		// check if the value has changed.
		if (tempAnodisingStart != this.anodisingStart)
			actionProcess();
	}
	
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack is) 
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return;
				
		storage[slotIndex] = is;
		
		if(is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();

		if (!getSealed())
			setSealTime();
		
		if (!worldObj.isRemote) 
			updateGui();
	}

	public void setSealTime()
	{
		if (getSealed())
		{
			sealtime = (int) TFC_Time.getTotalHours();
			unsealtime = 0;
		}		
		else
		{
			unsealtime = (int) TFC_Time.getTotalHours();
			sealtime = 0;
			
			// unsealed - reset the anodising start marker.
			setAnodisingStart(0);
		}
	}

	@Override
	protected void switchTab(EntityPlayer player, int tab)
	{
		if (player == null) return;
		
		switch (tab)
		{
			default:
				player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdAnodisingVessel, worldObj, xCoord, yCoord, zCoord);
		}
	}	
	
	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote) return;
		
		// If lightning can strike here then it means that the container can see the sky, so rain can hit it.
		// If true then we fill the container when its raining.
		if (!this.getSealed() && worldObj.canLightningStrikeAt(xCoord, yCoord+1, zCoord))
		{
			if (this.fluid == null)
			{
				this.fluid = new FluidStack(TFCFluids.FRESHWATER, 1);
			}
			else if (this.fluid != null && this.fluid.getFluid() == TFCFluids.FRESHWATER)
			{
				this.fluid.amount = Math.min(this.fluid.amount+1, getMaxLiquid());
			}
			
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}

		if (getCanCreateFluid())
		{
			performCreateFluid();
		}
		
		if (getCanEvaporate())
		{
			// increase the timer
			evaporationTimer++;
			
			// check if the timer has expired
			if (evaporationTimer >= evaporationDelay)
			{
				performEvaporation();
				
				// reset the timer
				evaporationTimer = 0;
			}			
		}

		if (getCanAnodise())
		{
			// increase the timer
			anodisingTimer++;
			
			// check if the timer has expired
			if (anodisingTimer >= anodisingDelay)
			{
				performAnodisation();
				
				// reset the timer
				anodisingTimer = 0;
			}			
		}
		else
		{
			setAnodisingStart(0);
		}
		
		TFC_Core.handleItemTicking(this, this.worldObj, xCoord, yCoord, zCoord);
	
		// check the fluid amount
		if (this.fluid != null && this.fluid.amount <= 0)
		{
			// fluid amount is 0, clear fluid.
			actionEmpty();
		}
	}
	
	public void updateGui()
	{
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	
	@Override
	public void createInitNBT(NBTTagCompound nbt) 
	{
		TFC_Core.writeInventoryToNBT(nbt, storage);
		nbt.setLong(ModTags.TAG_ANODISING_START, this.anodisingStart);
		
		super.createInitNBT(nbt);
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt) 
	{
		clearInventory();

		TFC_Core.readInventoryFromNBT(nbt, storage);
		this.anodisingStart = nbt.getLong(ModTags.TAG_ANODISING_START);
		
		super.handleInitPacket(nbt);
	}
	
	@Override
	public void handleDataPacket(NBTTagCompound nbt)
	{
		boolean markBlockForUpdate = false;

		if (nbt.hasKey(ModTags.TAG_ANODISING_START))
		{
			this.anodisingStart = nbt.getLong(ModTags.TAG_ANODISING_START);	
			markBlockForUpdate = true;
		}
		
		if (markBlockForUpdate)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
		super.handleDataPacket(nbt);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		this.anodisingStart = nbt.getLong(ModTags.TAG_ANODISING_START);

		super.readFromNBT(nbt);
	}

	@Override
	public void readFromItemNBT(NBTTagCompound nbt)
	{
		super.readFromItemNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		nbt.setLong(ModTags.TAG_ANODISING_START, this.anodisingStart);

		super.writeToNBT(nbt);
	}
}
