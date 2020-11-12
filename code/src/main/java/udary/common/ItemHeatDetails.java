package udary.common;

import net.minecraft.item.ItemStack;

import com.dunk.tfc.Items.ItemBloom;
import com.dunk.tfc.Items.ItemIngot;
import com.dunk.tfc.Items.ItemMetalSheet;
import com.dunk.tfc.Items.ItemUnfinishedArmor;
import com.dunk.tfc.api.HeatRegistry;
import com.dunk.tfc.api.TFC_ItemHeat;

public class ItemHeatDetails 
{
	public ItemHeatDetails(ItemStack is)
	{
		hasTemp = false;
		meltTemp = -1;
		temp = 0;
		
		color = 0x000000;
		range = 0;
		subRange = 0;
		isWorkable = false;
		isWeldable = false;
		isInDanger = false;
		isLiquid = false;
		
		initialise(is);
	}
	
	public boolean hasTemp;
	public float meltTemp;
	public float temp;
	
	public int color;
	public int range;
	public int subRange;
	public boolean isWorkable;
	public boolean isWeldable;
	public boolean isInDanger;
	public boolean isLiquid;
	
	public void initialise(ItemStack is)
	{
		if (is == null) return;

		hasTemp = TFC_ItemHeat.hasTemp(is);
		if (!hasTemp) return;
		
		meltTemp = TFC_ItemHeat.isCookable(is);
		temp = TFC_ItemHeat.getTemp(is);
		
		if (meltTemp <= 0 || temp <= 0) return;
		
		if (temp < meltTemp)
		{
			if (temp < 80)	// Warming
			{
				color = 0x400000;
				range = 1;
				if(temp>(80 * 0.2))
					subRange = 1;
				if(temp>(80 * 0.4))
					subRange = 2;
				if(temp>(80 * 0.6))
					subRange = 3;
				if(temp>(80 * 0.8))
					subRange = 4;
			}
			else if (temp < 210)	// Hot
			{
				color = 0x600000;
				range = 2;
				if(temp>80+((210-80) * 0.2))
					subRange = 1;
				if(temp>80+((210-80) * 0.4))
					subRange = 2;
				if(temp>80+((210-80) * 0.6))
					subRange = 3;
				if(temp>80+((210-80) * 0.8))
					subRange = 4;
			}
			else if (temp < 480)	// VeryHot
			{
				color = 0x800000;
				range = 3;						
				if(temp>210+((480-210) * 0.2))
					subRange = 1;
				if(temp>210+((480-210) * 0.4))
					subRange = 2;
				if(temp>210+((480-210) * 0.6))
					subRange = 3;
				if(temp>210+((480-210) * 0.8))
					subRange = 4;
			}
			else if (temp < 580)	// FaintRed
			{
				color = 0xa00000;
				range = 4;
				if(temp>480+((580-480) * 0.2))
					subRange = 1;
				if(temp>480+((580-480) * 0.4))
					subRange = 2;
				if(temp>480+((580-480) * 0.6))
					subRange = 3;
				if(temp>480+((580-480) * 0.8))
					subRange = 4;
			}
			else if (temp < 730)	// DarkRed
			{
				color = 0xc40000;
				range = 5;
				if(temp>580+((730-580) * 0.2))
					subRange = 1;
				if(temp>580+((730-580) * 0.4))
					subRange = 2;
				if(temp>580+((730-580) * 0.6))
					subRange = 3;
				if(temp>580+((730-580) * 0.8))
					subRange = 4;
			}
			else if (temp < 930)	// BrightRed
			{
				color = 0xff5555;
				range = 6;
				if(temp>730+((930-730) * 0.2))
					subRange = 1;
				if(temp>730+((930-730) * 0.4))
					subRange = 2;
				if(temp>730+((930-730) * 0.6))
					subRange = 3;
				if(temp>730+((930-730) * 0.8))
					subRange = 4;
			}
			else if (temp < 1100)	// Orange
			{
				color = 0xffaa00;
				range = 7;
				if(temp>930+((1100-930) * 0.2))
					subRange = 1;
				if(temp>930+((1100-930) * 0.4))
					subRange = 2;
				if(temp>930+((1100-930) * 0.6))
					subRange = 3;
				if(temp>930+((1100-930) * 0.8))
					subRange = 4;
			}
			else if (temp < 1300)	// Yellow
			{
				color = 0xffff00;
				range = 8;
				if(temp>1100+((1300-1100) * 0.2))
					subRange = 1;
				if(temp>1100+((1300-1100) * 0.4))
					subRange = 2;
				if(temp>1100+((1300-1100) * 0.6))
					subRange = 3;
				if(temp>1100+((1300-1100) * 0.8))
					subRange = 4;
			}
			else if (temp < 1400)	// YellowWhite
			{
				color = 0xffffc0;
				range = 9;
				if(temp>1300+((1400-1300) * 0.2))
					subRange = 1;
				if(temp>1300+((1400-1300) * 0.4))
					subRange = 2;
				if(temp>1300+((1400-1300) * 0.6))
					subRange = 3;
				if(temp>1300+((1400-1300) * 0.8))
					subRange = 4;
			}
			else if (temp < 1500)	// White
			{
				color = 0xe5e5e5;
				range = 10;
				if(temp>1400+((1500-1400) * 0.2))
					subRange = 1;
				if(temp>1400+((1500-1400) * 0.4))
					subRange = 2;
				if(temp>1400+((1500-1400) * 0.6))
					subRange = 3;
				if(temp>1400+((1500-1400) * 0.8))
					subRange = 4;
			}
			else // BrilliantWhite
			{
				color = 0xffffff;
				range = 11;
				subRange = 4;
			}
		}
		else if (temp >= meltTemp)
		{
			color = 0x0080ff;
			range = 12;
			subRange = 4;
			isLiquid = true;
		}

		if (range < 0) range = 0;

		if (is.getItem() instanceof ItemIngot || is.getItem() instanceof ItemMetalSheet ||
			is.getItem() instanceof ItemUnfinishedArmor || is.getItem() instanceof ItemBloom)
		{
			isWorkable = HeatRegistry.getInstance().isTemperatureWorkable(is);
			isWeldable = HeatRegistry.getInstance().isTemperatureWeldable(is);
			isInDanger = HeatRegistry.getInstance().isTemperatureDanger(is);
		}
	}
}
