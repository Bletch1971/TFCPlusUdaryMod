package udary.tfcplusudarymod.handlers;

import java.util.HashMap;

import cpw.mods.fml.common.IFuelHandler;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class FuelHandler implements IFuelHandler
{
	private static HashMap<Item, Integer> fuelItems = new HashMap<Item, Integer>();

	public static void registerFuel(Item item, int burnTime)
	{
		if (!fuelItems.containsKey(item))
			fuelItems.put(item, burnTime);
	}

	@Override
	public int getBurnTime(ItemStack is)
	{
		Item item = is.getItem();
		if (!fuelItems.containsKey(item))
			return 0;
		
		Integer burnTime = fuelItems.get(item);
		return (burnTime != null ? burnTime : 0); 
	}
}
