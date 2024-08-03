package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SlotLectern extends Slot {

    public SlotLectern(Container inv, int slotIndex, int xPosition, int yPosition) {
        super(inv, slotIndex, xPosition, yPosition);
    }

    @Override
    public void setChanged() {
        this.f_40218_.setChanged();
    }

    @Override
    public void onTake(@NotNull Player playerIn, @NotNull ItemStack stack) {
        this.checkTakeAchievements(stack);
        super.onTake(playerIn, stack);
    }

    @Override
    protected void onQuickCraft(@NotNull ItemStack stack, int amount) {
        this.checkTakeAchievements(stack);
    }

    @Override
    protected void checkTakeAchievements(@NotNull ItemStack stack) {
    }
}