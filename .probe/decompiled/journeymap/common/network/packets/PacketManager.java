package journeymap.common.network.packets;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import journeymap.common.network.data.PacketContainer;
import journeymap.common.network.data.PacketContext;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;

public abstract class PacketManager {

    protected final Map<Class<?>, PacketContainer<?>> PACKET_MAP = new HashMap();

    public PacketManager() {
        this.setupPacket(ServerAdminRequestPropPacket.CHANNEL, ServerAdminRequestPropPacket.class, ServerAdminRequestPropPacket::encode, ServerAdminRequestPropPacket::decode, ServerAdminRequestPropPacket::handle);
        this.setupPacket(ServerAdminSavePropPacket.CHANNEL, ServerAdminSavePropPacket.class, ServerAdminSavePropPacket::encode, ServerAdminSavePropPacket::decode, ServerAdminSavePropPacket::handle);
        this.setupPacket(ClientPermissionsPacket.CHANNEL, ClientPermissionsPacket.class, ClientPermissionsPacket::encode, ClientPermissionsPacket::decode, ClientPermissionsPacket::handle);
        this.setupPacket(ServerPlayerLocationPacket.CHANNEL, ServerPlayerLocationPacket.class, ServerPlayerLocationPacket::encode, ServerPlayerLocationPacket::decode, ServerPlayerLocationPacket::handle);
        this.setupPacket(TeleportPacket.CHANNEL, TeleportPacket.class, TeleportPacket::encode, TeleportPacket::decode, TeleportPacket::handle);
        this.setupPacket(MultiplayerOptionsPacket.CHANNEL, MultiplayerOptionsPacket.class, MultiplayerOptionsPacket::encode, MultiplayerOptionsPacket::decode, MultiplayerOptionsPacket::handle);
        this.setupPacket(CommonPacket.CHANNEL, CommonPacket.class, CommonPacket::encode, CommonPacket::decode, CommonPacket::handle);
        this.setupPacket(WorldIdPacket.CHANNEL, WorldIdPacket.class, WorldIdPacket::encode, WorldIdPacket::decode, WorldIdPacket::handle);
        this.setupPacket(WaypointPacket.CHANNEL, WaypointPacket.class, WaypointPacket::encode, WaypointPacket::decode, WaypointPacket::handle);
        this.setupPacket(HandshakePacket.CHANNEL, HandshakePacket.class, HandshakePacket::encode, HandshakePacket::decode, HandshakePacket::handle);
    }

    private <T> void setupPacket(ResourceLocation packetIdentifier, Class<T> messageType, BiConsumer<T, FriendlyByteBuf> encoder, Function<FriendlyByteBuf, T> decoder, Consumer<PacketContext<T>> handler) {
        PacketContainer<T> container = new PacketContainer<>(packetIdentifier, messageType, encoder, decoder, handler);
        this.PACKET_MAP.put(messageType, container);
    }
}