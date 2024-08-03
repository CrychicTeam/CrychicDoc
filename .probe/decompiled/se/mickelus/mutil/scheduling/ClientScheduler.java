package se.mickelus.mutil.scheduling;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ClientScheduler extends AbstractScheduler {

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        this.tick(event);
    }
}