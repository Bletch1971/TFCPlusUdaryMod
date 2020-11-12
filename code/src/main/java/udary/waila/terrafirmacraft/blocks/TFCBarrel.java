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
import net.minecraftforge.fluids.FluidStack;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.TileEntities.TEBarrel;
import com.dunk.tfc.api.Crafting.BarrelManager;
import com.dunk.tfc.api.Crafting.BarrelRecipe;

public class TFCBarrel implements IWailaDataProvider
{
	private static String showProgress = "udary.terrafirmacraft.barrel.progress";
	
	private static int INPUT_SLOT = 0;
	
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
		if (accessor.getTileEntity() instanceof TEBarrel)
		{
			TEBarrel tileEntity = (TEBarrel)accessor.getTileEntity();
			
			NBTTagCompound tag = accessor.getNBTData();
			ItemStack[] storage = new ItemStack[tileEntity.getSizeInventory()];

			if (config.getConfig(showProgress))
			{
				WailaUtils.populateStorageItems(tag, storage);
			}
			
			if (config.getConfig(showProgress))
			{
				FluidStack fs = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag("fluidNBT"));
				ItemStack is = storage[INPUT_SLOT];
				Boolean sealed = tag.getBoolean("Sealed");
				
				BarrelRecipe recipe = BarrelManager.getInstance().findMatchingRecipe(is, fs, sealed, tileEntity.getTechLevel());				

				if (recipe != null && recipe.isSealedRecipe() && recipe.sealTime > 1)
				{
					float sealTime = tag.getInteger("SealTime") * TFC_Time.HOUR_LENGTH;
					float currTime = (int)TFC_Time.getTotalTicks();
					float diffTime = currTime - sealTime;
					float recipeTime = recipe.sealTime * TFC_Time.HOUR_LENGTH;
					int percent = Math.round(Math.min((diffTime / recipeTime) * 100, 100));

					String progressString = WailaUtils.getProgressInformation(percent);
					if (progressString != "")
						currenttip.add(progressString);
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
		TFCBarrel dataProvider = new TFCBarrel();
		
		reg.registerNBTProvider(dataProvider, TEBarrel.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEBarrel.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showProgress, false);
	}
}