package udary.waila.terrafirmacraft.blocks;

import java.util.List;

import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.dunk.tfc.Blocks.BlockFarmland;
import com.dunk.tfc.TileEntities.TEFarmland;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCFarmland implements IWailaDataProvider
{
	private static String showNutrients = "udary.terrafirmacraft.farmland.nutrients";
	private static String showStone = "udary.terrafirmacraft.farmland.stone";
	
	private static String NBT_TAG_NUTRIENTS = "nutrients";
	private static String NBT_TAG_SOILMAX = "soilMax";
	
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
		if (accessor.getBlock() instanceof BlockFarmland)
		{
			NBTTagCompound tag = accessor.getNBTData();
			
			BlockFarmland block = (BlockFarmland)accessor.getBlock();

			if (config.getConfig(showStone))
			{
				int damage = itemStack != null ? itemStack.getItemDamage() : 0;
				
				if (block == TFCBlocks.tilledSoil2)
					damage += 16;
	
				if (damage < Global.STONE_ALL.length)
					currenttip.add(Global.STONE_ALL[damage]);
				else
					currenttip.add(StatCollector.translateToLocal("tooltip.unknown"));
			}
			
			if (config.getConfig(showNutrients))
			{
				if (tag.hasKey(NBT_TAG_NUTRIENTS) && tag.hasKey(NBT_TAG_SOILMAX))
				{
					int nutrients[] = tag.getIntArray(NBT_TAG_NUTRIENTS);
					int soilMax = tag.getInteger(NBT_TAG_SOILMAX);

					for (int index = 0; index < nutrients.length; index++)
					{
						int percent = Math.max(nutrients[index] * 100 / soilMax, 0);

						if (index == 0)
							currenttip.add(EnumChatFormatting.RED+StatCollector.translateToLocal("gui.Nutrient.A")+WailaUtils.SEPARATOR_COLON+percent+"%");
						else if (index == 1)
							currenttip.add(EnumChatFormatting.GOLD+StatCollector.translateToLocal("gui.Nutrient.B")+WailaUtils.SEPARATOR_COLON+percent+"%");
						else if (index == 2)
							currenttip.add(EnumChatFormatting.YELLOW+StatCollector.translateToLocal("gui.Nutrient.C")+WailaUtils.SEPARATOR_COLON+percent+"%");
						else if (index == 3 && percent != 0)
							currenttip.add(EnumChatFormatting.WHITE+StatCollector.translateToLocal("item.Fertilizer.name")+WailaUtils.SEPARATOR_COLON+percent+"%");
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

			if (te instanceof TEFarmland)
			{
				tag.setInteger(NBT_TAG_SOILMAX, ((TEFarmland)te).getSoilMax());
			}
		}
			
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCFarmland dataProvider = new TFCFarmland();
		
		reg.registerNBTProvider(dataProvider, TEFarmland.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TEFarmland.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showNutrients, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_BLOCKS, showStone, false);
	}
}
