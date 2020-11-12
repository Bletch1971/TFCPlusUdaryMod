package udary.waila;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import udary.common.Recipe;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.interfaces.IBattery;
import udary.tfcplusudarymod.items.tools.ItemTuckerBag;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemMeltedMetal;
import com.dunk.tfc.Items.Pottery.ItemPotteryMold;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.IFood;
import com.dunk.tfc.api.Interfaces.ISmeltable;

public class WailaUtils 
{
	public static final String MESSAGE_KEY_REGISTER = "register";
	public static final String SEPARATOR_COLON = " : ";
	public static final String SEPARATOR_DASH = " - ";
	public static final String SEPARATOR_FSLASH = "/";
	public static final String SEPARATOR_TIMES = "x ";
	public static final String INDENT = "  ";
	
	public static final String SYMBOL_GREENTICK = EnumChatFormatting.GREEN+"\u2714";
	public static final String SYMBOL_REDCROSS = EnumChatFormatting.RED+"\u2718";
	
	public static void combineStorage(ItemStack[] storage)
	{
		if (storage == null || storage.length == 0)
			return;
		
		for (int i = 0; i < storage.length; i++)
		{
			if (storage[i] == null || storage[i].getItem() instanceof IFood) continue;

			// cycle through the storage list in reverse.
			for (int j = storage.length - 1; j > i; j--)
			{
				if (storage[j] == null || storage[j].getItem() instanceof IFood) continue;
				
				// check if the 2nd storage item is equal to 1st storage item.
				if (OreDictionary.itemMatches(storage[i], storage[j], false))
				{
					// increase 1st storage item stack size.
					storage[i].stackSize += storage[j].stackSize;
					// clear 2nd storage slot.
					storage[j] = null;
				}
			}
		}		
	}
	
	public static String getCapacityInformation(int capacity)
	{
		if (capacity < 0) 
			return "";
		
		return StatCollector.translateToLocal("gui.capacity")+SEPARATOR_COLON+capacity+StatCollector.translateToLocal("gui.barrel.unit1");
	}
	
	public static String getBatteryChargeInformation(ItemStack is, boolean showDetailedUnits)
	{
		if (is == null || !(is.getItem() instanceof IBattery))
			return "";
		
		int maxChargeUnits = is.getMaxDamage();
		int curChargeUnits = is.getItemDamage();
		EnumChatFormatting color = WailaUtils.getPercentageColor(curChargeUnits, maxChargeUnits);
		
		if (showDetailedUnits)
		{
			int charge = Math.max(0, maxChargeUnits - curChargeUnits);
			
			return StatCollector.translateToLocal("gui.Battery.charge")+WailaUtils.SEPARATOR_COLON+color+charge+StatCollector.translateToLocal("gui.battery.unit1");
		}
		else
		{
			int charge = ((IBattery)is.getItem()).getCurrentCharge(is);

			return StatCollector.translateToLocal("gui.Battery.charge")+WailaUtils.SEPARATOR_COLON+color+charge+StatCollector.translateToLocal("gui.battery.unit1000");
		}
	}
	
	public static String getCountInformation(int count)
	{
		return StatCollector.translateToLocal("gui.count")+WailaUtils.SEPARATOR_COLON+count;
	}
	
	public static String getDamageInformation(ItemStack is)
	{
		if (!ModOptions.showDamageValues || is == null) 
			return "";
		
		if (is.getMaxDamage() <= 0)
			return "";
		
		String damageString = "";
		
		// gets the damage values
		float maxDamage = is.getMaxDamage()+1;
		float currentDamage = is.getItemDamage();
		float remaining = maxDamage-currentDamage;
		
		EnumChatFormatting color = getPercentageColor(currentDamage, maxDamage);
		damageString += EnumChatFormatting.WHITE+StatCollector.translateToLocal("gui.remaining")+color+" ("+(int)remaining+SEPARATOR_FSLASH+(int)maxDamage+")";
		
		return damageString;
	}

