package com.mna.gui.containers.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.StonecutterMenu;

public class ContainerSpectralStonecutter extends StonecutterMenu {

    public ContainerSpectralStonecutter(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public ContainerSpectralStonecutter(int id, Inventory playerInventory, ContainerLevelAccess p_i50090_3_) {
        super(id, playerInventory, p_i50090_3_);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }
}