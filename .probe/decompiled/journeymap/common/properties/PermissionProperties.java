package journeymap.common.properties;

import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.IntegerField;

public abstract class PermissionProperties extends ServerPropertiesBase {

    public final BooleanField teleportEnabled = new BooleanField(Category.Inherit, "jm.server.edit.chkbox.teleport", false, 45);

    public final IntegerField renderRange = new IntegerField(Category.Inherit, "jm.server.render_range", 0, 32, 0, 49);

    public final EnumField<ServerOption> surfaceMapping = new EnumField<>(Category.Inherit, "jm.server.edit.mapping.toggle.surface", ServerOption.ALL, 50);

    public final EnumField<ServerOption> topoMapping = new EnumField<>(Category.Inherit, "jm.server.edit.mapping.toggle.topo", ServerOption.ALL, 55);

    public final EnumField<ServerOption> biomeMapping = new EnumField<>(Category.Inherit, "jm.server.edit.mapping.toggle.biome", ServerOption.ALL, 55);

    public final EnumField<ServerOption> caveMapping = new EnumField<>(Category.Inherit, "jm.server.edit.mapping.toggle.cave", ServerOption.ALL, 60);

    public final EnumField<ServerOption> radarEnabled = new EnumField<>(Category.Inherit, "jm.server.edit.radar", ServerOption.ALL, 80);

    public final BooleanField playerRadarEnabled = new BooleanField(Category.Inherit, "jm.server.edit.radar.chkbox.player", true, 81).setParent("radarEnabled", ServerOption.ALL);

    public final BooleanField villagerRadarEnabled = new BooleanField(Category.Inherit, "jm.server.edit.radar.chkbox.villager", true, 82).setParent("radarEnabled", ServerOption.ALL);

    public final BooleanField animalRadarEnabled = new BooleanField(Category.Inherit, "jm.server.edit.radar.chkbox.animal", true, 83).setParent("radarEnabled", ServerOption.ALL);

    public final BooleanField mobRadarEnabled = new BooleanField(Category.Inherit, "jm.server.edit.radar.chkbox.mob", true, 84).setParent("radarEnabled", ServerOption.ALL);

    protected PermissionProperties(String displayName, String description) {
        super(displayName, description);
    }

    @Override
    protected void postLoad(boolean isNew) {
        if (!isNew) {
            this.serverOptionFix(this.radarEnabled);
            this.serverOptionFix(this.surfaceMapping);
            this.serverOptionFix(this.topoMapping);
            this.serverOptionFix(this.caveMapping);
        }
        super.postLoad(isNew);
    }

    protected void serverOptionFix(EnumField<ServerOption> option) {
        if ("true".equals(option.get("value").toString()) || "false".equals(option.get("value").toString())) {
            option.set(Boolean.parseBoolean(option.get("value").toString()) ? ServerOption.ALL : ServerOption.NONE);
            this.save();
        }
    }
}