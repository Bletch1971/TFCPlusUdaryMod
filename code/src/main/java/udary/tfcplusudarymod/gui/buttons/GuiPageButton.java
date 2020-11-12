package udary.tfcplusudarymod.gui.buttons;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.core.ModDetails;

import com.dunk.tfc.Core.TFC_Core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

public class GuiPageButton extends GuiButton 
{
	protected static ResourceLocation texture = new ResourceLocation(ModDetails.ModID+":"+ModDetails.AssetPathGui+"buttonsPage.png");
	
	protected int bX = 0;
	protected int bY = 0;
	protected int bW = 0;
	protected int bH = 0;
	
	public GuiPageButton(int index, int xPos, int yPos, int width, int height, int buttonX, int buttonY, int buttonW, int buttonH)
	{
		super(index, xPos, yPos, width, height, "");
		
		bX = buttonX;
		bY = buttonY;
		bW = buttonW;
		bH = buttonH;
	}
	
	@Override
	public void drawButton(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			TFC_Core.bindTexture(texture);
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition, this.bX+(this.enabled?0:30), this.bY, this.bW, this.bH);

			this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

			this.mouseDragged(mc, x, y);
		}
	}
}
