package udary.tfcplusudarymod.items.devices;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModTabs;

import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISize;

public class ItemAlloyCalculator extends ItemTerraBlock implements ISize
{
	protected boolean canStack;
	protected EnumItemReach reach;
	protected EnumSize size;
	protected EnumWeight weight;
	
	public ItemAlloyCalculator(Block block) 
	{
		super(block);
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		this.setFolder("devices/");
		
		this.setMaxStackSize(1);
		
		this.canStack = false;
		this.reach = EnumItemReach.SHORT;
		this.size = EnumSize.LARGE;
		this.weight = EnumWeight.MEDIUM;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag) 
	{
		ItemTerra.addSizeInformation(is, arraylist);
	}

	@Override
	public boolean canStack()
	{
		return this.canStack;
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	@Override
	public EnumSize getSize(ItemStack is)
	{
		return this.size;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return this.weight;
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
