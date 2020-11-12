package udary.waila.terrafirmacraft.entities;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Entities.EntityStand;
import com.dunk.tfc.api.Constant.Global;

import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaEntityAccessor;
import mcp.mobius.waila.api.IWailaEntityProvider;
import mcp.mobius.waila.api.IWailaRegistrar;

public class TFCStand implements IWailaEntityProvider  
{
	private static String showArmor = "udary.terrafirmacraft.stand.armor";
	
	private static int MAX_ARMOUR_SLOTS = 4;

    @Override
    public Entity getWailaOverride(IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
        return accessor.getEntity();
    }

    @Override
    public List<String> getWailaHead(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
    	if (entity instanceof EntityStand)
    	{
    		EntityStand entityStand = (EntityStand)entity;
    		
			String tip = EnumChatFormatting.WHITE.toString()+StatCollector.translateToLocal("tile.ArmourStand."+Global.WOOD_ALL[entityStand.woodType]+".name");
			currenttip.set(0, tip);		
    	}
    	
        return currenttip;
    }

    @Override
    public List<String> getWailaBody(Entity entity, List<String> currenttip, IWailaEntityAccessor accessor, IWailaConfigHandler config) 
    {
    	if (entity instanceof EntityStand)
    	{
    		EntityStand entityStand = (EntityStand)entity;

    		if (config.getConfig(showArmor))
    		{
    			for (int index = MAX_ARMOUR_SLOTS - 1; index >= 0; index--)
    			{
    				ItemStack itemStack = entityStand.getArmorInSlot(index);
    				String stackString = WailaUtils.getStackInformation(itemStack);
    				if (stackString != "")
    					currenttip.add(stackString);
    			}
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
            entity.writeToNBT(tag);

        return tag;
    }

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCStand entityProvider = new TFCStand();
		
		reg.registerNBTProvider(entityProvider, EntityStand.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerHeadProvider(entityProvider, EntityStand.class);
		reg.registerBodyProvider(entityProvider, EntityStand.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_DEVICES_1, showArmor, false);
	}
}
