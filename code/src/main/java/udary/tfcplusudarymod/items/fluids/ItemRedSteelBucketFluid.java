package udary.tfcplusudarymod.items.fluids;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModTabs;
import udary.waila.WailaUtils;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemRedSteelBucketFluid extends ItemTerra
{
    @SideOnly(Side.CLIENT)
    protected IIcon containerIcon;
	protected Fluid fluid = null;
	protected int capacity;
	protected EnumAction action;
	protected EnumItemReach reach;
	
	public ItemRedSteelBucketFluid(Fluid fluid)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryFluids);	
		this.setFolder("fluids/");
		
		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.LARGE);
		this.setWeight(EnumWeight.LIGHT);
		
		this.fluid = fluid;
		this.stackable = false;
		this.capacity = ModFluids.RedSteelBucketVolume;
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
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registerer)
	{
		this.containerIcon = registerer.registerIcon(ModDetails.ModID + ":Red Steel Bucket");
		this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":Red Steel Bucket Overlay");		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack is, int pass)
	{
		return pass == 1 ? this.itemIcon : this.containerIcon;
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
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