	public static String getDamageInformation(ItemStack is, boolean returnEmptyString)
	{
		if (is == null)
		{
			if (returnEmptyString)
				return StatCollector.translateToLocal("tooltip.empty");
			else
				return "";
		}

		if (is.getMaxDamage() <= 0)
			return "";
		
		String damageString = EnumChatFormatting.AQUA+is.getDisplayName();

		if (is.getItem() instanceof ItemMeltedMetal)
		{
			// special case for metal items, damage will be the number of metal units.
			int maxDamage = is.getMaxDamage()-1;	// should equal 100.
			int currentDamage = is.getItemDamage();
			int remaining = maxDamage-currentDamage;
			
			damageString += EnumChatFormatting.WHITE+" ("+remaining+SEPARATOR_FSLASH+maxDamage+")";
		}
		else if (is.getItem() instanceof ItemTuckerBag)
		{
			// gets the damage values
			float maxDamage = is.getMaxDamage()+1;
			float currentDamage = is.getItemDamage();
			float remaining = maxDamage-currentDamage;
			
			EnumChatFormatting color = getPercentageColor(currentDamage, maxDamage);
			damageString = EnumChatFormatting.WHITE+StatCollector.translateToLocal("gui.remaining")+color+" ("+(int)remaining+SEPARATOR_FSLASH+(int)maxDamage+")";
		}
		else if (ModOptions.showDamageValues) 
		{
			// gets the damage values
			int maxDamage = is.getMaxDamage()+1;
			int currentDamage = is.getItemDamage();
			int remaining = maxDamage-currentDamage;
			
			EnumChatFormatting color = getPercentageColor(currentDamage, maxDamage);
			damageString += color+" ("+remaining+SEPARATOR_FSLASH+maxDamage+")";
		}
		
		return damageString;
	}
	
	public static int getDamageColor(int currentDamage, int maxDamage)
	{
		float percent = 100 - (int)(currentDamage / maxDamage * 100.0D);
		
		if (percent < 50.0F) 
			return 16711680 + ((int)(255.0F * percent / 50.0F) << 8);

		return 65280 + ((int)(255.0F * (100.0F - (percent - 50.0F) * 2.0F) / 100.0F) << 16);
	}
	
	public static String getDryingInformation(ItemStack is)
	{
		if (is == null || !is.hasTagCompound()) 
			return "";
		
		if (is.stackTagCompound.hasKey(ModTags.TAG_DRYING_TAG))
		{
			NBTTagCompound tag = is.stackTagCompound.getCompoundTag(ModTags.TAG_DRYING_TAG);
			long dryingTicks = tag.getLong(ModTags.TAG_DRYING_COUNT);
			long driedTicks = Food.DRYHOURS * TFC_Time.HOUR_LENGTH;
			
			float percent = dryingTicks > driedTicks ? 100f : ((float)dryingTicks / (float)driedTicks) * 100.0f;
			if (percent <= 0)
				return "";
			
			return getProgressInformation((int)percent);
		}
		
		return "";
	}
	
	public static String getEntityInformation(ItemStack is)
	{
		if (is == null || !is.hasTagCompound())
			return "";

		// tucker bag entity
		if (is.stackTagCompound.hasKey(ModTags.TAG_TUCKER_BAG))
		{
			NBTTagCompound tag = is.stackTagCompound.getCompoundTag(ModTags.TAG_TUCKER_BAG);
			
			if (tag != null && tag.hasKey(ModTags.TAG_ENTITY_NAME))
			{
				return EnumChatFormatting.BLUE+tag.getString(ModTags.TAG_ENTITY_NAME);
			}
		}
		
		return "";
	}
	
	public static String getFluidInformation(FluidStack fs, int total, boolean includeFluidName)
	{
		if (fs == null || total < 0)
			return "";
		
		String fluidString = "";
		
		if (includeFluidName)
			fluidString += EnumChatFormatting.BLUE+fs.getFluid().getLocalizedName(fs)+" (";
		
		fluidString += String.valueOf(fs.amount);
		
		if (total > 0)
			fluidString += WailaUtils.SEPARATOR_FSLASH+String.valueOf(total);
		
		fluidString += StatCollector.translateToLocal("gui.barrel.unit1");
		
		if (includeFluidName)
			fluidString += ")";
		
		return fluidString;
	}
	
	public static String getFoodInformation(ItemStack is, boolean includeDecay)
	{
		if (is == null || !is.hasTagCompound() || !(is.getItem() instanceof IFood)) 
			return "";
		
		String foodString = EnumChatFormatting.AQUA+is.getDisplayName();
		
		NBTTagCompound tag = is.getTagCompound();
		
		if (tag.hasKey(ModTags.TAG_FOOD_WEIGHT) && tag.getFloat(ModTags.TAG_FOOD_WEIGHT) != 999)
		{
			float weight = roundNumber(tag.getFloat(ModTags.TAG_FOOD_WEIGHT), 100);
			
			if (weight > 0)
			{
				float decay = 0;
				
				if (tag.hasKey(ModTags.TAG_FOOD_DECAY))
				{
					decay = tag.getFloat(ModTags.TAG_FOOD_DECAY);
				}
				
				float percent = roundNumber(decay / weight * 100, 10);
				EnumChatFormatting color = getPercentageColor(100 - percent);
				
				foodString += color+" ("+weight+StatCollector.translateToLocal("gui.food.unit");

				if (decay > 0 && includeDecay)
					foodString += EnumChatFormatting.RESET+SEPARATOR_DASH+EnumChatFormatting.DARK_GRAY+percent+StatCollector.translateToLocal("gui.decay.unit");
				
				foodString += color+")";
			}
		}
		
		return foodString;
	}
	
