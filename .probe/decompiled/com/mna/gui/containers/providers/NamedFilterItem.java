package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerFilterItem;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NamedFilterItem implements MenuProvider {

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerFilterItem(i, inventory, (FriendlyByteBuf) null);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}