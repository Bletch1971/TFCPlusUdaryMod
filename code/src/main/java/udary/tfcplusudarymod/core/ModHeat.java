package udary.tfcplusudarymod.core;

import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import com.dunk.tfc.api.HeatIndex;
import com.dunk.tfc.api.HeatRaw;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;

public class ModHeat 
{
	private static HeatRegistry heatRegistry = HeatRegistry.getInstance();
	
	public static final int WILDCARD_VALUE = OreDictionary.WILDCARD_VALUE;
	
	public static final int MAXIMUM_TEMPERATURE = 2500;
	
	public static HeatRaw CopperRaw = new HeatRaw(0.35, 1080);
	public static HeatRaw IronRaw = new HeatRaw(0.35, 1535);
	public static HeatRaw NickelRaw = new HeatRaw(0.48, 1453);
	public static HeatRaw SilverRaw = new HeatRaw(0.48, 961);
	public static HeatRaw WroughtIronRaw = new HeatRaw(0.35, 1535);
	
	public static void initialise()
	{		
		System.out.println("[" + ModDetails.ModName + "] Registering Heat");
		
		registerBlockHeat();
		registerItemHeat();
		
		System.out.println("[" + ModDetails.ModName + "] Done Registering Heat");
	}
	
	private static void registerBlockHeat()
	{
	}

	private static void registerItemHeat()
	{
		HeatIndex heatIndex;
		
		if (ModItems.CopperRod != null)
		{
			heatIndex = new HeatIndex(new ItemStack(ModItems.CopperRod, 1, WILDCARD_VALUE), CopperRaw, new ItemStack(TFCItems.copperUnshaped,1));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
		}
		
		if (ModItems.CopperWire != null)
		{
			heatIndex = new HeatIndex(new ItemStack(ModItems.CopperWire, 1, WILDCARD_VALUE), CopperRaw, new ItemStack(TFCItems.copperUnshaped,1));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
		}
		
		if (ModItems.NickelFlake != null)
		{
			heatIndex = new HeatIndex(new ItemStack(ModItems.NickelFlake, 1, TFCOptions.smallOreUnits), NickelRaw, new ItemStack(TFCItems.nickelUnshaped, TFCOptions.smallOreUnits));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
			
			heatIndex = new HeatIndex(new ItemStack(ModItems.NickelFlake, 1, ModGlobal.tinyOreUnits), NickelRaw, new ItemStack(TFCItems.nickelUnshaped, ModGlobal.tinyOreUnits));
			heatRegistry.addIndex(heatIndex);			
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
		}

		if (ModItems.SilverFlake != null)
		{
			heatIndex = new HeatIndex(new ItemStack(ModItems.SilverFlake, 1, TFCOptions.smallOreUnits), SilverRaw, new ItemStack(TFCItems.silverUnshaped, TFCOptions.smallOreUnits));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
			
			heatIndex = new HeatIndex(new ItemStack(ModItems.SilverFlake, 1, ModGlobal.tinyOreUnits), SilverRaw, new ItemStack(TFCItems.silverUnshaped, ModGlobal.tinyOreUnits));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
		}
		
		if (ModItems.WroughtIronBuckle != null)
		{
			heatIndex = new HeatIndex(new ItemStack(ModItems.WroughtIronBuckle, 1, WILDCARD_VALUE), WroughtIronRaw, new ItemStack(TFCItems.wroughtIronUnshaped,1));
			heatRegistry.addIndex(heatIndex);
			if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registering Heat: " + heatIndex.input.getDisplayName());
		}
	}
}
