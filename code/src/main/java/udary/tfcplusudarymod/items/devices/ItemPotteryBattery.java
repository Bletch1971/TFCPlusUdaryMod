package udary.tfcplusudarymod.items.devices;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.interfaces.IBattery;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.Pottery.ItemPotteryBase;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemPotteryBattery extends ItemPotteryBase implements IBattery
{
	public static final int TICKS_PER_CHARGE_LEVEL = 1000;
	
	protected String batteryType;
	protected EnumItemReach reach;
	
	public ItemPotteryBattery(int maxCharge)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		this.setFolder("devices/");
		
		this.setMaxCharge(maxCharge);
		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.MEDIUM);
		this.setWeight(EnumWeight.MEDIUM);
		
		this.hasSubtypes = false;
		this.metaNames = new String[0];
		
		this.stackable = false;
		this.batteryType = ModGlobal.BATTERY_TYPE_CERAMIC;
		this.reach = EnumItemReach.SHORT;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List arraylist) 
	{		
		super.addExtraInformation(is, player, arraylist);

		String chargeString = WailaUtils.getBatteryChargeInformation(is, TFC_Core.showCtrlInformation());
		if (chargeString != "")
			arraylist.add(chargeString);
	}
	
	@Override
	public void decreaseCharge(ItemStack is, int chargeUnits)
	{
		if (is == null || is.stackSize == 0) return;

		// to decrease the charge level of the battery, we must increase the damage level of the item stack.
		int maxChargeUnits = is.getMaxDamage();
		int curChargeUnits = is.getItemDamage();
		int newChargeUnits = Math.min(maxChargeUnits, curChargeUnits + chargeUnits);
		is.setItemDamage(Math.max(0, newChargeUnits));		
	}

	@Override
	public String getBatteryType() 
	{
		return this.batteryType;
	}	
	
	@Override
	public int getCurrentCharge(ItemStack is)
	{
		if (is == null || is.stackSize == 0) return 0;

		int maxChargeUnits = is.getMaxDamage();
		int curChargeUnits = is.getItemDamage();
		return Math.max(0, (int)((maxChargeUnits - curChargeUnits) / TICKS_PER_CHARGE_LEVEL));
	}
	
	@Override
	public int getMaxCharge(ItemStack is)
	{
		if (is == null || is.stackSize == 0) return 0;

		int maxChargeUnits = is.getMaxDamage();
		return (int)(maxChargeUnits / TICKS_PER_CHARGE_LEVEL);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int damage)
	{
		return this.ceramicIcon;
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	@SideOnly(Side.CLIENT)
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(new ItemStack(item, 1, 0));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		return this.getUnlocalizedName();
	}

	@Override
	public boolean hasEnoughCharge(ItemStack is, int chargeUnits)
	{
		if (is == null || is.stackSize == 0) return false;
		
		int maxChargeUnits = is.getMaxDamage();
		int curChargeUnits = is.getItemDamage();
		int newChargeUnits = curChargeUnits + chargeUnits;
		
		return newChargeUnits <= maxChargeUnits;
	}
	
	@Override
	public void increaseCharge(ItemStack is, int chargeUnits)
	{
		if (is == null || is.stackSize == 0) return;
		
		// to increase the charge level of the battery, we must decrease the damage level of the item stack.
		int maxChargeUnits = is.getMaxDamage();
		int curChargeUnits = is.getItemDamage();
		int newChargeUnits = Math.max(0, curChargeUnits - chargeUnits);
		is.setItemDamage(Math.min(maxChargeUnits, newChargeUnits));	
	}
	
	@Override
	public boolean onUpdate(ItemStack is, World world, int x, int y, int z)
	{
		return false;
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		if (this.iconString != null)
		{
			this.clayIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getIconString());
			this.ceramicIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getIconString());
		}
		else
		{
			this.clayIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", ""));
			this.ceramicIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", ""));
		}
	}
	
	@Override
	public void setCurrentCharge(ItemStack is, int charge)
	{
		if (is == null || is.stackSize == 0) return;

		int maxChargeUnits = is.getMaxDamage();
		int newChargeUnits = maxChargeUnits - (charge * TICKS_PER_CHARGE_LEVEL);
		is.setItemDamage(Math.max(0, newChargeUnits));
	}
	
	@Override
	public Item setMaxCharge(int maxCharge)
	{
		this.setMaxDamage(maxCharge * TICKS_PER_CHARGE_LEVEL);
		return this;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
