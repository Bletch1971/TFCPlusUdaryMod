package udary.tfcplusudarymod.containerslots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumSize;

public class SlotAnodisingVesselInput extends SlotSize
{
	protected EntityPlayer player;
	
	public SlotAnodisingVesselInput(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
		
		this.setSize(EnumSize.HUGE);
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		// allow all powders, but recipes will dictate if the powder is usable.
		return  (is != null && 
				(is.getItem() == TFCItems.powder || 
				 is.getItem() instanceof udary.tfcplusudarymod.items.powders.ItemPowder)); 
	}
}
