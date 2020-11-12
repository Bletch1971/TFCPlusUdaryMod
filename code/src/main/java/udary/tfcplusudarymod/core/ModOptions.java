package udary.tfcplusudarymod.core;

import java.io.File;

import udary.tfcplusudarymod.TFCUdaryMod;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;

public class ModOptions
{
	// Debug
	public static boolean showVerboseStartup = false;
	
	// General
	public static boolean showDamageValues = false;

	// Mods - General
	public static boolean enableAlloyCalculatorMod = true;
	public static boolean enableDryingMatMod = true;
	public static boolean enableEvaporationMod = true;
	public static boolean enableGalenaMod = true;
	public static boolean enableLimoniteMod = true;
	public static boolean enableTerraFirmaCraftAdditions = false;
	public static boolean enableTuckerBagMod = true;
	
	// Mods - Anodising
	public static int anodisingDelay = 25;	
	
	// Mods - Cooking
	public static int cookingDelay = 25;	
	
	// Mods - Drying
	public static int dryingDelay = 100;
	public static int dryingTemperature = 20;
	
	// Mods - Evaporation
	public static boolean enableHotSpringWater = true;
	public static int evaporationDelay = 25;
	public static int evaporationTemperature = 30;
	
	public static int hotWaterEvaporationAmount = 100;
	public static int nitricAcidEvaporationAmount = 100;
	public static int saltwaterEvaporationAmount = 100;
	public static int sulfuricAcidEvaporationAmount = 100;
	
	// Mods - TFC Additions
	public static int stickConversion = 4;
	
	// Mods - Tucker Bag
	public static boolean enableReinforcedBag = true;
	public static boolean canCaptureTFCBear = false; 
	public static boolean canCaptureTFCChicken = true; 
	public static boolean canCaptureTFCCow = true; 
	public static boolean canCaptureTFCDeer = false; 
	public static boolean canCaptureTFCHorse = true; 
	public static boolean canCaptureTFCPheasant = false; 
	public static boolean canCaptureTFCPig = true; 
	public static boolean canCaptureTFCSheep = true; 
	public static boolean canCaptureTFCWolf = false; 
	
	// WAILA
	public static boolean loadTerraFirmaCraftWailaClasses = false;
	public static boolean loadUdaryModWailaClasses = true;
	
