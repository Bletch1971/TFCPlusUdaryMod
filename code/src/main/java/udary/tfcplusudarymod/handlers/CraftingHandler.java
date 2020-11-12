package udary.tfcplusudarymod.handlers;

import udary.tfcplusudarymod.core.ModGlobal;
import udary.tfcplusudarymod.core.ModItems;
import udary.tfcplusudarymod.core.ModTags;
import udary.tfcplusudarymod.items.materials.ItemDryingMat;

import com.dunk.tfc.Core.Recipes;
import com.dunk.tfc.Items.ItemOre;
import com.dunk.tfc.api.TFCItems;
import com.dunk.tfc.api.TFCOptions;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;

public class CraftingHandler
{
	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent e)
	{
		ItemStack itemstack = e.crafting;
		Item item = itemstack.getItem();
		IInventory inventory = e.craftMatrix;
		
		if (item == null || inventory == null) return;

		if (item == ModItems.CarbonRod)
		{
			// damage the knife.
			handleItem(e.player, inventory, Recipes.knives);

			return;
		}
		
		if (item == ModItems.CopperRod)
		{
			// check for a silver coated copper rod.
			ItemStack anodisedItem = getItemStack(inventory, ModItems.CopperRodSilverCoated);
			
			if (anodisedItem != null)
			{
				handleAnodisedItem(e.player, inventory, anodisedItem, ModItems.SilverFlake, ModTags.TAG_UNITS_SILVER);
				handleAnodisedItem(e.player, inventory, anodisedItem, ModItems.SilverFlake, ModTags.TAG_ANODISING_UNITS);
				return;
			}
				
			// check for a nickel coated copper rod.
			anodisedItem = getItemStack(inventory, ModItems.CopperRodNickelCoated);
			
			if (anodisedItem != null)
			{
				handleAnodisedItem(e.player, inventory, anodisedItem, ModItems.NickelFlake, ModTags.TAG_UNITS_NICKLE);
				handleAnodisedItem(e.player, inventory, anodisedItem, ModItems.NickelFlake, ModTags.TAG_ANODISING_UNITS);
				return;
			}

			return;
		}

		if (item == ModItems.CookedLimonitePowder)
		{
			// damage the hammer.
			handleItem(e.player, inventory, Recipes.hammers);

			return;
		}

		if (item == ModItems.CeramicBattery)
		{
			ItemStack bottledFluid = getItemStack(inventory, ModItems.BottledSulfuricAcid);
			
			if (bottledFluid != null)
			{
				// return empty bottle to player inventory
				ItemStack outputStack = new ItemStack(TFCItems.glassBottle);
				
				if (!e.player.inventory.addItemStackToInventory(outputStack))
				{
					// no room in the players inventory, so drop them on the ground
					e.player.entityDropItem(outputStack, 0.0F);			
				}
			}
			
			return;
		}

		if (item == TFCItems.oreChunk)
		{
			ItemStack inputOre = getItemStack(inventory, TFCItems.oreChunk);
			
			if (inputOre != null)
			{
				int amount = ((ItemOre)inputOre.getItem()).getMetalReturnAmount(inputOre);
				int damage = inputOre.getItemDamage();
				
				if (amount == TFCOptions.richOreUnits)
				{
					ItemStack outputStack = new ItemStack(TFCItems.smallOreChunk, 1, damage - 35);

					if (!e.player.inventory.addItemStackToInventory(outputStack))
					{
						// no room in the players inventory, so drop them on the ground
						e.player.entityDropItem(outputStack, 0.0F);			
					}
					
					// damage the hammer.
					handleItem(e.player, inventory, Recipes.hammers);						
				}
				else if (amount == TFCOptions.normalOreUnits)
				{
					ItemStack outputStack = new ItemStack(TFCItems.smallOreChunk, 1, damage);

					if (!e.player.inventory.addItemStackToInventory(outputStack))
					{
						// no room in the players inventory, so drop them on the ground
						e.player.entityDropItem(outputStack, 0.0F);			
					}
					
					// damage the hammer.
					handleItem(e.player, inventory, Recipes.hammers);
				}
			}

			return;
		}

		if (item == ModItems.TuckerBagv2)
		{
			// damage the knife.
			handleItem(e.player, inventory, Recipes.knives);

			return;
		}

		if (item instanceof ItemDryingMat)
		{
			// damage the knife.
			handleItem(e.player, inventory, Recipes.knives);

			return;
		}
	}

	private static void damageItem(EntityPlayer entityPlayer, IInventory inventory, int index, Item shiftedIndex)
	{
		if (inventory.getStackInSlot(index).getItem() != shiftedIndex) 
			return;
		
		ItemStack item = inventory.getStackInSlot(index).copy();
		if (item == null) 
			return;

		item.damageItem(1 , entityPlayer);
		
		if (item.getItemDamage() != 0 || entityPlayer.capabilities.isCreativeMode)
		{
			inventory.setInventorySlotContents(index, item);
			inventory.getStackInSlot(index).stackSize = inventory.getStackInSlot(index).stackSize + 1;
			
			if (inventory.getStackInSlot(index).stackSize > 2)
				inventory.getStackInSlot(index).stackSize = 2;
		}
	}

	private static ItemStack getItemStack(IInventory inventory, Item item)
	{
		for (int index = 0; index < inventory.getSizeInventory(); index++)
		{
			if(inventory.getStackInSlot(index) == null)
				continue;
			
			if(inventory.getStackInSlot(index).getItem() == item)
				return inventory.getStackInSlot(index);
		}
		
		return null;
	}

	private void handleAnodisedItem(EntityPlayer entityPlayer, IInventory inventory, ItemStack anodisedItem, Item flakeItem, String unitsTag)
	{
		if (entityPlayer == null || inventory == null || anodisedItem == null || flakeItem == null || unitsTag == null)
			return;
		
		if (!anodisedItem.hasTagCompound() || !anodisedItem.stackTagCompound.hasKey(unitsTag))
			return;
		
		// damage the knife.
		handleItem(entityPlayer, inventory, Recipes.knives);
		
		// get the number of flakes.
		float metalUnits = anodisedItem.stackTagCompound.getFloat(unitsTag);
		
		// each flake stack is 10 units, so divide the number of flakes by 10.
		int flakeStacks = MathHelper.floor_float(metalUnits / TFCOptions.smallOreUnits);
		
		// add flakes stacks to the players inventory
		if (flakeStacks > 0)
		{
			ItemStack smallStack = new ItemStack(flakeItem, flakeStacks, TFCOptions.smallOreUnits);
			
			if (!entityPlayer.inventory.addItemStackToInventory(smallStack))
			{
				// no room in the players inventory, so drop them on the ground
				entityPlayer.entityDropItem(smallStack, 0.0F);			
			}
		}
		
		// get the number of flakes remaining.
		int flakeRemainder = Math.max(0, (int)(metalUnits - (flakeStacks * TFCOptions.smallOreUnits)));

		// add remaining flakes stacks to the players inventory
		if (flakeRemainder > 0)
		{
			ItemStack tinyStack = new ItemStack(flakeItem, flakeRemainder, ModGlobal.tinyOreUnits);
			
			if (!entityPlayer.inventory.addItemStackToInventory(tinyStack))
			{
				// no room in the players inventory, so drop them on the ground
				entityPlayer.entityDropItem(tinyStack, 0.0F);			
			}
		}		
	}

	private static void handleItem(EntityPlayer entityPlayer, IInventory inventory, Item[] items)
	{
		for (int index = 0; index < inventory.getSizeInventory(); index++)
		{
			if (inventory.getStackInSlot(index) == null)
				continue;
			
			for (int itemIndex = 0; itemIndex < items.length; itemIndex++)
			{
				damageItem(entityPlayer, inventory, index, items[itemIndex]);
			}
		}
	}
}
