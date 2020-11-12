package udary.tfcplusudarymod.core;

import java.io.File;
import java.util.Iterator;

import com.dunk.tfc.Items.ItemBloom;
import com.dunk.tfc.Items.ItemIngot;
import com.dunk.tfc.Items.ItemMeltedMetal;
import com.dunk.tfc.Items.ItemMetalSheet;
import com.dunk.tfc.Items.ItemUnfinishedArmor;
import com.dunk.tfc.Items.ItemBlocks.ItemSoil;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.MinecraftForge;
import udary.tfcplusudarymod.core.managers.RenderManager;
import udary.tfcplusudarymod.render.blocks.devices.RenderAlloyCalculator;
import udary.tfcplusudarymod.render.blocks.devices.RenderAnodisingVessel;
import udary.tfcplusudarymod.render.blocks.devices.RenderEvaporatorPan;
import udary.tfcplusudarymod.render.blocks.devices.RenderOreCooker;
import udary.tfcplusudarymod.render.blocks.materials.RenderDryingMat;
import udary.tfcplusudarymod.render.item.Item2DHeatRenderer;
import udary.tfcplusudarymod.render.item.Item3DHeatRenderer;
import udary.tfcplusudarymod.render.tesr.devices.TesrAlloyCalculator;
import udary.tfcplusudarymod.render.tesr.devices.TesrAnodisingVessel;
import udary.tfcplusudarymod.render.tesr.devices.TesrEvaporatorPan;
import udary.tfcplusudarymod.render.tesr.devices.TesrOreCooker;
import udary.tfcplusudarymod.render.tesr.materials.TesrDryingMat;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

public class ModClientProxy extends ModCommonProxy
{
	@Override
	public String getCurrentLanguage()
	{
		return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
	}

	@Override
	public World getCurrentWorld()
	{
		return Minecraft.getMinecraft().theWorld;
	}

	@Override
	public boolean getGraphicsLevel()
	{
		Minecraft.getMinecraft();
		return Minecraft.isFancyGraphicsEnabled();
	}

	@Override
	public File getMinecraftDirectory()
	{
		return Minecraft.getMinecraft().mcDataDir;
	}
	
	@Override
	public void hideNEIItems()
	{
		if (Loader.isModLoaded(ModDetails.MODID_NEI))
		{
			if (ModBlocks.EmptyBlock != null)
				codechicken.nei.api.API.hideItem(new ItemStack(ModBlocks.EmptyBlock));
			
			if (ModItems.EmptyItem != null)
				codechicken.nei.api.API.hideItem(new ItemStack(ModItems.EmptyItem));
			
			if (ModItems.CopperRodNickelCoated != null)
				codechicken.nei.api.API.hideItem(new ItemStack(ModItems.CopperRodNickelCoated));
			
			if (ModItems.CopperRodSilverCoated != null)
				codechicken.nei.api.API.hideItem(new ItemStack(ModItems.CopperRodSilverCoated));
		}
	}

