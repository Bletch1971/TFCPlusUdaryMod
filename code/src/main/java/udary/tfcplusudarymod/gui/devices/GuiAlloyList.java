package udary.tfcplusudarymod.gui.devices;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import udary.tfcplusudarymod.containers.devices.ContainerAlloyList;
import udary.tfcplusudarymod.core.ModDetails;
import udary.tfcplusudarymod.core.ModTextures;
import udary.tfcplusudarymod.gui.buttons.GuiPageButton;
import udary.tfcplusudarymod.gui.buttons.GuiTabButton;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;

import com.dunk.tfc.Core.Metal.Alloy;
import com.dunk.tfc.Core.Metal.AlloyManager;
import com.dunk.tfc.Core.Metal.AlloyMetal;
import com.dunk.tfc.Core.Metal.AlloyMetalCompare;
import com.dunk.tfc.GUI.GuiContainerTFC;

public class GuiAlloyList extends GuiContainerTFC
{
	protected static final ResourceLocation guiTexture = new ResourceLocation(ModDetails.ModID, ModDetails.AssetPathGui + "gui_AlloyList.png");
	
	public static final int BUTTON_PREVIOUS = 11;
	public static final int BUTTON_NEXT = 12;
	
	protected InventoryPlayer inventoryPlayer;
	protected TileEntityAlloyCalculator tileEntity;	
	protected World world;
	protected int x;
	protected int y;
	protected int z;
	
	protected int alloysPage = 0;
	protected final int alloysPerPage = 3;
	
	public GuiAlloyList(InventoryPlayer inventoryPlayer, TileEntityAlloyCalculator tileEntity, World world, int x, int y, int z) 
	{
		super(new ContainerAlloyList(inventoryPlayer, tileEntity, world, x, y, z), 176, 172);
		
		this.inventoryPlayer = inventoryPlayer;
		this.tileEntity = tileEntity;
		this.world = world;
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.setDrawInventory(false);
	}

	@Override
	protected void actionPerformed(GuiButton guibutton)
	{
		if (guibutton.id == ContainerAlloyList.BUTTON_CALCULATOR)
			Minecraft.getMinecraft().displayGuiScreen(new GuiAlloyCalculator(inventoryPlayer, tileEntity, world, x, y, z));
		//if (guibutton.id == ContainerAlloyList.BUTTON_LIST)
		//	Minecraft.getMinecraft().displayGuiScreen(new GuiAlloyList(inventoryPlayer, tileEntity, world, x, y, z));
		
		if (guibutton.id == BUTTON_PREVIOUS)
		{
			alloysPage--;
			if (alloysPage < 0)
				alloysPage = 0;
		}
		
		if (guibutton.id == BUTTON_NEXT)
		{
			alloysPage++;
		}
	}
	
	protected void displayResults()
	{
		if (tileEntity == null) 
			return;
		
		DecimalFormat fmt = new DecimalFormat("#0;-#0");
		int xPosition = 6;
		int yPosition = 6;
		
		fontRendererObj.drawString(StatCollector.translateToLocal("gui.alloycalculator.result"), xPosition, yPosition, 0x000000);
		
		yPosition += 8;
		
		ArrayList<AlloyMetal> ingredientList = tileEntity.getIngredientList(false);
		
		if (ingredientList != null && ingredientList.size() > 0)
		{
			ArrayList<Alloy> results = findAlloys(ingredientList);
			
			if (results != null && results.size() > 0)
			{
				for (int index = 0; index < results.size(); index++)
				{
					// check if the alloy is to be displayed
					if (index < (alloysPerPage * alloysPage) || index >= (alloysPerPage * alloysPage) + alloysPerPage) continue;
						
					Alloy matchedAlloy = results.get(index);
					if (matchedAlloy == null || matchedAlloy.alloyIngred.size() < 2) continue;
					
					xPosition = 6;
					yPosition += 8;

					fontRendererObj.drawString(matchedAlloy.outputType.name, xPosition, yPosition, 0x0000FF);
					
					for (int index2 = 0; index2 < matchedAlloy.alloyIngred.size(); index2++)
					{
						AlloyMetal alloyMetal = matchedAlloy.alloyIngred.get(index2);
						if (alloyMetal == null) continue;
					
						xPosition = 12;
						yPosition += 8;
						
						if (alloyMetal instanceof AlloyMetalCompare)
							fontRendererObj.drawString(alloyMetal.metalType.name+" ("+fmt.format(alloyMetal.metal)+"% - "+fmt.format(((AlloyMetalCompare)alloyMetal).getMetalMax())+"%)", xPosition, yPosition, 0x000000); 
						else
							fontRendererObj.drawString(alloyMetal.metalType.name+" ("+fmt.format(alloyMetal.metal)+"%)", xPosition, yPosition, 0x000000); 
					}
				}
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
		displayResults();
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
		buttonList.add(new GuiTabButton(ContainerAlloyList.BUTTON_CALCULATOR, guiLeft+xSize, guiTop+4,  25, 20, 0, 0, 25, 20, StatCollector.translateToLocal("gui.alloycalculator.calculator"), ModTextures.GuiAlloyCalculator));
		buttonList.add(new GuiTabButton(ContainerAlloyList.BUTTON_LIST,       guiLeft+xSize, guiTop+23, 25, 20, 0, 0, 25, 20, StatCollector.translateToLocal("gui.alloycalculator.list"), ModTextures.GuiAlloyList));
		
		buttonList.add(new GuiPageButton(BUTTON_PREVIOUS, guiLeft+xSize-4-60, guiTop+4, 30, 15, 0, 0,  30, 15));
		buttonList.add(new GuiPageButton(BUTTON_NEXT,     guiLeft+xSize-4-30, guiTop+4, 30, 15, 0, 15, 30, 15));				
	}
	
	protected ArrayList<Alloy> findAlloys(ArrayList<AlloyMetal> ingredients)
	{
		if (ingredients == null || ingredients.size() == 0) 
			return new ArrayList<Alloy>(0);
		
		Iterator<Alloy> alloys = AlloyManager.INSTANCE.alloys.iterator();
		ArrayList<Alloy> results = new ArrayList<Alloy>();
		
		while(alloys.hasNext())
		{
			Alloy alloy = alloys.next();
			Alloy matchedAlloy = matchAlloy(alloy, ingredients);
			
			if (matchedAlloy != null && matchedAlloy.alloyIngred.size() > 1)
				results.add(matchedAlloy);
		}
		
		return results;
	}
	
	protected Alloy matchAlloy(Alloy alloy, ArrayList<AlloyMetal> ingredients)
	{
		if (alloy == null || ingredients == null || ingredients.size() == 0)
			return null;
		
		for (int index = 0; index < ingredients.size(); index++)
		{
			if (!alloy.searchForAlloyMetal(ingredients.get(index)))
				return null;
		}
		
		return alloy;
	}

	@Override
	public void updateScreen()
	{
		super.updateScreen();

		for (int index = 0; index < buttonList.size(); index++)
		{
			if (!(buttonList.get(index) instanceof GuiButton)) continue;
			
			switch (((GuiButton)buttonList.get(index)).id)
			{
				case BUTTON_PREVIOUS:
					((GuiButton)buttonList.get(index)).enabled = alloysPage > 0;
					break;
				case BUTTON_NEXT:
					ArrayList<AlloyMetal> ingredientList = tileEntity.getIngredientList(false);
					ArrayList<Alloy> results = findAlloys(ingredientList);
					((GuiButton)buttonList.get(index)).enabled = (alloysPage+1)*alloysPerPage < results.size();
					break;
			}
		}
	}
}
