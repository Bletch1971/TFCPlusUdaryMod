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
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.TFCOptions;

public class TFCPottery implements IWailaDataProvider
{
	private static String showPitKilnFuel = "udary.terrafirmacraft.pottery.pitkiln";
	private static String showProgress = "udary.terrafirmacraft.pottery.progress";
	private static String showStorage = "udary.terrafirmacraft.pottery.storage";
	
	private static String NBT_TAG_BURNTIME = "burnTime";
	private static String NBT_TAG_REMAINING = "remaining";
	private static String NBT_TAG_STRAW = "straw";
	private static String NBT_TAG_WOOD = "wood";
	
	private static int SLOT_INPUT_1 = 0; 
	private static int SLOT_INPUT_2 = 1; 
	private static int SLOT_INPUT_3 = 2; 
	private static int SLOT_INPUT_4 = 3; 
	
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
		if (accessor.getTileEntity() instanceof TEPottery)
		{
			TEPottery tileEntity = (TEPottery)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			Boolean isProcessing = tag.hasKey(ModTags.TAG_PROCESSING) && tag.getBoolean(ModTags.TAG_PROCESSING);

			if (config.getConfig(showStorage) || config.getConfig(showProgress) || config.getConfig(showPitKilnFuel))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showPitKilnFuel))
			{
				if (!isProcessing)
				{
					int strawCount = tag.getInteger(NBT_TAG_STRAW);
					int woodCount = tag.getInteger(NBT_TAG_WOOD);
					
					if (strawCount >= 8)
						currenttip.add(StatCollector.translateToLocal("item.Straw.name")+" "+WailaUtils.SYMBOL_GREENTICK);
					else if (strawCount > 0)
						currenttip.add(StatCollector.translateToLocal("item.Straw.name")+WailaUtils.SEPARATOR_COLON+strawCount);
					
					if (woodCount >= 8)
						currenttip.add(StatCollector.translateToLocal("gui.logs")+" "+WailaUtils.SYMBOL_GREENTICK);
					else if (woodCount > 0)
						currenttip.add(StatCollector.translateToLocal("gui.logs")+WailaUtils.SEPARATOR_COLON+woodCount);
				}
			}
			
			if (config.getConfig(showStorage))
			{
				String stackString = WailaUtils.getStackInformation(storage[SLOT_INPUT_1]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[SLOT_INPUT_2]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[SLOT_INPUT_3]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[SLOT_INPUT_4]);
				if (stackString != "")
					currenttip.add(stackString);
			}
			
			if (config.getConfig(showProgress))
			{
				if (isProcessing)
				{
					long remaining = 0;
					if (tag.hasKey(NBT_TAG_REMAINING))
						remaining = Math.max(0, tag.getInteger(NBT_TAG_REMAINING));
					
					int percent = 0;
					if (tag.hasKey(ModTags.TAG_PROGRESS))
						percent = tag.getInteger(ModTags.TAG_PROGRESS);
					
					String processString = WailaUtils.getRemainingInformation(remaining, StatCollector.translateToLocal("tooltip.hours"), percent);
					if (processString != "")
						currenttip.add(processString);					
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

			if (te instanceof TEPottery)
			{
				tag.setFloat(NBT_TAG_BURNTIME, TFCOptions.pitKilnBurnTime);

				if (((TEPottery)te).burnStart > 0)
				{
					float burnTotal = TFCOptions.pitKilnBurnTime * TFC_Time.HOUR_LENGTH;
					long burnTime = TFC_Time.getTotalTicks() - ((TEPottery)te).burnStart;
					int percent = (int)Math.min(((float)burnTime / burnTotal * 100F), 100F);

					tag.setBoolean(ModTags.TAG_PROCESSING, true);
					tag.setInteger(ModTags.TAG_PROGRESS, percent);
					
					if (burnTime % TFC_Time.HOUR_LENGTH == 0)
						tag.setInteger(NBT_TAG_REMAINING, (int)((burnTotal - burnTime) / TFC_Time.HOUR_LENGTH));
					else
						tag.setInteger(NBT_TAG_REMAINING, (int)((burnTotal - burnTime) / TFC_Time.HOUR_LENGTH) + 1);
				}
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCPottery dataProvider = new TFCPottery();
		
		reg.registerNBTProvider(dataProvider, TEPottery.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEPottery.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showPitKilnFuel, true);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showProgress, true);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showStorage, true);
	}
}
