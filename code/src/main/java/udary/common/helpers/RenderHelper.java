package udary.common.helpers;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.RenderBlocks;
import net.minecraft.client.renderer.Tessellator;

@SideOnly(Side.CLIENT)
public final class RenderHelper  
{
	protected static Tessellator tessellator = Tessellator.instance;
	
	/**
	 * Bottom face of a block (y negative).
	 */
    public static final int SIDE_BOTTOM = 0;
	/**
	 * Top face of a block (y positive).
	 */
    public static final int SIDE_TOP    = 1;
	/**
	 * North face of a block (z negative).
	 */
    public static final int SIDE_NORTH 	= 2;
	/**
	 * South face of a block (z positive).
	 */
    public static final int SIDE_SOUTH 	= 3;
	/**
	 * West face of a block (x negative).
	 */
    public static final int SIDE_WEST  	= 4;
	/**
	 * East face of a block (x positive).
	 */
    public static final int SIDE_EAST  	= 5;
	/**
	 * Same as bottom side.
	 */
    public static final int SIDE_DOWN	= SIDE_BOTTOM;
	/**
	 * Same as top side.
	 */
    public static final int SIDE_UP     = SIDE_TOP;
    
    /**
     * Gets the normal for the specified side (0-6); otherwise null if side not valid.
     */
    public static float[] getNormals(int side)
    {
    	switch (side)
    	{
    		case SIDE_BOTTOM:
    			return new float[] { 0.0F, -1.0F,  0.0F};
    		case SIDE_TOP:
    			return new float[] { 0.0F,  1.0F,  0.0F};
    		case SIDE_NORTH:
    			return new float[] { 0.0F,  0.0F, -1.0F};
    		case SIDE_SOUTH:
    			return new float[] { 0.0F,  0.0F,  1.0F};
    		case SIDE_WEST:
    			return new float[] {-1.0F,  0.0F,  0.0F};
    		case SIDE_EAST:
    			return new float[] { 1.0F,  0.0F,  0.0F};
    	}
    	return null;
    }

    /**
     * Sets the normal for the current draw call.
     */
    public static boolean setNormal(int side)
    {
    	float[] normals = getNormals(side);
    	if (normals == null || normals.length != 3) return false;
    	
    	tessellator.setNormal(normals[0], normals[1], normals[2]);
    	return true;
    }

    /**
     * Renders the block in the inventory, using default scale and translation.
     */
    public static void renderInventoryBlock(Block block, int meta, RenderBlocks renderBlocks)
    {
    	renderInventoryBlockWithBoundsTranslateScale(block, meta, renderBlocks, 0.0D, 0.0D, 0.0D, -0.5F, -0.5F, -0.5F, 1.0F, 1.0F, 1.0F);
    }
    
    /**
     * Renders the block in the inventory, using specified scale and default translation.
     */
    public static void renderInventoryBlockWithScale(Block block, int meta, RenderBlocks renderBlocks, float scaleX, float scaleY, float scaleZ)
    {
    	renderInventoryBlockWithBoundsTranslateScale(block, meta, renderBlocks, 0.0D, 0.0D, 0.0D, -0.5F, -0.5F, -0.5F, scaleX, scaleY, scaleZ);
    }

    /**
     * Renders the block in the inventory, using default scale and specified translation.
     */
    public static void renderInventoryBlockWithTranslate(Block block, int meta, RenderBlocks renderBlocks, float translateX, float translateY, float translateZ)
    {
    	renderInventoryBlockWithBoundsTranslateScale(block, meta, renderBlocks, 0.0D, 0.0D, 0.0D, translateX, translateY, translateZ, 1.0F, 1.0F, 1.0F);
    }

    /**
     * Renders the block in the inventory, using specified bounds, scale and default translation.
     */
    public static void renderInventoryBlockWithBoundsScale(Block block, int meta, RenderBlocks renderBlocks, double x, double y, double z, float scaleX, float scaleY, float scaleZ)
    {
    	renderInventoryBlockWithBoundsTranslateScale(block, meta, renderBlocks, x, y, z, -0.5F, -0.5F, -0.5F, scaleX, scaleY, scaleZ);
    }

