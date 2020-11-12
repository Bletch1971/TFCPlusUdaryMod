package udary.waila.tfcudarymod.entities;

import java.util.List;

import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.core.managers.TuckerBagManager;
import udary.tfcplusudarymod.core.recipes.TuckerBagEntity;
import udary.waila.WailaUtils;
import udary.waila.tfcudarymod.WailaTFCUdaryMod;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCAnimal implements IWailaEntityProvider  
{
	private static String showTuckerBagInfo = "udary.terrafirmacraft.animal.tuckerbaginfo";
	
	private static String NBT_TAG_TBAG_BAGVERSION = "tbag_BagVersion";
	private static String NBT_TAG_TBAG_DAMAGEAMOUNT = "tbag_DamageAmount";
	
    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
        return accessor.getEntity();
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
		NBTTagCompound tag = accessor.getNBTData();
		
    	if (entity instanceof EntityAnimal)
    	{
			if (config.getConfig(showTuckerBagInfo) && ModOptions.enableTuckerBagMod)
			{
				currenttip.add(StatCollector.translateToLocal("tooltip.tuckerbag"));
				
				if (tag.hasKey(NBT_TAG_TBAG_BAGVERSION))
					currenttip.add(WailaUtils.INDENT+EnumChatFormatting.WHITE.toString()+StatCollector.translateToLocal("tooltip.tuckerbagversion")+WailaUtils.SEPARATOR_COLON+tag.getString(NBT_TAG_TBAG_BAGVERSION));
				
				if (tag.hasKey(NBT_TAG_TBAG_DAMAGEAMOUNT))
					currenttip.add(WailaUtils.INDENT+EnumChatFormatting.WHITE.toString()+StatCollector.translateToLocal("tooltip.tuckerbagdamageamount")+WailaUtils.SEPARATOR_COLON+tag.getInteger(NBT_TAG_TBAG_DAMAGEAMOUNT));
			}
    	}
    	
        return currenttip;
    }

    @Override
    public List<String> getWailaTail(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP player, Entity entity, NBTTagCompound tag, World world) 
    {
		if (entity != null)
		{
			entity.writeToNBT(tag);
			
			if (ModOptions.enableTuckerBagMod)
			{
				TuckerBagManager tuckerBagManager = TuckerBagManager.getInstance();
				
				TuckerBagEntity entityFound = tuckerBagManager.findMatchingEntity(entity.getClass());
				if (entityFound != null)
				{
					EnumTuckerBagVersion entityBagVersion = tuckerBagManager.getEntityBagVersion(entity);
					tag.setString(NBT_TAG_TBAG_BAGVERSION, entityBagVersion.getName());
					
					int entityDamage = tuckerBagManager.getEntityDamage(entity);
					if (entityDamage >= 0 && EnumTuckerBagVersion.isValid(entityBagVersion))
						tag.setInteger(NBT_TAG_TBAG_DAMAGEAMOUNT, tuckerBagManager.getEntityDamage(entity));
				}
				else
				{
					tag.setString(NBT_TAG_TBAG_BAGVERSION, EnumTuckerBagVersion.NONE.getName());
				}
			}
		}

        return tag;
    }

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCAnimal entityProvider = new TFCAnimal();
		
		reg.registerNBTProvider(entityProvider, Entity.class);
		if (!ModOptions.loadUdaryModWailaClasses) return;

		reg.registerBodyProvider(entityProvider, Entity.class);
		
		if (ModOptions.enableTuckerBagMod)
			reg.addConfig(WailaTFCUdaryMod.CONFIG_MODNAME_DEVICES, showTuckerBagInfo, true);
	}
}
