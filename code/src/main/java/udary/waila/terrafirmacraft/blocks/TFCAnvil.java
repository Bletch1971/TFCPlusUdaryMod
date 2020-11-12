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

import com.dunk.tfc.TileEntities.TEAnvil;

public class TFCAnvil implements IWailaDataProvider
{
	private static String showStorage = "udary.terrafirmacraft.anvil.storage";
	private static String showTier = "udary.terrafirmacraft.anvil.tier";
	
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
		if (accessor.getTileEntity() instanceof TEAnvil)
		{			
			TEAnvil tileEntity = (TEAnvil)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showTier))
			{
				int tier = tileEntity.anvilTier;
				currenttip.add(StatCollector.translateToLocal("gui.tier") + WailaUtils.SEPARATOR_COLON + tier);				
			}
			
			if (config.getConfig(showStorage))
			{
				String stackString = WailaUtils.getStackInformation(storage[TEAnvil.HAMMER_SLOT]);
				if (stackString != "")
					currenttip.add(stackString);

//				String stackString = WailaUtils.getStackInformation(storage[TEAnvil.FLUX_SLOT]);
//				if (stackString != "")
//					currenttip.add(stackString);
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
		TFCAnvil dataProvider = new TFCAnvil();
		
		reg.registerNBTProvider(dataProvider, TEAnvil.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEAnvil.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showStorage, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showTier, false);
	}
}