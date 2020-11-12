package udary.tfcplusudarymod.items.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import udary.common.helpers.RayTraceHelper;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.core.managers.TuckerBagManager;
import udary.tfcplusudarymod.core.recipes.TuckerBagEntity;
import udary.tfcplusudarymod.interfaces.ITuckerBag;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IEquipable;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemTuckerBag extends ItemTerra implements ITuckerBag, IEquipable
{
	protected static TuckerBagManager tuckerBagManager = TuckerBagManager.getInstance();
	
	protected long lastAttemptAnimalId = 0;
	protected long lastAttemptTime = TFC_Time.getTotalTicks();
	
    @SideOnly(Side.CLIENT)
    protected IIcon itemFullIcon;
    protected Random rand;
    
    protected EnumTuckerBagVersion bagVersion;
    protected EnumItemReach reach;
    protected EquipType equipType;
    
	public ItemTuckerBag(int maxDamage, EnumTuckerBagVersion bagVersion)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryTools);
		this.setFolder("tools/");
		
		this.setMaxDamage(maxDamage);
		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.SMALL);
		this.setWeight(EnumWeight.LIGHT);
		
		this.stackable = false;
		this.rand = new Random();
		this.bagVersion = bagVersion;
		this.reach = EnumItemReach.SHORT;
		this.equipType = EquipType.BACK;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		super.addInformation(is, player, arraylist, flag);
		
		String damageString = WailaUtils.getDamageInformation(is, false);
		if (damageString != "")
			arraylist.add(damageString);
		
		String entityString = WailaUtils.getEntityInformation(is);
		if (entityString != "")
			arraylist.add(entityString);
	}
	
	public EnumTuckerBagVersion getBagVersion()
	{
		return this.bagVersion;
	}
	
	@Override
	public IIcon getIconFromDamage(int damage)
	{
		return this.itemIcon;
	}
	
    @SideOnly(Side.CLIENT)
    public IIcon getIconIndex(ItemStack is)
    {
        return hasEntity(is) ? this.itemFullIcon : this.itemIcon;
    }
	
	@Override
    public int getItemStackLimit(ItemStack is)
    {
		if (hasEntity(is))
			return 1;
		
        return maxStackSize;
    }
    
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		if (hasEntity(is))
			return EnumSize.HUGE;
		
		return size;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		if (hasEntity(is))
			return EnumWeight.HEAVY;
		
		return weight;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack is)
	{
		return super.getUnlocalizedName() + (hasEntity(is) ? " Full" : "");
	}

	@Override
    public boolean itemInteractionForEntity(ItemStack is, EntityPlayer player, EntityLivingBase entity)
    {
//		if (is.getItem() instanceof ItemTuckerBag)
//		{
//			return tuckerBagManager.canStoreEntity(entity);
//		}
		
		return false;
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registerer)
	{
		this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", ""));
		this.itemFullIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", "") + " Full");
	}
	
	@Override
	public ItemStack clearEntity(ItemStack is)
	{
		return tuckerBagManager.clearEntity(is);
	}
	
	@Override
	public EnumTuckerBagVersion getBagVersion(ItemStack is)
	{
		if (is.getItem() instanceof ItemTuckerBag)
		{
			return ((ItemTuckerBag)is.getItem()).getBagVersion();
		}
		
		return EnumTuckerBagVersion.NONE;
	}
	
	@Override
	public int getDamageLeft(ItemStack is)
	{
		int remaining = 0;
		
		int maxDamage = is.getMaxDamage()+1;
		if (maxDamage > 0)
		{
			int currentDamage = is.getItemDamage();
			remaining = maxDamage-currentDamage;
		}
		
		return remaining;
	}
	
	@Override
	public EntityAnimal getEntity(World world, ItemStack is)
	{
		return tuckerBagManager.getEntity(world, is);
	}
	
	@Override
	public Boolean hasEntity(ItemStack is)
	{
		return tuckerBagManager.hasEntity(is);
	}
	
	@Override
	public ItemStack setEntity(EntityAnimal entity, ItemStack is)
	{
		return tuckerBagManager.setEntity(entity, is);
	}
	
	@Override
	public EquipType getEquipType(ItemStack is) 
	{
		return this.equipType;
	}

	@Override
	public boolean getTooHeavyToCarry(ItemStack is) 
	{
		if (hasEntity(is))
			return true;
		
		return false;
	}

	@Override
	public void onEquippedRender()
	{
	}
	
    public boolean onDroppedByPlayer(ItemStack is, EntityPlayer player)
    {
    	if (is == null || player == null)
    		return true;
    	
    	releaseEntity(is, player.worldObj, player, true);
    	
    	return true;
    }
    
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		if (world.isRemote) 
			return is;
		
		MovingObjectPosition mop = RayTraceHelper.getInstance().fire(world, player, false);
		
		if (mop == null)
		{
			releaseEntity(is, world, player, false);
		}		
