package udary.tfcplusudarymod.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import com.dunk.tfc.Core.FluidBaseTFC;
import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.api.Food;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;

public class ModFluids
{
	// Fluids
	public static Fluid NITRICACID;
	public static Fluid SULFURICACID;
	
	// Fluid values
	public static final int AnodisingVesselVolume = 5000;
	public static final int BlueSteelBucketVolume = 1000;
	public static final int CeramicBucketVolume = 1000;
	public static final int CeramicJugVolume = 1000;
	public static final int EvaporatorPanVolume = 2000;
	public static final int GlassBottleVolume = 250;
	public static final int RedSteelBucketVolume = 1000;
	public static final int WoodenBucketVolume = 1000;
	
	// Fluid multipliers
	public static final int MilkDrinkMultiplier = 16;
	public static final int WaterDrinkMultiplier = 24;
	
	// Fluid decay rates
	public static final float MilkDecayRate = 6.0F;
	
	// Fluid max food weights
	public static final float CeramicJugMaxFoodWeight = 80.0F;
	public static final float GlassBottleMaxFoodWeight = 20.0F;
	
	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Fluids");
		
		registerFluids();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Fluids");		
	}
	
	public static void initialiseFluidContainers()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Fluid Containers");
		
		registerFluidContainers();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Fluid Containers");		
	}
	
	private static void registerFluids()
	{	
		// update existing fluids
		if (TFCFluids.FRESHWATER != null && TFCFluids.FRESHWATER instanceof FluidBaseTFC)
		{
			// change the colour of the TFC fresh water fluid 
			((FluidBaseTFC)TFCFluids.FRESHWATER).setBaseColor(0x355ac1);
		}

		if (TFCFluids.SALTWATER != null && TFCFluids.SALTWATER instanceof FluidBaseTFC)
		{
			// change the colour of the TFC salt water fluid
			((FluidBaseTFC)TFCFluids.SALTWATER).setBaseColor(0x1B4A63);
		}

		if (ModOptions.showVerboseStartup)
		{
			for (int index = 0; index < FluidRegistry.getMaxID(); index++)
			{
				Fluid fluid = FluidRegistry.getFluid(index);
				if (fluid != null)
					System.out.println("[" + ModDetails.ModName + "] Existing Fluid: " + fluid.getName() + "@" + fluid.getID());
			}
		}
		
		// register new fluids
		if (ModOptions.enableGalenaMod)
		{
			// Nitric acid
			NITRICACID = new FluidBaseTFC("nitricacid").setBaseColor(0xf3e8be);
			FluidRegistry.registerFluid(NITRICACID);
			if (ModOptions.showVerboseStartup && FluidRegistry.isFluidRegistered(NITRICACID))
				System.out.println("[" + ModDetails.ModName + "] Registered Fluid: " + NITRICACID.getName() + "@" + NITRICACID.getID());
		}
		
		if (ModOptions.enableGalenaMod || ModOptions.enableLimoniteMod)
		{
			// Sulfuric acid
			SULFURICACID = new FluidBaseTFC("sulfuricacid").setBaseColor(0xd9f2fa);
			FluidRegistry.registerFluid(SULFURICACID);
			if (ModOptions.showVerboseStartup && FluidRegistry.isFluidRegistered(NITRICACID))
				System.out.println("[" + ModDetails.ModName + "] Registered Fluid: " + SULFURICACID.getName() + "@" + SULFURICACID.getID());
		}
	}
	
	private static void registerFluidContainers()
	{
		ItemStack filledContainer;
		
		// Beer
		if (FluidRegistry.isFluidRegistered(TFCFluids.BEER))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketBeer != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketBeer);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BEER, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Brine
		if (FluidRegistry.isFluidRegistered(TFCFluids.BRINE))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledBrine != null)
			{
				filledContainer = new ItemStack(ModItems.BottledBrine);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketBrine != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketBrine);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.BRINE, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Cider
		if (FluidRegistry.isFluidRegistered(TFCFluids.CIDER))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketCider != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketCider);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.CIDER, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Corn Whiskey
		if (FluidRegistry.isFluidRegistered(TFCFluids.CORNWHISKEY))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketCornWhiskey != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketCornWhiskey);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.CORNWHISKEY, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Fresh water
		if (FluidRegistry.isFluidRegistered(TFCFluids.FRESHWATER))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledWater != null)
			{
				filledContainer = new ItemStack(ModItems.BottledWater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}

			if (ModItems.CeramicBucket != null && ModItems.CeramicBucketWater != null)
			{
				filledContainer = new ItemStack(ModItems.CeramicBucketWater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.FRESHWATER, CeramicBucketVolume), filledContainer, new ItemStack(ModItems.CeramicBucket));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Hot Water
		if (FluidRegistry.isFluidRegistered(TFCFluids.HOTWATER))
		{
			if (ModItems.CeramicBucket != null && ModItems.CeramicBucketHotWater != null)
			{
				filledContainer = new ItemStack(ModItems.CeramicBucketHotWater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.HOTWATER, CeramicBucketVolume), filledContainer, new ItemStack(ModItems.CeramicBucket));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Lime water
		if (FluidRegistry.isFluidRegistered(TFCFluids.LIMEWATER))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledLimewater != null)
			{
				filledContainer = new ItemStack(ModItems.BottledLimewater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.LIMEWATER, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketLimewater != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketLimewater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.LIMEWATER, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Milk
		if (FluidRegistry.isFluidRegistered(TFCFluids.MILK))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledMilk != null)
			{
				filledContainer = new ItemStack(ModItems.BottledMilk);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILK, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}

			if (TFCItems.potteryJug != null && ModItems.CeramicJugMilk != null)
			{
				filledContainer = new ItemStack(ModItems.CeramicJugMilk);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.MILK, CeramicJugVolume), filledContainer, new ItemStack(TFCItems.potteryJug, 1, 1));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Nitric acid
		if (ModFluids.NITRICACID != null && FluidRegistry.isFluidRegistered(ModFluids.NITRICACID))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledNitricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.BottledNitricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(NITRICACID, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}

			if (TFCItems.blueSteelBucketEmpty != null && ModItems.BlueSteelBucketNitricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.BlueSteelBucketNitricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(NITRICACID, BlueSteelBucketVolume), filledContainer, new ItemStack(TFCItems.blueSteelBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.redSteelBucketEmpty != null && ModItems.RedSteelBucketNitricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.RedSteelBucketNitricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(NITRICACID, RedSteelBucketVolume), filledContainer, new ItemStack(TFCItems.redSteelBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Olive Oil
		if (FluidRegistry.isFluidRegistered(TFCFluids.OLIVEOIL))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledOliveOil != null)
			{
				filledContainer = new ItemStack(ModItems.BottledOliveOil);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.OLIVEOIL, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketOliveOil != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketOliveOil);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.OLIVEOIL, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Rum
		if (FluidRegistry.isFluidRegistered(TFCFluids.RUM))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketRum != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketRum);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.RUM, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Rye Whiskey
		if (FluidRegistry.isFluidRegistered(TFCFluids.RYEWHISKEY))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketRyeWhiskey != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketRyeWhiskey);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.RYEWHISKEY, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Sake
		if (FluidRegistry.isFluidRegistered(TFCFluids.SAKE))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketSake != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketSake);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.SAKE, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Salt water
		if (FluidRegistry.isFluidRegistered(TFCFluids.SALTWATER))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledSaltWater != null)
			{
				filledContainer = new ItemStack(ModItems.BottledSaltWater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.SALTWATER, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}

			if (ModItems.CeramicBucket != null && ModItems.CeramicBucketSaltWater != null)
			{
				filledContainer = new ItemStack(ModItems.CeramicBucketSaltWater);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.SALTWATER, CeramicBucketVolume), filledContainer, new ItemStack(ModItems.CeramicBucket));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Sulfuric acid
		if (ModFluids.SULFURICACID != null && FluidRegistry.isFluidRegistered(ModFluids.SULFURICACID))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledSulfuricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.BottledSulfuricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(SULFURICACID, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}

			if (TFCItems.blueSteelBucketEmpty != null && ModItems.BlueSteelBucketSulfuricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.BlueSteelBucketSulfuricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(SULFURICACID, BlueSteelBucketVolume), filledContainer, new ItemStack(TFCItems.blueSteelBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.redSteelBucketEmpty != null && ModItems.RedSteelBucketSulfuricAcid != null)
			{
				filledContainer = new ItemStack(ModItems.RedSteelBucketSulfuricAcid);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(SULFURICACID, RedSteelBucketVolume), filledContainer, new ItemStack(TFCItems.redSteelBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}

		// Tannin
		if (FluidRegistry.isFluidRegistered(TFCFluids.TANNIN))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledTannin != null)
			{
				filledContainer = new ItemStack(ModItems.BottledTannin);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.TANNIN, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
			
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketTannin != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketTannin);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.TANNIN, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Vinegar
		if (FluidRegistry.isFluidRegistered(TFCFluids.VINEGAR))
		{
			if (TFCItems.glassBottle != null && ModItems.BottledVinegar != null)
			{
				filledContainer = new ItemStack(ModItems.BottledVinegar);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.VINEGAR, GlassBottleVolume), filledContainer, new ItemStack(TFCItems.glassBottle));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Vodka
		if (FluidRegistry.isFluidRegistered(TFCFluids.VODKA))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketVodka != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketVodka);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.VODKA, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
		
		// Whiskey
		if (FluidRegistry.isFluidRegistered(TFCFluids.WHISKEY))
		{
			if (TFCItems.woodenBucketEmpty != null && ModItems.WoodenBucketWhiskey != null)
			{
				filledContainer = new ItemStack(ModItems.WoodenBucketWhiskey);
				FluidContainerRegistry.registerFluidContainer(new FluidStack(TFCFluids.WHISKEY, WoodenBucketVolume), filledContainer, new ItemStack(TFCItems.woodenBucketEmpty));
				if (ModOptions.showVerboseStartup && FluidContainerRegistry.isFilledContainer(filledContainer))
					System.out.println("[" + ModDetails.ModName + "] Registering Fluid Container: " + filledContainer.getDisplayName());
			}
		}
	}
	
	/**
	 * @param fs
	 * @param amount This should be the amount that is actually consumed aka (weight - decay)
	 * @return The exact amount that should enter the stomach
	 */
	public static float getEatAmount(FoodStatsTFC fs, float amount)
	{
		if (fs == null) 
			return 0.0F;
		
		float eatAmount = Math.min(amount, 5);
		float stomachDiff = fs.stomachLevel+eatAmount-fs.getMaxStomach(fs.player);
		if(stomachDiff > 0)
			eatAmount-=stomachDiff;
		return eatAmount;
	}
	
	public static float getEatAmount(FoodStatsTFC fs, ItemStack is)
	{
		if (fs == null || is == null) 
			return 0.0F;
		
		float weight = Food.getWeight(is);
		float decay = Math.max(Food.getDecay(is), 0);
		return getEatAmount(fs, weight-decay);
	}
}
