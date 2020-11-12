package udary.tfcplusudarymod.core;

import java.util.Map;

import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Crafting.AnvilManager;
import com.dunk.tfc.api.Crafting.AnvilRecipe;
import com.dunk.tfc.api.Crafting.AnvilReq;
import com.dunk.tfc.api.Crafting.CraftingManagerTFC;
import com.dunk.tfc.api.Crafting.KilnCraftingManager;
import com.dunk.tfc.api.Crafting.KilnRecipe;
import com.dunk.tfc.api.Crafting.PlanRecipe;
import com.dunk.tfc.api.Enums.RuleEnum;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModRecipes
{	
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	
	private static AnvilManager anvilManager = AnvilManager.getInstance();
	//private static BarrelManager barrelManager = BarrelManager.getInstance();
	private static CraftingManagerTFC craftingManager = CraftingManagerTFC.getInstance();
	private static KilnCraftingManager kilnCraftingManager = KilnCraftingManager.getInstance();
	//private static QuernManager quernManager = QuernManager.getInstance();

	// Plan values
	public static String BucklePlanName = "_buckle";
	public static String EvaporatorPanPlanName = "_evaporatorpan";
	public static String RodPlanName = "_rod";
	public static String WirePlanName = "_wire";
	
	// Recipe duration
	public static int SealTimeZincChloridePowder = 4;
	
	// Recipe Return values
	public static int BuckleNumberReturned = 100 / ModItems.BuckleMaxMetalAmount;
	public static int PanNumberReturned = 1;
	public static int RodNumberReturned = 100 / ModItems.RodMaxMetalAmount;
	public static int WireNumberReturned = 100 / ModItems.WireMaxMetalAmount;
	
	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Recipes");
		
		registerFluidRecipes();
		registerDeviceRecipes();
		registerToolRecipes();
		registerKnappingRecipes();
		registerMoldRecipes();	
		registerKilnRecipes();
		registerBarrelRecipes();
		registerQuernRecipes();
		registerMiscRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Recipes");
	}

	public static void initialiseAnvil()
	{
		// check if the plans/recipes have already been initialised.
		if (ModRecipes.areAnvilRecipesInitialised()) return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Anvil Recipes");
		
		registerAnvilPlans();
		registerAnvilRecipes();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Anvil Recipes");
	}

    public static boolean areAnvilRecipesInitialised() 
    { 
        Map<String, PlanRecipe> map = anvilManager.getPlans(); 
         
        return map != null && ( map.containsKey(EvaporatorPanPlanName) || 
        						map.containsKey(BucklePlanName) ||
				        		map.containsKey(RodPlanName) || 
				        		map.containsKey(WirePlanName)
				        	  ); 
    } 

	private static void registerAnvilPlans()
	{
		if (ModOptions.enableTuckerBagMod)
		{
			if (ModOptions.enableReinforcedBag)
			{
				// Buckle Plan
				anvilManager.addPlan(BucklePlanName, new PlanRecipe(new RuleEnum[]{RuleEnum.BENDLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.ANY}));
			}
		}
		
		if (ModOptions.enableEvaporationMod)
		{
			// Rod Plan
			anvilManager.addPlan(EvaporatorPanPlanName, new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.HITSECONDFROMLAST, RuleEnum.ANY}));
		}

		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			// Rod Plan
			anvilManager.addPlan(RodPlanName, new PlanRecipe(new RuleEnum[]{RuleEnum.HITLAST, RuleEnum.ANY, RuleEnum.ANY}));
		}

		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			// Wire Plan
			anvilManager.addPlan(WirePlanName, new PlanRecipe(new RuleEnum[]{RuleEnum.DRAWLAST, RuleEnum.ANY, RuleEnum.ANY}));
		}
	}
	
	private static void registerAnvilRecipes()
	{		
		// Wrought Iron Buckle
		if (ModItems.WroughtIronBuckle != null && anvilManager.getPlans().containsKey(BucklePlanName))
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.wroughtIronIngot), null, BucklePlanName, AnvilReq.WROUGHTIRON, new ItemStack(ModItems.WroughtIronBuckle, BuckleNumberReturned)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		}

		// Copper Evaporator Pan
		if (ModBlocks.EvaporatorPan != null && anvilManager.getPlans().containsKey(EvaporatorPanPlanName))
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperSheet), new ItemStack(TFCItems.copperSheet), EvaporatorPanPlanName, AnvilReq.COPPER, new ItemStack(ModBlocks.EvaporatorPan, PanNumberReturned)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		}
		
		// Copper Rod
		if (ModItems.CopperRod != null && anvilManager.getPlans().containsKey(RodPlanName))
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, RodPlanName, AnvilReq.COPPER, new ItemStack(ModItems.CopperRod, RodNumberReturned)).addRecipeSkill(Global.SKILL_TOOLSMITH));
		}
		
		// Copper Wire
		if (ModItems.CopperWire != null && anvilManager.getPlans().containsKey(WirePlanName))
		{
			anvilManager.addRecipe(new AnvilRecipe(new ItemStack(TFCItems.copperIngot), null, WirePlanName, AnvilReq.COPPER, new ItemStack(ModItems.CopperWire, WireNumberReturned)).addRecipeSkill(Global.SKILL_GENERAL_SMITHING));
		}
	}
	
	private static void registerBarrelRecipes()
	{
	}
	
	private static void registerDeviceRecipes()
	{
		// Alloy Calculator
		if (ModBlocks.AlloyCalculator != null)
		{
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.AlloyCalculator, 1), new Object[] {
				"PGP", 
				"PPP", 'G', ModOreDictionary.TFC_GEM_CHIPPED, 'P', ModOreDictionary.TFC_WOOD_LUMBER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.AlloyCalculator, 1), new Object[] {
				"PGP", 
				"PPP", 'G', ModOreDictionary.GEM_FLAWED, 'P', ModOreDictionary.TFC_WOOD_LUMBER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.AlloyCalculator, 1), new Object[] {
				"PGP", 
				"PPP", 'G', ModOreDictionary.GEM_NORMAL, 'P', ModOreDictionary.TFC_WOOD_LUMBER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.AlloyCalculator, 1), new Object[] {
				"PGP", 
				"PPP", 'G', ModOreDictionary.GEM_FLAWLESS, 'P', ModOreDictionary.TFC_WOOD_LUMBER}));
			GameRegistry.addRecipe(new ShapedOreRecipe(new ItemStack(ModBlocks.AlloyCalculator, 1), new Object[] {
				"PGP", 
				"PPP", 'G', ModOreDictionary.GEM_EXQUISITE, 'P', ModOreDictionary.TFC_WOOD_LUMBER}));
		}		
		
		// Anodising Vessel
		if (ModBlocks.AnodisingVessel != null && ModItems.CopperWire != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.AnodisingVessel), new ItemStack(TFCBlocks.vessel, 1, 1 /* large ceramic vessel */), new ItemStack(ModItems.CopperWire), new ItemStack(ModItems.CopperWire)));
		}
		
		// Ceramic Battery
		if (ModItems.CeramicBattery != null && ModItems.BottledSulfuricAcid != null)
		{
			if (ModItems.CopperWire != null)
			{
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CeramicBattery), new ItemStack(TFCItems.potterySmallVessel, 1, 1 /* small ceramic vessel*/), new ItemStack(ModItems.BottledSulfuricAcid), new ItemStack(ModItems.CopperWire), new ItemStack(ModItems.CopperWire)));
			}

			// repair recipe
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CeramicBattery), new ItemStack(ModItems.CeramicBattery, 1, WILDCARD_VALUE), new ItemStack(ModItems.BottledSulfuricAcid)));
		}
		
		if (ModBlocks.DryingMat != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModBlocks.DryingMat, 2), new ItemStack(TFCBlocks.thatch), ModOreDictionary.TFC_ITEM_KNIFE));
		}
	}
	
	private static void registerKilnRecipes()
	{
		// Rod Mold
		if (ModItems.ClayMoldRod != null)
		{
			kilnCraftingManager.addRecipe(new KilnRecipe(new ItemStack(ModItems.ClayMoldRod, 1, 0 /* clay mold */), 0, new ItemStack(ModItems.ClayMoldRod, 1, 1 /* ceramic mold */)));
		}
	}
	
	private static void registerKnappingRecipes()
	{
		if (ModBlocks.OreCooker != null)
		{
			craftingManager.addRecipe(new ItemStack(ModBlocks.OreCooker), new Object[] { 
				"#####",
				" ### ",
				" ### ",
				" ### ",
				"     ", '#', new ItemStack(TFCItems.flatClay, 1, 3 /* fire clay */)});
		}
		
		if (ModItems.CeramicBucket != null)
		{
			craftingManager.addRecipe(new ItemStack(ModItems.CeramicBucket), new Object[] { 
				"#####",
				"#####",
				" ### ",
				" ### ",
				"#   #", '#', new ItemStack(TFCItems.flatClay, 1, 3 /* fire clay */)});
		}
	}
	
	private static void registerFluidRecipes()
	{
		// Bottled Nitric Acid
		if (ModItems.BottledNitricAcid != null && ModItems.BottledWater != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.BottledNitricAcid), new ItemStack(ModItems.BottledWater), new ItemStack(TFCItems.powder, 1, 4 /* saltpeter */), new ItemStack(TFCItems.powder, 1, 4 /* saltpeter */)));
		}

		// Bottled Sulfuric Acid
		if (ModItems.BottledSulfuricAcid != null && ModItems.BottledWater != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.BottledSulfuricAcid), new ItemStack(ModItems.BottledWater), new ItemStack(Items.gunpowder)));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.BottledSulfuricAcid), new ItemStack(ModItems.BottledWater), new ItemStack(TFCItems.powder, 1, 3 /* Sulfer */), new ItemStack(TFCItems.powder, 1, 3 /* Sulfer */)));
		}
	}
	
	private static void registerMoldRecipes()
	{
		// Rod Mold
		if (ModItems.ClayMoldRod != null)
		{
			craftingManager.addRecipe(new ItemStack(ModItems.ClayMoldRod, 1), new Object[] { 
				"     ",
				"     ",
				"#####",
				"     ",
				"     ", '#', new ItemStack(TFCItems.flatClay, 1, 1 /* clay */)});
		}

		// Copper Rod Mold      
		if (ModItems.ClayMoldRod != null)
		{
			craftingManager.addRecipe(new ItemStack(ModItems.ClayMoldRod, 1, 2), new Object[] {
				"12", '1', getStackTemp(new ItemStack(TFCItems.copperUnshaped, 1, 1)), '2', new ItemStack(ModItems.ClayMoldRod, 1, 1)});
		}
	}
	
	private static void registerMiscRecipes()
	{
		if (ModOptions.enableTerraFirmaCraftAdditions)
		{
			// convert lumber to sticks
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.stick, ModOptions.stickConversion), new Object[] { "woodLumber" }));

			// create ore breakup recipes
			for (int index = 0; index < 14; index++)
			{
				// Normal Ore (25 units)
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.oreChunk, 1, index+49), new Object[] { new ItemStack(TFCItems.oreChunk, 1, index   ), ModOreDictionary.TFC_ITEM_HAMMER }));
				// Rich Ore (35 units)
				GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(TFCItems.oreChunk, 1, index   ), new Object[] { new ItemStack(TFCItems.oreChunk, 1, index+35), ModOreDictionary.TFC_ITEM_HAMMER }));		
			}
			
			// convert sticks to oak log
			GameRegistry.addRecipe(new ItemStack(TFCItems.logs, 1, 0 /* oak log */), new Object[] { 
				"SSS", 
				"SSS",
				"SSS", 'S', new ItemStack(TFCItems.stick) });			
		}
		
		if (ModItems.CookedLimonitePowder != null && ModItems.CookedLimonite != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CookedLimonitePowder, 1, 0), new Object[] { new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.smallOreUnits),  ModOreDictionary.TFC_ITEM_HAMMER }));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CookedLimonitePowder, 2, 0), new Object[] { new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.poorOreUnits),   ModOreDictionary.TFC_ITEM_HAMMER }));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CookedLimonitePowder, 4, 0), new Object[] { new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.normalOreUnits), ModOreDictionary.TFC_ITEM_HAMMER }));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CookedLimonitePowder, 6, 0), new Object[] { new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.richOreUnits),   ModOreDictionary.TFC_ITEM_HAMMER }));
		}
	}
	
	private static void registerQuernRecipes()
	{
	}
	
	private static void registerToolRecipes()
	{
		// Carbon Rod
		if (ModItems.CarbonRod != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CarbonRod), new Object[] { new ItemStack(TFCItems.coal, 1, 0 /* coal     */), ModOreDictionary.TFC_ITEM_KNIFE }));
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CarbonRod), new Object[] { new ItemStack(TFCItems.coal, 1, 1 /* charcoal */), ModOreDictionary.TFC_ITEM_KNIFE }));
		}
		
		// Copper Rod
		if (ModItems.CopperRod != null)
		{
			if (ModItems.ClayMoldRod != null)
			{
				GameRegistry.addShapelessRecipe(new ItemStack(ModItems.CopperRod, RodNumberReturned), new Object[] {getStackNoTemp(new ItemStack(ModItems.ClayMoldRod, 1, 2))});
			}
			
			if (ModItems.CopperRodNickelCoated != null)
			{
				// need to add one recipe for each damage level of the silver coated copper rod.
				for (int i = 0; i < ModItems.CopperRodDamage; i++)
				{
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CopperRod, 1, i), new Object[] { new ItemStack(ModItems.CopperRodNickelCoated, 1, i), ModOreDictionary.TFC_ITEM_KNIFE }));
				}
			}
			
			if (ModItems.CopperRodSilverCoated != null)
			{
				// need to add one recipe for each damage level of the silver coated copper rod.
				for (int i = 0; i < ModItems.CopperRodDamage; i++)
				{
					GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.CopperRod, 1, i), new Object[] { new ItemStack(ModItems.CopperRodSilverCoated, 1, i), ModOreDictionary.TFC_ITEM_KNIFE }));
				}
			}
		}
		
		// Tucker Bag v1
		if (ModItems.TuckerBagv1 != null)
		{
			GameRegistry.addRecipe(new ItemStack(ModItems.TuckerBagv1), new Object[] { 
				"FCF", 
				"FCF", 'C', new ItemStack(TFCItems.burlapCloth), 'F', new ItemStack(TFCItems.juteFiber) });			

			// repair recipe
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.TuckerBagv1), new Object[] { new ItemStack(ModItems.TuckerBagv1, 1, WILDCARD_VALUE), new ItemStack(TFCItems.burlapCloth), new ItemStack(TFCItems.juteFiber) }));
		}

		// Tucker Bag v2
		if (ModItems.TuckerBagv1 != null && ModItems.TuckerBagv2 != null && ModItems.WroughtIronBuckle != null)
		{
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.TuckerBagv2), new Object[] { new ItemStack(ModItems.TuckerBagv1), new ItemStack(ModItems.WroughtIronBuckle), new ItemStack(TFCItems.leather), new ItemStack(TFCItems.leather), ModOreDictionary.TFC_ITEM_KNIFE }));

			// repair recipe
			GameRegistry.addRecipe(new ShapelessOreRecipe(new ItemStack(ModItems.TuckerBagv2), new Object[] { new ItemStack(ModItems.TuckerBagv2, 1, WILDCARD_VALUE), new ItemStack(TFCItems.burlapCloth), new ItemStack(TFCItems.leather), ModOreDictionary.TFC_ITEM_KNIFE }));
		}
	}
	
	public static ItemStack getStackTemp(ItemStack is)
	{
		NBTTagCompound Temp = new NBTTagCompound();
		Temp.setBoolean("temp", true);
		is.setTagCompound(Temp);
		return is;
	}
	
	public static ItemStack	getStackNoTemp(ItemStack is)
	{
		NBTTagCompound noTemp = new NBTTagCompound();
		noTemp.setBoolean("noTemp", true);
		is.setTagCompound(noTemp);
		return is;
	}
}