package udary.tfcplusudarymod.core.recipes;

import java.util.Arrays;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fluids.FluidStack;
import udary.common.Recipe;
import udary.common.RecipeResult;
import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.interfaces.IAnode;
import udary.waila.WailaUtils;

import com.dunk.tfc.api.Metal;

public class AnodisingVesselAnodiseRecipe extends Recipe 
{
	protected int additionalResultPercentage;
	protected int anodeDamageAmount;
	protected float baseResultAmount;
	protected int batteryChargeAmount;
	protected int cathodeDamageAmount;
	protected Metal anodisedMetal;
	
	public AnodisingVesselAnodiseRecipe(FluidStack inputFS, ItemStack inputIS, ItemStack outputIS, int duration, int batteryChargeAmount, int cathodeDamageAmount, int anodeDamageAmount, float baseResultAmount)
	{
		super();
		
		if (inputFS != null)
			addInputFluidStack(inputFS);
		if (inputIS != null)
			addInputItemStack(inputIS);
		if (outputIS != null && 
			outputIS.getItem() instanceof IAnode && ((IAnode)outputIS.getItem()).isAnode(outputIS) && ((IAnode)outputIS.getItem()).getAnodisedMetal() != null)
			setOutputItemStack(outputIS);
		
		setRecipeDuration(duration);
		
		this.additionalResultPercentage = 0;
		this.anodeDamageAmount = anodeDamageAmount;
		this.baseResultAmount = baseResultAmount;
		this.batteryChargeAmount = batteryChargeAmount;
		this.cathodeDamageAmount = cathodeDamageAmount;
		
		if (outputIS != null && 
			outputIS.getItem() instanceof IAnode && ((IAnode)outputIS.getItem()).isAnode(outputIS) && ((IAnode)outputIS.getItem()).getAnodisedMetal() != null)
		{
			this.anodisedMetal = ((IAnode)outputIS.getItem()).getAnodisedMetal();
			
			String resultName = WailaUtils.getMetalNameUnlocalized(this.anodisedMetal, true);
			setUnlocalizedResultName(resultName);
		}
	}

	@Override
	public boolean isRecipeValid()
	{
		return 	inputFSList.size() == 1 && inputFSList.get(0).amount > 0 && 
				inputISList.size() == 1 && inputISList.get(0).stackSize > 0 &&
				outputIS != null && outputIS.stackSize > 0 && 
				anodisedMetal != null;
	}

	/**
	 * Returns the percentage value (0-100) used to add an additional result amount. The percent value is applied to a random the produce an extra result amount.
	 */
	public int getAdditionalResultPercentage()
	{
		return this.additionalResultPercentage;
	}
	
	/**
	 * Gets the anode damage applied when completed the recipe.
	 */
	public int getAnodeDamageAmount()
	{
		return this.anodeDamageAmount;
	}
	
	/**
	 * Gets the base result amount when completed the recipe.
	 */
	public float getBaseResultAmount()
	{
		return this.baseResultAmount;
	}
	
	/**
	 * Gets the battery charge required to complete the recipe.
	 */
	public int getBatteryChargeAmount()
	{
		return this.batteryChargeAmount;
	}
	
	/**
	 * Gets the cathode damage applied when completed the recipe.
	 */
	public int getCathodeDamageAmount()
	{
		return this.cathodeDamageAmount;
	}
	
	@Override
	public int getRecipeDuration()
	{
		return this.recipeDuration;
	}
		
