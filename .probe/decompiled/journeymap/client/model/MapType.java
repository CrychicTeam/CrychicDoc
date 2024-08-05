package journeymap.client.model;

import journeymap.client.api.display.Context;
import journeymap.client.data.DataCache;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.common.helper.DimensionHelper;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class MapType {

    public final Integer vSlice;

    public final MapType.Name name;

    public final ResourceKey<Level> dimension;

    public final Context.MapType apiMapType;

    private final int theHashCode;

    private final String theCacheKey;

    public MapType(MapType.Name name, Integer vSlice, ResourceKey<Level> dimension) {
        vSlice = name != MapType.Name.underground ? null : vSlice;
        this.name = name;
        this.vSlice = vSlice;
        this.dimension = dimension;
        this.apiMapType = this.toApiContextMapType(name);
        this.theCacheKey = toCacheKey(name, vSlice, dimension);
        this.theHashCode = this.theCacheKey.hashCode();
    }

    public static MapType from(MapType.Name name, Integer vSlice, ResourceKey<Level> dimension) {
        return DataCache.INSTANCE.getMapType(name, vSlice, dimension);
    }

    public static MapType from(Integer vSlice, ResourceKey<Level> dimension) {
        return from(vSlice == null ? MapType.Name.surface : MapType.Name.underground, vSlice, dimension);
    }

    public static MapType from(MapType.Name name, EntityDTO player) {
        return from(name, player.chunkCoordY, player.dimension);
    }

    public static MapType day(ResourceKey<Level> dimension) {
        return from(MapType.Name.day, null, dimension);
    }

    public static MapType day(EntityDTO player) {
        return from(MapType.Name.day, null, player.dimension);
    }

    public static MapType night(ResourceKey<Level> dimension) {
        return from(MapType.Name.night, null, dimension);
    }

    public static MapType night(EntityDTO player) {
        return from(MapType.Name.night, null, player.dimension);
    }

    public static MapType topo(ResourceKey<Level> dimension) {
        return from(MapType.Name.topo, null, dimension);
    }

    public static MapType topo(EntityDTO player) {
        return from(MapType.Name.topo, null, player.dimension);
    }

    public static MapType underground(EntityDTO player) {
        return from(MapType.Name.underground, player.chunkCoordY, player.dimension);
    }

    public static MapType biome(EntityDTO player) {
        return from(MapType.Name.biome, null, player.dimension);
    }

    public static MapType biome(ResourceKey<Level> dimension) {
        return from(MapType.Name.biome, null, dimension);
    }

    public static MapType underground(Integer vSlice, ResourceKey<Level> dimension) {
        return from(MapType.Name.underground, vSlice, dimension);
    }

    public static MapType none() {
        return from(MapType.Name.none, 0, Level.OVERWORLD);
    }

    public static String toCacheKey(MapType.Name name, Integer vSlice, ResourceKey<Level> dimension) {
        return String.format("%s|%s|%s", DimensionHelper.getDimName(dimension), name, vSlice == null ? "_" : vSlice);
    }

    private Context.MapType toApiContextMapType(MapType.Name name) {
        return switch(name) {
            case day ->
                Context.MapType.Day;
            case topo ->
                Context.MapType.Topo;
            case night ->
                Context.MapType.Night;
            case underground ->
                Context.MapType.Underground;
            case biome ->
                Context.MapType.Biome;
            default ->
                Context.MapType.Any;
        };
    }

    public static MapType fromApiContextMapType(Context.MapType apiMapType, Integer vSlice, ResourceKey<Level> dimension) {
        switch(apiMapType) {
            case Night:
                return new MapType(MapType.Name.night, vSlice, dimension);
            case Underground:
                return new MapType(MapType.Name.underground, vSlice, dimension);
            case Topo:
                return new MapType(MapType.Name.topo, vSlice, dimension);
            case Biome:
                return new MapType(MapType.Name.biome, vSlice, dimension);
            default:
                return new MapType(MapType.Name.day, vSlice, dimension);
        }
    }

    public String toCacheKey() {
        return this.theCacheKey;
    }

    public String toString() {
        return this.theCacheKey;
    }

    public String name() {
        return this.name.name();
    }

    public boolean isUnderground() {
        return this.name == MapType.Name.underground;
    }

    public boolean isSurface() {
        return this.name == MapType.Name.surface;
    }

    public boolean isSurfaceType() {
        return this.name == MapType.Name.surface || this.name == MapType.Name.day || this.name == MapType.Name.night || this.name == MapType.Name.biome;
    }

    public boolean isDay() {
        return this.name == MapType.Name.day;
    }

    public boolean isNight() {
        return this.name == MapType.Name.night;
    }

    public boolean isTopo() {
        return this.name == MapType.Name.topo;
    }

    public boolean isBiome() {
        return this.name == MapType.Name.biome;
    }

    public boolean isDayOrNight() {
        return this.name == MapType.Name.day || this.name == MapType.Name.night;
    }

    public boolean isAllowed() {
        if (this.isUnderground()) {
            return FeatureManager.getInstance().isAllowed(Feature.MapCaves);
        } else if (this.isTopo()) {
            return FeatureManager.getInstance().isAllowed(Feature.MapTopo);
        } else if (this.isDayOrNight() || this.isSurface()) {
            return FeatureManager.getInstance().isAllowed(Feature.MapSurface);
        } else {
            return this.isBiome() ? FeatureManager.getInstance().isAllowed(Feature.MapBiome) : this.name == MapType.Name.none;
        }
    }

    public int hashCode() {
        return this.theHashCode;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            MapType mapType = (MapType) o;
            if (this.dimension != mapType.dimension) {
                return false;
            } else if (this.name != mapType.name) {
                return false;
            } else {
                return this.vSlice != null ? this.vSlice.equals(mapType.vSlice) : mapType.vSlice == null;
            }
        } else {
            return false;
        }
    }

    public static enum Name {

        day,
        night,
        underground,
        surface,
        topo,
        biome,
        none
    }
}