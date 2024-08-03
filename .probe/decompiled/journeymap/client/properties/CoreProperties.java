package journeymap.client.properties;

import java.util.Arrays;
import java.util.HashMap;
import journeymap.client.cartography.color.RGB;
import journeymap.client.io.ThemeLoader;
import journeymap.client.log.JMLogger;
import journeymap.client.model.GridSpecs;
import journeymap.client.task.multi.RenderSpec;
import journeymap.common.properties.PropertiesBase;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.BooleanField;
import journeymap.common.properties.config.EnumField;
import journeymap.common.properties.config.IntegerField;
import journeymap.common.properties.config.StringField;
import net.minecraft.client.Minecraft;

public class CoreProperties extends ClientPropertiesBase implements Comparable<CoreProperties> {

    public static final String PATTERN_COLOR = "^#[a-f0-9]{6}$";

    public final StringField logLevel = new StringField(ClientCategory.Advanced, "jm.advanced.loglevel", JMLogger.LogLevelStringProvider.class);

    public final IntegerField autoMapPoll = new IntegerField(ClientCategory.Advanced, "jm.advanced.automappoll", 500, 10000, 2000);

    public final IntegerField cacheAnimalsData = new IntegerField(ClientCategory.Advanced, "jm.advanced.cache_animals", 1000, 10000, 3100);

    public final IntegerField cacheMobsData = new IntegerField(ClientCategory.Advanced, "jm.advanced.cache_mobs", 1000, 10000, 3000);

    public final IntegerField cachePlayerData = new IntegerField(ClientCategory.Advanced, "jm.advanced.cache_player", 500, 2000, 1000);

    public final IntegerField cachePlayersData = new IntegerField(ClientCategory.Advanced, "jm.advanced.cache_players", 1000, 10000, 2000);

    public final IntegerField cacheVillagersData = new IntegerField(ClientCategory.Advanced, "jm.advanced.cache_villagers", 1000, 10000, 2200);

    public final BooleanField announceMod = new BooleanField(ClientCategory.Advanced, "jm.advanced.announcemod", true);

    public final BooleanField checkUpdates = new BooleanField(ClientCategory.Advanced, "jm.advanced.checkupdates", true);

    public final BooleanField recordCacheStats = new BooleanField(ClientCategory.Advanced, "jm.advanced.recordcachestats", false);

    public final StringField themeName = new StringField(ClientCategory.FullMap, "jm.common.ui_theme", ThemeLoader.ThemeValuesProvider.class);

    public final BooleanField caveIgnoreGlass = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_caveignoreglass", true);

    public final BooleanField mapBathymetry = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_bathymetry", false);

    public final BooleanField mapWaterBiomeColors = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_biomewatercolor", false);

    public final BooleanField mapTopography = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_topography", true);

    public final BooleanField mapBiome = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_biome", true);

    public final BooleanField mapTransparency = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_transparency", true);

    public final BooleanField mapCaveLighting = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_cavelighting", true);

    public final BooleanField mapAntialiasing = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_antialiasing", true);

    public final BooleanField mapPlantShadows = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_plantshadows", false);

    public final BooleanField mapPlants = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_plants", false);

    public final BooleanField mapCrops = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_crops", true);

    public final BooleanField mapBlendGrass = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_blendgrass", true);

    public final BooleanField mapBlendFoliage = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_blendfoliage", true);

    public final BooleanField mapBlendWater = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_blendwater", false);

    public final BooleanField mapSurfaceAboveCaves = new BooleanField(ClientCategory.Cartography, "jm.common.map_style_caveshowsurface", true);

    public final BooleanField caveBlackAsClear = new BooleanField(ClientCategory.Cartography, "jm.common.render.clear_caves", false);

    public final IntegerField renderDistanceCaveMax = new IntegerField(ClientCategory.Cartography, "jm.common.renderdistance_cave_max", 0, 32, 0, 102);

    public final IntegerField renderDistanceSurfaceMax = new IntegerField(ClientCategory.Cartography, "jm.common.renderdistance_surface_max", 0, 32, 0, 104);

    public final IntegerField renderDelay = new IntegerField(ClientCategory.Cartography, "jm.common.renderdelay", 0, 10, 2);

    public final EnumField<RenderSpec.RevealShape> revealShape = new EnumField<>(ClientCategory.Cartography, "jm.common.revealshape", RenderSpec.RevealShape.Circle);

    public final BooleanField alwaysMapCaves = new BooleanField(ClientCategory.Cartography, "jm.common.alwaysmapcaves", false);

    public final BooleanField alwaysMapSurface = new BooleanField(ClientCategory.Cartography, "jm.common.alwaysmapsurface", false);

    public final BooleanField tileHighDisplayQuality = new BooleanField(ClientCategory.Advanced, "jm.common.tile_display_quality", true);

