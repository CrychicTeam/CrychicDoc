package com.mna.recipes;

import com.google.common.collect.ImmutableList;
import java.util.Collection;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ItemAndPatternCraftingInventory extends TransientCraftingContainer {

    private ImmutableList<String> addedPatterns;

    public ItemAndPatternCraftingInventory(int size, Collection<String> patterns) {
        super(new AbstractContainerMenu((MenuType) null, -1) {

            @Override
            public boolean stillValid(Player playerIn) {
                return false;
            }

            @Override
            public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
                return ItemStack.EMPTY;
            }
        }, size, 1);
        this.addedPatterns = ImmutableList.copyOf(patterns);
    }

    public ImmutableList<String> getPatterns() {
        return this.addedPatterns;
    }
}