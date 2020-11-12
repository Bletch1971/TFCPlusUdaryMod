package udary.tfcplusudarymod.interfaces;

import com.dunk.tfc.api.Metal;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IAnode 
{
	public Metal getAnodisedMetal();
	
	public boolean getIsAnode();
	
	public boolean isAnode(ItemStack is);
	
	public Item setAnodisedMetal(Metal metal);
	
	public Item setIsAnode(boolean isAnode);
}