	public static List<String> getFoodInformation(ItemStack is)
	{
		if (is == null || !is.hasTagCompound() || !(is.getItem() instanceof IFood)) 
			return new ArrayList<String>();
		
		List<String> foodStrings = new ArrayList<String>();
		
		NBTTagCompound tag = is.getTagCompound();
		
		if (tag.hasKey(ModTags.TAG_FOOD_WEIGHT) && tag.getFloat(ModTags.TAG_FOOD_WEIGHT) != 999)
		{
			float foodWeight = roundNumber(tag.getFloat(ModTags.TAG_FOOD_WEIGHT), 100);
			
			if (foodWeight > 0)
			{
				foodStrings.add(StatCollector.translateToLocal("gui.food.amount")+" "+foodWeight+" "+StatCollector.translateToLocal("gui.food.unit"));
								
				if (tag.hasKey(ModTags.TAG_FOOD_DECAY))
				{
					float foodDecay = tag.getFloat(ModTags.TAG_FOOD_DECAY);
					if (foodWeight > 0 && foodDecay > 0)
					{
						float decayPercentage = foodDecay/foodWeight;
						foodStrings.add(EnumChatFormatting.DARK_GRAY+StatCollector.translateToLocal("gui.food.decay")+" "+roundNumber(decayPercentage*100, 10)+StatCollector.translateToLocal("gui.decay.unit"));
					}
				}
			}
		}
		
		return foodStrings;
	}

	public static List<String> getMetalInformation(ItemStack is, boolean useNameMethod, boolean additionalInformation)
	{
		if (is == null || !(is.getItem() instanceof ISmeltable))
			return new ArrayList<String>();
		
		List<String> metalStrings = new ArrayList<String>();
		
		Metal metal = ((ISmeltable)is.getItem()).getMetalType(is);
		String metalString = getMetalInformation(metal, useNameMethod);
		
		if (metalString != "")
		{
			if (additionalInformation)
			{
				if (metal == Global.PLATINUM || 
					metal == Global.LEAD)
					metalString += WailaUtils.SEPARATOR_DASH+StatCollector.translateToLocal("gui.useless");
			}

			metalStrings.add(metalString);
		}

		int units = ((ISmeltable)is.getItem()).getMetalReturnAmount(is);
		String unitsString = WailaUtils.getUnitsInformation(units);
		if (unitsString != "")
			metalStrings.add(unitsString);

		return metalStrings;
	}
	
	public static String getMetalInformation(Metal metal, boolean useNameMethod)
	{
		if (metal == null)
			return "";
		
		String metalName = "";
		if (useNameMethod)
		{
			metalName = getMetalName(metal, true);
		}
		else
		{
			String name = metal.name.replace(" ", "");
			metalName = StatCollector.translateToLocal("gui.metal."+name);			
		}
		
		if (metalName == "")
			return "";
		
		return EnumChatFormatting.DARK_AQUA+metalName;
	}
	
	public static String getMetalName(Metal metal, boolean handleIron)
	{
		if (handleIron && metal.name == "Pig Iron")
			return StatCollector.translateToLocal("gui.metal.Iron");
		
		String name = metal.name.replace(" ", "");
		return StatCollector.translateToLocal("gui.metal."+name);
	}
	
	public static String getMetalNameUnlocalized(Metal metal, boolean handleIron)
	{
		if (handleIron && metal.name == "Pig Iron")
			return "gui.metal.Iron";
		
		String name = metal.name.replace(" ", "");
		return "gui.metal."+name;
	}
	
