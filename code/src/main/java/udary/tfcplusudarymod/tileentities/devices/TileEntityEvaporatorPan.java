package udary.tfcplusudarymod.tileentities.devices;

import java.util.Random;
import java.util.Stack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerEvaporatorPan;
import udary.tfcplusudarymod.containerslots.SlotEvaporatorPanOutput;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.ModTime;
import udary.tfcplusudarymod.core.managers.EvaporatorPanManager;
import udary.tfcplusudarymod.core.recipes.EvaporatorPanRecipe;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.NetworkTileEntity;
import com.dunk.tfc.api.TFCFluids;

public class TileEntityEvaporatorPan extends NetworkTileEntity implements IInventory
{
	protected static EvaporatorPanManager evaporatorPanManager = EvaporatorPanManager.getInstance();
	
	public static final int STORAGE_SIZE = 4;

	public FluidStack inputFS;
	protected ItemStack[] storage;
	
	protected int evaporationCount;
	protected int evaporationDelay;
	protected int evaporationTemperature;
	protected long evaporationTimer;
	
	public TileEntityEvaporatorPan()
	{
		clearInventory();

		this.broadcastRange = 5;
		
		this.inputFS = null;
		
		this.evaporationCount = 0;
		this.evaporationDelay = Math.max(ModOptions.evaporationDelay, 0);
		this.evaporationTemperature = Math.max(ModOptions.evaporationTemperature, 0);
		this.evaporationTimer = 0;
	}

