package com.rekindled.embers.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

public class BlockStateContext implements Container {

    public BlockState state;

    public BlockStateContext(BlockState state) {
        this.state = state;
    }

    @Deprecated
    @Override
    public void clearContent() {
    }

    @Deprecated
    @Override
    public int getContainerSize() {
        return 0;
    }

    @Deprecated
    @Override
    public boolean isEmpty() {
        return true;
    }

    @Deprecated
    @Override
    public ItemStack getItem(int pSlot) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public ItemStack removeItem(int pSlot, int pAmount) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public ItemStack removeItemNoUpdate(int pSlot) {
        return ItemStack.EMPTY;
    }

    @Deprecated
    @Override
    public void setItem(int pSlot, ItemStack pStack) {
    }

    @Deprecated
    @Override
    public void setChanged() {
    }

    @Deprecated
    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }
}