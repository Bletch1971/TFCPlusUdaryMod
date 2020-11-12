package udary.tfcplusudarymod.core.recipes;

import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import com.dunk.tfc.api.Crafting.BarrelMultiItemRecipe;
import com.dunk.tfc.api.Crafting.BarrelRecipe;

public class BarrelMultiItemRecipeExt extends BarrelMultiItemRecipe 
{
	public BarrelMultiItemRecipeExt(ItemStack inputIS, FluidStack inputFS, ItemStack outIS, FluidStack outputFS, int sealTime) 
	{
		super(inputIS, inputFS, outIS, outputFS);
		
		this.sealTime = sealTime;
	}
	
	public BarrelRecipe setSealTime(int sealTime)
	{
		this.sealTime = sealTime;
		return this;
	}
}
