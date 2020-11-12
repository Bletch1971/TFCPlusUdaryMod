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
import udary.common.Recipe;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.materials.ContainerDryingMat;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.managers.DryingMatManager;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;
import udary.waila.WailaUtils;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;

public class WDryingMat implements IWailaDataProvider
{
	private static String showProcess = "udary.terrafirmacraft.dryingmat.process";
	private static String showStorage = "udary.terrafirmacraft.dryingmat.storage";
	
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
		if (accessor.getTileEntity() instanceof TileEntityDryingMat)
		{
			TileEntityDryingMat tileEntity = (TileEntityDryingMat)accessor.getTileEntity();
		
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showStorage) || config.getConfig(showProcess))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showStorage))
			{
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
				ItemStack is = storage[ContainerDryingMat.SLOT_INPUT_1];
				Recipe recipe = DryingMatManager.getInstance().findRecipe(is, EnumRecipeMatchType.MINIMUM);
				
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

			if (te instanceof TileEntityDryingMat)
			{
				tag.setBoolean(ModTags.TAG_PROCESSING, ((TileEntityDryingMat)te).getCanDry());
				tag.setInteger(ModTags.TAG_PROGRESS, ((TileEntityDryingMat) te).getProcessPercentage());
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		WDryingMat dataProvider = new WDryingMat();
		
		reg.registerNBTProvider(dataProvider, TileEntityDryingMat.class);
		if (!ModOptions.loadUdaryModWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TileEntityDryingMat.class);
		
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showProcess, true);
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showStorage, true);
	}
}
