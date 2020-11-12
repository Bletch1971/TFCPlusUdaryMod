package udary.tfcplusudarymod.items.fluids;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModFluids;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.core.ModTags;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.TFC_Sounds;
import com.dunk.tfc.Food.ItemFoodTFC;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.TileEntities.TEPottery;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemCeramicJugFluid extends ItemTerra
{
    @SideOnly(Side.CLIENT)
    protected IIcon containerIcon;   
	protected Fluid fluid = null;
	protected int capacity;
	protected EnumAction action;
	protected EnumItemReach reach;	
	
	public ItemCeramicJugFluid(Fluid fluid)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryFluids);	
		this.setFolder("fluids/");

		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.SMALL);
		this.setWeight(EnumWeight.LIGHT);
		
		this.fluid = fluid;
		this.stackable = false;
		this.capacity = ModFluids.CeramicJugVolume;
		this.action = EnumAction.none;
		this.reach = EnumItemReach.SHORT;
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
		
		if (TFC_Core.showShiftInformation()) 
		{
			arraylist.add(StatCollector.translateToLocal("gui.Help"));
			arraylist.add(StatCollector.translateToLocal("gui.PotteryBase.Inst0"));
		} 
		else
		{
			arraylist.add(StatCollector.translateToLocal("gui.ShowHelp"));
		}
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	protected void addFoodInformation(ItemStack is, EntityPlayer player, List arraylist, NBTTagCompound nbt)
	{
		List<String> foodStrings = WailaUtils.getFoodInformation(is);
		if (foodStrings != null)
			arraylist.addAll(foodStrings);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister registerer)
	{
		this.containerIcon = registerer.registerIcon(ModDetails.ModID + ":Ceramic Jug");
		this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":Ceramic Jug Overlay");		
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
		if (fluid == null) return super.getColorFromItemStack(is, pass);
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
			boolean broken = false;
			if(world.rand.nextInt(50) == 0)
			{
				player.playSound(TFC_Sounds.CERAMICBREAK, 0.7f, player.worldObj.rand.nextFloat() * 0.2F + 0.8F);
				broken = true;
			}

			ItemStack outputIS = new ItemStack(TFCItems.potteryJug, 1, 1);
			outputIS.stackTagCompound = null;

			if (!broken && is.stackSize <= 0)
				return outputIS;

			player.inventory.addItemStackToInventory(outputIS);
		}

		return is;
	}
	
	protected void updatePlayerFoodStats(ItemStack is, World world, EntityPlayer player)
	{
	}
	
	@Override
	public boolean onItemUse(ItemStack itemstack, EntityPlayer entityplayer, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ)
	{
		if (!world.isRemote && entityplayer.isSneaking())
		{
			if (side == 1)
			{
				int offset = 0;
				if(world.getBlock(x, y, z) != TFCBlocks.pottery && world.isAirBlock(x, y+1, z))
				{
					// We only want the pottery to be placeable if the block is solid on top.
					if(!world.isSideSolid(x, y, z, ForgeDirection.UP))
						return false;
					
					world.setBlock(x, y+1, z, TFCBlocks.pottery);
					offset = 1;
				}

				if (world.getTileEntity(x, y+offset, z) != null && world.getTileEntity(x, y+offset, z) instanceof TEPottery) 
				{
					TEPottery te = (TEPottery)world.getTileEntity(x, y+offset, z);
					
					if (hitX < 0.5 && hitZ < 0.5)
					{
						if (te.canAddItem(0))
						{
							te.inventory[0] = new ItemStack(this, 1, itemstack.getItemDamage());
							te.inventory[0].stackTagCompound = itemstack.stackTagCompound;
							itemstack.stackSize--;
							world.markBlockForUpdate(x, y+offset, z);
						}
					}
					else if(hitX > 0.5 && hitZ < 0.5)
					{
						if (te.canAddItem(1))
						{
							te.inventory[1] = new ItemStack(this, 1, itemstack.getItemDamage());
							te.inventory[1].stackTagCompound = itemstack.stackTagCompound;
							itemstack.stackSize--;
							world.markBlockForUpdate(x, y+offset, z);
						}
					}
					else if(hitX < 0.5 && hitZ > 0.5)
					{
						if (te.canAddItem(2))
						{
							te.inventory[2] = new ItemStack(this, 1, itemstack.getItemDamage());
							te.inventory[2].stackTagCompound = itemstack.stackTagCompound;
							itemstack.stackSize--;
							world.markBlockForUpdate(x, y+offset, z);
						}
					}
					else if (hitX > 0.5 && hitZ > 0.5)
					{
						if (te.canAddItem(3))
						{
							te.inventory[3] = new ItemStack(this, 1, itemstack.getItemDamage());
							te.inventory[3].stackTagCompound = itemstack.stackTagCompound;
							itemstack.stackSize--;
							world.markBlockForUpdate(x, y+offset, z);
						}
					}
				}
				
				return true;				
			}
		}
		
		return false;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
}
