package udary.tfcplusudarymod.tileentities.materials;

import java.util.Random;
import java.util.Stack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.containers.materials.ContainerDryingMat;
import udary.tfcplusudarymod.containerslots.SlotDryingMat;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.ModTime;
import udary.tfcplusudarymod.core.managers.DryingMatManager;
import udary.tfcplusudarymod.core.recipes.DryingMatRecipe;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.NetworkTileEntity;
import com.dunk.tfc.api.Food;

public class TileEntityDryingMat extends NetworkTileEntity implements IInventory
{
	protected static DryingMatManager dryingMatManager = DryingMatManager.getInstance();
	
	public static final int STORAGE_SIZE = 1;

	protected ItemStack[] storage;
	
	protected int dryingDelay;
	protected int dryingTemperature;
	protected int dryingTimer;
	
	protected float baseModifierDry;
	protected float baseModifierRain;
	protected float envModifierDry;
	protected float envModifierRain;

	public TileEntityDryingMat()
	{
		clearInventory();
		
		dryingDelay = Math.max(ModOptions.dryingDelay, 0);
		dryingTemperature = Math.max(ModOptions.dryingTemperature, 0);
		dryingTimer = 0;
		
		baseModifierDry = 0.75f;
		baseModifierRain = 2.0f;
		envModifierDry = 0.75f;
		envModifierRain = 3.0f;
	}
	
	public void actionProcess()
	{
		NBTTagCompound nbt = ModTags.createDryingMatProcessTagCompound(this.storage);
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	public void clearInventory()
	{
		storage = new ItemStack[STORAGE_SIZE];
	}

	@Override
	public void closeInventory() 
	{
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

	public void ejectContents()
	{
		Random rand = new Random();

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
	
	public boolean getCanDry()
	{
		if (dryingDelay <= 0)
			return false;
		
		// check if the block above can see the sky, it is daytime and it has something to dry.
		if (!worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord) || worldObj.isRaining() || !ModTime.isDayTime())
			return false;
		
		// get the current temperature.
		float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord+1, zCoord);
		
		// check the current temperature against the drying temperature.
		if (temp < dryingTemperature)
			return false;
		
		// get the input item stack.
		ItemStack inputIS = storage[ContainerDryingMat.SLOT_INPUT_1];

		// find a recipe that matches the input components.
		DryingMatRecipe recipe = dryingMatManager.findRecipe(inputIS, EnumRecipeMatchType.MINIMUM);
		if (recipe == null)
			return false;
		
		return true;
	}
	
	public static int[] getInputSlotIndexes()
	{
		return ContainerDryingMat.getInputSlotIndexes();
	}
	
	@Override
	public String getInventoryName()
	{
		return "DryingMat";
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return ContainerDryingMat.getOutputSlotIndexes();
	}
	
	public int getProcessPercentage()
	{
		// get the input item stack.
		ItemStack inputIS = storage[ContainerOreCooker.SLOT_INPUT_1];

		// find a recipe that matches the input components.
		DryingMatRecipe recipe = dryingMatManager.findRecipe(inputIS, EnumRecipeMatchType.BASIC);
		if (recipe == null)
			return 0;

		// check if the item has a drying process tag.
		NBTTagCompound tag = ModTags.applyDryingTag(inputIS);
		if (tag == null)
			return 0;
		
		// get the current drying count.
		long dryingCount = tag.getLong(ModTags.TAG_DRYING_COUNT);
		
		return (int)Math.min((int)((float)dryingCount / (float)recipe.getRecipeDuration(inputIS) * 100F), 100F);	
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
			case ContainerDryingMat.SLOT_INPUT_1:
				return new SlotDryingMat(null, this, slotIndex, 0, 0);
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
	public boolean hasCustomInventoryName() 
	{
		return false;
	}
	
	@Override
	public boolean isItemValidForSlot(int slotIndex, ItemStack is)
	{
		Slot slot = getSlot(slotIndex);
		if (slot == null)
			return false;
		
		return slot.isItemValid(is);
	}

	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return true;
	}

	public boolean mergeStackInSlot(int slotIndex, ItemStack is)
	{
		return ModGlobal.mergeItemStack(this, getSlot(slotIndex), is);
	}
	
	@Override
	public void openInventory() 
	{
	}
	
	protected void performDrying()
	{
		if (worldObj.isRemote) return;

		// get the item that is drying.
		ItemStack inputIS = storage[ContainerDryingMat.SLOT_INPUT_1];
		
		if (inputIS != null)
		{
			// find a recipe that matches the input components.
			DryingMatRecipe recipe = dryingMatManager.findRecipe(inputIS, EnumRecipeMatchType.MINIMUM);

			// check if a recipe was found.
			if (recipe != null)
			{
				// check if the item has a drying process tag.
				NBTTagCompound tag = ModTags.applyDryingTag(inputIS);
				
				// get the current drying count.
				long dryingCount = tag.getLong(ModTags.TAG_DRYING_COUNT);
				
				// increase the drying count by the delay.
				dryingCount += dryingDelay;
				
				// update the drying count on the item.
				tag.setLong(ModTags.TAG_DRYING_COUNT, dryingCount);
				
				// check if the item is fully dried.
				if (dryingCount >= recipe.getRecipeDuration(inputIS))
				{
					// get the recipe results.
					RecipeResult result = recipe.getRecipeResult(inputIS);
										
					if (result != null && result.getOutputItemCount() > 0)
					{
						Stack<ItemStack> resultISStack = result.getOutputItemStacks();

						// get the first result from the list.
						ItemStack resultIS = resultISStack.pop();
						
						// set the storage slot to the result item.
						storage[ContainerDryingMat.SLOT_INPUT_1] = resultIS;
					}
				}

				ItemStack storageIS = storage[ContainerDryingMat.SLOT_INPUT_1];
				
				// check if the item has been dried.
				if (storageIS != null && Food.isDried(storageIS))
				{
					// item dried, remove the drying process tag.
					ModTags.clearTag(storageIS, ModTags.TAG_DRYING_TAG, true);
				}
			}			
			
			actionProcess();
			
			updateGui();
		}
	}
	
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack is) 
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return;
		if (ItemStack.areItemStacksEqual(storage[slotIndex], is)) return;
		if (TFC_Core.areItemsEqual(storage[slotIndex], is)) return;
				
