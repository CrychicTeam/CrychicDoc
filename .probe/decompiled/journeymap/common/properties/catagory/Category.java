package journeymap.common.properties.catagory;

import com.google.common.base.Objects;
import journeymap.client.Constants;

public class Category implements Comparable<Category> {

    public static final Category Inherit = new Category("Inherit", 0, "");

    public static final Category Hidden = new Category("Hidden", 0, "");

    String name;

    String label;

    String tooltip;

    int order;

    boolean unique;

    public Category(String name, int order, String label) {
        this(name, order, label, label + ".tooltip");
    }

    public Category(String name, int order, String label, boolean unique) {
        this(name, order, label, label + ".tooltip");
        this.unique = unique;
    }

    public Category(String name, int order, String label, String tooltip) {
        this.name = name;
        this.order = order;
        this.label = label;
        this.tooltip = tooltip;
    }

    public boolean isUnique() {
        return this.unique;
    }

    public String getName() {
        return this.name;
    }

    public String getLabel() {
        return this.label == null ? this.getName() : Constants.getString(this.label);
    }

    public String getTooltip() {
        return this.tooltip == null ? this.getLabel() : Constants.getString(this.tooltip);
    }

    public int getOrder() {
        return this.order;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else {
            return !(o instanceof Category category) ? false : Objects.equal(this.getName(), category.getName());
        }
    }

    public int hashCode() {
        return Objects.hashCode(new Object[] { this.getName() });
    }

    public String toString() {
        return this.name;
    }

    public int compareTo(Category o) {
        int result = Integer.compare(this.order, o.order);
        if (result == 0) {
            result = this.name.compareTo(o.name);
        }
        return result;
    }
}