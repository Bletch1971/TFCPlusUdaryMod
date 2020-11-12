package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Blocks.Flora.BlockFruitLeaves;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Food.FloraIndex;
import com.dunk.tfc.Food.FloraManager;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.TileEntities.TEFruitLeaves;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCFruitLeaves implements IWailaDataProvider 
{
	private static String showHarvest = "udary.terrafirmacraft.fruitleaves.harvest";
	private static String showHasFruit = "udary.terrafirmacraft.fruitleaves.hasfruit";
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		ItemStack itemstack = null;

		if (accessor.getBlock() instanceof BlockFruitLeaves)
		{
			BlockFruitLeaves block = (BlockFruitLeaves)accessor.getBlock();
			int meta = accessor.getMetadata();
			FloraIndex fi = FloraManager.getInstance().findMatchingIndex(BlockFruitLeaves.getType(block, meta & 7));
			
			if (fi != null)
			{
				if (fi.output != null)
					itemstack = fi.output.copy();
							
				if (itemstack != null)
					// must create a tag as the itemstack is food
					ItemFoodTFC.createTag(itemstack);
			}
		}
		
		return itemstack;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof BlockFruitLeaves)
		{
			String tip = EnumChatFormatting.WHITE.toString()+StatCollector.translateToLocal("tooltip.FruitLeaves."+itemStack.getDisplayName());
			currenttip.set(0, tip);			
		}
		
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getBlock() instanceof BlockFruitLeaves && accessor.getTileEntity() instanceof TEFruitLeaves)
		{
			TEFruitLeaves tileEntity = (TEFruitLeaves)accessor.getTileEntity();
			BlockFruitLeaves block = (BlockFruitLeaves)accessor.getBlock();
			
			if (config.getConfig(showHarvest))
			{
				int meta = accessor.getMetadata();
				FloraIndex fi = FloraManager.getInstance().findMatchingIndex(BlockFruitLeaves.getType(block, meta & 7));
				
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
		TFCFruitLeaves dataProvider = new TFCFruitLeaves();
		
		reg.registerStackProvider(dataProvider, TEFruitLeaves.class);
		reg.registerHeadProvider(dataProvider, TEFruitLeaves.class);
		reg.registerBodyProvider(dataProvider, TEFruitLeaves.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_FLORA, showHarvest, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_FLORA, showHasFruit, false);
	}
}
