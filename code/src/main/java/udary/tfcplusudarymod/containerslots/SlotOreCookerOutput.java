package udary.tfcplusudarymod.containerslots;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotOreCookerOutput extends Slot  
{
	protected EntityPlayer player;
	
	public SlotOreCookerOutput(EntityPlayer player, IInventory inventory, int slotIndex, int xDisplayPosition, int yDisplayPosition)
	{
		super(inventory, slotIndex, xDisplayPosition, yDisplayPosition);
		
		this.player = player;
	}

	@Override
	public boolean isItemValid(ItemStack is)
	{
		return false; 
	}
}
