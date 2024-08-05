package com.mna.gui.containers.block;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;

public class ContainerSpectralAnvil extends AnvilMenu {

    public ContainerSpectralAnvil(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(id, playerInventory, ContainerLevelAccess.NULL);
    }

    public ContainerSpectralAnvil(int id, Inventory playerInventory, ContainerLevelAccess p_i50090_3_) {
        super(id, playerInventory, p_i50090_3_);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }
}