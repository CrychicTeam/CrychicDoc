package com.mna.tools;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;

public class ContainerTools {

    public static CraftingContainer createTemporaryContainer(ItemStack item) {
        return createTemporaryContainer(1, 1, NonNullList.of(item, item));
    }

    public static CraftingContainer createTemporaryContainer(int width, int height, NonNullList<ItemStack> items) {
        return new TransientCraftingContainer(new ContainerTools.TemporaryMenu(), width, height, items);
    }

    public static class TemporaryMenu extends AbstractContainerMenu {

        public TemporaryMenu() {
            super((MenuType<?>) null, -1);
        }

        @Override
        public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
            return ItemStack.EMPTY;
        }

        @Override
        public boolean stillValid(Player pPlayer) {
            return false;
        }
    }
}