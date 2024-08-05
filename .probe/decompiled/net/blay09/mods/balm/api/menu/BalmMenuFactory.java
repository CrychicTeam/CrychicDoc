package net.blay09.mods.balm.api.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;

public interface BalmMenuFactory<T extends AbstractContainerMenu> {

    T create(int var1, Inventory var2, FriendlyByteBuf var3);
}