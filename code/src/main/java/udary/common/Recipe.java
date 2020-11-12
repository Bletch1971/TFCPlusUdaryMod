package udary.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import udary.common.enums.EnumRecipeMatchType;
import udary.waila.WailaUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;

public class Recipe 
{
	public static int MIN_RECIPE_DURATION = 0;
	public static int MIN_RECIPE_LEVEL = 0;
	
	protected Random random;

	protected List<FluidStack> inputFSList;
	protected List<ItemStack> inputISList;
	protected FluidStack outputFS;
	protected ItemStack outputIS;
	protected int recipeId;
	protected int recipeDuration;
	protected int recipeLevel;
    protected String unlocalizedName;
	protected String unlocalizedProcessingMessage;
	protected String unlocalizedResultName;

    protected Recipe()
    {
    	this.random = null;
    	
    	this.inputFSList = new ArrayList<FluidStack>();
    	this.inputISList = new ArrayList<ItemStack>();
    	this.outputFS = null;
    	this.outputIS = null;
    	this.recipeDuration = 0;
    	this.recipeId = 0;
    	this.recipeLevel = 0;
    	this.unlocalizedName = "";
    	this.unlocalizedProcessingMessage = "";
    	this.unlocalizedResultName = "";
    }
    
    /**
     * Compares Recipe argument1 with Recipe argument2; returns true if both Recipes are equal.
     */
    public static boolean areRecipesEqual(Recipe recipe1, Recipe recipe2)
    {
        return recipe1 == null && recipe2 == null ? true : (recipe1 != null && recipe2 != null ? recipe1.isRecipeEqual(recipe2) : false);    	
    }
    
    /**
     * Adds a fluid stack used as input for the recipe.
     * @param fs The FluidStack object used as input for the recipe.
     */
    protected Recipe addInputFluidStack(FluidStack fs)
    {
    	if (fs != null)
    		this.inputFSList.add(fs);
    	return this;
    }
    
    /**
     * Adds an item stack used as input for the recipe.
     * @param is The ItemStack object used as input for the recipe.
     */
    protected Recipe addInputItemStack(ItemStack is)
    {
    	if (is != null)
    		this.inputISList.add(is);
    	return this;
    }
     
	/**
	 * Gets the list of fluid stacks used as the input for the recipe.
	 * @return a list of FluidStack objects that contains the input fluids for the recipe.
	 */
	protected List<FluidStack> getInputFluidStacks()
	{
		return this.inputFSList;
	}
	
	/**
	 * Gets the list of item stacks used as the input for the recipe.
	 * @return a list of ItemStack objects that contains the input items for the recipe.
	 */
	protected List<ItemStack> getInputItemStacks()
	{
		return this.inputISList;
	}
	
    /**
     * Returns the localized name of this recipe.
     */
    public String getLocalizedName()
    {
    	String name = this.unlocalizedName;
    	
    	// check if we have a name defined.
    	if (name != null && !name.trim().equalsIgnoreCase(""))
    		// name defined, return the localized name.
            return StatCollector.translateToLocal(this.getUnlocalizedName() + ".name").trim();
    	
    	// name not defined, return the default instead.
    	return StatCollector.translateToLocal("recipe.unknown.name");
    }
    
    /**
     * Returns the localized processing message of this recipe.
     */
    public String getLocalizedProcessingMessage()
    {
        String message = this.unlocalizedProcessingMessage;
        
        // check if we have a message defined.
        if (message != null && !message.trim().equalsIgnoreCase(""))
        	// message defined, return the localized message.
        	return StatCollector.translateToLocal(this.getUnlocalizedProcessingMessage()).trim();
        
        // message not defined, return the default instead.
        return StatCollector.translateToLocal("gui.processing");
    }
	
    /**
     * Returns the localized result name of this recipe.
     * @param includePrefix true to include 'Result :' before the result name.
     */
    public String getLocalizedResultName(boolean includePrefix)
    {
    	String name = this.unlocalizedResultName;
    	
    	// check if we have a name defined.
    	if (name != null && !name.trim().equalsIgnoreCase(""))
    		// name defined, return the localized name.
            return (includePrefix ? StatCollector.translateToLocal("gui.result")+WailaUtils.SEPARATOR_COLON : "")+StatCollector.translateToLocal(this.getUnlocalizedResultMessage()).trim();
    	
    	// name not defined, return the default instead.
		name = "";
		
    	// check if we have an output item stack.
    	if (this.outputIS != null)
		{
    		// check if the output item stack has multiples.
			if (this.outputIS.stackSize > 1)
				name += this.outputIS.stackSize+WailaUtils.SEPARATOR_TIMES;
			
			name += this.outputIS.getDisplayName();
		}
    	// check if the output fluid is an input fluid.
    	else if (this.outputFS != null && !isInputFluid(this.outputFS))
    	{
			// output fluid not an input fluid, return fluid name.
			name = this.outputFS.getLocalizedName();
    	}
		
    	if (name != null && !name.trim().equalsIgnoreCase(""))
    		return (includePrefix ? StatCollector.translateToLocal("gui.result")+WailaUtils.SEPARATOR_COLON : "")+name;
    	return "";
    }

