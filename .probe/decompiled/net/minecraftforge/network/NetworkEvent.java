package net.minecraftforge.network;

import io.netty.util.Attribute;
import io.netty.util.AttributeKey;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Supplier;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.eventbus.api.Event;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;

public class NetworkEvent extends Event {

    private final FriendlyByteBuf payload;

    private final Supplier<NetworkEvent.Context> source;

    private final int loginIndex;

    private NetworkEvent(ICustomPacket<?> payload, Supplier<NetworkEvent.Context> source) {
        this.payload = payload.getInternalData();
        this.source = source;
        this.loginIndex = payload.getIndex();
    }

    private NetworkEvent(FriendlyByteBuf payload, Supplier<NetworkEvent.Context> source, int loginIndex) {
        this.payload = payload;
        this.source = source;
        this.loginIndex = loginIndex;
    }

    public NetworkEvent(Supplier<NetworkEvent.Context> source) {
        this.source = source;
        this.payload = null;
        this.loginIndex = -1;
    }

    public FriendlyByteBuf getPayload() {
        return this.payload;
    }

    public Supplier<NetworkEvent.Context> getSource() {
        return this.source;
    }

    public int getLoginIndex() {
        return this.loginIndex;
    }

    public static class ChannelRegistrationChangeEvent extends NetworkEvent {

        private final NetworkEvent.RegistrationChangeType changeType;

        ChannelRegistrationChangeEvent(Supplier<NetworkEvent.Context> source, NetworkEvent.RegistrationChangeType changeType) {
            super(source);
            this.changeType = changeType;
        }

        public NetworkEvent.RegistrationChangeType getRegistrationChangeType() {
            return this.changeType;
        }
    }

    public static class ClientCustomPayloadEvent extends NetworkEvent {

        ClientCustomPayloadEvent(ICustomPacket<?> payload, Supplier<NetworkEvent.Context> source) {
            super(payload, source);
        }
    }

    public static class ClientCustomPayloadLoginEvent extends NetworkEvent.ClientCustomPayloadEvent {

        ClientCustomPayloadLoginEvent(ICustomPacket<?> payload, Supplier<NetworkEvent.Context> source) {
            super(payload, source);
        }
    }

    public static class Context {

        private final Connection networkManager;

        private final NetworkDirection networkDirection;

        private final NetworkEvent.PacketDispatcher packetDispatcher;

        private boolean packetHandled;

        Context(Connection netHandler, NetworkDirection networkDirection, int index) {
            this(netHandler, networkDirection, new NetworkEvent.PacketDispatcher.NetworkManagerDispatcher(netHandler, index, networkDirection.reply()::buildPacket));
        }

        Context(Connection networkManager, NetworkDirection networkDirection, BiConsumer<ResourceLocation, FriendlyByteBuf> packetSink) {
            this(networkManager, networkDirection, new NetworkEvent.PacketDispatcher(packetSink));
        }

        Context(Connection networkManager, NetworkDirection networkDirection, NetworkEvent.PacketDispatcher dispatcher) {
            this.networkManager = networkManager;
            this.networkDirection = networkDirection;
            this.packetDispatcher = dispatcher;
        }

        public NetworkDirection getDirection() {
            return this.networkDirection;
        }

        public NetworkEvent.PacketDispatcher getPacketDispatcher() {
            return this.packetDispatcher;
        }

        public <T> Attribute<T> attr(AttributeKey<T> key) {
            return this.networkManager.channel().attr(key);
        }

        public void setPacketHandled(boolean packetHandled) {
            this.packetHandled = packetHandled;
        }

        public boolean getPacketHandled() {
            return this.packetHandled;
        }

        public CompletableFuture<Void> enqueueWork(Runnable runnable) {
            BlockableEventLoop<?> executor = LogicalSidedProvider.WORKQUEUE.get(this.getDirection().getReceptionSide());
            if (!executor.isSameThread()) {
                return executor.submitAsync(runnable);
            } else {
                runnable.run();
                return CompletableFuture.completedFuture(null);
            }
        }

        @Nullable
        public ServerPlayer getSender() {
            return this.networkManager.getPacketListener() instanceof ServerGamePacketListenerImpl netHandlerPlayServer ? netHandlerPlayServer.player : null;
        }

        public Connection getNetworkManager() {
            return this.networkManager;
        }
    }

    public static class GatherLoginPayloadsEvent extends Event {

        private final List<NetworkRegistry.LoginPayload> collected;

        private final boolean isLocal;

        public GatherLoginPayloadsEvent(List<NetworkRegistry.LoginPayload> loginPayloadList, boolean isLocal) {
            this.collected = loginPayloadList;
            this.isLocal = isLocal;
        }

        public void add(FriendlyByteBuf buffer, ResourceLocation channelName, String context) {
            this.collected.add(new NetworkRegistry.LoginPayload(buffer, channelName, context));
        }

        public void add(FriendlyByteBuf buffer, ResourceLocation channelName, String context, boolean needsResponse) {
            this.collected.add(new NetworkRegistry.LoginPayload(buffer, channelName, context, needsResponse));
        }

        public boolean isLocal() {
            return this.isLocal;
        }
    }

    public static class LoginPayloadEvent extends NetworkEvent {

        LoginPayloadEvent(FriendlyByteBuf payload, Supplier<NetworkEvent.Context> source, int loginIndex) {
            super(payload, source, loginIndex);
        }
    }

    public static class PacketDispatcher {

        BiConsumer<ResourceLocation, FriendlyByteBuf> packetSink;

        PacketDispatcher(BiConsumer<ResourceLocation, FriendlyByteBuf> packetSink) {
            this.packetSink = packetSink;
        }

        private PacketDispatcher() {
        }

        public void sendPacket(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
            this.packetSink.accept(resourceLocation, buffer);
        }

        static class NetworkManagerDispatcher extends NetworkEvent.PacketDispatcher {

            private final Connection manager;

            private final int packetIndex;

            private final BiFunction<Pair<FriendlyByteBuf, Integer>, ResourceLocation, ICustomPacket<?>> customPacketSupplier;

            NetworkManagerDispatcher(Connection manager, int packetIndex, BiFunction<Pair<FriendlyByteBuf, Integer>, ResourceLocation, ICustomPacket<?>> customPacketSupplier) {
                this.packetSink = this::dispatchPacket;
                this.manager = manager;
                this.packetIndex = packetIndex;
                this.customPacketSupplier = customPacketSupplier;
            }

            private void dispatchPacket(ResourceLocation resourceLocation, FriendlyByteBuf buffer) {
                ICustomPacket<?> packet = (ICustomPacket<?>) this.customPacketSupplier.apply(Pair.of(buffer, this.packetIndex), resourceLocation);
                this.manager.send(packet.getThis());
            }
        }
    }

    public static enum RegistrationChangeType {

        REGISTER, UNREGISTER
    }

    public static class ServerCustomPayloadEvent extends NetworkEvent {

        ServerCustomPayloadEvent(ICustomPacket<?> payload, Supplier<NetworkEvent.Context> source) {
            super(payload, source);
        }
    }

    public static class ServerCustomPayloadLoginEvent extends NetworkEvent.ServerCustomPayloadEvent {

        ServerCustomPayloadLoginEvent(ICustomPacket<?> payload, Supplier<NetworkEvent.Context> source) {
            super(payload, source);
        }
    }
}