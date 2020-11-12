package udary.tfcplusudarymod.containers.devices;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.ICrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import udary.tfcplusudarymod.containerslots.SlotEvaporatorPanOutput;
import udary.tfcplusudarymod.tileentities.devices.TileEntityEvaporatorPan;

import com.dunk.tfc.Containers.ContainerTFC;
import com.dunk.tfc.Core.Player.PlayerInventory;

public class ContainerEvaporatorPan extends ContainerTFC
{
	public static final int SLOT_OUTPUT_1 = 0;
	public static final int SLOT_OUTPUT_2 = 1;
	public static final int SLOT_OUTPUT_3 = 2;
	public static final int SLOT_OUTPUT_4 = 3;
	
	protected TileEntityEvaporatorPan evaporatorPan;
	protected InventoryPlayer inventoryPlayer;
	protected EntityPlayer player;
	protected int minSlotNumber = 0;
	protected int maxSlotNumber = 0;

	protected boolean canInteractWith;
	protected int fluidId;
	protected int fluidLevel;

	public ContainerEvaporatorPan(InventoryPlayer inventoryPlayer, TileEntityEvaporatorPan tileEntity, World world, int x, int y, int z)
	{
		this.evaporatorPan = tileEntity;
		this.inventoryPlayer = inventoryPlayer;
		this.player = inventoryPlayer.player;
		
		this.canInteractWith = true;
		this.fluidId = -1;
		this.fluidLevel = 0;
		
		buildContainerLayout();
	}

	protected void buildContainerLayout()
	{
		PlayerInventory.buildInventoryLayout(this, inventoryPlayer, 8, 90, false, true);
		
		// record the starting point of our slots
		minSlotNumber = this.inventorySlots.size();
		
		// output slot 1
		addSlotToContainer(new SlotEvaporatorPanOutput(player, evaporatorPan, SLOT_OUTPUT_1,  62, 26));
		addSlotToContainer(new SlotEvaporatorPanOutput(player, evaporatorPan, SLOT_OUTPUT_2,  80, 26));
		addSlotToContainer(new SlotEvaporatorPanOutput(player, evaporatorPan, SLOT_OUTPUT_3,  98, 26));
		addSlotToContainer(new SlotEvaporatorPanOutput(player, evaporatorPan, SLOT_OUTPUT_4, 116, 26));

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
				{
					((ICrafting)this.crafters.get(i)).sendSlotContents(this, slotIndex, itemStack);
				}
			}
		}

		for (int var1 = 0; var1 < this.crafters.size(); ++var1)
		{
			ICrafting var2 = (ICrafting)this.crafters.get(var1);

			if (evaporatorPan.getFluidStack() != null && fluidId != evaporatorPan.getFluidStack().getFluidID())
			{
				fluidId = evaporatorPan.getFluidStack().getFluidID();
				
				var2.sendProgressBarUpdate(this, 0, fluidId);
			}
			
			if (fluidLevel != evaporatorPan.getFluidLevel())
			{
				fluidLevel = evaporatorPan.getFluidLevel();
				
				var2.sendProgressBarUpdate(this, 1, fluidLevel);
			}
		}
	}
	
	public static int[] getInputSlotIndexes()
	{
		return new int[] { };
	}
	
	public static int[] getOutputSlotIndexes()
	{
		return new int[] { SLOT_OUTPUT_1, SLOT_OUTPUT_2, SLOT_OUTPUT_3, SLOT_OUTPUT_4 };
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

	@Override
	public void updateProgressBar(int id, int value)
	{
		if (id == 0)
		{
			Fluid fluid = FluidRegistry.getFluid(value);
			if (fluid != null)
			{
				if (evaporatorPan.inputFS != null)
					evaporatorPan.inputFS = new FluidStack(fluid, evaporatorPan.inputFS.amount);
				else
					evaporatorPan.inputFS = new FluidStack(fluid, 1000);
			}
		}
		else if (id == 1)
		{
			if (evaporatorPan.inputFS != null)
				evaporatorPan.inputFS.amount = value;
		}
	}
}