	/**
	 * Gets the resulting fluid stack for the recipe.
	 * @return A FluidStack object that contains the result of the recipe.
	 */
	protected FluidStack getOutputFluidStack()
	{
		return this.outputFS;
	}
	
	/**
	 * Gets the resulting item stack for the recipe. 
	 * @return an ItemStack object that contains the result of the recipe.
	 */
	protected ItemStack getOutputItemStack()
	{
		return this.outputIS;
	}
	
	/**
	 * Get a random object using the recipeid as it's seed. 
	 */
	protected Random getRandom()
	{
		if (this.random == null)
			this.random = new Random(recipeId);
		
		return this.random;
	}
  	
	/**
	 * Gets the amount of time the recipe requires to complete.
	 */
	protected int getRecipeDuration()
	{
		return this.recipeDuration;
	}
	
	/**
	 * Gets the number assigned by the manager or 0 if not assigned.
	 */
	public int getRecipeId()
	{
		return this.recipeId;
	}
	
	/**
	 * Gets the minimum level the recipe required to complete.
	 */
	protected int getRecipeLevel()
	{
		return this.recipeLevel;
	}

    /**
     * Returns the unlocalized name of this recipe.
     */
    public String getUnlocalizedName()
    {
    	if (this.unlocalizedName == null)
    		return "recipe." + "";
    	
        return "recipe." + this.unlocalizedName; 	
    }
    
    /**
     * Returns the unlocalized processing message of this recipe.
     */
    public String getUnlocalizedProcessingMessage()
    {
    	if (this.unlocalizedProcessingMessage == null)
    		return "";
    	
        return this.unlocalizedProcessingMessage;
    }
    
    /**
     * Returns the unlocalized result name of this recipe.
     */
    public String getUnlocalizedResultMessage()
    {
    	if (this.unlocalizedResultName == null)
    		return "";
    	
        return this.unlocalizedResultName;
    }
	
    /**
     * Compares the FluidStack argument to the list of input FluidStacks; return true if argument found in the list.
     */
    protected boolean isInputFluid(FluidStack fs)
    {
    	if (fs == null)
    		return false;
    	
    	for (FluidStack inputFS : this.inputFSList)
    	{
    		if (fs.isFluidEqual(inputFS))
    			return true;
    	}
    	
    	return false;
    }
	
    /**
     * Compares the ItemStack argument to the list of input ItemStacks; return true if argument found in the list.
     */
    protected boolean isInputItem(ItemStack is)
    {
    	if (is == null)
    		return false;
    	
    	for (ItemStack inputIS : this.inputISList)
    	{
    		if (is.isItemEqual(inputIS) || OreDictionary.itemMatches(is, inputIS, false))
    			return true;
    	}
    	
    	return false;
    }
    
    /**
     * Compares Recipe argument to the instance Recipe; returns true if both Recipes are equal.
     */
	public boolean isRecipeEqual(Recipe recipe)
	{
		return isRecipeEqual(recipe, EnumRecipeMatchType.EXACT);
	}
    
    /**
     * Compares Recipe argument to the instance Recipe; returns true if both Recipes are equal.
     */
	public boolean isRecipeEqual(Recipe recipe, EnumRecipeMatchType matchType)
	{
		if (recipe == null)
			return false;
		
		return recipe.matches(recipe.getInputFluidStacks(), recipe.getInputItemStacks(), recipe.getRecipeLevel(), EnumRecipeMatchType.EXACT);
	}

	/**
	 * Returns true if the recipe is valid; otherwise false.
	 */
	public boolean isRecipeValid()
	{
		return false;
	}

