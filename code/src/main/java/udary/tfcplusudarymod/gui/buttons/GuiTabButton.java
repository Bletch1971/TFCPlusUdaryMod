package udary.tfcplusudarymod.gui.buttons;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.core.ModDetails;

import com.dunk.tfc.Core.TFC_Core;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;

public class GuiTabButton extends GuiButton 
{
	protected static ResourceLocation texture = new ResourceLocation(ModDetails.ModID+":"+ModDetails.AssetPathGui+"buttonsTab.png");
	
	protected IIcon icon;
	protected int bX = 0;
	protected int bY = 0;
	protected int bW = 0;
	protected int bH = 0;

	public GuiTabButton(int index, int xPos, int yPos, int width, int height, int buttonX, int buttonY, int buttonW, int buttonH, String s, IIcon ico)
	{
		super(index, xPos, yPos, width, height, s);
		
		bX = buttonX;
		bY = buttonY;
		bW = buttonW;
		bH = buttonH;
		icon = ico;
	}

	@Override
	public void drawButton(Minecraft mc, int x, int y)
	{
		if (this.visible)
		{
			TFC_Core.bindTexture(texture);
			
			this.drawTexturedModalRect(this.xPosition, this.yPosition, this.bX, this.bY, this.bW, this.bH);

			this.field_146123_n = x >= this.xPosition && y >= this.yPosition && x < this.xPosition + this.width && y < this.yPosition + this.height;

			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			
			if(icon != null) 
			{
				TFC_Core.bindTexture(TextureMap.locationBlocksTexture);
				this.drawTexturedModelRectFromIcon(this.xPosition+4, this.yPosition+2, icon, 16, 16);
			}

			this.mouseDragged(mc, x, y);
		}
	}
}
