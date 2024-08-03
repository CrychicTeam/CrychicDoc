package journeymap.client.waypoint;

import journeymap.client.api.event.WaypointEvent;
import journeymap.client.api.impl.ClientAPI;

public class WaypointEventManager {

    public static void deleteWaypointEvent(Waypoint waypoint) {
        WaypointEvent.WaypointDeletedEvent event = new WaypointEvent.WaypointDeletedEvent(waypoint.modWaypoint());
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(event);
    }

    public static void readWaypointEvent(Waypoint waypoint) {
        WaypointEvent.WaypointReadEvent event = new WaypointEvent.WaypointReadEvent(waypoint.modWaypoint());
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(event);
    }

    public static void updateWaypointEvent(Waypoint waypoint) {
        WaypointEvent.WaypointUpdateEvent event = new WaypointEvent.WaypointUpdateEvent(waypoint.modWaypoint());
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(event);
    }

    public static void createWaypointEvent(Waypoint waypoint) {
        WaypointEvent.WaypointCreatedEvent event = new WaypointEvent.WaypointCreatedEvent(waypoint.modWaypoint());
        ClientAPI.INSTANCE.getClientEventManager().fireEvent(event);
    }
}