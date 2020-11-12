package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.dunk.tfc.Blocks.Terrain.BlockGravel;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCGravel implements IWailaDataProvider 
{
	private static String showStone = "udary.terrafirmacraft.gravel.stone";
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof BlockGravel)
		{
			BlockGravel block = (BlockGravel)accessor.getBlock();

			if (config.getConfig(showStone))
			{
				int damage = itemStack != null ? itemStack.getItemDamage() : 0;
				
				if (block == TFCBlocks.gravel2)
					damage += 16;
	
				if (damage < Global.STONE_ALL.length)
					currenttip.add(Global.STONE_ALL[damage]);
				else
					currenttip.add(StatCollector.translateToLocal("tooltip.unknown"));
			}
		}
		return currenttip;
	}

	@Override
	public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		return currenttip;
	}

	@Override
	public NBTTagCompound getNBTData(EntityPlayerMP player, TileEntity te, NBTTagCompound tag, World world, int x, int y, int z) 
	{
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCGravel dataProvider = new TFCGravel();
		
		reg.registerBodyProvider(dataProvider, BlockGravel.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showStone, false);
	}
}
