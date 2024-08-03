package journeymap.client.api.impl;

import java.util.Collection;
import java.util.EnumSet;
import java.util.Iterator;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.api.event.ClientEvent;
import journeymap.client.api.event.DeathWaypointEvent;
import journeymap.client.api.event.DisplayUpdateEvent;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

@ParametersAreNonnullByDefault
public class ClientEventManager {

    private final DisplayUpdateEventThrottle displayUpdateEventThrottle = new DisplayUpdateEventThrottle();

    private final Collection<PluginWrapper> plugins;

    private EnumSet<ClientEvent.Type> subscribedClientEventTypes = EnumSet.noneOf(ClientEvent.Type.class);

    public ClientEventManager(Collection<PluginWrapper> plugins) {
        this.plugins = plugins;
    }

    public void updateSubscribedTypes() {
        this.subscribedClientEventTypes = EnumSet.noneOf(ClientEvent.Type.class);
        for (PluginWrapper wrapper : this.plugins) {
            this.subscribedClientEventTypes.addAll(wrapper.getSubscribedClientEventTypes());
        }
    }

    public boolean canFireClientEvent(ClientEvent.Type type) {
        return this.subscribedClientEventTypes.contains(type);
    }

    public void fireMappingEvent(boolean started, ResourceKey<Level> dimension) {
        ClientEvent.Type type = started ? ClientEvent.Type.MAPPING_STARTED : ClientEvent.Type.MAPPING_STOPPED;
        if (started) {
            ClientAPI.INSTANCE.refreshDataPathCache(false);
        }
        if (!this.plugins.isEmpty() && this.subscribedClientEventTypes.contains(type)) {
            ClientEvent clientEvent = new ClientEvent(type, dimension);
            for (PluginWrapper wrapper : this.plugins) {
                try {
                    wrapper.notify(clientEvent);
                } catch (Throwable var8) {
                    ClientAPI.INSTANCE.logError("Error in fireMappingEvent(): " + clientEvent, var8);
                }
            }
            if (!started) {
                ClientAPI.INSTANCE.refreshDataPathCache(true);
            }
        }
    }

    public void fireDeathpointEvent(DeathWaypointEvent clientEvent) {
        if (!this.plugins.isEmpty() && this.subscribedClientEventTypes.contains(ClientEvent.Type.DEATH_WAYPOINT)) {
            for (PluginWrapper wrapper : this.plugins) {
                try {
                    wrapper.notify(clientEvent);
                } catch (Throwable var5) {
                    ClientAPI.INSTANCE.logError("Error in fireDeathpointEvent(): " + clientEvent, var5);
                }
            }
        }
    }

    public void fireEvent(ClientEvent event) {
        if (!this.plugins.isEmpty() && this.subscribedClientEventTypes.contains(event.type)) {
            for (PluginWrapper wrapper : this.plugins) {
                try {
                    if (wrapper.getSubscribedClientEventTypes().contains(event.type)) {
                        wrapper.notify(event);
                    }
                } catch (Throwable var5) {
                    ClientAPI.INSTANCE.logError("Error in fireEvent(): " + event, var5);
                }
            }
        }
    }

    public void fireDisplayUpdateEvent(DisplayUpdateEvent clientEvent) {
        if (this.plugins.size() != 0 && this.subscribedClientEventTypes.contains(ClientEvent.Type.DISPLAY_UPDATE)) {
            try {
                this.displayUpdateEventThrottle.add(clientEvent);
            } catch (Throwable var3) {
                ClientAPI.INSTANCE.logError("Error in fireDisplayUpdateEvent(): " + clientEvent, var3);
            }
        }
    }

    public void fireNextClientEvents() {
        if (!this.plugins.isEmpty() && this.displayUpdateEventThrottle.isReady()) {
            Iterator<DisplayUpdateEvent> iterator = this.displayUpdateEventThrottle.iterator();
            while (iterator.hasNext()) {
                DisplayUpdateEvent clientEvent = (DisplayUpdateEvent) iterator.next();
                iterator.remove();
                for (PluginWrapper wrapper : this.plugins) {
                    try {
                        wrapper.notify(clientEvent);
                    } catch (Throwable var6) {
                        ClientAPI.INSTANCE.logError("Error in fireDeathpointEvent(): " + clientEvent, var6);
                    }
                }
            }
        }
    }

    void purge() {
        this.plugins.clear();
        this.subscribedClientEventTypes.clear();
    }
}