package udary.tfcplusudarymod.blocks.devices;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
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
import udary.tfcplusudarymod.render.blocks.devices.RenderAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;

import com.dunk.tfc.Blocks.Devices.BlockBarrel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockAnodisingVessel extends BlockBarrel
{
	protected IIcon[] icons;
	protected boolean isOpaqueCube;
	protected boolean renderAsNormalBlock;
	protected int renderType;
	
	public BlockAnodisingVessel()
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		
		this.isOpaqueCube = false;
		this.renderAsNormalBlock = false;
		this.renderType = ModBlocks.AnodisingVesselRenderId;
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@SideOnly(Side.CLIENT)
	@Override
	public void getSubBlocks(Item item, CreativeTabs creativeTabs, List list) 
	{
		list.add(new ItemStack(this, 1, 0));
	}
	
	@Override
	public void registerBlockIcons(IIconRegister iconRegister)
	{
		icons = new IIcon[6];
		icons[RenderHelper.SIDE_BOTTOM] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_bottom");
		icons[RenderHelper.SIDE_TOP] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_top");
		icons[RenderHelper.SIDE_NORTH] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_side");
		icons[RenderHelper.SIDE_SOUTH] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_back");
		icons[RenderHelper.SIDE_WEST] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_side");
		icons[RenderHelper.SIDE_EAST] = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/AnodisingVessel_side");
	}

	@Override
	public IIcon getIcon(int side, int meta)
	{
		if (side >= icons.length)
			return icons[RenderHelper.SIDE_TOP];
		return icons[side];
	}

	@Override
	public IIcon getIcon(IBlockAccess world, int x, int y, int z, int side)
	{
		int meta = world.getBlockMetadata(x, y, z);
		return getIcon(side, meta);
	}
    
	@Override
	public boolean isOpaqueCube()
	{
		return this.isOpaqueCube;
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return this.renderAsNormalBlock;
	}

	@Override
	public int getRenderType()
	{
		return this.renderType;
	}
	
	protected boolean handleInteraction(EntityPlayer player, TileEntityAnodisingVessel tileEntity, float hitX, float hitY, float hitZ)
	{
		// check for valid parameters.
		if (player == null || tileEntity == null || tileEntity.getWorldObj().isRemote)
			return false;
		
		// get the player's currently equipped item.
		ItemStack equippedIS = player.getCurrentEquippedItem();

		// check if anything is equipped in hand
		if (equippedIS == null)
		{
			// check if the vessel is sealed.
			if (!tileEntity.getSealed())
			{
				// vessel not sealed, get the anode rod.
				
				//return true;
			}
		}
		else
		{
			// try to put the item into a slot.
			
			// check if the vessel is sealed.
			if (!tileEntity.getSealed())
			{
//				// vessel not sealed, cycle through each slot.
//				for (int i = 0; i < tileEntity.getSizeInventory(); i++)
//				{
//					// get the item in the slot.
//					ItemStack is = tileEntity.getStackInSlot(i);
//					// item in the slot, move to the next slot.
//					if (is != null) continue;
//
//					// check if the item is valid for the slot
//					if (!tileEntity.isItemValidForSlot(i, equippedIS)) continue;
//						
//					// try to put the equipped item into the slot.
//					tileEntity.setInventorySlotContents(i, equippedIS);
//					
//					// check if the item was put in the slot
//					is = tileEntity.getStackInSlot(i);
//					if (is == null) continue;
//					
//					// remove the item from the player
//					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
//					
//					tileEntity.updateGui();
//					return true;
//				}
			}
		}
		
		// check if the player is sneaking
		if (player.isSneaking())
		{
			// player is sneaking, toggle the sealed state of the vessel
			if (tileEntity.getSealed())
				tileEntity.actionUnSeal(0, player);
			else
				tileEntity.actionSeal(0, player);
			tileEntity.setSealTime();
			
			tileEntity.updateGui();
			return true;
		}

		return false;
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

		if (tileEntity == null || (!(tileEntity instanceof TileEntityAnodisingVessel)))
			return false;

		TileEntityAnodisingVessel anodisingVessel = (TileEntityAnodisingVessel)tileEntity;
		
		// try to handle the players interaction with the block.
		if (handleInteraction(player, anodisingVessel, hitX, hitY, hitZ))
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}
		
		if (super.handleInteraction(player, anodisingVessel))
			return true;
		
		// could not handle the interaction, check if the player is sneaking.
		if (player.isSneaking())
			// player is sneaking, just exit.
			return false;
		
		// open the Gui associated with the block.
		player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdAnodisingVessel, world, x, y, z);
		
		return true;
	}
	
	@Override
	public TileEntity createNewTileEntity(World world, int meta)
	{
		return new TileEntityAnodisingVessel();
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		setBlockBounds(RenderAnodisingVessel.minX, RenderAnodisingVessel.minY, RenderAnodisingVessel.minZ, RenderAnodisingVessel.maxX, RenderAnodisingVessel.maxY, RenderAnodisingVessel.maxZ);
    }
    
    @Override
	@SideOnly(Side.CLIENT)
	public void randomDisplayTick(World world, int x, int y, int z, Random rand)
	{
		TileEntity tileEntity = world.getTileEntity(x, y, z);

		if (tileEntity == null || (!(tileEntity instanceof TileEntityAnodisingVessel)))
			return;
		
		TileEntityAnodisingVessel anodisingVessel = (TileEntityAnodisingVessel)tileEntity;
		if (anodisingVessel.getIsProcessing())
		{
			double centerX = (double)((float)x + 0.5F);
			double centerY = (double)((float)y + 0.5F);
			double centerZ = (double)((float)z + 0.5F);
			
		    double motionX = rand.nextGaussian() * 0.02D;
		    double motionY = rand.nextGaussian() * 0.02D;
		    double motionZ = rand.nextGaussian() * 0.02D;
		    
			world.spawnParticle(EnumParticle.CLOUD.toString(), centerX+(rand.nextDouble()-0.5), centerY, centerZ+(rand.nextDouble()-0.5), motionX, motionY, motionZ);			
		}
		else if (anodisingVessel.getCanEvaporate())
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
