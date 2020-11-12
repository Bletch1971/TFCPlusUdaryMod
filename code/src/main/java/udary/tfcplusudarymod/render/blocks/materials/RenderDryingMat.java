package udary.tfcplusudarymod.render.blocks.materials;

import udary.common.helpers.RenderHelper;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.world.IBlockAccess;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderDryingMat implements ISimpleBlockRenderingHandler
{
	public static float minX = 0.05F;
	public static float maxX = 0.95F;
	public static float minY = 0.00F;
	public static float maxY = 0.05F;
	public static float minZ = 0.05F;
	public static float maxZ = 0.95F;
	
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
		
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
		RenderHelper.rotate(renderer, 1);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
	}
	
	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer) 
	{
        if (world == null || block == null || renderer == null) 
        	return false;
        
        renderer.renderAllFaces = true;

		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY, maxZ);
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
