package udary.common.enums;

public enum EnumRecipeMatchType 
{
	/** Basic check - check if the objects are the same. */
	BASIC(0),
	/** Minimum check - check if the objects are the same and the minimum amount. */
	MINIMUM(1),
	/** Exact check - check if the objects are the same and the amounts are the same. */
	EXACT(2);
	
	protected final int matchType;
		
	private EnumRecipeMatchType(int matchType)
	{
		this.matchType = matchType;
	}
	
	public int getMatchType()
	{
		return matchType;
	}
}