	public void actionEmpty()
	{
		evaporationCount = 0;
		inputFS = null;		
		
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setByte(ModTags.TAG_FLUID_ID, (byte)-1);
		
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	protected void actionProcess()
	{
		NBTTagCompound nbt = ModTags.createEvaporationPanProcessTagCompound(evaporationCount, storage);
		this.broadcastPacketInRange(this.createDataPacket(nbt));
	}
	
	public boolean addFluid(FluidStack fs)
	{
		if (inputFS == null)
		{
			// copy the fluid stack.
			inputFS = fs.copy();
			
			// check if the fluid amount > maximum amount held by the container.
			if (inputFS.amount > getMaxFluid())
			{
				inputFS.amount = getMaxFluid();
				fs.amount = fs.amount - inputFS.amount;
			}
			else 
			{
				fs.amount = 0;
			}
		}
		else
		{
			//check if full or if the fluid being added does not match the current fluid.
			if (inputFS.amount == getMaxFluid() || !inputFS.isFluidEqual(fs))
			{
				return false;
			}

			// check if there will be any overflow of fluid.
			int fluidOverflow = (inputFS.amount+fs.amount) - getMaxFluid();
			inputFS.amount = Math.min(inputFS.amount+fs.amount, getMaxFluid());
			
			if (fluidOverflow > 0) 
			{
				fs.amount = fluidOverflow;
			}
			else 
			{
				fs.amount = 0;
			}
		}
		
		worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		
		return true;
	}

	public ItemStack addFluid(ItemStack is)
	{
		if (is == null || is.stackSize > 1)
		{
			return is;
		}
		
		if (FluidContainerRegistry.isFilledContainer(is))
		{
			FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(is);
			
			if (addFluid(fs))
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				
				return FluidContainerRegistry.drainFluidContainer(is);
			}
		}
		else if (is.getItem() instanceof IFluidContainerItem)
		{
			FluidStack fs = ((IFluidContainerItem)is.getItem()).getFluid(is);
			int maxDrain = ((IFluidContainerItem)is.getItem()).getCapacity(is);
			
			if (fs != null && addFluid(fs))
			{
				((IFluidContainerItem)is.getItem()).drain(is, maxDrain, true);
				
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
		
		return is;
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

	public void drainFluid(int amount)
	{
		if (inputFS != null)
		{
			inputFS.amount -= amount;
			
			if (inputFS.amount <= 0)
			{
				actionEmpty();
			}
			else
			{
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
			}
		}
	}

	public void ejectContents()
	{
		Random rand = new Random();

		// clear the fluid.
		evaporationCount = 0;
		inputFS = null;

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
	
	public boolean getCanEvaporate()
	{
		if (this.evaporationDelay <= 0)
			return false;
		
		// check if the block above can see the sky, it is daytime and it has fluid.
		if (!worldObj.canBlockSeeTheSky(xCoord, yCoord+1, zCoord) || worldObj.isRaining() || !ModTime.isDayTime() || this.inputFS == null)
			return false;
		
		// get the current temperature.
		float temp = TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord+1, zCoord);
		
		// check the current temperature against the evaporation temperature.
		if (temp < this.evaporationTemperature)
			return false;
		
		return true;
	}

	public int getFluidLevel()
	{
		if (inputFS != null)
			return inputFS.amount;
		
		return 0;
	}

	public int getFluidScaled(int scale)
	{
		if (inputFS != null)
			return inputFS.amount * scale / getMaxFluid();
		
		return 0;
	}

	public FluidStack getFluidStack()
	{
		return inputFS;
	}
	
	public static int[] getInputSlotIndexes()
	{
		return ContainerEvaporatorPan.getInputSlotIndexes();
	}

	@Override
	public String getInventoryName()
	{
		return "EvaporatorPan";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public int getMaxFluid()
	{
		return ModFluids.EvaporatorPanVolume;
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return ContainerEvaporatorPan.getOutputSlotIndexes();
	}
	
	public int getProcessPercentage()
	{
		// find a recipe that matches the input components.
		EvaporatorPanRecipe recipe = (EvaporatorPanRecipe)evaporatorPanManager.findRecipe(inputFS, EnumRecipeMatchType.BASIC);
		if (recipe == null)
			return 0;
		
		return (int)Math.min((int)((float)evaporationCount / (float)recipe.getInputFluidAmount() * 100F), 100F);			
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
			case ContainerEvaporatorPan.SLOT_OUTPUT_1:
			case ContainerEvaporatorPan.SLOT_OUTPUT_2:
			case ContainerEvaporatorPan.SLOT_OUTPUT_3:
			case ContainerEvaporatorPan.SLOT_OUTPUT_4:
				return new SlotEvaporatorPanOutput(null, this, slotIndex, 0, 0);
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
	
	protected void performEvaporation()
	{
		if (worldObj.isRemote) return;

		if (inputFS != null && inputFS.amount > 0)
		{
			// increase the evaporation count.
			evaporationCount++;
			
			// reduce the fluid amount.
			inputFS.amount = Math.max(inputFS.amount-1, 0);
			
			// create a temporary input fluid stack.
			FluidStack tempFS = inputFS.copy();
			tempFS.amount = evaporationCount;
			
			// check the fluid amount.
			if (inputFS.amount <= 0)
			{
				// fluid amount is 0, clear fluid.
				actionEmpty();
			}

			// find a recipe that matches the fluid stack.
			EvaporatorPanRecipe recipe = (EvaporatorPanRecipe)evaporatorPanManager.findRecipe(tempFS, EnumRecipeMatchType.MINIMUM);
			
			boolean recipeProcessed = false;
			
			// check if a valid recipe was found.
			if (recipe != null)
			{
				// get the recipe results.
				RecipeResult result = recipe.getRecipeResult(tempFS, true);
				
				if (result != null && result.getOutputItemCount() > 0)
				{
					Stack<ItemStack> resultISStack = result.getOutputItemStacks();
					
					while (!resultISStack.isEmpty())
					{
						ItemStack resultIS = resultISStack.pop();
						
						// check if there is enough space in an output slot
						int[] outputSlotIndexes = getOutputSlotIndexes();
						
						for (int i = 0; i < outputSlotIndexes.length; i++)
						{
							ItemStack storageIS = storage[outputSlotIndexes[i]];						
	
							// check if storage slot is empty.
							if (storageIS == null)
							{
								// assign the result to the storage slot
								storage[outputSlotIndexes[i]] = resultIS.copy();
								// clear the result stack size.
								resultIS.stackSize = 0;
								
								// result stack size is 0, exit loop.
								break;
							}
							
							// check if the storage slot contains the same item.
							if (TFC_Core.areItemsEqual(storageIS, resultIS))
							{
								// check if the result stack size will fit into the storage slot.
								if (storageIS.stackSize + resultIS.stackSize <= storageIS.getMaxStackSize())
								{
									// increase the storage stack size by the result stack size. 
									storageIS.stackSize += resultIS.stackSize;
									// clear the result stack size.
									resultIS.stackSize = 0; 

									// result stack size is 0, exit loop.
									break;
								}
								else
								{
									// result stack size has to many, fill the storage slot.
									
									// reduce the result stack size by the difference.
									resultIS.stackSize -= storageIS.getMaxStackSize() - storageIS.stackSize;
									// set the storage stack size to the maximum.
									storageIS.stackSize = storageIS.getMaxStackSize();
								}
							}
						}
						
						// no room left in the storage for the result item, spawn into the world.
						if (resultIS.stackSize > 0)
						{
							worldObj.spawnEntityInWorld(new EntityItem(worldObj, xCoord, yCoord, zCoord, resultIS));
						}
					}		
					
					recipeProcessed = true;
				}
			}
			
			// check if the recipe was processed.
			if (recipeProcessed)
			{
				// set the evaporation count.
				evaporationCount = tempFS != null ? Math.max(tempFS.amount, 0) : 0;
			}
			
			actionProcess();
			
			updateGui();
		}
	}
	
	public ItemStack removeFluid(ItemStack is)
	{
		if (is == null || is.stackSize > 1)
		{
			return is;
		}
		
		if (FluidContainerRegistry.isEmptyContainer(is))
		{
			ItemStack outIS = FluidContainerRegistry.fillFluidContainer(inputFS, is);
			
			if (outIS != null)
			{
				FluidStack fs = FluidContainerRegistry.getFluidForFilledItem(outIS);
				inputFS.amount -= fs.amount;
				is = null;
				
				if (inputFS.amount <= 0)
				{
					actionEmpty();
				}
				
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				
				return outIS;
			}
		}
		else if (inputFS != null && is.getItem() instanceof IFluidContainerItem)
		{
			FluidStack fs = ((IFluidContainerItem)is.getItem()).getFluid(is);
			
			if (fs == null || inputFS.isFluidEqual(fs))
			{
				inputFS.amount -= ((IFluidContainerItem)is.getItem()).fill(is, inputFS, true);
				
				if (inputFS.amount <= 0)
				{
					actionEmpty();
				}
				
				worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
				
				return is;
			}
		}
		
		return is;
	}
	
	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack is)
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return;
				
		storage[slotIndex] = is;
		
		if(is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
		
		actionProcess();
		
		if (!worldObj.isRemote) 
			updateGui();
	}

	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote) return;
		
		// If lightning can strike here then it means that the container can see the sky, so rain can hit it.
		// If true then we fill the container when its raining.
		if (worldObj.canLightningStrikeAt(xCoord, yCoord+1, zCoord))
		{
			if (inputFS == null)
			{
				inputFS = new FluidStack(TFCFluids.FRESHWATER, 1);
			}
			else if (inputFS != null && inputFS.getFluid() == TFCFluids.FRESHWATER)
			{
				inputFS.amount = Math.min(inputFS.amount+1, getMaxFluid());
			}
			
			this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
		}
		
		if (getCanEvaporate())
		{
			// increase the timer.
			evaporationTimer++;
			
			// check if the timer has expired.
			if (evaporationTimer >= evaporationDelay)
			{
				performEvaporation();
				
				// reset the timer.
				evaporationTimer = 0;
			}			
		}
		
		TFC_Core.handleItemTicking(this, this.worldObj, xCoord, yCoord, zCoord);
		
		// check the fluid amount.
		if (inputFS != null && inputFS.amount <= 0)
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
		
		if (inputFS != null)
		{
			nbt.setTag(ModTags.TAG_FLUID_TAG, inputFS.writeToNBT(new NBTTagCompound()));
			nbt.setInteger(ModTags.TAG_EVAPORATION_COUNT, evaporationCount);
		}
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt)
	{
		clearInventory();

		TFC_Core.readInventoryFromNBT(nbt, storage);
		
		if (nbt.hasKey(ModTags.TAG_FLUID_TAG))
		{
			inputFS = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(ModTags.TAG_FLUID_TAG));
			evaporationCount = nbt.getInteger(ModTags.TAG_EVAPORATION_COUNT);
		}
		else
		{
			inputFS = null;
			evaporationCount = 0;
		}
		
		this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void handleDataPacket(NBTTagCompound nbt)
	{
		boolean markBlockForUpdate = false;
		
		if (nbt.hasKey(ModTags.TAG_FLUID_ID))
		{
			if(nbt.getByte(ModTags.TAG_FLUID_ID) == -1)
			{
				inputFS = null;
				evaporationCount = 0;

				markBlockForUpdate = true;
			}
		}
		
		if (nbt.hasKey(ModTags.TAG_EVAPORATION_COUNT) && inputFS != null)
		{
			evaporationCount = nbt.getInteger(ModTags.TAG_EVAPORATION_COUNT);
			markBlockForUpdate = true;
		}
		
		if (markBlockForUpdate)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		super.handleDataPacket(nbt);
	}

	public void readFromItemNBT(NBTTagCompound nbt)
	{
		TFC_Core.readInventoryFromNBT(nbt, storage);
		
		if (nbt.hasKey(ModTags.TAG_FLUID_TAG))
		{
			inputFS = FluidStack.loadFluidStackFromNBT(nbt.getCompoundTag(ModTags.TAG_FLUID_TAG));
			evaporationCount = nbt.getInteger(ModTags.TAG_EVAPORATION_COUNT);
		}
		else
		{
			inputFS = null;
			evaporationCount = 0;
		}
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
		
		if (inputFS != null)
		{
			nbt.setTag(ModTags.TAG_FLUID_TAG, inputFS.writeToNBT(new NBTTagCompound()));	
			nbt.setInteger(ModTags.TAG_EVAPORATION_COUNT, evaporationCount);
		}
	}
}
