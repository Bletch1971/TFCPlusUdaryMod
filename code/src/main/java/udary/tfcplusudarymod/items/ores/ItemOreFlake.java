package udary.tfcplusudarymod.items.ores;

import java.util.List;

import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModTabs;

import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCOptions;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISmeltable;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class ItemOreFlake extends ItemOre implements ISmeltable
{
	protected Metal metalType;
	protected EnumTier smeltTier;
	protected boolean isSmeltable;
	
	public ItemOreFlake(String flakeName, Metal metalType)
	{
		super(flakeName, metalType);
		
		this.setCreativeTab(ModTabs.UdaryMaterials);
		this.setFolder("ores/");
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(flakeName);
		
		this.setSize(EnumSize.TINY);
		this.setWeight(EnumWeight.LIGHT);

		this.metalType = metalType;
		this.smeltTier = EnumTier.TierX;
		this.isSmeltable = false;
		
		metaNames = new String[] { flakeName, flakeName+" Clump" };
	}

	public ItemOreFlake(String flakeName, Metal metalType, EnumTier smeltTier)
	{
		this(flakeName, metalType);
		
		this.smeltTier = smeltTier;
		this.isSmeltable = true;
	}

	@Override
	public IIcon getIconFromDamage(int damage)
	{
		if (damage == ModGlobal.tinyOreUnits && metaIcons.length > 0)
			return metaIcons[0];
		if (damage == TFCOptions.smallOreUnits && metaIcons.length > 1)
			return metaIcons[1];
		return this.itemIcon;
	}

	@Override
	public int getItemStackLimit(ItemStack is)
	{
		if (canStack())
			return getSize(is).stackSize;
		else
			return 1;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(this, 1, ModGlobal.tinyOreUnits));
		list.add(new ItemStack(this, 1, TFCOptions.smallOreUnits));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		if (is.getItemDamage() == ModGlobal.tinyOreUnits && metaNames.length > 0)
			return "item." + metaNames[0];
		if (is.getItemDamage() == TFCOptions.smallOreUnits && metaNames.length > 1)
			return "item." + metaNames[1];
		return super.getUnlocalizedName(is);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		metaIcons = new IIcon[metaNames.length];
		
		for (int i = 0; i < metaNames.length; i++)
		{
			metaIcons[i] = register.registerIcon(ModDetails.ModID + ":" + textureFolder + metaNames[i]);
		}
	}

	@Override
	public Metal getMetalType(ItemStack is)
	{
		return this.metalType;
	}

	@Override
	public short getMetalReturnAmount(ItemStack is)
	{
		return (short)is.getItemDamage();
	}

	@Override
	public boolean isSmeltable(ItemStack is)
	{
		return this.isSmeltable;
	}

	@Override
	public EnumTier getSmeltTier(ItemStack is)
	{
		return this.smeltTier;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
