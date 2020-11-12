package udary.tfcplusudarymod.core;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.util.IIcon;

public class ModTextures 
{
	public static IIcon GuiAlloyCalculator;
	public static IIcon GuiAlloyList;
	
	public static void registerBlockIcons(IIconRegister iconRegister)
	{
		if (iconRegister == null)
			return;
		
		GuiAlloyCalculator = iconRegister.registerIcon(ModDetails.ModID + ":" + "button_alloycalc");
		GuiAlloyList = iconRegister.registerIcon(ModDetails.ModID + ":" + "button_alloylist");
	}

	public static void registerItemIcons(IIconRegister iconRegister)
	{
		if (iconRegister == null)
			return;
	}
}
