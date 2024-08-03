package mezz.jei.common.network;

import java.util.EnumMap;
import java.util.Optional;
import mezz.jei.common.config.IServerConfig;
import mezz.jei.common.network.packets.IClientPacketHandler;
import mezz.jei.common.network.packets.PacketCheatPermission;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.FriendlyByteBuf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ClientPacketRouter {

    private static final Logger LOGGER = LogManager.getLogger();

    public final EnumMap<PacketIdClient, IClientPacketHandler> clientHandlers = new EnumMap(PacketIdClient.class);

    private final IConnectionToServer connection;

    private final IServerConfig serverConfig;

    public ClientPacketRouter(IConnectionToServer connection, IServerConfig serverConfig) {
        this.connection = connection;
        this.serverConfig = serverConfig;
        this.clientHandlers.put(PacketIdClient.CHEAT_PERMISSION, PacketCheatPermission::readPacketData);
    }

    public void onPacket(FriendlyByteBuf packetBuffer, LocalPlayer player) {
        this.getPacketId(packetBuffer).ifPresent(packetId -> {
            IClientPacketHandler packetHandler = (IClientPacketHandler) this.clientHandlers.get(packetId);
            ClientPacketContext context = new ClientPacketContext(player, this.connection, this.serverConfig);
            ClientPacketData data = new ClientPacketData(packetBuffer, context);
            try {
                packetHandler.readPacketData(data).exceptionally(e -> {
                    LOGGER.error("Packet error while executing packet on the client thread: {}", packetId.name(), e);
                    return null;
                });
            } catch (Throwable var8) {
                LOGGER.error("Packet error when reading packet: {}", packetId.name(), var8);
            }
        });
    }

    private Optional<PacketIdClient> getPacketId(FriendlyByteBuf packetBuffer) {
        try {
            int packetIdOrdinal = packetBuffer.readByte();
            PacketIdClient packetId = PacketIdClient.VALUES[packetIdOrdinal];
            return Optional.of(packetId);
        } catch (RuntimeException var4) {
            LOGGER.error("Packet error when trying to read packet id", var4);
            return Optional.empty();
        }
    }
}