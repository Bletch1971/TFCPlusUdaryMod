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
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TESmokeRack;
import com.dunk.tfc.api.Food;

public class TFCSmokeRack implements IWailaDataProvider
{	
	private static String showStorage = "udary.terrafirmacraft.smokerack.storage";

	private static String NBT_TAG_DRIEDCOUNTER = "driedCounter";
	
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
		if (accessor.getTileEntity() instanceof TESmokeRack)
		{
			TESmokeRack tileEntity = (TESmokeRack)accessor.getTileEntity();

			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
				if (tag.hasKey(NBT_TAG_DRIEDCOUNTER))
				{
					int[] driedCounter = tag.getIntArray(NBT_TAG_DRIEDCOUNTER);
					if(driedCounter.length == 0)
						driedCounter = new int[] {0,0};
					
					for (int i = 0; i < storage.length; i++)
					{
						if (storage[i] == null) continue;
						
						int hangTime = (int)TFC_Time.getTotalHours()-driedCounter[i];
		
						String stackString = WailaUtils.getStackInformation(storage[i]);
						if (stackString != "")
							currenttip.add(stackString+EnumChatFormatting.RESET.toString()+" ("+hangTime+")");
		
						Boolean isDried = Food.isDried(storage[i]);
						if (isDried)
							currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.dried")+" "+WailaUtils.SYMBOL_GREENTICK);
						else
						{
							int dryHours = Food.getDried(storage[i]);
							if (dryHours > 0 && dryHours < Food.DRYHOURS)
							{
								int percent = (int)((float)dryHours / (float)Food.DRYHOURS * 100.0F);
								currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.dried")+WailaUtils.SEPARATOR_COLON+String.valueOf(percent)+StatCollector.translateToLocal("gui.progress.unit"));
							}
							else
								currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.dried")+" "+WailaUtils.SYMBOL_REDCROSS);
						}
		
						Boolean isSmoked = Food.isSmoked(storage[i]);
						if (isSmoked)
							currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.smoked")+" "+WailaUtils.SYMBOL_GREENTICK);
						else
						{
							int smokeHours = Food.getSmokeCounter(storage[i]);
							if (smokeHours > 0 && smokeHours < Food.SMOKEHOURS)
							{
								int percent = (int)((float)smokeHours / (float)Food.SMOKEHOURS * 100.0F);
								currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.smoked")+WailaUtils.SEPARATOR_COLON+String.valueOf(percent)+StatCollector.translateToLocal("gui.progress.unit"));
							}
							else
								currenttip.add(WailaUtils.INDENT+StatCollector.translateToLocal("word.smoked")+" "+WailaUtils.SYMBOL_REDCROSS);
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
			te.writeToNBT(tag);
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCSmokeRack dataProvider = new TFCSmokeRack();
		
		reg.registerNBTProvider(dataProvider, TESmokeRack.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TESmokeRack.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showStorage, false);
	}
}