	public static boolean getBooleanFor(Configuration config,String heading, String item, boolean value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getBoolean(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static boolean getBooleanFor(Configuration config,String heading, String item, boolean value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getBoolean(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static int getIntFor(Configuration config, String heading, String item, int value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getInt(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static int getIntFor(Configuration config,String heading, String item, int value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getInt(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Integer, config wasn't loaded properly!");
		}
		return value;
	}

	public static double getDoubleFor(Configuration config,String heading, String item, double value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getDouble(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Double, config wasn't loaded properly!");
		}
		return value;
	}

	public static double getDoubleFor(Configuration config,String heading, String item, double value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getDouble(value);
		}
		catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add Double, config wasn't loaded properly!");
		}
		return value;
	}

	public static String getStringFor(Configuration config, String heading, String item, String value)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			return prop.getString();
		} catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add String, config wasn't loaded properly!");
		}
		return value;
	}

	public static String getStringFor(Configuration config, String heading, String item, String value, String comment)
	{
		if (config == null)
			return value;
		try
		{
			Property prop = config.get(heading, item, value);
			prop.comment = comment;
			return prop.getString();
		} catch (Exception e)
		{
			System.out.println("[" + ModDetails.ModName + "] Error while trying to add String, config wasn't loaded properly!");
		}
		return value;
	}
	
	public static void loadSettings()
	{	
		System.out.println("[" + ModDetails.ModName + "] Loading options from configuration file.");

		Configuration config;

		try
		{
			config = new Configuration(new File(TFCUdaryMod.instance.getMinecraftDirectory(), ModDetails.ConfigFilePath+ModDetails.ConfigFileName));
			config.load();
		} 
		catch (Exception ex) 
		{
			System.out.println("["+ModDetails.ModName+"] Error while trying to access settings configuration!");
			config = null;
		}
		
		/** Start Here **/

		// Debug
		ModOptions.showVerboseStartup = ModOptions.getBooleanFor(config, "Debug", "showVerboseStartup", false, "Set this to true if you want to more detailed startup information.");
		
		// General
		ModOptions.showDamageValues = ModOptions.getBooleanFor(config, "General", "showDamageValues", false, "Set this to true if you want to see item damage values.");

		// Mods - General
		ModOptions.enableAlloyCalculatorMod = ModOptions.getBooleanFor(config, "Mods", "enableAlloyCalculatorMod", true, "Set this to true if you want to turn on the Alloy Calculator Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableDryingMatMod = ModOptions.getBooleanFor(config, "Mods", "enableDryingMatMod", true, "Set this to true if you want to turn on the Drying Mat Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableEvaporationMod = ModOptions.getBooleanFor(config, "Mods", "enableEvaporationMod", true, "Set this to true if you want to turn on the Evaporation Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableGalenaMod = ModOptions.getBooleanFor(config, "Mods", "enableGalenaMod", true, "Set this to true if you want to turn on the Galena Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableLimoniteMod = ModOptions.getBooleanFor(config, "Mods", "enableLimoniteMod", true, "Set this to true if you want to turn on the Limonite Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableTerraFirmaCraftAdditions = ModOptions.getBooleanFor(config, "Mods", "enableTerraFirmaCraftAdditions", false, "Set this to true if you want to turn on additional TFC items, blocks and recipes. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.enableTuckerBagMod = ModOptions.getBooleanFor(config, "Mods", "enableTuckerBagMod", true, "Set this to true if you want to turn on the Tucker Bag Mod. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");

		// Mods - Anodising
		ModOptions.anodisingDelay = ModOptions.getIntFor(config, "Anodising", "anodisingDelay", 25, "Set this value to the number of ticks between each anodising process attempt. Set to 0 to turn off anodising.");

		// Mods - Cooking
		ModOptions.cookingDelay = ModOptions.getIntFor(config, "Cooking", "cookingDelay", 25, "Set this value to the number of ticks between each cooking process attempt. Set to 0 to turn off cooking.");

		// Mods - Drying
		ModOptions.dryingDelay = ModOptions.getIntFor(config, "Drying", "dryingDelay", 100, "Set this value to the number of ticks between each drying process attempt. Set to 0 to turn off drying.");
		ModOptions.dryingTemperature = ModOptions.getIntFor(config, "Drying", "dryingTemperature", 20, "Set this value to the temperature in which food exposed to the sun starts drying.");

		// Mods - Evaporation
		ModOptions.enableHotSpringWater = ModOptions.getBooleanFor(config, "Evaporation", "enableHotSpringWater", true, "Set this to true to allow the evaporation of Hot Spring Water. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.evaporationDelay = ModOptions.getIntFor(config, "Evaporation", "evaporationDelay", 25, "Set this value to the number of ticks between each evaporation process attempt. Set to 0 to turn off evaporation.");
		ModOptions.evaporationTemperature = ModOptions.getIntFor(config, "Evaporation", "evaporationTemperature", 30, "Set this value to the temperature in which fluids exposed to the sun starts evaporating.");

		ModOptions.hotWaterEvaporationAmount = ModOptions.getIntFor(config, "Evaporation", "hotWaterEvaporationAmount", 100, "Set this value to the amount of hot water that must be evaporated to get one item.");
		ModOptions.nitricAcidEvaporationAmount = ModOptions.getIntFor(config, "Evaporation", "nitricAcidEvaporationAmount", 100, "Set this value to the amount of nitric acid that must be evaporated to get one item.");
		ModOptions.saltwaterEvaporationAmount = ModOptions.getIntFor(config, "Evaporation", "saltwaterEvaporationAmount", 100, "Set this value to the amount of salt water that must be evaporated to get one item.");
		ModOptions.sulfuricAcidEvaporationAmount = ModOptions.getIntFor(config, "Evaporation", "sulfuricAcidEvaporationAmount", 100, "Set this value to the amount of sulfuric acid that must be evaporated to get one item.");
		
		// Mods - TFC Additions
		ModOptions.stickConversion = ModOptions.getIntFor(config, "TFCAdditions", "stickConversion", 4, "Set the value to convert one log into n sticks.");
		
		// Mods - Tucker Bag
		ModOptions.enableReinforcedBag = ModOptions.getBooleanFor(config, "TuckerBag", "enableReinforcedBag", true, "Set this to true to allow the creation and use of the reinforced tucker bag. ** NOTE: Turning this off will cause some items and blocks to be removed from your world.");
		ModOptions.canCaptureTFCBear = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCBear", false, "Set this to true to allow tucker bags to capture TFC Bears.");
		ModOptions.canCaptureTFCChicken = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCChicken", true, "Set this to true to allow tucker bags to capture TFC Chickens.");
		ModOptions.canCaptureTFCCow = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCCow", true, "Set this to true to allow tucker bags to capture TFC Cows.");
		ModOptions.canCaptureTFCDeer = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCDeer", false, "Set this to true to allow tucker bags to capture TFC Deer.");
		ModOptions.canCaptureTFCHorse = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCHorse", true, "Set this to true to allow tucker bags to capture TFC Horses.");
		ModOptions.canCaptureTFCPheasant = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCPheasant", false, "Set this to true to allow tucker bags to capture TFC Pheasant.");
		ModOptions.canCaptureTFCPig = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCPig", true, "Set this to true to allow tucker bags to capture TFC Pigs.");
		ModOptions.canCaptureTFCSheep = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCSheep", true, "Set this to true to allow tucker bags to capture TFC Sheep.");
		ModOptions.canCaptureTFCWolf = ModOptions.getBooleanFor(config, "TuckerBag", "canCaptureTFCWolf", false, "Set this to true to allow tucker bags to capture TFC Wolves.");
		
		// WAILA
		ModOptions.loadTerraFirmaCraftWailaClasses = ModOptions.getBooleanFor(config, "WAILA", "loadTerraFirmaCraftWailaClasses", false, "Set this to true if you want to register additional WAILA classes for TerraFirmaCraft mod.");
		ModOptions.loadUdaryModWailaClasses = ModOptions.getBooleanFor(config, "WAILA", "loadUdaryModWailaClasses", true, "Set this to true if you want to register additional WAILA classes for Udary mod.");
				
		/** End Here*/
		if (config != null)
			config.save();

		System.out.println("[" + ModDetails.ModName + "] Done loading options from configuration file.");
	}
	
	@SuppressWarnings("static-access")
	public static void loadSettings(ModOptions options)
	{
		System.out.println("[" + ModDetails.ModName + "] Loading options from options class.");

		/** Start Here **/

		// Debug
		ModOptions.showVerboseStartup = options == null ? false : options.showVerboseStartup;

		// General
		ModOptions.showDamageValues = options == null ? false : options.showDamageValues;

		// Mods - General
		ModOptions.enableAlloyCalculatorMod = options == null ? true : options.enableAlloyCalculatorMod;
		ModOptions.enableDryingMatMod = options == null ? true : options.enableDryingMatMod;
		ModOptions.enableEvaporationMod = options == null ? true : options.enableEvaporationMod;
		ModOptions.enableGalenaMod = options == null ? true : options.enableGalenaMod;
		ModOptions.enableLimoniteMod = options == null ? true : options.enableLimoniteMod;
		ModOptions.enableTerraFirmaCraftAdditions = options == null ? true : options.enableTerraFirmaCraftAdditions;
		ModOptions.enableTuckerBagMod = options == null ? true : options.enableTuckerBagMod;

		// Mods - Anodising
		ModOptions.anodisingDelay = options == null ? 25 : options.anodisingDelay;

		// Mods - Cooking
		ModOptions.cookingDelay = options == null ? 25 : options.cookingDelay;
		
		// Mods - Drying
		ModOptions.dryingDelay = options == null ? 25 : options.dryingDelay;
		ModOptions.dryingTemperature = options == null ? 25 : options.dryingTemperature;
		
		// Mods - Evaporation
		ModOptions.enableHotSpringWater = options == null ? true : options.enableHotSpringWater;
		ModOptions.evaporationDelay = options == null ? 25 : options.evaporationDelay;
		ModOptions.evaporationTemperature = options == null ? 30 : options.evaporationTemperature;

		ModOptions.hotWaterEvaporationAmount = options == null ? 100 : options.hotWaterEvaporationAmount;
		ModOptions.nitricAcidEvaporationAmount = options == null ? 100 : options.nitricAcidEvaporationAmount;
		ModOptions.saltwaterEvaporationAmount = options == null ? 100 : options.saltwaterEvaporationAmount;
		ModOptions.sulfuricAcidEvaporationAmount = options == null ? 100 : options.sulfuricAcidEvaporationAmount;
		
		// Mods - TFC Additions
		ModOptions.stickConversion = options == null ? 8 : options.stickConversion;
		
		// Mods - Tucker Bag
		ModOptions.enableReinforcedBag = options == null ? false : options.enableReinforcedBag;
		ModOptions.canCaptureTFCBear = options == null ? false : options.canCaptureTFCBear;
		ModOptions.canCaptureTFCChicken = options == null ? true : options.canCaptureTFCChicken;
		ModOptions.canCaptureTFCCow = options == null ? true : options.canCaptureTFCCow;
		ModOptions.canCaptureTFCDeer = options == null ? false : options.canCaptureTFCDeer;
		ModOptions.canCaptureTFCHorse = options == null ? true : options.canCaptureTFCHorse;
		ModOptions.canCaptureTFCPheasant = options == null ? false : options.canCaptureTFCPheasant;
		ModOptions.canCaptureTFCPig = options == null ? true : options.canCaptureTFCPig;
		ModOptions.canCaptureTFCSheep = options == null ? true : options.canCaptureTFCSheep;
		ModOptions.canCaptureTFCWolf = options == null ? false : options.canCaptureTFCWolf;
		
		// WAILA
		ModOptions.loadTerraFirmaCraftWailaClasses = options == null ? false : options.loadTerraFirmaCraftWailaClasses;
		ModOptions.loadUdaryModWailaClasses = options == null ? false : options.loadUdaryModWailaClasses;
		
		/** End Here*/

		System.out.println("[" + ModDetails.ModName + "] Done loading options from options class.");
	}
}