package journeymap.client.event.handlers;

import journeymap.client.JourneymapClient;
import journeymap.common.Journeymap;

public class PlayerConnectHandler {

    public void onConnect() {
        try {
            JourneymapClient.getInstance().getDispatcher().sendHandshakePacket(Journeymap.JM_VERSION.toJson());
        } catch (Exception var2) {
            Journeymap.getLogger().error("Error handling ClientPlayerNetworkEvent.LoggedInEvent", var2);
        }
    }
}