package journeymap.client.model;

import com.google.common.base.Objects;
import com.google.common.collect.Iterables;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import journeymap.client.JourneymapClient;
import journeymap.client.api.impl.ClientAPI;
import journeymap.client.data.DataCache;
import journeymap.client.feature.Feature;
import journeymap.client.feature.FeatureManager;
import journeymap.client.io.FileHandler;
import journeymap.client.log.StatTimer;
import journeymap.client.properties.CoreProperties;
import journeymap.client.properties.InGameMapProperties;
import journeymap.client.properties.MapProperties;
import journeymap.client.render.draw.DrawStep;
import journeymap.client.render.draw.DrawWayPointStep;
import journeymap.client.render.draw.RadarDrawStepFactory;
import journeymap.client.render.draw.WaypointDrawStepFactory;
import journeymap.client.render.map.GridRenderer;
import journeymap.client.task.multi.MapPlayerTask;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import journeymap.common.properties.catagory.Category;
import journeymap.common.properties.config.IntegerField;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

public class MapState {

    public final int minZoom = 0;

    public final int maxZoom = 5;

    public AtomicBoolean follow = new AtomicBoolean(true);

    public String playerLastPos = "0,0";

    private StatTimer refreshTimer = StatTimer.get("MapState.refresh");

    private StatTimer generateDrawStepsTimer = StatTimer.get("MapState.generateDrawSteps");

    private MapType lastMapType;

    private File worldDir = null;

    private long lastRefresh = 0L;

    private long lastMapTypeChange = 0L;

    private IntegerField lastSlice = new IntegerField(Category.Hidden, "", -4, 15, 4);

    private boolean surfaceMappingAllowed = false;

    private boolean caveMappingAllowed = false;

    private boolean caveMappingEnabled = false;

    private boolean topoMappingAllowed = false;

    private boolean biomeMappingAllowed = false;

    private List<DrawStep> drawStepList = new ArrayList();

    private List<DrawWayPointStep> drawWaypointStepList = new ArrayList();

    private String playerBiome = "";

    private InGameMapProperties lastMapProperties = null;

    private List<EntityDTO> entityList = new ArrayList(32);

    private int lastPlayerChunkX = 0;

    private int lastPlayerChunkY = 0;

    private int lastPlayerChunkZ = 0;

    private boolean highQuality;

    private String lastWorldName;

    private boolean forceRefreshState = false;

    public void refresh(Minecraft mc, Player player, InGameMapProperties mapProperties) {
        Level world = mc.level;
        if (world != null && world.dimensionType() != null) {
            this.refreshTimer.start();
            try {
                CoreProperties coreProperties = JourneymapClient.getInstance().getCoreProperties();
                this.lastMapProperties = mapProperties;
                if (!FileHandler.getWorldDirectoryName(mc).equals(this.lastWorldName)) {
                    this.worldDir = FileHandler.getJMWorldDir(mc);
                    this.lastWorldName = FileHandler.getWorldDirectoryName(mc);
                }
                if (world != null && world.dimensionType().logicalHeight() != 256 && this.lastSlice.getMaxValue() != world.dimensionType().logicalHeight() / 16 - 1) {
                    int maxSlice = world.dimensionType().logicalHeight() / 16 - 1;
                    int minSlice = world.dimensionType().minY() / 16;
                    int seaLevel = Math.round((float) (world.getSeaLevel() >> 4));
                    int currentSlice = this.lastSlice.get();
                    this.lastSlice = new IntegerField(Category.Hidden, "", minSlice, maxSlice, seaLevel);
                    this.lastSlice.set(Integer.valueOf(currentSlice));
                }
                boolean hasSurface = !world.dimension().equals(Level.NETHER);
                this.caveMappingAllowed = FeatureManager.getInstance().isAllowed(Feature.MapCaves);
                this.caveMappingEnabled = this.caveMappingAllowed && mapProperties.showCaves.get();
                this.surfaceMappingAllowed = hasSurface && FeatureManager.getInstance().isAllowed(Feature.MapSurface);
                this.topoMappingAllowed = hasSurface && FeatureManager.getInstance().isAllowed(Feature.MapTopo) && coreProperties.mapTopography.get();
                this.biomeMappingAllowed = hasSurface && FeatureManager.getInstance().isAllowed(Feature.MapBiome) && coreProperties.mapBiome.get();
                this.highQuality = coreProperties.tileHighDisplayQuality.get();
                this.lastPlayerChunkX = player.m_146902_().x;
                this.lastPlayerChunkY = player.m_146904_() >> 4;
                this.lastPlayerChunkZ = player.m_146902_().z;
                EntityDTO playerDTO = DataCache.getPlayer();
                this.playerBiome = playerDTO.biome;
                if (this.lastMapType != null) {
                    if (player.m_9236_().dimension() != this.lastMapType.dimension) {
                        this.lastMapType = null;
                    } else if (this.caveMappingEnabled && this.follow.get() && playerDTO.underground && !this.lastMapType.isUnderground()) {
                        this.lastMapType = null;
                    } else if (this.caveMappingEnabled && this.follow.get() && !playerDTO.underground && this.lastMapType.isUnderground()) {
                        this.lastMapType = null;
                    } else if (!this.lastMapType.isAllowed()) {
                        this.lastMapType = null;
                    }
                }
                this.lastMapType = this.getMapType();
                this.updateLastRefresh();
            } catch (Exception var13) {
                Journeymap.getLogger().error("Error refreshing MapState: " + LogFormatter.toPartialString(var13));
            } finally {
                this.refreshTimer.stop();
            }
        }
    }

