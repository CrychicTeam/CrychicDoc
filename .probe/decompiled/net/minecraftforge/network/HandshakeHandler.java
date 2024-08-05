package net.minecraftforge.network;

import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CancellationException;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.IntSupplier;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.Registry;
import net.minecraft.network.Connection;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LogMessageAdapter;
import net.minecraftforge.event.entity.player.PlayerNegotiationEvent;
import net.minecraftforge.registries.DataPackRegistriesHooks;
import net.minecraftforge.registries.ForgeRegistry;
import net.minecraftforge.registries.GameData;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.MarkerManager;

public class HandshakeHandler {

    static final Marker FMLHSMARKER = MarkerManager.getMarker("FMLHANDSHAKE").setParents(new Marker[] { NetworkConstants.NETWORK });

    private static final Logger LOGGER = LogManager.getLogger();

    private static final LoginWrapper loginWrapper = new LoginWrapper();

    private List<NetworkRegistry.LoginPayload> messageList;

    private List<Integer> sentMessages = new ArrayList();

    private final NetworkDirection direction;

    private final Connection manager;

    private int packetPosition;

    private Map<ResourceLocation, ForgeRegistry.Snapshot> registrySnapshots;

    private Set<ResourceLocation> registriesToReceive;

    private Map<ResourceLocation, String> registryHashes;

    private boolean negotiationStarted = false;

    private final List<Future<Void>> pendingFutures = new ArrayList();

    static void registerHandshake(Connection manager, NetworkDirection direction) {
        manager.channel().attr(NetworkConstants.FML_HANDSHAKE_HANDLER).compareAndSet(null, new HandshakeHandler(manager, direction));
    }

    static boolean tickLogin(Connection networkManager) {
        return ((HandshakeHandler) networkManager.channel().attr(NetworkConstants.FML_HANDSHAKE_HANDLER).get()).tickServer();
    }

    private HandshakeHandler(Connection networkManager, NetworkDirection side) {
        this.direction = side;
        this.manager = networkManager;
        if (networkManager.isMemoryConnection()) {
            this.messageList = NetworkRegistry.gatherLoginPayloads(this.direction, true);
            LOGGER.debug(FMLHSMARKER, "Starting local connection.");
        } else if (NetworkHooks.getConnectionType((Supplier<Connection>) (() -> this.manager)) == ConnectionType.VANILLA) {
            this.messageList = Collections.emptyList();
            LOGGER.debug(FMLHSMARKER, "Starting new vanilla impl connection.");
        } else {
            this.messageList = NetworkRegistry.gatherLoginPayloads(this.direction, false);
            LOGGER.debug(FMLHSMARKER, "Starting new modded impl connection. Found {} messages to dispatch.", this.messageList.size());
        }
    }

    public static <MSG extends IntSupplier> BiConsumer<MSG, Supplier<NetworkEvent.Context>> biConsumerFor(HandshakeHandler.HandshakeConsumer<MSG> consumer) {
        return (m, c) -> consumer.accept(getHandshake(c), (MSG) m, c);
    }

    public static <MSG extends IntSupplier> BiConsumer<MSG, Supplier<NetworkEvent.Context>> indexFirst(HandshakeHandler.HandshakeConsumer<MSG> next) {
        BiConsumer<MSG, Supplier<NetworkEvent.Context>> loginIndexedMessageSupplierBiConsumer = biConsumerFor(HandshakeHandler::handleIndexedMessage);
        return loginIndexedMessageSupplierBiConsumer.andThen(biConsumerFor(next));
    }

    private static HandshakeHandler getHandshake(Supplier<NetworkEvent.Context> contextSupplier) {
        return (HandshakeHandler) ((NetworkEvent.Context) contextSupplier.get()).attr(NetworkConstants.FML_HANDSHAKE_HANDLER).get();
    }

