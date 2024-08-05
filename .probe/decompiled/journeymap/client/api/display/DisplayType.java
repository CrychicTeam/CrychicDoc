package journeymap.client.api.display;

import java.util.HashMap;

public enum DisplayType {

    Image(ImageOverlay.class), Marker(MarkerOverlay.class), Polygon(PolygonOverlay.class), Waypoint(Waypoint.class), WaypointGroup(WaypointGroup.class);

    private static HashMap<Class<? extends Displayable>, DisplayType> reverseLookup;

    private final Class<? extends Displayable> implClass;

    private DisplayType(Class<? extends Displayable> implClass) {
        this.implClass = implClass;
    }

    public static DisplayType of(Class<? extends Displayable> implClass) {
        if (reverseLookup == null) {
            reverseLookup = new HashMap();
            for (DisplayType type : values()) {
                reverseLookup.put(type.getImplClass(), type);
            }
        }
        DisplayType displayType = (DisplayType) reverseLookup.get(implClass);
        if (displayType == null) {
            throw new IllegalArgumentException("Not a valid Displayable implementation: " + implClass);
        } else {
            return displayType;
        }
    }

    public Class<? extends Displayable> getImplClass() {
        return this.implClass;
    }
}