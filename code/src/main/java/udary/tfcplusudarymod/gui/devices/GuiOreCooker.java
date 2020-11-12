package udary.tfcplusudarymod.gui.devices;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.managers.OreCookerManager;
import udary.tfcplusudarymod.core.recipes.OreCookerRecipe;
import udary.tfcplusudarymod.tileentities.devices.TileEntityOreCooker;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.GUI.GuiContainerTFC;

public class GuiOreCooker extends GuiContainerTFC
{
	protected static final ResourceLocation guiTexture = new ResourceLocation(ModDetails.ModID, ModDetails.AssetPathGui + "gui_OreCooker.png");
	
	protected TileEntityOreCooker tileEntity;
	
	public GuiOreCooker(InventoryPlayer inventoryPlayer, TileEntityOreCooker tileEntity, World world, int x, int y, int z)
	{
		super(new ContainerOreCooker(inventoryPlayer, tileEntity, world, x, y, z), 176, 85);

		this.tileEntity = tileEntity;
	}

	@Override
	public void drawCenteredString(FontRenderer fontRenderer, String text, int x, int y, int color)
	{
		fontRenderer.drawString(text, x - fontRenderer.getStringWidth(text) / 2, y, color);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int mouseX, int mouseY) 
	{
		TFC_Core.bindTexture(guiTexture);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);
		
		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);
		
		if (tileEntity != null)
		{
			int scaledTemperature = tileEntity.getTemperatureScaled(49);
			drawTexturedModalRect(guiLeft + 7, guiTop + 65 - scaledTemperature, 185, 31, 15, 6);

			ItemStack is = tileEntity.getStackInSlot(ContainerOreCooker.SLOT_INPUT_1);
			OreCookerRecipe recipe = OreCookerManager.getInstance().findRecipe(is, EnumRecipeMatchType.MINIMUM);

			if (recipe != null)
			{
				drawCenteredString(fontRendererObj, EnumChatFormatting.UNDERLINE+recipe.getLocalizedResultName(false), guiLeft+98, guiTop+15, 0x555555);
				
				if (tileEntity.getCanCook())
				{
					int percent = tileEntity.getProcessPercentage();
					drawCenteredString(fontRendererObj, recipe.getLocalizedProcessingMessage()+(percent > 0 ? " ("+String.valueOf(percent)+"%)" : ""), guiLeft+98, guiTop+72, 0x555555);
				}
			}
		}
		
		PlayerInventory.drawInventory(this, width, height, ySize - PlayerInventory.invYSize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		if (this.mouseInRegion(10, 17, 9, 53, mouseX, mouseY))
		{
			if (tileEntity != null)
			{
				ArrayList<String> list = new ArrayList<String>();
				list.add(""+tileEntity.getTemperature());
				
				drawHoveringText(list, mouseX-guiLeft, mouseY-guiTop+8, this.fontRendererObj);
			}
		}
	}

	@Override
	public void drawTooltip(int mouseX, int mouseY, String text) 
	{
		List<String> list = new ArrayList<String>();
		list.add(text);
		
		drawHoveringText(list, mouseX, mouseY+15, this.fontRendererObj);
		RenderHelper.disableStandardItemLighting();
		
		GL11.glDisable(GL11.GL_LIGHTING);
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}
}
