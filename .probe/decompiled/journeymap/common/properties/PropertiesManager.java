package journeymap.common.properties;

import java.util.HashMap;
import java.util.Map;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class PropertiesManager {

    private static PropertiesManager INSTANCE;

    private Map<ResourceKey<Level>, DimensionProperties> dimensionProperties;

    private GlobalProperties globalProperties;

    private DefaultDimensionProperties defaultDimensionProperties;

    public static PropertiesManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PropertiesManager();
            INSTANCE.loadConfigs();
        }
        return INSTANCE;
    }

    private void loadConfigs() {
        this.dimensionProperties = new HashMap();
        this.globalProperties = new GlobalProperties();
        this.globalProperties.load();
        this.defaultDimensionProperties = new DefaultDimensionProperties();
        this.defaultDimensionProperties.load();
        for (ResourceKey<Level> dim : DimensionHelper.getServerDimNameList()) {
            this.genConfig(dim);
        }
    }

    public void reloadConfigs() {
        this.loadConfigs();
    }

    public DimensionProperties getDimProperties(ResourceKey<Level> dim) {
        if (this.dimensionProperties.get(dim) == null) {
            this.genConfig(dim);
        }
        return (DimensionProperties) this.dimensionProperties.get(dim);
    }

    public DefaultDimensionProperties getDefaultDimensionProperties() {
        return this.defaultDimensionProperties;
    }

    public GlobalProperties getGlobalProperties() {
        return this.globalProperties;
    }

    private void genConfig(ResourceKey<Level> dim) {
        DimensionProperties prop = new DimensionProperties(dim);
        this.dimensionProperties.put(dim, prop);
        if (!prop.getFile().exists()) {
            prop.build();
        }
        prop.load();
    }
}