package udary.tfcplusudarymod.core.managers;

import java.util.Arrays;
import java.util.Iterator;

import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IFood;

import cpw.mods.fml.common.registry.FMLControlledNamespacedRegistry;
import cpw.mods.fml.common.registry.GameData;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import udary.common.Recipe;
import udary.common.RecipeManager;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.recipes.DryingMatFoodRecipe;
import udary.tfcplusudarymod.core.recipes.DryingMatRecipe;

public class DryingMatManager extends RecipeManager
{
	public static final float DRIED_TASTE_MODIFIER = 2.0f;
	public static final float DRIED_WEIGHT_MODIFIER = 0.5f;

	private static final DryingMatManager instance = new DryingMatManager("Drying Mat Manager");
	public static final DryingMatManager getInstance()
	{
		return instance;
	}
	
	protected DryingMatManager(String managerName) 
	{
		super(managerName);
	}

	public static void initialise()
	{
		if (!ModOptions.enableDryingMatMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Drying Mat Recipes");
		
		registerRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Total Recipes Registered: " + getInstance().recipes.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Drying Mat Recipes");
	}

	protected static void registerRecipes()
	{
		FMLControlledNamespacedRegistry<Item> itemList = GameData.getItemRegistry();
		for (Iterator<Item> i = itemList.iterator(); i.hasNext();)
		{
			Item item = i.next();
			
			if (item instanceof IFood)
			{
				EnumFoodGroup foodGroup = ((IFood)item).getFoodGroup();
				if (foodGroup != null && (foodGroup == EnumFoodGroup.Fruit || foodGroup == EnumFoodGroup.Vegetable))
				{
					ItemStack inputIS =  ItemFoodTFC.createTag(new ItemStack(item), Global.FOOD_MAX_WEIGHT);
					
					Recipe recipe = new DryingMatFoodRecipe(inputIS, DRIED_WEIGHT_MODIFIER, DRIED_TASTE_MODIFIER).setUnlocalizedName(item.getUnlocalizedName().substring(5)).setUnlocalizedProcessingMessage("gui.drying");
					if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
						System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
					else if (ModOptions.showVerboseStartup)
						System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
				}
			}
		}
	}

    public boolean addRecipe(Recipe recipe, String name)
    {
    	if (!(recipe instanceof DryingMatRecipe))
    		return false;
    	
    	return super.addRecipe(recipe, name);
    }
    
    /**
     * Find a recipe by it's input components.
     * @param inputIS The ItemStack object that contains the input item for the recipe.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
	public DryingMatRecipe findRecipe(ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputIS == null)
			return null;
		
		return (DryingMatRecipe) super.findRecipe(null, Arrays.asList(inputIS), matchType);
	}
}
