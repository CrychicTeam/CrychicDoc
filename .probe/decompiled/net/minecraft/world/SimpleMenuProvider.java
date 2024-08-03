package net.minecraft.world;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuConstructor;

public final class SimpleMenuProvider implements MenuProvider {

    private final Component title;

    private final MenuConstructor menuConstructor;

    public SimpleMenuProvider(MenuConstructor menuConstructor0, Component component1) {
        this.menuConstructor = menuConstructor0;
        this.title = component1;
    }

    @Override
    public Component getDisplayName() {
        return this.title;
    }

    @Override
    public AbstractContainerMenu createMenu(int int0, Inventory inventory1, Player player2) {
        return this.menuConstructor.createMenu(int0, inventory1, player2);
    }
}