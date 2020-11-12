package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Blocks.Flora.BlockBerryBush;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.FloraIndex;
import com.dunk.tfc.Food.FloraManager;
import com.dunk.tfc.TileEntities.TEBerryBush;

public class TFCBerryBush implements IWailaDataProvider
{
	private static String showHarvest = "udary.terrafirmacraft.berrybush.harvest";
	private static String showHasFruit = "udary.terrafirmacraft.berrybush.hasfruit";
	
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
		if (accessor.getTileEntity() instanceof TEBerryBush)
		{
			TEBerryBush tileEntity = (TEBerryBush)accessor.getTileEntity();

			if (config.getConfig(showHarvest))
			{
				if (accessor.getBlock() instanceof BlockBerryBush)
				{
					BlockBerryBush blockBerryBush = (BlockBerryBush)accessor.getBlock();

					int meta = accessor.getMetadata();
					FloraIndex fi = FloraManager.getInstance().findMatchingIndex(blockBerryBush.getType(meta));

					if (fi != null)
					{
						EnumChatFormatting colour = EnumChatFormatting.WHITE;
						if (TFC_Time.currentMonth >= fi.harvestStart && TFC_Time.currentMonth <= fi.harvestFinish)
							colour = EnumChatFormatting.GREEN;
						
						if (fi.harvestStart == fi.harvestFinish)
							currenttip.add(StatCollector.translateToLocal("tooltip.harvest")+WailaUtils.SEPARATOR_COLON+colour.toString()+TFC_Time.MONTHS[fi.harvestStart]);
						else
							currenttip.add(StatCollector.translateToLocal("tooltip.harvest")+WailaUtils.SEPARATOR_COLON+colour.toString()+TFC_Time.MONTHS[fi.harvestStart]+" - "+TFC_Time.MONTHS[fi.harvestFinish]);
					}
				}
			}
			
			if (config.getConfig(showHasFruit))
			{
				if (tileEntity.hasFruit)
					currenttip.add(StatCollector.translateToLocal("tooltip.hasfruit")+" "+WailaUtils.SYMBOL_GREENTICK);
				else
					currenttip.add(StatCollector.translateToLocal("tooltip.hasfruit")+" "+WailaUtils.SYMBOL_REDCROSS);
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
		TFCBerryBush dataProvider = new TFCBerryBush();
		
		reg.registerBodyProvider(dataProvider, TEBerryBush.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_FLORA, showHarvest, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_FLORA, showHasFruit, false);
	}
}
