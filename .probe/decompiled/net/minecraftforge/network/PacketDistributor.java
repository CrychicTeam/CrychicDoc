package net.minecraftforge.network;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.Packet;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraftforge.server.ServerLifecycleHooks;

public class PacketDistributor<T> {

    public static final PacketDistributor<ServerPlayer> PLAYER = new PacketDistributor<>(PacketDistributor::playerConsumer, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<ResourceKey<Level>> DIMENSION = new PacketDistributor<>(PacketDistributor::playerListDimConsumer, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<PacketDistributor.TargetPoint> NEAR = new PacketDistributor<>(PacketDistributor::playerListPointConsumer, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<Void> ALL = new PacketDistributor<>(PacketDistributor::playerListAll, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<Void> SERVER = new PacketDistributor<>(PacketDistributor::clientToServer, NetworkDirection.PLAY_TO_SERVER);

    public static final PacketDistributor<Entity> TRACKING_ENTITY = new PacketDistributor<>(PacketDistributor::trackingEntity, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<Entity> TRACKING_ENTITY_AND_SELF = new PacketDistributor<>(PacketDistributor::trackingEntityAndSelf, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<LevelChunk> TRACKING_CHUNK = new PacketDistributor<>(PacketDistributor::trackingChunk, NetworkDirection.PLAY_TO_CLIENT);

    public static final PacketDistributor<List<Connection>> NMLIST = new PacketDistributor<>(PacketDistributor::networkManagerList, NetworkDirection.PLAY_TO_CLIENT);

    private final BiFunction<PacketDistributor<T>, Supplier<T>, Consumer<Packet<?>>> functor;

    private final NetworkDirection direction;

    public PacketDistributor(BiFunction<PacketDistributor<T>, Supplier<T>, Consumer<Packet<?>>> functor, NetworkDirection direction) {
        this.functor = functor;
        this.direction = direction;
    }

    public PacketDistributor.PacketTarget with(Supplier<T> input) {
        return new PacketDistributor.PacketTarget((Consumer<Packet<?>>) this.functor.apply(this, input), this);
    }

    public PacketDistributor.PacketTarget noArg() {
        return new PacketDistributor.PacketTarget((Consumer<Packet<?>>) this.functor.apply(this, (Supplier) () -> null), this);
    }

    private Consumer<Packet<?>> playerConsumer(Supplier<ServerPlayer> entityPlayerMPSupplier) {
        return p -> ((ServerPlayer) entityPlayerMPSupplier.get()).connection.connection.send(p);
    }

    private Consumer<Packet<?>> playerListDimConsumer(Supplier<ResourceKey<Level>> dimensionTypeSupplier) {
        return p -> this.getServer().getPlayerList().broadcastAll(p, (ResourceKey<Level>) dimensionTypeSupplier.get());
    }

    private Consumer<Packet<?>> playerListAll(Supplier<Void> voidSupplier) {
        return p -> this.getServer().getPlayerList().broadcastAll(p);
    }

    private Consumer<Packet<?>> clientToServer(Supplier<Void> voidSupplier) {
        return p -> Minecraft.getInstance().getConnection().send(p);
    }

    private Consumer<Packet<?>> playerListPointConsumer(Supplier<PacketDistributor.TargetPoint> targetPointSupplier) {
        return p -> {
            PacketDistributor.TargetPoint tp = (PacketDistributor.TargetPoint) targetPointSupplier.get();
            this.getServer().getPlayerList().broadcast(tp.excluded, tp.x, tp.y, tp.z, tp.r2, tp.dim, p);
        };
    }

    private Consumer<Packet<?>> trackingEntity(Supplier<Entity> entitySupplier) {
        return p -> {
            Entity entity = (Entity) entitySupplier.get();
            ((ServerChunkCache) entity.getCommandSenderWorld().m_7726_()).broadcast(entity, p);
        };
    }

    private Consumer<Packet<?>> trackingEntityAndSelf(Supplier<Entity> entitySupplier) {
        return p -> {
            Entity entity = (Entity) entitySupplier.get();
            ((ServerChunkCache) entity.getCommandSenderWorld().m_7726_()).broadcastAndSend(entity, p);
        };
    }

    private Consumer<Packet<?>> trackingChunk(Supplier<LevelChunk> chunkPosSupplier) {
        return p -> {
            LevelChunk chunk = (LevelChunk) chunkPosSupplier.get();
            ((ServerChunkCache) chunk.getLevel().m_7726_()).chunkMap.getPlayers(chunk.m_7697_(), false).forEach(e -> e.connection.send(p));
        };
    }

    private Consumer<Packet<?>> networkManagerList(Supplier<List<Connection>> nmListSupplier) {
        return p -> ((List) nmListSupplier.get()).forEach(nm -> nm.send(p));
    }

    private MinecraftServer getServer() {
        return ServerLifecycleHooks.getCurrentServer();
    }

    public static class PacketTarget {

        private final Consumer<Packet<?>> packetConsumer;

        private final PacketDistributor<?> distributor;

        PacketTarget(Consumer<Packet<?>> packetConsumer, PacketDistributor<?> distributor) {
            this.packetConsumer = packetConsumer;
            this.distributor = distributor;
        }

        public void send(Packet<?> packet) {
            this.packetConsumer.accept(packet);
        }

        public NetworkDirection getDirection() {
            return this.distributor.direction;
        }
    }

    public static final class TargetPoint {

        private final ServerPlayer excluded;

        private final double x;

        private final double y;

        private final double z;

        private final double r2;

        private final ResourceKey<Level> dim;

        public TargetPoint(ServerPlayer excluded, double x, double y, double z, double r2, ResourceKey<Level> dim) {
            this.excluded = excluded;
            this.x = x;
            this.y = y;
            this.z = z;
            this.r2 = r2;
            this.dim = dim;
        }

        public TargetPoint(double x, double y, double z, double r2, ResourceKey<Level> dim) {
            this.excluded = null;
            this.x = x;
            this.y = y;
            this.z = z;
            this.r2 = r2;
            this.dim = dim;
        }

        public static Supplier<PacketDistributor.TargetPoint> p(double x, double y, double z, double r2, ResourceKey<Level> dim) {
            PacketDistributor.TargetPoint tp = new PacketDistributor.TargetPoint(x, y, z, r2, dim);
            return () -> tp;
        }
    }
}