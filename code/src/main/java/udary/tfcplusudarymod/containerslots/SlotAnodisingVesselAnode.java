package udary.tfcplusudarymod.containerslots;

import com.dunk.tfc.api.Enums.EnumSize;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import udary.tfcplusudarymod.interfaces.IAnode;

public class SlotAnodisingVesselAnode extends SlotSize
{
	protected EntityPlayer player;
	
	public SlotAnodisingVesselAnode(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
		
		this.setSize(EnumSize.HUGE);
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return (is != null && 
				is.getItem() instanceof IAnode && ((IAnode)is.getItem()).isAnode(is));
	}
}
