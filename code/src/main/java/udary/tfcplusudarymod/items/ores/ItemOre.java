package udary.tfcplusudarymod.items.ores;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCOptions;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISmeltable;

public class ItemOre extends ItemTerra implements ISmeltable
{
	protected Metal metalType;
	protected EnumTier smeltTier;
	protected boolean isSmeltable;
	
	public ItemOre(String oreName, Metal metalType)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryMaterials);
		this.setFolder("ores/");
		
		this.setMaxDamage(0);
		this.setHasSubtypes(true);
		this.setUnlocalizedName(oreName);
		
		this.setSize(EnumSize.SMALL);
		this.setWeight(EnumWeight.HEAVY);

		this.metalType = metalType;
		this.smeltTier = EnumTier.TierX;
		this.isSmeltable = false;
		
		metaNames = new String[] { oreName+" Small", oreName+" Poor", oreName, oreName+" Rich" };
	}
	
	public ItemOre(String oreName, Metal metalType, EnumTier smeltTier)
	{
		this(oreName, metalType);

		this.smeltTier = smeltTier;
		this.isSmeltable = true;
	}

	@Override
	public IIcon getIconFromDamage(int damage)
	{
		if (damage == TFCOptions.smallOreUnits && metaIcons.length > 0)
			return metaIcons[0];
		if (damage == TFCOptions.poorOreUnits && metaIcons.length > 1)
			return metaIcons[1];
		if (damage == TFCOptions.normalOreUnits && metaIcons.length > 2)
			return metaIcons[2];
		if (damage == TFCOptions.richOreUnits && metaIcons.length > 3)
			return metaIcons[3];
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
	
	@Override
	public EnumSize getSize(ItemStack is)
	{
		if (is.getItemDamage() == TFCOptions.smallOreUnits)
			return EnumSize.TINY;
		return super.getSize(is);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(this, 1, TFCOptions.smallOreUnits));
		list.add(new ItemStack(this, 1, TFCOptions.poorOreUnits));
		list.add(new ItemStack(this, 1, TFCOptions.normalOreUnits));
		list.add(new ItemStack(this, 1, TFCOptions.richOreUnits));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		if (is.getItemDamage() == TFCOptions.smallOreUnits && metaNames.length > 0)
			return "item." + metaNames[0];
		if (is.getItemDamage() == TFCOptions.poorOreUnits && metaNames.length > 1)
			return "item." + metaNames[1];
		if (is.getItemDamage() == TFCOptions.normalOreUnits && metaNames.length > 2)
			return "item." + metaNames[2];
		if (is.getItemDamage() == TFCOptions.richOreUnits && metaNames.length > 3)
			return "item." + metaNames[3];
		return super.getUnlocalizedName(is);
	}

	@Override
	public void registerIcons(IIconRegister register)
	{
		metaIcons = new IIcon[metaNames.length];
		
		for (int i = 0; i < metaNames.length; i++)
		{
			metaIcons[i] = register.registerIcon(ModDetails.ModID + ":" + textureFolder + metaNames[i] + " Ore");
		}
	}

	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List<String> arraylist)
	{
		Metal metal = getMetalType(is);
		
		if (metal != null)
		{
			if (TFC_Core.showShiftInformation())
			{
				String unitsString = WailaUtils.getUnitsInformation(getMetalReturnAmount(is));
				if (unitsString != "")
					arraylist.add(unitsString);
			}
			else
			{
				arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
			}
			
			String metalString = WailaUtils.getMetalInformation(metal, true);
			if (metalString != "")
				arraylist.add(metalString);
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
