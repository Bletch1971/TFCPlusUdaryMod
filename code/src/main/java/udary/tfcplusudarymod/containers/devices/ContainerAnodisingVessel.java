package udary.tfcplusudarymod.containers.devices;

import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselAnode;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselBattery;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselCathode;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselInput;
import udary.tfcplusudarymod.containerslots.SlotAnodisingVesselSolute;
import udary.tfcplusudarymod.tileentities.devices.TileEntityAnodisingVessel;

import com.dunk.tfc.Containers.ContainerBarrel;
import com.dunk.tfc.Containers.Slots.SlotForShowOnly;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ContainerAnodisingVessel extends ContainerBarrel
{	
	public static final int SLOT_INPUT = 0;
	public static final int SLOT_BATTERY = 1;
	public static final int SLOT_CATHODE = 2;
	public static final int SLOT_ANODE = 3;
	public static final int SLOT_SOLUTE = 4;
	
	protected TileEntityAnodisingVessel anodisingVessel;
	protected InventoryPlayer inventoryPlayer;
	protected EntityPlayer player;
	protected int minSlotNumber = 0;
	protected int maxSlotNumber = 0;
	
	public ContainerAnodisingVessel(InventoryPlayer inventoryPlayer, TileEntityAnodisingVessel tileEntity, World world, int x, int y, int z)
	{
		super(inventoryPlayer, tileEntity, world, x, y, z, 0);
				
		this.anodisingVessel = tileEntity;
		this.inventoryPlayer = inventoryPlayer;
		this.player = this.inventoryPlayer.player;
		
		buildContainerLayout();
	}

	@Override
	protected void buildLayout()
	{	
		// intentional left blank to prevent building the layout until the this class's constructor is fully run.
	}
	
	protected void buildContainerLayout()
	{
		// record the starting point of our slots (HACK: this is because TFC add the inventory slots before ours)
		minSlotNumber = this.inventorySlots.size();
		
		//Input slot
		if (!anodisingVessel.getSealed())
		{
			addSlotToContainer(new SlotAnodisingVesselInput(player, anodisingVessel, SLOT_INPUT, 29, 15));
			addSlotToContainer(new SlotAnodisingVesselBattery(player, anodisingVessel, SLOT_BATTERY, 151, 15));
			addSlotToContainer(new SlotAnodisingVesselCathode(player, anodisingVessel, SLOT_CATHODE, 61, 28));
			addSlotToContainer(new SlotAnodisingVesselAnode(player, anodisingVessel, SLOT_ANODE, 120, 28));
			addSlotToContainer(new SlotAnodisingVesselSolute(player, anodisingVessel, SLOT_SOLUTE, 90, 28));
		}
		else
		{
			addSlotToContainer(new SlotForShowOnly(anodisingVessel, SLOT_INPUT, 29, 15));
			addSlotToContainer(new SlotForShowOnly(anodisingVessel, SLOT_BATTERY, 151, 15));
			addSlotToContainer(new SlotForShowOnly(anodisingVessel, SLOT_CATHODE, 61, 28));
			addSlotToContainer(new SlotForShowOnly(anodisingVessel, SLOT_ANODE, 120, 28));
			addSlotToContainer(new SlotForShowOnly(anodisingVessel, SLOT_SOLUTE, 90, 28));
		}
		
		// record the ending point of our slots (HACK: this is because TFC add the inventory slots before ours)
		maxSlotNumber = this.inventorySlots.size();
	}
	
	public static int[] getInputSlotIndexes()
	{
		return new int[] { SLOT_BATTERY, SLOT_CATHODE, SLOT_ANODE, SLOT_SOLUTE };
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
			if (!anodisingVessel.getSealed() && slotNumber >= minSlotNumber && slotNumber <= maxSlotNumber)
			{
				// slot index is a Tile Entity slot, merge item stack with player inventory slot. 
				if (!this.mergeItemStack(is, 0, minSlotNumber, true))
					return null;
			}
			else if (!anodisingVessel.getSealed())
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
