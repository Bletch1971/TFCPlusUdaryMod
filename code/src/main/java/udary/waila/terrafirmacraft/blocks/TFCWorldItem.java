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
import net.minecraft.world.World;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.TileEntities.TEWorldItem;

public class TFCWorldItem implements IWailaDataProvider  
{
	private static String showStorage = "udary.terrafirmacraft.worlditem.storage";
	
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
		if (accessor.getTileEntity() instanceof TEWorldItem)
		{
			TEWorldItem tileEntity = (TEWorldItem)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
				for (int i = 0; i < storage.length; i++)
				{
					if (storage[i] == null) continue;
					
					List<String> metalStrings = WailaUtils.getMetalInformation(storage[i], true, true);
					if (metalStrings != null)
						currenttip.addAll(metalStrings);
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
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCWorldItem dataProvider = new TFCWorldItem();
		
		reg.registerBodyProvider(dataProvider, TEWorldItem.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showStorage, false);
	}
}
