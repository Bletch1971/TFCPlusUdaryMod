package udary.tfcplusudarymod.interfaces;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public interface IBattery 
{
	public void decreaseCharge(ItemStack is, int chargeUnits);
	
	public String getBatteryType();

	public int getCurrentCharge(ItemStack is);
	
	public int getMaxCharge(ItemStack is);
		
	public boolean hasEnoughCharge(ItemStack is, int chargeUnits);
	
	public void increaseCharge(ItemStack is, int chargeUnits);
	
	public void setCurrentCharge(ItemStack is, int charge);
	
	public Item setMaxCharge(int maxCharge);
}
