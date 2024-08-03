package journeymap.common.properties;

import journeymap.common.helper.DimensionHelper;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class DimensionProperties extends PermissionProperties {

    public final BooleanField enabled = new BooleanField(Category.Inherit, "jm.server.edit.chkbox.enable", false, 1).categoryMaster(true);

    protected final ResourceKey<Level> dimension;

    public DimensionProperties(ResourceKey<Level> dimension) {
        super(String.format("Dimension %s Configuration", dimension), "Overrides the Global Server Configuration for this dimension - sent enable true to override global settings for this dim");
        this.dimension = dimension;
    }

    public DimensionProperties(String name) {
        this(DimensionHelper.getWorldKeyForName(name));
    }

    @Override
    public String getName() {
        return DimensionHelper.getSafeDimName(this.dimension);
    }

    public ResourceKey<Level> getDimension() {
        return this.dimension;
    }

    public DimensionProperties build() {
        DefaultDimensionProperties defaultProp = PropertiesManager.getInstance().getDefaultDimensionProperties();
        this.teleportEnabled.set(defaultProp.teleportEnabled.get());
        this.enabled.set(defaultProp.enabled.get());
        this.caveMapping.set(defaultProp.caveMapping.get());
        this.topoMapping.set(defaultProp.topoMapping.get());
        this.surfaceMapping.set(defaultProp.surfaceMapping.get());
        this.radarEnabled.set(defaultProp.radarEnabled.get());
        this.playerRadarEnabled.set(defaultProp.playerRadarEnabled.get());
        this.villagerRadarEnabled.set(defaultProp.villagerRadarEnabled.get());
        this.animalRadarEnabled.set(defaultProp.animalRadarEnabled.get());
        this.mobRadarEnabled.set(defaultProp.mobRadarEnabled.get());
        this.save();
        return this;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        Object clone = super.clone();
        return ((DimensionProperties) clone).fromJsonString(this.toJsonString(false), this.getClass(), false);
    }
}