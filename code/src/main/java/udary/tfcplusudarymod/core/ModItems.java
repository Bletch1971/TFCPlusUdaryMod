package udary.tfcplusudarymod.core;

import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fluids.FluidRegistry;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.handlers.FuelHandler;
import udary.tfcplusudarymod.items.ItemEmpty;
import udary.tfcplusudarymod.items.devices.ItemPotteryBattery;
import udary.tfcplusudarymod.items.fluids.ItemBlueSteelBucketFluid;
import udary.tfcplusudarymod.items.fluids.ItemCeramicJugMilk;
import udary.tfcplusudarymod.items.fluids.ItemGlassBottleFluid;
import udary.tfcplusudarymod.items.fluids.ItemGlassBottleMilk;
import udary.tfcplusudarymod.items.fluids.ItemGlassBottleWater;
import udary.tfcplusudarymod.items.fluids.ItemRedSteelBucketFluid;
import udary.tfcplusudarymod.items.fluids.ItemWoodenBucketFluid;
import udary.tfcplusudarymod.items.materials.ItemBuckle;
import udary.tfcplusudarymod.items.materials.ItemWire;
import udary.tfcplusudarymod.items.ores.ItemOre;
import udary.tfcplusudarymod.items.ores.ItemOreFlake;
import udary.tfcplusudarymod.items.pottery.ItemPotteryMold;
import udary.tfcplusudarymod.items.powders.ItemPowder;
import udary.tfcplusudarymod.items.tools.ItemCeramicBucket;
import udary.tfcplusudarymod.items.tools.ItemRod;
import udary.tfcplusudarymod.items.tools.ItemTuckerBag;

import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Constant.Global;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISmeltable.EnumTier;

import cpw.mods.fml.common.registry.GameRegistry;

public class ModItems 
{
	// Device values
	public static int CeramicBatteryCharge = 16;
	
	// Fuel values
	public static int CarbonRodBurnTime = 800;
	
	// Material values
	public static String CarbonName = "Carbon";
	public static int CarbonHarvestLevel = 0;		/** The level of material this tool can harvest */
	public static int CarbonUses = 50;				/** The number of uses this material allows. */
	public static float CarbonEfficiency = 7;		/** The strength of this tool material against blocks which it is effective against. */
	public static float CarbonDamage = 0;			/** Damage versus entities. */    
	public static int CarbonEnchantability = 5;		/** Defines the natural enchantability factor of the material. */
	
	// Metal Amount Values
	public static short BuckleMaxMetalAmount = 50;
	public static short RodMaxMetalAmount = 100;
	public static short WireMaxMetalAmount = 10;
	
	// Ore values	
	public static Metal CookedLimoniteMetalType = Global.PIGIRON;
	public static Metal NickelFlakeMetalType = Global.NICKEL;
	public static EnumTier NickelFlakeSmeltTier = EnumTier.TierIII;
	public static Metal SilverFlakeMetalType = Global.SILVER;
	public static EnumTier SilverFlakeSmeltTier = EnumTier.TierI;
	
	// Tool values
	public static int CarbonRodDamage = CarbonUses;
	public static int CopperRodDamage = TFCItems.copperUses;
	public static int CopperWireDamage = TFCItems.copperUses;
	public static int TuckerBagDamage = 2;
	public static int TuckerBagIIDamage = 5;
	public static int WroughtIronBuckleDamage = TFCItems.wroughtIronUses;
	
	// Materials
	public static ToolMaterial CarbonToolMaterial;
	
	public static Item EmptyItem;
	
	// Device items
	public static Item CeramicBattery;
	
	// Fluid items - Bottled
	public static Item BottledBrine;
	public static Item BottledLimewater;
	public static Item BottledMilk;
	public static Item BottledNitricAcid;
	public static Item BottledOliveOil;
	public static Item BottledSaltWater;
	public static Item BottledSulfuricAcid;
	public static Item BottledTannin;
	public static Item BottledVinegar;
	public static Item BottledWater;
	
	// Fluid items - Ceramic Jug
	public static Item CeramicJugMilk;
	
