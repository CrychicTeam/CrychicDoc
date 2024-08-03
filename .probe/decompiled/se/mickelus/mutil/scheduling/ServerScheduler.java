package se.mickelus.mutil.scheduling;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ServerScheduler extends AbstractScheduler {

    @SubscribeEvent
    public void onServerTick(TickEvent.ServerTickEvent event) {
        this.tick(event);
    }
}