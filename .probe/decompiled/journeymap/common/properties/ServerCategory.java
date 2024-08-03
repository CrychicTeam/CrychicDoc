package journeymap.common.properties;

import java.util.Arrays;
import java.util.List;
import journeymap.common.properties.catagory.Category;

public class ServerCategory {

    private static int order = 1;

    public static final Category Global = create("Global", "jm.server.edit.label.selection.global", "jm.server.edit.label.selection.global.tooltip");

    public static final Category Default = create("Default", "jm.server.edit.label.selection.default", "jm.server.edit.label.selection.default.tooltip");

    public static final Category Dimension = create("Dimension", "jm.server.edit.label.selection.dimension", "jm.server.edit.label.selection.dimension.tooltip");

    public static final List<Category> values = Arrays.asList(Category.Inherit, Category.Hidden, Global, Default, Dimension);

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