    void handleServerModListOnClient(HandshakeMessages.S2CModList serverModList, Supplier<NetworkEvent.Context> c) {
        LOGGER.debug(FMLHSMARKER, "Logging into server with mod list [{}]", String.join(", ", serverModList.getModList()));
        Map<ResourceLocation, String> mismatchedChannels = NetworkRegistry.validateClientChannels(serverModList.getChannels());
        ((NetworkEvent.Context) c.get()).setPacketHandled(true);
        NetworkHooks.appendConnectionData(((NetworkEvent.Context) c.get()).getNetworkManager(), (Map<String, Pair<String, String>>) serverModList.getModList().stream().collect(Collectors.toMap(Function.identity(), s -> Pair.of("", ""))), serverModList.getChannels());
        if (!mismatchedChannels.isEmpty()) {
            LOGGER.error(FMLHSMARKER, "Terminating connection with server, mismatched mod list");
            ((NetworkEvent.Context) c.get()).getNetworkManager().channel().attr(NetworkConstants.FML_MOD_MISMATCH_DATA).set(ConnectionData.ModMismatchData.channel(mismatchedChannels, NetworkHooks.getConnectionData(((NetworkEvent.Context) c.get()).getNetworkManager()), true));
            ((NetworkEvent.Context) c.get()).getNetworkManager().disconnect(Component.literal("Connection closed - mismatched mod channel list"));
        } else {
            List<String> missingDataPackRegistries = new ArrayList();
            Set<ResourceKey<? extends Registry<?>>> clientDataPackRegistries = DataPackRegistriesHooks.getSyncedCustomRegistries();
            for (ResourceKey<? extends Registry<?>> key : serverModList.getCustomDataPackRegistries()) {
                if (!clientDataPackRegistries.contains(key)) {
                    ResourceLocation location = key.location();
                    LOGGER.error(FMLHSMARKER, "Missing required datapack registry: {}", location);
                    missingDataPackRegistries.add(key.location().toString());
                }
            }
            if (!missingDataPackRegistries.isEmpty()) {
                ((NetworkEvent.Context) c.get()).getNetworkManager().disconnect(Component.translatable("fml.menu.multiplayer.missingdatapackregistries", String.join(", ", missingDataPackRegistries)));
            } else {
                NetworkConstants.handshakeChannel.reply(new HandshakeMessages.C2SModListReply(), (NetworkEvent.Context) c.get());
                LOGGER.debug(FMLHSMARKER, "Accepted server connection");
                ((NetworkEvent.Context) c.get()).getNetworkManager().channel().attr(NetworkConstants.FML_NETVERSION).set("FML3");
                this.registriesToReceive = new HashSet(serverModList.getRegistries());
                this.registrySnapshots = Maps.newHashMap();
                LOGGER.debug(ForgeRegistry.REGISTRIES, "Expecting {} registries: {}", new org.apache.logging.log4j.util.Supplier[] { () -> this.registriesToReceive.size(), () -> this.registriesToReceive });
            }
        }
    }

    void handleModData(HandshakeMessages.S2CModData serverModData, Supplier<NetworkEvent.Context> c) {
        ((NetworkEvent.Context) c.get()).getNetworkManager().channel().attr(NetworkConstants.FML_CONNECTION_DATA).set(new ConnectionData(serverModData.getMods(), new HashMap()));
        ((NetworkEvent.Context) c.get()).setPacketHandled(true);
    }

    <MSG extends IntSupplier> void handleIndexedMessage(MSG message, Supplier<NetworkEvent.Context> c) {
        LOGGER.debug(FMLHSMARKER, "Received client indexed reply {} of type {}", message.getAsInt(), message.getClass().getName());
        boolean removed = this.sentMessages.removeIf(i -> i == message.getAsInt());
        if (!removed) {
            LOGGER.error(FMLHSMARKER, "Recieved unexpected index {} in client reply", message.getAsInt());
        }
    }

