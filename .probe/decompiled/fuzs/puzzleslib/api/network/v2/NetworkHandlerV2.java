package fuzs.puzzleslib.api.network.v2;

import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.puzzleslib.impl.core.ModContext;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public interface NetworkHandlerV2 {

    static NetworkHandlerV2 build(String modId) {
        return build(modId, null);
    }

    static NetworkHandlerV2 build(String modId, @Nullable String context) {
        return build(modId, context, false, false);
    }

    static NetworkHandlerV2 build(String modId, boolean clientAcceptsVanillaOrMissing, boolean serverAcceptsVanillaOrMissing) {
        return build(modId, null, clientAcceptsVanillaOrMissing, serverAcceptsVanillaOrMissing);
    }

    static NetworkHandlerV2 build(String modId, @Nullable String context, boolean clientAcceptsVanillaOrMissing, boolean serverAcceptsVanillaOrMissing) {
        return ModContext.get(modId).getNetworkHandlerV2(context, clientAcceptsVanillaOrMissing, serverAcceptsVanillaOrMissing);
    }

    @Deprecated(forRemoval = true)
    <T extends MessageV2<T>> void register(Class<? extends T> var1, Supplier<T> var2, MessageDirection var3);

    <T extends MessageV2<T>> NetworkHandlerV2 registerClientbound(Class<T> var1);

    <T extends MessageV2<T>> NetworkHandlerV2 registerServerbound(Class<T> var1);

    Packet<ServerGamePacketListener> toServerboundPacket(MessageV2<?> var1);

    Packet<ClientGamePacketListener> toClientboundPacket(MessageV2<?> var1);

    default void sendToServer(MessageV2<?> message) {
        Proxy.INSTANCE.getClientPacketListener().send(this.toServerboundPacket(message));
    }

    default void sendTo(MessageV2<?> message, ServerPlayer player) {
        player.connection.send(this.toClientboundPacket(message));
    }

    default void sendToAll(MessageV2<?> message) {
        CommonAbstractions.INSTANCE.getMinecraftServer().getPlayerList().broadcastAll(this.toClientboundPacket(message));
    }

    default void sendToAllExcept(MessageV2<?> message, ServerPlayer exclude) {
        for (ServerPlayer player : CommonAbstractions.INSTANCE.getMinecraftServer().getPlayerList().getPlayers()) {
            if (player != exclude) {
                this.sendTo(message, player);
            }
        }
    }

    default void sendToAllNear(MessageV2<?> message, BlockPos pos, Level level) {
        this.sendToAllNearExcept(message, null, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), 64.0, level);
    }

    default void sendToAllNear(MessageV2<?> message, double posX, double posY, double posZ, double distance, Level level) {
        this.sendToAllNearExcept(message, null, posX, posY, posZ, 64.0, level);
    }

    default void sendToAllNearExcept(MessageV2<?> message, @Nullable ServerPlayer exclude, double posX, double posY, double posZ, double distance, Level level) {
        CommonAbstractions.INSTANCE.getMinecraftServer().getPlayerList().broadcast(exclude, posX, posY, posZ, distance, level.dimension(), this.toClientboundPacket(message));
    }

    default void sendToAllTracking(MessageV2<?> message, Entity entity) {
        ((ServerChunkCache) entity.getCommandSenderWorld().m_7726_()).broadcast(entity, this.toClientboundPacket(message));
    }

    default void sendToAllTrackingAndSelf(MessageV2<?> message, Entity entity) {
        ((ServerChunkCache) entity.getCommandSenderWorld().m_7726_()).broadcastAndSend(entity, this.toClientboundPacket(message));
    }

    default void sendToDimension(MessageV2<?> message, Level level) {
        this.sendToDimension(message, level.dimension());
    }

    default void sendToDimension(MessageV2<?> message, ResourceKey<Level> dimension) {
        CommonAbstractions.INSTANCE.getMinecraftServer().getPlayerList().broadcastAll(this.toClientboundPacket(message), dimension);
    }
}