package udary.tfcplusudarymod.core.managers;

import java.util.Arrays;
import java.util.List;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import udary.common.Recipe;
import udary.common.RecipeManager;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModItems;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.recipes.AnodisingVesselAnodiseRecipe;

import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;

public class AnodisingVesselAnodiseManager extends RecipeManager
{
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	
	public static final float CHARGE_MODIFIER_NITRICACID = 5f;
	public static final float CHARGE_MODIFIER_SULFURICACID = 6f;
	public static final float CHARGE_MODIFIER_VINEGAR = 8f;
	
	public static final float DAMAGE_MODIFIER_NITRICACID = 0.14f;
	public static final float DAMAGE_MODIFIER_SULFURICACID = 0.12f;
	public static final float DAMAGE_MODIFIER_VINEGAR = 0.10f;
	
	public static final float DISOLVE_MODIFIER_NITRICACID = 5f;
	public static final float DISOLVE_MODIFIER_SULFURICACID = 6f;
	public static final float DISOLVE_MODIFIER_VINEGAR = 8f;
	
	public static final float DURATION_MODIFIER_NITRICACID = 5f;
	public static final float DURATION_MODIFIER_SULFURICACID = 6f;
	public static final float DURATION_MODIFIER_VINEGAR = 8f;
	
	public static final float RESULT_MODIFIER_NITRICACID = 0.10f;
	public static final float RESULT_MODIFIER_SULFURICACID = 0.10f;
	public static final float RESULT_MODIFIER_VINEGAR = 0.08f;
	
	public static final int ADDITIONAL_RESULT_PERCENTAGE = 10;
	
	private static final AnodisingVesselAnodiseManager instance = new AnodisingVesselAnodiseManager("Anodising Vessel Anodise Manager");
	public static final AnodisingVesselAnodiseManager getInstance()
	{
		return instance;
	}

	protected AnodisingVesselAnodiseManager(String managerName) 
	{
		super(managerName);
	}
	
	public static void initialise()
	{
		if (!ModOptions.enableGalenaMod && !ModOptions.enableLimoniteMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Anodising Vessel Anodise Recipes");
		
		registerRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Total Recipes Registered: " + getInstance().recipes.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Anodising Vessel Anodise Recipes");		
	}
	
