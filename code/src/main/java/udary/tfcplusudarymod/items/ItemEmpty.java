package udary.tfcplusudarymod.items;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.Item;
import udary.tfcplusudarymod.core.ModTextures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemEmpty extends Item
{
	/*
	 * This Item class exists to provide icon register functionality to items 
	 * that are unable to receive the registerIcons event.
	 * Also to register icons required outside of the normal item register.
	 */
	
	public ItemEmpty()
	{
		super();
		
		this.setCreativeTab(null);		
	}
	
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister)
    {
        ModTextures.registerItemIcons(iconRegister);
    }
}
