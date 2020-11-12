package udary.waila.terrafirmacraft;

import net.minecraftforge.common.MinecraftForge;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import cpw.mods.fml.common.event.FMLInterModComms;

public class WailaTerraFirmaCraft 
{
	public static String CONFIG_MODNAME_BLOCKS = "Udary-TFC 1";
	public static String CONFIG_MODNAME_FLORA = "Udary-TFC 2"; 
	public static String CONFIG_MODNAME_DEVICES_1 = "Udary-TFC 3";
	public static String CONFIG_MODNAME_DEVICES_2 = "Udary-TFC 5";
	public static String CONFIG_MODNAME_ANIMALS = "Udary-TFC 4";
	
	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering WAILA Classes (TFC)");
		
		registerClasses();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering WAILA Classes (TFC)");
	}
	
	private static void registerClasses()
	{	
		// blocks
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCAnvil.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCBarrel.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCBerryBush.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCBlastFurnace.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCClay.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCCrucible.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCDirt.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCFarmland.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCFirepit.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCFoodPrep.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCForge.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCFruitLeaves.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCFruitWood.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCGrass.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCGravel.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCGrill.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCHopper.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCIngotPile.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCLogPile.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCPottery.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCQuern.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCSand.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCSapling.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCSmokeRack.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCToolRack.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCTorch.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.blocks.TFCWorldItem.callbackRegister");
		
		// entities
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.entities.TFCAnimal.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.terrafirmacraft.entities.TFCStand.callbackRegister");

		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		// tooltips
		MinecraftForge.EVENT_BUS.register(new udary.waila.terrafirmacraft.items.TFCGenericTooltip());
	}
}
