package udary.tfcplusudarymod.blocks.materials;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import udary.common.enums.EnumParticle;
import udary.tfcplusudarymod.TFCUdaryMod;
import udary.tfcplusudarymod.core.ModBlocks;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.handlers.GuiHandler;
import udary.tfcplusudarymod.items.materials.ItemDryingMat;
import udary.tfcplusudarymod.render.blocks.materials.RenderDryingMat;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

import com.dunk.tfc.Blocks.BlockTerraContainer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockDryingMat extends BlockTerraContainer
{
	protected boolean isOpaqueCube;
	protected boolean renderAsNormalBlock;
	protected int renderType;
	
	public BlockDryingMat()
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryMaterials);
		
		this.isOpaqueCube = false;
		this.renderAsNormalBlock = false;
		this.renderType = ModBlocks.DryingMatRenderId;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addDestroyEffects(World world, int x, int y, int z, int meta, EffectRenderer effectRenderer)
	{
		return true;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean addHitEffects(World world, MovingObjectPosition target, EffectRenderer effectRenderer)
	{
		return true;
	}
	
	@Override
	public void breakBlock(World world, int x, int y, int z, Block block, int metadata)
	{
		eject(world, x, y, z);
	}
	
	@Override
	public boolean canPlaceBlockAt(World world, int x, int y, int z)
	{
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityDryingMat();
	}

	@Override
	public int damageDropped(int damage)
	{
		return damage;
	}

	@Override
	protected void dropBlockAsItem(World world, int x, int y, int z, ItemStack is)
	{
	}
	
	public void eject(World world, int x, int y, int z)
	{
		if (world.isRemote)
			return;
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (tileEntity == null || !(tileEntity instanceof TileEntityDryingMat))
			return;
		
		TileEntityDryingMat dryingMat = (TileEntityDryingMat)tileEntity;
		
		dryingMat.ejectContents();
		
		world.removeTileEntity(x, y, z);
	}
	
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
	
	@Override
	public IIcon getIcon(int side, int meta)
	{
		return blockIcon;
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return getIcon(side, meta);
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
 
	protected boolean handleInteraction(EntityPlayer player, TileEntityDryingMat tileEntity, float hitX, float hitY, float hitZ)
	{
		// check for valid parameters.
		if (player == null || tileEntity == null || tileEntity.getWorldObj().isRemote)
			return false;
		
		// get the player's currently equipped item.
		ItemStack equippedIS = player.getCurrentEquippedItem();
		
		// check if anything is equipped in hand.
		if (equippedIS == null)
		{
			// nothing equipped.
			boolean itemRemoved = false;
			
			// check if the player is sneaking.
			if (player.isSneaking())
			{
				// check if there are any items in the slots.
				int[] slotIndexes = TileEntityDryingMat.getInputSlotIndexes();
				
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
				int[] slotIndexes = TileEntityDryingMat.getInputSlotIndexes();
				
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
	public void harvestBlock(World world, EntityPlayer player, int x, int y, int z, int l)
	{
		eject(world, x, y, z);
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

		if (tileEntity == null || (!(tileEntity instanceof TileEntityDryingMat)))
			return false;

		TileEntityDryingMat dryingMat = (TileEntityDryingMat)tileEntity;
		
		// try to handle the players interaction with the block.
		if (handleInteraction(player, dryingMat, hitX, hitY, hitZ))
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}

		// could not handle the interaction, check if the player is sneaking.
		if (player.isSneaking())
			// player is sneaking, just exit.
			return false;
		
		// open the Gui associated with the block.
		player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdDryingMat, world, x, y, z);
		
		return true;
	}

	@Override
	public void onBlockDestroyedByExplosion(World world, int x, int y, int z, Explosion ex)
	{
		eject(world, x, y, z);
	}
	
	@Override
	public void onBlockPlacedBy(World world, int x, int y, int z, EntityLivingBase player, ItemStack is)
	{
		super.onBlockPlacedBy(world, x, y, z, player, is);
		
		if (!(is.getItem() instanceof ItemDryingMat && is.hasTagCompound()))
			return;
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (tileEntity != null && tileEntity instanceof TileEntityDryingMat)
		{
			TileEntityDryingMat evaporatorPan = (TileEntityDryingMat)tileEntity;
			
			evaporatorPan.readFromItemNBT(is.getTagCompound());
			
			world.markBlockForUpdate(x, y, z);
		}
	}
	
	@Override
	public void onBlockPreDestroy(World world, int x, int y, int z, int meta)
	{
		if (world.isRemote || !world.getGameRules().getGameRuleBooleanValue("doTileDrops")) 
			return;
		
		ItemStack is = new ItemStack(Item.getItemFromBlock(this));
		EntityItem ei = new EntityItem(world, x, y, z, is);
		
		world.spawnEntityInWorld(ei);		
	}
	
    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity == null || (!(tileEntity instanceof TileEntityDryingMat)))
			return;
		
		TileEntityDryingMat dryingMat = (TileEntityDryingMat)tileEntity;
		
		if (dryingMat.getCanDry())
		{
			double centerX = (double)((float)x + 0.5F);
			double centerY = (double)((float)y + 0.5F);
			double centerZ = (double)((float)z + 0.5F);
			
		    double motionX = 0.0D;
		    double motionY = 0.1D;
		    double motionZ = 0.0D;

			world.spawnParticle(EnumParticle.SPELL.toString(), centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), motionX, motionY, motionZ);
		}
	}
    
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		blockIcon = iconRegister.registerIcon(ModDetails.ModID + ":" + "materials/Thatch");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return this.renderAsNormalBlock;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		setBlockBounds(RenderDryingMat.minX, RenderDryingMat.minY, RenderDryingMat.minZ, RenderDryingMat.maxX, RenderDryingMat.maxY, RenderDryingMat.maxZ);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
}