    public MapType setMapType(MapType.Name mapTypeName) {
        return this.setMapType(MapType.from(mapTypeName, DataCache.getPlayer()));
    }

    public MapType toggleMapType() {
        MapType.Name next = this.getNextMapType(this.getMapType().name);
        return this.setMapType(next);
    }

    public MapType.Name getNextMapType(MapType.Name name) {
        EntityDTO player = DataCache.getPlayer();
        LivingEntity playerEntity = (LivingEntity) player.entityLivingRef.get();
        if (playerEntity == null) {
            return name;
        } else {
            List<MapType.Name> types = new ArrayList(4);
            if (this.surfaceMappingAllowed) {
                types.add(MapType.Name.day);
                types.add(MapType.Name.night);
            }
            if (this.caveMappingAllowed && (player.underground || name == MapType.Name.underground)) {
                types.add(MapType.Name.underground);
            }
            if (this.topoMappingAllowed) {
                types.add(MapType.Name.topo);
            }
            if (this.biomeMappingAllowed) {
                types.add(MapType.Name.biome);
            }
            if (name == MapType.Name.none && !types.isEmpty()) {
                return (MapType.Name) types.get(0);
            } else {
                if (types.contains(name)) {
                    Iterator<MapType.Name> cyclingIterator = Iterables.cycle(types).iterator();
                    while (cyclingIterator.hasNext()) {
                        MapType.Name current = (MapType.Name) cyclingIterator.next();
                        if (current == name) {
                            return (MapType.Name) cyclingIterator.next();
                        }
                    }
                }
                return name;
            }
        }
    }

    public MapType setMapType(MapType mapType) {
        if (!mapType.isAllowed()) {
            mapType = MapType.from(this.getNextMapType(mapType.name), DataCache.getPlayer());
            if (!mapType.isAllowed()) {
                mapType = MapType.none();
            }
        }
        EntityDTO player = DataCache.getPlayer();
        if (player.underground != mapType.isUnderground()) {
            this.follow.set(false);
        }
        if (mapType.isUnderground()) {
            if (player.chunkCoordY != mapType.vSlice) {
                this.follow.set(false);
            }
            this.lastSlice.set(mapType.vSlice);
        } else if (this.lastMapProperties != null && mapType.name != MapType.Name.none && this.lastMapProperties.preferredMapType.get() != mapType.name) {
            this.lastMapProperties.preferredMapType.set(mapType.name);
            this.lastMapProperties.save();
        }
        this.setLastMapTypeChange(mapType);
        return this.lastMapType;
    }

    public MapType getMapType() {
        if (this.lastMapType == null) {
            EntityDTO player = DataCache.getPlayer();
            MapType mapType = null;
            try {
                if (this.caveMappingEnabled && player.underground) {
                    mapType = MapType.underground(player);
                } else if (this.follow.get()) {
                    if (this.surfaceMappingAllowed && !player.underground) {
                        mapType = MapType.day(player);
                    } else if (this.topoMappingAllowed && !player.underground) {
                        mapType = MapType.topo(player);
                    } else if (this.biomeMappingAllowed && !player.underground) {
                        mapType = MapType.biome(player);
                    }
                }
                if (mapType == null) {
                    mapType = MapType.from(this.lastMapProperties.preferredMapType.get(), player);
                }
            } catch (Exception var4) {
                mapType = MapType.day(player);
            }
            this.setMapType(mapType);
        }
        return this.lastMapType;
    }

    public long getLastMapTypeChange() {
        return this.lastMapTypeChange;
    }

    private void setLastMapTypeChange(MapType mapType) {
        if (!Objects.equal(mapType, this.lastMapType)) {
            this.lastMapTypeChange = System.currentTimeMillis();
            this.requireRefresh();
        }
        this.lastMapType = mapType;
    }

    public boolean isUnderground() {
        return this.getMapType().isUnderground();
    }

    public File getWorldDir() {
        return this.worldDir;
    }

    public String getPlayerBiome() {
        return this.playerBiome;
    }

    public List<? extends DrawStep> getDrawSteps() {
        return this.drawStepList;
    }

    public List<DrawWayPointStep> getDrawWaypointSteps() {
        return this.drawWaypointStepList;
    }

