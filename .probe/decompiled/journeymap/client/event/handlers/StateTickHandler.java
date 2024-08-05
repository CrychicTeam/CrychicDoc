package journeymap.client.event.handlers;

import journeymap.client.JourneymapClient;
import journeymap.client.api.impl.ClientAPI;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;

public class StateTickHandler {

    Minecraft mc = Minecraft.getInstance();

    int counter = 0;

    public void onClientTick() {
        this.mc.getProfiler().push("journeymap");
        try {
            if (this.counter == 20) {
                this.mc.getProfiler().push("mainTasks");
                JourneymapClient.getInstance().performMainThreadTasks();
                this.counter = 0;
                this.mc.getProfiler().pop();
            } else if (this.counter == 10) {
                this.mc.getProfiler().push("multithreadTasks");
                if (JourneymapClient.getInstance().isMapping() && this.mc.level != null) {
                    JourneymapClient.getInstance().performMultithreadTasks();
                }
                this.counter++;
                this.mc.getProfiler().pop();
            } else if (this.counter != 5 && this.counter != 15) {
                this.counter++;
            } else {
                this.mc.getProfiler().push("clientApiEvents");
                ClientAPI.INSTANCE.getClientEventManager().fireNextClientEvents();
                this.counter++;
                this.mc.getProfiler().pop();
            }
        } catch (Throwable var5) {
            Journeymap.getLogger().warn("Error during onClientTick: " + LogFormatter.toPartialString(var5));
        } finally {
            this.mc.getProfiler().pop();
        }
    }
}