package journeymap.client.properties;

import journeymap.client.model.MapType;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.IntegerField;

public abstract class MapProperties extends ClientPropertiesBase implements Comparable<MapProperties> {

    public final BooleanField showWaypoints = new BooleanField(Category.Inherit, "jm.common.show_waypoints", true);

    public final BooleanField showSelf = new BooleanField(Category.Inherit, "jm.common.show_self", true);

    public final BooleanField showGrid = new BooleanField(Category.Inherit, "jm.common.show_grid", true);

    public final BooleanField showCaves = new BooleanField(Category.Inherit, "jm.common.show_caves", true);

    public final BooleanField showEntityNames = new BooleanField(Category.Inherit, "jm.common.show_entity_names", true);

    public final EnumField<MapType.Name> preferredMapType = new EnumField<>(Category.Hidden, "", MapType.Name.day);

    public final IntegerField zoomLevel = new IntegerField(Category.Hidden, "", 0, 8, 0);

    public int compareTo(MapProperties other) {
        return Integer.valueOf(this.hashCode()).compareTo(other.hashCode());
    }
}