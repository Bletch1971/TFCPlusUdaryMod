package udary.tfcplusudarymod.core.managers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import udary.common.Recipe;
import udary.common.RecipeManager;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.recipes.AnodisingVesselFluidRecipe;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;

public class AnodisingVesselFluidManager extends RecipeManager
{
	private static final AnodisingVesselFluidManager instance = new AnodisingVesselFluidManager("Anodising Vessel Fluid Manager");
	public static final AnodisingVesselFluidManager getInstance()
	{
		return instance;
	}

	protected AnodisingVesselFluidManager(String managerName) 
	{
		super(managerName);
	}
	
	public static void initialise()
	{
		if (!ModOptions.enableGalenaMod && !ModOptions.enableLimoniteMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Anodising Vessel Fluid Recipes");
		
		registerRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Total Recipes Registered: " + getInstance().recipes.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Anodising Vessel Fluid Recipes");		
	}
	
	protected static void registerRecipes()
	{
		Recipe recipe;

		// Fluid Recipes
		if (FluidRegistry.isFluidRegistered(ModFluids.NITRICACID))
		{
			// Nitric Acid
			recipe = new AnodisingVesselFluidRecipe(new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(TFCItems.powder, 1, 4 /* saltpeter */), new FluidStack(ModFluids.NITRICACID, 200)).setUnlocalizedName("AV Nitric Acid");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}

		if (FluidRegistry.isFluidRegistered(ModFluids.SULFURICACID))
		{
			// Sulfuric Acid
			recipe = new AnodisingVesselFluidRecipe(new FluidStack(TFCFluids.FRESHWATER, 200), new ItemStack(TFCItems.powder, 1, 3 /* Sulfer */), new FluidStack(ModFluids.SULFURICACID, 200)).setUnlocalizedName("AV Sulfuric Acid");
			if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
		}
	}

    public boolean addRecipe(Recipe recipe, String name)
    {
    	if (!(recipe instanceof AnodisingVesselFluidRecipe))
    		return false;
    	
    	return super.addRecipe(recipe, name);
    }

    /**
     * Find a recipe by it's input components.
	 * @param inputFS The FluidStack object that contains the input fluid for the recipe.
	 * @param inputIS The ItemStack object that contains the input item for the recipe.
     * @param matchType The type of match to be performed. 
     * @return The recipe or null if not found.
     */
	public AnodisingVesselFluidRecipe findRecipe(FluidStack inputFS, ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null || inputIS == null)
			return null;

		for (Recipe recipe : this)
    	{
    		if (!(recipe instanceof AnodisingVesselFluidRecipe))
    			continue;

    		if (((AnodisingVesselFluidRecipe)recipe).matches(inputFS, inputIS, matchType))
    			return (AnodisingVesselFluidRecipe)recipe;
    	}
    	
    	return null;
	}
}