		storage[slotIndex] = is;
		
		if (slotIndex == ContainerDryingMat.SLOT_INPUT_1)
		{
			dryingTimer = 0;
		}
		
		actionProcess();
		
		updateGui();
	}

	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote) return;

		// set the base and environment decay values. 
		float base = 1.0f;
		float env = 1.0f;

		// If lightning can strike here then it means that the container can see the sky, so rain can hit it.
		// If true then we increase the decay modifiers to decay faster.
		if (worldObj.canLightningStrikeAt(xCoord, yCoord+1, zCoord))
		{
			// the food is exposed to the rain, decay the food faster.
			base *= baseModifierRain;
			env *= envModifierRain; 
		}
		
		if (getCanDry())
		{
			base *= baseModifierDry;
			env *= envModifierDry; 

			dryingTimer++;
			
			// check if the timer has expired
			if (dryingTimer >= dryingDelay)
			{
				performDrying();
				
				// reset the timer
				dryingTimer = 0;
			}
		}
		
		TFC_Core.handleItemTicking(this, worldObj, xCoord, yCoord, zCoord, env, base);
	}
	
	public void updateGui()
	{
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}

	
	@Override
	public void createInitNBT(NBTTagCompound nbt)
	{
		TFC_Core.writeInventoryToNBT(nbt, storage);
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt)
	{
		clearInventory();

		TFC_Core.readInventoryFromNBT(nbt, storage);
		
		this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void handleDataPacket(NBTTagCompound nbt)
	{
		boolean markBlockForUpdate = false;

		if (markBlockForUpdate)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		super.handleDataPacket(nbt);
	}

	public void readFromItemNBT(NBTTagCompound nbt)
	{
		TFC_Core.readInventoryFromNBT(nbt, storage);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt)
	{
		super.readFromNBT(nbt);
		
		readFromItemNBT(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt)
	{
		super.writeToNBT(nbt);

		TFC_Core.writeInventoryToNBT(nbt, storage);
	}
}
