package udary.tfcplusudarymod.render.item;

import org.lwjgl.opengl.GL11;

import udary.common.ItemHeatDetails;
import udary.common.helpers.RenderHelper;
import udary.tfcplusudarymod.core.ModOptions;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class Item2DHeatRenderer implements IItemRenderer
{
	protected Tessellator tessellator = Tessellator.instance;

	@Override
	public boolean handleRenderType(ItemStack is, ItemRenderType type) 
	{
		switch (type) 
		{ 
			case INVENTORY: 
				return true; 
			default: 
				return false; 
		} 
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack is, ItemRendererHelper helper) 
	{	
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack is, Object... data) 
	{
		if (type != ItemRenderType.INVENTORY)
		{
			System.out.println("Item2DHeatRenderer.renderItem called with wrong render type: " + type.toString());
			return;
		}
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		GL11.glEnable(GL11.GL_BLEND);
		
		tessellator.startDrawingQuads();

		IIcon icon = is.getIconIndex();
		tessellator.addVertexWithUV( 0.0, 16.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV());
		tessellator.addVertexWithUV(16.0, 16.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV());
		tessellator.addVertexWithUV(16.0,  0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV());
		tessellator.addVertexWithUV( 0.0,  0.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV());
		
		tessellator.draw();		
		
		renderHeatBar(type, is);
		
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}

	protected void renderHeatBar(ItemRenderType type, ItemStack is)
	{
		if (type != ItemRenderType.INVENTORY) return;
		if (!ModOptions.enableTerraFirmaCraftAdditions) return;

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		ItemHeatDetails details = new ItemHeatDetails(is);
		if (details.hasTemp && details.range > 0)
		{
			RenderHelper.renderQuad(1, 1, 10, 1, 0);

			int tempValue = details.range > 0 ? 2 : 0;
			tempValue += (2 * details.subRange);
			
			if (tempValue < 0) tempValue = 0;
			if (tempValue > 10) tempValue = 10;
			RenderHelper.renderQuad(1, 1, tempValue, 1, details.color);

			if (details.isWorkable || details.isWeldable || details.isInDanger)
			{
				RenderHelper.renderQuad(12, 1, 3, 1, 0);

				if (details.isWorkable)
					RenderHelper.renderQuad(12, 1, 1, 1, 0x00ff00);
				if (details.isWeldable)
					RenderHelper.renderQuad(13, 1, 1, 1, 0xffaa00);
				if (details.isInDanger)
					RenderHelper.renderQuad(14, 1, 1, 1, 0xff0000);
			}
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
