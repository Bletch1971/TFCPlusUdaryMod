package udary.tfcplusudarymod.core.managers;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import udary.common.Recipe;
import udary.common.RecipeManager;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModItems;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.recipes.OreCookerRecipe;

import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;
import com.dunk.tfc.api.TFC_ItemHeat;

public class OreCookerManager extends RecipeManager 
{
	public static final int COOK_TEMP_MINIMUM = 800;
	public static final float COOK_TEMP_MODIFIER = 0.6f;
	
	private static final OreCookerManager instance = new OreCookerManager("Ore Cooker Manager");
	public static final OreCookerManager getInstance()
	{
		return instance;
	}

	protected OreCookerManager(String managerName) 
	{
		super(managerName);
	}

	public static void initialise()
	{
		if (!ModOptions.enableLimoniteMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Ore Cooker Recipes");
		
		registerRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Total Recipes Registered: " + getInstance().recipes.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Ore Cooker Recipes");
	}
	
	protected static void registerRecipes()
	{
		Recipe recipe;
		ItemStack inputIS;
		int cookTemp;
		
		if (ModItems.CookedLimonite != null)
		{
			// Limonite (10) -> Cooked Limonite (10)
			inputIS = new ItemStack(TFCItems.smallOreChunk, 1, 11);
			cookTemp = (int) Math.max(TFC_ItemHeat.isCookable(inputIS) * COOK_TEMP_MODIFIER, COOK_TEMP_MINIMUM);
			
			recipe = new OreCookerRecipe(inputIS, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.smallOreUnits), TFCOptions.smallOreUnits, cookTemp).setUnlocalizedName("OC Cooked Limonite (10)").setUnlocalizedProcessingMessage("gui.cooking");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());

			// Limonite (15) -> Cooked Limonite (15)
			inputIS = new ItemStack(TFCItems.oreChunk, 1, 60);
			cookTemp = (int) Math.max(TFC_ItemHeat.isCookable(inputIS) * COOK_TEMP_MODIFIER, COOK_TEMP_MINIMUM);
			
			recipe = new OreCookerRecipe(inputIS, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.poorOreUnits), TFCOptions.poorOreUnits, cookTemp).setUnlocalizedName("OC Cooked Limonite (15)").setUnlocalizedProcessingMessage("gui.cooking");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());

			// Limonite (25) -> Cooked Limonite (25)
			inputIS = new ItemStack(TFCItems.oreChunk, 1, 11);
			cookTemp = (int) Math.max(TFC_ItemHeat.isCookable(inputIS) * COOK_TEMP_MODIFIER, COOK_TEMP_MINIMUM);
			
			recipe = new OreCookerRecipe(inputIS, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.normalOreUnits), TFCOptions.normalOreUnits, cookTemp).setUnlocalizedName("OC Cooked Limonite (25)").setUnlocalizedProcessingMessage("gui.cooking");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());

			// Limonite (35) -> Cooked Limonite (35)
			inputIS = new ItemStack(TFCItems.oreChunk, 1, 46);
			cookTemp = (int) Math.max(TFC_ItemHeat.isCookable(inputIS) * COOK_TEMP_MODIFIER, COOK_TEMP_MINIMUM);
			
			recipe = new OreCookerRecipe(inputIS, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.richOreUnits), TFCOptions.richOreUnits, cookTemp).setUnlocalizedName("OC Cooked Limonite (35)").setUnlocalizedProcessingMessage("gui.cooking");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
	}

    public boolean addRecipe(Recipe recipe, String name)
    {
    	if (!(recipe instanceof OreCookerRecipe))
    		return false;
    	
    	return super.addRecipe(recipe, name);
    }
    
    /**
     * Find a recipe by it's input components.
     * @param inputIS The ItemStack object that contains the input stack for the recipe.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
	public OreCookerRecipe findRecipe(ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputIS == null)
			return null;
		
		return (OreCookerRecipe)super.findRecipe(null, Arrays.asList(inputIS), matchType);
	}
}
