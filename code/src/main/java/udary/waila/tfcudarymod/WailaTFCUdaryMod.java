package udary.waila.tfcudarymod;

import net.minecraftforge.common.MinecraftForge;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import cpw.mods.fml.common.event.FMLInterModComms;

public class WailaTFCUdaryMod 
{
	public static final String CONFIG_MODNAME_DEVICES = "Udary Devices";
	
	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering WAILA Classes (Udary)");
		
		registerClasses();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering WAILA Classes (Udary)");
	}
	
	private static void registerClasses()
	{
		// blocks
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.blocks.WAlloyCalculator.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.blocks.WAnodisingVessel.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.blocks.WDryingMat.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.blocks.WEvaporatorPan.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.blocks.WOreCooker.callbackRegister");
		FMLInterModComms.sendMessage(ModDetails.MODID_WAILA, WailaUtils.MESSAGE_KEY_REGISTER, "udary.waila.tfcudarymod.entities.TFCAnimal.callbackRegister");

		if (!ModOptions.loadUdaryModWailaClasses) return;
		
		// tooltips
		MinecraftForge.EVENT_BUS.register(new udary.waila.tfcudarymod.items.WGenericTooltip());
	}
}
