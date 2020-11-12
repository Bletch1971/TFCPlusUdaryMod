package udary.tfcplusudarymod.core.managers;

import java.util.HashMap;

import net.minecraft.item.Item;
import net.minecraft.util.StatCollector;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModItems;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.interfaces.IRender;
import udary.tfcplusudarymod.render.item.RenderTuckerBag;

public class RenderManager 
{
	private static final RenderManager instance = new RenderManager();
	public static final RenderManager getInstance()
	{
		return instance;
	}
	
	protected HashMap<Item, IRender> renderers = new HashMap<Item, IRender>(); 

	public static void initialise()
	{
		System.out.println("[" + ModDetails.ModName + "] Registering Render Classes");
		
		registerRenderClasses();
		
		System.out.println("[" + ModDetails.ModName + "] Total Renderers Registered: " + getInstance().renderers.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Render Classes");
	}
	
	protected static void registerRenderClasses()
	{
		String name;
		
		if (ModItems.TuckerBagv1 != null)
		{
			name = StatCollector.translateToLocal(ModItems.TuckerBagv1.getUnlocalizedName());
			if (!getInstance().addRender(ModItems.TuckerBagv1, new RenderTuckerBag()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Render Class not registered (" + name + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Render Class: " + name);
		}
		
		if (ModItems.TuckerBagv2 != null)
		{
			name = StatCollector.translateToLocal(ModItems.TuckerBagv2.getUnlocalizedName());
			if (!getInstance().addRender(ModItems.TuckerBagv2, new RenderTuckerBag()))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Render Class not registered (" + name + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Render Class: " + name);
		}
	}
	
	public Boolean addRender(Item item, IRender render)
	{
		// check for valid parameters.
		if (item == null || render == null || renderers.containsKey(item))
			return false;
				
		renderers.put(item, render);
		return true;
	}
	
	public void clearRenders()
	{
		renderers.clear();
	}
	
	public IRender getRender(Item item)
	{
		// check for valid parameters.
		if (item == null || !renderers.containsKey(item))
			return null;
		
		return renderers.get(item);
	}
	
	public Boolean removeRender(Item item)
	{
		// check for valid parameters.
		if (item == null || !renderers.containsKey(item))
			return false;
		
		renderers.remove(item);
		return true;
	}
}
