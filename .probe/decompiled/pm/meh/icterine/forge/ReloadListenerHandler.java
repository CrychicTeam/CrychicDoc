package pm.meh.icterine.forge;

import net.minecraftforge.event.AddReloadListenerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import pm.meh.icterine.impl.ReloadListenerHandlerBase;

public class ReloadListenerHandler {

    @SubscribeEvent
    public static void addReloadListeners(AddReloadListenerEvent event) {
        event.addListener(new ReloadListenerHandlerBase());
    }
}