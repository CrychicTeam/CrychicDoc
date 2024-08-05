package com.rekindled.embers.recipe;

import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class AlchemyContext implements Container {

    public ItemStack tablet;

    public List<IAlchemyRecipe.PedestalContents> contents;

    public long seed;

    public AlchemyContext(ItemStack tablet, List<IAlchemyRecipe.PedestalContents> contents, long seed) {
        this.tablet = tablet;
        this.contents = contents;
        this.seed = seed;
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