	// Fluid items - Steel Bucket
	public static Item BlueSteelBucketNitricAcid;
	public static Item BlueSteelBucketSulfuricAcid;
	public static Item RedSteelBucketNitricAcid;	
	public static Item RedSteelBucketSulfuricAcid;	
	
	// Fluid items - Wooden Bucket	
	public static Item WoodenBucketBeer;
	public static Item WoodenBucketBrine;
	public static Item WoodenBucketCider;
	public static Item WoodenBucketCornWhiskey;
	public static Item WoodenBucketLimewater;
	public static Item WoodenBucketOliveOil;
	public static Item WoodenBucketRum;
	public static Item WoodenBucketRyeWhiskey;
	public static Item WoodenBucketSake;
	public static Item WoodenBucketTannin;
	public static Item WoodenBucketVodka;
	public static Item WoodenBucketWhiskey;
	
	// Fluid Items - Ceramic Bucket
	public static Item CeramicBucket;
	public static Item CeramicBucketWater;
	public static Item CeramicBucketSaltWater;
	public static Item CeramicBucketHotWater;
	
	// Material items
	public static Item CopperWire;
	public static Item WroughtIronBuckle;
	
	// Ore items
	public static Item CookedLimonite;
	public static Item NickelFlake;
	public static Item SilverFlake;
	
	// Pottery items
	public static Item ClayMoldRod;

	// Powder items
	public static Item CookedLimonitePowder;
	
	// Tool items
	public static Item CarbonRod;
	public static Item CopperRod;
	public static Item CopperRodNickelCoated;
	public static Item CopperRodSilverCoated;
	public static Item TuckerBagv1;
	public static Item TuckerBagv2;
	
	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Items");
		