	public static List<String> getMilkInformation(ItemStack is)
	{
		if (is == null || !is.hasTagCompound()) 
			return new ArrayList<String>();
		
		List<String> foodStrings = new ArrayList<String>();
		
		NBTTagCompound tag = is.getTagCompound();
		
		if (tag.hasKey(ModTags.TAG_FOOD_WEIGHT) && tag.getFloat(ModTags.TAG_FOOD_WEIGHT) != 999)
		{
			float foodWeight = roundNumber(tag.getFloat(ModTags.TAG_FOOD_WEIGHT), 100);
			
			if (foodWeight > 0)
			{
				foodStrings.add(StatCollector.translateToLocal("gui.food.amount")+" "+foodWeight+" "+StatCollector.translateToLocal("gui.food.unit"));
				
				if (tag.hasKey(ModTags.TAG_FOOD_DECAY))
				{
					float foodDecay = tag.getFloat(ModTags.TAG_FOOD_DECAY);
					if (foodWeight > 0 && foodDecay > 0)
					{
						float decayPercentage = foodDecay/foodWeight;
						
						String decayState = EnumChatFormatting.DARK_GRAY+StatCollector.translateToLocal("gui.milk.fresh");
						if (decayPercentage > 50)
							decayState = EnumChatFormatting.DARK_GRAY+StatCollector.translateToLocal("gui.milk.old");
						if (decayPercentage > 70)
							decayState = EnumChatFormatting.DARK_GRAY+StatCollector.translateToLocal("gui.milk.sour");
	
						foodStrings.add(decayState);
					}
				}
			}
		}
		
		return foodStrings;
	}

	public static EnumChatFormatting getPercentageColor(float value, float total)
	{
		if (total > 0)
		{
			float percent = 100F - (value / total * 100.0F);
			
			return getPercentageColor(percent);
		}
		
		return EnumChatFormatting.GREEN;
	}
	
	public static EnumChatFormatting getPercentageColor(int value, int total)
	{
		if (total > 0)
		{
			float percent = 100F - ((float)value / (float)total * 100.0F);
			
			return getPercentageColor(percent);
		}
		
		return EnumChatFormatting.GREEN;
	}

	public static EnumChatFormatting getPercentageColor(long value, long total)
	{
		if (total > 0)
		{
			float percent = 100F - ((float)value / (float)total * 100.0F);
			
			return getPercentageColor(percent);
		}
		
		return EnumChatFormatting.GREEN;
	}
	
	public static EnumChatFormatting getPercentageColor(float percent)
	{
		if (percent < 15)
			return EnumChatFormatting.DARK_RED;
		else if (percent < 34)
			return EnumChatFormatting.RED;
		else if (percent < 50)
			return EnumChatFormatting.GOLD;
		else if (percent < 67)
			return EnumChatFormatting.YELLOW;
		else if (percent < 80)
			return EnumChatFormatting.DARK_GREEN;
		
		return EnumChatFormatting.GREEN;
	}
	
	public static String getProgressInformation(int percent)
	{
		if (percent <= 0 || percent > 100)
			return "";
		
		return StatCollector.translateToLocal("gui.progress")+SEPARATOR_COLON+String.valueOf(percent)+StatCollector.translateToLocal("gui.progress.unit");
	}
	
	public static String getRecipeProcessInformation(Recipe recipe)
	{
		if (recipe == null)
			return "";

		return recipe.getLocalizedProcessingMessage();
	}
	
	public static String getRecipeProcessInformation(Recipe recipe, int percent)
	{
		if (recipe == null)
			return "";

		return recipe.getLocalizedProcessingMessage()+(percent > 0 && percent <= 100 ? " ("+String.valueOf(percent)+"%)" : "");
	}
	
	public static String getRecipeResultInformation(Recipe recipe, boolean includePrefix)
	{
		if (recipe == null)
			return "";
		
		return recipe.getLocalizedResultName(includePrefix);
	}
	
	public static String getRemainingInformation(long remaining, String unitString, int percent)
	{
		if (unitString == null)
			unitString = "";
		
		String result = "";
		
		if (remaining < 0)
			result = EnumChatFormatting.WHITE+StatCollector.translateToLocal("gui.remaining")+SEPARATOR_COLON+EnumChatFormatting.RED+remaining+" "+unitString;
		else
			result = EnumChatFormatting.WHITE+StatCollector.translateToLocal("gui.remaining")+SEPARATOR_COLON+EnumChatFormatting.GREEN+remaining+" "+unitString;

		if (percent > 0)
			result += EnumChatFormatting.RESET+" ("+String.valueOf(percent)+StatCollector.translateToLocal("gui.progress.unit")+")";
			
		return result;
	}
	
