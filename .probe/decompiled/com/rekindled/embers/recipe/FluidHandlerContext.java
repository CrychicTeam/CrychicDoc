package com.rekindled.embers.recipe;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.fluids.capability.IFluidHandler;

public class FluidHandlerContext implements Container {

    public IFluidHandler fluid;

    public FluidHandlerContext(IFluidHandler fluid) {
        this.fluid = fluid;
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