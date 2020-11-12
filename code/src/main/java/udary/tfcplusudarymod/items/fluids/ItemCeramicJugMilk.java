package udary.tfcplusudarymod.items.fluids;

import java.util.List;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Player.FoodStatsTFC;
import com.dunk.tfc.api.TFCFluids;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumFoodGroup;
import com.dunk.tfc.api.Interfaces.IFood;

public class ItemCeramicJugMilk  extends ItemCeramicJugFluid implements IFood
{
	protected float decayRate;
	protected EnumFoodGroup foodGroup;
	protected float foodMaxWeight;
	protected int tasteBitter;
	protected int tasteSalty;
	protected int tasteSavory;
	protected int tasteSour;
	protected int tasteSweet;
	protected boolean isEdible;
	protected boolean isUsable;
	
 	public ItemCeramicJugMilk()
	{
		super(TFCFluids.MILK);
		
		this.setMaxStackSize(1);
		
		this.stackable = false;
		this.action = EnumAction.drink;
	
		this.decayRate = ModFluids.MilkDecayRate;
		this.foodGroup = EnumFoodGroup.Dairy;
		this.foodMaxWeight = ModFluids.CeramicJugMaxFoodWeight;
		this.tasteBitter = 0;
		this.tasteSalty = 0;
		this.tasteSavory = 10;
		this.tasteSour = 0;
		this.tasteSweet = 0;
		this.isEdible = true;
		this.isUsable = false;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	protected void addFoodInformation(ItemStack is, EntityPlayer player, List arraylist, NBTTagCompound nbt)
	{
		List<String> foodStrings = WailaUtils.getMilkInformation(is);
		if (foodStrings != null)
			arraylist.addAll(foodStrings);
	}
	
	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 32;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void getSubItems(Item item, CreativeTabs tabs, List list)
	{
		list.add(ModTags.applyFoodTag(new ItemStack(this), ModFluids.CeramicJugMaxFoodWeight));
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
	public void onUpdate(ItemStack is, World world, Entity entity, int i, boolean isSelected)
	{
		super.onUpdate(is, world, entity, i, isSelected);
		
		if (!is.hasTagCompound())
		{
			ModTags.applyFoodTag(is, ModFluids.CeramicJugMaxFoodWeight);
		}
	}

	@Override
	protected void updatePlayerFoodStats(ItemStack is, World world, EntityPlayer player)
	{
		FoodStatsTFC fs = TFC_Core.getPlayerFoodStats(player);
		
		if (fs.needFood())
		{
			world.playSoundAtEntity(player, "random.burp", 0.5F, world.rand.nextFloat() * 0.1F + 0.9F);
			
			if (is.hasTagCompound())
			{
				IFood food = (IFood)is.getItem();
				if (food != null)
				{
					float eatAmount = ModFluids.getEatAmount(fs, is);
					float tasteFactor = fs.getTasteFactor(is);
					
					fs.addNutrition(food.getFoodGroup(), eatAmount*tasteFactor);
				}
			}
		}
		
		int restoreAmount = (int)(ModFluids.MilkDrinkMultiplier * this.getCapacity());
		fs.restoreWater(player, restoreAmount);
		
		TFC_Core.setPlayerFoodStats(player, fs);
	}

	@Override
	public float getDecayRate(ItemStack is) 
	{
		return this.decayRate;
	}

	@Override
	public EnumFoodGroup getFoodGroup() 
	{
		return this.foodGroup;
	}

	@Override
	public int getFoodID() 
	{
		return -1;
	}

	@Override
	public float getFoodMaxWeight(ItemStack is) 
	{
		return this.foodMaxWeight;
	}

	@Override
	public int getTasteBitter(ItemStack is) 
	{
		return this.tasteBitter;
	}

	@Override
	public int getTasteSalty(ItemStack is) 
	{
		return this.tasteSalty;
	}

	@Override
	public int getTasteSavory(ItemStack is) 
	{
		return this.tasteSavory;
	}

	@Override
	public int getTasteSour(ItemStack is) 
	{
		return this.tasteSour;
	}

	@Override
	public int getTasteSweet(ItemStack is) 
	{
		return this.tasteSweet;
	}

	@Override
	public boolean isEdible(ItemStack is) 
	{
		return this.isEdible;
	}

	@Override
	public boolean isUsable(ItemStack is) 
	{
		return this.isUsable;
	}

	@Override
	public ItemStack onDecayed(ItemStack is, World world, int i, int j, int k) 
	{
		ItemStack outputIS = new ItemStack(TFCItems.potteryJug, 1, 1);
		outputIS.stackTagCompound = null;
		
		return outputIS;
	}

	@Override
	public boolean renderDecay() 
	{
		return true;
	}

	@Override
	public boolean renderWeight() 
	{
		return false;
	}
}
