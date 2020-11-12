package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TESapling;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCSapling implements IWailaDataProvider 
{
	private static String showRemaining = "udary.terrafirmacraft.sapling.remaining";
	
	private static String NBT_TAG_GROWTIME = "growTime";
	
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
		if (accessor.getTileEntity() instanceof TESapling)
		{
			NBTTagCompound tag = accessor.getNBTData();
			
			if (config.getConfig(showRemaining))
			{
				if (tag.hasKey(NBT_TAG_GROWTIME))
				{
					long growTime = tag.getLong(NBT_TAG_GROWTIME);
					
			        if (growTime <= 0)
			        	currenttip.add(StatCollector.translateToLocal("tooltip.remaining")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.GRAY.toString()+StatCollector.translateToLocal("tooltip.unknown"));
			        else
			        {
				        long remaining = growTime - TFC_Time.getTotalTicks();
				        String unitString = StatCollector.translateToLocal("tooltip.ticks");
				        
				        if (remaining < -TFC_Time.HOUR_LENGTH || remaining > TFC_Time.HOUR_LENGTH)
				        {
				        	remaining /= TFC_Time.HOUR_LENGTH;
				        	unitString = StatCollector.translateToLocal("tooltip.hours");
				        }
				        
						String remainingString = WailaUtils.getRemainingInformation(remaining, unitString, 0);
						if (remainingString != "")
							currenttip.add(remainingString);
			        }
				}
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
		if (te != null)
		{
			te.writeToNBT(tag);

			if (te instanceof TESapling)
			{
				tag.setLong(NBT_TAG_GROWTIME, ((TESapling)te).growTime);
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCSapling dataProvider = new TFCSapling();
		
		reg.registerNBTProvider(dataProvider, TESapling.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TESapling.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_FLORA, showRemaining, false);
	}
}
