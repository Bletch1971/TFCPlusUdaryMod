package udary.tfcplusudarymod.containerslots;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumSize;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public class SlotAnodisingVesselSolute extends SlotSize 
{
	protected EntityPlayer player;
	
	public SlotAnodisingVesselSolute(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
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
				(is.getItem() == TFCItems.powder || 
				 is.getItem() instanceof com.dunk.tfc.Items.ItemOre || 
				 is.getItem() instanceof udary.tfcplusudarymod.items.ores.ItemOre ||
				 is.getItem() instanceof udary.tfcplusudarymod.items.powders.ItemPowder)); 
	}
}
