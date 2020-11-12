package udary.tfcplusudarymod.core.recipes;

import java.lang.reflect.Constructor;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;

public class TuckerBagEntity 
{
	protected EnumTuckerBagVersion bagVersionAdult;
	protected EnumTuckerBagVersion bagVersionChild;
	protected int damageAmountAdult;
	protected int damageAmountChild;
	protected Class<EntityAnimal> entityClass;
	protected String entityName;
	
	@SuppressWarnings("unchecked")
	public TuckerBagEntity(Class<? extends EntityAnimal> entityClass, EnumTuckerBagVersion bagVersionAdult, int damageAmountAdult, EnumTuckerBagVersion bagVersionChild, int damageAmountChild)
	{
		this.entityClass = (Class<EntityAnimal>)entityClass;
		this.entityName = "";
		this.bagVersionAdult = bagVersionAdult;
		this.bagVersionChild = bagVersionChild;
		this.damageAmountAdult = damageAmountAdult;
		this.damageAmountChild = damageAmountChild;
		
		if (EntityList.classToStringMapping.containsKey(entityClass))
			this.entityName = EntityList.classToStringMapping.get(entityClass).toString();
	}
	
	public EnumTuckerBagVersion getBagVersionAdult()
	{
		if (ModOptions.enableReinforcedBag)
			return bagVersionAdult;
		else
		{
			if (bagVersionAdult == EnumTuckerBagVersion.VERSION_2)
				return EnumTuckerBagVersion.NONE;
			return bagVersionAdult;
		}
	}
	
	public EnumTuckerBagVersion getBagVersionChild()
	{
		if (ModOptions.enableReinforcedBag)
			return bagVersionChild;
		else
		{
			if (bagVersionChild == EnumTuckerBagVersion.VERSION_2)
				return EnumTuckerBagVersion.NONE;
			return bagVersionChild;
		}
	}
	
	public int getDamageAmountAdult()
	{
		if (ModOptions.enableReinforcedBag)
			return damageAmountAdult;
		else
		{
			if (bagVersionAdult == EnumTuckerBagVersion.VERSION_2)
				return 0;
			return damageAmountAdult;
		}
	}	
	
	public int getDamageAmountChild()
	{
		if (ModOptions.enableReinforcedBag)
			return damageAmountChild;
		else
		{
			if (bagVersionChild == EnumTuckerBagVersion.VERSION_2)
				return 0;
			return damageAmountChild;
		}
	}
	
	public Class<EntityAnimal> getEntityClass()
	{
		return entityClass;
	}

	@SuppressWarnings({ "rawtypes" })
	public Constructor getEntityConstructor()
	{
		if (entityClass == null)
			return null;

		try 
		{
			// check if the class has a specific constructor (World).
			Class[] parameterTypes = new Class[] { World.class };
			return entityClass.getConstructor(parameterTypes);
		} 
		catch (Exception e) 
		{ 
			// do nothing.
		} 

		return null;
	}
	
	public String getEntityName()
	{
		return entityName;
	}
	
	public String getLocalizedName()
	{
		return StatCollector.translateToLocal(getUnlocalizedName());	
	}
	
	public String getUnlocalizedName()
	{
		return "entity." + entityName + ".name";
	}
	
	public boolean matches(TuckerBagEntity item)
	{   
		return (item.entityClass == this.entityClass);
	}

	public boolean matches(Class<? extends Entity> entityClass)
	{   
		return (entityClass == this.entityClass);
	}

	public boolean matches(String entityClassName)
	{   
		if (entityClassName == null || entityClassName.trim() == "")
			return false;
		
		String className = this.entityClass.getCanonicalName();
		return className.equalsIgnoreCase(entityClassName);
	}
	
	public Boolean isValid()
	{
		if (entityClass == null || entityName == null || entityName.trim() == "")
			return false;
		
		return getEntityConstructor() != null;
	}
}
