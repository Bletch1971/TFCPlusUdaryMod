package udary.tfcplusudarymod.items.fluids;

import udary.tfcplusudarymod.core.ModFluids;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.api.TFCFluids;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemGlassBottleWater extends ItemGlassBottleFluid
{
	public ItemGlassBottleWater()
	{
		super(TFCFluids.FRESHWATER);
		
		this.stackable = true;
		this.action = EnumAction.drink;
	}
	
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(player);
		
		if (fs.needDrink())
			player.setItemInUse(is, this.getMaxItemUseDuration(is));
		return is;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 32;
	}

	@Override
	protected void updatePlayerFoodStats(ItemStack is, World world, EntityPlayer player)
	{
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(player);
		
		int restoreAmount = (int)(ModFluids.WaterDrinkMultiplier * this.getCapacity());
		fs.restoreWater(player, restoreAmount);
		
		TFC_Core.setPlayerFoodStats(player, fs);
	}
}
