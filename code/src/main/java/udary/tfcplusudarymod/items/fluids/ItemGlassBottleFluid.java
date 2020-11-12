package udary.tfcplusudarymod.items.fluids;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

import com.dunk.tfc.Reference;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemGlassBottleFluid extends ItemTerra
{
	protected Fluid fluid = null;
	protected int capacity;
	protected EnumAction action;
	protected EnumItemReach reach;	
	
	public ItemGlassBottleFluid(Fluid fluid)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryFluids);	
		this.setFolder("fluids/");

		this.setMaxStackSize(EnumSize.TINY.stackSize);
		
		this.setSize(EnumSize.TINY);
		this.setWeight(EnumWeight.LIGHT);
		
		this.fluid = fluid;	
		this.stackable = true;
		this.capacity = ModFluids.GlassBottleVolume;
		this.action = EnumAction.none;
		this.reach = EnumItemReach.SHORT;	
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List arraylist) 
	{		
		String capacityString = WailaUtils.getCapacityInformation(getCapacity());
		if (capacityString != "")
			arraylist.add(capacityString);
		
		NBTTagCompound nbt = is.getTagCompound();
		if (nbt != null)
		{
			addFoodInformation(is, player, arraylist, nbt);
			
			if (nbt.hasKey(ModTags.TAG_FOOD_WEIGHT) && nbt.getFloat(ModTags.TAG_FOOD_WEIGHT) != 999)
			{
				if (TFC_Core.showCtrlInformation())
					ItemFoodTFC.addTasteInformation(is, player, arraylist);
				else
					arraylist.add(StatCollector.translateToLocal("gui.showtaste"));				
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addFoodInformation(ItemStack is, EntityPlayer player, List arraylist, NBTTagCompound nbt)
	{
		List<String> foodStrings = WailaUtils.getFoodInformation(is);
		if (foodStrings != null)
			arraylist.addAll(foodStrings);
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon(Reference.MOD_ID + ":Glass Bottle Overlay");		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack is, int pass)
	{
		return pass == 1 ? this.itemIcon : TFCItems.glassBottle.getIcon(is, pass);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack is, int pass)
	{
		if (fluid == null) return super.getColorFromItemStack(is, pass);
		return pass == 1 ? fluid.getColor() : super.getColorFromItemStack(is, pass);
	}

	public int getCapacity()
	{
		return this.capacity;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack is)
	{
		return this.action;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
			--is.stackSize;
		
		if (!world.isRemote)
		{
			updatePlayerFoodStats(is, world, player);
		}

		if (!player.capabilities.isCreativeMode)
		{
			boolean broken = false;
			if(world.rand.nextInt(50) == 0)
			{
				player.playSound("random.glass", 0.7f, player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
				broken = true;
			}

			ItemStack outputIS = new ItemStack(TFCItems.glassBottle);
			outputIS.stackTagCompound = null;

			if (!broken && is.stackSize <= 0)
				return outputIS;

			player.inventory.addItemStackToInventory(outputIS);
		}

		return is;
	}
	
	protected void updatePlayerFoodStats(ItemStack is, World world, EntityPlayer player)
	{
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