		registerMaterials();
		registerItems();
		registerFurnaceFuel();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Items");
	}
	
	private static void registerFurnaceFuel()
	{
		// 1 second = 20 burn time value
		if (CarbonRod != null)
		{
			FuelHandler.registerFuel(CarbonRod, CarbonRodBurnTime);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Furnace Fuel: " + StatCollector.translateToLocal(CarbonRod.getUnlocalizedName()));
		}
	}
	
	private static void registerItems()
	{
		Item item;
		
		EmptyItem = new ItemEmpty().setUnlocalizedName("empty");	
		item = GameRegistry.registerItem(EmptyItem , EmptyItem.getUnlocalizedName(), ModDetails.ModID);
		if (ModOptions.showVerboseStartup && item != null)
			System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		
		// Devices
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			CeramicBattery = new ItemPotteryBattery(CeramicBatteryCharge).setUnlocalizedName("Ceramic Battery");		
			item = GameRegistry.registerItem(CeramicBattery , CeramicBattery.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		// Fluids
		BottledWater = new ItemGlassBottleWater().setUnlocalizedName("Bottled Water");
		item = GameRegistry.registerItem(BottledWater, BottledWater.getUnlocalizedName(), ModDetails.ModID);
		if (ModOptions.showVerboseStartup && item != null)
			System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		
		if (ModOptions.enableTerraFirmaCraftAdditions)
		{
			// Bottled Fluids
			BottledBrine = new ItemGlassBottleFluid(TFCFluids.BRINE).setUnlocalizedName("Bottled Brine");
			item = GameRegistry.registerItem(BottledBrine, BottledBrine.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			BottledLimewater = new ItemGlassBottleFluid(TFCFluids.LIMEWATER).setUnlocalizedName("Bottled Limewater");
			item = GameRegistry.registerItem(BottledLimewater, BottledLimewater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			BottledMilk = new ItemGlassBottleMilk().setUnlocalizedName("Bottled Milk");
			item = GameRegistry.registerItem(BottledMilk, BottledMilk.getUnlocalizedName(), ModDetails.ModID);		
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			BottledOliveOil = new ItemGlassBottleFluid(TFCFluids.OLIVEOIL).setUnlocalizedName("Bottled Olive Oil");
			item = GameRegistry.registerItem(BottledOliveOil, BottledOliveOil.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			BottledSaltWater = new ItemGlassBottleFluid(TFCFluids.SALTWATER).setUnlocalizedName("Bottled Salt Water");
			item = GameRegistry.registerItem(BottledSaltWater, BottledSaltWater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			BottledVinegar = new ItemGlassBottleFluid(TFCFluids.VINEGAR).setUnlocalizedName("Bottled Vinegar");
			item = GameRegistry.registerItem(BottledVinegar, BottledVinegar.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			BottledTannin = new ItemGlassBottleFluid(TFCFluids.TANNIN).setUnlocalizedName("Bottled Tannin");
			item = GameRegistry.registerItem(BottledTannin, BottledTannin.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			// Ceramic Jug Fluids
			CeramicJugMilk = new ItemCeramicJugMilk().setUnlocalizedName("Jug Milk");
			item = GameRegistry.registerItem(CeramicJugMilk, CeramicJugMilk.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			// Wooden Bucket Fluids
			WoodenBucketBeer = new ItemWoodenBucketFluid(TFCFluids.BEER).setUnlocalizedName("Beer");
			item = GameRegistry.registerItem(WoodenBucketBeer, WoodenBucketBeer.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketBrine = new ItemWoodenBucketFluid(TFCFluids.BRINE).setUnlocalizedName("Brine");
			item = GameRegistry.registerItem(WoodenBucketBrine, WoodenBucketBrine.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			WoodenBucketCider = new ItemWoodenBucketFluid(TFCFluids.CIDER).setUnlocalizedName("Cider");
			item = GameRegistry.registerItem(WoodenBucketCider, WoodenBucketCider.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			WoodenBucketCornWhiskey = new ItemWoodenBucketFluid(TFCFluids.CORNWHISKEY).setUnlocalizedName("Corn Whiskey");
			item = GameRegistry.registerItem(WoodenBucketCornWhiskey, WoodenBucketCornWhiskey.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketLimewater = new ItemWoodenBucketFluid(TFCFluids.LIMEWATER).setUnlocalizedName("Limewater");
			item = GameRegistry.registerItem(WoodenBucketLimewater, WoodenBucketLimewater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			WoodenBucketOliveOil = new ItemWoodenBucketFluid(TFCFluids.OLIVEOIL).setUnlocalizedName("Olive Oil");
			item = GameRegistry.registerItem(WoodenBucketOliveOil, WoodenBucketOliveOil.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			WoodenBucketRum = new ItemWoodenBucketFluid(TFCFluids.RUM).setUnlocalizedName("Rum");
			item = GameRegistry.registerItem(WoodenBucketRum, WoodenBucketRum.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketRyeWhiskey = new ItemWoodenBucketFluid(TFCFluids.RYEWHISKEY).setUnlocalizedName("Rye Whiskey");
			item = GameRegistry.registerItem(WoodenBucketRyeWhiskey, WoodenBucketRyeWhiskey.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketSake = new ItemWoodenBucketFluid(TFCFluids.SAKE).setUnlocalizedName("Sake");
			item = GameRegistry.registerItem(WoodenBucketSake, WoodenBucketSake.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketTannin = new ItemWoodenBucketFluid(TFCFluids.TANNIN).setUnlocalizedName("Tannin");
			item = GameRegistry.registerItem(WoodenBucketTannin, WoodenBucketTannin.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketVodka = new ItemWoodenBucketFluid(TFCFluids.VODKA).setUnlocalizedName("Vodka");
			item = GameRegistry.registerItem(WoodenBucketVodka, WoodenBucketVodka.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			WoodenBucketWhiskey = new ItemWoodenBucketFluid(TFCFluids.WHISKEY).setUnlocalizedName("Whiskey");
			item = GameRegistry.registerItem(WoodenBucketWhiskey, WoodenBucketWhiskey.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModFluids.NITRICACID != null && FluidRegistry.isFluidRegistered(ModFluids.NITRICACID))
		{
			// Bottled Fluids
			BottledNitricAcid = new ItemGlassBottleFluid(ModFluids.NITRICACID).setUnlocalizedName("Bottled Nitric Acid");
			item = GameRegistry.registerItem(BottledNitricAcid, BottledNitricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			// Steel Bucket Fluids
			BlueSteelBucketNitricAcid = new ItemBlueSteelBucketFluid(ModFluids.NITRICACID).setUnlocalizedName("Blue Steel Bucket Nitric Acid");
			item = GameRegistry.registerItem(BlueSteelBucketNitricAcid, BlueSteelBucketNitricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			RedSteelBucketNitricAcid = new ItemRedSteelBucketFluid(ModFluids.NITRICACID).setUnlocalizedName("Red Steel Bucket Nitric Acid");
			item = GameRegistry.registerItem(RedSteelBucketNitricAcid, RedSteelBucketNitricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModFluids.SULFURICACID != null && FluidRegistry.isFluidRegistered(ModFluids.SULFURICACID))
		{
			// Bottled Fluids
			BottledSulfuricAcid = new ItemGlassBottleFluid(ModFluids.SULFURICACID).setUnlocalizedName("Bottled Sulfuric Acid");
			item = GameRegistry.registerItem(BottledSulfuricAcid, BottledSulfuricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));

			// Steel Bucket Fluids
			BlueSteelBucketSulfuricAcid  = new ItemBlueSteelBucketFluid(ModFluids.SULFURICACID).setUnlocalizedName("Blue Steel Bucket Sulfuric Acid");
			item = GameRegistry.registerItem(BlueSteelBucketSulfuricAcid, BlueSteelBucketSulfuricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			RedSteelBucketSulfuricAcid  = new ItemRedSteelBucketFluid(ModFluids.SULFURICACID).setUnlocalizedName("Red Steel Bucket Sulfuric Acid");
			item = GameRegistry.registerItem(RedSteelBucketSulfuricAcid, RedSteelBucketSulfuricAcid.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}

		if (ModOptions.enableEvaporationMod && ModOptions.enableHotSpringWater)
		{
			// Ceramic Bucket Fluids
			CeramicBucket = new ItemCeramicBucket(Blocks.air).setUnlocalizedName("Ceramic Bucket");
			item = GameRegistry.registerItem(CeramicBucket, CeramicBucket.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			CeramicBucketWater = new ItemCeramicBucket(TFCBlocks.freshWater, CeramicBucket).setUnlocalizedName("Ceramic Bucket Water");
			item = GameRegistry.registerItem(CeramicBucketWater, CeramicBucketWater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			CeramicBucketSaltWater = new ItemCeramicBucket(TFCBlocks.saltWater, CeramicBucket).setUnlocalizedName("Ceramic Bucket Salt Water");
			item = GameRegistry.registerItem(CeramicBucketSaltWater, CeramicBucketSaltWater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			CeramicBucketHotWater = new ItemCeramicBucket(TFCBlocks.hotWater, CeramicBucket).setUnlocalizedName("Ceramic Bucket Hot Water");
			item = GameRegistry.registerItem(CeramicBucketHotWater, CeramicBucketHotWater.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		// Materials
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			CopperWire = new ItemWire(TFCItems.copperToolMaterial, CopperWireDamage).setSmeltable(Global.COPPER, WireMaxMetalAmount, true, EnumTier.TierI).setUnlocalizedName("Copper Wire");
			item = GameRegistry.registerItem(CopperWire, CopperWire.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModOptions.enableTuckerBagMod)
		{
			if (ModOptions.enableReinforcedBag)
			{
				WroughtIronBuckle = new ItemBuckle(TFCItems.ironToolMaterial, WroughtIronBuckleDamage).setSmeltable(Global.WROUGHTIRON, BuckleMaxMetalAmount, true, EnumTier.TierI).setUnlocalizedName("Wrought Iron Buckle");
				item = GameRegistry.registerItem(WroughtIronBuckle, WroughtIronBuckle.getUnlocalizedName(), ModDetails.ModID);
				if (ModOptions.showVerboseStartup && item != null)
					System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			}
		}
		
		// Ores
		if (ModOptions.enableGalenaMod)
		{
			SilverFlake = new ItemOreFlake("Silver Flake", SilverFlakeMetalType, SilverFlakeSmeltTier);
			item = GameRegistry.registerItem(SilverFlake , SilverFlake.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModOptions.enableLimoniteMod)
		{
			CookedLimonite = new ItemOre("Cooked Limonite", CookedLimoniteMetalType);
			item = GameRegistry.registerItem(CookedLimonite , CookedLimonite.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			NickelFlake = new ItemOreFlake("Nickel Flake", NickelFlakeMetalType, NickelFlakeSmeltTier);
			item = GameRegistry.registerItem(NickelFlake , NickelFlake.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		// Pottery
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			ClayMoldRod = new ItemPotteryMold().setMetaNames(new String[]{"Clay Mold Rod", "Ceramic Mold Rod", "Ceramic Mold Rod Copper"}).setUnlocalizedName("Rod Mold");
			item = GameRegistry.registerItem(ClayMoldRod , ClayMoldRod.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}

		// Powders
		if (ModOptions.enableLimoniteMod)
		{
			CookedLimonitePowder = new ItemPowder().setUnlocalizedName("Cooked Limonite Powder");
			item = GameRegistry.registerItem(CookedLimonitePowder, CookedLimonitePowder.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		// Tools
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			if (CarbonToolMaterial != null)
			{
				CarbonRod = new ItemRod(CarbonToolMaterial, CarbonRodDamage).setIsCathode(true).setWeight(EnumWeight.LIGHT).setUnlocalizedName("Carbon Rod");
				item = GameRegistry.registerItem(CarbonRod, CarbonRod.getUnlocalizedName(), ModDetails.ModID);
				if (ModOptions.showVerboseStartup && item != null)
					System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			}
		}

		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			CopperRod = new ItemRod(TFCItems.copperToolMaterial, CopperRodDamage).setIsAnode(true).setSmeltable(Global.COPPER, RodMaxMetalAmount, true, EnumTier.TierI).setUnlocalizedName("Copper Rod");
			item = GameRegistry.registerItem(CopperRod, CopperRod.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModOptions.enableGalenaMod)
		{
			CopperRodSilverCoated = new ItemRod(TFCItems.copperToolMaterial, CopperRodDamage).setIsAnode(true).setAnodisedMetal(Global.SILVER).setUnlocalizedName("Copper Rod Silver Coated").setCreativeTab(null);
			item = GameRegistry.registerItem(CopperRodSilverCoated, CopperRodSilverCoated.getUnlocalizedName(), ModDetails.ModID);
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModOptions.enableLimoniteMod)
		{
			CopperRodNickelCoated = new ItemRod(TFCItems.copperToolMaterial, CopperRodDamage).setIsAnode(true).setAnodisedMetal(Global.NICKEL).setUnlocalizedName("Copper Rod Nickel Coated").setCreativeTab(null);
			item = GameRegistry.registerItem(CopperRodNickelCoated, CopperRodNickelCoated.getUnlocalizedName(), ModDetails.ModID);			
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
		}
		
		if (ModOptions.enableTuckerBagMod)
		{
			TuckerBagv1 = new ItemTuckerBag(TuckerBagDamage, EnumTuckerBagVersion.VERSION_1).setUnlocalizedName("Tucker Bag");
			item = GameRegistry.registerItem(TuckerBagv1, TuckerBagv1.getUnlocalizedName(), ModDetails.ModID);	
			if (ModOptions.showVerboseStartup && item != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			
			if (ModOptions.enableReinforcedBag)
			{
				TuckerBagv2 = new ItemTuckerBag(TuckerBagIIDamage, EnumTuckerBagVersion.VERSION_2).setUnlocalizedName("Tucker Bag v2");
				item = GameRegistry.registerItem(TuckerBagv2, TuckerBagv2.getUnlocalizedName(), ModDetails.ModID);				
				if (ModOptions.showVerboseStartup && item != null)
					System.out.println("[" + ModDetails.ModName + "] Registered Item: " + StatCollector.translateToLocal(item.getUnlocalizedName()) + "@" + Item.getIdFromItem(item));
			}
		}
	}
	
	private static void registerMaterials()
	{
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			CarbonToolMaterial = EnumHelper.addToolMaterial(CarbonName, CarbonHarvestLevel, CarbonUses, CarbonEfficiency, CarbonDamage, CarbonEnchantability);
			if (ModOptions.showVerboseStartup && CarbonToolMaterial != null)
				System.out.println("[" + ModDetails.ModName + "] Registered Material: " + CarbonToolMaterial.name());
		}
	}
}
