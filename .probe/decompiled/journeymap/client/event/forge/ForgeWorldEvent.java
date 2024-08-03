package journeymap.client.event.forge;

import journeymap.client.event.handlers.WorldEventHandler;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

public class ForgeWorldEvent implements ForgeEventHandlerManager.EventHandler {

    private final WorldEventHandler worldEventHandler = new WorldEventHandler();

    @SubscribeEvent
    public void onUnload(LevelEvent.Unload event) {
        this.worldEventHandler.onUnload(event.getLevel());
    }
}