package udary.tfcplusudarymod.render.tesr.devices;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.containers.devices.ContainerAlloyCalculator;
import udary.tfcplusudarymod.render.blocks.devices.RenderAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;

import com.dunk.tfc.Render.TESR.TESRBase;

public class TesrAlloyCalculator extends TESRBase
{
	protected void drawItem(ItemStack itemStack, double xDis, double yDis, double zDis, int direction, EntityItem customItem, float blockScale, int slotIndex)
	{
		if (itemStack == null || customItem == null) return;
		
		float[] positions = getLocation(direction, slotIndex);
		
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)xDis+positions[0], (float)yDis+positions[1], (float)zDis+positions[2]);
			
			GL11.glRotatef(positions[3], positions[4], positions[5], positions[6]);
			
			GL11.glScalef(positions[7], positions[8], positions[9]);
			
			customItem.setEntityItemStack(itemStack);
			
			itemRenderer.doRender(customItem, 0, 0, 0, 0, 0);
		}
		GL11.glPopMatrix();
	}

	protected float[] getLocation(int direction, int slotIndex)
	{
		float[] out = new float[10];
		float angle = (float) (360.0 * (System.currentTimeMillis() & 0x3FFFL) / 0x3FFFL);
		float cX;
		float cZ;
		
		switch (slotIndex)
		{
			case ContainerAlloyCalculator.SLOT_INPUT_1:
				// position offset
				cX = ((RenderAlloyCalculator.maxX-RenderAlloyCalculator.sW-RenderAlloyCalculator.iP)-(RenderAlloyCalculator.maxX-RenderAlloyCalculator.cX+(RenderAlloyCalculator.iP/2))) / 2;
				cZ = ((RenderAlloyCalculator.maxZ-RenderAlloyCalculator.sW-RenderAlloyCalculator.iP)-(RenderAlloyCalculator.maxZ-RenderAlloyCalculator.cZ+(RenderAlloyCalculator.iP/2))) / 2;
				
				out[0] = (RenderAlloyCalculator.maxX-RenderAlloyCalculator.cX+(RenderAlloyCalculator.iP/2))+cX;
				out[1] = (RenderAlloyCalculator.minY+RenderAlloyCalculator.bH+RenderAlloyCalculator.iH);
				out[2] = (RenderAlloyCalculator.maxZ-RenderAlloyCalculator.cZ+(RenderAlloyCalculator.iP/2))+cZ;
				
				// rotation
				if (RenderManager.instance.options.fancyGraphics)
				{
					out[3] = angle;	// angle
					out[4] = 0f;
					out[5] = 1f;
					out[6] = 0f;				
				}
				else
				{
					out[3] = 0f;	// angle
					out[4] = 0f;
					out[5] = 0f;
					out[6] = 0f;						
				}
				
				// scale
				out[7] = 0.50f;
				out[8] = 0.50f;
				out[9] = 0.50f;
				break;
				
			case ContainerAlloyCalculator.SLOT_INPUT_2:
				// position offset
				cX = ((RenderAlloyCalculator.minX+RenderAlloyCalculator.cX-(RenderAlloyCalculator.iP/2))-(RenderAlloyCalculator.minX+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)) / 2;
				cZ = ((RenderAlloyCalculator.maxZ-RenderAlloyCalculator.sW-RenderAlloyCalculator.iP)-(RenderAlloyCalculator.maxZ-RenderAlloyCalculator.cZ+(RenderAlloyCalculator.iP/2))) / 2;
				
				out[0] = (RenderAlloyCalculator.minX+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)+cX;
				out[1] = (RenderAlloyCalculator.minY+RenderAlloyCalculator.bH+RenderAlloyCalculator.iH);
				out[2] = (RenderAlloyCalculator.maxZ-RenderAlloyCalculator.cZ+(RenderAlloyCalculator.iP/2))+cZ;
				
				// rotation
				if (RenderManager.instance.options.fancyGraphics)
				{
					out[3] = angle;	// angle
					out[4] = 0f;
					out[5] = 1f;
					out[6] = 0f;				
				}
				else
				{
					out[3] = 0f;	// angle
					out[4] = 0f;
					out[5] = 0f;
					out[6] = 0f;					
				}
				
				// scale
				out[7] = 0.50f;
				out[8] = 0.50f;
				out[9] = 0.50f;
				break;
				
			case ContainerAlloyCalculator.SLOT_INPUT_3:
				// position offset
				cX = ((RenderAlloyCalculator.maxX-RenderAlloyCalculator.sW-RenderAlloyCalculator.iP)-(RenderAlloyCalculator.maxX-RenderAlloyCalculator.cX+(RenderAlloyCalculator.iP/2))) / 2;
				cZ = ((RenderAlloyCalculator.minZ+RenderAlloyCalculator.cZ-(RenderAlloyCalculator.iP/2))-(RenderAlloyCalculator.minZ+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)) / 2;
				
				out[0] = (RenderAlloyCalculator.maxX-RenderAlloyCalculator.cX+(RenderAlloyCalculator.iP/2))+cX;
				out[1] = (RenderAlloyCalculator.minY+RenderAlloyCalculator.bH+RenderAlloyCalculator.iH);
				out[2] = (RenderAlloyCalculator.minZ+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)+cZ;
				
				// rotation
				if (RenderManager.instance.options.fancyGraphics)
				{
					out[3] = angle;	// angle
					out[4] = 0f;
					out[5] = 1f;
					out[6] = 0f;				
				}
				else
				{
					out[3] = 0f;	// angle
					out[4] = 0f;
					out[5] = 0f;
					out[6] = 0f;						
				}
				
				// scale
				out[7] = 0.50f;
				out[8] = 0.50f;
				out[9] = 0.50f;
				break;
				
			case ContainerAlloyCalculator.SLOT_INPUT_4:
				// position offset
				cX = ((RenderAlloyCalculator.minX+RenderAlloyCalculator.cX-(RenderAlloyCalculator.iP/2))-(RenderAlloyCalculator.minX+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)) / 2;
				cZ = ((RenderAlloyCalculator.minZ+RenderAlloyCalculator.cZ-(RenderAlloyCalculator.iP/2))-(RenderAlloyCalculator.minZ+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)) / 2;
				
				out[0] = (RenderAlloyCalculator.minX+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)+cX;
				out[1] = (RenderAlloyCalculator.minY+RenderAlloyCalculator.bH+RenderAlloyCalculator.iH);
				out[2] = (RenderAlloyCalculator.minZ+RenderAlloyCalculator.sW+RenderAlloyCalculator.iP)+cZ;
				
				// rotation
				if (RenderManager.instance.options.fancyGraphics)
				{
					out[3] = angle;	// angle
					out[4] = 0f;
					out[5] = 1f;
					out[6] = 0f;				
				}
				else
				{
					out[3] = 0f;	// angle
					out[4] = 0f;
					out[5] = 0f;
					out[6] = 0f;					
				}
				
				// scale
				out[7] = 0.50f;
				out[8] = 0.50f;
				out[9] = 0.50f;
				break;
		}
		
		return out;
	}	
	
	protected void renderAt(TileEntityAlloyCalculator tileEntity, double xDis, double yDis, double zDis)
	{
		if (tileEntity == null || tileEntity.getWorldObj() == null) return;
		
		int direction = 0;

		EntityItem customItem = new EntityItem(field_147501_a.field_147550_f);
		customItem.hoverStart = 0f;
		float blockScale = 1.0F;

		drawItem(tileEntity.getStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_1), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAlloyCalculator.SLOT_INPUT_1);
		drawItem(tileEntity.getStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_2), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAlloyCalculator.SLOT_INPUT_2);
		drawItem(tileEntity.getStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_3), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAlloyCalculator.SLOT_INPUT_3);
		drawItem(tileEntity.getStackInSlot(ContainerAlloyCalculator.SLOT_INPUT_4), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAlloyCalculator.SLOT_INPUT_4);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double xDis, double yDis, double zDis, float f)
	{
		renderAt((TileEntityAlloyCalculator)tileEntity, xDis, yDis, zDis);
	}
}
