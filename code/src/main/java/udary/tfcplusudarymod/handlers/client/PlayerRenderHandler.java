package udary.tfcplusudarymod.handlers.client;

import udary.tfcplusudarymod.core.managers.RenderManager;
import udary.tfcplusudarymod.interfaces.IRender;
import com.dunk.tfc.Core.Player.InventoryPlayerTFC;

import net.minecraft.item.ItemStack;
import net.minecraftforge.client.event.RenderPlayerEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class PlayerRenderHandler 
{	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onPlayerRenderTick(RenderPlayerEvent.Specials.Pre e)
	{
		if (e.entityPlayer != null && e.entityPlayer.inventory instanceof InventoryPlayerTFC)
		{
			RenderManager manager = RenderManager.getInstance();

			ItemStack[] equipables = ((InventoryPlayerTFC)e.entityPlayer.inventory).extraEquipInventory;
			for (ItemStack is : equipables)
			{
				if (is != null && manager.getRender(is.getItem()) != null)
				{
					IRender renderer = manager.getRender(is.getItem());
					renderer.render(e.entityPlayer, is);
				}
			}
		}
	}
}
