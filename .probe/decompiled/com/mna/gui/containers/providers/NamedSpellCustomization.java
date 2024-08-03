package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerSpellName;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NamedSpellCustomization implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerSpellName(i, inventory, null);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}