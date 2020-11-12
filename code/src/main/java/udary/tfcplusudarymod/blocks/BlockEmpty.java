package udary.tfcplusudarymod.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import udary.tfcplusudarymod.core.ModTextures;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEmpty extends Block
{
	/*
	 * This Block class exists to provide icon register functionality to blocks 
	 * that are unable to receive the registerBlockIcons event.
	 * Also to register icons required outside of the normal block register.
	 */
	
	public BlockEmpty() 
	{
		super(Material.wood);
		
		this.setCreativeTab(null);
	}

    @SideOnly(Side.CLIENT)
    public void registerBlockIcons(IIconRegister iconRegister)
    {
    	ModTextures.registerBlockIcons(iconRegister);
    }
}
