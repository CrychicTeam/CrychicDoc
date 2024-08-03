package journeymap.common.properties;

import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.IntegerField;

public class GlobalProperties extends PermissionProperties {

    public final BooleanField journeymapEnabled = new BooleanField(Category.Inherit, "jm.server.allow_journeymap", true, 1).categoryMaster(true);

    public final BooleanField useWorldId = new BooleanField(Category.Inherit, "jm.server.edit.chkbox.world.id", true, 10);

    public final BooleanField viewOnlyServerProperties = new BooleanField(Category.Inherit, "jm.server.allow_view_server_prop", true, 15);

    public final EnumField<ServerOption> allowMultiplayerSettings = new EnumField<>(Category.Inherit, "jm.server.multiplayer_settings", ServerOption.ALL, 20);

    public final EnumField<ServerOption> worldPlayerRadar = new EnumField<>(Category.Inherit, "jm.server.edit.tracking", ServerOption.ALL, 20).setParent("radarEnabled", ServerOption.ALL);

    public final IntegerField worldPlayerRadarUpdateTime = new IntegerField(Category.Inherit, "jm.server.edit.slider.update", 1, 20, 5, 30).setParent("worldPlayerRadar", ServerOption.ALL);

    public final EnumField<ServerOption> seeUndergroundPlayers = new EnumField<>(Category.Inherit, "jm.server.see_underground_players", ServerOption.ALL, 42);

    public final BooleanField hideOps = new BooleanField(Category.Inherit, "jm.server.edit.hide_ops", false, 43);

    public final BooleanField hideSpectators = new BooleanField(Category.Inherit, "jm.server.radar_hide_spectators", false, 44);

    public final BooleanField allowDeathPoints = new BooleanField(Category.Inherit, "jm.server.allow_death_points", true, 44);

    public final BooleanField showInGameBeacons = new BooleanField(Category.Inherit, "jm.server.allow_in_game_beacons", true, 44);

    public final BooleanField allowWaypoints = new BooleanField(Category.Inherit, "jm.server.allow_waypoints", true, 44);

    public GlobalProperties() {
        super("Global Server Configuration", "Applies to all dimensions unless overridden.");
    }

    @Override
    public String getName() {
        return "global";
    }

    @Override
    protected void postLoad(boolean isNew) {
        if (!isNew) {
            this.serverOptionFix(this.worldPlayerRadar);
        }
        super.postLoad(isNew);
    }

    @Override
    protected void preSave() {
        super.preSave();
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        return ((GlobalProperties) clone).fromJsonString(this.toJsonString(false), this.getClass(), false);
    }
}