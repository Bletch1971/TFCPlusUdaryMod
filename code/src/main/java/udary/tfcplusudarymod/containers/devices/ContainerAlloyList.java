package udary.tfcplusudarymod.containers.devices;

import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import com.dunk.tfc.Containers.ContainerTFC;

public class ContainerAlloyList extends ContainerTFC
{
	public static final int BUTTON_CALCULATOR = 0;
	public static final int BUTTON_LIST = 1;
	
	protected EntityPlayer player;
	
	public ContainerAlloyList(InventoryPlayer inventoryPlayer, TileEntityAlloyCalculator tileEntity, World world, int x, int y, int z) 
	{
		this.player = inventoryPlayer.player;
	}

	@Override
	public void putStackInSlot(int slotNumber, ItemStack itemStack)
	{
		this.player.inventoryContainer.getSlot(slotNumber).putStack(itemStack);
	}
}