    void handleClientModListOnServer(HandshakeMessages.C2SModListReply clientModList, Supplier<NetworkEvent.Context> c) {
        LOGGER.debug(FMLHSMARKER, "Received client connection with modlist [{}]", String.join(", ", clientModList.getModList()));
        Map<ResourceLocation, String> mismatchedChannels = NetworkRegistry.validateServerChannels(clientModList.getChannels());
        ((NetworkEvent.Context) c.get()).getNetworkManager().channel().attr(NetworkConstants.FML_CONNECTION_DATA).set(new ConnectionData((Map<String, Pair<String, String>>) clientModList.getModList().stream().collect(Collectors.toMap(Function.identity(), s -> Pair.of("", ""))), clientModList.getChannels()));
        ((NetworkEvent.Context) c.get()).setPacketHandled(true);
        if (!mismatchedChannels.isEmpty()) {
            LOGGER.error(FMLHSMARKER, "Terminating connection with client, mismatched mod list");
            NetworkConstants.handshakeChannel.reply(new HandshakeMessages.S2CChannelMismatchData(mismatchedChannels), (NetworkEvent.Context) c.get());
            ((NetworkEvent.Context) c.get()).getNetworkManager().disconnect(Component.literal("Connection closed - mismatched mod channel list"));
        } else {
            LOGGER.debug(FMLHSMARKER, "Accepted client connection mod list");
        }
    }

    void handleModMismatchData(HandshakeMessages.S2CChannelMismatchData modMismatchData, Supplier<NetworkEvent.Context> c) {
        if (!modMismatchData.getMismatchedChannelData().isEmpty()) {
            LOGGER.error(FMLHSMARKER, "Channels [{}] rejected their client side version number", modMismatchData.getMismatchedChannelData().keySet().stream().map(Object::toString).collect(Collectors.joining(",")));
            LOGGER.error(FMLHSMARKER, "Terminating connection with server, mismatched mod list");
            ((NetworkEvent.Context) c.get()).setPacketHandled(true);
            ((NetworkEvent.Context) c.get()).getNetworkManager().channel().attr(NetworkConstants.FML_MOD_MISMATCH_DATA).set(ConnectionData.ModMismatchData.channel(modMismatchData.getMismatchedChannelData(), NetworkHooks.getConnectionData(((NetworkEvent.Context) c.get()).getNetworkManager()), false));
            ((NetworkEvent.Context) c.get()).getNetworkManager().disconnect(Component.literal("Connection closed - mismatched mod channel list"));
        }
    }

    void handleRegistryMessage(HandshakeMessages.S2CRegistry registryPacket, Supplier<NetworkEvent.Context> contextSupplier) {
        LOGGER.debug(FMLHSMARKER, "Received registry packet for {}", registryPacket.getRegistryName());
        this.registriesToReceive.remove(registryPacket.getRegistryName());
        this.registrySnapshots.put(registryPacket.getRegistryName(), registryPacket.getSnapshot());
        boolean continueHandshake = true;
        if (this.registriesToReceive.isEmpty()) {
            continueHandshake = this.handleRegistryLoading(contextSupplier);
        }
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
        if (!continueHandshake) {
            LOGGER.error(FMLHSMARKER, "Connection closed, not continuing handshake");
        } else {
            NetworkConstants.handshakeChannel.reply(new HandshakeMessages.C2SAcknowledge(), (NetworkEvent.Context) contextSupplier.get());
        }
    }

    private boolean handleRegistryLoading(Supplier<NetworkEvent.Context> contextSupplier) {
        AtomicBoolean successfulConnection = new AtomicBoolean(false);
        AtomicReference<Multimap<ResourceLocation, ResourceLocation>> registryMismatches = new AtomicReference();
        CountDownLatch block = new CountDownLatch(1);
        ((NetworkEvent.Context) contextSupplier.get()).enqueueWork(() -> {
            LOGGER.debug(FMLHSMARKER, "Injecting registry snapshot from server.");
            Multimap<ResourceLocation, ResourceLocation> missingData = GameData.injectSnapshot(this.registrySnapshots, false, false);
            LOGGER.debug(FMLHSMARKER, "Snapshot injected.");
            if (!missingData.isEmpty()) {
                LOGGER.error(FMLHSMARKER, "Missing registry data for impl connection:\n{}", LogMessageAdapter.adapt(sb -> missingData.forEach((reg, entry) -> sb.append("\t").append(reg).append(": ").append(entry).append('\n'))));
            }
            successfulConnection.set(missingData.isEmpty());
            registryMismatches.set(missingData);
            block.countDown();
        });
        LOGGER.debug(FMLHSMARKER, "Waiting for registries to load.");
        try {
            block.await();
        } catch (InterruptedException var6) {
            Thread.interrupted();
        }
        if (successfulConnection.get()) {
            LOGGER.debug(FMLHSMARKER, "Registry load complete, continuing handshake.");
        } else {
            LOGGER.error(FMLHSMARKER, "Failed to load registry, closing connection.");
            this.manager.channel().attr(NetworkConstants.FML_MOD_MISMATCH_DATA).set(ConnectionData.ModMismatchData.registry((Multimap<ResourceLocation, ResourceLocation>) registryMismatches.get(), NetworkHooks.getConnectionData(((NetworkEvent.Context) contextSupplier.get()).getNetworkManager())));
            this.manager.disconnect(Component.literal("Failed to synchronize registry data from server, closing connection"));
        }
        return successfulConnection.get();
    }

