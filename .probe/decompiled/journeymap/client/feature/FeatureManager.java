package journeymap.client.feature;

import java.util.HashMap;
import java.util.Map;
import journeymap.client.api.display.Context;
import journeymap.client.data.DataCache;
import journeymap.client.model.MapType;
import journeymap.client.ui.fullscreen.Fullscreen;
import journeymap.client.ui.minimap.MiniMap;
import journeymap.common.Journeymap;
import journeymap.common.properties.GlobalProperties;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class FeatureManager {

    private static FeatureManager INSTANCE;

    private final HashMap<Feature, Policy> policyMap = new HashMap();

    private FeatureManager() {
        this.reset();
    }

    public static FeatureManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new FeatureManager();
        }
        return INSTANCE;
    }

    public String getPolicyDetails() {
        StringBuilder sb = new StringBuilder("Features: ");
        for (Feature feature : Feature.values()) {
            boolean single = false;
            boolean multi = false;
            if (INSTANCE.policyMap.containsKey(feature)) {
                single = ((Policy) INSTANCE.policyMap.get(feature)).allowInSingleplayer;
                multi = ((Policy) INSTANCE.policyMap.get(feature)).allowInMultiplayer;
            }
            sb.append(String.format("\n\t%s : singleplayer = %s , multiplayer = %s", feature.name(), single, multi));
        }
        return sb.toString();
    }

    public boolean isAllowed(Feature feature) {
        Policy policy = (Policy) INSTANCE.policyMap.get(feature);
        return policy != null && policy.isCurrentlyAllowed();
    }

    public Map<Feature, Boolean> getAllowedFeatures() {
        Map<Feature, Boolean> map = new HashMap(Feature.values().length * 2);
        for (Feature feature : Feature.values()) {
            map.put(feature, this.isAllowed(feature));
        }
        return map;
    }

    public void updateDimensionFeatures(GlobalProperties properties) {
        if (((Policy) this.policyMap.get(Feature.MapSurface)).isCurrentlyAllowed() && !properties.surfaceMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature disabled: " + Feature.MapSurface);
            this.nextMapType(Feature.MapSurface);
            this.policyMap.put(Feature.MapSurface, new Policy(Feature.MapSurface, false, false));
        } else if (!((Policy) this.policyMap.get(Feature.MapSurface)).isCurrentlyAllowed() && properties.surfaceMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature enabled: " + Feature.MapSurface);
            this.policyMap.put(Feature.MapSurface, new Policy(Feature.MapSurface, true, true));
            if (MapType.none().equals(Fullscreen.state().getMapType())) {
                long time = ((LivingEntity) DataCache.getPlayer().entityLivingRef.get()).m_9236_().getLevelData().getGameTime() % 24000L;
                MapType mapType = time < 13800L ? MapType.day(DataCache.getPlayer()) : MapType.night(DataCache.getPlayer());
                this.setMapType(mapType);
            }
        }
        if (((Policy) this.policyMap.get(Feature.MapTopo)).isCurrentlyAllowed() && !properties.topoMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature disabled: " + Feature.MapTopo);
            this.nextMapType(Feature.MapTopo);
            this.policyMap.put(Feature.MapTopo, new Policy(Feature.MapTopo, false, false));
        } else if (!((Policy) this.policyMap.get(Feature.MapTopo)).isCurrentlyAllowed() && properties.topoMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature enabled: " + Feature.MapTopo);
            this.policyMap.put(Feature.MapTopo, new Policy(Feature.MapTopo, true, true));
            if (MapType.none().equals(Fullscreen.state().getMapType())) {
                this.setMapType(MapType.topo(DataCache.getPlayer()));
            }
        }
        if (((Policy) this.policyMap.get(Feature.MapBiome)).isCurrentlyAllowed() && !properties.biomeMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature disabled: " + Feature.MapBiome);
            this.nextMapType(Feature.MapBiome);
            this.policyMap.put(Feature.MapBiome, new Policy(Feature.MapBiome, false, false));
        } else if (!((Policy) this.policyMap.get(Feature.MapBiome)).isCurrentlyAllowed() && properties.biomeMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature enabled: " + Feature.MapBiome);
            this.policyMap.put(Feature.MapBiome, new Policy(Feature.MapBiome, true, true));
            if (MapType.none().equals(Fullscreen.state().getMapType())) {
                this.setMapType(MapType.biome(DataCache.getPlayer()));
            }
        }
        if (((Policy) this.policyMap.get(Feature.MapCaves)).isCurrentlyAllowed() && !properties.caveMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature disabled: " + Feature.MapCaves);
            this.nextMapType(Feature.MapCaves);
            this.policyMap.put(Feature.MapCaves, new Policy(Feature.MapCaves, false, false));
        } else if (!((Policy) this.policyMap.get(Feature.MapCaves)).isCurrentlyAllowed() && properties.caveMapping.get().enabled()) {
            Journeymap.getLogger().info("Feature enabled: " + Feature.MapCaves);
            this.policyMap.put(Feature.MapCaves, new Policy(Feature.MapCaves, true, true));
            if (MapType.none().equals(Fullscreen.state().getMapType())) {
                this.setMapType(MapType.underground(DataCache.getPlayer()));
            }
        }
        if (properties.radarEnabled.get().enabled()) {
            this.setMultiplayerFeature(Feature.RadarAnimals, properties.animalRadarEnabled.get());
            this.setMultiplayerFeature(Feature.RadarMobs, properties.mobRadarEnabled.get());
            this.setMultiplayerFeature(Feature.RadarPlayers, properties.playerRadarEnabled.get());
            this.setMultiplayerFeature(Feature.RadarVillagers, properties.villagerRadarEnabled.get());
        } else {
            this.setMultiplayerFeature(Feature.RadarAnimals, false);
            this.setMultiplayerFeature(Feature.RadarMobs, false);
            this.setMultiplayerFeature(Feature.RadarPlayers, false);
            this.setMultiplayerFeature(Feature.RadarVillagers, false);
        }
    }

    private void nextMapType(Feature feature) {
        if (((Policy) this.policyMap.get(feature)).isCurrentlyAllowed() && Fullscreen.state() != null) {
            if (Fullscreen.state().isSurfaceMappingAllowed() && !Feature.MapSurface.equals(feature)) {
                this.setMapType(MapType.day(DataCache.getPlayer()));
            } else if (Fullscreen.state().isTopoMappingAllowed() && !Feature.MapTopo.equals(feature)) {
                this.setMapType(MapType.topo(DataCache.getPlayer()));
            } else if (Fullscreen.state().isCaveMappingAllowed() && !Feature.MapCaves.equals(feature)) {
                this.setMapType(MapType.underground(DataCache.getPlayer()));
            } else if (Fullscreen.state().isBiomeMappingAllowed() && !Feature.MapBiome.equals(feature)) {
                this.setMapType(MapType.biome(DataCache.getPlayer()));
            }
        }
    }

    private void setMapType(MapType to) {
        Fullscreen.state().setMapType(to);
        MiniMap.state().setMapType(to);
    }

    private void setMultiplayerFeature(Feature feature, boolean enable) {
        if (!enable && ((Policy) this.policyMap.get(feature)).isCurrentlyAllowed()) {
            Journeymap.getLogger().info("Feature disabled: " + feature);
        } else if (enable && !((Policy) this.policyMap.get(feature)).isCurrentlyAllowed()) {
            Journeymap.getLogger().info("Feature enabled: " + feature);
        }
        this.policyMap.put(feature, new Policy(feature, true, enable));
    }

    public void reset() {
        for (Policy policy : Policy.bulkCreate(true, true)) {
            this.policyMap.put(policy.feature, policy);
        }
    }

    public void disableFeature(Context.MapType apiMapType, Context.UI mapUI, ResourceKey<Level> dimension) {
        Feature feature = Feature.fromApiMapType(apiMapType, dimension);
        this.nextMapType(feature);
        Journeymap.getLogger().info("Feature disabled: " + feature);
        this.policyMap.put(feature, new Policy(feature, false, false));
    }
}