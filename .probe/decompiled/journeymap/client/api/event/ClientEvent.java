package journeymap.client.api.event;

import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class ClientEvent {

    public final ClientEvent.Type type;

    public final ResourceKey<Level> dimension;

    public final long timestamp;

    private boolean cancelled;

    public ClientEvent(ClientEvent.Type type, ResourceKey<Level> dimension) {
        this.type = type;
        this.dimension = dimension;
        this.timestamp = System.currentTimeMillis();
    }

    public ClientEvent(ClientEvent.Type type) {
        this(type, Level.OVERWORLD);
    }

    public boolean isCancelled() {
        return this.cancelled;
    }

    public void cancel() {
        if (this.isCancellable()) {
            this.cancelled = true;
        }
    }

    public boolean isCancellable() {
        return this.type.cancellable;
    }

    public static enum Type {

        DISPLAY_UPDATE(false),
        DEATH_WAYPOINT(true),
        MAPPING_STARTED(false),
        MAPPING_STOPPED(false),
        MAP_CLICKED(true),
        MAP_DRAGGED(true),
        MAP_MOUSE_MOVED(false),
        REGISTRY(false),
        WAYPOINT(false);

        public final boolean cancellable;

        private Type(boolean cancellable) {
            this.cancellable = cancellable;
        }
    }
}