package journeymap.client.properties;

import journeymap.client.ui.option.DateFormat;
import journeymap.client.ui.option.TimeFormat;
import journeymap.common.JM;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.CustomField;
import journeymap.common.properties.config.FloatField;
import journeymap.common.properties.config.IntegerField;
import journeymap.common.properties.config.StringField;

public class WaypointProperties extends ClientPropertiesBase implements Comparable<WaypointProperties> {

    public final BooleanField managerEnabled = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.enable_manager", true, true);

    public final BooleanField beaconEnabled = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.enable_beacons", true, true);

    public final BooleanField showTexture = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.show_texture", true);

    public final BooleanField showStaticBeam = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.show_static_beam", true);

    public final BooleanField showRotatingBeam = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.show_rotating_beam", true);

    public final BooleanField showName = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.show_name", true);

    public final BooleanField showDistance = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.show_distance", true);

    public final BooleanField autoHideLabel = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.auto_hide_label", true);

    public final BooleanField showDeviationLabel = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.show_deviation", false);

    public final BooleanField disableStrikeThrough = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.manager.disable_strikethrough", false);

    public final BooleanField boldLabel = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.bold_label", false);

    public final FloatField fontScale = new FloatField(ClientCategory.WaypointBeacon, "jm.common.font_scale", 0.5F, 5.0F, 2.0F);

    public final BooleanField textureSmall = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.texture_size", true);

    public final BooleanField shaderBeacon = new BooleanField("fabric".equalsIgnoreCase(JM.LOADER_NAME) ? ClientCategory.WaypointBeacon : Category.Hidden, "jm.waypoint.shader", false);

    public final IntegerField maxDistance = new IntegerField(ClientCategory.WaypointBeacon, "jm.waypoint.max_distance", 0, 10000, 0);

    public final IntegerField minDistance = new IntegerField(ClientCategory.WaypointBeacon, "jm.waypoint.min_distance", 0, 64, 4);

    public final BooleanField createDeathpoints = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.create_deathpoints", true);

    public final BooleanField autoRemoveDeathpoints = new BooleanField(ClientCategory.WaypointBeacon, "jm.waypoint.auto_remove_deathpoints", false);

    public final IntegerField autoRemoveDeathpointDistance = new IntegerField(ClientCategory.WaypointBeacon, "jm.waypoint.auto_deathpoint_remove_distance", 2, 64, 2);

    public final IntegerField autoRemoveTempWaypoints = new IntegerField(ClientCategory.WaypointBeacon, "jm.waypoint.temp_waypoint_remove_distance", 2, 64, 2);

    public final BooleanField showDeathpointlabel = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.show_death_point_label_on_map", true);

    public final BooleanField fullscreenDoubleClickToCreate = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.double_click_create", true);

    public final CustomField teleportCommand = new CustomField(ClientCategory.Waypoint, "jm.waypoint.teleport_command", "/tp {name} {x} {y} {z}");

    public final StringField dateFormat = new StringField(ClientCategory.Waypoint, "jm.waypoint.death_date_format", DateFormat.Provider.class);

    public final StringField timeFormat = new StringField(ClientCategory.Waypoint, "jm.waypoint.death_time_format", TimeFormat.Provider.class);

    public final BooleanField managerDimensionFocus = new BooleanField(ClientCategory.Waypoint, "jm.waypoint.manager.focus_dim", false);

    @Override
    public String getName() {
        return "waypoint";
    }

    public int compareTo(WaypointProperties other) {
        return Integer.valueOf(this.hashCode()).compareTo(other.hashCode());
    }
}