	/**
	 * Gets the result fluid/item stacks of the recipe.
	 * @param inputFS The fluid stack used with the recipe.
	 * @param batteryIS The ItemStack object that contains the battery item for the recipe.
	 * @param cathodeIS The ItemStack object that contains the cathode item for the recipe.
	 * @param anodeIS The ItemStack object that contains the anode item for the recipe.
	 * @param soluteIS The ItemStack object that contains the solute item for the recipe.
	 * @param adjustInputs True to adjust the input component arguments.
	 */
	public RecipeResult getRecipeResult(FluidStack inputFS, ItemStack batteryIS, ItemStack cathodeIS, ItemStack anodeIS, ItemStack soluteIS, boolean adjustInputs)
	{
		// check the parameters are valid
		if (batteryIS == null || batteryIS.stackSize != 1 || 
			cathodeIS == null || cathodeIS.stackSize != 1 || 
			anodeIS == null || anodeIS.stackSize != 1)
				return null;
			
		// check the input components match the recipe.
		if (inputFS == null || !matches(inputFS, soluteIS, EnumRecipeMatchType.MINIMUM))
			return null;

		RecipeResult result = new RecipeResult();
		
		FluidStack recipeFS = this.inputFSList.get(0);
		ItemStack recipeSoluteIS = this.inputISList.get(0);
		ItemStack outputIS = null;

		// check if the anode items are the same.
		if (anodeIS.getItem() != this.outputIS.getItem())
		{
			// anode items are different, get metal from input anode item.
			Metal anodeMetal = anodeIS.getItem() instanceof IAnode ? ((IAnode)anodeIS.getItem()).getAnodisedMetal() : null;
			
			// check if the input anode item has a defined metal (or just a generic anode item).
			if (anodeMetal != null)
			{
				// input anode is not a generic anode item, get metal from output anode item.
				Metal outputMetal = this.outputIS.getItem() instanceof IAnode ? ((IAnode)this.outputIS.getItem()).getAnodisedMetal() : null;
				
				// check the metals match.
				if (anodeMetal != outputMetal)
					// metals do not match, exit.
					return null;
				
				// use the existing anode item, uses same metal.
				outputIS = anodeIS.copy();
			}
			else
			{
				// create the new anode item.
				outputIS = new ItemStack(this.outputIS.getItem(), anodeIS.stackSize, anodeIS.getItemDamage());
			}
		}
		else
			// anode items are the same, make a copy of the existing anode item.
			outputIS = anodeIS.copy();
		
		// check the output anode item is valid.
		if (outputIS != null)
		{			
			// anode items are the same, assign the to the result.
			result.addOutputItemStack(outputIS);

			// get the tag from the anode item.
			NBTTagCompound anodeTag = ModTags.applyEmptyTag(outputIS);
			
			// get the current result amount from the anode item.
			float anodisingUnits = anodeTag.getFloat(ModTags.TAG_ANODISING_UNITS);
			
			// calculate result amount = base + (base * random percentage).
			anodisingUnits += this.baseResultAmount + (this.baseResultAmount * ((float)getRandom().nextInt(this.additionalResultPercentage) / 100f));
			
			// apply the new result amount to the anode item.
			anodeTag.setFloat(ModTags.TAG_ANODISING_UNITS, anodisingUnits);

			// damage the anode item.
			outputIS.attemptDamageItem(this.anodeDamageAmount, this.random);
			
			// check damage is within valid range (0 to max damage).
			if (outputIS.getItemDamage() < 0)
				outputIS.setItemDamage(0);
			if (outputIS.getItemDamage() > outputIS.getMaxDamage())
				outputIS.setItemDamage(outputIS.getMaxDamage());
		}
		
		if (adjustInputs)
		{
			// 1. Reduce the fluid amount.
			inputFS.amount -= recipeFS.amount;
			
			// 2. Reduce the solute item.
			soluteIS.stackSize -= recipeSoluteIS.stackSize;
			
			// 3. Damage the battery item.
			batteryIS.attemptDamageItem(this.batteryChargeAmount, getRandom());
			
			if (batteryIS.getItemDamage() < 0)
				batteryIS.setItemDamage(0);
			if (batteryIS.getItemDamage() > batteryIS.getMaxDamage())
				batteryIS.setItemDamage(batteryIS.getMaxDamage());
			
			// 4. Damage the cathode item.
			cathodeIS.attemptDamageItem(this.cathodeDamageAmount, getRandom());
			
			// 5. Damage the anode item.
			anodeIS.setItemDamage(outputIS.getItemDamage());
			
			if (anodeIS.getItemDamage() < 0)
				anodeIS.setItemDamage(0);
			if (anodeIS.getItemDamage() > anodeIS.getMaxDamage())
				anodeIS.setItemDamage(anodeIS.getMaxDamage());
		}

		return result;
	}
	
	/**
	 * Gets the result fluid/item stacks of the recipe.
	 * @param inputFS The fluid stack used with the recipe.
	 * @param soluteIS The item Stack that contains the input item for the recipe.
	 */
	public boolean matches(FluidStack inputFS, ItemStack soluteIS, EnumRecipeMatchType matchType)
	{
		if (inputFS == null || soluteIS == null)
			return false;

		return super.matches(Arrays.asList(inputFS), Arrays.asList(soluteIS), matchType);
	}
	
	/**
	 * Sets the percentage value (0-100) used to add an additional result amount. The percent value is applied to a random the produce an extra result amount. 
	 * @param additionalResultPercentage The additional percentage amount for the result.
	 */
	public AnodisingVesselAnodiseRecipe setAdditionalResultPercentage(int additionalResultPercentage)
	{
		this.additionalResultPercentage = Math.min(Math.max(additionalResultPercentage, 0), 100);
		return this;
	}
}
