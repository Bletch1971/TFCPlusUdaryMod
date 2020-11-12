package udary.tfcplusudarymod.core.recipes;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import udary.common.Recipe;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;

public class EvaporatorPanRecipe extends Recipe
{
	public EvaporatorPanRecipe(FluidStack inputFS, ItemStack outputIS)
	{
		super();
		
		if (inputFS != null)
			addInputFluidStack(inputFS);
		if (outputIS != null)
			setOutputItemStack(outputIS);
	}

	@Override
	public boolean isRecipeValid()
	{
		return 	inputFSList.size() == 1 && inputFSList.get(0).amount > 0 && 
				outputIS != null && outputIS.stackSize > 0;
	}

	/**
	 * Gets the fluid used as the input for the recipe.
	 * @return a Fluid object that contains the input fluid for the recipe.
	 */
	public Fluid getInputFluid()
	{
		return inputFSList.size() == 1 ? inputFSList.get(0).getFluid() : null;
	}

	/**
	 * Gets the amount of fluid used as the input for the recipe.
	 * @return an integer value indicating the fluid amount.
	 */
	public int getInputFluidAmount()
	{
		return inputFSList.size() == 1 ? inputFSList.get(0).amount : 0;
	}

	/**
	 * Gets the resulting item stack for the recipe.
	 * @return A ItemStack object that contains the result of the recipe.
	 */
	@Override
	public ItemStack getOutputItemStack()
	{
		return outputIS;
	}
	
	/**
	 * Gets the result item stack of the recipe.
	 * @param inputFS The fluid stack used with the recipe.
	 * @param adjustInputs True to adjust the input component arguments.
	 */
	public RecipeResult getRecipeResult(FluidStack inputFS, boolean adjustInputs)
	{
		// check the parameters are valid and the input components match the recipe.
		if (inputFS == null || !matches(inputFS, EnumRecipeMatchType.MINIMUM))
			return null;
		
		RecipeResult result = new RecipeResult();
		
		// check if there is an output item stack.
		if (outputIS != null)
		{
			// calculate the number of output items to create.
			int outputTotal = inputFS.amount / this.getInputFluidAmount();
		
			for (int count = 0; count < outputTotal; count++)
			{
				// make a copy of the recipe's output item stack.
				result.addOutputItemStack(outputIS.copy());
			}
			
			// check if we need to adjust the input components.
			if (adjustInputs)
				// reduce the fluid amount by the total fluid amount used for the recipe.
				inputFS.amount = Math.max(inputFS.amount - (outputTotal * this.getInputFluidAmount()), 0);
		}		
		
		return result;
	}

	/**
	 * Match a recipe by it's input components.
	 * @param inputFS The FluidStack object that contains the input fluid for the recipe.
	 * @param matchType The type of match to be performed. 
	 * @return True if the recipe was matched; otherwise false.
	 */
	public boolean matches(FluidStack inputFS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null)
			return false;
		
		return super.matches(Arrays.asList(inputFS), null, matchType);
	}
}
