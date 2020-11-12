package udary.tfcplusudarymod.containerslots;

import java.util.ArrayList;
import java.util.List;

import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Interfaces.ISize;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class SlotSize extends Slot
{
	protected List<Item> exclusions = new ArrayList<Item>();
	protected List<Item> inclusions = new ArrayList<Item>();
	protected EnumSize size;

	public SlotSize(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		setSize(EnumSize.MEDIUM);
	}

	public SlotSize addItemExclusion(Item... items)
	{
		for(int i = 0; i < items.length; i++)
		{
			this.exclusions.add(items[i]);
		}
		
		return this;
	}

	public SlotSize addItemInclusion(Item... items)
	{
		for(int i = 0; i < items.length; i++)
		{
			this.inclusions.add(items[i]);
		}
		
		return this;
	}

	public EnumSize getSize()
	{
		return this.size;
	}
	
	public SlotSize setSize(EnumSize size)
	{
		this.size = size;
		return this;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		if (is == null)
			return false;
		
		boolean exclude = this.exclusions.contains(is.getItem());
		boolean include = this.inclusions.contains(is.getItem()) || this.inclusions.size() == 0;
		
		if (exclude || !include)
			return false;
		
		if (!(is.getItem() instanceof ISize))
			return true;
		
		if (((ISize)is.getItem()).getSize(is).stackSize >= this.size.stackSize)
			return true;
		
		return false;
	}
}
