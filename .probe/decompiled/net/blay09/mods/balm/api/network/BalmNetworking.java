package net.blay09.mods.balm.api.network;

import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;

public interface BalmNetworking {

    void openGui(Player var1, MenuProvider var2);

    default void allowClientAndServerOnly(String modId) {
        this.allowClientOnly(modId);
        this.allowServerOnly(modId);
    }

    void allowClientOnly(String var1);

    void allowServerOnly(String var1);

    <T> void reply(T var1);

    <T> void sendTo(Player var1, T var2);

    <T> void sendToTracking(ServerLevel var1, BlockPos var2, T var3);

    <T> void sendToTracking(Entity var1, T var2);

    <T> void sendToAll(MinecraftServer var1, T var2);

    <T> void sendToServer(T var1);

    <T> void registerClientboundPacket(ResourceLocation var1, Class<T> var2, BiConsumer<T, FriendlyByteBuf> var3, Function<FriendlyByteBuf, T> var4, BiConsumer<Player, T> var5);

    <T> void registerServerboundPacket(ResourceLocation var1, Class<T> var2, BiConsumer<T, FriendlyByteBuf> var3, Function<FriendlyByteBuf, T> var4, BiConsumer<ServerPlayer, T> var5);
}