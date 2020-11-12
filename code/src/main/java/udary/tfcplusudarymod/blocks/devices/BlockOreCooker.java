package udary.tfcplusudarymod.blocks.devices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import udary.common.enums.EnumParticle;
import udary.common.helpers.RenderHelper;
import udary.tfcplusudarymod.TFCUdaryMod;
import udary.tfcplusudarymod.core.ModBlocks;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.handlers.GuiHandler;
import udary.tfcplusudarymod.render.blocks.devices.RenderOreCooker;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;

import com.dunk.tfc.Blocks.BlockTerraContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockOreCooker extends BlockTerraContainer 
{
	protected IIcon[] icons;
	protected boolean isOpaqueCube;
	protected boolean renderAsNormalBlock;
	protected int renderType;
	
	public BlockOreCooker()
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		
		this.isOpaqueCube = false;
		this.renderAsNormalBlock = false;
		this.renderType = ModBlocks.OreCookerRenderId;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityOreCooker();
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return getIcon(side, meta);
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (side < 0 || side >= icons.length)
			return icons[RenderHelper.SIDE_TOP];
		return icons[side];
	}

	@Override
	public int getRenderType()
	{
		return this.renderType;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) 
	{
		list.add(new ItemStack(this, 1, 0));
	}

	protected boolean handleInteraction(EntityPlayer player, TileEntityOreCooker tileEntity, float hitX, float hitY, float hitZ)
	{
		// check for valid parameters.
		if (player == null || tileEntity == null || tileEntity.getWorldObj().isRemote)
			return false;
		
		// get the player's currently equipped item.
		ItemStack equippedIS = player.getCurrentEquippedItem();
		
		// check if anything is equipped in hand
		if (equippedIS == null)
		{
			// nothing equipped.
			boolean itemRemoved = false;
			
			// check if the player is sneaking.
			if (player.isSneaking())
			{
				// check if there are any items in the slots.
				int[] slotIndexes = TileEntityOreCooker.getOutputSlotIndexes();
				
				// cycle through each slot.
				for (int i = 0; i < slotIndexes.length; i++)
				{
					itemRemoved = tileEntity.ejectItem(slotIndexes[i]);
					if (itemRemoved) break;
				}
			}
				
			// check if the item was removed.
			if (itemRemoved)
			{
				tileEntity.updateGui();
				return true;
			}
		}
		else
		{
			// try to put the item into a slot.
			boolean itemAdded = false;
			
			// check if the player is sneaking.
			if (player.isSneaking())
			{
				// check if there are any items in the slots.
				int[] slotIndexes = TileEntityOreCooker.getInputSlotIndexes();
				
				// cycle through each slot.
				for (int i = 0; i < slotIndexes.length; i++)
				{
					// try to merge the item.
					if (!tileEntity.mergeStackInSlot(slotIndexes[i], equippedIS)) continue;

					itemAdded = true;

					// item merged, check if we need to remove item from player.
					if (equippedIS.stackSize <= 0)
					{
						player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
						break;
					}
				}
			}
			
			// check if the item was added.
			if (itemAdded)
			{
				tileEntity.updateGui();
				return true;
			}
		}

		return false;
	}
	
	@Override
	public boolean isOpaqueCube()
	{
		return this.isOpaqueCube;
	}

	@Override
	public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int side, float hitX, float hitY, float hitZ)
	{
		if (world.isRemote)
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}

		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity == null || (!(tileEntity instanceof TileEntityOreCooker)))
			return false;

		TileEntityOreCooker oreCooker = (TileEntityOreCooker)tileEntity;
		
		// try to handle the players interaction with the block.
		if (handleInteraction(player, oreCooker, hitX, hitY, hitZ))
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}

		// could not handle the interaction, check if the player is sneaking.
		if (player.isSneaking())
			// player is sneaking, just exit.
			return false;
		
		// open the Gui associated with the block.
		player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdOreCooker, world, x, y, z);
		
		return true;
	}

	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is)
	{
		super.onBlockPlacedBy(world, x, y, z, player, is);
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (tileEntity != null && tileEntity instanceof TileEntityOreCooker)
		{
			TileEntityOreCooker oreCooker = (TileEntityOreCooker)tileEntity;

			if (is.hasTagCompound())
				oreCooker.readFromItemNBT(is.getTagCompound());
		}
	}
    
    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity == null || (!(tileEntity instanceof TileEntityOreCooker)))
			return;
		
		TileEntityOreCooker oreCooker = (TileEntityOreCooker)tileEntity;
		if (oreCooker.getCanCook())
		{
			double centerX = (double)((float)x + 0.5F);
			double centerY = (double)((float)y + 0.5F);
			double centerZ = (double)((float)z + 0.5F);
			
		    double motionX = 0.0D;
		    double motionY = 0.1D;
		    double motionZ = 0.0D;

			world.spawnParticle(EnumParticle.SMOKE.toString(), centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), motionX, motionY, motionZ);
		}
	}

    @Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		icons = new IIcon[6];
		icons[RenderHelper.SIDE_BOTTOM] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_bottom");
		icons[RenderHelper.SIDE_TOP] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_top");
		icons[RenderHelper.SIDE_NORTH] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_side");
		icons[RenderHelper.SIDE_SOUTH] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_side");
		icons[RenderHelper.SIDE_WEST] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_side");
		icons[RenderHelper.SIDE_EAST] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/OreCooker_side");
	}

    @Override
	public boolean renderAsNormalBlock()
	{
		return this.renderAsNormalBlock;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		setBlockBounds(RenderOreCooker.minX, RenderOreCooker.minY2, RenderOreCooker.minZ, RenderOreCooker.maxX, RenderOreCooker.maxY, RenderOreCooker.maxZ);
    }
    
    @Override
    public int getDamageValue(World world, int x, int y, int z)
    {
        return 0;
    }

	@Override
	public ArrayList<ItemStack> getDrops(World world, int x, int y, int z, int meta, int fortune)
	{
		ArrayList<ItemStack> itemStacks = new ArrayList<ItemStack>();
		
		int damageValue = getDamageValue(world, x, y, z);
		itemStacks.add(new ItemStack(this, 1, damageValue));
		
		return itemStacks;
	}
}
