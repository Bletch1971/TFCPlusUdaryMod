package udary.tfcplusudarymod.handlers;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyCalculator;
import udary.tfcplusudarymod.containers.devices.ContainerAnodisingVessel;
import udary.tfcplusudarymod.containers.devices.ContainerEvaporatorPan;
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.containers.materials.ContainerDryingMat;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

import cpw.mods.fml.common.network.IGuiHandler;

public class GuiHandler implements IGuiHandler
{
	public static final int GuiIdAnodisingVessel = ModDetails.GuiOffset + 1;
	public static final int GuiIdOreCooker = ModDetails.GuiOffset + 2;
	public static final int GuiIdAlloyCalculator = ModDetails.GuiOffset + 3;
	public static final int GuiIdEvaporatorPan = ModDetails.GuiOffset + 4;
	public static final int GuiIdDryingMat = ModDetails.GuiOffset + 5;
	
	@Override
	public Object getServerGuiElement(int Id, EntityPlayer player, World world, int x, int y, int z) 
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
				return new ContainerAnodisingVessel(player.inventory, (TileEntityAnodisingVessel)tileEntity, world, x, y, z);
			case GuiIdOreCooker:
				return new ContainerOreCooker(player.inventory, (TileEntityOreCooker)tileEntity, world, x, y, z);
			case GuiIdAlloyCalculator:
				return new ContainerAlloyCalculator(player.inventory, (TileEntityAlloyCalculator)tileEntity, world, x, y, z);
			case GuiIdEvaporatorPan:
				return new ContainerEvaporatorPan(player.inventory, (TileEntityEvaporatorPan)tileEntity, world, x, y, z);
			case GuiIdDryingMat:
				return new ContainerDryingMat(player.inventory, (TileEntityDryingMat)tileEntity, world, x, y, z);
				
			default:
				return null;
		}
	}

	@Override
	public Object getClientGuiElement(int Id, EntityPlayer player, World world, int x, int y, int z)
	{
		return null;
	}
}
