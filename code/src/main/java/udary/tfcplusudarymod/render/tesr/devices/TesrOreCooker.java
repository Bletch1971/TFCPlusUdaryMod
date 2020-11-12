package udary.tfcplusudarymod.render.tesr.devices;

import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.render.blocks.devices.RenderOreCooker;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;

import com.dunk.tfc.Render.TESR.TESRBase;

public class TesrOreCooker extends TESRBase
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
			case ContainerOreCooker.SLOT_INPUT_1:
				// position offset
				cX = (RenderOreCooker.maxX - RenderOreCooker.minX) / 2;
				cZ = (RenderOreCooker.maxZ - RenderOreCooker.minZ) / 2;

				out[0] = RenderOreCooker.minX+cX;
				out[1] = RenderOreCooker.minY+(RenderOreCooker.bH*2);
				out[2] = RenderOreCooker.minZ+cZ;
				
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

	protected void renderAt(TileEntityOreCooker tileEntity, double xDis, double yDis, double zDis)
	{
		if (tileEntity == null || tileEntity.getWorldObj() == null) return;
		
		int direction = 0;

		EntityItem customItem = new EntityItem(field_147501_a.field_147550_f);
		customItem.hoverStart = 0f;
		float blockScale = 1.0F;

		drawItem(tileEntity.getStackInSlot(ContainerOreCooker.SLOT_INPUT_1), xDis, yDis, zDis, direction, customItem, blockScale, ContainerOreCooker.SLOT_INPUT_1);
	}

	@Override
	public void renderTileEntityAt(TileEntity tileEntity, double xDis, double yDis, double zDis, float f)
	{
		renderAt((TileEntityOreCooker)tileEntity, xDis, yDis, zDis);
	}

}
