package udary.tfcplusudarymod.render.blocks.devices;

import udary.common.helpers.RenderHelper;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;

public class RenderOreCooker implements ISimpleBlockRenderingHandler
{
	public static float minX = 0.0625F;
	public static float maxX = 0.9375F;
	public static float minY = 0.0000F;
	public static float maxY = 0.8125F;
	public static float minZ = 0.0625F;
	public static float maxZ = 0.9375F;

	public static float minY2 = 0.25F;
	
	public static float bH = 2f/16f;		// bottom height
	public static float sP = 2f/16f;		// side padding

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
		renderer.setRenderBounds(minX, minY, minZ, maxX, minY+bH, maxZ);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		// sides
		renderer.setRenderBounds(minX, minY, minZ+sP, minX+sP, maxY, maxZ-sP);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		renderer.setRenderBounds(maxX-sP, minY, minZ+sP, maxX, maxY, maxZ-sP);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, minZ+sP);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		renderer.setRenderBounds(minX, minY, maxZ-sP, maxX, maxY, maxZ);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
        if (world == null || block == null || renderer == null) 
        	return false;

		renderer.renderAllFaces = true;

        // bottom
		renderer.setRenderBounds(minX, minY, minX, maxX, minY+bH, maxZ);
		renderer.renderStandardBlock(block, x, y, z);
		
		// sides
		renderer.setRenderBounds(minX, minY, minZ+sP, minX+sP, maxY, maxZ-sP);
		renderer.renderStandardBlock(block, x, y, z);
		
		renderer.setRenderBounds(maxX-sP, minY, minZ+sP, maxX, maxY, maxZ-sP);
		renderer.renderStandardBlock(block, x, y, z);
		
		renderer.setRenderBounds(minX, minY, minX, maxX, maxY, minZ+sP);
		renderer.renderStandardBlock(block, x, y, z);
		
		renderer.setRenderBounds(minX, minY, maxX-sP, maxX, maxY, maxZ);
		renderer.renderStandardBlock(block, x, y, z);

		renderer.renderAllFaces = false;

		return true;
	}

	@Override
	public boolean shouldRender3DInInventory(int modelId)
	{
		return true;
	}
}
