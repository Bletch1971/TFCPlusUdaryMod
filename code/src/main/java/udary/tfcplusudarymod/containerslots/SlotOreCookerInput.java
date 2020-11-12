package udary.tfcplusudarymod.containerslots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.dunk.tfc.api.Enums.EnumSize;

public class SlotOreCookerInput extends SlotSize  
{
	protected EntityPlayer player;
	
	public SlotOreCookerInput(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
		
		this.setSize(EnumSize.HUGE);
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		// allow all ores, but recipes will dictate if the ore is usable.
		return  (is != null && 
				(is.getItem() instanceof com.dunk.tfc.Items.ItemOre || 
				 is.getItem() instanceof udary.tfcplusudarymod.items.ores.ItemOre)); 
	}
}