	/**
	 * Match a recipe by it's input components.
	 * @param inputFSList The list of FluidStack objects that contains the input fluids for the recipe.
	 * @param inputISList The list of ItemStack objects that contains the input items for the recipe.
	 * @param matchType The type of match to be performed. 
	 * @return True if the recipe was matched; otherwise false.
	 */
	protected boolean matches(List<FluidStack> inputFSList, List<ItemStack> inputISList, EnumRecipeMatchType matchType)
	{
		// check if all components are null or empty.
		if ((inputFSList == null || inputFSList.size() == 0) && (inputISList == null || inputISList.size() == 0))
			return false;
		
		// check if only one stack list is null or empty.
		if ((inputFSList == null || inputFSList.size() == 0) && (this.inputFSList != null && this.inputFSList.size() > 0) || 
			(inputFSList != null && inputFSList.size() > 0) && (this.inputFSList == null || this.inputFSList.size() == 0))
			return false;
		if ((inputISList == null || inputISList.size() == 0) && (this.inputISList != null && this.inputISList.size() > 0) || 
			(inputISList != null && inputISList.size() > 0) && (this.inputISList == null || this.inputISList.size() == 0))
			return false;
		
		// check if all the fluids are included.
		if (inputFSList != null && this.inputFSList != null)
		{
			// create a temporary list of input fluids.
			List<FluidStack> tempFSList = new ArrayList<FluidStack>(this.inputFSList);

			for (FluidStack inputFS : inputFSList)
			{
				boolean matched = false;
				
				for (FluidStack tempFS : tempFSList)
				{
					// check if the fluids match.
					if (tempFS.isFluidEqual(inputFS))
					{
						// fluids match, check the match type and the fluid amounts.
						if ((matchType == EnumRecipeMatchType.BASIC) ||
							(matchType == EnumRecipeMatchType.MINIMUM && inputFS.amount >= tempFS.amount) ||
							(matchType == EnumRecipeMatchType.EXACT && inputFS.amount == tempFS.amount))
						{
							// fluid matched, remove from temp list and exit.
							tempFSList.remove(tempFS);
							
							matched = true;
							break;
						}
					}
				}
				
				// check if the fluid was matched.
				if (!matched)
					return false;
			}
			
			// check if any input fluids were not matched.
			if (tempFSList.size() > 0)
				return false;
		}
		
		// check if all the items are included.
		if (inputISList != null && this.inputISList != null)
		{
			// create a temporary list of input items.
			List<ItemStack> tempISList = new ArrayList<ItemStack>(this.inputISList);
			
			for (ItemStack inputIS : inputISList)
			{
				boolean matched = false;
				
				for (ItemStack tempIS : tempISList)
				{
					if (/*recipeIS.isItemEqual(inputIS) ||*/ OreDictionary.itemMatches(tempIS, inputIS, false))
					{
						if ((matchType == EnumRecipeMatchType.BASIC) ||
							(matchType == EnumRecipeMatchType.MINIMUM && inputIS.stackSize >= tempIS.stackSize) ||
							(matchType == EnumRecipeMatchType.EXACT && inputIS.stackSize == tempIS.stackSize))
						{
							// item matched, remove from temp list and exit.
							tempISList.remove(tempIS);
							
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
	 * Match a recipe by it's input components and level.
	 * @param inputFSList The list of FluidStack objects that contains the input fluids for the recipe.
	 * @param inputISList The list of ItemStack objects that contains the input items for the recipe.
	 * @param maximumRecipeLevel The maximum recipe level value.
	 * @param matchType The type of match to be performed. 
	 * @return True if the recipe was matched; otherwise false.
	 */
	protected boolean matches(List<FluidStack> inputFSList, List<ItemStack> inputISList, int maximumRecipeLevel, EnumRecipeMatchType matchType)
	{
		// check if the recipe level is valid.
		if (this.recipeLevel > maximumRecipeLevel)
			return false;

		return matches(inputFSList, inputISList, matchType);
	}
	
	/**
	 * Sets the resulting fluid stack for the recipe.
	 * @param fs The resulting fluid stack when the recipe completes.
	 */
    protected Recipe setOutputFluidStack(FluidStack fs)
    {
    	this.outputFS = fs;
    	return this;
    }
    
    /**
     * Sets the resulting item stack for the recipe.
     * @param is The resulting item stack when the recipe completes.
     */
    protected Recipe setOutputItemStack(ItemStack is)
    {
    	this.outputIS = is;
    	return this;
    }

    /**
     * Sets the amount of time the recipe requires to complete.
     * @param duration The amount of time to complete the recipe.
     */
    protected Recipe setRecipeDuration(int duration)
    {
    	this.recipeDuration = Math.max(duration, MIN_RECIPE_DURATION);
    	return this;
    }
	
    /**
     * Sets the recipe number. Should only be assigned by the manager.
     * @param recipeId The number of the recipe.
     */
    protected Recipe setRecipeId(int recipeId)
	{
		this.recipeId = recipeId;
		return this;
	}    
	
    /**
     * Sets the minimum level the recipe required to complete.
     * @param recipeLevel The minimum level of the recipe.
     */
    protected Recipe setRecipeLevel(int recipeLevel)
	{
		this.recipeLevel = Math.max(recipeLevel, MIN_RECIPE_LEVEL);
		return this;
	}    

    /**
     * Sets the unlocalized name of this recipe to the string passed as the parameter, prefixed by "recipe.".
     */
    public Recipe setUnlocalizedName(String name)
    {
        this.unlocalizedName = name != null ? name : "";
        return this;
    }

    /**
     * Sets the unlocalized processing message of this recipe to the string passed as the parameter.
     */
    public Recipe setUnlocalizedProcessingMessage(String message)
    {
        this.unlocalizedProcessingMessage = message != null ? message : "";
        return this;
    }

    /**
     * Sets the unlocalized result name of this recipe to the string passed as the parameter.
     */
    public Recipe setUnlocalizedResultName(String name)
    {
        this.unlocalizedResultName = name != null ? name : "";
        return this;
    }

    @Override
    public String toString()
    {
        return this.recipeId + ":" + this.unlocalizedName + "@" + this.unlocalizedResultName;
    }
}
