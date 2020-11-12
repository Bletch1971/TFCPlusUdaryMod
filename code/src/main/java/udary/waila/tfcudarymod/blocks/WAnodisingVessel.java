package udary.waila.tfcudarymod.blocks;

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
import net.minecraftforge.fluids.FluidStack;
import udary.common.Recipe;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerAnodisingVessel;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.managers.AnodisingVesselAnodiseManager;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import udary.waila.WailaUtils;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;

public class WAnodisingVessel implements IWailaDataProvider  
{
	private static String showProcess = "udary.terrafirmacraft.anodisingvessel.process";
	private static String showStorage = "udary.terrafirmacraft.anodisingvessel.storage";
	
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
		if (accessor.getTileEntity() instanceof TileEntityAnodisingVessel)
		{
			TileEntityAnodisingVessel tileEntity = (TileEntityAnodisingVessel)accessor.getTileEntity();

			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			FluidStack fs = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(ModTags.TAG_FLUID_TAG));

			if (config.getConfig(showStorage) || config.getConfig(showProcess))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
				String stackString = WailaUtils.getStackInformation(storage[ContainerAnodisingVessel.SLOT_BATTERY]);
				if (stackString != "")
					currenttip.add(stackString);
				
				stackString = WailaUtils.getStackInformation(storage[ContainerAnodisingVessel.SLOT_ANODE]);
				if (stackString != "")
					currenttip.add(stackString);

				stackString = WailaUtils.getStackInformation(storage[ContainerAnodisingVessel.SLOT_CATHODE]);
				if (stackString != "")
					currenttip.add(stackString);

				stackString = WailaUtils.getStackInformation(storage[ContainerAnodisingVessel.SLOT_SOLUTE]);
				if (stackString != "")
					currenttip.add(stackString);
			}
	
			if (config.getConfig(showProcess))
			{
				ItemStack is = storage[ContainerAnodisingVessel.SLOT_SOLUTE];
				Recipe recipe = AnodisingVesselAnodiseManager.getInstance().findRecipe(fs, is, EnumRecipeMatchType.MINIMUM);
				
				String resultString = WailaUtils.getRecipeResultInformation(recipe, true);
				if (resultString != "")
					currenttip.add(resultString);
				
				Boolean isProcessing = tag.hasKey(ModTags.TAG_PROCESSING) && tag.getBoolean(ModTags.TAG_PROCESSING);
				if (isProcessing)
				{
					int percent = 0;
					if (tag.hasKey(ModTags.TAG_PROGRESS))
						percent = tag.getInteger(ModTags.TAG_PROGRESS);
					
					String processString = WailaUtils.getRecipeProcessInformation(recipe, percent);
					if (processString != "")
						currenttip.add(processString);
				}
				else
				{
					if (tag.hasKey(ModTags.TAG_PROGRESS))
					{
						int percent = tag.getInteger(ModTags.TAG_PROGRESS);
						String progressString = WailaUtils.getProgressInformation(percent);
						if (progressString != "")
							currenttip.add(progressString);
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
			
			if (te instanceof TileEntityAnodisingVessel)
			{
				tag.setBoolean(ModTags.TAG_PROCESSING, ((TileEntityAnodisingVessel)te).getIsProcessing());
				tag.setInteger(ModTags.TAG_PROGRESS, ((TileEntityAnodisingVessel)te).getProcessPercentage());
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		WAnodisingVessel dataProvider = new WAnodisingVessel();
		
		reg.registerNBTProvider(dataProvider, TileEntityAnodisingVessel.class);
		if (!ModOptions.loadUdaryModWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TileEntityAnodisingVessel.class);
		
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showProcess, true);
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showStorage, true);
	}	
}