    public final IntegerField maxAnimalsData = new IntegerField(ClientCategory.Advanced, "jm.common.radar_max_animals", 1, 128, 32);

    public final IntegerField maxMobsData = new IntegerField(ClientCategory.Advanced, "jm.common.radar_max_mobs", 1, 128, 32);

    public final IntegerField maxPlayersData = new IntegerField(ClientCategory.Advanced, "jm.common.radar_max_players", 1, 128, 32);

    public final IntegerField maxVillagersData = new IntegerField(ClientCategory.Advanced, "jm.common.radar_max_villagers", 1, 128, 32);

    public final BooleanField hideSneakingEntities = new BooleanField(ClientCategory.Advanced, "jm.common.radar_hide_sneaking", true);

    public final BooleanField hideSpectators = new BooleanField(ClientCategory.Advanced, "jm.common.radar_hide_spectators", false);

    public final IntegerField radarLateralDistance = new IntegerField(ClientCategory.Advanced, "jm.common.radar_lateral_distance", 16, 512, 64);

    public final IntegerField radarVerticalDistance = new IntegerField(ClientCategory.Advanced, "jm.common.radar_vertical_distance", 8, 256, 16);

    public final IntegerField tileRenderType = new IntegerField(ClientCategory.Advanced, "jm.advanced.tile_render_type", 1, 4, 1);

    public final BooleanField dataCachingEnabled = new BooleanField(ClientCategory.Advanced, "jm.advanced.chunk_caching", true);

    public final BooleanField glErrorChecking = new BooleanField(ClientCategory.Advanced, "jm.advanced.gl_error_check", false);

    public final BooleanField seedId = new BooleanField(ClientCategory.Advanced, "jm.advanced.seed_id", false);

    public final BooleanField mappingEnabled = new BooleanField(Category.Hidden, "", true);

    public final StringField optionsManagerViewed = new StringField(Category.Hidden, "", null);

    public final StringField splashViewed = new StringField(Category.Hidden, "", null);

    public final GridSpecs gridSpecs = new GridSpecs();

    public final StringField colorPassive = new StringField(Category.Hidden, "jm.common.radar_color_passive", null, "#bbbbbb").pattern("^#[a-f0-9]{6}$");

    public final StringField colorHostile = new StringField(Category.Hidden, "jm.common.radar_color_hostile", null, "#ff0000").pattern("^#[a-f0-9]{6}$");

    public final StringField colorPet = new StringField(Category.Hidden, "jm.common.radar_color_pet", null, "#0077ff").pattern("^#[a-f0-9]{6}$");

    public final StringField colorVillager = new StringField(Category.Hidden, "jm.common.radar_color_villager", null, "#88e188").pattern("^#[a-f0-9]{6}$");

    public final StringField colorPlayer = new StringField(Category.Hidden, "jm.common.radar_color_player", null, "#ffffff").pattern("^#[a-f0-9]{6}$");

    public final StringField colorSelf = new StringField(Category.Hidden, "jm.common.radar_color_self", null, "#0000ff").pattern("^#[a-f0-9]{6}$");

    public final BooleanField verboseColorPalette = new BooleanField(Category.Hidden, "", false);

    private transient HashMap<StringField, Integer> mobColors = new HashMap(6);

    @Override
    public String getName() {
        return "core";
    }

    public int compareTo(CoreProperties other) {
        return Integer.valueOf(this.hashCode()).compareTo(other.hashCode());
    }

    @Override
    public <T extends PropertiesBase> void updateFrom(T otherInstance) {
        super.updateFrom(otherInstance);
        if (otherInstance instanceof CoreProperties) {
            this.gridSpecs.updateFrom(((CoreProperties) otherInstance).gridSpecs);
        }
        this.mobColors.clear();
    }

    @Override
    public boolean isValid(boolean fix) {
        boolean valid = super.isValid(fix);
        if (Minecraft.getInstance() != null) {
            int gameRenderDistance = Minecraft.getInstance().options.renderDistance().get();
            for (IntegerField prop : Arrays.asList(this.renderDistanceCaveMax, this.renderDistanceSurfaceMax)) {
                if (prop.get() > gameRenderDistance) {
                    this.warn(String.format("Render distance %s is less than %s", gameRenderDistance, prop.getDeclaredField()));
                    if (fix) {
                        prop.set(Integer.valueOf(gameRenderDistance));
                    } else {
                        valid = false;
                    }
                }
            }
        }
        return valid;
    }

    public int getColor(StringField colorField) {
        Integer color = (Integer) this.mobColors.get(colorField);
        if (color == null) {
            color = RGB.hexToInt(colorField.get());
            this.mobColors.put(colorField, color);
        }
        return color;
    }
}