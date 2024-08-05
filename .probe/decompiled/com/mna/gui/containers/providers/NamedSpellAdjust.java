package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerSpellAdjustments;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NamedSpellAdjust implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerSpellAdjustments(i, inventory);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}