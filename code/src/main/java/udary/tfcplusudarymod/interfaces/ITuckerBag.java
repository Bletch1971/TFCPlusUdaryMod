package udary.tfcplusudarymod.interfaces;

import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface ITuckerBag 
{
	public ItemStack clearEntity(ItemStack is);
	
	public EnumTuckerBagVersion getBagVersion(ItemStack is);
	
	public int getDamageLeft(ItemStack is);
	
	public EntityAnimal getEntity(World world, ItemStack is);
	
	public Boolean hasEntity(ItemStack is);
	
	public ItemStack setEntity(EntityAnimal entity, ItemStack is);
}
