package udary.tfcplusudarymod.blocks.devices;

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
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidContainerItem;
import udary.common.enums.EnumParticle;
import udary.tfcplusudarymod.TFCUdaryMod;
import udary.tfcplusudarymod.containers.devices.ContainerEvaporatorPan;
import udary.tfcplusudarymod.core.ModBlocks;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.handlers.GuiHandler;
import udary.tfcplusudarymod.items.devices.ItemEvaporatorPan;
import udary.tfcplusudarymod.render.blocks.devices.RenderEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;

import com.dunk.tfc.Blocks.BlockTerraContainer;
import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Items.ItemBlocks.ItemBarrels;
import com.dunk.tfc.Items.ItemBlocks.ItemLargeVessel;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockEvaporatorPan extends BlockTerraContainer
{
	protected boolean isOpaqueCube;
	protected boolean renderAsNormalBlock;
	protected int renderType;
	
	public BlockEvaporatorPan()
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryDevices);
		
		this.isOpaqueCube = false;
		this.renderAsNormalBlock = false;
		this.renderType = ModBlocks.EvaporatorPanRenderId;
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
		return new TileEntityEvaporatorPan();
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
		
		if (tileEntity == null || !(tileEntity instanceof TileEntityEvaporatorPan))
			return;
		
		TileEntityEvaporatorPan evaporatorPan = (TileEntityEvaporatorPan)tileEntity;
		
		evaporatorPan.ejectContents();
		
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
 
	protected boolean handleInteraction(EntityPlayer player, TileEntityEvaporatorPan tileEntity, float hitX, float hitY, float hitZ)
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
					itemRemoved = tileEntity.ejectItem(ContainerEvaporatorPan.SLOT_OUTPUT_4);
				else if (hitX > 0.5 && hitZ < 0.5)
					itemRemoved = tileEntity.ejectItem(ContainerEvaporatorPan.SLOT_OUTPUT_3);
				else if (hitX < 0.5 && hitZ > 0.5)
					itemRemoved = tileEntity.ejectItem(ContainerEvaporatorPan.SLOT_OUTPUT_2);
				else
					itemRemoved = tileEntity.ejectItem(ContainerEvaporatorPan.SLOT_OUTPUT_1);
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
				if (hitX <= 0.5 && hitZ <= 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_4, equippedIS);
				else if (hitX > 0.5 && hitZ <= 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_3, equippedIS);
				else if (hitX <= 0.5 && hitZ > 0.5)
					itemAdded = tileEntity.mergeStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_2, equippedIS);
				else
					itemAdded = tileEntity.mergeStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_1, equippedIS);

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
			
			// check if the equipped item stack is a filled fluid container.
			if ((FluidContainerRegistry.isFilledContainer(equippedIS) || 
				(equippedIS.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)equippedIS.getItem()).getFluid(equippedIS) != null)))
			{
				ItemStack tempIS = equippedIS.copy();
				tempIS.stackSize = 1;
				equippedIS.stackSize--;
	
				if (equippedIS.stackSize == 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
	
				ItemStack resultIS = tileEntity.addFluid(tempIS);
	
				if (equippedIS.stackSize == 0 && (resultIS.getMaxStackSize() == 1 || !player.inventory.hasItemStack(resultIS)))
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, resultIS);
				}
				else
				{
					if (!player.inventory.addItemStackToInventory(resultIS))
					{
						// if the new item dosn't fit, drop it.
						player.dropPlayerItemWithRandomChoice(resultIS, false); 
					}
				}
	
				if (player.inventoryContainer != null)
				{
					// for some reason the players inventory won't update without this.
					player.inventoryContainer.detectAndSendChanges();
				}
	
				return true;
			}
			// check if the equipped item stack is an empty fluid container.
			else if ((FluidContainerRegistry.isEmptyContainer(equippedIS) || 
				(equippedIS.getItem() instanceof IFluidContainerItem && ((IFluidContainerItem)equippedIS.getItem()).getFluid(equippedIS) == null)))
			{
				ItemStack tempIS = equippedIS.copy();
				tempIS.stackSize = 1;
				equippedIS.stackSize--;
	
				if (equippedIS.stackSize == 0)
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, null);
				}
	
				ItemStack resultIS = tileEntity.removeFluid(tempIS);
	
				if (equippedIS.stackSize == 0 && (resultIS.getMaxStackSize() == 1 || !player.inventory.hasItemStack(resultIS)))
				{
					player.inventory.setInventorySlotContents(player.inventory.currentItem, resultIS);
				}
				else
				{
					if (!player.inventory.addItemStackToInventory(resultIS))
					{
						// if the new item dosn't fit, drop it.
						player.dropPlayerItemWithRandomChoice(resultIS, false);
					}
				}
	
				if (player.inventoryContainer != null)
				{
					// for some reason the players inventory won't update without this.
					player.inventoryContainer.detectAndSendChanges();
				}
	
				return true;			
			}
			else if (equippedIS.getItem() instanceof ItemBarrels || equippedIS.getItem() instanceof ItemLargeVessel)
			{
				ItemStack tempIS = equippedIS.copy();
				tempIS.stackSize = 1;
				
				if (equippedIS.hasTagCompound())
				{
					if (equippedIS.getTagCompound().hasKey(ModTags.TAG_FLUID_TAG) && !equippedIS.getTagCompound().hasKey("Items") && tileEntity.getFluidLevel() < tileEntity.getMaxFluid())
					{
						FluidStack fs = FluidStack.loadFluidStackFromNBT(equippedIS.getTagCompound().getCompoundTag(ModTags.TAG_FLUID_TAG));
						tileEntity.addFluid(fs);
						
						if (fs.amount == 0)
						{
							equippedIS.getTagCompound().removeTag(ModTags.TAG_FLUID_TAG);
							equippedIS.getTagCompound().removeTag(ModTags.TAG_SEALED);
							if (equippedIS.getTagCompound().hasNoTags())
							{
								equippedIS.setTagCompound(null);
							}
						}
						else
						{
							fs.writeToNBT(equippedIS.getTagCompound().getCompoundTag(ModTags.TAG_FLUID_TAG));
						}
						
						return true;
					}
				}
				else
				{
					if (tileEntity.getFluidStack() != null)
					{
						NBTTagCompound nbt = new NBTTagCompound();
						
						if (tempIS.getItem() instanceof ItemBarrels)
						{
							nbt.setTag(ModTags.TAG_FLUID_TAG, tileEntity.getFluidStack().writeToNBT(new NBTTagCompound()));
							nbt.setBoolean(ModTags.TAG_SEALED, true);
							tempIS.setTagCompound(nbt);
							
							tileEntity.actionEmpty();
							equippedIS.stackSize--;
							
							TFC_Core.giveItemToPlayer(tempIS, player);
						}
						else if (tempIS.getItem() instanceof ItemLargeVessel)
						{
							if (tempIS.getItemDamage() == 0)
								return false;
	
							FluidStack fs = tileEntity.getFluidStack().copy();
							
							if (fs.amount > 5000)
							{
								fs.amount = 5000;
								tileEntity.drainFluid(5000);
							}
							else
							{
								tileEntity.actionEmpty();
							}
							
							nbt.setTag(ModTags.TAG_FLUID_TAG, fs.writeToNBT(new NBTTagCompound()));
							nbt.setBoolean(ModTags.TAG_SEALED, true);
							tempIS.setTagCompound(nbt);
							
							equippedIS.stackSize--;
							
							TFC_Core.giveItemToPlayer(tempIS, player);
						}
						return true;
					}
				}
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

		if (tileEntity == null || (!(tileEntity instanceof TileEntityEvaporatorPan)))
			return false;

		TileEntityEvaporatorPan evaporatorPan = (TileEntityEvaporatorPan)tileEntity;
		
		// try to handle the players interaction with the block.
		if (handleInteraction(player, evaporatorPan, hitX, hitY, hitZ))
		{
			world.markBlockForUpdate(x, y, z);
			return true;
		}

		// could not handle the interaction, check if the player is sneaking.
		if (player.isSneaking())
			// player is sneaking, just exit.
			return false;
		
		// open the Gui associated with the block.
		player.openGui(TFCUdaryMod.instance, GuiHandler.GuiIdEvaporatorPan, world, x, y, z);
		
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
		
		if (!(is.getItem() instanceof ItemEvaporatorPan && is.hasTagCompound()))
			return;
		
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (tileEntity != null && tileEntity instanceof TileEntityEvaporatorPan)
		{
			TileEntityEvaporatorPan evaporatorPan = (TileEntityEvaporatorPan)tileEntity;
			
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

		if (tileEntity == null || (!(tileEntity instanceof TileEntityEvaporatorPan)))
			return;
		
		TileEntityEvaporatorPan evaporatorPan = (TileEntityEvaporatorPan)tileEntity;
		
		if (evaporatorPan.getCanEvaporate())
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
		blockIcon = iconRegister.registerIcon(ModDetails.ModID + ":" + "devices/EvaporatorPan_bottom");
	}

	@Override
	public boolean renderAsNormalBlock()
	{
		return this.renderAsNormalBlock;
	}

    @Override
    public void setBlockBoundsBasedOnState(IBlockAccess world, int x, int y, int z)
    {
		setBlockBounds(RenderEvaporatorPan.minX, RenderEvaporatorPan.minY, RenderEvaporatorPan.minZ, RenderEvaporatorPan.maxX, RenderEvaporatorPan.maxY, RenderEvaporatorPan.maxZ);
    }

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockAccess world, int x, int y, int z, int side)
	{
		return true;
	}
}