    void handleClientAck(HandshakeMessages.C2SAcknowledge msg, Supplier<NetworkEvent.Context> contextSupplier) {
        LOGGER.debug(FMLHSMARKER, "Received acknowledgement from client");
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
    }

    void handleConfigSync(HandshakeMessages.S2CConfigData msg, Supplier<NetworkEvent.Context> contextSupplier) {
        LOGGER.debug(FMLHSMARKER, "Received config sync from server");
        ConfigSync.INSTANCE.receiveSyncedConfig(msg, contextSupplier);
        ((NetworkEvent.Context) contextSupplier.get()).setPacketHandled(true);
        NetworkConstants.handshakeChannel.reply(new HandshakeMessages.C2SAcknowledge(), (NetworkEvent.Context) contextSupplier.get());
    }

    public boolean tickServer() {
        if (!this.negotiationStarted) {
            GameProfile profile = ((ServerLoginPacketListenerImpl) this.manager.getPacketListener()).gameProfile;
            PlayerNegotiationEvent event = new PlayerNegotiationEvent(this.manager, profile, this.pendingFutures);
            MinecraftForge.EVENT_BUS.post(event);
            this.negotiationStarted = true;
        }
        if (this.packetPosition < this.messageList.size()) {
            NetworkRegistry.LoginPayload message = (NetworkRegistry.LoginPayload) this.messageList.get(this.packetPosition);
            LOGGER.debug(FMLHSMARKER, "Sending ticking packet info '{}' to '{}' sequence {}", message.getMessageContext(), message.getChannelName(), this.packetPosition);
            if (message.needsResponse()) {
                this.sentMessages.add(this.packetPosition);
            }
            loginWrapper.sendServerToClientLoginPacket(message.getChannelName(), message.getData(), this.packetPosition, this.manager);
            this.packetPosition++;
        }
        this.pendingFutures.removeIf(future -> {
            if (!future.isDone()) {
                return false;
            } else {
                try {
                    future.get();
                } catch (ExecutionException var2x) {
                    LOGGER.error("Error during negotiation", var2x.getCause());
                } catch (InterruptedException | CancellationException var3x) {
                }
                return true;
            }
        });
        if (this.sentMessages.isEmpty() && this.packetPosition >= this.messageList.size() - 1 && this.pendingFutures.isEmpty()) {
            this.manager.channel().attr(NetworkConstants.FML_HANDSHAKE_HANDLER).set(null);
            LOGGER.debug(FMLHSMARKER, "Handshake complete!");
            return true;
        } else {
            return false;
        }
    }

    public static boolean packetNeedsResponse(Connection mgr, int packetPosition) {
        HandshakeHandler handler = (HandshakeHandler) mgr.channel().attr(NetworkConstants.FML_HANDSHAKE_HANDLER).get();
        return handler != null ? handler.sentMessages.contains(packetPosition) : false;
    }

    @FunctionalInterface
    public interface HandshakeConsumer<MSG extends IntSupplier> {

        void accept(HandshakeHandler var1, MSG var2, Supplier<NetworkEvent.Context> var3);
    }
}