package udary.tfcplusudarymod.render.blocks.devices;

import udary.common.helpers.RenderHelper;

import com.dunk.tfc.api.TFCBlocks;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.init.Blocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAlloyCalculator implements ISimpleBlockRenderingHandler
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
	public static float sW = 0.05F;			// side width

	public static float iP = 0.05F;			// insert padding
	public static float iH = 0.02F;			// insert height

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
		
		// wooden bottom
		renderer.setRenderBounds(minX+sW, minY, minZ+sW, maxX-sW, minY+bH, maxZ-sW);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(TFCBlocks.woodSupportV, meta, renderer);

		// wooden sides
		renderer.setRenderBounds(minX, minY, minZ, minX+sW, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(TFCBlocks.woodSupportV, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(maxX-sW, minY, minZ, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(TFCBlocks.woodSupportV, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, minZ+sW);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(TFCBlocks.woodSupportV, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		renderer.setRenderBounds(minX, minY, maxZ-sW, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(TFCBlocks.woodSupportV, meta, renderer);
		RenderHelper.rotate(renderer, 0);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		// redstone inserts
		renderer.setRenderBounds(minX+sW+iP, minY+bH, minZ+sW+iP, minX+cX-(iP/2), minY+bH+iH, minZ+cZ-(iP/2));
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(Blocks.redstone_block, meta, renderer);
		
		renderer.setRenderBounds(minX+sW+iP, minY+bH, maxZ-cZ+(iP/2), minX+cX-(iP/2), minY+bH+iH, maxZ-sW-iP);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(Blocks.redstone_block, meta, renderer);

		renderer.setRenderBounds(maxX-cX+(iP/2), minY+bH, minZ+sW+iP, maxX-sW-iP, minY+bH+iH, minZ+cZ-(iP/2));
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(Blocks.redstone_block, meta, renderer);

		renderer.setRenderBounds(maxX-cX+(iP/2), minY+bH, maxZ-cZ+(iP/2), maxX-sW-iP, minY+bH+iH, maxZ-sW-iP);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(Blocks.redstone_block, meta, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
        if (world == null || block == null || renderer == null) 
        	return false;
        
        renderer.renderAllFaces = true;
		
		// wooden bottom
		renderer.setRenderBounds(minX+sW, minY, minZ+sW, maxX-sW, minY+bH, maxZ-sW);
		renderer.renderStandardBlock(TFCBlocks.woodSupportV, x, y, z);

		// wooden sides
		renderer.setRenderBounds(minX, minY, minZ, minX+sW, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(TFCBlocks.woodSupportV, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(maxX-sW, minY, minZ, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(TFCBlocks.woodSupportV, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, minZ+sW);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(TFCBlocks.woodSupportV, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.setRenderBounds(minX, minY, maxZ-sW, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		renderer.renderStandardBlock(TFCBlocks.woodSupportV, x, y, z);
		RenderHelper.rotate(renderer, 0);
		renderer.renderStandardBlock(block, x, y, z);
		
		// redstone inserts
		renderer.setRenderBounds(minX+sW+iP, minY+bH, minZ+sW+iP, minX+cX-(iP/2), minY+bH+iH, minZ+cZ-(iP/2));
		renderer.renderStandardBlock(Blocks.redstone_block, x, y, z);
		
		renderer.setRenderBounds(minX+sW+iP, minY+bH, maxZ-cZ+(iP/2), minX+cX-(iP/2), minY+bH+iH, maxZ-sW-iP);
		renderer.renderStandardBlock(Blocks.redstone_block, x, y, z);

		renderer.setRenderBounds(maxX-cX+(iP/2), minY+bH, minZ+sW+iP, maxX-sW-iP, minY+bH+iH, minZ+cZ-(iP/2));
		renderer.renderStandardBlock(Blocks.redstone_block, x, y, z);

		renderer.setRenderBounds(maxX-cX+(iP/2), minY+bH, maxZ-cZ+(iP/2), maxX-sW-iP, minY+bH+iH, maxZ-sW-iP);
		renderer.renderStandardBlock(Blocks.redstone_block, x, y, z);

		renderer.renderAllFaces = false;
		
		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId) 
	{
		return true;
	}
}
