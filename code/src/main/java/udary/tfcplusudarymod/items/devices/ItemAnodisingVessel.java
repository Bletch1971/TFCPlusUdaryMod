package udary.tfcplusudarymod.items.devices;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidBlock;
import net.minecraftforge.fluids.IFluidContainerItem;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Time;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.Items.ItemBlocks.ItemBarrels;
import com.dunk.tfc.Items.ItemBlocks.ItemTerraBlock;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.IEquipable;
import com.dunk.tfc.api.Util.Helper;

public class ItemAnodisingVessel extends ItemTerraBlock implements IFluidContainerItem, IEquipable
{
	protected boolean canStack;
	protected EnumItemReach reach;
	protected EnumSize size;
	protected EnumWeight weight;
	protected int capacity;
	protected EquipType equipType;
	
	public ItemAnodisingVessel(Block block)
	{
		super(block);
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		this.setFolder("devices/");
		
		this.setMaxDamage(ModFluids.AnodisingVesselVolume);
		this.setMaxStackSize(1);
		
		this.canStack = false;
		this.reach = EnumItemReach.SHORT;
		this.size = EnumSize.MEDIUM;
		this.weight = EnumWeight.HEAVY;
		this.capacity = ModFluids.AnodisingVesselVolume;
		this.equipType = EquipType.BACK;
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addInformation(ItemStack is, EntityPlayer player, List arraylist, boolean flag)
	{
		ItemTerra.addSizeInformation(is, arraylist);
		
		String capacityString = WailaUtils.getCapacityInformation(getCapacity(null));
		if (capacityString != "")
			arraylist.add(capacityString);
		
		NBTTagCompound nbt = is.getTagCompound();
		
		if (nbt != null && nbt.hasKey(ModTags.TAG_FLUID_TAG))
		{
			NBTTagCompound fluidTag = nbt.getCompoundTag(ModTags.TAG_FLUID_TAG);
			FluidStack fs = FluidStack.loadFluidStackFromNBT(fluidTag);

			String fluidString = WailaUtils.getFluidInformation(fs, 0, true);
			if (fluidString != "")
				arraylist.add(fluidString);
		}
	}
	
	@Override
	public boolean canStack()
	{
		return this.canStack;
	}

	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}

	@Override
	public EnumSize getSize(ItemStack is)
	{
		return this.size;
	}

	@Override
	public EnumWeight getWeight(ItemStack is)
	{
		return this.weight;
	}
	
	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		MovingObjectPosition mop = Helper.getMovingObjectPositionFromPlayer(world, player, true);

		if (mop != null && mop.typeOfHit == MovingObjectType.BLOCK)
		{
			int i = mop.blockX;
			int j = mop.blockY;
			int k = mop.blockZ;
			Block mopBlock = world.getBlock(i, j, k);

			if (player.canPlayerEdit(i, j, k, mop.sideHit, is) && mopBlock instanceof IFluidBlock && !is.hasTagCompound())
			{
				// get the fluid from the fluid block
				Fluid fluid = ((IFluidBlock)mopBlock).getFluid();
				
				if (fluid != null)
				{
					// set the fluid block to air
					world.setBlockToAir(i, j, k);

					// get the capacity of the container
					int amount = getCapacity(null);
					
					if (is.stackSize > 1)
					{
						// reduce the stack size by 1
						is.stackSize--;
						
						// copy the item stack
						ItemStack outIS = is.copy();
						// reset the output stack size to 1
						outIS.stackSize = 1;
						
						// fill the output item stack with the fluid
						ItemBarrels.fillItemBarrel(outIS, new FluidStack(fluid, amount), amount);
						
						// give the output stack to the player
						if (!player.inventory.addItemStackToInventory(outIS))
						{
							player.entityDropItem(outIS, 0);
						}
					}
					else
					{
						// fill the item stack with the fluid
						ItemBarrels.fillItemBarrel(is, new FluidStack(fluid, amount), amount);
					}
					
					return true;					
				}
			}
		}

