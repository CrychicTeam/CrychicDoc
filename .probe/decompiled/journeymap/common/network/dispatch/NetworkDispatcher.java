package journeymap.common.network.dispatch;

import journeymap.common.network.data.NetworkHandler;
import journeymap.common.network.packets.ClientPermissionsPacket;
import journeymap.common.network.packets.HandshakePacket;
import journeymap.common.network.packets.MultiplayerOptionsPacket;
import journeymap.common.network.packets.ServerAdminRequestPropPacket;
import journeymap.common.network.packets.ServerPlayerLocationPacket;
import journeymap.common.network.packets.WaypointPacket;
import journeymap.common.network.packets.WorldIdPacket;
import net.minecraft.server.level.ServerPlayer;

public class NetworkDispatcher {

    protected final NetworkHandler handler;

    public NetworkDispatcher(NetworkHandler handler) {
        this.handler = handler;
    }

    public void sendPlayerLocationPacket(ServerPlayer player, ServerPlayer radarPlayer, boolean visible) {
        this.handler.sendToClient(new ServerPlayerLocationPacket(radarPlayer, visible), player);
    }

    public void sendClientPermissions(ServerPlayer player, String payload, boolean serverAdmin) {
        this.handler.sendToClient(new ClientPermissionsPacket(payload, serverAdmin, true), player);
    }

    public void sendWaypointPacket(ServerPlayer player, String waypoint, boolean announce, String action) {
        this.handler.sendToClient(new WaypointPacket(waypoint, announce, action), player);
    }

    public void sendServerAdminPacket(ServerPlayer player, int type, String payload, String dimension) {
        this.handler.sendToClient(new ServerAdminRequestPropPacket(type, payload, dimension), player);
    }

    public void sendWorldIdPacket(ServerPlayer player, String worldId) {
        this.handler.sendToClient(new WorldIdPacket(worldId), player);
    }

    public void sendMultiplayerOptionsPacket(ServerPlayer player, String payload) {
        this.handler.sendToClient(new MultiplayerOptionsPacket(payload), player);
    }

    public void sendHandshakePacket(ServerPlayer player, String version) {
        this.handler.sendToClient(new HandshakePacket(version), player);
    }
}