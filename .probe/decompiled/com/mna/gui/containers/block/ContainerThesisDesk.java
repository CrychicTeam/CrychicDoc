package com.mna.gui.containers.block;

import com.mna.api.affinity.Affinity;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.blocks.tileentities.wizard_lab.ISelectSpellComponents;
import com.mna.blocks.tileentities.wizard_lab.ThesisDeskTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.gui.containers.slots.SlotNoPlace;
import com.mna.items.ItemInit;
import com.mna.items.filters.VellumFilter;
import com.mna.network.ClientMessageDispatcher;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.level.Level;

public class ContainerThesisDesk extends SimpleWizardLabContainer<ContainerThesisDesk, ThesisDeskTile> implements ISelectSpellComponents {

    public ContainerThesisDesk(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.THESIS_DESK.get(), i, playerInventory, packetBuffer);
    }

    public ContainerThesisDesk(int id, Inventory playerInventory, ThesisDeskTile tile) {
        super(ContainerInit.THESIS_DESK.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        this.m_38897_(new ItemFilterSlot(this.tileItemHandler, 0, 159, 7, new VellumFilter()));
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 1, 133, 7, ItemInit.ARCANIST_INK.get()));
        this.m_38897_(new SingleItemSlot(this.tileItemHandler, 2, 107, 7, ItemInit.ROTE_BOOK.get()));
        this.m_38897_(new SlotNoPlace(this.tileItemHandler, 3, 229, 129));
        return 4;
    }

    @Override
    public void setSpellComponent(ISpellComponent part) {
        if (!this.tile.isActive()) {
            Level world = this.tile.m_58904_();
            if (world.isClientSide) {
                ClientMessageDispatcher.sendWizardLabSelectSpellComponentMessage(part.getRegistryName());
            }
            this.tile.setSpellComponent(part);
        }
    }

    @Override
    public ISpellComponent getSpellComponent() {
        return this.tile.getSpellComponent();
    }

    public Affinity getAffinity() {
        return this.tile.getSelectedAffinity();
    }

    @Override
    protected int playerInventoryXStart() {
        return 48;
    }

    @Override
    protected int playerInventoryYStart() {
        return 151;
    }
}