		return super.onItemUse(is, player, world, x, y, z, side, hitX, hitY, hitZ);
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
	}

	@Override
	public int getCapacity(ItemStack container) 
	{
		return this.capacity;
	}

	@Override
	public FluidStack getFluid(ItemStack container) 
	{
		if (container == null || container.stackTagCompound == null || !container.stackTagCompound.hasKey(ModTags.TAG_FLUID_TAG))
		{
			return null;
		}
		
		NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag(ModTags.TAG_FLUID_TAG);
		return FluidStack.loadFluidStackFromNBT(fluidTag);
	}

	@Override
	public int fill(ItemStack container, FluidStack resource, boolean doFill) 
	{
		if (container == null || resource == null) return 0;
		
		int capacity = getCapacity(container);
		int filled = 0;
		
        if (!doFill)
        {
            if (container.stackTagCompound == null || !container.stackTagCompound.hasKey(ModTags.TAG_FLUID_TAG))
            {
                return Math.min(capacity, resource.amount);
            }

            NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag(ModTags.TAG_FLUID_TAG);
            FluidStack fs = FluidStack.loadFluidStackFromNBT(fluidTag);

            if (fs == null)
            {
                return Math.min(capacity, resource.amount);
            }

            if (!fs.isFluidEqual(resource))
            {
                return 0;
            }

            return Math.min(capacity - fs.amount, resource.amount);
        }

        if (container.stackTagCompound == null)
        {
            container.stackTagCompound = new NBTTagCompound();
        }

        if (!container.stackTagCompound.hasKey(ModTags.TAG_FLUID_TAG))
        {
            NBTTagCompound fluidTag = resource.writeToNBT(new NBTTagCompound());
            filled = resource.amount;
            
            if (capacity < resource.amount)
            {
            	filled = capacity;
                fluidTag.setInteger(ModTags.TAG_FLUID_AMOUNT, capacity);
            }

            container.stackTagCompound.setTag(ModTags.TAG_FLUID_TAG, fluidTag);
            container.stackTagCompound.setBoolean(ModTags.TAG_SEALED, true);
            container.stackTagCompound.setInteger(ModTags.TAG_SEALTIME, (int)TFC_Time.getTotalHours());

            return filled;
        }

        NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag(ModTags.TAG_FLUID_TAG);
        FluidStack fs = FluidStack.loadFluidStackFromNBT(fluidTag);

        if (!fs.isFluidEqual(resource))
        {
            return 0;
        }

        filled = capacity - fs.amount;
        if (resource.amount < filled)
        {
        	fs.amount += resource.amount;
            filled = resource.amount;
        }
        else
        {
        	fs.amount = capacity;
        }

        container.stackTagCompound.setTag(ModTags.TAG_FLUID_TAG, fs.writeToNBT(fluidTag));
        container.stackTagCompound.setBoolean(ModTags.TAG_SEALED, true);
        container.stackTagCompound.setInteger(ModTags.TAG_SEALTIME, (int)TFC_Time.getTotalHours());
        
        return filled;
	}

	@Override
	public FluidStack drain(ItemStack container, int maxDrain, boolean doDrain) 
	{
		if (container == null || container.stackTagCompound == null || !container.stackTagCompound.hasKey(ModTags.TAG_FLUID_TAG))
		{
			return null;
		}

		NBTTagCompound fluidTag = container.stackTagCompound.getCompoundTag(ModTags.TAG_FLUID_TAG);
		FluidStack fs = FluidStack.loadFluidStackFromNBT(fluidTag);
		
		if (fs == null)
		{
			return null;
		}

		int currentAmount = fs.amount;
		fs.amount = Math.min(fs.amount, maxDrain);

		if (doDrain)
		{
			if (currentAmount == fs.amount)
			{
				container.stackTagCompound.removeTag(ModTags.TAG_FLUID_TAG);
				container.stackTagCompound.removeTag(ModTags.TAG_SEALED);
				container.stackTagCompound.removeTag(ModTags.TAG_SEALTIME);

				if (container.stackTagCompound.hasNoTags())
				{
					container.stackTagCompound = null;
				}
			}
			else
			{
				fluidTag.setInteger(ModTags.TAG_FLUID_AMOUNT, currentAmount - fs.amount);
				
				container.stackTagCompound.setTag(ModTags.TAG_FLUID_TAG, fluidTag);
				container.stackTagCompound.setBoolean(ModTags.TAG_SEALED, true);
				container.stackTagCompound.setInteger(ModTags.TAG_SEALTIME, (int)TFC_Time.getTotalHours());
			}
		}
		
		return fs;
	}

	@Override
	public EquipType getEquipType(ItemStack is) 
	{
		return this.equipType;
	}

	@Override
	public boolean getTooHeavyToCarry(ItemStack is) 
	{
		if (is.hasTagCompound())
		{
			if (is.stackTagCompound.hasKey(ModTags.TAG_FLUID_TAG))
			{
				NBTTagCompound fluidTag = is.stackTagCompound.getCompoundTag(ModTags.TAG_FLUID_TAG);
				FluidStack fs = FluidStack.loadFluidStackFromNBT(fluidTag);
				
				return (fs != null && fs.amount > 0);
			}
		}
		
		return false;
	}

	@Override
	public void onEquippedRender()
	{
		GL11.glTranslatef(0, 0.0f, -0.2F);
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
