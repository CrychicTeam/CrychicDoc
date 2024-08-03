package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.TranscriptionTableTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.items.ItemInit;
import com.mna.items.filters.SpellItemFilter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Items;

public class ContainerTranscriptionTable extends SimpleWizardLabContainer<ContainerTranscriptionTable, TranscriptionTableTile> {

    public ContainerTranscriptionTable(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.TRANSCRIPTION_TABLE.get(), i, playerInventory, packetBuffer);
    }

    public ContainerTranscriptionTable(int id, Inventory playerInventory, TranscriptionTableTile tile) {
        super(ContainerInit.TRANSCRIPTION_TABLE.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 0, 8, 8, ItemInit.ARCANIST_INK.get()));
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 1, 31, 8, Items.LAPIS_LAZULI));
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 2, 8, 31, ItemInit.ROTE_BOOK.get()));
        this.m_38897_(new ItemFilterSlot(this.tileItemHandler, 3, 80, 31, new SpellItemFilter()).setMaxStackSize(1));
        return 4;
    }

    public int getInkRequired() {
        return this.tile.getInkRequired();
    }

    public int getLapisRequired(Player player) {
        return this.tile.getLapisRequired(player);
    }

    @Override
    protected int playerInventoryXStart() {
        return 8;
    }

    @Override
    protected int playerInventoryYStart() {
        return 77;
    }
}