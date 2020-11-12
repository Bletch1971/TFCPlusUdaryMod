package udary.tfcplusudarymod.containerslots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import udary.tfcplusudarymod.interfaces.ICathode;

import com.dunk.tfc.api.Enums.EnumSize;

public class SlotAnodisingVesselCathode extends SlotSize 
{
	protected EntityPlayer player;
	
	public SlotAnodisingVesselCathode(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
		
		this.setSize(EnumSize.HUGE);
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return (is != null && 
				is.getItem() instanceof ICathode && ((ICathode)is.getItem()).isCathode(is)); 
	}
}
