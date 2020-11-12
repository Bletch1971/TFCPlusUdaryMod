package udary.common.helpers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.IShearable;

public class RayTraceHelper 
{
	private static final RayTraceHelper instance = new RayTraceHelper();
	public static final RayTraceHelper getInstance()
	{
		return instance;
	}
	
	protected MovingObjectPosition targetMOP = null;
	protected ItemStack targetItemStack = null;
	protected Entity targetEntity= null;
	protected World world = null;
	protected EntityPlayer player = null;
	
	public MovingObjectPosition fire(World world, EntityPlayer player, Boolean includeLiquids)
	{
		this.targetMOP = null;
		this.targetEntity = null;
		this.targetItemStack = null;
		this.world = world;
		this.player = player;
		
		// check if called from the server or client.
		if (FMLCommonHandler.instance().getSide() == Side.SERVER)
		{
			// server call.
	        float f = 1.0F;
	        float f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * f;
	        float f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * f;
	        
	        double d0 = player.prevPosX + (player.posX - player.prevPosX) * (double)f;
	        double d1 = player.prevPosY + (player.posY - player.prevPosY) * (double)f + (double)(world.isRemote ? player.getEyeHeight() - player.getDefaultEyeHeight() : player.getEyeHeight()); // isRemote check to revert changes to ray trace position due to adding the eye height clientside and player yOffset differences
	        double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * (double)f;
	        
	        Vec3 vec3 = Vec3.createVectorHelper(d0, d1, d2);
	        
	        float f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
	        float f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
	        float f5 = -MathHelper.cos(-f1 * 0.017453292F);
	        float f6 = MathHelper.sin(-f1 * 0.017453292F);
	        float f7 = f4 * f5;
	        float f8 = f3 * f5;
	        double d3 = 5.0D;
	        
	        if (player instanceof EntityPlayerMP)
	        {
	            d3 = ((EntityPlayerMP)player).theItemInWorldManager.getBlockReachDistance();
	        }
	        
	        Vec3 vec31 = vec3.addVector((double)f7 * d3, (double)f6 * d3, (double)f8 * d3);
	        targetMOP = world.func_147447_a(vec3, vec31, includeLiquids, !includeLiquids, false);			
		}
		else
		{
			// Client Call.
			Minecraft mc = Minecraft.getMinecraft();
			if (mc != null)
			{
				if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
				{
					targetMOP = mc.objectMouseOver;
				}
				else
				{
					if (mc.renderViewEntity != null)
						targetMOP = rayTrace(mc.renderViewEntity, mc.playerController.getBlockReachDistance(), 0F, includeLiquids);
				}
			}
		}

		if (targetMOP != null)
		{
			if (targetMOP.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
				targetItemStack = getIdentifierStack();
			
			if (targetMOP.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
				targetEntity = getIdentifierEntity();
		}
		
		return targetMOP;
	}
	
	protected Entity getIdentifierEntity()
	{
        if (targetMOP == null)
    		return null;        
        
        return targetMOP.entityHit;
	}
	
	protected ArrayList<ItemStack> getIdentifierItems()
    {
        ArrayList<ItemStack> items = new ArrayList<ItemStack>();
        
    	if (targetMOP == null|| world == null || player == null)
    		return items;
    	
        int x = targetMOP.blockX;
        int y = targetMOP.blockY;
        int z = targetMOP.blockZ;
        
        Block mouseoverBlock = world.getBlock(x, y, z);
        
        if (mouseoverBlock == null) 
        	return items;

        TileEntity tileEntity = world.getTileEntity(x, y, z); 
     
        if (tileEntity == null)
        {
	        try
	        {
	        	ItemStack block = new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z));

	        	if (block.getItem() != null)
	        		items.add(block);
	        } 
	        catch(Exception e)
	        {
	        }
        }

        if (items.size() > 0)
            return items;

        try
        {
	        ItemStack pick = mouseoverBlock.getPickBlock(targetMOP, world, x, y, z, player);
	        
	        if (pick != null)
	            items.add(pick);
        }
        catch(Exception e)
        {
        }
        
        if (items.size() > 0)
            return items;           

        if (mouseoverBlock instanceof IShearable)
        {
            IShearable shearable = (IShearable)mouseoverBlock;
            
            if(shearable.isShearable(new ItemStack(Items.shears), world, x, y, z))
            {
                items.addAll(shearable.onSheared(new ItemStack(Items.shears), world, x, y, z, 0));
            }
        }
        
        if (items.size() == 0)
           items.add(0, new ItemStack(mouseoverBlock, 1, world.getBlockMetadata(x, y, z)));
        
        return items;
    }

	protected ItemStack getIdentifierStack()
	{
		ArrayList<ItemStack> items = getIdentifierItems();
        
        if (items.isEmpty())
            return null;
        
        Collections.sort(items, new Comparator<ItemStack>()
					        {
					            @Override
					            public int compare(ItemStack stack0, ItemStack stack1)
					            {
					                return stack1.getItemDamage() - stack0.getItemDamage();
					            }
					        }
        				);

        return items.get(0);		
	}
	
	public MovingObjectPosition getTarget()
	{ 
		return targetMOP;
	}
	
	public Entity getTargetEntity()
	{
		return targetEntity;
	}	
	
	public ItemStack getTargetStack()
	{
		return targetItemStack;
	}
    
	protected MovingObjectPosition rayTrace(EntityLivingBase entity, double scale, float par3, Boolean includeLiquids)
    {
        Vec3 vec3  = entity.getPosition(par3);
        Vec3 vec31 = entity.getLook(par3);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * scale, vec31.yCoord * scale, vec31.zCoord * scale);
        
        return entity.worldObj.rayTraceBlocks(vec3, vec32, false);
    }
}
