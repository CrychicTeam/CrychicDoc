package journeymap.client.api.event;

import journeymap.client.api.display.Waypoint;
import net.minecraft.client.Minecraft;

public class WaypointEvent extends ClientEvent {

    public final Waypoint waypoint;

    public final WaypointEvent.Context context;

    private WaypointEvent(Waypoint waypoint, WaypointEvent.Context context) {
        super(ClientEvent.Type.WAYPOINT, Minecraft.getInstance().level.m_46472_());
        this.waypoint = waypoint;
        this.context = context;
    }

    public Waypoint getWaypoint() {
        return this.waypoint;
    }

    public WaypointEvent.Context getContext() {
        return this.context;
    }

    public static enum Context {

        CREATE, UPDATE, DELETED, READ
    }

    public static class WaypointCreatedEvent extends WaypointEvent {

        public WaypointCreatedEvent(Waypoint waypoint) {
            super(waypoint, WaypointEvent.Context.CREATE);
        }
    }

    public static class WaypointDeletedEvent extends WaypointEvent {

        public WaypointDeletedEvent(Waypoint waypoint) {
            super(waypoint, WaypointEvent.Context.DELETED);
        }
    }

    public static class WaypointReadEvent extends WaypointEvent {

        public WaypointReadEvent(Waypoint waypoint) {
            super(waypoint, WaypointEvent.Context.READ);
        }
    }

    public static class WaypointUpdateEvent extends WaypointEvent {

        public WaypointUpdateEvent(Waypoint waypoint) {
            super(waypoint, WaypointEvent.Context.UPDATE);
        }
    }
}