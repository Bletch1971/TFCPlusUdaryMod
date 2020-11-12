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
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TELightEmitter;
import com.dunk.tfc.api.TFCOptions;

public class TFCTorch implements IWailaDataProvider 
{
	private static String showRemaining = "udary.terrafirmacraft.torch.remaining";
	
	private static String NBT_TAG_BURNTIME = "burnTime";
	private static String NBT_TAG_REMAINING = "remaining";
	
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
		if (accessor.getTileEntity() instanceof TELightEmitter)
		{
			NBTTagCompound tag = accessor.getNBTData();

			if (config.getConfig(showRemaining))
			{
				if (tag.hasKey(NBT_TAG_BURNTIME) && tag.hasKey(NBT_TAG_REMAINING))
				{
					// get the burn time from the tile entity
					int burnTime = tag.getInteger(NBT_TAG_BURNTIME);
					if (burnTime > 0)
					{
						// check if the torch is lit (meta < 8)
						int meta = accessor.getMetadata();			
						if (meta < 8)
						{
							long remaining = Math.max(0, tag.getInteger(NBT_TAG_REMAINING));
							String unitString = StatCollector.translateToLocal("tooltip.hours");
							
							String remainingString = WailaUtils.getRemainingInformation(remaining, unitString, 0);
							if (remainingString != "")
								currenttip.add(remainingString);
						}
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
	
			if (te instanceof TELightEmitter)
			{
				tag.setInteger(NBT_TAG_BURNTIME, TFCOptions.torchBurnTime);
				tag.setLong(NBT_TAG_REMAINING, ((TELightEmitter)te).hourPlaced + TFCOptions.torchBurnTime - TFC_Time.getTotalHours());
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCTorch dataProvider = new TFCTorch();
		
		reg.registerNBTProvider(dataProvider, TELightEmitter.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TELightEmitter.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showRemaining, false);
	}
}
