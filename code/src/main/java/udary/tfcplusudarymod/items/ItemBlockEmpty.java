package udary.tfcplusudarymod.items;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;

public class ItemBlockEmpty extends ItemBlock
{
	public ItemBlockEmpty(Block block) 
	{
		super(block);
		
		this.setCreativeTab(null);	
	}
}
