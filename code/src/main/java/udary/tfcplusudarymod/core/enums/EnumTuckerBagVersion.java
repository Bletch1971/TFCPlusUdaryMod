package udary.tfcplusudarymod.core.enums;

import net.minecraft.util.StatCollector;

public enum EnumTuckerBagVersion 
{
	NONE(StatCollector.translateToLocal("string.tuckerbag.version.none"), 0),
	VERSION_1(StatCollector.translateToLocal("string.tuckerbag.version.1"), 1),
	VERSION_2(StatCollector.translateToLocal("string.tuckerbag.version.2"), 2);
	
	protected final String name;
	protected final int versionNumber;

	private EnumTuckerBagVersion(String name, int versionNumber)
	{
		this.name = name;
		this.versionNumber = versionNumber;
	}
	
	public String getName()
	{
		return name;
	}
	
	public int getVersionNumber()
	{
		return versionNumber;
	}
	
	public Boolean isEqual(EnumTuckerBagVersion e)
	{
		return (versionNumber == e.versionNumber);
	}
	
	public Boolean isEqualOrHigher(EnumTuckerBagVersion e)
	{
		return (versionNumber >= e.versionNumber);
	}
	
	public Boolean isEqualOrLower(EnumTuckerBagVersion e)
	{
		return (versionNumber <= e.versionNumber);
	}
	
	public Boolean isHigher(EnumTuckerBagVersion e)
	{
		return (versionNumber > e.versionNumber);
	}
	
	public Boolean isLower(EnumTuckerBagVersion e)
	{
		return (versionNumber < e.versionNumber);
	}
	
	public static Boolean isValid(EnumTuckerBagVersion e)
	{
		return e != EnumTuckerBagVersion.NONE;
	}
}
