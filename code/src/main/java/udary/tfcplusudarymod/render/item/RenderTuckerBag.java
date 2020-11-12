package udary.tfcplusudarymod.render.item;

import org.lwjgl.opengl.GL11;

import com.dunk.tfc.api.Interfaces.IEquipable;

import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.enums.EnumTuckerBagVersion;
import udary.tfcplusudarymod.interfaces.IRender;
import udary.tfcplusudarymod.items.tools.ItemTuckerBag;
import udary.tfcplusudarymod.render.models.ModelTuckerBag;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class RenderTuckerBag implements IRender
{
	protected static final ResourceLocation texture_I = new ResourceLocation(ModDetails.ModID, "textures/models/tools/tuckerbag.png");
	protected static final ResourceLocation texture_II = new ResourceLocation(ModDetails.ModID, "textures/models/tools/tuckerbagII.png");
	
	protected ModelTuckerBag model = new ModelTuckerBag();

	@Override
	public void render(Entity entity, ItemStack is) 
	{
		if (entity instanceof EntityPlayer && is != null)
			doRender((EntityPlayer)entity, is);
	}
	
	protected void doRender(EntityPlayer player, ItemStack is)
	{
		if (player == null || is == null || !(is.getItem() instanceof ItemTuckerBag))
			return;
		
		ItemTuckerBag tuckerBag = (ItemTuckerBag)is.getItem();
		if (!EnumTuckerBagVersion.isValid(tuckerBag.getBagVersion(is)))
			return;
			
        GL11.glPushMatrix();

        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

        switch (tuckerBag.getBagVersion(is))
        {
	        case VERSION_2:
	            Minecraft.getMinecraft().renderEngine.bindTexture(texture_II);
	        	break;
			default:
	            Minecraft.getMinecraft().renderEngine.bindTexture(texture_I);
				break;
        }
        
		if (is.getItem() instanceof IEquipable)
			((IEquipable)is.getItem()).onEquippedRender();

		if (tuckerBag.hasEntity(is))
	        model.renderFull(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);
		else
	        model.render(player, 0F, 0F, 0F, 0F, 0F, 0.0625F);

        GL11.glPopMatrix();
	}
}
