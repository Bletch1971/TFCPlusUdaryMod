package udary.tfcplusudarymod.containers.devices;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import udary.tfcplusudarymod.containerslots.SlotAlloyCalculator;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAlloyCalculator;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;

public class ContainerAlloyCalculator extends ContainerTFC 
{
	public static final int SLOT_INPUT_1 = 0;
	public static final int SLOT_INPUT_2 = 1;
	public static final int SLOT_INPUT_3 = 2;
	public static final int SLOT_INPUT_4 = 3;
	
	protected TileEntityAlloyCalculator alloyCalculator;
	protected InventoryPlayer inventoryPlayer;
	protected EntityPlayer player;
	protected int minSlotNumber = 0;
	protected int maxSlotNumber = 0;
	
	protected boolean canInteractWith;
	
	public ContainerAlloyCalculator(InventoryPlayer inventoryPlayer, TileEntityAlloyCalculator tileEntity, World world, int x, int y, int z)
	{
		this.alloyCalculator = tileEntity;
		this.inventoryPlayer = inventoryPlayer;
		this.player = inventoryPlayer.player;
		
		this.canInteractWith = true;
		
		buildContainerLayout();
	}

	protected void buildContainerLayout()
	{
		PlayerInventory.buildInventoryLayout(this, inventoryPlayer, 8, 90, false, true);
		
		// record the starting point of our slots
		minSlotNumber = this.inventorySlots.size();
		
		addSlotToContainer(new SlotAlloyCalculator(player, alloyCalculator, SLOT_INPUT_1, 6, 6));
		addSlotToContainer(new SlotAlloyCalculator(player, alloyCalculator, SLOT_INPUT_2, 24, 6));
		addSlotToContainer(new SlotAlloyCalculator(player, alloyCalculator, SLOT_INPUT_3, 6, 24));
		addSlotToContainer(new SlotAlloyCalculator(player, alloyCalculator, SLOT_INPUT_4, 24, 24));

		// record the ending point of our slots
		maxSlotNumber = this.inventorySlots.size();
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return this.canInteractWith;
	}

	@SuppressWarnings("unchecked")
	@Override
	public void detectAndSendChanges()
	{
		super.detectAndSendChanges();

		for (int slotIndex = 0; slotIndex < this.inventorySlots.size(); ++slotIndex)
		{
			ItemStack slotStack = ((Slot)this.inventorySlots.get(slotIndex)).getStack();
			ItemStack itemStack = (ItemStack)this.inventoryItemStacks.get(slotIndex);

			if (!ItemStack.areItemStacksEqual(itemStack, slotStack))
			{
				itemStack = slotStack == null ? null : slotStack.copy();
				this.inventoryItemStacks.set(slotIndex, itemStack);

				for (int i = 0; i < this.crafters.size(); ++i)
					((ICrafting)this.crafters.get(i)).sendSlotContents(this, slotIndex, itemStack);
			}
		}
	}
	
	public static int[] getInputSlotIndexes()
	{
		return new int[] { SLOT_INPUT_1, SLOT_INPUT_2, SLOT_INPUT_3, SLOT_INPUT_4 };
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return new int[] { };
	}
	
	@Override
	public ItemStack transferStackInSlotTFC(EntityPlayer player, int slotNumber)
	{
		Slot slot = (Slot)inventorySlots.get(slotNumber);
		if (slot == null || !slot.getHasStack()) return null;
		
		ItemStack is = slot.getStack();
		
		// check if the stack size has 1 or more items.
		if (is.stackSize > 0)
		{
			// check if the slot index is a Tile Entity slot.
			if (slotNumber >= minSlotNumber && slotNumber <= maxSlotNumber)
			{
				// slot index is a Tile Entity slot, merge item stack with player inventory slot. 
				if (!this.mergeItemStack(is, 0, minSlotNumber, true))
					return null;
			}
			else
			{
				// slot index is a player inventory slot, merge item stack with Tile Entity slot.
				if (!this.mergeItemStack(is, minSlotNumber, maxSlotNumber, false))
					return null;
			}
		}

		// check if the item stack size is now zero.
		if (is.stackSize == 0)
			// stack size is zero, empty the slot.
			slot.putStack(null);
		else
			// stack size is not zero, just request a slot update.
			slot.onSlotChanged();
		
		return null;	
	}
}
