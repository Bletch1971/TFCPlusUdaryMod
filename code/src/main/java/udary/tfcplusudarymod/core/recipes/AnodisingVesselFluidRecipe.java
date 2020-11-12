package udary.tfcplusudarymod.core.recipes;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import udary.common.Recipe;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;

public class AnodisingVesselFluidRecipe extends Recipe
{
	public AnodisingVesselFluidRecipe(FluidStack inputFS, ItemStack inputIS, FluidStack outputFS)
	{
		super();

		if (inputFS != null)
			addInputFluidStack(inputFS);
		if (inputIS != null)
			addInputItemStack(inputIS);
		if (outputFS != null)
			setOutputFluidStack(outputFS);		
	}

	@Override
	public boolean isRecipeValid()
	{
		return 	inputFSList.size() == 1 && inputFSList.get(0).amount > 0 && 
				inputISList.size() == 1 && inputISList.get(0).stackSize > 0 &&
				outputFS != null && outputFS.amount > 0;
	}	
	
	/**
	 * Gets the result fluid/item stacks of the recipe.
	 * @param inputFS The fluid stack used with the recipe.
	 * @param inputIS The item Stack that contains the input item for the recipe.
	 * @param adjustInputs True to adjust the input component arguments.
	 */
	public RecipeResult getRecipeResult(FluidStack inputFS, ItemStack inputIS, boolean adjustInputs)
	{
		// check the parameters are valid and the input components match the recipe.
		if (inputFS == null || !matches(inputFS, inputIS, EnumRecipeMatchType.MINIMUM))
			return null;
		
		RecipeResult result = new RecipeResult();
		
		// check if there is an output fluid stack defined.
		if (outputFS != null)
		{
			// get the recipe and input stack values.
			int recipeFluidAmount = this.inputFSList.get(0).amount;
			int recipeItemAmount = this.inputISList.get(0).stackSize;
			
			int inputFluidAmount = inputFS.amount;
			int inputItemAmount = inputIS.stackSize;
			
			// get the amount of fluid overflow for the input fluid stack.
			double fluidOverflowAmount = inputFluidAmount % recipeFluidAmount;
			
			// check if there is fluid overflow (for recipe to complete the input fluid amount must be an integer multiple of the recipe fluid amount).
			if (fluidOverflowAmount == 0)
			{
				// no fluid overflow, calculate the number of recipe runs.
				int recipeRuns = inputFluidAmount / recipeFluidAmount;
				
				// calculate the amount of items required to complete the recipe based on the number of runs.
				int requiredItemAmount = recipeItemAmount * recipeRuns;
				
				// check if enough input items.
				if (requiredItemAmount <= inputItemAmount)
				{
					// create a copy of the output fluid and set the fluid amount.
					FluidStack resultFS = outputFS.copy();
					resultFS.amount *= recipeRuns;
					
					// set the result fluid.
					result.setOutputFluidStack(resultFS);
					
					if (adjustInputs)
					{
						// reduce the input fluid amount.
						inputFS.amount -= resultFS.amount;
						
						// reduce the input item amount.
						inputIS.stackSize -= requiredItemAmount;
					}
				}				
			}
		}
		
		return result;
	}
	
	/**
	 * Gets the result fluid/item stacks of the recipe.
	 * @param inputFS The fluid stack used with the recipe.
	 * @param inputIS The item Stack that contains the input item for the recipe.
	 */
	public boolean matches(FluidStack inputFS, ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null || inputIS == null)
			return false;

		return super.matches(Arrays.asList(inputFS), Arrays.asList(inputIS), matchType);
	}
}
