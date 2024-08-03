package net.blay09.mods.balm.api.client.screen;

import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;

@FunctionalInterface
public interface BalmScreenFactory<T, S> {

    S create(T var1, Inventory var2, Component var3);
}