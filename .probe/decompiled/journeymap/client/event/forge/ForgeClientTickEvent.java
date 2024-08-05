package journeymap.client.event.forge;

import journeymap.client.event.handlers.StateTickHandler;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeClientTickEvent implements ForgeEventHandlerManager.EventHandler {

    private final StateTickHandler stateTickHandler = new StateTickHandler();

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) {
            this.stateTickHandler.onClientTick();
        }
    }
}