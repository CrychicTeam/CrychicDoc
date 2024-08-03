package journeymap.common.network.forge;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Supplier;
import journeymap.client.JourneymapClient;
import journeymap.common.network.data.NetworkHandler;
import journeymap.common.network.data.PacketContainer;
import journeymap.common.network.data.PacketContext;
import journeymap.common.network.data.Side;
import journeymap.common.network.packets.PacketManager;
import net.minecraft.client.Minecraft;
import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ForgeNetworkHandler extends PacketManager implements NetworkHandler {

    private final Map<Class<?>, SimpleChannel> CHANNELS = new HashMap();

    public ForgeNetworkHandler() {
        this.PACKET_MAP.forEach((clazz, packetContainer) -> this.registerPacket(packetContainer));
    }

    private <T> void registerPacket(PacketContainer<T> container) {
        SimpleChannel channel = NetworkRegistry.ChannelBuilder.named(container.packetIdentifier()).clientAcceptedVersions(a -> true).serverAcceptedVersions(a -> true).networkProtocolVersion(() -> "1").simpleChannel();
        channel.registerMessage(0, container.messageType(), container.encoder(), container.decoder(), this.buildHandler(container.handler()));
        this.CHANNELS.put(container.messageType(), channel);
    }

    @Override
    public <T> void sendToServer(T packet) {
        SimpleChannel channel = (SimpleChannel) this.CHANNELS.get(packet.getClass());
        Connection connection = Minecraft.getInstance().getConnection().getConnection();
        if (channel.isRemotePresent(connection) || JourneymapClient.getInstance().getStateHandler().isJourneyMapServerConnection()) {
            channel.sendToServer(packet);
        }
    }

    @Override
    public <T> void sendToClient(T packet, ServerPlayer player) {
        SimpleChannel channel = (SimpleChannel) this.CHANNELS.get(packet.getClass());
        Connection connection = player.connection.connection;
        if (channel.isRemotePresent(connection)) {
            channel.sendTo(packet, player.connection.connection, NetworkDirection.PLAY_TO_CLIENT);
        }
    }

    private <T> BiConsumer<T, Supplier<NetworkEvent.Context>> buildHandler(Consumer<PacketContext<T>> handler) {
        return (message, ctx) -> {
            ((NetworkEvent.Context) ctx.get()).enqueueWork(() -> {
                Side side = ((NetworkEvent.Context) ctx.get()).getDirection().getReceptionSide().isServer() ? Side.SERVER : Side.CLIENT;
                ServerPlayer player = ((NetworkEvent.Context) ctx.get()).getSender();
                handler.accept(new PacketContext<>(player, message, side));
            });
            ((NetworkEvent.Context) ctx.get()).setPacketHandled(true);
        };
    }
}