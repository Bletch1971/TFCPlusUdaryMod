package udary.tfcplusudarymod.core;

import java.lang.reflect.Field;
import java.util.HashMap;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ModGlobal 
{
	// Battery Types
	public static final String BATTERY_TYPE_CERAMIC = "C";
	
	// ore sizes
	public static int tinyOreUnits = 1;
	
	
	
	@SuppressWarnings({ "rawtypes" })
	public static boolean deregisterTileEntity(Class<? extends TileEntity> tileEntityClass, String id)
	{
		String FIELD_CLASSTONAMEMAP = "classToNameMap";
		String FIELD_NAMETOCLASSMAP = "nameToClassMap";
		
		try 
		{
			Field classToNameMapField = TileEntity.class.getDeclaredField(FIELD_CLASSTONAMEMAP);
			Field nameToClassMapField = TileEntity.class.getDeclaredField(FIELD_NAMETOCLASSMAP);
			
			classToNameMapField.setAccessible(true);
			nameToClassMapField.setAccessible(true);
			
			HashMap classToNameMap = (HashMap)classToNameMapField.get(null);
			HashMap nameToClassMap = (HashMap)nameToClassMapField.get(null);
			
			if (classToNameMap == null || nameToClassMap == null)
				return false;
			
			classToNameMap.remove(tileEntityClass);
			nameToClassMap.remove(id);
			
			return true;
		} 
		catch (Exception e) 
		{
			return false;
		}
	}
	
	public static boolean mergeItemStack(IInventory inventory, Slot slot, ItemStack is)
	{
		if (inventory == null || slot == null || is == null)
			return false;
		
		boolean merged = false;

		int slotIndex = slot.getSlotIndex(); 
		ItemStack inventoryIS = inventory.getStackInSlot(slotIndex);

		if (is.isStackable())
		{
			if (inventoryIS != null && 
				inventoryIS.getItem() == is.getItem() && 
				is.getItemDamage() == inventoryIS.getItemDamage() && 
				ItemStack.areItemStackTagsEqual(is, inventoryIS) && 
				inventoryIS.stackSize < slot.getSlotStackLimit())
			{
				int mergedStackSize = is.stackSize + getSmaller(inventoryIS.stackSize, slot.getSlotStackLimit());

				// check if we can add the two stacks together and the resulting stack is smaller than the maximum size for the slot or the stack.
				if (mergedStackSize <= is.getMaxStackSize() && mergedStackSize <= slot.getSlotStackLimit())
				{
					is.stackSize = 0;
					inventoryIS.stackSize = mergedStackSize;
					
					merged = true;
				}
				else if (inventoryIS.stackSize < is.getMaxStackSize())
				{
					is.stackSize -= is.getMaxStackSize() - inventoryIS.stackSize;
					inventoryIS.stackSize = is.getMaxStackSize();
					
					merged = true;
				}
			}			
		}

		if (is.stackSize > 0)
		{
			if (inventoryIS == null && slot.isItemValid(is) && slot.getSlotStackLimit() < is.stackSize)
			{
				ItemStack resultIS = is.copy();
				resultIS.stackSize = slot.getSlotStackLimit();
				
				inventory.setInventorySlotContents(slotIndex, resultIS);
				
				is.stackSize -= resultIS.stackSize;
				
				merged = true;
			}
			else if (inventoryIS == null && slot.isItemValid(is))
			{
				ItemStack resultIS = is.copy();
				
				inventory.setInventorySlotContents(slotIndex, resultIS);
				
				is.stackSize = 0;
				
				merged = true;
			}			
		}

		return merged;
	}

	public static int getSmaller(int value1, int value2)
	{
		return (value1 < value2 ? value1 : value2);
	}
}