	@Override
	public boolean isRemote()
	{
		return true;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void loadOptions()
	{
		//Load our settings from the server
		ModOptions.loadSettings();
	}

	@Override
	public void registerGuiHandler()
	{
		NetworkRegistry.INSTANCE.registerGuiHandler(udary.tfcplusudarymod.TFCUdaryMod.instance, new udary.tfcplusudarymod.handlers.client.GuiHandler());
	}

	@Override
	public void registerHandlers()
	{
		// register new handlers
		MinecraftForge.EVENT_BUS.register(new udary.tfcplusudarymod.handlers.client.FarmlandHighlightHandler());
		
		MinecraftForge.EVENT_BUS.register(new udary.tfcplusudarymod.handlers.client.PlayerRenderHandler());
	}

	@Override
	public void registerKeys()
	{
		//ModKeyBindings.addKeyBinding(KeyBindingHandler.Key_DebugMode, false);

		uploadKeyBindingsToGame();
	}

	@Override
	public void registerKeyBindingHandler()
	{
		KeyBinding[] keyBindings = ModKeyBindings.gatherKeyBindings();
		if (keyBindings == null || keyBindings.length == 0)
			return;

		FMLCommonHandler.instance().bus().register(new udary.tfcplusudarymod.handlers.client.KeyBindingHandler());
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerRenderInformation()
	{
		if (ModBlocks.AlloyCalculator != null)
			RenderingRegistry.registerBlockHandler(ModBlocks.AlloyCalculatorRenderId, new RenderAlloyCalculator());

		if (ModBlocks.AnodisingVessel != null)
			RenderingRegistry.registerBlockHandler(ModBlocks.AnodisingVesselRenderId, new RenderAnodisingVessel());

		if (ModBlocks.DryingMat != null)
			RenderingRegistry.registerBlockHandler(ModBlocks.DryingMatRenderId, new RenderDryingMat());

		if (ModBlocks.EvaporatorPan != null)
			RenderingRegistry.registerBlockHandler(ModBlocks.EvaporatorPanRenderId, new RenderEvaporatorPan());

		if (ModBlocks.OreCooker != null)
			RenderingRegistry.registerBlockHandler(ModBlocks.OreCookerRenderId, new RenderOreCooker());

		// apply the heat bar renderer
		FMLControlledNamespacedRegistry<Item> itemList = GameData.getItemRegistry();
		for (Iterator<Item> i = itemList.iterator(); i.hasNext();)
		{
			Item item = i.next();
			
			if (item instanceof ItemBloom ||
				item instanceof ItemIngot ||
				item instanceof ItemMeltedMetal ||
				item instanceof ItemMetalSheet ||
				item instanceof ItemUnfinishedArmor)
			{
				MinecraftForgeClient.registerItemRenderer(item, new Item2DHeatRenderer());
			}

			if (item instanceof ItemSoil)
			{
				MinecraftForgeClient.registerItemRenderer(item, new Item3DHeatRenderer());
			}
		}
		
		// Initialize all the player item renders
		RenderManager.initialise();
	}
	
	@Override
	public void registerTileEntities(boolean flag)
	{
		super.registerTileEntities(false);
		
		// TESR registers
		if (ModBlocks.AlloyCalculator != null)
			ClientRegistry.registerTileEntity(TileEntityAlloyCalculator.class, "C_UdaryAlloyCalculator", new TesrAlloyCalculator());
		
		if (ModBlocks.AnodisingVessel != null)
			ClientRegistry.registerTileEntity(TileEntityAnodisingVessel.class, "C_UdaryAnodisingVessel", new TesrAnodisingVessel());
		
		if (ModBlocks.DryingMat != null)
			ClientRegistry.registerTileEntity(TileEntityDryingMat.class, "C_UdaryDryingMat", new TesrDryingMat());
		
		if (ModBlocks.EvaporatorPan != null)
			ClientRegistry.registerTileEntity(TileEntityEvaporatorPan.class, "C_UdaryEvaporatorPan", new TesrEvaporatorPan());
		
		if (ModBlocks.OreCooker != null)
			ClientRegistry.registerTileEntity(TileEntityOreCooker.class, "C_UdaryOreCooker", new TesrOreCooker());
	}

	@Override
	public void uploadKeyBindingsToGame()
	{
		KeyBinding[] keyBindings = ModKeyBindings.gatherKeyBindings();
		if (keyBindings == null || keyBindings.length == 0)
			return;

		GameSettings settings = Minecraft.getMinecraft().gameSettings;
		KeyBinding[] allKeys = new KeyBinding[settings.keyBindings.length + keyBindings.length];
		System.arraycopy(settings.keyBindings, 0, allKeys, 0, settings.keyBindings.length);
		System.arraycopy(keyBindings, 0, allKeys, settings.keyBindings.length, keyBindings.length);
		
		settings.keyBindings = allKeys;
		settings.loadOptions();
	}
}
