package udary.tfcplusudarymod.render.tesr.devices;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.containers.devices.ContainerEvaporatorPan;
import udary.tfcplusudarymod.render.blocks.devices.RenderEvaporatorPan;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;

import com.dunk.tfc.Render.TESR.TESRBase;

public class TesrEvaporatorPan extends TESRBase
{
	protected void drawItem(ItemStack itemStack, double xDis, double yDis, double zDis, int direction, EntityItem customItem, float blockScale, int slotIndex)
	{
		if (itemStack == null || customItem == null) return;
		
		float[] positions = getLocation(direction, slotIndex);
		
		GL11.glPushMatrix();
		{
			GL11.glTranslatef((float)xDis + positions[0], (float)yDis + positions[1], (float)zDis + positions[2]);
			
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
			case ContainerEvaporatorPan.SLOT_OUTPUT_1:
				// position offset
				cX = ((RenderEvaporatorPan.maxX-RenderEvaporatorPan.sW) - (RenderEvaporatorPan.maxX-RenderEvaporatorPan.cX)) / 2;
				cZ = ((RenderEvaporatorPan.maxZ-RenderEvaporatorPan.sW) - (RenderEvaporatorPan.maxZ-RenderEvaporatorPan.cZ)) / 2;
				
				out[0] = (RenderEvaporatorPan.maxX-RenderEvaporatorPan.cX) + cX;
				out[1] = (RenderEvaporatorPan.minY+RenderEvaporatorPan.bH+RenderEvaporatorPan.iP);
				out[2] = (RenderEvaporatorPan.maxZ-RenderEvaporatorPan.cZ) + cZ;
				
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
				
			case ContainerEvaporatorPan.SLOT_OUTPUT_2:
				// position offset
				cX = ((RenderEvaporatorPan.minX+RenderEvaporatorPan.cX) - (RenderEvaporatorPan.minX+RenderEvaporatorPan.sW)) / 2;
				cZ = ((RenderEvaporatorPan.maxZ-RenderEvaporatorPan.sW) - (RenderEvaporatorPan.maxZ-RenderEvaporatorPan.cZ)) / 2;
				
				out[0] = (RenderEvaporatorPan.minX+RenderEvaporatorPan.sW) + cX;
				out[1] = (RenderEvaporatorPan.minY+RenderEvaporatorPan.bH+RenderEvaporatorPan.iP);
				out[2] = (RenderEvaporatorPan.maxZ-RenderEvaporatorPan.cZ) + cZ;
				
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
				
			case ContainerEvaporatorPan.SLOT_OUTPUT_3:
				// position offset
				cX = ((RenderEvaporatorPan.maxX-RenderEvaporatorPan.sW) - (RenderEvaporatorPan.maxX-RenderEvaporatorPan.cX)) / 2;
				cZ = ((RenderEvaporatorPan.minZ+RenderEvaporatorPan.cZ) - (RenderEvaporatorPan.minZ+RenderEvaporatorPan.sW)) / 2;
				
				out[0] = (RenderEvaporatorPan.maxX-RenderEvaporatorPan.cX) + cX;
				out[1] = (RenderEvaporatorPan.minY+RenderEvaporatorPan.bH+RenderEvaporatorPan.iP);
				out[2] = (RenderEvaporatorPan.minZ+RenderEvaporatorPan.sW) + cZ;
				
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
				
			case ContainerEvaporatorPan.SLOT_OUTPUT_4:
				// position offset
				cX = ((RenderEvaporatorPan.minX+RenderEvaporatorPan.cX) - (RenderEvaporatorPan.minX+RenderEvaporatorPan.sW)) / 2;
				cZ = ((RenderEvaporatorPan.minZ+RenderEvaporatorPan.cZ) - (RenderEvaporatorPan.minZ+RenderEvaporatorPan.sW)) / 2;
				
				out[0] = (RenderEvaporatorPan.minX+RenderEvaporatorPan.sW) + cX;
				out[1] = (RenderEvaporatorPan.minY+RenderEvaporatorPan.bH+RenderEvaporatorPan.iP);
				out[2] = (RenderEvaporatorPan.minZ+RenderEvaporatorPan.sW) + cZ;
				
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
	
	protected void renderAt(TileEntityEvaporatorPan tileEntity, double xDis, double yDis, double zDis)
	{
		if (tileEntity == null || tileEntity.getWorldObj() == null) return;
		
		int direction = 0;

		EntityItem customItem = new EntityItem(field_147501_a.field_147550_f);
		customItem.hoverStart = 0f;
		float blockScale = 1.0F;

		drawItem(tileEntity.getStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_1), xDis, yDis, zDis, direction, customItem, blockScale, ContainerEvaporatorPan.SLOT_OUTPUT_1);
		drawItem(tileEntity.getStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_2), xDis, yDis, zDis, direction, customItem, blockScale, ContainerEvaporatorPan.SLOT_OUTPUT_2);
		drawItem(tileEntity.getStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_3), xDis, yDis, zDis, direction, customItem, blockScale, ContainerEvaporatorPan.SLOT_OUTPUT_3);
		drawItem(tileEntity.getStackInSlot(ContainerEvaporatorPan.SLOT_OUTPUT_4), xDis, yDis, zDis, direction, customItem, blockScale, ContainerEvaporatorPan.SLOT_OUTPUT_4);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double xDis, double yDis, double zDis, float f)
	{
		renderAt((TileEntityEvaporatorPan)tileEntity, xDis, yDis, zDis);
	}
}
