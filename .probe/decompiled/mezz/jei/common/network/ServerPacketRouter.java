package mezz.jei.common.network;

import java.util.EnumMap;
import java.util.Optional;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.network.packets.IServerPacketHandler;
import mezz.jei.common.network.packets.PacketDeletePlayerItem;
import mezz.jei.common.network.packets.PacketGiveItemStack;
import mezz.jei.common.network.packets.PacketRecipeTransfer;
import mezz.jei.common.network.packets.PacketRequestCheatPermission;
import mezz.jei.common.network.packets.PacketSetHotbarItemStack;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerPacketRouter {

    private static final Logger LOGGER = LogManager.getLogger();

    public final EnumMap<PacketIdServer, IServerPacketHandler> handlers = new EnumMap(PacketIdServer.class);

    private final IConnectionToClient connection;

    private final IServerConfig serverConfig;

    public ServerPacketRouter(IConnectionToClient connection, IServerConfig serverConfig) {
        this.connection = connection;
        this.serverConfig = serverConfig;
        this.handlers.put(PacketIdServer.RECIPE_TRANSFER, PacketRecipeTransfer::readPacketData);
        this.handlers.put(PacketIdServer.DELETE_ITEM, PacketDeletePlayerItem::readPacketData);
        this.handlers.put(PacketIdServer.GIVE_ITEM, PacketGiveItemStack::readPacketData);
        this.handlers.put(PacketIdServer.SET_HOTBAR_ITEM, PacketSetHotbarItemStack::readPacketData);
        this.handlers.put(PacketIdServer.CHEAT_PERMISSION_REQUEST, PacketRequestCheatPermission::readPacketData);
    }

    public void onPacket(FriendlyByteBuf packetBuffer, ServerPlayer player) {
        this.getPacketId(packetBuffer).ifPresent(packetId -> {
            IServerPacketHandler packetHandler = (IServerPacketHandler) this.handlers.get(packetId);
            ServerPacketContext context = new ServerPacketContext(player, this.serverConfig, this.connection);
            ServerPacketData data = new ServerPacketData(packetBuffer, context);
            try {
                packetHandler.readPacketData(data).exceptionally(e -> {
                    LOGGER.error("Packet error while executing packet on the server thread: {}", packetId.name(), e);
                    return null;
                });
            } catch (Throwable var8) {
                LOGGER.error("Packet error when reading packet: {}", packetId.name(), var8);
            }
        });
    }

    private Optional<PacketIdServer> getPacketId(FriendlyByteBuf packetBuffer) {
        try {
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdServer packetId = PacketIdServer.VALUES[packetIdOrdinal];
            return Optional.of(packetId);
        } catch (RuntimeException var4) {
            LOGGER.error("Packet error when trying to read packet id", var4);
            return Optional.empty();
        }
    }
}