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
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.managers.EvaporatorPanManager;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import udary.waila.WailaUtils;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;

public class WEvaporatorPan implements IWailaDataProvider  
{
	private static String showProcess = "udary.terrafirmacraft.evaporatorpan.process";
	private static String showStorage = "udary.terrafirmacraft.evaporatorpan.storage";
	
	@Override
	public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config)
	{
		return null;
	}

	@Override
	public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getTileEntity() instanceof TileEntityEvaporatorPan)
		{
			String head = currenttip.get(0);
			
			NBTTagCompound tag = accessor.getNBTData();
			FluidStack fs = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(ModTags.TAG_FLUID_TAG));
	
			if (fs != null)
			{
				head += " (" + fs.getLocalizedName() + ")";
				currenttip.set(0, head);
			}
		}
		
		return currenttip;
	}

	@Override
	public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) 
	{
		if (accessor.getTileEntity() instanceof TileEntityEvaporatorPan)
		{
			TileEntityEvaporatorPan tileEntity = (TileEntityEvaporatorPan)accessor.getTileEntity();

			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			FluidStack fs = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(ModTags.TAG_FLUID_TAG));

			if (config.getConfig(showStorage) || config.getConfig(showProcess))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
				// Fluid Amount
				String fluidString = WailaUtils.getFluidInformation(fs, tileEntity.getMaxFluid(), false);
				if (fluidString != "")
					currenttip.add(fluidString);
				
				
				for (int i = 0; i < storage.length; i++)
				{
					if (storage[i] == null) continue;
					
					String stackString = WailaUtils.getStackInformation(storage[i]);
					if (stackString != "")
						currenttip.add(stackString);
				}
			}
	
			if (config.getConfig(showProcess))
			{
				Recipe recipe = EvaporatorPanManager.getInstance().findRecipe(fs, EnumRecipeMatchType.MINIMUM);
				
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
			
			if (te instanceof TileEntityEvaporatorPan)
			{
				tag.setBoolean(ModTags.TAG_PROCESSING, ((TileEntityEvaporatorPan)te).getCanEvaporate());
				tag.setInteger(ModTags.TAG_PROGRESS, ((TileEntityEvaporatorPan)te).getProcessPercentage());
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		WEvaporatorPan dataProvider = new WEvaporatorPan();
		
		reg.registerNBTProvider(dataProvider, TileEntityEvaporatorPan.class);
		if (!ModOptions.loadUdaryModWailaClasses) return;

		reg.registerHeadProvider(dataProvider, TileEntityEvaporatorPan.class);
		reg.registerBodyProvider(dataProvider, TileEntityEvaporatorPan.class);
		
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showProcess, true);
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showStorage, true);
	}	
}