	public static String getStackInformation(ItemStack is)
	{
		if (is == null) 
			return "";
		
		String result = "";
		
		result = getFoodInformation(is, true);
		if (result != "")
			return result;
				
		if (!(is.getItem() instanceof ItemPotteryMold))
		{
			result = getDamageInformation(is, false);
			if (result != "")
				return result;
		}
		
		result = EnumChatFormatting.AQUA+is.getDisplayName();
		if (is.stackSize > 0)
			result += EnumChatFormatting.WHITE+" ("+is.stackSize+")";
		
		return result;
	}

	public static String getTasteFactorInformation(ItemStack is, EntityPlayer player)
	{
		if (is == null || player == null) 
			return "";
		
		float tasteFactor = TFC_Core.getPlayerFoodStats(player).getTasteFactor(is);		
		return EnumChatFormatting.DARK_BLUE+StatCollector.translateToLocal("gui.taste.factor")+WailaUtils.SEPARATOR_COLON+EnumChatFormatting.WHITE+roundNumber(tasteFactor, 100);
	}

	public static String getUnitsInformation(int units)
	{
		return getUnitsInformation(units, StatCollector.translateToLocal("gui.units"));
	}

	public static String getUnitsInformation(int units, String unitString)
	{
		if (units <= 0) 
			return "";

		return unitString+WailaUtils.SEPARATOR_COLON+units;
	}

	public static String getUnitsInformation(float units, String unitString)
	{
		if (units <= 0) 
			return "";

		return unitString+WailaUtils.SEPARATOR_COLON+units;
	}

	public static String getUnitsInformation(ItemStack is, boolean showDetailedUnits)
	{
		if (is == null || !is.hasTagCompound())
			return "";
		
		NBTTagCompound tag = is.stackTagCompound;
		
		if (tag.hasKey(ModTags.TAG_UNITS))
		{
			float units = tag.getFloat(ModTags.TAG_UNITS);
			
			if (showDetailedUnits)
				return getUnitsInformation(units, StatCollector.translateToLocal("gui.units"));
			else
				return getUnitsInformation((int)units, StatCollector.translateToLocal("gui.units"));
		}
		
		if (tag.hasKey(ModTags.TAG_ANODISING_UNITS))
		{
			float anodisingUnits = tag.getFloat(ModTags.TAG_ANODISING_UNITS);
			
			if (showDetailedUnits)
				return getUnitsInformation(anodisingUnits, StatCollector.translateToLocal("gui.units"));
			else
				return getUnitsInformation((int)anodisingUnits, StatCollector.translateToLocal("gui.units"));
		}
		
		if (tag.hasKey(ModTags.TAG_UNITS_SILVER))
		{
			float units = tag.getFloat(ModTags.TAG_UNITS_SILVER);
			
			if (showDetailedUnits)
				return getUnitsInformation(units, StatCollector.translateToLocal("gui.silver.units"));
			else
				return getUnitsInformation((int)units, StatCollector.translateToLocal("gui.silver.units"));
		}
		
		if (tag.hasKey(ModTags.TAG_UNITS_NICKLE))
		{
			float units = tag.getFloat(ModTags.TAG_UNITS_NICKLE);
			
			if (showDetailedUnits)
				return getUnitsInformation(units, StatCollector.translateToLocal("gui.nickel.units"));
			else
				return getUnitsInformation((int)units, StatCollector.translateToLocal("gui.nickel.units"));
		}

		return "";
	}

	public static void populateStorageItems(NBTTagCompound tag, ItemStack[] storage)
	{
		populateStorageItems(tag, storage, ModTags.TAG_STORAGE_ITEMS, ModTags.TAG_STORAGE_SLOT);
	}
	
	public static void populateStorageItems(NBTTagCompound tag, ItemStack[] storage, String tagListName, String slotName)
	{
		if (tag == null || !tag.hasKey(tagListName) || storage == null || storage.length == 0)
			return;
		
		NBTTagList itemsTag = tag.getTagList(tagListName, 10);
		
		for (int i = 0; i < itemsTag.tagCount(); i++)
		{
			NBTTagCompound slotTag = itemsTag.getCompoundTagAt(i);
			if (slotTag == null || !slotTag.hasKey(slotName)) continue;
			
			byte slot = slotTag.getByte(slotName);
			
			if (slot >= 0 && slot < storage.length)
				storage[slot] = ItemStack.loadItemStackFromNBT(slotTag);
		}
	}
	
	public static float roundNumber(float input, float rounding)
	{
		int o = (int)(input * rounding);
		return o / rounding;
	}
}
