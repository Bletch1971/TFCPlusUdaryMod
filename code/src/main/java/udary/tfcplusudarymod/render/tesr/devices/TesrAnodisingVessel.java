package udary.tfcplusudarymod.render.tesr.devices;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.containers.devices.ContainerAnodisingVessel;
import udary.tfcplusudarymod.render.blocks.devices.RenderAnodisingVessel;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;

import com.dunk.tfc.Render.TESR.TESRBase;

public class TesrAnodisingVessel extends TESRBase
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
			case ContainerAnodisingVessel.SLOT_BATTERY:
				// position offset
				cX = (RenderAnodisingVessel.maxX-RenderAnodisingVessel.minX) / 2;
				
				out[0] = RenderAnodisingVessel.minX+cX;
				out[1] = RenderAnodisingVessel.minY;
				out[2] = RenderAnodisingVessel.maxZ;
				
				// rotation
				out[3] = 0f;	// angle
				out[4] = 0f;
				out[5] = 0f;
				out[6] = 0f;
				
				// scale
				out[7] = 1.00f;
				out[8] = 1.00f;
				out[9] = 1.00f;
				break;
				
			case ContainerAnodisingVessel.SLOT_CATHODE:
				// position offset
				out[0] = RenderAnodisingVessel.maxX-0.10f;
				out[1] = RenderAnodisingVessel.minY+0.22f;
				out[2] = RenderAnodisingVessel.maxZ-0.05f;
				
				// rotation
				out[3] = 45f;	// angle
				out[4] = 0f;
				out[5] = 0f;
				out[6] = 1f;
				
				// scale
				out[7] = 0.80f;
				out[8] = 0.80f;
				out[9] = 0.80f;
				break;
				
			case ContainerAnodisingVessel.SLOT_ANODE:
				// position offset
				out[0] = RenderAnodisingVessel.minX+0.10f;
				out[1] = RenderAnodisingVessel.minY+0.22f;
				out[2] = RenderAnodisingVessel.maxZ-0.05f;
				
				// rotation
				out[3] = 315f;	// angle
				out[4] = 0f;
				out[5] = 0f;
				out[6] = 1f;
				
				// scale
				out[7] = 0.80f;
				out[8] = 0.80f;
				out[9] = 0.80f;
				break;
				
			case ContainerAnodisingVessel.SLOT_SOLUTE:
				// position offset
				cX = (RenderAnodisingVessel.maxX-RenderAnodisingVessel.minX) / 2;
				cZ = (RenderAnodisingVessel.maxZ-RenderAnodisingVessel.minZ) / 2;

				out[0] = RenderAnodisingVessel.minX+cX;
				out[1] = RenderAnodisingVessel.minY+(RenderAnodisingVessel.bH*2);
				out[2] = RenderAnodisingVessel.minZ+cZ;
				
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
				out[7] = 1.00f;
				out[8] = 1.00f;
				out[9] = 1.00f;
				break;
		}

		return out;
	}	
	
	protected void renderAt(TileEntityAnodisingVessel tileEntity, double xDis, double yDis, double zDis)
	{
		if (tileEntity == null || tileEntity.getWorldObj() == null) return;
		
		int direction = 0;

		EntityItem customItem = new EntityItem(field_147501_a.field_147550_f);
		customItem.hoverStart = 0f;
		float blockScale = 1.0F;

		if (RenderManager.instance.options.fancyGraphics)
		{
			drawItem(tileEntity.getStackInSlot(ContainerAnodisingVessel.SLOT_BATTERY), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAnodisingVessel.SLOT_BATTERY);
			drawItem(tileEntity.getStackInSlot(ContainerAnodisingVessel.SLOT_CATHODE), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAnodisingVessel.SLOT_CATHODE);
			drawItem(tileEntity.getStackInSlot(ContainerAnodisingVessel.SLOT_ANODE), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAnodisingVessel.SLOT_ANODE);
		}
		drawItem(tileEntity.getStackInSlot(ContainerAnodisingVessel.SLOT_SOLUTE), xDis, yDis, zDis, direction, customItem, blockScale, ContainerAnodisingVessel.SLOT_SOLUTE);
	}
	
	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double xDis, double yDis, double zDis, float f)
	{
		renderAt((TileEntityAnodisingVessel)tileEntity, xDis, yDis, zDis);
	}
}
