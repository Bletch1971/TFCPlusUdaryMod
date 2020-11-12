package udary.tfcplusudarymod.core.recipes;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import udary.common.Recipe;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;

public class DryingMatRecipe extends Recipe
{
	protected DryingMatRecipe()
	{
		super();
	}
	
	/**
	 * Gets the item stack used as the input for the recipe.
	 * @return an ItemStack object that contains the item used for the recipe input.
	 */
	public ItemStack getInputItemStack()
	{
		return this.inputISList.size() == 1 ? this.inputISList.get(0) : null;
	}
	
	/**
	 * Gets the amount of time the recipe requires to complete based of the weight of the item.
	 * @param inputIS The item stack used to calculate the duration of the recipe.
	 */
	public int getRecipeDuration(ItemStack inputIS)
	{
		return this.recipeDuration;
	}
	
	/**
	 * Gets the result item stack of the recipe.
	 * @param inputIS The item stack used with the recipe.
	 */
	public RecipeResult getRecipeResult(ItemStack inputIS)
	{
		return null;
	}
	
	/**
	 * Match a recipe by it's input components.
	 * @param inputIS The ItemStack object that contains the input item for the recipe.
	 * @param matchType The type of match to be performed. 
	 * @return True if the recipe was matched; otherwise false.
	 */
	public boolean matches(ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputIS == null)
			return false;
		
		return super.matches(null, inputIS != null ? Arrays.asList(inputIS) : null, matchType);
	}
}
