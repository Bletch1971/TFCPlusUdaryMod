package udary.tfcplusudarymod.interfaces;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface ICathode 
{
	public boolean getIsCathode();
	
	public boolean isCathode(ItemStack is);
	
	public Item setIsCathode(boolean isCathode);
}
