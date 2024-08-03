package journeymap.common.properties;

import journeymap.common.properties.config.BooleanField;

public class MultiplayerProperties extends ServerPropertiesBase {

    public final BooleanField visible = new BooleanField(MultiplayerCategory.Radar, "jm.options.multiplayer.radar.visible", true, 10);

    public final BooleanField hideSelfUnderground = new BooleanField(MultiplayerCategory.Radar, "jm.options.multiplayer.radar.hide_self_underground", false, 15);

    public MultiplayerProperties() {
        super("displayName", "description");
    }

    @Override
    public String getName() {
        return "mp";
    }

    @Override
    public boolean save() {
        return true;
    }
}