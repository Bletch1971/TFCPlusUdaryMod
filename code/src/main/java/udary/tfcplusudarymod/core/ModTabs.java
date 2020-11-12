package udary.tfcplusudarymod.core;

import com.dunk.tfc.api.TFCItems;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ModTabs extends CreativeTabs
{
	//public static CreativeTabs UdaryBuilding = new ModTabs("UdaryBuilding");
	public static CreativeTabs UdaryDevices = new ModTabs("UdaryDevices");
	public static CreativeTabs UdaryTools = new ModTabs("UdaryTools");
	//public static CreativeTabs UdaryWeapons = new ModTabs("UdaryWeapons");
	//public static CreativeTabs UdaryArmor = new ModTabs("UdaryArmor");
	public static CreativeTabs UdaryMaterials = new ModTabs("UdaryMaterials");
	public static CreativeTabs UdaryPottery = new ModTabs("UdaryPottery");
	//public static CreativeTabs UdaryFoods = new ModTabs("UdaryFoods");
	//public static CreativeTabs UdaryDecoration = new ModTabs("UdaryDecoration");
	public static CreativeTabs UdaryFluids = new ModTabs("UdaryFluids");
	//public static CreativeTabs UdaryMisc = new ModTabs("UdaryMisc");

	private ItemStack itemStack;

	public ModTabs(String lable)
	{
		super(lable);
	}

	@Override
	public ItemStack getIconItemStack()
	{
		return itemStack;
	}
	
	@SideOnly(Side.CLIENT)
	@Override
	public Item getTabIconItem()
	{
		return itemStack.getItem();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public String getTranslatedTabLabel()
	{
		return StatCollector.translateToLocal("itemGroup." + this.getTabLabel());
	}	

	public void setTabIconItem(Item item)
	{
		itemStack = new ItemStack(item);
	}

	public void setTabIconItemStack(ItemStack itemStack)
	{
		this.itemStack = itemStack;
	}
	
	public static void initialise()
	{
		((ModTabs)UdaryDevices).setTabIconItem(ModItems.CopperWire);
		//((ModTabs)UdaryFoods).setTabIconItem(Items.apple);
		((ModTabs)UdaryMaterials).setTabIconItem(TFCItems.steelIngot);
		//((ModTabs)UdaryMisc).setTabIconItem(TFCItems.RedSteelBucketEmpty);
		((ModTabs)UdaryPottery).setTabIconItemStack(new ItemStack(TFCItems.potteryJug, 1, 1));
		((ModTabs)UdaryFluids).setTabIconItem(ModItems.BottledWater);
		((ModTabs)UdaryTools).setTabIconItem(TFCItems.steelPick);
	}
}
