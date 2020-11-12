package udary.tfcplusudarymod.core.recipes;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Interfaces.IFood;

public class DryingMatFoodRecipe extends DryingMatRecipe
{
	// Food drying hours = 4 @ 1000 ticks per hour = 4000 ticks to dry 160oz food, that means 25 ticks per 1oz of food.
	public static final int WEIGHT_DURATION_MODIFIER = 25;
	// Minimum duration set for 10oz food weight.
	public static final int MINIMUM_DURATION = WEIGHT_DURATION_MODIFIER * 10;	
	public static final float MINIMUM_TASTE_MODIFIER = 0.1f;
	public static final float MINIMUM_WEIGHT_MODIFIER = 0.1f;
	
	protected float driedTasteModifier;
	protected float driedWeightModifier;

	public DryingMatFoodRecipe(ItemStack inputIS, float driedWeightModifier, float driedTasteModifier)
	{
		super();

		if (inputIS != null && inputIS.getItem() instanceof IFood)
		{
			if (!ModTags.hasTag(inputIS, ModTags.TAG_FOOD_WEIGHT))
				inputIS = ItemFoodTFC.createTag(inputIS, Global.FOOD_MAX_WEIGHT);
			
			addInputItemStack(inputIS);
		}
		setDriedTasteModifier(driedTasteModifier);
		setDriedWeightModifier(driedWeightModifier);	
	}
	
	@Override
	public boolean isRecipeValid()
	{
		return 	this.inputISList.size() == 1 && 
				this.inputISList.get(0).getItem() instanceof IFood;
	}

	/**
	 * Gets the taste modifier when dried the recipe requires.
	 */
	public float getDriedTasteModifier()
	{
		return this.driedTasteModifier;
	}

	/**
	 * Gets the weight modifier when dried the recipe requires.
	 */
	public float getDriedWeightModifier()
	{
		return this.driedWeightModifier;
	}

	@Override
	public String getLocalizedResultName(boolean includePrefix)
	{
		String name = this.unlocalizedResultName;
	
		// check if we have a name defined.
		if (name != null && !name.trim().equalsIgnoreCase(""))
			// name defined, return the localized name.
			return (includePrefix ? StatCollector.translateToLocal("gui.result")+WailaUtils.SEPARATOR_COLON : "")+name;
		
		// name not defined, return the default instead.
		name = "";
		
		if (this.inputISList != null && this.inputISList.size() == 1)
			name = StatCollector.translateToLocal("word.dried")+" "+this.inputISList.get(0).getDisplayName();
		
    	if (name != null && !name.trim().equalsIgnoreCase(""))
    		return (includePrefix ? StatCollector.translateToLocal("gui.result")+WailaUtils.SEPARATOR_COLON : "")+name;
    	return "";
	}

	@Override
	public int getRecipeDuration(ItemStack inputIS)
	{
		if (inputIS == null || !(inputIS.getItem() instanceof IFood))
			return 0;
		
		return Math.max((((int)Food.getWeight(inputIS)) + 1) * WEIGHT_DURATION_MODIFIER, MINIMUM_DURATION);
	}
	
	/**
	 * Gets the result item stack of the recipe.
	 * @param inputFS The item stack used with the recipe.
	 */
	public RecipeResult getRecipeResult(ItemStack inputIS)
	{
		// check the parameters are valid and the input components match the recipe.
		if (inputIS == null || !matches(inputIS, EnumRecipeMatchType.MINIMUM))
			return null;
		
		RecipeResult result = new RecipeResult();
		
		// make a copy of the input item.
		ItemStack resultIS = inputIS.copy();
		
		// set dried tag.
		Food.setDried(resultIS, Food.DRYHOURS);
		
		// adjust the food profile.
		int[] foodProfile = Food.getFoodTasteProfile(resultIS);
		for (int i = 0; i < foodProfile.length; i++)
		{
			foodProfile[i] *= getDriedTasteModifier();
		}
		
		// update the food profile.
		Food.setSweetMod(resultIS, foodProfile[0]);
		Food.setSourMod(resultIS, foodProfile[1]);
		Food.setSaltyMod(resultIS, foodProfile[2]);
		Food.setBitterMod(resultIS, foodProfile[3]);
		Food.setSavoryMod(resultIS, foodProfile[4]);
		
		// adjust the food weight.
		float weight = Food.getWeight(resultIS);
		weight *= getDriedWeightModifier();
		Food.setWeight(resultIS, weight);
		
		// adjust the decay modifier.
		float decay = Food.getDecay(resultIS);
		if (decay > 0)
			decay *= getDriedWeightModifier();
		Food.setDecay(resultIS, decay);
		
		result.addOutputItemStack(resultIS);
		
		return result;
	}
	
	protected boolean matches(List<FluidStack> inputFSList, List<ItemStack> inputISList, EnumRecipeMatchType matchType)
	{
		if (inputFSList != null)
			return false;
		
		// check if all components are null or empty.
		if ((inputISList == null || inputISList.size() == 0))
			return false;
		
		// check if only one stack list is null or empty.
		if ((inputISList == null || inputISList.size() == 0) && (this.inputISList != null && this.inputISList.size() > 0) || 
			(inputISList != null && inputISList.size() > 0) && (this.inputISList == null || this.inputISList.size() == 0))
			return false;

		// check if all the items are included.
		if (inputISList != null && this.inputISList != null)
		{
			// create a temporary list of input items.
			List<ItemStack> tempISList = new ArrayList<ItemStack>(this.inputISList);
			
			for (ItemStack inputIS : inputISList)
			{
				if (!(inputIS.getItem() instanceof IFood) || Food.isDried(inputIS))
					return false;
				
				boolean matched = false;
				
				for (ItemStack recipeIS : tempISList)
				{
					if (/*recipeIS.isItemEqual(inputIS) ||*/ OreDictionary.itemMatches(recipeIS, inputIS, false))
					{
						if ((matchType == EnumRecipeMatchType.BASIC) ||
							(matchType == EnumRecipeMatchType.MINIMUM && inputIS.stackSize >= recipeIS.stackSize) ||
							(matchType == EnumRecipeMatchType.EXACT && inputIS.stackSize == recipeIS.stackSize))
						{
							// item matched, remove from temp list and exit.
							tempISList.remove(recipeIS);
							
							matched = true;
							break;
						}
					}
				}
				
				// check if the item was matched.
				if (!matched)
					return false;
			}
			
			// check if any input items were not matched.
			if (tempISList.size() > 0)
				return false;
		}
		
		return true;
	}
	
	/**
	 * Sets the taste modifier when dried the recipe requires.
	 */
	public DryingMatRecipe setDriedTasteModifier(float driedTasteModifier)
	{
		this.driedTasteModifier = Math.max(driedTasteModifier, MINIMUM_TASTE_MODIFIER);
		return this;
	}
	
	/**
	 * Sets the weight modifier when dried the recipe requires.
	 */
	public DryingMatRecipe setDriedWeightModifier(float driedWeightModifier)
	{
		this.driedWeightModifier = Math.max(driedWeightModifier, MINIMUM_WEIGHT_MODIFIER);
		return this;
	}
}
