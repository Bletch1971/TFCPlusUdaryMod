package udary.tfcplusudarymod.items.powders;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumItemReach;

public class ItemPowder extends ItemTerra 
{
	protected EnumItemReach reach;
	
	public ItemPowder()
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryMaterials);
		this.setFolder("powders/");
		
		this.setMaxDamage(0);
		this.setHasSubtypes(false);
		
		this.reach = EnumItemReach.SHORT;
	}

	@Override
	public void registerIcons(IIconRegister registerer)
	{
		if (this.metaNames == null)
		{
			if (this.iconString != null)
				this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getIconString());
			else
				this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", ""));
		}
		else
		{
			metaIcons = new IIcon[metaNames.length];
			for(int i = 0; i < metaNames.length; i++)
			{
				metaIcons[i] = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + metaNames[i]);
			}
		}		
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
