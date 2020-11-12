package udary.tfcplusudarymod.handlers.client;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;

public class KeyBindingHandler
{	
//	public static KeyBinding Key_DebugMode = new KeyBinding("key.DebugMode", Keyboard.KEY_U, ModDetails.ModName);
	
	@SubscribeEvent
	public void onKeyInput(InputEvent.KeyInputEvent e)
	{		
//		if (Key_DebugMode.isPressed())
//		{
//			if (FMLClientHandler.instance().getClient().inGameHasFocus && FMLClientHandler.instance().getClient().currentScreen == null)
//				ModOptions.enableDebugMode = !ModOptions.enableDebugMode;
//		}
	}
}
