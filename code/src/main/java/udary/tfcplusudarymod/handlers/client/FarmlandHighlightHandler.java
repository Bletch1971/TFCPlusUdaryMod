package udary.tfcplusudarymod.handlers.client;

import net.minecraft.block.Block;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;

import org.lwjgl.opengl.GL11;

import udary.tfcplusudarymod.core.ModOptions;

import com.dunk.tfc.Core.TFC_Core;
import com.dunk.tfc.Core.Player.PlayerManagerTFC;
import com.dunk.tfc.Core.Player.SkillStats.SkillRank;
import com.dunk.tfc.Food.CropIndex;
import com.dunk.tfc.Food.CropManager;
import com.dunk.tfc.Items.Tools.ItemCustomHoe;
import com.dunk.tfc.TileEntities.TECrop;
import com.dunk.tfc.api.TFCBlocks;
import com.dunk.tfc.api.Constant.Global;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;

public class FarmlandHighlightHandler
{
	@SubscribeEvent
	public void DrawBlockHighlightEvent(DrawBlockHighlightEvent evt)
	{
		if (!ModOptions.enableTerraFirmaCraftAdditions) return;
		
		World world = evt.player.worldObj;
		double var8 = evt.player.lastTickPosX + (evt.player.posX - evt.player.lastTickPosX) * evt.partialTicks;
		double var10 = evt.player.lastTickPosY + (evt.player.posY - evt.player.lastTickPosY) * evt.partialTicks;
		double var12 = evt.player.lastTickPosZ + (evt.player.posZ - evt.player.lastTickPosZ) * evt.partialTicks;

		if(evt.currentItem != null && evt.currentItem.getItem() instanceof ItemCustomHoe && PlayerManagerTFC.getInstance().getClientPlayer().hoeMode == 3)
		{
			SkillRank sr = TFC_Core.getSkillStats(evt.player).getSkillRank(Global.SKILL_AGRICULTURE);
			if (sr == SkillRank.Expert || sr == SkillRank.Master)
			{
				Block b = world.getBlock(evt.target.blockX,evt.target.blockY,evt.target.blockZ);
				if (b == TFCBlocks.crops)
				{
					TECrop te = (TECrop) world.getTileEntity(evt.target.blockX, evt.target.blockY, evt.target.blockZ);
					CropIndex crop = CropManager.getInstance().getCropFromId(te.cropId);
					
					int growthStages = crop.numGrowthStages + 1;
					int growthStage = (int) Math.floor(te.growth) + 1;
					if (growthStage > growthStages)
						growthStage = growthStages;
					
					double xSize = 1.0 / growthStages;
					double ySize = growthStages > 8 ? 0.05 : 0.1;
					double xOffset = 0;
					double yOffset = ySize;
	
					double blockX = evt.target.blockX;
					double blockY = evt.target.blockY;
					double blockZ = evt.target.blockZ;				
	
					//Setup GL for the depth box
					GL11.glEnable(GL11.GL_BLEND);
					GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
	
					GL11.glDisable(GL11.GL_CULL_FACE);
					GL11.glDisable(GL11.GL_TEXTURE_2D);
					GL11.glDepthMask(false);
	
					/**
					 * Draw the stage boxes
					 **/
					
					for (int stage = 1; stage <= growthStages; stage++)
					{
						if (stage <= growthStage)
							GL11.glColor4ub((byte)64, (byte)200, (byte)37, (byte)200);
						else
							GL11.glColor4ub((byte)200, (byte)37, (byte)37, (byte)200);
							
						drawBox(AxisAlignedBB.getBoundingBox(
								blockX + xOffset,
								blockY,
								blockZ,
								blockX + xOffset + xSize,
								blockY + yOffset,
								blockZ + 1.0
								).expand(0.002F, 0.002F, 0.002F).getOffsetBoundingBox(-var8, -var10, -var12));
		
						xOffset += xSize;
						yOffset += ySize;
					}
	
					GL11.glEnable(GL11.GL_CULL_FACE);
	
					/**
					 * Draw the outlines around the stage boxes
					 **/
	
					GL11.glColor4f(0.1F, 0.1F, 0.1F, 1.0F);
					GL11.glLineWidth(3.0F);
					GL11.glDepthMask(false);
	
					xOffset = 0;
					yOffset = 0.1;
	
					for (int stage = 0; stage < growthStages; stage++)
					{
						drawOutlinedBoundingBox(AxisAlignedBB.getBoundingBox(
								blockX + xOffset,
								blockY,
								blockZ,
								blockX + xOffset + xSize,
								blockY + yOffset,
								blockZ + 1.0
								).expand(0.002F, 0.002F, 0.002F).getOffsetBoundingBox(-var8, -var10, -var12));
		
						xOffset += xSize;
						yOffset += ySize;
					}
				}
			}
		}
	}

	private void drawBox(AxisAlignedBB par1AxisAlignedBB)
	{
		Tessellator tessellator = Tessellator.instance;

		//Top
		tessellator.startDrawing(GL11.GL_QUADS);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.draw();

		//-x
		tessellator.startDrawing(GL11.GL_QUADS);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.draw();

		//+x
		tessellator.startDrawing(GL11.GL_QUADS);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();

		//-z
		tessellator.startDrawing(GL11.GL_QUADS);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();

		//+z
		tessellator.startDrawing(GL11.GL_QUADS);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
	}

	private void drawOutlinedBoundingBox(AxisAlignedBB par1AxisAlignedBB)
	{
		Tessellator tessellator = Tessellator.instance;
		
		tessellator.startDrawing(GL11.GL_LINE_STRIP);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		
		tessellator.startDrawing(GL11.GL_LINE_STRIP);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.draw();
		
		tessellator.startDrawing(GL11.GL_LINES);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.minZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.maxX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.minY, par1AxisAlignedBB.maxZ);
		tessellator.addVertex(par1AxisAlignedBB.minX, par1AxisAlignedBB.maxY, par1AxisAlignedBB.maxZ);
		tessellator.draw();
	}
}
