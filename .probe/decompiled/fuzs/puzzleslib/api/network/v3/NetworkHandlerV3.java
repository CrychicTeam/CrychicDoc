package fuzs.puzzleslib.api.network.v3;

import com.google.common.base.Preconditions;
import fuzs.puzzleslib.api.core.v1.Buildable;
import fuzs.puzzleslib.api.core.v1.CommonAbstractions;
import fuzs.puzzleslib.api.core.v1.Proxy;
import fuzs.puzzleslib.api.network.v3.serialization.MessageSerializer;
import fuzs.puzzleslib.api.network.v3.serialization.MessageSerializers;
import fuzs.puzzleslib.impl.core.ModContext;
import fuzs.puzzleslib.impl.network.NetworkHandlerRegistry;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.Vec3i;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ServerGamePacketListener;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.chunk.LevelChunk;
import org.jetbrains.annotations.Nullable;

public interface NetworkHandlerV3 {

    static NetworkHandlerV3.Builder builder(String modId) {
        return builder(modId, null);
    }

    static NetworkHandlerV3.Builder builder(String modId, @Nullable String context) {
        return ModContext.get(modId).getNetworkHandlerV3$Builder(context);
    }

    <T extends Record & ClientboundMessage<T>> Packet<ClientGamePacketListener> toClientboundPacket(T var1);

    <T extends Record & ServerboundMessage<T>> Packet<ServerGamePacketListener> toServerboundPacket(T var1);

    default <T extends Record & ServerboundMessage<T>> void sendToServer(T message) {
        ClientPacketListener clientPacketListener = Proxy.INSTANCE.getClientPacketListener();
        Objects.requireNonNull(clientPacketListener, "client packet listener is null");
        clientPacketListener.send(this.toServerboundPacket(message));
    }

