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
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.TileEntities.TECrucible;

public class TFCCrucible implements IWailaDataProvider 
{
	private static String showMetal = "udary.terrafirmacraft.crucible.showmetal";

	private static String NBT_TAG_ALLOY = "alloy";
	
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
		if (accessor.getTileEntity() instanceof TECrucible)
		{
			NBTTagCompound tag = accessor.getNBTData();
			
			if (config.getConfig(showMetal))
			{
				if (tag.hasKey(NBT_TAG_ALLOY))
				{
					NBTTagCompound alloyTag = tag.getCompoundTag(NBT_TAG_ALLOY);
					
					if (alloyTag != null)
					{
						Alloy currentAlloy = new Alloy().fromNBT(alloyTag);
						
						if (currentAlloy != null && currentAlloy.outputType != null)
						{
							String metalString = WailaUtils.getMetalInformation(currentAlloy.outputType, false);
							if (metalString != "")
								currenttip.add(metalString);
							
							String unitsString = WailaUtils.getUnitsInformation((int)currentAlloy.outputAmount);
							if (unitsString != "")
								currenttip.add(unitsString);
						}
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
			
			if (te instanceof TECrucible)
			{
				if (((TECrucible)te).currentAlloy != null)
				{
					NBTTagCompound alloyTag = new NBTTagCompound();
					((TECrucible)te).currentAlloy.toNBT(alloyTag);
					tag.setTag(NBT_TAG_ALLOY, alloyTag);
				}
			}
		}
				
		return tag;
	}

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCCrucible dataProvider = new TFCCrucible();
		
		reg.registerNBTProvider(dataProvider, TECrucible.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(dataProvider, TECrucible.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showMetal, false);
	}
}
