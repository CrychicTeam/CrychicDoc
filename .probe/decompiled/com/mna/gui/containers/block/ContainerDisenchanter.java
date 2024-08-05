package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.DisenchanterTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.items.ItemInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

public class ContainerDisenchanter extends SimpleWizardLabContainer<ContainerDisenchanter, DisenchanterTile> {

    public ContainerDisenchanter(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.DISENCHANTER.get(), i, playerInventory, packetBuffer);
    }

    public ContainerDisenchanter(int id, Inventory playerInventory, DisenchanterTile tile) {
        super(ContainerInit.DISENCHANTER.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        this.m_38897_(new Slot(this.tile, 0, 79, 7));
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 1, 79, 49, ItemInit.RUNE_PROJECTION.get()).setMaxStackSize(1));
        return 2;
    }

    @Override
    protected int playerInventoryXStart() {
        return 8;
    }

    @Override
    protected int playerInventoryYStart() {
        return 71;
    }
}