package net.minecraftforge.network;

import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.network.Connection;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.handshake.ClientIntentionPacket;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerLoginPacketListenerImpl;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.config.ConfigTracker;
import net.minecraftforge.fml.util.thread.EffectiveSide;
import net.minecraftforge.network.filters.NetworkFilters;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;

public class NetworkHooks {

    private static final Logger LOGGER = LogManager.getLogger();

    public static String getFMLVersion(String ip) {
        return ip.contains("\u0000") ? (Objects.equals(ip.split("\u0000")[1], "FML3") ? "FML3" : ip.split("\u0000")[1]) : "NONE";
    }

    public static ConnectionType getConnectionType(Supplier<Connection> connection) {
        return getConnectionType(((Connection) connection.get()).channel());
    }

    public static ConnectionType getConnectionType(ChannelHandlerContext context) {
        return getConnectionType(context.channel());
    }

    private static ConnectionType getConnectionType(Channel channel) {
        return ConnectionType.forVersionFlag((String) channel.attr(NetworkConstants.FML_NETVERSION).get());
    }

    public static Packet<ClientGamePacketListener> getEntitySpawningPacket(Entity entity) {
        return (Packet<ClientGamePacketListener>) NetworkConstants.playChannel.toVanillaPacket(new PlayMessages.SpawnEntity(entity), NetworkDirection.PLAY_TO_CLIENT);
    }

    public static boolean onCustomPayload(ICustomPacket<?> packet, Connection manager) {
        return (Boolean) NetworkRegistry.findTarget(packet.getName()).filter(ni -> validateSideForProcessing(packet, ni, manager)).map(ni -> ni.dispatch(packet.getDirection(), packet, manager)).orElse(Boolean.FALSE);
    }

    private static boolean validateSideForProcessing(ICustomPacket<?> packet, NetworkInstance ni, Connection manager) {
        if (packet.getDirection().getReceptionSide() != EffectiveSide.get()) {
            manager.disconnect(Component.literal("Illegal packet received, terminating connection"));
            return false;
        } else {
            return true;
        }
    }

    public static void validatePacketDirection(NetworkDirection packetDirection, Optional<NetworkDirection> expectedDirection, Connection connection) {
        if (packetDirection != expectedDirection.orElse(packetDirection)) {
            connection.disconnect(Component.literal("Illegal packet received, terminating connection"));
            throw new IllegalStateException("Invalid packet received, aborting connection");
        }
    }

    public static void registerServerLoginChannel(Connection manager, ClientIntentionPacket packet) {
        manager.channel().attr(NetworkConstants.FML_NETVERSION).set(packet.getFMLVersion());
        HandshakeHandler.registerHandshake(manager, NetworkDirection.LOGIN_TO_CLIENT);
    }

    public static synchronized void registerClientLoginChannel(Connection manager) {
        manager.channel().attr(NetworkConstants.FML_NETVERSION).set("NONE");
        HandshakeHandler.registerHandshake(manager, NetworkDirection.LOGIN_TO_SERVER);
    }

    public static synchronized void sendMCRegistryPackets(Connection manager, String direction) {
        NetworkFilters.injectIfNecessary(manager);
        Set<ResourceLocation> resourceLocations = (Set<ResourceLocation>) NetworkRegistry.buildChannelVersions().keySet().stream().filter(rl -> !Objects.equals(rl.getNamespace(), "minecraft")).collect(Collectors.toSet());
        MCRegisterPacketHandler.INSTANCE.addChannels(resourceLocations, manager);
        MCRegisterPacketHandler.INSTANCE.sendRegistry(manager, NetworkDirection.valueOf(direction));
    }

    public static boolean isVanillaConnection(Connection manager) {
        if (manager != null && manager.channel() != null) {
            return getConnectionType((Supplier<Connection>) (() -> manager)) == ConnectionType.VANILLA;
        } else {
            throw new NullPointerException("ARGH! Network Manager is null (" + manager != null ? "CHANNEL" : "MANAGER)");
        }
    }

    public static void handleClientLoginSuccess(Connection manager) {
        if (isVanillaConnection(manager)) {
            LOGGER.info("Connected to a vanilla server. Catching up missing behaviour.");
            ConfigTracker.INSTANCE.loadDefaultServerConfigs();
        } else {
            LOGGER.info("Connected to a modded server.");
        }
    }

    public static boolean tickNegotiation(ServerLoginPacketListenerImpl netHandlerLoginServer, Connection networkManager, ServerPlayer player) {
        return HandshakeHandler.tickLogin(networkManager);
    }

    public static void openScreen(ServerPlayer player, MenuProvider containerSupplier) {
        openScreen(player, containerSupplier, buf -> {
        });
    }

    public static void openScreen(ServerPlayer player, MenuProvider containerSupplier, BlockPos pos) {
        openScreen(player, containerSupplier, buf -> buf.writeBlockPos(pos));
    }

    public static void openScreen(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter) {
        if (!player.m_9236_().isClientSide) {
            player.doCloseContainer();
            player.nextContainerCounter();
            int openContainerId = player.containerCounter;
            FriendlyByteBuf extraData = new FriendlyByteBuf(Unpooled.buffer());
            extraDataWriter.accept(extraData);
            extraData.readerIndex(0);
            FriendlyByteBuf output = new FriendlyByteBuf(Unpooled.buffer());
            output.writeVarInt(extraData.readableBytes());
            output.writeBytes(extraData);
            if (output.readableBytes() <= 32600 && output.readableBytes() >= 1) {
                AbstractContainerMenu c = containerSupplier.m_7208_(openContainerId, player.m_150109_(), player);
                if (c != null) {
                    MenuType<?> type = c.getType();
                    PlayMessages.OpenContainer msg = new PlayMessages.OpenContainer(type, openContainerId, containerSupplier.getDisplayName(), output);
                    NetworkConstants.playChannel.sendTo(msg, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
                    player.f_36096_ = c;
                    player.initMenu(player.f_36096_);
                    MinecraftForge.EVENT_BUS.post(new PlayerContainerEvent.Open(player, c));
                }
            } else {
                throw new IllegalArgumentException("Invalid PacketBuffer for openGui, found " + output.readableBytes() + " bytes");
            }
        }
    }

    static void appendConnectionData(Connection mgr, Map<String, Pair<String, String>> modData, Map<ResourceLocation, String> channels) {
        ConnectionData oldData = (ConnectionData) mgr.channel().attr(NetworkConstants.FML_CONNECTION_DATA).get();
        oldData = oldData != null ? new ConnectionData((Map<String, Pair<String, String>>) (oldData.getModData().isEmpty() ? modData : oldData.getModData()), (Map<ResourceLocation, String>) (oldData.getChannels().isEmpty() ? channels : oldData.getChannels())) : new ConnectionData(modData, channels);
        mgr.channel().attr(NetworkConstants.FML_CONNECTION_DATA).set(oldData);
    }

    @Nullable
    public static ConnectionData getConnectionData(Connection mgr) {
        return (ConnectionData) mgr.channel().attr(NetworkConstants.FML_CONNECTION_DATA).get();
    }

    @Nullable
    public static ConnectionData.ModMismatchData getModMismatchData(Connection mgr) {
        return (ConnectionData.ModMismatchData) mgr.channel().attr(NetworkConstants.FML_MOD_MISMATCH_DATA).get();
    }

    @Nullable
    public static MCRegisterPacketHandler.ChannelList getChannelList(Connection mgr) {
        return (MCRegisterPacketHandler.ChannelList) mgr.channel().attr(NetworkConstants.FML_MC_REGISTRY).get();
    }
}