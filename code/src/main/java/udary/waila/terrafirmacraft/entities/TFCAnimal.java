package udary.waila.terrafirmacraft.entities;

import java.util.List;

import udary.tfcplusudarymod.core.ModOptions;
import udary.waila.WailaUtils;
import udary.waila.terrafirmacraft.WailaTerraFirmaCraft;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.api.Entities.IAnimal;
import com.dunk.tfc.api.Entities.IAnimal.GenderEnum;
import com.dunk.tfc.api.Entities.IAnimal.InteractionEnum;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
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
	private static String showAdult = "udary.terrafirmacraft.animal.adult";
	private static String showBirthDate = "udary.terrafirmacraft.animal.birthdate";
	private static String showFamiliarity = "udary.terrafirmacraft.animal.familiarity";
	private static String showGender = "udary.terrafirmacraft.animal.gender";
	private static String showHP = "udary.terrafirmacraft.animal.hp";
	private static String showInLove = "udary.terrafirmacraft.animal.inlove";
	private static String showMilked = "udary.terrafirmacraft.animal.milked";
	private static String showMisc = "udary.terrafirmacraft.animal.misc";
	private static String showPregnant = "udary.terrafirmacraft.animal.pregnant";
	
	private static String NBT_TAG_BIRTHDAY = "birthDay";
	private static String NBT_TAG_CONCEPTION = "ConceptionTime";
	private static String NBT_TAG_FAMILIAR = "Familiarity";
	private static String NBT_TAG_FAMILIARISEDLAST = "lastFamUpdate";
	private static String NBT_TAG_FAMILIARISEDTODAY = "Familiarized Today";
	private static String NBT_TAG_INLOVE = "inLove";
	private static String NBT_TAG_HASMILKTIME = "HasMilkTime";
	private static String NBT_TAG_NUMBEROFDAYSTOADULT = "NumberOfDaysToAdult";
	private static String NBT_TAG_PREGNANCYTIMEREQUIRED = "PregnancyRequiredTime";
	private static String NBT_TAG_PREGNANT = "pregnant";
	private static String NBT_TAG_SEX = "Sex";
	private static String NBT_TAG_TAMED = "tamed";
	private static String NBT_TAG_WASROPED = "wasRoped";
	
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
		if (entity instanceof EntityLivingBase)
		{
			if (config.getConfig(showHP))
			{
				currenttip.add(String.format(StatCollector.translateToLocal("tooltip.hp")+WailaUtils.SEPARATOR_COLON + EnumChatFormatting.WHITE + "%.0f" + EnumChatFormatting.GRAY + " / " + EnumChatFormatting.WHITE + "%.0f", ((EntityLivingBase)entity).getHealth(), ((EntityLivingBase)entity).getMaxHealth()));
			}
		}
		
		NBTTagCompound tag = accessor.getNBTData();
		
    	if (entity instanceof IAnimal)
    	{
    		int birthDay = tag.getInteger(NBT_TAG_BIRTHDAY);
    		
    		GenderEnum gender = GenderEnum.MALE;    		
    		if (tag.hasKey(NBT_TAG_SEX))
    		{
        		int sex = tag.getInteger(NBT_TAG_SEX);
    			gender = GenderEnum.GENDERS[sex];
    		}
    		
 			int numberOfDaysToAdult = tag.getInteger(NBT_TAG_NUMBEROFDAYSTOADULT);
			int adultDate = birthDay+numberOfDaysToAdult;
			Boolean isAdult = adultDate <= TFC_Time.getTotalDays();
   		
    		if (config.getConfig(showGender) && tag.hasKey(NBT_TAG_SEX))
    		{
	    		if (gender == GenderEnum.FEMALE)
	    			currenttip.add(StatCollector.translateToLocal("tooltip.gender")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.LIGHT_PURPLE.toString()+gender);
	    		else
	    			currenttip.add(StatCollector.translateToLocal("tooltip.gender")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.AQUA.toString()+gender);
    		}
    		
    		if (config.getConfig(showBirthDate))
    		{    			
	    		String birthDate = TFC_Time.getDateStringFromHours(birthDay * TFC_Time.HOURS_IN_DAY);
	    		currenttip.add(StatCollector.translateToLocal("tooltip.birthdate")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.WHITE.toString()+birthDate);     			
    		}
    		
    		if (config.getConfig(showAdult))
    		{
	    		if (isAdult)
	    			currenttip.add(StatCollector.translateToLocal("tooltip.adult")); 
	    		else
    			{
	    			int daysToAdult = adultDate - TFC_Time.getTotalDays();
	    			currenttip.add(StatCollector.translateToLocal("tooltip.daystoadult")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.WHITE.toString()+daysToAdult);    			
    			}
    		}
    		
    		if (config.getConfig(showInLove))
    		{
    			Boolean inLove = tag.getBoolean(NBT_TAG_INLOVE);
	    		if (inLove)
	    			currenttip.add(EnumChatFormatting.RED.toString()+StatCollector.translateToLocal("tooltip.inlove")); 
    		}
    		
    		if (config.getConfig(showPregnant))
    		{
    			Boolean isPregnant = tag.getBoolean(NBT_TAG_PREGNANT);
	    		if (isPregnant)
	    			currenttip.add(EnumChatFormatting.YELLOW.toString()+StatCollector.translateToLocal("tooltip.pregnant")); 
	    		
	    		if (isPregnant)
	    		{
		    		int pregnancyRequiredTime = -1;
		    		if (tag.hasKey(NBT_TAG_PREGNANCYTIMEREQUIRED))
		    			pregnancyRequiredTime = tag.getInteger(NBT_TAG_PREGNANCYTIMEREQUIRED);
		    		
		    		long conception = -1;
		    		if (tag.hasKey(NBT_TAG_CONCEPTION))
		    			conception = tag.getLong(NBT_TAG_CONCEPTION);
		    		
		    		if (pregnancyRequiredTime > -1 && conception > -1)
		    		{
				        long remaining = (conception + pregnancyRequiredTime) - TFC_Time.getTotalTicks();

				        String timeString = " "+StatCollector.translateToLocal("tooltip.ticks");
				        if (remaining < -TFC_Time.HOUR_LENGTH || remaining > TFC_Time.HOUR_LENGTH)
				        {
				        	remaining /= TFC_Time.HOUR_LENGTH;
				        	timeString = " "+StatCollector.translateToLocal("tooltip.hours");
				        }

				        if (remaining < 0)
				        	currenttip.add(EnumChatFormatting.YELLOW.toString()+StatCollector.translateToLocal("tooltip.pregnancyremaining")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.RED.toString()+remaining+timeString);
				        else
				        	currenttip.add(EnumChatFormatting.YELLOW.toString()+StatCollector.translateToLocal("tooltip.pregnancyremaining")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.GREEN.toString()+remaining+timeString);
		    		}
	    		}
    		}

    		if (config.getConfig(showMilked))
    		{
    			if (isAdult && gender == GenderEnum.FEMALE && tag.hasKey(NBT_TAG_HASMILKTIME))
    			{
    				long hasMilkTime = tag.getLong(NBT_TAG_HASMILKTIME);
    				if (hasMilkTime < TFC_Time.getTotalTicks())
    					currenttip.add(StatCollector.translateToLocal("tooltip.milkedtoday")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_REDCROSS);
    				else
    					currenttip.add(StatCollector.translateToLocal("tooltip.milkedtoday")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_GREENTICK);
    			}
    		}
    		
    		if (config.getConfig(showMisc))
    		{
    			if (tag.hasKey(NBT_TAG_WASROPED))
    			{
    				Boolean wasRoped = tag.getBoolean(NBT_TAG_WASROPED);
			        if (wasRoped)
    					currenttip.add(StatCollector.translateToLocal("tooltip.wasroped")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_GREENTICK);
    			}

    			if (tag.hasKey(NBT_TAG_TAMED))
    			{
    				byte tamed = tag.getByte(NBT_TAG_TAMED);
			        if ((tamed & 4) != 0)
    					currenttip.add(StatCollector.translateToLocal("tooltip.tamed")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_GREENTICK);
    			}
    		}
    	}  
    		
    	if (entity instanceof IAnimal)
    	{
    		IAnimal animal = (IAnimal)entity;
    		
    		GenderEnum gender = GenderEnum.MALE;    		
    		if (tag.hasKey(NBT_TAG_SEX))
    		{
        		int sex = tag.getInteger(NBT_TAG_SEX);
    			gender = GenderEnum.GENDERS[sex];
    		}

    		if (config.getConfig(showFamiliarity))
    		{
    			if (tag.hasKey(NBT_TAG_FAMILIARISEDTODAY))
    			{
    				Boolean familiarizedToday = tag.getBoolean(NBT_TAG_FAMILIARISEDTODAY);
    				if (familiarizedToday)
    					currenttip.add(StatCollector.translateToLocal("tooltip.familiarisedtoday")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_GREENTICK);
    				else
    					currenttip.add(StatCollector.translateToLocal("tooltip.familiarisedtoday")+WailaUtils.SEPARATOR_COLON+WailaUtils.SYMBOL_REDCROSS);
    			}
    			
    			if (tag.hasKey(NBT_TAG_FAMILIARISEDLAST))
    			{
    				int lastFamiliarised = tag.getInteger(NBT_TAG_FAMILIARISEDLAST);
    				currenttip.add(StatCollector.translateToLocal("tooltip.familiarisedlast")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.WHITE.toString()+TFC_Time.getDateStringFromHours(lastFamiliarised * TFC_Time.HOURS_IN_DAY));
    			}
    			
    			String familiarity = StatCollector.translateToLocal("tooltip.familiarity");
    			
    			if (tag.hasKey(NBT_TAG_FAMILIAR))
    			{
    				familiarity += " ("+tag.getInteger(NBT_TAG_FAMILIAR)+")";
    			}
    			
				currenttip.add(familiarity);

				Boolean hasFamiliarity = false;
				
        		for (int index = 0; index < InteractionEnum.INTERACTIONS.length; index++)
        		{
        			if (InteractionEnum.INTERACTIONS[index] == InteractionEnum.MILK && gender == GenderEnum.MALE) continue;
        			
            		Boolean familiar = animal.checkFamiliarity(InteractionEnum.INTERACTIONS[index], null);
            		if (familiar)
        			{
            			currenttip.add(WailaUtils.INDENT+EnumChatFormatting.DARK_AQUA.toString()+InteractionEnum.INTERACTIONS[index].name());
            			hasFamiliarity = true;
        			}
        		}
        		
        		if (!hasFamiliarity)
        		{
        			currenttip.add(WailaUtils.INDENT+EnumChatFormatting.RED.toString()+StatCollector.translateToLocal("tooltip.none"));
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
		{
			entity.writeToNBT(tag);
			
			if (entity instanceof IAnimal)
			{
				tag.setInteger(NBT_TAG_BIRTHDAY, ((IAnimal)entity).getBirthDay());
				tag.setBoolean(NBT_TAG_INLOVE, ((IAnimal)entity).getInLove());
				tag.setInteger(NBT_TAG_NUMBEROFDAYSTOADULT, ((IAnimal)entity).getNumberOfDaysToAdult());
				tag.setBoolean(NBT_TAG_PREGNANT, ((IAnimal)entity).isPregnant());
			}
		}

        return tag;
    }

	public static void callbackRegister(IWailaRegistrar reg)
	{
		TFCAnimal entityProvider = new TFCAnimal();
		
		reg.registerNBTProvider(entityProvider, Entity.class);
		if (!ModOptions.loadTerraFirmaCraftWailaClasses) return;

		reg.registerBodyProvider(entityProvider, Entity.class);
		
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showAdult, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showBirthDate, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showFamiliarity, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showGender, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showHP, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showInLove, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showMilked, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showMisc, false);
		reg.addConfig(WailaTerraFirmaCraft.CONFIG_MODNAME_ANIMALS, showPregnant, false);
	}
}
