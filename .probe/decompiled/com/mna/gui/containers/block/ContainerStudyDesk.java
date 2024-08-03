package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.StudyDeskTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.items.filters.ThesisItemFilter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;

public class ContainerStudyDesk extends SimpleWizardLabContainer<ContainerStudyDesk, StudyDeskTile> {

    public ContainerStudyDesk(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        super(ContainerInit.STUDY_DESK.get(), i, playerInventory, packetBuffer);
    }

    public ContainerStudyDesk(int id, Inventory playerInventory, StudyDeskTile tile) {
        super(ContainerInit.STUDY_DESK.get(), id, playerInventory, tile);
    }

    @Override
    protected int addInventorySlots() {
        this.m_38897_(new ItemFilterSlot(this.tileItemHandler, 0, 62, 49, new ThesisItemFilter()));
        return 1;
    }

    @Override
    protected int playerInventoryXStart() {
        return 8;
    }

    @Override
    protected int playerInventoryYStart() {
        return 72;
    }

    public StudyDeskTile.CantStartReason getCantStartReason(Player player) {
        return this.tile.getCantStartReason(player);
    }
}