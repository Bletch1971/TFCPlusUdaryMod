package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.TileEntities.TEFoodPrep;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCFoodPrep implements IWailaDataProvider
{
	private static String showStorage = "udary.terrafirmacraft.foodprep.storage";
	
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
		if (accessor.getTileEntity() instanceof TEFoodPrep)
		{
			TEFoodPrep tileEntity = (TEFoodPrep)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
				WailaUtils.combineStorage(storage);
				
				for (int i = 0; i < storage.length; i++)
				{
					if (storage[i] == null) continue;
					
					String stackString = WailaUtils.getStackInformation(storage[i]);
					if (stackString != "")
						currenttip.add(stackString);
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
		TFCFoodPrep dataProvider = new TFCFoodPrep();
		
		reg.registerNBTProvider(dataProvider, TEFoodPrep.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEFoodPrep.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_2, showStorage, false);
	}
}