//		else if (mop.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY && mop.entityHit != null)
//		{
//			Entity entity = world.getEntityByID(mop.entityHit.getEntityId());
//			
//			storeEntity(is, player, entity);
//		}
		
		return is;
	}
	
	@Override
    public boolean onLeftClickEntity(ItemStack is, EntityPlayer player, Entity entity)
    {
		if (player.worldObj.isRemote)
			return false;

		// check if we need to display tucker bag message (no within 5 seconds)
		boolean displayMessages = lastAttemptAnimalId != entity.getEntityId() || lastAttemptTime + 100 < TFC_Time.getTotalTicks();
		boolean entityStored = storeEntity(is, player, entity, displayMessages);
		
		if (!entityStored && displayMessages)
		{
			lastAttemptAnimalId = entity.getEntityId();
			lastAttemptTime = TFC_Time.getTotalTicks();
		}
		
		return true;
    }
	
	protected Vec3 scaleVector(Vec3 vector, float scale)
	{
		if (vector == null)
			return null;
		
		return Vec3.createVectorHelper(vector.xCoord * scale, vector.yCoord * scale, vector.zCoord * scale);
	}
	
	protected Boolean releaseEntity(ItemStack is, World world, EntityPlayer player, boolean bagDroppedByPlayer)
	{
		if (is == null || world == null || player == null)
			return false;
		
		if (hasEntity(is))
		{
			EntityAnimal entity = getEntity(world, is);
			if (entity != null)
			{
				Vec3 vector = scaleVector(player.getLookVec(), 3);
				
				// set new position of the entity.
				entity.setLocationAndAngles(player.posX+vector.xCoord, player.posY+1, player.posZ+vector.zCoord, 0.0F, 0.0F);
				entity.rotationYawHead = entity.rotationYaw;
				entity.renderYawOffset = entity.rotationYaw;

				EnumTuckerBagVersion entityBagVersion = tuckerBagManager.getEntityBagVersion(entity);
				EnumTuckerBagVersion bagVersion = getBagVersion(is);
				
				// check if the bag was dropped and whether the bag is strong enough to hold the animal.
				if (!bagDroppedByPlayer || !bagVersion.isHigher(entityBagVersion))
				{
					// spawn entity into the world.
					if (world.spawnEntityInWorld(entity))
					{
						// clear the stored entity.
						clearEntity(is);
						
						if (!player.capabilities.isCreativeMode)
						{
							int damageAmount = 1;
							
							TuckerBagEntity entityFound = tuckerBagManager.findMatchingEntity(entity.getClass());
							if (entityFound != null)
							{
								damageAmount = Math.max(tuckerBagManager.getEntityDamage(entity), 0);
							}
	
							// apply damage.
							is.attemptDamageItem(damageAmount, rand);
						}
						
						return true;
					}
				}
			}
		}			
	
		return false;
	}

	@SuppressWarnings({ "rawtypes" })
	protected Boolean storeEntity(ItemStack is, EntityPlayer player, Entity entity, boolean displayMessages)
	{
		if (is == null || entity == null || player == null)
			return false;
		
		if (!hasEntity(is))
		{
			// only initialise the list if we are to display the messages.
			List returnMessages = displayMessages ? new ArrayList<String>() : null;
			
			if (tuckerBagManager.canStoreEntity(is, entity, returnMessages))
			{
				// store the entity data.
				ItemStack resultIS = setEntity((EntityAnimal)entity, is);
				
				if (hasEntity(resultIS))
				{
					// remove entity from the world
					player.worldObj.removeEntity(entity);
					
					return true;
				}
			}
			else
			{
				if (returnMessages != null)
				{
					for (Object message : returnMessages)
					{
						player.addChatMessage(new ChatComponentText(StatCollector.translateToLocal(message.toString())));
					}
				}
			}
		}
		
		return false;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }

	@Override
	public ResourceLocation getClothingTexture(Entity arg0, ItemStack arg1, int arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClothingType getClothingType() {
		// TODO Auto-generated method stub
		return null;
	}
}
