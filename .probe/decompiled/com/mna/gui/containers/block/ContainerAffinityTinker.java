package com.mna.gui.containers.block;

import com.mna.api.affinity.Affinity;
import com.mna.blocks.tileentities.wizard_lab.AffinityTinkerTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SlotNoPlace;
import com.mna.items.filters.SpellItemFilter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.DataSlot;

public class ContainerAffinityTinker extends SimpleWizardLabContainer<ContainerAffinityTinker, AffinityTinkerTile> {

    protected DataSlot selectedAffinityOrdinal;

    public ContainerAffinityTinker(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.AFFINITY_TINKER.get(), i, playerInventory, packetBuffer);
        this.selectedAffinityOrdinal.set(Affinity.ARCANE.ordinal());
    }

    public ContainerAffinityTinker(int id, Inventory playerInventory, AffinityTinkerTile tile) {
        super(ContainerInit.AFFINITY_TINKER.get(), id, playerInventory, tile);
        this.selectedAffinityOrdinal.set(Affinity.ARCANE.ordinal());
    }

    @Override
    protected int addInventorySlots() {
        this.selectedAffinityOrdinal = DataSlot.standalone();
        this.m_38895_(this.selectedAffinityOrdinal);
        this.m_38897_(new ItemFilterSlot(this.tileItemHandler, 0, 80, 72, new SpellItemFilter()).setMaxStackSize(1));
        this.m_38897_(new SlotNoPlace(this.tileItemHandler, 1, 80, 128));
        return 2;
    }

    public Affinity getSelectedAffinity() {
        return this.tile.getAffinity();
    }

    @Override
    protected int playerInventoryXStart() {
        return 8;
    }

    @Override
    protected int playerInventoryYStart() {
        return 151;
    }
}