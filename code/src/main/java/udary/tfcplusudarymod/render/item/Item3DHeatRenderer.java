package udary.tfcplusudarymod.render.item;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;
import udary.common.ItemHeatDetails;
import udary.common.helpers.RenderHelper;
import udary.tfcplusudarymod.core.ModOptions;

public class Item3DHeatRenderer implements IItemRenderer
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
		switch (type) 
		{ 
			case INVENTORY: 
				return (helper == ItemRendererHelper.INVENTORY_BLOCK);
			default: 
				return false; 
		} 
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack is, Object... data) 
	{
		if (type != ItemRenderType.INVENTORY)
		{
			System.out.println("Item3DHeatRenderer.renderItem called with wrong render type: " + type.toString());
			return;
		}

		tessellator.startDrawingQuads();
		
		GL11.glTranslatef(-0.5F, -0.5F, -0.5F);
		
		IIcon icon;
		
		// Renders the given texture to the east (x-positive) face of the block.
		icon = is.getItem().getIconFromDamage(5);
		tessellator.setNormal(1.0F, 0.0F, 0.0F);
	    tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
	    tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
	    tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV()); 
	    tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV()); 
		
	    // Renders the given texture to the west (x-negative) face of the block.
		icon = is.getItem().getIconFromDamage(4);
		tessellator.setNormal(-1.0F, 0.0F, 0.0F);
		tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV()); 
		
		// Renders the given texture to the south (z-positive) face of the block.
		icon = is.getItem().getIconFromDamage(3); 
		tessellator.setNormal(0.0F, 0.0F, 1.0F); 
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMaxV()); 

		// Renders the given texture to the north (z-negative) face of the block.
		icon = is.getItem().getIconFromDamage(2); 
		tessellator.setNormal(0.0F, 0.0F, -1.0F); 
		tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
		tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV()); 
		
		// Renders the given texture to the top face of the block.
		icon = is.getItem().getIconFromDamage(1); 
		tessellator.setNormal(0.0F, 1.0F, 0.0F); 
		tessellator.addVertexWithUV(1.0, 1.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
		tessellator.addVertexWithUV(1.0, 1.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(0.0, 1.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV()); 
		
		// Renders the given texture to the bottom face of the block.
		icon = is.getItem().getIconFromDamage(0); 
		tessellator.setNormal(0.0F, -1.0F, 0.0F); 
		tessellator.addVertexWithUV(0.0, 0.0, 1.0, (double)icon.getMaxU(), (double)icon.getMaxV()); 
		tessellator.addVertexWithUV(0.0, 0.0, 0.0, (double)icon.getMaxU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(1.0, 0.0, 0.0, (double)icon.getMinU(), (double)icon.getMinV()); 
		tessellator.addVertexWithUV(1.0, 0.0, 1.0, (double)icon.getMinU(), (double)icon.getMaxV()); 
		 
		tessellator.draw(); 
				
		GL11.glRotatef(45.0F, 0.0F, 1.0F, 0.0F);
		GL11.glTranslatef(-0.7F, 1.55F, 0.5F);
		
		renderHeatBar(type, is);
	}

	protected void renderHeatBar(ItemRenderType type, ItemStack is)
	{
		if (type != ItemRenderType.INVENTORY) return;
		if (!ModOptions.enableTerraFirmaCraftAdditions) return;

		GL11.glDisable(GL11.GL_TEXTURE_2D);

		ItemHeatDetails details = new ItemHeatDetails(is);
		if (details.hasTemp && details.range > 0)
		{
			RenderHelper.renderQuadWithNormal(0.0, 0.10, 1.0, 0.12, 0x000000, 0.0F, 0.0F, -1.0F);

			int tempValue = details.range > 0 ? 2 : 0;
			tempValue += (2 * details.subRange);
			
			if (tempValue < 0) tempValue = 0;
			if (tempValue > 10) tempValue = 10;
			RenderHelper.renderQuadWithNormal(0.0, 0.10, tempValue * 0.1, 0.12, details.color, 0.0F, 0.0F, -1.0F);

			if (details.isWorkable || details.isWeldable || details.isInDanger)
			{
				RenderHelper.renderQuadWithNormal(1.1, 0.10, 0.3, 0.12, 0x000000, 0.0F, 0.0F, -1.0F);

				if (details.isWorkable)
					RenderHelper.renderQuadWithNormal(1.1, 0.10, 0.1, 0.12, 0x00ff00, 0.0F, 0.0F, -1.0F);
				if (details.isWeldable)
					RenderHelper.renderQuadWithNormal(1.2, 0.10, 0.1, 0.12, 0xffaa00, 0.0F, 0.0F, -1.0F);
				if (details.isInDanger)
					RenderHelper.renderQuadWithNormal(1.3, 0.10, 0.1, 0.12, 0xff0000, 0.0F, 0.0F, -1.0F);					
			}
		}

		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
