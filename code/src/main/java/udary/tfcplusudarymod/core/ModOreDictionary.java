package udary.tfcplusudarymod.core;

import java.util.ArrayList;

import com.dunk.tfc.Core.Recipes;
import com.dunk.tfc.api.TFCOptions;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class ModOreDictionary 
{
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	
	// TFC names
	public static final String TFC_GEM_CHIPPED = "gemChipped";
	public static final String TFC_ITEM_KNIFE = "itemKnife";
	public static final String TFC_ITEM_HAMMER= "itemHammer";
	public static final String TFC_WOOD_LUMBER = "woodLumber";
	public static final String TFC_BUCKET_EMPTY = "bucketEmpty";
	public static final String TFC_BUCKET_WATER = "bucketWater";
	public static final String TFC_BUCKET_FRESHWATER = "bucketFreshWater";
	public static final String TFC_BUCKET_SALTWATER = "bucketSaltWater";

	// Mod Names
	public static final String GEM_FLAWED = "gemFlawed";
	public static final String GEM_NORMAL = "gemNormal";
	public static final String GEM_FLAWLESS = "gemFlawless";
	public static final String GEM_EXQUISITE = "gemExquisite";
	public static final String ORE_COOKEDLIMINOTE = "oreCookedLimonite";
	public static final String ORE_NICKELFLAKE = "oreNickelFlake";
	public static final String ORE_SILVERFLAKE = "oreSilverFlake";
	public static final String POWDER_ZINC = "dustZinc";
	public static final String POWDER_ZINCCHLORIDE = "dustZincChloride";
	public static final String BUCKET_NITRICACID = "bucketNitricAcid";
	public static final String BUCKET_SULFURICACID = "bucketSulfuricAcid";
	public static final String BUCKET_HOTWATER = "bucketHotWater";

	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Ore Dictionary");
		
		registerExtraTFCOreDictionary();
		registerOreDictionary();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Ore Dictionary");
	}

	private static void registerExtraTFCOreDictionary()
	{
		ArrayList<ItemStack> ores;

		//Gems
		ores = OreDictionary.getOres(TFC_GEM_CHIPPED);
		if (ores == OreDictionary.EMPTY_LIST || ores.size() == 0)
		{
			for (Item item : Recipes.gems)
			{
				OreDictionary.registerOre(TFC_GEM_CHIPPED, new ItemStack(item, 1, 0));
			}
		}

		ores = OreDictionary.getOres(GEM_FLAWED);
		if (ores == OreDictionary.EMPTY_LIST || ores.size() == 0)
		{
			for (Item item : Recipes.gems)
			{
				OreDictionary.registerOre(GEM_FLAWED, new ItemStack(item, 1, 1));
			}
		}

		ores = OreDictionary.getOres(GEM_NORMAL);
		if (ores == OreDictionary.EMPTY_LIST || ores.size() == 0)
		{
			for (Item item : Recipes.gems)
			{
				OreDictionary.registerOre(GEM_NORMAL, new ItemStack(item, 1, 2));
			}
		}

		ores = OreDictionary.getOres(GEM_FLAWLESS);
		if (ores == OreDictionary.EMPTY_LIST || ores.size() == 0)
		{
			for (Item item : Recipes.gems)
			{
				OreDictionary.registerOre(GEM_FLAWLESS, new ItemStack(item, 1, 3));
			}
		}

		ores = OreDictionary.getOres(GEM_EXQUISITE);
		if (ores == OreDictionary.EMPTY_LIST || ores.size() == 0)
		{
			for (Item item : Recipes.gems)
			{
				OreDictionary.registerOre(GEM_EXQUISITE, new ItemStack(item, 1, 4));
			}
		}
	}
	private static void registerOreDictionary()
	{
		// Ores
		if (ModItems.CookedLimonite != null)
		{
			OreDictionary.registerOre(ORE_COOKEDLIMINOTE, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.smallOreUnits));
			OreDictionary.registerOre(ORE_COOKEDLIMINOTE, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.poorOreUnits));
			OreDictionary.registerOre(ORE_COOKEDLIMINOTE, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.normalOreUnits));
			OreDictionary.registerOre(ORE_COOKEDLIMINOTE, new ItemStack(ModItems.CookedLimonite, 1, TFCOptions.richOreUnits));
		}

		if (ModItems.NickelFlake != null)
		{
			OreDictionary.registerOre(ORE_NICKELFLAKE, new ItemStack(ModItems.NickelFlake, 1, ModGlobal.tinyOreUnits));
			OreDictionary.registerOre(ORE_NICKELFLAKE, new ItemStack(ModItems.NickelFlake, 1, TFCOptions.smallOreUnits));
		}
		
		if (ModItems.SilverFlake != null)
		{
			OreDictionary.registerOre(ORE_SILVERFLAKE, new ItemStack(ModItems.SilverFlake, 1, ModGlobal.tinyOreUnits));
			OreDictionary.registerOre(ORE_SILVERFLAKE, new ItemStack(ModItems.SilverFlake, 1, TFCOptions.smallOreUnits));
		}
		
		// Ore Powders
		
		// Materials
		
		// Tools
		if (ModItems.CeramicBucket != null)
			OreDictionary.registerOre(TFC_BUCKET_EMPTY, new ItemStack(ModItems.CeramicBucket));

		// Miscellaneous Blocks
		
		// Miscellaneous Items
		if (ModItems.BlueSteelBucketNitricAcid != null)
			OreDictionary.registerOre(BUCKET_NITRICACID, new ItemStack(ModItems.BlueSteelBucketNitricAcid));
		
		if (ModItems.RedSteelBucketNitricAcid != null)
			OreDictionary.registerOre(BUCKET_NITRICACID, new ItemStack(ModItems.RedSteelBucketNitricAcid));
		
		if (ModItems.BlueSteelBucketSulfuricAcid != null)
			OreDictionary.registerOre(BUCKET_SULFURICACID, new ItemStack(ModItems.BlueSteelBucketSulfuricAcid));
		
		if (ModItems.RedSteelBucketSulfuricAcid != null)
			OreDictionary.registerOre(BUCKET_SULFURICACID, new ItemStack(ModItems.RedSteelBucketSulfuricAcid));
		
		if (ModItems.CeramicBucketWater != null)
		{
			OreDictionary.registerOre(TFC_BUCKET_WATER, new ItemStack(ModItems.CeramicBucketWater));
			OreDictionary.registerOre(TFC_BUCKET_FRESHWATER, new ItemStack(ModItems.CeramicBucketWater));
		}

		if (ModItems.CeramicBucketSaltWater != null)
			OreDictionary.registerOre(TFC_BUCKET_SALTWATER, new ItemStack(ModItems.CeramicBucketSaltWater));

		if (ModItems.CeramicBucketHotWater != null)
			OreDictionary.registerOre(BUCKET_HOTWATER, new ItemStack(ModItems.CeramicBucketHotWater));
	}
}
