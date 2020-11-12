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

import com.dunk.tfc.TileEntities.TEFirepit;

public class TFCFirepit implements IWailaDataProvider
{
	private static String showFuel = "udary.terrafirmacraft.firepit.fuel";
	private static String showStorage = "udary.terrafirmacraft.firepit.storage";
	
	private static int SLOT_FUEL_1 = 0;
	private static int SLOT_FUEL_2 = 3;
	private static int SLOT_FUEL_3 = 4;
	private static int SLOT_FUEL_4 = 5;
	private static int SLOT_INPUT_1 = 1;
	private static int SLOT_OUTPUT_1 = 7;
	private static int SLOT_OUTPUT_2 = 8;
	
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
		if (accessor.getTileEntity() instanceof TEFirepit)
		{
			TEFirepit tileEntity = (TEFirepit)accessor.getTileEntity();

			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showFuel) || config.getConfig(showStorage))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showFuel))
			{
				int fuelCount = 0;
				if (storage[SLOT_FUEL_1] != null) fuelCount += storage[SLOT_FUEL_1].stackSize;
				if (storage[SLOT_FUEL_2] != null) fuelCount += storage[SLOT_FUEL_2].stackSize;
				if (storage[SLOT_FUEL_3] != null) fuelCount += storage[SLOT_FUEL_3].stackSize;
				if (storage[SLOT_FUEL_4] != null) fuelCount += storage[SLOT_FUEL_4].stackSize;
				
				if (fuelCount > 0)
					currenttip.add(StatCollector.translateToLocal("gui.fuel")+WailaUtils.SEPARATOR_COLON+fuelCount);
			}
			
			if (config.getConfig(showStorage))
			{
				String stackString = WailaUtils.getStackInformation(storage[SLOT_INPUT_1]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[SLOT_OUTPUT_1]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[SLOT_OUTPUT_2]);
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
		TFCFirepit dataProvider = new TFCFirepit();
		
		reg.registerNBTProvider(dataProvider, TEFirepit.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEFirepit.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_2, showFuel, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_2, showStorage, false);
	}
}