    default <T extends Record & ClientboundMessage<T>> void sendTo(ServerPlayer player, T message) {
        Objects.requireNonNull(player, "player is null");
        player.connection.send(this.toClientboundPacket(message));
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAll(MinecraftServer server, T message) {
        this.sendToAll(server, null, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAll(MinecraftServer server, @Nullable ServerPlayer exclude, T message) {
        Objects.requireNonNull(server, "server is null");
        this.sendToAll(server.getPlayerList().getPlayers(), exclude, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAll(Collection<ServerPlayer> playerList, @Nullable ServerPlayer exclude, T message) {
        Objects.requireNonNull(playerList, "player list is null");
        for (ServerPlayer player : playerList) {
            if (player != exclude) {
                this.sendTo(player, message);
            }
        }
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAll(ServerLevel level, T message) {
        Objects.requireNonNull(level, "level is null");
        for (ServerPlayer player : level.players()) {
            this.sendTo(player, message);
        }
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllNear(Vec3i pos, ServerLevel level, T message) {
        Objects.requireNonNull(pos, "pos is null");
        this.sendToAllNear((double) pos.getX(), (double) pos.getY(), (double) pos.getZ(), level, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllNear(double posX, double posY, double posZ, ServerLevel level, T message) {
        this.sendToAllNear(null, posX, posY, posZ, 64.0, level, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllNear(@Nullable ServerPlayer exclude, double posX, double posY, double posZ, double distance, ServerLevel level, T message) {
        Objects.requireNonNull(level, "level is null");
        level.getServer().getPlayerList().broadcast(exclude, posX, posY, posZ, distance, level.m_46472_(), this.toClientboundPacket(message));
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllTracking(BlockEntity blockEntity, T message) {
        Objects.requireNonNull(blockEntity, "block entity is null");
        Level level = blockEntity.getLevel();
        Objects.requireNonNull(level, "block entity level is null");
        Preconditions.checkState(!level.isClientSide, "block entity level is client level");
        this.sendToAllNear(blockEntity.getBlockPos(), (ServerLevel) level, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllTracking(LevelChunk chunk, T message) {
        Objects.requireNonNull(chunk, "chunk is null");
        Preconditions.checkState(!chunk.getLevel().isClientSide, "chunk level is client level");
        this.sendToAllTracking((ServerLevel) chunk.getLevel(), chunk.m_7697_(), message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllTracking(ServerLevel level, ChunkPos chunkPos, T message) {
        Objects.requireNonNull(level, "level is null");
        Objects.requireNonNull(chunkPos, "chunk pos is null");
        List<ServerPlayer> players = level.getChunkSource().chunkMap.getPlayers(chunkPos, false);
        this.sendToAll(players, null, message);
    }

    default <T extends Record & ClientboundMessage<T>> void sendToAllTracking(Entity entity, T message, boolean includeSelf) {
        Objects.requireNonNull(entity, "entity is null");
        Preconditions.checkState(!entity.getCommandSenderWorld().isClientSide, "entity level is client level");
        ServerChunkCache chunkSource = ((ServerLevel) entity.getCommandSenderWorld()).getChunkSource();
        if (includeSelf) {
            chunkSource.broadcastAndSend(entity, this.toClientboundPacket(message));
        } else {
            chunkSource.broadcast(entity, this.toClientboundPacket(message));
        }
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAll(T message) {
        this.sendToAll(CommonAbstractions.INSTANCE.getMinecraftServer(), message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAll(@Nullable ServerPlayer exclude, T message) {
        this.sendToAll(CommonAbstractions.INSTANCE.getMinecraftServer(), exclude, message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAllNear(BlockPos pos, Level level, T message) {
        Objects.requireNonNull(level, "level is null");
        Preconditions.checkState(!level.isClientSide, "level is client level");
        this.sendToAllNear(pos, (ServerLevel) level, message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAllNear(double posX, double posY, double posZ, double distance, Level level, T message) {
        Objects.requireNonNull(level, "level is null");
        Preconditions.checkState(!level.isClientSide, "level is client level");
        this.sendToAllNear(posX, posY, posZ, (ServerLevel) level, message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAllNearExcept(@Nullable ServerPlayer exclude, double posX, double posY, double posZ, double distance, Level level, T message) {
        Objects.requireNonNull(level, "level is null");
        Preconditions.checkState(!level.isClientSide, "level is client level");
        this.sendToAllNear(exclude, posX, posY, posZ, distance, (ServerLevel) level, message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAllTracking(Entity entity, T message) {
        this.sendToAllTracking(entity, message, false);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToAllTrackingAndSelf(Entity entity, T message) {
        this.sendToAllTracking(entity, message, true);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToDimension(Level level, T message) {
        Objects.requireNonNull(level, "level is null");
        Preconditions.checkState(!level.isClientSide, "level is client level");
        this.sendToAll((ServerLevel) level, message);
    }

    @Deprecated(forRemoval = true)
    default <T extends Record & ClientboundMessage<T>> void sendToDimension(ResourceKey<Level> resourceKey, T message) {
        Objects.requireNonNull(resourceKey, "resource key is null");
        ServerLevel level = CommonAbstractions.INSTANCE.getMinecraftServer().getLevel(resourceKey);
        Objects.requireNonNull(level, "level is null");
        this.sendToAll(level, message);
    }

    public interface Builder extends NetworkHandlerRegistry, Buildable {

        default <T> NetworkHandlerV3.Builder registerSerializer(Class<T> type, FriendlyByteBuf.Writer<T> writer, FriendlyByteBuf.Reader<T> reader) {
            MessageSerializers.registerSerializer(type, writer, reader);
            return this;
        }

        default <T> NetworkHandlerV3.Builder registerSerializer(Class<? super T> type, ResourceKey<Registry<T>> resourceKey) {
            MessageSerializers.registerSerializer(type, resourceKey);
            return this;
        }

        default <T> NetworkHandlerV3.Builder registerContainerProvider(Class<T> type, Function<Type[], MessageSerializer<? extends T>> factory) {
            MessageSerializers.registerContainerProvider(type, factory);
            return this;
        }

        <T extends Record & ClientboundMessage<T>> NetworkHandlerV3.Builder registerClientbound(Class<T> var1);

        <T extends Record & ServerboundMessage<T>> NetworkHandlerV3.Builder registerServerbound(Class<T> var1);

        NetworkHandlerV3.Builder clientAcceptsVanillaOrMissing();

        NetworkHandlerV3.Builder serverAcceptsVanillaOrMissing();

        default NetworkHandlerV3.Builder allAcceptVanillaOrMissing() {
            return this.clientAcceptsVanillaOrMissing().serverAcceptsVanillaOrMissing();
        }
    }
}