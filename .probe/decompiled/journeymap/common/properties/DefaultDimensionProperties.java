package journeymap.common.properties;

import journeymap.common.properties.config.BooleanField;

public class DefaultDimensionProperties extends PermissionProperties {

    public final BooleanField enabled = new BooleanField(ServerCategory.Default, "jm.server.edit.chkbox.enable", false, 1).categoryMaster(true);

    public DefaultDimensionProperties() {
        super("default", "jm.server.edit.label.selection.default");
    }

    @Override
    public String getName() {
        return "default";
    }
}