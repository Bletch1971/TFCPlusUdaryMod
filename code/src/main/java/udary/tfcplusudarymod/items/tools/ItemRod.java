package udary.tfcplusudarymod.items.tools;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;
import udary.tfcplusudarymod.interfaces.IAnode;
import udary.tfcplusudarymod.interfaces.ICathode;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISmeltable;

public class ItemRod extends ItemTerra implements ISmeltable, IAnode, ICathode
{
	protected final ToolMaterial toolMaterial;
	protected short metalAmount;
	protected String metalName;
	protected boolean smeltable;
	protected EnumTier smeltTier;
	protected EnumItemReach reach;
	
	protected Metal anodisedMetal;
	protected boolean isAnode;
	protected boolean isCathode;
	
	public ItemRod(ToolMaterial toolMaterial, int maxDamage)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryTools);
		this.setFolder("tools/");
		
		this.setMaxDamage(maxDamage);
		this.setMaxStackSize(1);
		
		this.setSize(EnumSize.SMALL);
		this.setWeight(EnumWeight.MEDIUM);
		
		this.stackable = false;
		this.toolMaterial = toolMaterial;
		this.metalName = "";
		this.metalAmount = 0;
		this.smeltable = false;
		this.smeltTier = EnumTier.TierI;
		this.reach = EnumItemReach.SHORT;
		
		this.anodisedMetal = null;
		this.isAnode = false;
		this.isCathode = false;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public void addExtraInformation(ItemStack is, EntityPlayer player, List arraylist) 
	{		
		String unitsString = WailaUtils.getUnitsInformation(is, TFC_Core.showCtrlInformation());
		if (unitsString != "")
			arraylist.add(unitsString);
		
		String damageString = WailaUtils.getDamageInformation(is);
		if (damageString != "")
			arraylist.add(damageString);
	}
	
	@Override
	public void registerIcons(IIconRegister registerer)
	{
		if (this.metaNames == null)
		{
			if (this.iconString != null)
				this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getIconString());
			else
				this.itemIcon = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + this.getUnlocalizedName().replace("item.", ""));
		}
		else
		{
			metaIcons = new IIcon[metaNames.length];
			for(int i = 0; i < metaNames.length; i++)
			{
				metaIcons[i] = registerer.registerIcon(ModDetails.ModID + ":" + this.textureFolder + metaNames[i]);
			}
		}		
	}
	
	@Override
	public EnumItemReach getReach(ItemStack is)
	{
		return this.reach;
	}
	
	public ToolMaterial getToolMaterial()
	{
		return this.toolMaterial;
	}
	
	public short getMetalAmount() 
	{
		return metalAmount;
	}	

	public ItemRod setSmeltable(String metalName, short metalAmount, boolean smeltable, EnumTier smeltTier)
	{
		this.metalName = metalName;
		this.metalAmount = metalAmount;
		this.smeltable = smeltable;
		this.smeltTier = smeltTier;
		return this;
	}
	
	public ItemRod setSmeltable(Metal metal, short metalAmount, boolean smeltable, EnumTier smeltTier)
	{
		this.metalName = metal != null ? metal.name : "";
		this.metalAmount = metalAmount;
		this.smeltable = smeltable;
		this.smeltTier = smeltTier;
		return this;
	}
	
	@Override
	public Metal getMetalType(ItemStack is) 
	{
		return MetalRegistry.instance.getMetalFromString(metalName);
	}

	@Override
	public short getMetalReturnAmount(ItemStack is) 
	{
		// get the amount of damage to the item stack.
		double damage = is.getItemDamage();
		// check if any damage exists.
		if (damage == 0)
			// no damage, return the full amount of metal.
			return metalAmount;
		
		// item stack is damaged, get the item stack maximum damage.
		double maxDamage = is.getMaxDamage();
		
		// calculate the metal percentage to return.
		double damagePercentage = Math.floor(((maxDamage - damage) / maxDamage) * 100) / 100;
		int returnAmount = (int)(metalAmount - damagePercentage);
		return (short)Math.max(0, returnAmount);
	}

	@Override
	public boolean isSmeltable(ItemStack is) 
	{
		return smeltable;
	}

	@Override
	public EnumTier getSmeltTier(ItemStack is) 
	{
		return smeltTier;
	}
	
	@Override
    public boolean doesSneakBypassUse(World world, int x, int y, int z, EntityPlayer player)
    {
        return true;
    }
	
	public Metal getAnodisedMetal()
	{
		return anodisedMetal;
	}
	
	@Override
	public boolean getIsAnode()
	{
		return this.isAnode;
	}
	
	@Override
	public boolean getIsCathode()
	{
		return this.isCathode;
	}

	@Override
	public boolean isAnode(ItemStack is)
	{
		return (is != null && is.getItem() instanceof IAnode && ((IAnode)is.getItem()).getIsAnode());
	}

	@Override
	public boolean isCathode(ItemStack is)
	{
		return (is != null && is.getItem() instanceof ICathode && ((ICathode)is.getItem()).getIsCathode());
	}
	
	public ItemRod setAnodisedMetal(Metal metal)
	{
		this.anodisedMetal = metal;
		return this;
	}
	
	@Override
	public ItemRod setIsAnode(boolean isAnode)
	{
		this.isAnode = isAnode;
		return this;
	}
	
	@Override
	public ItemRod setIsCathode(boolean isCathode)
	{
		this.isCathode = isCathode;
		return this;
	}
}
