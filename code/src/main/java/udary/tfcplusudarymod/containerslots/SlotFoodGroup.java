package udary.tfcplusudarymod.containerslots;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IFood;

public class SlotFoodGroup extends SlotSize
{
	protected List<EnumFoodGroup> exclusions = new ArrayList<EnumFoodGroup>();
	protected List<EnumFoodGroup> inclusions = new ArrayList<EnumFoodGroup>();
	protected float maximumWeight;
	
	public SlotFoodGroup(IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		setMaximumWeight(Global.FOOD_MAX_WEIGHT);
	}

	public SlotFoodGroup addFoodGroupExclusion(EnumFoodGroup... foodGroups)
	{
		for(int i = 0; i < foodGroups.length; i++)
		{
			exclusions.add(foodGroups[i]);
		}
		
		return this;
	}
	
	public SlotFoodGroup addFoodGroupInclusion(EnumFoodGroup... foodGroups)
	{
		for(int i = 0; i < foodGroups.length; i++)
		{
			inclusions.add(foodGroups[i]);
		}
		
		return this;
	}

	public float getMaximumWeight()
	{
		return this.maximumWeight;
	}
	
	public SlotFoodGroup setMaximumWeight(float maximumWeight)
	{
		this.maximumWeight = Math.max(Math.min(maximumWeight, Global.FOOD_MAX_WEIGHT), 0);
		return this;
	}
	
	@Override
	public boolean isItemValid(ItemStack is)
	{
		if (is != null && is.getItem() instanceof IFood)
		{
			EnumFoodGroup foodGroup = ((IFood)is.getItem()).getFoodGroup();
			if(foodGroup == null)
				return false;
			
			boolean exclude = exclusions.contains(foodGroup);
			boolean include = inclusions.contains(foodGroup) || inclusions.size() == 0;
			
			if (exclude || !include)
				return false;
			
			if (Food.getWeight(is) <= this.maximumWeight)
				return true;
			
			return false;
		}
		
		return super.isItemValid(is);
	}
}