    public void generateDrawSteps(Minecraft mc, GridRenderer gridRenderer, WaypointDrawStepFactory waypointRenderer, RadarDrawStepFactory radarRenderer, InGameMapProperties mapProperties, boolean checkWaypointDistance) {
        this.generateDrawStepsTimer.start();
        this.lastMapProperties = mapProperties;
        this.drawStepList.clear();
        this.drawWaypointStepList.clear();
        this.entityList.clear();
        ClientAPI.INSTANCE.getDrawSteps(this.drawStepList, gridRenderer.getUIState());
        if (FeatureManager.getInstance().isAllowed(Feature.RadarAnimals) && (mapProperties.showAnimals.get() || mapProperties.showPets.get())) {
            this.entityList.addAll(DataCache.INSTANCE.getAnimals(false).values());
        }
        if (FeatureManager.getInstance().isAllowed(Feature.RadarVillagers) && mapProperties.showVillagers.get()) {
            this.entityList.addAll(DataCache.INSTANCE.getVillagers(false).values());
        }
        if (FeatureManager.getInstance().isAllowed(Feature.RadarMobs) && mapProperties.showMobs.get()) {
            this.entityList.addAll(DataCache.INSTANCE.getMobs(false).values());
        }
        if (FeatureManager.getInstance().isAllowed(Feature.RadarPlayers) && mapProperties.showPlayers.get()) {
            mc.player.m_9236_().m_6907_();
            Collection<EntityDTO> cachedPlayers = DataCache.INSTANCE.getPlayers(false).values();
            if (cachedPlayers.size() != mc.getConnection().getOnlinePlayers().size()) {
                cachedPlayers = DataCache.INSTANCE.getPlayers(true).values();
            }
            this.entityList.addAll(cachedPlayers);
        }
        if (!this.entityList.isEmpty()) {
            Collections.sort(this.entityList, EntityHelper.entityMapComparator);
            this.drawStepList.addAll(radarRenderer.prepareSteps(this.entityList, gridRenderer, mapProperties));
        }
        if (mapProperties.showWaypoints.get()) {
            boolean showLabel = mapProperties.showWaypointLabels.get();
            this.drawWaypointStepList.addAll(waypointRenderer.prepareSteps(DataCache.INSTANCE.getWaypoints(false), gridRenderer, checkWaypointDistance, showLabel));
        }
        this.generateDrawStepsTimer.stop();
    }

    public boolean zoomIn() {
        return this.lastMapProperties.zoomLevel.get() < 5 ? this.setZoom(this.lastMapProperties.zoomLevel.get() + 1) : false;
    }

    public boolean zoomOut() {
        return this.lastMapProperties.zoomLevel.get() > 0 ? this.setZoom(this.lastMapProperties.zoomLevel.get() - 1) : false;
    }

    public boolean setZoom(int zoom) {
        if (zoom <= 5 && zoom >= 0 && zoom != this.lastMapProperties.zoomLevel.get()) {
            this.lastMapProperties.zoomLevel.set(Integer.valueOf(zoom));
            this.requireRefresh();
            return true;
        } else {
            return false;
        }
    }

    public int getZoom() {
        return this.lastMapProperties.zoomLevel.get();
    }

    public void requireRefresh() {
        this.lastRefresh = 0L;
    }

    public void updateLastRefresh() {
        this.lastRefresh = System.currentTimeMillis();
    }

    public boolean shouldRefresh(Minecraft mc, MapProperties mapProperties) {
        if (ClientAPI.INSTANCE.isDrawStepsUpdateNeeded()) {
            return true;
        } else if (MapPlayerTask.getlastTaskCompleted() - this.lastRefresh > 500L) {
            return true;
        } else if (this.lastMapType == null) {
            return true;
        } else {
            EntityDTO player = DataCache.getPlayer();
            if (!player.dimension.equals(this.getMapType().dimension)) {
                return true;
            } else {
                double d0 = (double) (this.lastPlayerChunkX - player.chunkCoordX);
                double d1 = (double) (this.lastPlayerChunkY - player.chunkCoordY);
                double d2 = (double) (this.lastPlayerChunkZ - player.chunkCoordZ);
                double diff = Math.sqrt(d0 * d0 + d1 * d1 + d2 * d2);
                if (diff > 2.0) {
                    return true;
                } else if (this.lastMapProperties == null || !this.lastMapProperties.equals(mapProperties)) {
                    return true;
                } else if (this.forceRefreshState) {
                    this.forceRefreshState = false;
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    public void setForceRefreshState(boolean force) {
        this.forceRefreshState = force;
    }

    public boolean isHighQuality() {
        return this.highQuality;
    }

    public boolean isCaveMappingAllowed() {
        return this.caveMappingAllowed;
    }

    public boolean isCaveMappingEnabled() {
        return this.caveMappingEnabled;
    }

    public boolean isSurfaceMappingAllowed() {
        return this.surfaceMappingAllowed;
    }

    public boolean isTopoMappingAllowed() {
        return this.topoMappingAllowed;
    }

    public ResourceKey<Level> getDimension() {
        return this.getMapType().dimension;
    }

    public IntegerField getLastSlice() {
        return this.lastSlice;
    }

    public void resetMapType() {
        this.lastMapType = null;
    }

    public boolean isBiomeMappingAllowed() {
        return this.biomeMappingAllowed;
    }
}