package udary.tfcplusudarymod.core.managers;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModOptions;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.core.recipes.TuckerBagEntity;
import udary.tfcplusudarymod.interfaces.ITuckerBag;

import com.dunk.tfc.Entities.Mobs.EntityBear;
import com.dunk.tfc.Entities.Mobs.EntityChickenTFC;
import com.dunk.tfc.Entities.Mobs.EntityCowTFC;
import com.dunk.tfc.Entities.Mobs.EntityDeer;
import com.dunk.tfc.Entities.Mobs.EntityHorseTFC;
import com.dunk.tfc.Entities.Mobs.EntityPheasantTFC;
import com.dunk.tfc.Entities.Mobs.EntityPigTFC;
import com.dunk.tfc.Entities.Mobs.EntitySheepTFC;
import com.dunk.tfc.Entities.Mobs.EntityWolfTFC;

public class TuckerBagManager 
{
	private static final TuckerBagManager instance = new TuckerBagManager();
	public static final TuckerBagManager getInstance()
	{
		return instance;
	}
	
	protected List<TuckerBagEntity> entities = new ArrayList<TuckerBagEntity>();
	
	public static void initialise()
	{
		if (!ModOptions.enableTuckerBagMod)
			return;
		
		System.out.println("[" + ModDetails.ModName + "] Registering Tucker Bag Entity Classes");
		
		registerEntities();
		
		System.out.println("[" + ModDetails.ModName + "] Total Entity Classes Registered: " + getInstance().entities.size());
		System.out.println("[" + ModDetails.ModName + "] Done Registering Tucker Bag Entity Classes");
	}
	
