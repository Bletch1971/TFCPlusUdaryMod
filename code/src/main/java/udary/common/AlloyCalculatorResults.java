package udary.common;

import java.util.ArrayList;

import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.api.Metal;

public class AlloyCalculatorResults 
{
	public class AlloyMetalDetails
	{
		public Metal metal;
		public int metalAmount;
		public float metalPercent;		
		
		public AlloyMetalDetails(Metal metal, int metalAmount)
		{
			this.metal = metal;
			this.metalAmount = metalAmount;
			this.metalPercent = 0;
		}
	}
	
	protected ArrayList<AlloyMetalDetails> detailsList;
	protected ArrayList<AlloyMetal> ingredientList;
	protected boolean useMetalAmounts;
	
	public AlloyCalculatorResults(boolean useMetalAmounts)
	{
		reset(useMetalAmounts);
	}
	
	public void addMetal(Metal metal, int metalAmount)
	{
		if (metal == null) return;
		
		// check if we can merge this metal with an existing array index
		boolean metalsMerged = false;
		for (int index = 0; index < detailsList.size(); index++)
		{
			if (detailsList.get(index) == null) continue;
			
			// get the merged metal amount (will return the old metal amount if cannot merge)
			int mergedMetalAmount = mergeMetals(detailsList.get(index).metal, metal, detailsList.get(index).metalAmount, metalAmount);
			if (mergedMetalAmount != detailsList.get(index).metalAmount)
			{
				// merge metal amount is different, metals can be merged
				detailsList.get(index).metalAmount += metalAmount;
				metalsMerged = true;
				break;
			}
		}
		
		// metal not merged, add to list
		if (!metalsMerged)
		{
			detailsList.add(new AlloyMetalDetails(metal, metalAmount));
		}		
		
		updateMetalPercents();
	}
		
	public ArrayList<AlloyMetalDetails> getDetailsList()
	{
		return detailsList;
	}

	public ArrayList<AlloyMetal> getIngredientList()
	{
		populateIngredientList();
		return ingredientList;
	}
	
	public int getMetalAmountTotal()
	{
		int metalTotal = 0;
		
		for (int index = 0; index < detailsList.size(); index++)
		{
			if (detailsList.get(index) == null) continue;
			
			metalTotal += detailsList.get(index).metalAmount;
		}
		
		return metalTotal;
	}
	
	protected static int mergeMetals(Metal metal1, Metal metal2, int metalAmount1, int metalAmount2)
	{
		if (metal1 != null && metal2 != null && metalAmount1 > 0)
		{
			if (metal1.name.equals(metal2.name))
				return metalAmount1 + metalAmount2;
		}
		return metalAmount1;
	}
	
	public void populateIngredientList()
	{
		if (ingredientList != null)
		{
			ingredientList.clear();
			ingredientList = null;
		}
		
		// create a list of the alloy metals
		ingredientList = new ArrayList<AlloyMetal>();
		
		for (int index = 0; index < detailsList.size(); index++)
		{
			if (detailsList.get(index) == null) continue;
			
			ingredientList.add(new AlloyMetal(detailsList.get(index).metal, useMetalAmounts ? detailsList.get(index).metalPercent : -1));
		}	
	}
	
	public void reset(Boolean useMetalAmounts)
	{
		if (this.detailsList != null)
		{
			this.detailsList.clear();
			this.detailsList = null;
		}
		
		if (this.ingredientList != null)
		{
			this.ingredientList.clear();
			this.ingredientList = null;
		}
		
		this.detailsList = new ArrayList<AlloyMetalDetails>();
		this.useMetalAmounts = useMetalAmounts;
	}
	
	public void updateMetalPercents()
	{
		// get the total amount of metal
		int metalAmountTotal = getMetalAmountTotal();			
		
		// calculate the metal percentages
		if (metalAmountTotal == 0)
		{
			for (int index = 0; index < detailsList.size(); index++)
			{
				if (detailsList.get(index) == null) continue;
				
				detailsList.get(index).metalPercent = 0;
			}
		}
		else
		{
			for (int index = 0; index < detailsList.size(); index++)
			{
				if (detailsList.get(index) == null) continue;
				
				detailsList.get(index).metalPercent = (detailsList.get(index).metalAmount / (float)metalAmountTotal) * 100f;
			}
		}
	}
}
