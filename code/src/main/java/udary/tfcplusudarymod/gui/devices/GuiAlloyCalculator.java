package udary.tfcplusudarymod.gui.devices;

import java.text.DecimalFormat;
import java.util.ArrayList;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.common.AlloyCalculatorResults;
import udary.common.AlloyCalculatorResults.AlloyMetalDetails;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyCalculator;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyList;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTextures;
import udary.tfcplusudarymod.gui.buttons.GuiTabButton;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;
import udary.waila.WailaUtils;

import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.Core.Metal.AlloyManager;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.GUI.GuiContainerTFC;
import com.dunk.tfc.api.Metal;

public class GuiAlloyCalculator extends GuiContainerTFC 
{
	protected static final ResourceLocation guiTexture = new ResourceLocation(ModDetails.ModID, ModDetails.AssetPathGui + "gui_AlloyCalculator.png");
	
	protected InventoryPlayer inventoryPlayer;
	protected TileEntityAlloyCalculator tileEntity;	
	protected World world;
	protected int x;
	protected int y;
	protected int z;
	
	public GuiAlloyCalculator(InventoryPlayer inventoryPlayer, TileEntityAlloyCalculator tileEntity, World world, int x, int y, int z) 
	{
		super(new ContainerAlloyCalculator(inventoryPlayer, tileEntity, world, x, y, z), 176, 85);

		this.inventoryPlayer = inventoryPlayer;
		this.tileEntity = tileEntity;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		//if (guibutton.id == ContainerAlloyList.BUTTON_CALCULATOR)
		//	Minecraft.getMinecraft().displayGuiScreen(new GuiAlloyCalculator(inventoryPlayer, tileEntity, world, x, y, z));
		if (guibutton.id == ContainerAlloyList.BUTTON_LIST)
			Minecraft.getMinecraft().displayGuiScreen(new GuiAlloyList(inventoryPlayer, tileEntity, world, x, y, z));
	}
	
	protected void displayAlloy()
	{
		if (tileEntity == null)
			return;
		
		DecimalFormat fmt = new DecimalFormat("#0.00;-#0.00");

		int yPosition = 6;

		AlloyCalculatorResults results = tileEntity.getAlloyCalculatorResults(true);
		
		ArrayList<AlloyMetal> ingredientList = results.getIngredientList();

		if (ingredientList != null && ingredientList.size() > 0)
		{
			int metalAmountTotal = results.getMetalAmountTotal();
			
			Metal alloy = AlloyManager.INSTANCE.matchesAlloy(ingredientList, Alloy.EnumTier.TierIX);
			
			if (alloy != null)
				fontRendererObj.drawString(alloy.name, 45, yPosition, 0x008000);
			else
				fontRendererObj.drawString(StatCollector.translateToLocal("gui.metal.Unknown"), 45, yPosition, 0xFF0000);
			fontRendererObj.drawString(metalAmountTotal+" "+StatCollector.translateToLocal("gui.units"), 45, yPosition+8, 0x000000);
		}

		yPosition += 30;

		ArrayList<AlloyMetalDetails> detailsList = results.getDetailsList();

		if (detailsList != null && detailsList.size() > 0)
		{
			for (int index = 0; index < detailsList.size(); index++)
			{
				if (detailsList.get(index) == null) continue;
					
				yPosition += 8;
				
				String result = detailsList.get(index).metal.name+WailaUtils.SEPARATOR_COLON+detailsList.get(index).metalAmount+" ("+fmt.format(detailsList.get(index).metalPercent)+"%)";
				fontRendererObj.drawString(result, 6, yPosition, 0x000000);
			}
		}
	}	
	
	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int i, int j) 
	{
		drawGui(guiTexture);
	}
	
	@Override
	protected void drawGuiContainerForegroundLayer(int i, int j)
	{
		displayAlloy();
	}
	
	@Override
	public void initGui()
	{
		super.initGui();
		
		guiLeft = (this.width - this.xSize) / 2;
		guiTop = (this.height - this.ySize) / 2;
		
		createButtons();
	}
	
	@SuppressWarnings("unchecked")
	protected void createButtons()
	{
		buttonList.clear();
		
		buttonList.add(new GuiTabButton(ContainerAlloyList.BUTTON_CALCULATOR, guiLeft+xSize, guiTop+4, 25, 20, 0, 0, 25, 20, StatCollector.translateToLocal("gui.alloycalculator.calculator"), ModTextures.GuiAlloyCalculator));
		buttonList.add(new GuiTabButton(ContainerAlloyList.BUTTON_LIST, guiLeft+xSize, guiTop+23, 25, 20, 0, 0, 25, 20, StatCollector.translateToLocal("gui.alloycalculator.list"), ModTextures.GuiAlloyList));		
	}
}
