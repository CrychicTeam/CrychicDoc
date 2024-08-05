package dev.architectury.networking;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.architectury.networking.forge.NetworkManagerImpl;
import dev.architectury.networking.transformers.PacketCollector;
import dev.architectury.networking.transformers.PacketSink;
import dev.architectury.networking.transformers.PacketTransformer;
import dev.architectury.networking.transformers.SinglePacketCollector;
import dev.architectury.utils.Env;
import java.util.Collections;
import java.util.List;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.ApiStatus.Experimental;
import org.jetbrains.annotations.ApiStatus.ScheduledForRemoval;

public final class NetworkManager {

    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, NetworkManager.NetworkReceiver receiver) {
        registerReceiver(side, id, Collections.emptyList(), receiver);
    }

    @ExpectPlatform
    @Experimental
    @Transformed
    public static void registerReceiver(NetworkManager.Side side, ResourceLocation id, List<PacketTransformer> packetTransformers, NetworkManager.NetworkReceiver receiver) {
        NetworkManagerImpl.registerReceiver(side, id, packetTransformers, receiver);
    }

    @Deprecated
    @ScheduledForRemoval
    public static Packet<?> toPacket(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        SinglePacketCollector sink = new SinglePacketCollector(null);
        collectPackets(sink, side, id, buf);
        return sink.getPacket();
    }

    @Deprecated
    @ScheduledForRemoval
    public static List<Packet<?>> toPackets(NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        PacketCollector sink = new PacketCollector(null);
        collectPackets(sink, side, id, buf);
        return sink.collect();
    }

    @ExpectPlatform
    @Transformed
    public static void collectPackets(PacketSink sink, NetworkManager.Side side, ResourceLocation id, FriendlyByteBuf buf) {
        NetworkManagerImpl.collectPackets(sink, side, id, buf);
    }

    public static void sendToPlayer(ServerPlayer player, ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayer(player), serverToClient(), id, buf);
    }

    public static void sendToPlayers(Iterable<ServerPlayer> players, ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.ofPlayers(players), serverToClient(), id, buf);
    }

    @OnlyIn(Dist.CLIENT)
    public static void sendToServer(ResourceLocation id, FriendlyByteBuf buf) {
        collectPackets(PacketSink.client(), clientToServer(), id, buf);
    }

    @OnlyIn(Dist.CLIENT)
    @ExpectPlatform
    @Transformed
    public static boolean canServerReceive(ResourceLocation id) {
        return NetworkManagerImpl.canServerReceive(id);
    }

    @ExpectPlatform
    @Transformed
    public static boolean canPlayerReceive(ServerPlayer player, ResourceLocation id) {
        return NetworkManagerImpl.canPlayerReceive(player, id);
    }

    @ExpectPlatform
    @Transformed
    public static Packet<ClientGamePacketListener> createAddEntityPacket(Entity entity) {
        return NetworkManagerImpl.createAddEntityPacket(entity);
    }

    public static NetworkManager.Side s2c() {
        return NetworkManager.Side.S2C;
    }

    public static NetworkManager.Side c2s() {
        return NetworkManager.Side.C2S;
    }

    public static NetworkManager.Side serverToClient() {
        return NetworkManager.Side.S2C;
    }

    public static NetworkManager.Side clientToServer() {
        return NetworkManager.Side.C2S;
    }

    @FunctionalInterface
    public interface NetworkReceiver {

        void receive(FriendlyByteBuf var1, NetworkManager.PacketContext var2);
    }

    public interface PacketContext {

        Player getPlayer();

        void queue(Runnable var1);

        Env getEnvironment();

        default Dist getEnv() {
            return this.getEnvironment().toPlatform();
        }
    }

    public static enum Side {

        S2C, C2S
    }
}