package net.blay09.mods.waystones.menu;

import net.blay09.mods.waystones.api.IWaystone;
import net.blay09.mods.waystones.block.entity.WarpPlateBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class WarpPlateContainer extends AbstractContainerMenu {

    private final WarpPlateBlockEntity blockEntity;

    private final ContainerData containerData;

    public WarpPlateContainer(int windowId, WarpPlateBlockEntity warpPlate, ContainerData containerData, Inventory playerInventory) {
        super(ModMenus.warpPlate.get(), windowId);
        this.blockEntity = warpPlate;
        this.containerData = containerData;
        warpPlate.markReadyForAttunement();
        m_38886_(containerData, 1);
        this.m_38897_(new WarpPlateAttunementSlot(warpPlate, 0, 80, 45));
        this.m_38897_(new WarpPlateAttunementSlot(warpPlate, 1, 80, 17));
        this.m_38897_(new WarpPlateAttunementSlot(warpPlate, 2, 108, 45));
        this.m_38897_(new WarpPlateAttunementSlot(warpPlate, 3, 80, 73));
        this.m_38897_(new WarpPlateAttunementSlot(warpPlate, 4, 52, 45));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 104 + i * 18));
            }
        }
        for (int j = 0; j < 9; j++) {
            this.m_38897_(new Slot(playerInventory, j, 8 + j * 18, 162));
        }
        this.m_38884_(containerData);
    }

    @Override
    public boolean stillValid(Player player) {
        BlockPos pos = this.blockEntity.m_58899_();
        return player.m_20275_((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5) <= 64.0;
    }

    public float getAttunementProgress() {
        return (float) this.containerData.get(0) / (float) this.blockEntity.getMaxAttunementTicks();
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index < 5) {
                if (!this.m_38903_(slotStack, 5, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38853_(0).hasItem()) {
                if (!this.m_38903_(slotStack.split(1), 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 1, 5, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemStack;
    }

    public IWaystone getWaystone() {
        return this.blockEntity.getWaystone();
    }
}