	protected static void registerRecipes()
	{
		int anodeDamageAmount;
		float baseResultAmount;
		int batteryChargeAmount;		
		int cathodeDamageAmount;
		int duration;
		FluidStack inputFS;
		ItemStack soluteIS;
		ItemStack resultIS;
		Recipe recipe;
		
		// Anodising Recipes
		if (ModOptions.enableGalenaMod)
		{
			if (ModItems.CopperRodSilverCoated != null)
			{
				// Silver Extraction Recipes 
				resultIS = new ItemStack(ModItems.CopperRodSilverCoated, 1, WILDCARD_VALUE);
				
				List<Item> soluteItems = Arrays.asList(TFCItems.smallOreChunk, TFCItems.oreChunk, TFCItems.oreChunk, TFCItems.oreChunk);
				List<Integer> soluteNumber = Arrays.asList(6, 55, 6, 41); /* Galena */
				List<Integer> soluteAmounts = Arrays.asList(TFCOptions.smallOreUnits, TFCOptions.poorOreUnits, TFCOptions.normalOreUnits, TFCOptions.richOreUnits);
				
				// Silver Extraction using Nitric Acid Recipes
				if (FluidRegistry.isFluidRegistered(ModFluids.NITRICACID))
				{
					for (int i = 0; i < 4; i++)
					{
						Item item = soluteItems.get(i);
						int number = soluteNumber.get(i);
						float amount = soluteAmounts.get(i);
						
						anodeDamageAmount = (int)(amount * DAMAGE_MODIFIER_NITRICACID);
						baseResultAmount = amount * RESULT_MODIFIER_NITRICACID;
						batteryChargeAmount = (int)(amount * CHARGE_MODIFIER_NITRICACID);
						cathodeDamageAmount = 1;
						duration = (int)(amount * DISOLVE_MODIFIER_NITRICACID);

						inputFS = new FluidStack(ModFluids.NITRICACID, (int)(amount * DISOLVE_MODIFIER_NITRICACID));
						soluteIS = new ItemStack(item, 1, number);
						
						recipe = new AnodisingVesselAnodiseRecipe(inputFS, soluteIS, resultIS, duration, batteryChargeAmount, cathodeDamageAmount, anodeDamageAmount, baseResultAmount).setAdditionalResultPercentage(ADDITIONAL_RESULT_PERCENTAGE).setUnlocalizedName("AV Galena Silver NitricAcid ("+amount+")");
						if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
							System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
						else if (ModOptions.showVerboseStartup)
							System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
					}
				}
				
				// Silver Extraction using Vinegar Recipes
				if (FluidRegistry.isFluidRegistered(TFCFluids.VINEGAR))
				{
					for (int i = 0; i < 4; i++)
					{
						Item item = soluteItems.get(i);
						int number = soluteNumber.get(i);
						float amount = soluteAmounts.get(i);
						
						anodeDamageAmount = (int)(amount * DAMAGE_MODIFIER_VINEGAR);
						baseResultAmount = amount * RESULT_MODIFIER_VINEGAR;
						batteryChargeAmount = (int)(amount * CHARGE_MODIFIER_VINEGAR);
						cathodeDamageAmount = 1;
						duration = (int)(amount * DISOLVE_MODIFIER_VINEGAR);

						inputFS = new FluidStack(TFCFluids.VINEGAR, (int)(amount * DISOLVE_MODIFIER_VINEGAR));
						soluteIS = new ItemStack(item, 1, number);
						
						recipe = new AnodisingVesselAnodiseRecipe(inputFS, soluteIS, resultIS, duration, batteryChargeAmount, cathodeDamageAmount, anodeDamageAmount, baseResultAmount).setAdditionalResultPercentage(ADDITIONAL_RESULT_PERCENTAGE).setUnlocalizedName("AV Galena Silver Vinegar ("+amount+")");
						if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
							System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
						else if (ModOptions.showVerboseStartup)
							System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
					}
				}
			}
		}
		
		if (ModOptions.enableLimoniteMod)
		{
			if (ModItems.CopperRodNickelCoated != null && ModItems.CookedLimonitePowder != null)
			{
				// Nickel Extraction Recipes 
				resultIS = new ItemStack(ModItems.CopperRodNickelCoated, 1, WILDCARD_VALUE);

				// Nickel Extraction using Sulfuric Acid Recipes
				if (FluidRegistry.isFluidRegistered(ModFluids.SULFURICACID))
				{
					Item item = ModItems.CookedLimonitePowder;
					float amount = 4.5f;
					
					anodeDamageAmount = 2;
					baseResultAmount = amount * RESULT_MODIFIER_SULFURICACID;
					batteryChargeAmount = (int)(amount * CHARGE_MODIFIER_SULFURICACID);
					cathodeDamageAmount = 1;
					duration = (int)(amount * DISOLVE_MODIFIER_SULFURICACID);

					inputFS = new FluidStack(ModFluids.SULFURICACID, (int)(amount * DISOLVE_MODIFIER_SULFURICACID));
					soluteIS = new ItemStack(item);
					
					recipe = new AnodisingVesselAnodiseRecipe(inputFS, soluteIS, resultIS, duration, batteryChargeAmount, cathodeDamageAmount, anodeDamageAmount, baseResultAmount).setAdditionalResultPercentage(ADDITIONAL_RESULT_PERCENTAGE).setUnlocalizedName("AV Limonite Nickel SulfuricAcid ("+amount+")");
					if (!getInstance().addRecipe(recipe, recipe.getUnlocalizedName()))
						System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Recipe not registered (" + recipe.getLocalizedName() + ")");
					else if (ModOptions.showVerboseStartup)
						System.out.println("[" + ModDetails.ModName + "] Registered Recipe: " + recipe.getLocalizedName());
				}

				// Nickel Extraction using Vinegar Recipes
				if (FluidRegistry.isFluidRegistered(TFCFluids.VINEGAR))
				{
					Item item = ModItems.CookedLimonitePowder;
					float amount = 4.5f;
					
					anodeDamageAmount = 1;
					baseResultAmount = amount * RESULT_MODIFIER_VINEGAR;
					batteryChargeAmount = (int)(amount * CHARGE_MODIFIER_VINEGAR);
					cathodeDamageAmount = 1;
					duration = (int)(amount * DISOLVE_MODIFIER_VINEGAR);

					inputFS = new FluidStack(TFCFluids.VINEGAR, (int)(amount * DISOLVE_MODIFIER_VINEGAR));
					soluteIS = new ItemStack(item);
					
					recipe = new AnodisingVesselAnodiseRecipe(inputFS, soluteIS, resultIS, duration, batteryChargeAmount, cathodeDamageAmount, anodeDamageAmount, baseResultAmount).setAdditionalResultPercentage(ADDITIONAL_RESULT_PERCENTAGE).setUnlocalizedName("AV Limonite Nickel Vinegar ("+amount+")");
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
    	if (!(recipe instanceof AnodisingVesselAnodiseRecipe))
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
	public AnodisingVesselAnodiseRecipe findRecipe(FluidStack inputFS, ItemStack inputIS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null || inputIS == null)
			return null;

		for (Recipe recipe : this)
    	{
    		if (!(recipe instanceof AnodisingVesselAnodiseRecipe))
    			continue;

    		if (((AnodisingVesselAnodiseRecipe)recipe).matches(inputFS, inputIS, matchType))
    			return (AnodisingVesselAnodiseRecipe)recipe;
    	}
    	
    	return null;
	}
}
