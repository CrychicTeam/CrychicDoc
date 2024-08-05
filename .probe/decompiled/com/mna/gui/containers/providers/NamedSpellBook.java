package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerSpellBook;
import com.mna.inventory.InventorySpellBook;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NamedSpellBook implements MenuProvider {

    private final ItemStack stack;

    public NamedSpellBook(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerSpellBook(i, inventory, new InventorySpellBook(this.stack));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}