package udary.tfcplusudarymod.handlers.client;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import udary.tfcplusudarymod.gui.devices.GuiAlloyCalculator;
import udary.tfcplusudarymod.gui.devices.GuiAnodisingVessel;
import udary.tfcplusudarymod.gui.devices.GuiEvaporatorPan;
import udary.tfcplusudarymod.gui.devices.GuiOreCooker;
import udary.tfcplusudarymod.gui.materials.GuiDryingMat;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

public class GuiHandler extends udary.tfcplusudarymod.handlers.GuiHandler
{
	@Override
	public Object getClientGuiElement(int Id, EntityPlayer player, World world, int x, int y, int z) 
	{
		TileEntity tileEntity;
		
		try
		{
			tileEntity = world.getTileEntity(x, y, z);
		}
		catch(Exception e)
		{
			return null;
		}

		switch (Id)
		{
			case GuiIdAnodisingVessel:
				return new GuiAnodisingVessel(player.inventory, (TileEntityAnodisingVessel)tileEntity, world, x, y, z);
			case GuiIdOreCooker:
				return new GuiOreCooker(player.inventory, (TileEntityOreCooker)tileEntity, world, x, y, z);
			case GuiIdAlloyCalculator:
				return new GuiAlloyCalculator(player.inventory, (TileEntityAlloyCalculator)tileEntity, world, x, y, z);
			case GuiIdEvaporatorPan:
				return new GuiEvaporatorPan(player.inventory, (TileEntityEvaporatorPan)tileEntity, world, x, y, z);
			case GuiIdDryingMat:
				return new GuiDryingMat(player.inventory, (TileEntityDryingMat)tileEntity, world, x, y, z);
				
			default:
				return null;
		}
	}
}
