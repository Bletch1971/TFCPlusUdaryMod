package udary.tfcplusudarymod.containerslots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.interfaces.IBattery;

import com.dunk.tfc.api.Enums.EnumSize;

public class SlotAnodisingVesselBattery extends SlotSize 
{
	protected EntityPlayer player;
	
	public SlotAnodisingVesselBattery(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
		
		this.setSize(EnumSize.HUGE);
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return (is != null && 
				is.getItem() instanceof IBattery && ((IBattery)is.getItem()).getBatteryType() == ModGlobal.BATTERY_TYPE_CERAMIC);
	}
}
