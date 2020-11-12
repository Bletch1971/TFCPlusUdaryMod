package udary.tfcplusudarymod.core.recipes;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import udary.common.Recipe;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;

public class OreCookerRecipe extends Recipe
{
	public static final int COOK_TEMP_MINIMUM = 1;
	
	protected int minimumTemperature;
	
	public OreCookerRecipe(ItemStack inputIS, ItemStack outputIS, int recipeDuration, int minimumTemperature)
	{
		super();

		if (inputIS != null)
			addInputItemStack(inputIS);
		if (outputIS != null)
			setOutputItemStack(outputIS);
		setRecipeDuration(recipeDuration);
		setMinimumTemperature(minimumTemperature);
	}
	
	@Override
	public boolean isRecipeValid()
	{
		return 	this.inputISList.size() == 1 && this.inputISList.get(0).stackSize > 0 && 
				this.outputIS != null && this.outputIS.stackSize > 0;
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
	 * Gets the minimum temperature the recipe requires.
	 */
	public int getMinimumTemperature()
	{
		return this.minimumTemperature;
	}
	
	@Override
	public ItemStack getOutputItemStack()
	{
		return this.outputIS;
	}
	
	@Override
	public int getRecipeDuration()
	{
		return this.recipeDuration;
	}
	
	/**
	 * Gets the result item stack of the recipe.
	 * @param inputFS The item stack used with the recipe.
	 * @param adjustInputs True to adjust the input component arguments.
	 * @return an item stack list that contains the results of the recipe.
	 */
	public RecipeResult getRecipeResult(ItemStack inputIS, boolean adjustInputs)
	{
		// check the parameters are valid and the input components match the recipe.
		if (inputIS == null || !matches(inputIS, EnumRecipeMatchType.MINIMUM))
			return null;
		
		RecipeResult result = new RecipeResult();
		
		// check if there is an output item stack.
		if (this.outputIS != null)
		{
			// calculate the number of output items to create.
			int outputTotal = inputIS.stackSize;
		
			for (int count = 0; count < outputTotal; count++)
			{
				// make a copy of the recipe's output item stack.
				result.addOutputItemStack(this.outputIS.copy());
			}
			
			// check if we need to adjust the input components.
			if (adjustInputs)
				// reduce the stack size by the total number used for the recipe.
				inputIS.stackSize -= outputTotal;
		}		
		
		return result;
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
	
	/**
	 * Sets the minimum temperature the recipe requires.
	 * @param minimumTemperature The minimum temperature for the recipe.
	 */
	public OreCookerRecipe setMinimumTemperature(int minimumTemperature)
	{
		this.minimumTemperature = Math.max(minimumTemperature, COOK_TEMP_MINIMUM);
		return this;
	}
}
