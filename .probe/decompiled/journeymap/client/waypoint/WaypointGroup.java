package journeymap.client.waypoint;

import com.google.common.base.MoreObjects;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Since;
import com.google.gson.internal.LinkedTreeMap;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.Constants;
import journeymap.client.cartography.color.RGB;

@ParametersAreNonnullByDefault
public class WaypointGroup implements Comparable<WaypointGroup> {

    public static final WaypointGroup DEFAULT = new WaypointGroup("journeymap", Constants.getString("jm.config.category.waypoint")).setEnable(true);

    public static final double VERSION = 5.2;

    public static final Gson GSON = new GsonBuilder().setVersion(5.2).create();

    @Since(5.2)
    protected String name;

    @Since(5.2)
    protected String origin;

    @Since(5.2)
    protected String icon;

    @Since(5.2)
    protected String color;

    @Since(5.2)
    protected boolean enable;

    @Since(5.2)
    protected int order;

    protected transient boolean dirty;

    protected transient Integer colorInt;

    public WaypointGroup(String origin, String name) {
        this.setOrigin(origin).setName(name);
    }

    public String getName() {
        return this.name;
    }

    public WaypointGroup setName(String name) {
        this.name = name;
        return this.setDirty();
    }

    public String getOrigin() {
        return this.origin;
    }

    public WaypointGroup setOrigin(String origin) {
        this.origin = origin;
        return this.setDirty();
    }

    public String getIcon() {
        return this.icon;
    }

    public WaypointGroup setIcon(String icon) {
        this.icon = icon;
        return this.setDirty();
    }

    public int getColor() {
        if (this.colorInt == null) {
            if (this.color == null) {
                this.color = RGB.toHexString(RGB.randomColor());
            }
            this.colorInt = RGB.hexToInt(this.color);
        }
        return this.colorInt;
    }

    public WaypointGroup setColor(String color) {
        this.colorInt = RGB.hexToInt(color);
        this.color = RGB.toHexString(this.colorInt);
        return this.setDirty();
    }

    public WaypointGroup setColor(int color) {
        this.color = RGB.toHexString(color);
        this.colorInt = color;
        return this.setDirty();
    }

    public boolean isEnable() {
        return this.enable;
    }

    public WaypointGroup setEnable(boolean enable) {
        this.enable = enable;
        return this.setDirty();
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public WaypointGroup setDirty() {
        return this.setDirty(true);
    }

    public WaypointGroup setDirty(boolean dirty) {
        this.dirty = dirty;
        return this;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            WaypointGroup group = (WaypointGroup) o;
            return !this.name.equals(group.name) ? false : this.origin.equals(group.origin);
        } else {
            return false;
        }
    }

    public int hashCode() {
        int result = this.name.hashCode();
        return 31 * result + this.origin.hashCode();
    }

    public int compareTo(WaypointGroup o) {
        int result = Integer.compare(this.order, o.order);
        if (result == 0) {
            result = this.name.compareTo(o.name);
        }
        return result;
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("name", this.name).add("origin", this.origin).toString();
    }

    public String getKey() {
        return String.format("%s:%s", this.origin, this.name);
    }

    public static WaypointGroup from(LinkedTreeMap<Object, Object> map) {
        String name = map.get("name").toString();
        String origin = map.get("origin").toString();
        Object icon = map.get("icon");
        Object color = map.get("color");
        Object enable = map.get("enable");
        Object order = map.get("order");
        WaypointGroup group = new WaypointGroup(name, origin);
        if (icon != null) {
            group.icon = icon.toString();
        }
        if (color != null) {
            group.color = color.toString();
        }
        if (enable != null) {
            group.enable = Boolean.parseBoolean(enable.toString());
        }
        if (order != null) {
            group.order = (int) Double.parseDouble(order.toString());
        }
        return group;
    }

    public static WaypointGroup getNamedGroup(String origin, String groupName) {
        return WaypointGroupStore.INSTANCE.get(origin, groupName);
    }
}