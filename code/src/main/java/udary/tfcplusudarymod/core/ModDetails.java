package udary.tfcplusudarymod.core;

public class ModDetails 
{
	public static final String ModID = "tfcplusudarymod";
	public static final String ModName = "TFCPlusUdaryMod";

	public static final int VersionMajor = 0;
	public static final int VersionMinor = 3;
	public static final int VersionRevision = 01;

	public static final String ModVersion = VersionMajor + "." + VersionMinor + "." + VersionRevision;
	public static final String ModDependencies = "after:terrafirmacraftplus";
	public static final String ModChannel = "TFCPlusUdaryMod";
	public static final String SERVER_PROXY_CLASS = "udary.tfcplusudarymod.core.ModCommonProxy";
	public static final String CLIENT_PROXY_CLASS = "udary.tfcplusudarymod.core.ModClientProxy";
	
	public static final String AssetPath = "/assets/" + ModID + "/";
	public static final String AssetPathGui = "textures/gui/";
	
	public static final String ConfigFilePath = "/config/";
	public static final String ConfigFileName = "TFCPlusUdaryMod.cfg";
	
	public static final int GuiOffset = 10000;
	public static final int GuiTFCOffset = 20000;
	
	public static final String VersionCheckerUpdatePath = "https://raw.githubusercontent.com/Bletch1971/TFCPlusUdaryMod/tfc_{0}/update.json";
	
	public static final String MODID_NEI = "NotEnoughItems";
	public static final String MODID_TFC = "terrafirmacraftplus";
	public static final String MODID_WAILA = "Waila";

	public static final String MODNAME_NEI = "Not Enough Items";
	public static final String MODNAME_TFC = "TerraFirmaCraft+";
	public static final String MODNAME_WAILA = "Waila";
}
