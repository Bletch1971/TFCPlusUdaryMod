package udary.tfcplusudarymod.items.pottery;

import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemPotteryMold extends com.dunk.tfc.Items.Pottery.ItemPotteryMold
{
	public ItemPotteryMold()
	{
		super();

		this.setFolder("pottery/");
		setCreativeTab(ModTabs.UdaryPottery);
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
	public IIcon getIconFromDamage(int damage)
	{
		if (damage >= metaIcons.length)
			damage = ((damage - 2) % 4) + 2;
		
		if (damage < 0 || damage >= metaIcons.length)
			return this.clayIcon;

		return metaIcons[damage];
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
