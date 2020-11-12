package udary.common;

import java.util.Stack;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

public class RecipeResult 
{
	protected FluidStack outputFS;
	protected Stack<ItemStack> outputISList;
	
	public RecipeResult()
	{
		outputFS = null;
		outputISList = new Stack<ItemStack>();
	}
	
	public RecipeResult(FluidStack outputFS, Stack<ItemStack> outputISList)
	{
		this();
		
		this.outputFS = outputFS;
		this.outputISList = outputISList;
	}
	   
    /**
     * Adds a resulting item stack for the recipe.
     * @param is The ItemStack object that contains the result of the recipe.
     */
    public RecipeResult addOutputItemStack(ItemStack is)
    {
    	if (is != null)
    		this.outputISList.push(is);
    	return this;
    }

	/**
	 * Gets the resulting fluid stack for the recipe.
	 * @return A FluidStack object that contains the result of the recipe.
	 */
	public FluidStack getOutputFluidStack()
	{
		return this.outputFS;
	}
	
	/**
	 * Gets the stack of resulting item stacks for the recipe.
	 * @return A Stack of ItemStack objects that contain the results of the recipe.
	 */
	public Stack<ItemStack> getOutputItemStacks()
	{
		return this.outputISList;
	}
	
	/**
	 * Returns the number of resulting item stacks.
	 */
	public int getOutputItemCount()
	{
		if (this.outputISList == null)
			return 0;
		return this.outputISList.size();
	}
	
	/**
	 * Sets the resulting fluid stack for the recipe.
	 * @param fs The resulting fluid stack when the recipe completes.
	 */
    public RecipeResult setOutputFluidStack(FluidStack fs)
    {
    	this.outputFS = fs;
    	return this;
    }
 }
