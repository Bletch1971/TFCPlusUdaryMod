package udary.tfcplusudarymod.render.tesr.materials;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.containers.materials.ContainerDryingMat;
import udary.tfcplusudarymod.render.blocks.materials.RenderDryingMat;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

import com.dunk.tfc.Render.TESR.TESRBase;

public class TesrDryingMat extends TESRBase
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
			case ContainerDryingMat.SLOT_INPUT_1:
				// position offset
				cX = (RenderDryingMat.maxX - RenderDryingMat.minX) / 2;
				cZ = (RenderDryingMat.maxZ - RenderDryingMat.minZ) / 2;

				out[0] = RenderDryingMat.minX+cX;
				out[1] = RenderDryingMat.maxY;
				out[2] = RenderDryingMat.minZ+cZ;
				
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
				out[7] = 1.0f;
				out[8] = 1.0f;
				out[9] = 1.0f;
				break;
		}
		
		return out;
	}	

	protected void renderAt(TileEntityDryingMat tileEntity, double xDis, double yDis, double zDis)
	{
		if (tileEntity == null || tileEntity.getWorldObj() == null) return;
		
		int direction = 0;

		EntityItem customItem = new EntityItem(field_147501_a.field_147550_f);
		customItem.hoverStart = 0f;
		float blockScale = 1.0F;

		drawItem(tileEntity.getStackInSlot(ContainerDryingMat.SLOT_INPUT_1), xDis, yDis, zDis, direction, customItem, blockScale, ContainerDryingMat.SLOT_INPUT_1);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double xDis, double yDis, double zDis, float f)
	{
		renderAt((TileEntityDryingMat)tileEntity, xDis, yDis, zDis);
	}
}
