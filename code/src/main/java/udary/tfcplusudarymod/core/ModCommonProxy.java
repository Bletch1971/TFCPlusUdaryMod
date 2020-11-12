package udary.tfcplusudarymod.core;

import java.io.File;

import net.minecraft.server.MinecraftServer;
import net.minecraft.world.World;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.GameRegistry;

public class ModCommonProxy
{
	public String getCurrentLanguage()
	{
		return null;
	}

	public World getCurrentWorld()
	{
		return MinecraftServer.getServer().getEntityWorld();
	}

	public boolean getGraphicsLevel()
	{
		return false;
	}
	
	public File getMinecraftDirectory()
	{
		return FMLCommonHandler.instance().getMinecraftServerInstance().getFile("");
	}

	public void hideNEIItems()
	{
	}
	
	public boolean isRemote()
	{
		return false;
	}

	public void loadOptions()
	{
		//Load our settings from the Options file
		ModOptions.loadSettings();
	}
	
	public void onClientLogin()
	{
	}

	public void registerFluidIcons()
	{
	}

	public void registerGuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(udary.tfcplusudarymod.TFCUdaryMod.instance, new udary.tfcplusudarymod.handlers.GuiHandler());
	}

	public void registerHandlers()
	{
	}

	public void registerKeys()
	{
	}

	public void registerKeyBindingHandler()
	{
	}

	public void registerRenderInformation()
	{
		// NOOP on server
	}

	public void registerSoundHandler()
	{
	}

	public void registerTickHandler()
	{
		FMLCommonHandler.instance().bus().register(new udary.tfcplusudarymod.handlers.ModServerTickHandler());
	}
	
	public void registerTileEntities(boolean flag)
	{
		// non TESR registers
		if (ModBlocks.OreCooker != null)
			GameRegistry.registerTileEntity(TileEntityOreCooker.class, "S_UdaryOreCooker");

		if (flag)
		{
			// TESR registers
			if (ModBlocks.AlloyCalculator != null)
				GameRegistry.registerTileEntity(TileEntityAlloyCalculator.class, "S_UdaryAlloyCalculator");
			
			if (ModBlocks.AnodisingVessel != null)
				GameRegistry.registerTileEntity(TileEntityAnodisingVessel.class, "S_UdaryAnodisingVessel");
			
			if (ModBlocks.DryingMat != null)
				GameRegistry.registerTileEntity(TileEntityDryingMat.class, "S_UdaryDryingMat");
			
			if (ModBlocks.EvaporatorPan != null)
				GameRegistry.registerTileEntity(TileEntityEvaporatorPan.class, "S_UdaryEvaporatorPan");
		}
	}

	public void registerToolClasses()
	{
	}

	public void registerWailaClasses()
	{
		WailaTerraFirmaCraft.initialise();
		WailaTFCUdaryMod.initialise();
	}

	public void uploadKeyBindingsToGame()
	{
	}
}
