package journeymap.client.feature;

import java.util.EnumSet;
import journeymap.client.api.display.Context;
import journeymap.client.model.MapType;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public enum Feature {

    RadarPlayers,
    RadarAnimals,
    RadarMobs,
    RadarVillagers,
    MapTopo,
    MapSurface,
    MapCaves,
    MapBiome;

    public static EnumSet<Feature> radar() {
        return EnumSet.of(RadarPlayers, RadarAnimals, RadarMobs, RadarVillagers);
    }

    public static EnumSet<Feature> all() {
        return EnumSet.allOf(Feature.class);
    }

    public static Feature fromMapType(MapType mapType) {
        MapType.Name name = mapType.name;
        return switch(name) {
            case topo ->
                MapTopo;
            case underground ->
                MapCaves;
            case biome ->
                MapBiome;
            default ->
                MapSurface;
        };
    }

    public static Feature fromApiMapType(Context.MapType mapType, ResourceKey<Level> dimension) {
        return fromMapType(MapType.fromApiContextMapType(mapType, 0, dimension == null ? Minecraft.getInstance().player.m_9236_().dimension() : dimension));
    }
}