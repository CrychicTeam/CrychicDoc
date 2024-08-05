package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerPractitionersPouch;
import com.mna.inventory.InventoryRitualKit;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NamedRitualKit implements MenuProvider {

    private final ItemStack stack;

    public NamedRitualKit(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerPractitionersPouch(i, inventory, new InventoryRitualKit(this.stack));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}