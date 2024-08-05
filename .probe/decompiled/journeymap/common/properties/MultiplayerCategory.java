package journeymap.common.properties;

import java.util.Arrays;
import java.util.List;
import journeymap.common.properties.catagory.Category;

public class MultiplayerCategory {

    private static int order = 1;

    public static final Category Multiplayer = create("Multiplayer", "base category", "base category");

    public static final Category Radar = create("Radar", "jm.options.multiplayer.category.radar", "jm.options.multiplayer.category.radar.tooltip");

    public static final List<Category> values = Arrays.asList(Category.Inherit, Category.Hidden, Radar);

    public static Category valueOf(String name) {
        for (Category category : values) {
            if (category.getName().equalsIgnoreCase(name)) {
                return category;
            }
        }
        return null;
    }

    private static Category create(String name, String label) {
        return create(name, label, null);
    }

    public static Category create(String name, String label, String tooltip) {
        return new Category(name, order++, label, tooltip);
    }
}