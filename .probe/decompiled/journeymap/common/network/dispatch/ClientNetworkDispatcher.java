package journeymap.common.network.dispatch;

import journeymap.common.network.data.NetworkHandler;
import journeymap.common.network.packets.ClientPermissionsPacket;
import journeymap.common.network.packets.HandshakePacket;
import journeymap.common.network.packets.MultiplayerOptionsPacket;
import journeymap.common.network.packets.ServerAdminRequestPropPacket;
import journeymap.common.network.packets.ServerAdminSavePropPacket;
import journeymap.common.network.packets.TeleportPacket;
import journeymap.common.network.packets.WorldIdPacket;

public class ClientNetworkDispatcher {

    protected final NetworkHandler handler;

    public ClientNetworkDispatcher(NetworkHandler handler) {
        this.handler = handler;
    }

    public void sendTeleportPacket(double x, int y, double z, String dim) {
        this.handler.sendToServer(new TeleportPacket(x, (double) y, z, dim));
    }

    public void sendServerAdminScreenRequest(int id, String dim) {
        this.handler.sendToServer(new ServerAdminRequestPropPacket(id, dim));
    }

    public void sendSaveAdminDataPacket(int id, String payload, String dim) {
        this.handler.sendToServer(new ServerAdminSavePropPacket(id, payload, dim));
    }

    public void sendPermissionRequest() {
        this.handler.sendToServer(new ClientPermissionsPacket());
    }

    public void sendWorldIdRequest() {
        this.handler.sendToServer(new WorldIdPacket());
    }

    public void sendHandshakePacket(String version) {
        this.handler.sendToServer(new HandshakePacket(version));
    }

    public void sendMultiplayerOptionsRequest() {
        this.handler.sendToServer(new MultiplayerOptionsPacket());
    }

    public void sendMultiplayerOptionsSaveRequest(String payload) {
        this.handler.sendToServer(new MultiplayerOptionsPacket(payload));
    }
}