package udary.tfcplusudarymod.core.managers;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;

import udary.common.Recipe;
import udary.common.RecipeManager;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.recipes.EvaporatorPanRecipe;

public class EvaporatorPanManager extends RecipeManager
{
	private static final EvaporatorPanManager instance = new EvaporatorPanManager("Evaporator Pan Manager");
	public static final EvaporatorPanManager getInstance()
	{
		return instance;
	}
	
	protected EvaporatorPanManager(String managerName) 
	{
		super(managerName);
	}

	public static void initialise()
	{
		if (!ModOptions.enableEvaporationMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Evaporation Pan Recipes");
		
		registerRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Total Recipes Registered: " + getInstance().recipes.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Evaporation Pan Recipes");
	}
	
	protected static void registerRecipes()
	{
		if (FluidRegistry.isFluidRegistered(TFCFluids.SALTWATER))
		{
			// Saltwater -> Salt
			Recipe recipe = new EvaporatorPanRecipe(new FluidStack(TFCFluids.SALTWATER, ModOptions.saltwaterEvaporationAmount), new ItemStack(TFCItems.powder, 1, 9) /* Salt */).setUnlocalizedName("EP Salt");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
		
		if (ModOptions.enableHotSpringWater && FluidRegistry.isFluidRegistered(TFCFluids.HOTWATER))
		{
			// Spring Water -> Fertilizer
			Recipe recipe = new EvaporatorPanRecipe(new FluidStack(TFCFluids.HOTWATER, ModOptions.hotWaterEvaporationAmount), new ItemStack(TFCItems.fertilizer)).setUnlocalizedName("EP Fertilizer");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
		
		if (FluidRegistry.isFluidRegistered(ModFluids.SULFURICACID))
		{
			// Surfuric Acid -> Sulfur
			Recipe recipe = new EvaporatorPanRecipe(new FluidStack(ModFluids.SULFURICACID, ModOptions.sulfuricAcidEvaporationAmount), new ItemStack(TFCItems.powder, 1, 3) /* Sulfer */).setUnlocalizedName("EP Sulfer");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
		
		if (FluidRegistry.isFluidRegistered(ModFluids.NITRICACID))
		{
			// Nitric Acid -> Saltpeter
			Recipe recipe = new EvaporatorPanRecipe(new FluidStack(ModFluids.NITRICACID, ModOptions.nitricAcidEvaporationAmount), new ItemStack(TFCItems.powder, 1, 4) /* saltpeter */).setUnlocalizedName("EP Saltpeter");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
	}

    public boolean addRecipe(Recipe recipe, String name)
    {
    	if (!(recipe instanceof EvaporatorPanRecipe))
    		return false;
    	
    	return super.addRecipe(recipe, name);
    }
    
    /**
     * Find a recipe by it's input components.
     * @param inputFS The FluidStack object that contains the input fluid for the recipe.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
	public EvaporatorPanRecipe findRecipe(FluidStack inputFS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null)
			return null;
		
		return (EvaporatorPanRecipe) super.findRecipe(Arrays.asList(inputFS), null, matchType);
	}
}