    /**
     * Renders the block in the inventory, using specified scale and translation.
     */
    public static void renderInventoryBlockWithTranslateScale(Block block, int meta, RenderBlocks renderBlocks, float translateX, float translateY, float translateZ, float scaleX, float scaleY, float scaleZ)
	{
    	renderInventoryBlockWithBoundsTranslateScale(block, meta, renderBlocks, 0.0D, 0.0D, 0.0D, translateX, translateY, translateZ, scaleX, scaleY, scaleZ);
	}
    
    /**
     * Renders the block in the inventory, using specified bounds, scale and translation.
     */
    public static void renderInventoryBlockWithBoundsTranslateScale(Block block, int meta, RenderBlocks renderBlocks, double x, double y, double z, float translateX, float translateY, float translateZ, float scaleX, float scaleY, float scaleZ)
	{
    	renderInventoryBlockWithBoundsTranslateRotateScale(block, meta, renderBlocks, x, y, z, translateX, translateY, translateZ, 90.0F, 0.0F, 1.0F, 0.0F, scaleX, scaleY, scaleZ);
	}

    /**
     * Renders the block, using specified bounds, rotate, scale and translation.
     */
    public static void renderInventoryBlockWithBoundsTranslateRotateScale(Block block, int meta, RenderBlocks renderBlocks, double x, double y, double z, float translateX, float translateY, float translateZ, float rotateAngle, float rotateX, float rotateY, float rotateZ, float scaleX, float scaleY, float scaleZ)
	{
    	GL11.glPushMatrix();
        GL11.glRotatef(rotateAngle, rotateX, rotateY, rotateZ);
        GL11.glTranslatef(translateX, translateY, translateZ);
		
        GL11.glScalef(scaleX, scaleY, scaleZ);
        
		tessellator.startDrawingQuads();
		
        setNormal(SIDE_BOTTOM);
		renderBlocks.renderFaceYNeg(block, x, y, z, block.getIcon(SIDE_BOTTOM, meta));

        setNormal(SIDE_TOP);
		renderBlocks.renderFaceYPos(block, x, y, z, block.getIcon(SIDE_TOP, meta));

        setNormal(SIDE_NORTH);
		renderBlocks.renderFaceXNeg(block, x, y, z, block.getIcon(SIDE_NORTH, meta));

        setNormal(SIDE_SOUTH);
		renderBlocks.renderFaceXPos(block, x, y, z, block.getIcon(SIDE_SOUTH, meta));

        setNormal(SIDE_WEST);
		renderBlocks.renderFaceZNeg(block, x, y, z, block.getIcon(SIDE_WEST, meta));

        setNormal(SIDE_EAST);
		renderBlocks.renderFaceZPos(block, x, y, z, block.getIcon(SIDE_EAST, meta));
		
		tessellator.draw();
		     
        GL11.glPopMatrix();
	}

    public static void rotate(RenderBlocks renderBlocks, int i)
	{
    	renderBlocks.uvRotateEast = i;
		renderBlocks.uvRotateWest = i;
		renderBlocks.uvRotateNorth = i;
		renderBlocks.uvRotateSouth = i;
	}

	
    public static void renderQuad(double x, double y, double sizeX, double sizeY, int color)
	{
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(color);
		tessellator.addVertex((double)(x + 0), 	   (double)(y + 0),     0.0D);
		tessellator.addVertex((double)(x + 0),     (double)(y + sizeY), 0.0D);
		tessellator.addVertex((double)(x + sizeX), (double)(y + sizeY), 0.0D);
		tessellator.addVertex((double)(x + sizeX), (double)(y + 0),     0.0D);
		tessellator.draw();
	}

	public static void renderQuadWithNormal(double x, double y, double sizeX, double sizeY, int color, float xNormal, float yNormal, float zNormal)
	{
		tessellator.startDrawingQuads();
		tessellator.setColorOpaque_I(color);
		tessellator.setNormal(xNormal, yNormal, zNormal);
		tessellator.addVertex((double)(x + sizeX), (double)(y + 0),     1.0D);
		tessellator.addVertex((double)(x + sizeX), (double)(y + sizeY), 1.0D);
		tessellator.addVertex((double)(x + 0),     (double)(y + sizeY), 1.0D);
		tessellator.addVertex((double)(x + 0),     (double)(y + 0),     1.0D);
		tessellator.draw();
	}
}

