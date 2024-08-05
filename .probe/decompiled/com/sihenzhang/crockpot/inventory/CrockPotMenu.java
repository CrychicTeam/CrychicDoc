package com.sihenzhang.crockpot.inventory;

import com.sihenzhang.crockpot.block.entity.CrockPotBlockEntity;
import com.sihenzhang.crockpot.inventory.slot.SlotCrockPotOutput;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class CrockPotMenu extends AbstractContainerMenu {

    private final CrockPotBlockEntity blockEntity;

    public CrockPotMenu(int windowId, Inventory playerInventory, CrockPotBlockEntity blockEntity) {
        super(CrockPotMenuTypes.CROCK_POT_MENU_TYPE.get(), windowId);
        this.blockEntity = blockEntity;
        if (this.blockEntity != null) {
            blockEntity.startOpen(playerInventory.player);
            ItemStackHandler itemHandler = this.blockEntity.getItemHandler();
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    this.m_38897_(new SlotItemHandler(itemHandler, j + i * 2, 39 + j * 18, 17 + i * 18));
                }
            }
            this.m_38897_(new SlotItemHandler(itemHandler, 4, 48, 71));
            this.m_38897_(new SlotCrockPotOutput(itemHandler, 5, 117, 44));
        }
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 102 + i * 18));
            }
        }
        for (int i = 0; i < 9; i++) {
            this.m_38897_(new Slot(playerInventory, i, 8 + i * 18, 160));
        }
    }

    @Override
    public void removed(Player pPlayer) {
        super.removed(pPlayer);
        this.blockEntity.stopOpen(pPlayer);
    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return Container.stillValidBlockEntity(this.blockEntity, pPlayer);
    }

    public BlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public int getBurningProgress() {
        return (int) (13.0F * this.blockEntity.getBurningProgress());
    }

    public int getCookingProgress() {
        return (int) (24.0F * this.blockEntity.getCookingProgress());
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemStack = slotStack.copy();
            if (index == 5) {
                if (!this.m_38903_(slotStack, 6, 42, true)) {
                    return ItemStack.EMPTY;
                }
                slot.onQuickCraft(slotStack, itemStack);
            } else if (index >= 6) {
                if (this.blockEntity.isValidIngredient(slotStack)) {
                    if (!this.m_38903_(slotStack, 0, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (CrockPotBlockEntity.isFuel(slotStack)) {
                    if (!this.m_38903_(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 33) {
                    if (!this.m_38903_(slotStack, 33, 42, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index < 42 && !this.m_38903_(slotStack, 6, 33, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(slotStack, 6, 42, false)) {
                return ItemStack.EMPTY;
            }
            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
            if (slotStack.getCount() == itemStack.getCount()) {
                return ItemStack.EMPTY;
            }
            slot.onTake(playerIn, slotStack);
        }
        return itemStack;
    }
}