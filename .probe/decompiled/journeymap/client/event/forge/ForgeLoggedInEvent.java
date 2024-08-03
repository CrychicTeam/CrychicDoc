package journeymap.client.event.forge;

import journeymap.client.event.handlers.PlayerConnectHandler;
import net.minecraftforge.client.event.ClientPlayerNetworkEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeLoggedInEvent implements ForgeEventHandlerManager.EventHandler {

    private final PlayerConnectHandler playerConnectHandler = new PlayerConnectHandler();

    @SubscribeEvent
    public void onConnect(ClientPlayerNetworkEvent.LoggingIn event) {
        this.playerConnectHandler.onConnect();
    }
}