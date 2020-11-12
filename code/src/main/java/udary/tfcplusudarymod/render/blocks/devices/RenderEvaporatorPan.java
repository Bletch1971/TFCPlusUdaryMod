package udary.tfcplusudarymod.render.blocks.devices;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraftforge.fluids.FluidStack;
import udary.common.helpers.RenderHelper;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderEvaporatorPan implements ISimpleBlockRenderingHandler
{
	public static float minX = 0.1F;
	public static float maxX = 0.9F;
	public static float minY = 0.0F;
	public static float maxY = 0.2F;
	public static float minZ = 0.1F;
	public static float maxZ = 0.9F;
	
	public static float bH = 0.1F;			// bottom height
	public static float cX = (maxX-minX)/2;	// center X
	public static float cZ = (maxZ-minZ)/2;	// center Z
	public static float sW = 0.03F;			// side width
	
	public static float iP = 0.1F;			// item padding
	
	public static int iC = 4;				// indent count
	public static float bI = 0.02F;			// bottom indent 
	public static float sI = 0.02F;			// side indent 
	
	@Override
	public int getRenderId() 
	{
		return 0;
	}

	@Override
	public void renderInventoryBlock(Block block, int meta, int modelId, RenderBlocks renderer) 
	{
		if (block == null || renderer == null) 
			return;
		
		// bottom
		float bottomHeight = bH / iC;
		float bottomIndent = 0f;
		float sideIndent = sI * iC;

		for (int i = 0; i <= iC; i++)
		{
			renderer.setRenderBounds(minX+sW+sideIndent, minY+bottomIndent, minZ+sW+sideIndent, maxX-sW-sideIndent, minY+bottomIndent+bottomHeight, maxZ-sW-sideIndent);
			RenderHelper.rotate(renderer, 1);
			RenderHelper.renderInventoryBlock(block, meta, renderer);
			
			bottomIndent += bI;
			sideIndent -= sI;
		}

		// sides
		renderer.setRenderBounds(minX, minY+bottomIndent, minZ, minX+sW, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(maxX-sW, minY+bottomIndent, minZ, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(minX, minY+bottomIndent, minZ, maxX, maxY, minZ+sW);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(minX, minY+bottomIndent, maxZ-sW, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
        if (world == null || block == null || renderer == null) 
        	return false;
        
        renderer.renderAllFaces = true;

		// bottom
		float bottomHeight = bH / iC;
		float bottomIndent = 0f;
		float sideIndent = sI * iC;

		for (int i = 0; i <= iC; i++)
		{
			renderer.setRenderBounds(minX+sW+sideIndent, minY+bottomIndent, minZ+sW+sideIndent, maxX-sW-sideIndent, minY+bottomIndent+bottomHeight, maxZ-sW-sideIndent);
			renderer.renderStandardBlock(block, x, y, z);
			
			bottomIndent += bI;
			sideIndent -= sI;
		}

		// sides
		renderer.setRenderBounds(minX, minY+bottomIndent, minZ, minX+sW, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(block, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(maxX-sW, minY+bottomIndent, minZ, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(block, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(minX, minY+bottomIndent, minZ, maxX, maxY, minZ+sW);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(block, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(minX, minY+bottomIndent, maxZ-sW, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(block, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		// fluid
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		
		if (tileEntity != null && tileEntity instanceof TileEntityEvaporatorPan && ((TileEntityEvaporatorPan)tileEntity).inputFS != null && renderer.overrideBlockTexture == null)
		{
			FluidStack fs = ((TileEntityEvaporatorPan)tileEntity).inputFS;
			
			IIcon stillIcon = fs.getFluid().getStillIcon();
			int color = fs.getFluid().getColor(fs);
			float f1 = (color >> 16 & 255) / 255.0F;
			float f2 = (color >>  8 & 255) / 255.0F;
			float f3 = (color       & 255) / 255.0F;
			float fluidLevel = Math.max(0.1f * ((float)fs.amount / (float)((TileEntityEvaporatorPan)tileEntity).getMaxFluid()), 0.01f);
			
			renderer.setRenderBounds(minX+sW, minY+bH, minZ+sW, maxX-sW, minY+bH+fluidLevel, maxZ-sW);
			renderer.setOverrideBlockTexture(stillIcon);
			renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, f1, f2, f3);
			renderer.clearOverrideBlockTexture();
		}

		renderer.renderAllFaces = false;
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) 
	{
		return true;
	}
}
