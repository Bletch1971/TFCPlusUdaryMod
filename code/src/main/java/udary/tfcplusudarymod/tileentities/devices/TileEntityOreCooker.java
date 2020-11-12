package udary.tfcplusudarymod.tileentities.devices;

import java.util.Random;
import java.util.Stack;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.containerslots.SlotOreCookerInput;
import udary.tfcplusudarymod.containerslots.SlotOreCookerOutput;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModHeat;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.managers.OreCookerManager;
import udary.tfcplusudarymod.core.recipes.OreCookerRecipe;

import com.dunk.tfc.Core.TFC_Climate;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.TileEntities.NetworkTileEntity;
import com.dunk.tfc.TileEntities.TEForge;

public class TileEntityOreCooker extends NetworkTileEntity implements IInventory
{
	protected static OreCookerManager oreCookerManager = OreCookerManager.getInstance();
	
	public static final int STORAGE_SIZE = 3;
	
	protected ItemStack[] storage;
	
	protected int cookingCount;
	protected int cookingDelay;
	protected int cookingTimer;
	protected int temperature;
	protected int temperatureDelayForge;
	protected int temperatureDelayHeight;
	protected int temperatureTimer; 
	
	public TileEntityOreCooker()
	{
		clearInventory();
		
		this.cookingCount = 0;
		this.cookingDelay = Math.max(ModOptions.cookingDelay, 0);
		this.cookingTimer = 0;
		
		this.temperature = 0;
		this.temperatureDelayForge = 5;
		this.temperatureDelayHeight = 20;
		this.temperatureTimer = 0; 
	}
	
