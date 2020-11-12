package udary.tfcplusudarymod.items.materials;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTabs;

import com.dunk.tfc.Core.Metal.MetalRegistry;
import com.dunk.tfc.Items.ItemTerra;
import com.dunk.tfc.api.Metal;
import com.dunk.tfc.api.Enums.EnumItemReach;
import com.dunk.tfc.api.Enums.EnumSize;
import com.dunk.tfc.api.Enums.EnumWeight;
import com.dunk.tfc.api.Interfaces.ISmeltable;

public class ItemBuckle extends ItemTerra implements ISmeltable
{
	protected ToolMaterial toolMaterial;
	protected short metalAmount;
	protected String metalName;
	protected boolean smeltable;
	protected EnumTier smeltTier;	
	protected EnumItemReach reach;
	
	public ItemBuckle(ToolMaterial toolMaterial, int damage)
	{
		super();
		
		this.setCreativeTab(ModTabs.UdaryMaterials);
		this.setFolder("materials/");
		
		this.setMaxDamage(damage);
		
		this.setSize(EnumSize.VERYSMALL);
		this.setWeight(EnumWeight.LIGHT);
		
		this.toolMaterial = toolMaterial;
		this.metalName = "";
		this.metalAmount = 0;
		this.smeltable = false;
		this.smeltTier = EnumTier.TierI;
		this.reach = EnumItemReach.SHORT;
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
	
	public ItemBuckle setSmeltable(String metalName, short metalAmount, boolean smeltable, EnumTier smeltTier)
	{
		this.metalName = metalName;
		this.metalAmount = metalAmount;
		this.smeltable = smeltable;
		this.smeltTier = smeltTier;
		return this;
	}
	
	public ItemBuckle setSmeltable(Metal metal, short metalAmount, boolean smeltable, EnumTier smeltTier)
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
}
