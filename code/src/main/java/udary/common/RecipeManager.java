package udary.common;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;
import udary.common.enums.EnumRecipeMatchType;

import com.google.common.collect.Maps;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ModContainer;

public class RecipeManager implements Iterable<Recipe>, Iterator<Recipe>
{
	protected String managerName;
	protected int nextRecipeId;

	protected Map<String, Recipe> recipes = Maps.newHashMap();
    
	protected RecipeManager(String managerName)
	{
		this.managerName = managerName != null ? managerName : "unknown manager";
		this.nextRecipeId = 1;
	}
	
    /**
     * Prefix the supplied name with the current mod id.
     *
     * If no mod id can be determined, minecraft will be assumed.
     * The prefix is separated with a colon.
     *
     * If there's already a prefix, it'll be prefixed again if the new prefix
     * doesn't match the old prefix, as used by vanilla calls to addObject.
     *
     * @param name name to prefix.
     * @return prefixed name.
     */
	protected String addPrefix(String name)
    {
        int index = name.lastIndexOf(':');
        
        String oldPrefix = index == -1 ? "" : name.substring(0, index);
        String prefix;
        
        ModContainer mc = Loader.instance().activeModContainer();

        if (mc != null)
            prefix = mc.getModId();
        else 
        	// no mod container, assume minecraft
            prefix = "minecraft";

        if (!oldPrefix.equals(prefix))
            name = prefix + ":" + name;

        return name;
    }

    /**
     * Register a recipe with the manager with a custom name.
     *
     * @param recipe The recipe to register.
     * @param name The name of the recipe.
     */
    public boolean addRecipe(Recipe recipe, String name)
    {
    	if (recipe == null || !recipe.isRecipeValid() || name == null || name.trim().equalsIgnoreCase(""))
    		return false;
    	
    	// check if a recipe with the same input components already exists.
    	if (findRecipe(recipe, EnumRecipeMatchType.BASIC) != null)
    		return false;

    	// assign the recipe an id from this manager.
    	recipe.setRecipeId(getNextAvailableRecipeId());

    	// get a modid prefixed name.
    	String key = addPrefix(name);
    	
    	// add the recipe to the manager.
    	recipes.put(key, recipe);

    	return true;
    }
	
	/**
	 * Removes all the recipes registered with the manager.
	 */
	public void clearRecipes()
	{
		recipes.clear();
	}
	
	public int count()
	{
		return recipes.size();
	}

    /**
     * Find a mod recipe in the global "named item list".
     * @param modId The modid owning the recipe.
     * @param name The name of the recipe itself.
     * @return The recipe or null if not found.
     */
    public Recipe findRecipe(String modId, String name)
    {
        String key = modId != null ? modId : "" + ":" + name != null ? name : "";
        return recipes.get(key);
    }

    /**
     * Find a mod recipe by it's id.
     * @param recipeId The id of the recipe to find.
     * @return The recipe or null if not found.
     */
    public Recipe findRecipe(int recipeId)
    {
		while (hasNext())
		{
			Recipe recipe = next();
			if (recipe == null) continue;
			
			if (recipe.getRecipeId() == recipeId)
				return recipe;
		}
		
		return null;
    }
    
    /**
     * Find a recipe using the input components of the Recipe argument.
     * @param recipe The recipe to check for.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
    public Recipe findRecipe(Recipe recipe, EnumRecipeMatchType matchType)
    {
    	if (recipe == null)
    		return null;
    	
    	for (Recipe recipe2 : this)
    	{
    		if (recipe2.matches(recipe.getInputFluidStacks(), recipe.getInputItemStacks(), matchType))
    			return recipe;
    	}
    	
    	return null;
    }
    
    /**
     * Find a recipe by it's input components.
     * @param inputFSList The list of FluidStack objects that contains the input fluids for the recipe.
     * @param inputISList The list of ItemStack objects that contains the input items for the recipe.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
    protected Recipe findRecipe(List<FluidStack> inputFSList, List<ItemStack> inputISList, EnumRecipeMatchType matchType)
    {
    	if ((inputFSList == null || inputFSList.size() == 0) && (inputISList == null || inputISList.size() == 0))
    		return null;
    	
    	for (Recipe recipe : this)
    	{
    		if (recipe.matches(inputFSList, inputISList, matchType))
    			return recipe;
    	}
    	
    	return null;
    }
    
    /**
     * Find a recipe by it's input components.
     * @param inputFSList The list of FluidStack objects that contains the input fluids for the recipe.
     * @param inputISList The list of ItemStack objects that contains the input items for the recipe.
     * @param maximumRecipeLevel The maximum recipe level value.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
    protected Recipe findRecipe(List<FluidStack> inputFSList, List<ItemStack> inputISList, int maximumRecipeLevel, EnumRecipeMatchType matchType)
    {
    	if ((inputFSList == null || inputFSList.size() == 0) && (inputISList == null || inputISList.size() == 0))
    		return null;
    	
    	for (Recipe recipe : this)
    	{
    		if (recipe.matches(inputFSList, inputISList, maximumRecipeLevel, matchType))
    			return recipe;
    	}
    	
    	return null;
    }
    
	/**
	 * Get the name of the manager.
	 */
	public String getManagerName()
	{
		return this.managerName;
	}
	
    /**
     * Get the next available recipeId.
     */
    protected int getNextAvailableRecipeId()
    {
        return nextRecipeId++;
    }
	
    /**
     * Removes a registered recipe from the manager.
     * @param modId The modid owning the recipe. 
     * @param name The name of the recipe itself.
     * @return True if the recipe was removed; otherwise false if recipe not removed or not registered.
     */
	public boolean removeRecipe(String modId, String name)
	{
		// check for valid parameters.
		if (modId == null || name == null)
			return false;

    	// get a modid prefixed name.
    	String key = addPrefix(name);
    	
		return recipes.remove(key) != null;
	}
	
	/**
	 * Removes a registered recipe from the manager.
	 * @param recipeId The id of the recipe to remove.
	 * @return True if the recipe was removed; otherwise false if recipe not removed or not registered.
	 */
	public boolean removeRecipe(int recipeId)
	{		
		while (hasNext())
		{
			Recipe recipe = next();
			if (recipe == null) continue;
			
			if (recipe.getRecipeId() == recipeId)
				remove();
			return true;
		}
		
		return false;
	}

    @Override
    public String toString()
    {
        return managerName + "@" + recipes.size();
    }
    
	// ***** Iterator<Recipe> *****
	@Override
	public boolean hasNext() 
	{
		Iterator<Recipe> iterator = iterator();
		if (iterator == null)
			return false;
		
		return iterator.hasNext();
	}

	@Override
	public Recipe next() 
	{
		Iterator<Recipe> iterator = iterator();
		if (iterator == null)
			return null;
		
		return iterator().next();
	}

	@Override
	public void remove() 
	{
		Iterator<Recipe> iterator = iterator();
		if (iterator == null)
			return;
		
		iterator().remove();
	}

	// ***** Iterable<Recipe> *****
	@Override
	public Iterator<Recipe> iterator() 
	{
		return recipes.values().iterator();
	}
}