	public void actionProcess()
	{
		NBTTagCompound nbt = ModTags.createOreCookerProcessTagCompound(this.cookingCount, this.temperature, this.storage);
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
	
	public boolean getCanAdjustTemperature()
	{
		return true;
	}
	
	public boolean getCanCook()
	{
		if (this.cookingDelay <= 0)
			return false;
		
		// get the input item stack.
		ItemStack inputIS = getStackInSlot(ContainerOreCooker.SLOT_INPUT_1);

		// find a recipe that matches the input components.
		OreCookerRecipe recipe = oreCookerManager.findRecipe(inputIS, EnumRecipeMatchType.MINIMUM);
		if (recipe == null)
			return false;

		// check if the cooker's temperature is high enough.
		if (this.temperature < recipe.getMinimumTemperature())
			return false;
		
		return true;
	}
	
	public static int[] getInputSlotIndexes()
	{
		return ContainerOreCooker.getInputSlotIndexes();
	}

	@Override
	public String getInventoryName()
	{
		return "OreCooker";
	}

	@Override
	public int getInventoryStackLimit()
	{
		return 64;
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return ContainerOreCooker.getOutputSlotIndexes();
	}
	
	public int getProcessPercentage()
	{
		// get the input item stack.
		ItemStack inputIS = storage[ContainerOreCooker.SLOT_INPUT_1];

		// find a recipe that matches the input components.
		OreCookerRecipe recipe = (OreCookerRecipe)oreCookerManager.findRecipe(inputIS, EnumRecipeMatchType.BASIC);
		if (recipe == null)
			return 0;
		
		return (int)Math.min(((float)cookingCount / (float)recipe.getRecipeDuration() * 100F), 100F);		
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
			case ContainerOreCooker.SLOT_INPUT_1:
				return new SlotOreCookerInput(null, this, slotIndex, 0, 0);
			case ContainerOreCooker.SLOT_OUTPUT_1:
			case ContainerOreCooker.SLOT_OUTPUT_2:
				return new SlotOreCookerOutput(null, this, slotIndex, 0, 0);
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
	
	public int getTemperature()
	{
		return this.temperature;
	}

	public int getTemperatureScaled(int scale)
	{
		return (this.temperature * scale) / ModHeat.MAXIMUM_TEMPERATURE;
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
	
	protected void performAdjustTemperature()
	{
		if (worldObj.isRemote) return;

		// record the current temperature.
		int origTemperature = temperature;
		
		// get the tile entity underneath the cooker.
		TileEntity tileEntity = worldObj.getTileEntity(xCoord, yCoord - 1, zCoord);
		
		// check if there is a forge underneath the cooker and it's temperature is higher than the cooker's temperature.
		if (tileEntity instanceof TEForge && ((TEForge)tileEntity).fireTemp > temperature)
		{
			// increase the cooker's temperature.
			temperature++;
		}
		else if (temperatureTimer % temperatureDelayForge == 0)
		{
			// decrease the cooker's temperature.
			temperature--;
		}
		
		// check if the timer has expired.
		if (temperatureTimer >= temperatureDelayHeight)
		{
			// check if the cooker's temperature is higher then the current temperature. 
			if (temperature > TFC_Climate.getHeightAdjustedTemp(worldObj, xCoord, yCoord, zCoord))
			{
				// decrease the cooker's temperature.
				temperature--;
			}

			// reset the timer.
			temperatureTimer = 0;
		}

		// make sure temperature does not drop below 0.
		temperature = Math.max(temperature, 0);

		// check if the temperature was changed.
		if (temperature != origTemperature)
		{
			actionProcess();
			
			updateGui();
		}
	}
	
	protected void performCook()
	{
		if (worldObj.isRemote) return;

		// get the input item stack.
		ItemStack inputIS = storage[ContainerOreCooker.SLOT_INPUT_1];

		if (inputIS != null)
		{
			// increase the cooking count.
			cookingCount++;
			
			boolean recipeProcessed = false;
			
			// find a recipe that matches the input components.
			OreCookerRecipe recipe = oreCookerManager.findRecipe(inputIS, EnumRecipeMatchType.MINIMUM);
			
			// check if a recipe was found.
			if (recipe != null && this.cookingCount >= recipe.getRecipeDuration())
			{
				// create a copy of the input item stack to be passed into the get result method.
				ItemStack tempIS = recipe.getInputItemStack();
				
				// get the recipe results.
				RecipeResult result = recipe.getRecipeResult(tempIS, false);

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
								// assign the result to the storage slot.
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

					// decrease the input item stack size.
					inputIS.stackSize -= tempIS.stackSize;
					
					// check if there are any more items to process.
					if (inputIS.stackSize <= 0)
					{
						// clear the input slot.
						storage[ContainerOreCooker.SLOT_INPUT_1] = null;
					}

					recipeProcessed = true;
				}
			}
			
			// check if the recipe was processed.
			if (recipeProcessed)
			{
				// set the cooking count.
				cookingCount = recipe != null ? Math.max(cookingCount - recipe.getRecipeDuration(), 0) : 0;
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
		
		if(is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
		
		if (slotIndex == ContainerOreCooker.SLOT_INPUT_1)
		{
			cookingCount = 0;
			cookingTimer = 0;
		}
		
		actionProcess();
		
		if (!worldObj.isRemote) 
			updateGui();
	}

	public void setTemperature(int value)
	{
		this.temperature = value;
	}
	
	@Override
	public void updateEntity()
	{
		if (worldObj.isRemote) return;

		if (getCanAdjustTemperature())
		{
			// increase the timer
			temperatureTimer++;
	
			performAdjustTemperature();
		}
		
		if (getCanCook())
		{
			// increase the timer.
			cookingTimer++;
			
			// check if the timer has expired.
			if (cookingTimer >= cookingDelay)
			{
				performCook();
				
				// reset the timer.
				cookingTimer = 0;				
			}
		}
		
		TFC_Core.handleItemTicking(this, this.worldObj, xCoord, yCoord, zCoord);
	}
	
	public void updateGui()
	{
		this.worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);
	}
	
	
	@Override
	public void createInitNBT(NBTTagCompound nbt)
	{
		TFC_Core.writeInventoryToNBT(nbt, storage);
		nbt.setInteger(ModTags.TAG_COOKING_COUNT, this.cookingCount);
		nbt.setInteger(ModTags.TAG_TEMPERATURE, this.temperature);
	}
	
	@Override
	public void handleInitPacket(NBTTagCompound nbt)
	{
		clearInventory();

		TFC_Core.readInventoryFromNBT(nbt, storage);
		this.cookingCount = nbt.getInteger(ModTags.TAG_COOKING_COUNT);
		this.temperature = nbt.getInteger(ModTags.TAG_TEMPERATURE);
		
		this.worldObj.func_147479_m(xCoord, yCoord, zCoord);
	}
	
	@Override
	public void handleDataPacket(NBTTagCompound nbt)
	{
		boolean markBlockForUpdate = false;
		
		if (nbt.hasKey(ModTags.TAG_COOKING_COUNT))
		{
			this.cookingCount = nbt.getInteger(ModTags.TAG_COOKING_COUNT);
			markBlockForUpdate = true;
		}
		
		if (nbt.hasKey(ModTags.TAG_TEMPERATURE))
		{
			this.temperature = nbt.getInteger(ModTags.TAG_TEMPERATURE);
			markBlockForUpdate = true;
		}
		
		if (markBlockForUpdate)
			worldObj.markBlockForUpdate(xCoord, yCoord, zCoord);

		super.handleDataPacket(nbt);
	}

	public void readFromItemNBT(NBTTagCompound nbt)
	{
		TFC_Core.readInventoryFromNBT(nbt, storage);
		this.cookingCount = nbt.getInteger(ModTags.TAG_COOKING_COUNT);
		this.temperature = nbt.getInteger(ModTags.TAG_TEMPERATURE);
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
		nbt.setInteger(ModTags.TAG_COOKING_COUNT, this.cookingCount);
		nbt.setInteger(ModTags.TAG_TEMPERATURE, this.temperature);
	}
}
