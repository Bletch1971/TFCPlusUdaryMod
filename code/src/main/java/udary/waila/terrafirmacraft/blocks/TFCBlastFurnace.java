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
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.TileEntities.TEBlastFurnace;

public class TFCBlastFurnace implements IWailaDataProvider
{
	private static String showStorage = "udary.terrafirmacraft.blastfurnace.storage";
	private static String showValidLevels = "udary.terrafirmacraft.blastfurnace.validlevels";
	
	private static String NBT_TAG_INPUT = "Input"; 
	private static String NBT_TAG_MAXVALIDSTACKSIZE= "maxValidStackSize"; 
	
	private static int SLOT_TUYERE = 1; 
	
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
		if (accessor.getTileEntity() instanceof TEBlastFurnace)
		{
			TEBlastFurnace tileEntity = (TEBlastFurnace)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];
		
			if (config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage, NBT_TAG_INPUT, ModTags.TAG_STORAGE_SLOT);
			}
			
			if (config.getConfig(showValidLevels))
			{
				if (tag.hasKey(NBT_TAG_MAXVALIDSTACKSIZE))
				{
					int maxSmokeStackLevel = tag.getInteger(NBT_TAG_MAXVALIDSTACKSIZE);
					if (maxSmokeStackLevel > 0)
						currenttip.add(StatCollector.translateToLocal("tooltip.stacklevels")+EnumChatFormatting.GREEN+" "+maxSmokeStackLevel);
					else
						currenttip.add(StatCollector.translateToLocal("tooltip.stacklevels")+EnumChatFormatting.RED+" 0");
				}
				else
					currenttip.add(StatCollector.translateToLocal("tooltip.stacklevels")+EnumChatFormatting.RED+" ?");
			}
			
			if (config.getConfig(showStorage))
			{
				String stackString = WailaUtils.getStackInformation(storage[SLOT_TUYERE]);
				if (stackString != "")
					currenttip.add(stackString);
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
		TFCBlastFurnace dataProvider = new TFCBlastFurnace();
		
		reg.registerNBTProvider(dataProvider, TEBlastFurnace.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEBlastFurnace.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showStorage, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showValidLevels, false);
	}
}