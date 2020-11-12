package udary.tfcplusudarymod.blocks.devices;

import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import udary.tfcplusudarymod.TFCUdaryMod;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyCalculator;
import udary.tfcplusudarymod.core.ModBlocks;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.handlers.GuiHandler;
import udary.tfcplusudarymod.render.blocks.devices.RenderAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;

import com.dunk.tfc.Blocks.BlockTerraContainer;
import com.dunk.tfc.Core.TFC_Textures;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAlloyCalculator extends BlockTerraContainer
{
	protected boolean blocksMovement;
	protected boolean isOpaqueCube;
	protected boolean renderAsNormalBlock;
	protected int renderType;
	
	public BlockAlloyCalculator()
	{
		super(Material.wood);
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		
		this.blocksMovement = true;
		this.isOpaqueCube = false;
		this.renderAsNormalBlock = false;
		this.renderType = ModBlocks.AlloyCalculatorRenderId;
	}

	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityAlloyCalculator();
	}

	@Override
	public boolean getBlocksMovement(IBlockAccess world, int x, int y, int z)
	{
		return this.blocksMovement;
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		return this.blockIcon;
	}

	@Override
	public int getRenderType()
	{
		return this.renderType;
	}

	protected boolean handleInteraction(EntityPlayer player, TileEntityAlloyCalculator tileEntity, float hitX, float hitY, float hitZ)
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
				// check the hit point of the block.
				if (hitX < 0.5 && hitZ < 0.5)
					itemRemoved = tileEntity.ejectItem(ContainerAlloyCalculator.SLOT_INPUT_4);
				else if (hitX > 0.5 && hitZ < 0.5)
					itemRemoved = tileEntity.ejectItem(ContainerAlloyCalculator.SLOT_INPUT_3);
				else if (hitX < 0.5 && hitZ > 0.5)
					itemRemoved = tileEntity.ejectItem(ContainerAlloyCalculator.SLOT_INPUT_2);
				else
					itemRemoved = tileEntity.ejectItem(ContainerAlloyCalculator.SLOT_INPUT_1);
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
				// check the hit point of the block.
				if (hitX < 0.5 && hitZ < 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_4, equippedIS);
				else if (hitX > 0.5 && hitZ < 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_3, equippedIS);
				else if (hitX < 0.5 && hitZ > 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_2, equippedIS);
				else
					itemAdded = tileEntity.mergeStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_1, equippedIS);

				// item merged, check if we need to remove item from player.
				if (equippedIS.stackSize <= 0)
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
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

		if (tileEntity == null || (!(tileEntity instanceof TileEntityAlloyCalculator)))
			return false;

		TileEntityAlloyCalculator alloyCalculator = (TileEntityAlloyCalculator)tileEntity;
		
		// try to handle the players interaction with the block.
		if (handleInteraction(player, alloyCalculator, hitX, hitY, hitZ))
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		
		// could not handle the interaction, check if the player is sneaking.
		if (player.isSneaking())
			// player is sneaking, just exit.
			return false;
		
		// open the Gui associated with the block.
		player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdAlloyCalculator, world, x, y, z);

		return true;
	}

	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		this.blockIcon = TFC_Textures.invisibleTexture;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return this.renderAsNormalBlock;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		setBlockBounds(RenderAlloyCalculator.minX, RenderAlloyCalculator.minY, RenderAlloyCalculator.minZ, RenderAlloyCalculator.maxX, RenderAlloyCalculator.maxY, RenderAlloyCalculator.maxZ);
    }
    
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side) 
	{
		return true;
	}
}
