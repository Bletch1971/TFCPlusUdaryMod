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
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.managers.OreCookerManager;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;
import udary.waila.WailaUtils;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;

public class WOreCooker implements IWailaDataProvider
{
	private static String showProcess = "udary.terrafirmacraft.orecooker.process";
	private static String showStorage = "udary.terrafirmacraft.orecooker.storage";
	
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
		if (accessor.getTileEntity() instanceof TileEntityOreCooker)
		{
			TileEntityOreCooker tileEntity = (TileEntityOreCooker)accessor.getTileEntity();
			
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
				ItemStack is = storage[ContainerOreCooker.SLOT_INPUT_1];
				Recipe recipe = OreCookerManager.getInstance().findRecipe(is, EnumRecipeMatchType.MINIMUM);
				
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

			if (te instanceof TileEntityOreCooker)
			{
				tag.setBoolean(ModTags.TAG_PROCESSING, ((TileEntityOreCooker)te).getCanCook());
				tag.setInteger(ModTags.TAG_PROGRESS, ((TileEntityOreCooker)te).getProcessPercentage());
			}
		}
		
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		WOreCooker dataProvider = new WOreCooker();
		
		reg.registerNBTProvider(dataProvider, TileEntityOreCooker.class);
		if (!ModOptions.loadUdaryModWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TileEntityOreCooker.class);
		
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showProcess, true);
		reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showStorage, true);
	}
}
