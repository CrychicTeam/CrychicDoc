package journeymap.client.properties;

import com.google.common.collect.Lists;
import java.util.List;
import journeymap.common.properties.catagory.Category;

public class ClientCategory {

    private static int order = 1;

    public static final List<Category> values = Lists.newArrayList(new Category[] { Category.Inherit, Category.Hidden });

    public static final Category MiniMap1 = create("MiniMap1", "jm.config.category.minimap");

    public static final Category MiniMap2 = create("MiniMap2", "jm.config.category.minimap2");

    public static final Category FullMap = create("FullMap", "jm.config.category.fullmap");

    public static final Category WebMap = create("WebMap", "jm.config.category.webmap");

    public static final Category Waypoint = create("Waypoint", "jm.config.category.waypoint");

    public static final Category WaypointBeacon = create("WaypointBeacon", "jm.config.category.waypoint_beacons");

    public static final Category Cartography = create("Cartography", "jm.config.category.cartography");

    public static final Category Advanced = create("Advanced", "jm.config.category.advanced");

    public static final Category MinimapPosition = create("Position", "jm.config.category.minimap_position", true);

    public static Category create(String name, String key) {
        return create(name, key, false);
    }

    public static Category create(String name, String key, boolean unique) {
        Category cat = new Category(name, order++, key, unique);
        values.add(cat);
        return cat;
    }

    public static Category create(String name, String key, String tooltip) {
        Category cat = new Category(name, order++, key, tooltip);
        values.add(cat);
        return cat;
    }

    public static Category valueOf(String name) {
        for (Category category : values) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }
}