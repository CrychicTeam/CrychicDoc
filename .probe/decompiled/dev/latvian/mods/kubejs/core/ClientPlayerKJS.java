package dev.latvian.mods.kubejs.core;

import dev.latvian.mods.kubejs.KubeJS;
import dev.latvian.mods.kubejs.net.SendDataFromClientMessage;
import dev.latvian.mods.kubejs.player.PlayerStatsJS;
import dev.latvian.mods.kubejs.util.NotificationBuilder;
import dev.latvian.mods.rhino.util.RemapPrefixForJS;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.Nullable;

@RemapPrefixForJS("kjs$")
public interface ClientPlayerKJS extends PlayerKJS {

    default AbstractClientPlayer kjs$self() {
        return (AbstractClientPlayer) this;
    }

    default boolean isSelf() {
        return this.kjs$self() == KubeJS.PROXY.getClientPlayer();
    }

    @Override
    default void kjs$sendData(String channel, @Nullable CompoundTag data) {
        if (!channel.isEmpty()) {
            new SendDataFromClientMessage(channel, data).sendToServer();
        }
    }

    @Override
    default void kjs$paint(CompoundTag tag) {
        if (this.isSelf()) {
            KubeJS.PROXY.paint(tag);
        }
    }

    @Override
    default PlayerStatsJS kjs$getStats() {
        if (!this.isSelf()) {
            throw new IllegalStateException("Can't access other client player stats!");
        } else {
            return new PlayerStatsJS(this.kjs$self(), ((LocalPlayer) this.kjs$self()).getStats());
        }
    }

    @Override
    default boolean kjs$isMiningBlock() {
        return this.isSelf() && Minecraft.getInstance().gameMode.isDestroying();
    }

    @Override
    default void kjs$notify(NotificationBuilder notification) {
        notification.show();
    }
}