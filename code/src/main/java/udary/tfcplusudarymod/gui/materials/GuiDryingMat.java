package udary.tfcplusudarymod.gui.materials;

import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

import org.lwjgl.opengl.GL11;

import udary.common.enums.EnumRecipeMatchType;
import udary.tfcplusudarymod.containers.devices.ContainerOreCooker;
import udary.tfcplusudarymod.containers.materials.ContainerDryingMat;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.managers.DryingMatManager;
import udary.tfcplusudarymod.core.recipes.DryingMatRecipe;
import udary.tfcplusudarymod.tileentities.materials.TileEntityDryingMat;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Player.PlayerInventory;
import com.dunk.tfc.GUI.GuiContainerTFC;

public class GuiDryingMat extends GuiContainerTFC
{
	protected static final ResourceLocation guiTexture = new ResourceLocation(ModDetails.ModID, ModDetails.AssetPathGui + "gui_DryingMat.png");
	
	public static final int BUTTON_EMPTY = 0;

	protected TileEntityDryingMat tileEntity;	
	
	public GuiDryingMat(InventoryPlayer inventoryPlayer, TileEntityDryingMat tileEntity, World world, int x, int y, int z) 
	{
		super(new ContainerDryingMat(inventoryPlayer, tileEntity, world, x, y, z), 176, 85);

		this.tileEntity = tileEntity;
	}

	@Override
	public void drawCenteredString(FontRenderer fontrenderer, String text, int x, int y, int color)
	{
		fontrenderer.drawString(text, x - fontrenderer.getStringWidth(text) / 2, y, color);
	}
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) 
	{
		TFC_Core.bindTexture(guiTexture);
		
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 0.5F);

		drawTexturedModalRect(guiLeft, guiTop, 0, 0, xSize, ySize);

		if (tileEntity != null)
		{
			ItemStack is = tileEntity.getStackInSlot(ContainerOreCooker.SLOT_INPUT_1);
			DryingMatRecipe recipe = DryingMatManager.getInstance().findRecipe(is, EnumRecipeMatchType.MINIMUM);

			if (recipe != null) 
			{
				drawCenteredString(fontRendererObj, EnumChatFormatting.UNDERLINE+recipe.getLocalizedResultName(false), guiLeft+88, guiTop+15, 0x555555);

				if (tileEntity.getCanDry())
				{
					int percent = tileEntity.getProcessPercentage();
					drawCenteredString(fontRendererObj, recipe.getLocalizedProcessingMessage()+(percent > 0 ? " ("+String.valueOf(percent)+"%)" : ""), guiLeft+88, guiTop+72, 0x555555);
				}
			}
		}
		
		PlayerInventory.drawInventory(this, width, height, ySize - PlayerInventory.invYSize);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		guiLeft = (width - xSize) / 2;
		guiTop = (height - ySize) / 2;
	}
}
