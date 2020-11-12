package udary.tfcplusudarymod.core;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Interfaces.IFood;

public class ModTags 
{
	public static final String TAG_PROCESSING = "Processing";
	public static final String TAG_PROGRESS = "Progress";
	public static final String TAG_TEMPERATURE = "Temperature";
	public static final String TAG_UNITS = "Units";
	public static final String TAG_UNITS_NICKLE = "NickleUnits";
	public static final String TAG_UNITS_SILVER = "SilverUnits";

	public static final String TAG_ANODISING_START = "AnodisingStart";
	public static final String TAG_ANODISING_UNITS = "AnodisingUnits";
	
	public static final String TAG_COOKING_COUNT = "CookingCount";
	
	public static final String TAG_DRYING_TAG = "DryingNBT";
	public static final String TAG_DRYING_COUNT = "DryingCount";

	public static final String TAG_EVAPORATION_COUNT = "EvaporationCount";

	public static final String TAG_FOOD_PROCESSING_TAG = "Processing Tag";
	public static final String TAG_FOOD_WEIGHT = "foodWeight";
	public static final String TAG_FOOD_DECAY = "foodDecay";
	public static final String TAG_DECAY_TIMER = "decayTimer";
	
	public static final String TAG_FLUID_AMOUNT = "Amount";
	public static final String TAG_FLUID_ID = "fluidID";
	public static final String TAG_FLUID_TAG = "fluidNBT";
	public static final String TAG_SEALED = "Sealed";
	public static final String TAG_SEALTIME = "SealTime";
	
	public static final String TAG_STORAGE_ITEMS = "Items";
	public static final String TAG_STORAGE_SLOT = "Slot";
	
	public static final String TAG_TUCKER_BAG = "tuckerBag";
	public static final String TAG_ENTITY_CLASS = "entityClass";
	public static final String TAG_ENTITY_NAME = "entityName";
	public static final String TAG_ENTITY_TAG = "entityNBT";
		
	public static NBTTagCompound applyEmptyTag(ItemStack is)
	{
		if (is == null)
			return null;
		
		if(!is.hasTagCompound())
			is.setTagCompound(new NBTTagCompound());	
		
		return is.stackTagCompound;
	}
	
	public static NBTTagCompound applyDryingTag(ItemStack is)
	{
		if (is == null)
			return null;
		
		if (is.getItem() instanceof IFood && Food.isDried(is))
			return null;
		
		if(!is.hasTagCompound())
			is.setTagCompound(new NBTTagCompound());	
		
		if (!is.stackTagCompound.hasKey(TAG_DRYING_TAG))
			is.stackTagCompound.setTag(TAG_DRYING_TAG, createDryingMatCountTagCompound(0));
		
		return is.stackTagCompound.getCompoundTag(TAG_DRYING_TAG);
	}
	
	public static ItemStack applyFoodTag(ItemStack is, float weight)
	{
		if (is == null)
			return is;
		
		return ItemFoodTFC.createTag(is, weight, 0);
	}
	
	public static ItemStack applyFoodTag(ItemStack is, float weight, float decay)
	{
		if (is == null)
			return is;
		
		return ItemFoodTFC.createTag(is, weight, decay);
	}
	
	public static void clearTag(ItemStack is, String tagName, boolean clearTagIfEmpty)
	{
		if (is == null)
			return;
		
		if (is.hasTagCompound() && is.stackTagCompound.hasKey(tagName))
			is.stackTagCompound.removeTag(tagName);
		
		if (clearTagIfEmpty)
			clearTagIfEmpty(is);
	}

	public static void clearTagIfEmpty(ItemStack is)
	{
		if (is == null)
			return;
		
		if(is.hasTagCompound() && is.stackTagCompound.hasNoTags())
			is.stackTagCompound = null;
	}
	
	public static NBTTagCompound createAnodisingVesselProcessTagCompound(long anodisingStart)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong(TAG_ANODISING_START, anodisingStart);
		return nbt;		
	}
	
	public static NBTTagCompound createAnodisingVesselUnitsCompound(float anodisingUnits)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setFloat(TAG_ANODISING_UNITS, anodisingUnits);
		return nbt;	
	}
	
	public static NBTTagCompound createDryingMatCountTagCompound(long dryingCount)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		nbt.setLong(TAG_DRYING_COUNT, dryingCount);
		return nbt;	
	}
	
	public static NBTTagCompound createDryingMatProcessTagCompound(ItemStack[] storage)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		TFC_Core.writeInventoryToNBT(nbt, storage);
		return nbt;		
	}
	
	public static NBTTagCompound createEvaporationPanProcessTagCompound(int evaporationCount, ItemStack[] storage)
	{
		NBTTagCompound nbt = new NBTTagCompound();
		TFC_Core.writeInventoryToNBT(nbt, storage);
		nbt.setInteger(TAG_EVAPORATION_COUNT, evaporationCount);
		return nbt;		
	}
	
	public static NBTTagCompound createOreCookerProcessTagCompound(int cookingCount, int temperature, ItemStack[] storage)
	{
		NBTTagCompound nbt = new NBTTagCompound();		
		TFC_Core.writeInventoryToNBT(nbt, storage);
		nbt.setInteger(ModTags.TAG_COOKING_COUNT, cookingCount);
		nbt.setInteger(ModTags.TAG_TEMPERATURE, temperature);
		return nbt;		
	}

	public static boolean hasTag(ItemStack is, String tagName)
	{
		if (is == null)
			return false;
		
		if (!is.hasTagCompound() || !is.stackTagCompound.hasKey(tagName))
			return false;
		
		return true;
	}
}