	protected static void registerEntities()
	{
		TuckerBagEntity entity;
		
		if (ModOptions.canCaptureTFCBear)
		{
			entity = new TuckerBagEntity(EntityBear.class, EnumTuckerBagVersion.VERSION_2, 4, EnumTuckerBagVersion.VERSION_2, 2);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCChicken)
		{
			entity = new TuckerBagEntity(EntityChickenTFC.class, EnumTuckerBagVersion.VERSION_1, 1, EnumTuckerBagVersion.VERSION_1, 0);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCCow)
		{
			entity = new TuckerBagEntity(EntityCowTFC.class, EnumTuckerBagVersion.VERSION_2, 2, EnumTuckerBagVersion.VERSION_1, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCDeer)
		{
			entity = new TuckerBagEntity(EntityDeer.class, EnumTuckerBagVersion.VERSION_1, 2, EnumTuckerBagVersion.VERSION_1, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCHorse)
		{
			entity = new TuckerBagEntity(EntityHorseTFC.class, EnumTuckerBagVersion.VERSION_2, 3, EnumTuckerBagVersion.VERSION_2, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCPheasant)
		{
			entity = new TuckerBagEntity(EntityPheasantTFC.class, EnumTuckerBagVersion.VERSION_1, 1, EnumTuckerBagVersion.VERSION_1, 0);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCPig)
		{
			entity = new TuckerBagEntity(EntityPigTFC.class, EnumTuckerBagVersion.VERSION_1, 2, EnumTuckerBagVersion.VERSION_1, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCSheep)
		{
			entity = new TuckerBagEntity(EntitySheepTFC.class, EnumTuckerBagVersion.VERSION_1, 2, EnumTuckerBagVersion.VERSION_1, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
		if (ModOptions.canCaptureTFCWolf)
		{
			entity = new TuckerBagEntity(EntityWolfTFC.class, EnumTuckerBagVersion.VERSION_2, 3, EnumTuckerBagVersion.VERSION_2, 1);
			if (!getInstance().addEntity(entity))
				System.out.println("[" + ModDetails.ModName + "] ** ERROR ** Entity not registered (" + entity.getLocalizedName() + ")");
			else if (ModOptions.showVerboseStartup)
				System.out.println("[" + ModDetails.ModName + "] Registered Entity Class: " + entity.getLocalizedName());
		}
	}
	
	public boolean addEntity(TuckerBagEntity entity)
	{
		// check for valid parameters
		if (entities == null || !entity.isValid())
			return false;
		
		// check if the entity has already been added.
		if (findMatchingEntity(entity.getEntityClass()) != null)
			return false;
		
		return entities.add(entity);
	}
	
	public void clearEntities()
	{
		entities.clear();
	}
		
	public TuckerBagEntity findMatchingEntity(Class<? extends Entity> entityClass)
	{
		// check for valid parameters
		if (entityClass == null)
			return null;
		
		String entityClassName = entityClass.getCanonicalName();
		
		return findMatchingEntity(entityClassName);
	}

	public TuckerBagEntity findMatchingEntity(String entityClassName)
	{
		// check for valid parameters
		if (entityClassName == null || entityClassName.trim() == "")
			return null;
		
		for (TuckerBagEntity entity : entities)
		{
			if (entity.matches(entityClassName))
				return entity;
		}
		
		return null;
	}

	public TuckerBagEntity findMatchingEntity(ItemStack is)
	{
		// check for valid parameters
		if (!hasEntity(is))
			return null;
		
		NBTTagCompound nbt = is.stackTagCompound.getCompoundTag(ModTags.TAG_TUCKER_BAG);
		if (nbt == null)
			return null;
		
		String entityClassName = nbt.getString(ModTags.TAG_ENTITY_CLASS);
		
		return findMatchingEntity(entityClassName);
	}
	
	public List<TuckerBagEntity> getEntities()
	{
		return entities;
	}
	
	public boolean removeEntity(TuckerBagEntity entity)
	{
		// check for valid parameters.
		if (entity == null || !entities.contains(entity))
			return false;

		return entities.remove(entity);
	}
	
	public boolean removeEntity(Class<? extends Entity> entity)
	{
		// check for valid parameters.
		if (entity == null)
			return false;
		
		// check if the entity has been added.
		TuckerBagEntity tbEntity = findMatchingEntity(entity);
		if (tbEntity == null)
			return false;

		return entities.remove(tbEntity);
	}
	
	
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public boolean canStoreEntity(ItemStack is, Entity entity, List returnMessages)
	{
		if (is == null || entity == null)
			return false;
		
		if (!(is.getItem() instanceof ITuckerBag && entity instanceof EntityAnimal))
			return false;
		
		EntityAnimal animal = (EntityAnimal)entity;
		ITuckerBag tuckerBag = (ITuckerBag)is.getItem();
		
		// check for non storeable attributes 
		if (animal.getLeashed())
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.entityleashed");
			return false;
		}
		
		if (entity.riddenByEntity != null)
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.entityridden");
			return false;
		}

		// try to find the entity class.
		TuckerBagEntity entityFound = findMatchingEntity(entity.getClass());
		
		// check if found.
		if (entityFound == null)
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.entitynotregistered");
			return false;
		}
		
		// check the animal can be captured.
		EnumTuckerBagVersion entityBagVersion = getEntityBagVersion(animal);
		int entityDamage = getEntityDamage(entity);
		if (!EnumTuckerBagVersion.isValid(entityBagVersion) || entityDamage < 0)
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.entitynotallowed");
			return false;
		}
		
		// check the bag version is high enough.
		EnumTuckerBagVersion bagVersion = tuckerBag.getBagVersion(is);
		if (bagVersion.isLower(entityBagVersion))
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.bagversiontolow");
			return false;
		}
		
		// check there is enough damage left.
		int bagDamageLeft = tuckerBag.getDamageLeft(is);		
		if (entityDamage > bagDamageLeft)
		{
			if (returnMessages != null)
				returnMessages.add("string.tuckerbag.bagtodamaged");
			return false;
		}
		
		return true;
	}

	public ItemStack clearEntity(ItemStack is)
	{
		// check for valid parameters.
		if (is == null || is.stackTagCompound == null)
			return is;
		
		if (!is.stackTagCompound.hasKey(ModTags.TAG_TUCKER_BAG))
			return is;		
		
		is.stackTagCompound.removeTag(ModTags.TAG_TUCKER_BAG);
		if (is.stackTagCompound.hasNoTags())
			is.stackTagCompound = null;
		
		return is;
	}
	
	@SuppressWarnings("rawtypes")
	public EntityAnimal getEntity(World world, NBTTagCompound nbt)
	{
		// check for valid parameters.
		if (world == null || nbt == null || nbt.hasNoTags() || !nbt.hasKey(ModTags.TAG_ENTITY_CLASS) || !nbt.hasKey(ModTags.TAG_ENTITY_TAG))
			return null;
		
		// get the entity class name from the nbt tag.
		String entityClassName = nbt.getString(ModTags.TAG_ENTITY_CLASS);
		
		// try to find the entity class.
		TuckerBagEntity entityFound = findMatchingEntity(entityClassName);
		if (entityFound == null)
			return null;
		
		// get the constructor for the entity class.
		Constructor constructor = entityFound.getEntityConstructor();
		if (constructor == null)
			return null;

		try 
		{
			// Instantiate the entity class.
			EntityAnimal entity = (EntityAnimal)constructor.newInstance(world);
			entity.readFromNBT(nbt.getCompoundTag(ModTags.TAG_ENTITY_TAG));
			
			// return the new entity.
			return entity;
		} 
		catch (Exception e) 
		{
			System.out.println("[" + ModDetails.ModName + "] Error Creating Tucker Bag Entity " + entityFound.getEntityClass().getSimpleName());
		} 
		
		// entity class was not found/created, return null.
		return null;
	}
	
	public EntityAnimal getEntity(World world, ItemStack is)
	{
		// check for valid parameters.
		if (world == null || is == null || is.stackTagCompound == null)
			return null;
		
		if (!is.stackTagCompound.hasKey(ModTags.TAG_TUCKER_BAG))
			return null;
		
		return getEntity(world, is.stackTagCompound.getCompoundTag(ModTags.TAG_TUCKER_BAG));
	}
	
	public EnumTuckerBagVersion getEntityBagVersion(Entity entity)
	{
		if (entity == null || !(entity instanceof EntityAnimal))
			return EnumTuckerBagVersion.NONE;
		
		EntityAnimal animal = (EntityAnimal)entity;	

		// try to find the entity class.
		TuckerBagEntity entityFound = findMatchingEntity(entity.getClass());		
		if (entityFound == null)
			return EnumTuckerBagVersion.NONE;
		
		if (animal.isChild())
			return entityFound.getBagVersionChild();
		else
			return entityFound.getBagVersionAdult();
	}
	
	public int getEntityDamage(Entity entity)
	{
		if (entity == null || !(entity instanceof EntityAnimal))
			return -1;
		
		EntityAnimal animal = (EntityAnimal)entity;	

		// try to find the entity class.
		TuckerBagEntity entityFound = findMatchingEntity(entity.getClass());		
		if (entityFound == null)
			return -1;
		
		if (animal.isChild())
			return entityFound.getDamageAmountChild();
		else
			return entityFound.getDamageAmountAdult();
	}
	
	public boolean hasEntity(ItemStack is)
	{
		// check for valid parameters.
		if (is == null || is.stackTagCompound == null)
			return false;	
		
		if (!is.stackTagCompound.hasKey(ModTags.TAG_TUCKER_BAG))
			return false;
		
		return true;
	}
	
	public NBTTagCompound setEntity(EntityAnimal entity)
	{
		// check for valid parameters.
		if (entity == null)
			return null;
		
		// check for non storeable attributes 
		if (entity.getLeashed() || entity.riddenByEntity != null)
			return null;
		
		// create the entity tag and populate
		NBTTagCompound nbtEntity = new NBTTagCompound();
		entity.writeToNBT(nbtEntity);

		// create the stored entity tag
		NBTTagCompound nbt = new NBTTagCompound();
		
		// populate the stored entity tag
		nbt.setString(ModTags.TAG_ENTITY_CLASS, entity.getClass().getCanonicalName());
		nbt.setString(ModTags.TAG_ENTITY_NAME, StatCollector.translateToLocal(entity.getCommandSenderName()));
		nbt.setTag(ModTags.TAG_ENTITY_TAG, nbtEntity);
		
		return nbt;
	}
	
	public ItemStack setEntity(EntityAnimal entity, ItemStack is)
	{
		// check for valid parameters.
		if (is == null || entity == null)
			return is;
		
		NBTTagCompound nbt = setEntity(entity);
		if (nbt == null)
			return is;
		
		if (is.stackTagCompound == null)
			is.stackTagCompound = new NBTTagCompound();
		
		is.stackTagCompound.setTag(ModTags.TAG_TUCKER_BAG, nbt);
		
		return is;
	}
}
