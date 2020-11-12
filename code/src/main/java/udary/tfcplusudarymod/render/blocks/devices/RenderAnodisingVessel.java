package udary.tfcplusudarymod.render.blocks.devices;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import udary.common.helpers.RenderHelper;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;
import cpw.mods.fml.client.registry.ISimpleBlockRenderingHandler;

public class RenderAnodisingVessel implements ISimpleBlockRenderingHandler 
{
	public static float minX = 0.2F;
	public static float maxX = 0.8F;
	public static float minY = 0.0F;
	public static float maxY = 0.7F;
	public static float minZ = 0.2F;
	public static float maxZ = 0.8F;
	
	public static float bH = 0.05F;			// bottom height
	public static float kP = 0.2375F;		// knob padding
	public static float lH = 0.05F;			// lid height
	public static float lP = 0.025F;		// lid padding
	public static float lW = 0.1F;			// lid width
	public static float sH = 0.6F;			// side height
	public static float sP = 0.05F;			// side padding
	
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

		// lid
		renderer.setRenderBounds(minX+kP, maxY-lH, minZ+kP, maxX-kP, maxY, maxZ-kP);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
		
		renderer.setRenderBounds(minX-lP, maxY-lH-lW, minZ-lP, maxX+lP, maxY-lH, maxZ+lP);
		RenderHelper.renderInventoryBlock(block, meta, renderer);

		// vessel
		renderer.setRenderBounds(minX, minY, minZ, maxX, maxY-lW, maxZ);
		RenderHelper.renderInventoryBlock(block, meta, renderer);
	}

	@Override
	public boolean renderWorldBlock(IBlockAccess world, int x, int y, int z, Block block, int modelId, RenderBlocks renderer)
	{
        if (world == null || block == null || renderer == null) 
        	return false;

		TileEntityAnodisingVessel tileEntity = (TileEntityAnodisingVessel)world.getTileEntity(x, y, z);
		if (tileEntity == null) 
			return false;
		
		renderer.renderAllFaces = true;

		if ((tileEntity.rotation & -128) == 0)
		{
			if (tileEntity.getSealed())
			{
				// lid
				renderer.setRenderBounds(minX+kP, maxY-lH, minZ+kP, maxX-kP, maxY, maxZ-kP);
				renderer.renderStandardBlock(block, x, y, z);
				
				renderer.setRenderBounds(minX-lP, maxY-lH-lW, minZ-lP, maxX+lP, maxY-lH, maxZ+lP);
				renderer.renderStandardBlock(block, x, y, z);
				
				// vessel
				renderer.setRenderBounds(minX+sP, minY, minZ+sP, maxX-sP, minY+bH, maxZ-sP);
				renderer.renderStandardBlock(block, x, y, z);
			}
			else
			{
				// bottom
				renderer.setRenderBounds(minX+sP, minY, minZ+sP, maxX-sP, minY+bH, maxZ-sP);
				renderer.renderStandardBlock(block, x, y, z);

				// fluid
				if (tileEntity.fluid != null && renderer.overrideBlockTexture == null)
				{
					IIcon stillIcon = tileEntity.fluid.getFluid().getStillIcon();
					int color = tileEntity.fluid.getFluid().getColor(tileEntity.fluid);
					float f1 = (color >> 16 & 255) / 255.0F;
					float f2 = (color >>  8 & 255) / 255.0F;
					float f3 = (color       & 255) / 255.0F;
					float fluidLevel = Math.max(0.5f * ((float)tileEntity.fluid.amount / (float)tileEntity.getMaxLiquid()), 0.01f);
					
					renderer.setRenderBounds(minX+sP, minY+bH, minZ+sP, maxX-sP, minY+bH+fluidLevel, maxZ-sP);
					renderer.setOverrideBlockTexture(stillIcon);
					renderer.renderStandardBlockWithColorMultiplier(block, x, y, z, f1, f2, f3);
					renderer.clearOverrideBlockTexture();
				}
			}
			
			// sides
			renderer.setRenderBounds(minX, minY, minZ+sP, minX+sP, minY+sH, maxZ-sP);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(maxX-sP, minY, minZ+sP, maxX, minY+sH, maxZ-sP);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(minX, minY, minZ, maxX, minY+sH, minZ+sP);
			renderer.renderStandardBlock(block, x, y, z);

			renderer.setRenderBounds(minX, minY, maxZ-sP, maxX, minY+sH, maxZ);
			renderer.renderStandardBlock(block, x, y, z);
		}
		else
		{
			if ((tileEntity.rotation & 3) == 0)
			{
				renderer.setRenderBounds(minX, minY, minZ+sP, maxX-sP, minY+bH, maxZ-sP);
				renderer.renderStandardBlock(block, x, y, z);
			}
			if ((tileEntity.rotation & 3) == 1)
			{
				renderer.setRenderBounds(minX+sP, minY, minZ, maxX-sP, minY+bH, maxZ-sP);
				renderer.renderStandardBlock(block, x, y, z);
			}
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
