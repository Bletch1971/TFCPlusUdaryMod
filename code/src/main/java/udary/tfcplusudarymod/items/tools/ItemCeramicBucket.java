package udary.tfcplusudarymod.items.tools;

import java.util.List;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import cpw.mods.fml.common.eventhandler.Event;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.IFluidBlock;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModItems;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

public class ItemCeramicBucket extends ItemTerra 
{
	/** field for checking if the bucket has been filled. */
	private Block bucketContents;
	protected Fluid fluid = null;
    protected IIcon containerIcon;
	protected int capacity;
	protected EnumAction action;
	protected EnumItemReach reach;

	public ItemCeramicBucket(Block contents)
	{
		super();

		this.setCreativeTab(ModTabs.UdaryTools);	
		this.setFolder("tools/");

		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.MEDIUM);
		this.setWeight(EnumWeight.LIGHT);
		
		this.bucketContents = contents;
		this.stackable = false;
		this.capacity = ModFluids.CeramicBucketVolume;
		this.action = EnumAction.none;
		this.reach = EnumItemReach.SHORT;
		
		if (contents instanceof IFluidBlock)
		{
			this.fluid = ((IFluidBlock)contents).getFluid();
		}
		
		if (this.fluid != null)
		{
			this.setCreativeTab(ModTabs.UdaryFluids);	
			this.setFolder("fluids/");
		}
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List arraylist) 
	{		
		String capacityString = WailaUtils.getCapacityInformation(getCapacity());
		if (capacityString != "")
			arraylist.add(capacityString);
		
		NBTTagCompound nbt = is.getTagCompound();
		if (nbt != null)
		{
			addFoodInformation(is, player, arraylist, nbt);
			
			if (nbt.hasKey(ModTags.TAG_FOOD_WEIGHT) && nbt.getFloat(ModTags.TAG_FOOD_WEIGHT) != 999)
			{
				if (TFC_Core.showCtrlInformation())
					ItemFoodTFC.addTasteInformation(is, player, arraylist);
				else
					arraylist.add(StatCollector.translateToLocal("gui.showtaste"));				
			}
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addFoodInformation(ItemStack is, EntityPlayer player, List arraylist, NBTTagCompound nbt)
	{
		List<String> foodStrings = WailaUtils.getFoodInformation(is);
		if (foodStrings != null)
			arraylist.addAll(foodStrings);
	}

	public ItemCeramicBucket(Block contents, Item container)
	{
		this(contents);
		this.setContainerItem(container);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registerer)
	{
		this.containerIcon = registerer.registerIcon(ModDetails.ModID + ":Ceramic Bucket");
		this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":Ceramic Bucket Overlay");		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(ItemStack is, int pass)
	{
		return pass == 1 ? this.itemIcon : this.containerIcon;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public int getColorFromItemStack(ItemStack is, int pass)
	{
		if (fluid == null) return pass == 1 ? 4669231 : super.getColorFromItemStack(is, pass);
		return pass == 1 ? fluid.getColor() : super.getColorFromItemStack(is, pass);
	}

	public int getCapacity()
	{
		return this.capacity;
	}

	@Override
	public EnumAction getItemUseAction(ItemStack is)
	{
		return this.action;
	}

	@Override
	public int getMaxItemUseDuration(ItemStack is)
	{
		return 0;
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	@Override
	public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
	{
		if (!player.capabilities.isCreativeMode)
			--is.stackSize;
		
		if (!world.isRemote)
		{
			updatePlayerFoodStats(is, world, player);
		}

		if (!player.capabilities.isCreativeMode)
		{
			ItemStack outputIS = new ItemStack(ModItems.CeramicBucket);
			outputIS.stackTagCompound = null;

			if (is.stackSize <= 0)
				return outputIS;

			player.inventory.addItemStackToInventory(outputIS);
		}

		return is;
	}
	
	protected void updatePlayerFoodStats(ItemStack is, World world, EntityPlayer player)
	{
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }

	/**
	 * Called whenever this item is equipped and the right mouse button is pressed. Args: itemStack, world, entityPlayer
	 */
	@Override
	public ItemStack onItemRightClick(ItemStack is, World world, EntityPlayer player)
	{
		boolean isEmpty = this.bucketContents == Blocks.air;
		MovingObjectPosition mop = this.getMovingObjectPositionFromPlayer(world, player, isEmpty);

		if (mop == null)
			return is;

		if (mop.typeOfHit == MovingObjectType.BLOCK)
		{
			int x = mop.blockX;
			int y = mop.blockY;
			int z = mop.blockZ;

			if (!world.canMineBlock(player, x, y, z))
				return is;

			if (isEmpty)
			{
				if (!player.canPlayerEdit(x, y, z, mop.sideHit, is))
					return is;

				FillBucketEvent event = new FillBucketEvent(player, is, world, mop);
				if (MinecraftForge.EVENT_BUS.post(event) || event.isCanceled())
					return is;

				if (event.getResult() == Event.Result.ALLOW)
					return event.result;

				Block block = world.getBlock(x, y, z);
				
				if (TFC_Core.isFreshWater(block))
				{
					world.setBlockToAir(x, y, z);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketWater);
				}
				else if (TFC_Core.isSaltWater(block))
				{
					world.setBlockToAir(x, y, z);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketSaltWater);
				}
				else if (TFC_Core.isHotWater(block))
				{
					world.setBlockToAir(x, y, z);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketHotWater);
				}
				
				// Handle flowing water
				int flowX = x;
				int flowY = y;
				int flowZ = z;
				switch(mop.sideHit)
				{
				case 0:
					flowY = y - 1;
					break;
				case 1:
					flowY = y + 1;
					break;
				case 2:
					flowZ = z - 1;
					break;
				case 3:
					flowZ = z + 1;
					break;
				case 4:
					flowX = x - 1;
					break;
				case 5:
					flowX = x + 1;
					break;
				}
				
				Block flowBlock = world.getBlock(flowX, flowY, flowZ);
				
				if (TFC_Core.isFreshWater(flowBlock))
				{
					world.setBlockToAir(flowX, flowY, flowZ);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketWater);
				}
				else if (TFC_Core.isSaltWater(flowBlock))
				{
					world.setBlockToAir(flowX, flowY, flowZ);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketSaltWater);
				}
				else if (TFC_Core.isHotWater(flowBlock))
				{
					world.setBlockToAir(flowX, flowY, flowZ);
					if (player.capabilities.isCreativeMode)
						return is;
					return new ItemStack(ModItems.CeramicBucketHotWater);
				}
			}
			else
			{
				return new ItemStack(ModItems.CeramicBucket);
			}
		}
		
		return is;
	}

	@Override
	public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		boolean isEmpty = this.bucketContents == Blocks.air;
		int[][] map = {{0,-1,0},{0,1,0},{0,0,-1},{0,0,1},{-1,0,0},{1,0,0}};

		if (!isEmpty && world.getBlock(x, y, z) != Blocks.cauldron && world.isAirBlock(x + map[side][0], y + map[side][1], z + map[side][2]))
		{
			world.setBlock( x + map[side][0], y + map[side][1], z + map[side][2], TFCBlocks.freshWater, 2, 0x1 );
			player.setCurrentItemOrArmor(0, new ItemStack(ModItems.CeramicBucket));
			return true;
		}

		if (!isEmpty && world.getBlock(x, y, z) == Blocks.cauldron)
		{
			int meta = world.getBlockMetadata(x, y, z);
			if (meta < 3)
			{
				if (!player.capabilities.isCreativeMode)
				{
					player.setCurrentItemOrArmor(0, new ItemStack(ModItems.CeramicBucket));
				}
				world.setBlockMetadataWithNotify(x, y, z, MathHelper.clamp_int(3, 0, 3), 2);
				world.func_147453_f(x, y, z, Blocks.cauldron);

				return true;
			}
		}

		return false;
	}
}
