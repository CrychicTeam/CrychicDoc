package dev.architectury.networking;

import com.google.common.collect.Maps;
import dev.architectury.platform.Platform;
import dev.architectury.utils.Env;
import io.netty.buffer.Unpooled;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public final class NetworkChannel {

    private final ResourceLocation id;

    private final Map<Class<?>, NetworkChannel.MessageInfo<?>> encoders = Maps.newHashMap();

    private NetworkChannel(ResourceLocation id) {
        this.id = id;
    }

    public static NetworkChannel create(ResourceLocation id) {
        return new NetworkChannel(id);
    }

    public <T> void register(Class<T> type, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkManager.PacketContext>> messageConsumer) {
        String s = UUID.nameUUIDFromBytes(type.getName().getBytes(StandardCharsets.UTF_8)).toString().replace("-", "");
        NetworkChannel.MessageInfo<T> info = new NetworkChannel.MessageInfo<>(new ResourceLocation(this.id + "/" + s), encoder, decoder, messageConsumer);
        this.encoders.put(type, info);
        NetworkManager.NetworkReceiver receiver = (buf, context) -> info.messageConsumer.accept(info.decoder.apply(buf), (Supplier) () -> context);
        NetworkManager.registerReceiver(NetworkManager.c2s(), info.packetId, receiver);
        if (Platform.getEnvironment() == Env.CLIENT) {
            NetworkManager.registerReceiver(NetworkManager.s2c(), info.packetId, receiver);
        }
    }

    public static long hashCodeString(String str) {
        long h = 0L;
        int length = str.length();
        for (int i = 0; i < length; i++) {
            h = 31L * h + (long) str.charAt(i);
        }
        return h;
    }

    public <T> Packet<?> toPacket(NetworkManager.Side side, T message) {
        NetworkChannel.MessageInfo<T> messageInfo = (NetworkChannel.MessageInfo<T>) Objects.requireNonNull((NetworkChannel.MessageInfo) this.encoders.get(message.getClass()), "Unknown message type! " + message);
        FriendlyByteBuf buf = new FriendlyByteBuf(Unpooled.buffer());
        messageInfo.encoder.accept(message, buf);
        return NetworkManager.toPacket(side, messageInfo.packetId, buf);
    }

    public <T> void sendToPlayer(ServerPlayer player, T message) {
        ((ServerPlayer) Objects.requireNonNull(player, "Unable to send packet to a 'null' player!")).connection.send(this.toPacket(NetworkManager.s2c(), message));
    }

    public <T> void sendToPlayers(Iterable<ServerPlayer> players, T message) {
        Packet<?> packet = this.toPacket(NetworkManager.s2c(), message);
        for (ServerPlayer player : players) {
            ((ServerPlayer) Objects.requireNonNull(player, "Unable to send packet to a 'null' player!")).connection.send(packet);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public <T> void sendToServer(T message) {
        if (Minecraft.getInstance().getConnection() != null) {
            Minecraft.getInstance().getConnection().send(this.toPacket(NetworkManager.c2s(), message));
        } else {
            throw new IllegalStateException("Unable to send packet to the server while not in game!");
        }
    }

    @OnlyIn(Dist.CLIENT)
    public <T> boolean canServerReceive(Class<T> type) {
        return NetworkManager.canServerReceive(((NetworkChannel.MessageInfo) this.encoders.get(type)).packetId);
    }

    public <T> boolean canPlayerReceive(ServerPlayer player, Class<T> type) {
        return NetworkManager.canPlayerReceive(player, ((NetworkChannel.MessageInfo) this.encoders.get(type)).packetId);
    }

    private static record MessageInfo<T>(ResourceLocation packetId, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, BiConsumer<T, Supplier<NetworkManager.PacketContext>> messageConsumer) {
    }
}