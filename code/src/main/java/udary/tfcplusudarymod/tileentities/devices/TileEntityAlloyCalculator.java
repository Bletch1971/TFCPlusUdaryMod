package udary.tfcplusudarymod.tileentities.devices;

import java.util.ArrayList;
import java.util.Random;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import udary.common.AlloyCalculatorResults;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyCalculator;
import udary.tfcplusudarymod.containerslots.SlotAlloyCalculator;
import udary.tfcplusudarymod.core.ModGlobal;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.TileEntities.NetworkTileEntity;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Interfaces.ISmeltable;

public class TileEntityAlloyCalculator extends NetworkTileEntity implements IInventory
{
	public static final int STORAGE_SIZE = 4;
	
	protected ItemStack[] storage;
	
	public TileEntityAlloyCalculator()
	{
		clearInventory();
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
	
	public AlloyCalculatorResults getAlloyCalculatorResults(Boolean useMetalAmounts)
	{
		int containerBagSlots = getSizeInventory();

		AlloyCalculatorResults results = new AlloyCalculatorResults(useMetalAmounts);
		
		for (int slotIndex = 0; slotIndex < containerBagSlots; slotIndex++)
		{
			ItemStack slotStack = getStackInSlot(slotIndex);
			if (slotStack == null) continue;
			
			Item slotItem = slotStack.getItem();
			Metal slotMetal = ((ISmeltable)slotItem).getMetalType(slotStack);
			int slotMetalAmount = ((ISmeltable)slotItem).getMetalReturnAmount(slotStack) * slotStack.stackSize;
			
			results.addMetal(slotMetal, slotMetalAmount);
		}
		
		return results;
	}
	
	public ArrayList<AlloyMetal> getIngredientList(Boolean useMetalAmounts)
	{
		AlloyCalculatorResults results = getAlloyCalculatorResults(useMetalAmounts);

		return results.getIngredientList();
	}
	
	public static int[] getInputSlotIndexes()
	{
		return ContainerAlloyCalculator.getInputSlotIndexes();
	}
	
	@Override
	public String getInventoryName()
	{
		return "AlloyCalculator";
	}

	@Override
	public int getInventoryStackLimit() 
	{
		return 64;
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return ContainerAlloyCalculator.getOutputSlotIndexes();
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
			case ContainerAlloyCalculator.SLOT_INPUT_1:
			case ContainerAlloyCalculator.SLOT_INPUT_2:
			case ContainerAlloyCalculator.SLOT_INPUT_3:
			case ContainerAlloyCalculator.SLOT_INPUT_4:
				return new SlotAlloyCalculator(null, this, slotIndex, 0, 0);
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

	@Override
	public void setInventorySlotContents(int slotIndex, ItemStack is) 
	{
		if (slotIndex < 0 || slotIndex >= storage.length) return;
		
		storage[slotIndex] = is;
		
		if(is != null && is.stackSize > getInventoryStackLimit())
			is.stackSize = getInventoryStackLimit();
		
		if (!worldObj.isRemote) 
			updateGui();
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
		boolean updateBlock = false;
		
		if (updateBlock)
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
