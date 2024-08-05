package journeymap.client.data;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheStats;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Sets;
import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import journeymap.client.JourneymapClient;
import journeymap.client.api.display.ImageOverlay;
import journeymap.client.api.display.MarkerOverlay;
import journeymap.client.api.display.PolygonOverlay;
import journeymap.client.io.nbt.JMChunkLoader;
import journeymap.client.model.BlockMD;
import journeymap.client.model.ChunkMD;
import journeymap.client.model.EntityDTO;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.client.model.RegionImageSet;
import journeymap.client.render.draw.DrawEntityStep;
import journeymap.client.render.draw.DrawImageStep;
import journeymap.client.render.draw.DrawMarkerStep;
import journeymap.client.render.draw.DrawPolygonStep;
import journeymap.client.render.draw.DrawWayPointStep;
import journeymap.client.waypoint.Waypoint;
import journeymap.client.waypoint.WaypointStore;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public enum DataCache {

    INSTANCE;

    final LoadingCache<Long, Map> all;

    final LoadingCache<Class, Map<String, EntityDTO>> animals;

    final LoadingCache<Class, Map<String, EntityDTO>> mobs;

    final LoadingCache<Class, Map<String, EntityDTO>> players;

    final LoadingCache<Class, Map<String, EntityDTO>> villagers;

    final LoadingCache<Class, Collection<Waypoint>> waypoints;

    final LoadingCache<Class, EntityDTO> player;

    final LoadingCache<Class, WorldData> world;

    final LoadingCache<RegionImageSet.Key, RegionImageSet> regionImageSets;

    final LoadingCache<Class, Map<String, Object>> messages;

    final LoadingCache<LivingEntity, DrawEntityStep> entityDrawSteps;

    final LoadingCache<Waypoint, DrawWayPointStep> waypointDrawSteps;

    final LoadingCache<LivingEntity, EntityDTO> entityDTOs;

    final Cache<String, RegionCoord> regionCoords;

    final Cache<String, MapType> mapTypes;

    final LoadingCache<BlockState, BlockMD> blockMetadata;

    final Cache<ChunkPos, ChunkMD> chunkMetadata;

    final LoadingCache<PolygonOverlay, List<BlockPos>> polygonTriangulation;

    final LoadingCache<PolygonOverlay, DrawPolygonStep> polygonDrawSteps;

    final LoadingCache<MarkerOverlay, DrawMarkerStep> markerDrawSteps;

    final LoadingCache<ImageOverlay, DrawImageStep> imageDrawSteps;

    private final Long2ObjectMap<ChunkPos> chunkCoordCache = new Long2ObjectOpenHashMap();

    final HashMap<Cache, String> managedCaches = new HashMap();

    private final int chunkCacheExpireSeconds = 30;

    private final int defaultConcurrencyLevel = 2;

    private DataCache() {
        AllData allData = new AllData();
        this.all = this.getCacheBuilder().maximumSize(1L).expireAfterWrite(allData.getTTL(), TimeUnit.MILLISECONDS).build(allData);
        this.managedCaches.put(this.all, "AllData (web)");
        AnimalsData animalsData = new AnimalsData();
        this.animals = this.getCacheBuilder().expireAfterWrite(animalsData.getTTL(), TimeUnit.MILLISECONDS).build(animalsData);
        this.managedCaches.put(this.animals, "Animals");
        MobsData mobsData = new MobsData();
        this.mobs = this.getCacheBuilder().expireAfterWrite(mobsData.getTTL(), TimeUnit.MILLISECONDS).build(mobsData);
        this.managedCaches.put(this.mobs, "Mobs");
        PlayerData playerData = new PlayerData();
        this.player = this.getCacheBuilder().expireAfterWrite(playerData.getTTL(), TimeUnit.MILLISECONDS).build(playerData);
        this.managedCaches.put(this.player, "Player");
        PlayersData playersData = new PlayersData();
        this.players = this.getCacheBuilder().expireAfterWrite(playersData.getTTL(), TimeUnit.MILLISECONDS).build(playersData);
        this.managedCaches.put(this.players, "Players");
        VillagersData villagersData = new VillagersData();
        this.villagers = this.getCacheBuilder().expireAfterWrite(villagersData.getTTL(), TimeUnit.MILLISECONDS).build(villagersData);
        this.managedCaches.put(this.villagers, "Villagers");
        WaypointsData waypointsData = new WaypointsData();
        this.waypoints = this.getCacheBuilder().expireAfterWrite(waypointsData.getTTL(), TimeUnit.MILLISECONDS).build(waypointsData);
        this.managedCaches.put(this.waypoints, "Waypoints");
        WorldData worldData = new WorldData();
        this.world = this.getCacheBuilder().expireAfterWrite(worldData.getTTL(), TimeUnit.MILLISECONDS).build(worldData);
        this.managedCaches.put(this.world, "World");
        MessagesData messagesData = new MessagesData();
        this.messages = this.getCacheBuilder().expireAfterWrite(messagesData.getTTL(), TimeUnit.MILLISECONDS).build(messagesData);
        this.managedCaches.put(this.messages, "Messages (web)");
        this.entityDrawSteps = this.getCacheBuilder().weakKeys().build(new DrawEntityStep.SimpleCacheLoader());
        this.managedCaches.put(this.entityDrawSteps, "DrawEntityStep");
        this.waypointDrawSteps = this.getCacheBuilder().weakKeys().build(new DrawWayPointStep.SimpleCacheLoader());
        this.managedCaches.put(this.waypointDrawSteps, "DrawWaypointStep");
        this.polygonDrawSteps = this.getCacheBuilder().weakKeys().build(new DrawPolygonStep.SimpleCacheLoader());
        this.managedCaches.put(this.polygonDrawSteps, "PolygonDrawSteps");
        this.polygonTriangulation = this.getCacheBuilder().weakKeys().build(new DrawPolygonStep.TriangulationCacheLoader());
        this.managedCaches.put(this.polygonTriangulation, "Triangulatio");
        this.imageDrawSteps = this.getCacheBuilder().weakKeys().build(new DrawImageStep.SimpleCacheLoader());
        this.managedCaches.put(this.imageDrawSteps, "ImageDrawSteps");
        this.markerDrawSteps = this.getCacheBuilder().weakKeys().build(new DrawMarkerStep.SimpleCacheLoader());
        this.managedCaches.put(this.markerDrawSteps, "MarkerDrawSteps");
        this.entityDTOs = this.getCacheBuilder().weakKeys().build(new EntityDTO.SimpleCacheLoader());
        this.managedCaches.put(this.entityDTOs, "EntityDTO");
        this.regionImageSets = RegionImageCache.INSTANCE.initRegionImageSetsCache(this.getCacheBuilder());
        this.managedCaches.put(this.regionImageSets, "RegionImageSet");
        this.blockMetadata = this.getCacheBuilder().weakKeys().build(new BlockMD.CacheLoader());
        this.managedCaches.put(this.blockMetadata, "BlockMD");
        this.chunkMetadata = this.getCacheBuilder().expireAfterAccess(30L, TimeUnit.SECONDS).build();
        this.managedCaches.put(this.chunkMetadata, "ChunkMD");
        this.regionCoords = this.getCacheBuilder().expireAfterAccess(30L, TimeUnit.SECONDS).build();
        this.managedCaches.put(this.regionCoords, "RegionCoord");
        this.mapTypes = this.getCacheBuilder().build();
        this.managedCaches.put(this.mapTypes, "MapType");
    }

    public static EntityDTO getPlayer() {
        return INSTANCE.getPlayer(false);
    }

    private CacheBuilder<Object, Object> getCacheBuilder() {
        CacheBuilder<Object, Object> builder = CacheBuilder.newBuilder();
        builder.concurrencyLevel(2);
        if (JourneymapClient.getInstance().getCoreProperties().recordCacheStats.get()) {
            builder.recordStats();
        }
        return builder;
    }

    public Map getAll(long since) {
        synchronized (this.all) {
            Map var10000;
            try {
                var10000 = (Map) this.all.get(since);
            } catch (ExecutionException var6) {
                Journeymap.getLogger().error("ExecutionException in getAll: " + LogFormatter.toString(var6));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public Map<String, EntityDTO> getAnimals(boolean forceRefresh) {
        synchronized (this.animals) {
            Map var10000;
            try {
                if (forceRefresh) {
                    this.animals.invalidateAll();
                }
                var10000 = (Map) this.animals.get(AnimalsData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getAnimals: " + LogFormatter.toString(var5));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public Map<String, EntityDTO> getMobs(boolean forceRefresh) {
        synchronized (this.mobs) {
            Map var10000;
            try {
                if (forceRefresh) {
                    this.mobs.invalidateAll();
                }
                var10000 = (Map) this.mobs.get(MobsData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getMobs: " + LogFormatter.toString(var5));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public Map<String, EntityDTO> getPlayers(boolean forceRefresh) {
        synchronized (this.players) {
            Map var10000;
            try {
                if (forceRefresh) {
                    this.players.invalidateAll();
                }
                var10000 = (Map) this.players.get(PlayersData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getPlayers: " + LogFormatter.toString(var5));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public EntityDTO getPlayer(boolean forceRefresh) {
        synchronized (this.player) {
            EntityDTO var10000;
            try {
                if (forceRefresh) {
                    this.player.invalidateAll();
                }
                var10000 = (EntityDTO) this.player.get(PlayerData.class);
            } catch (Exception var5) {
                Journeymap.getLogger().error("ExecutionException in getPlayer: " + LogFormatter.toString(var5));
                return null;
            }
            return var10000;
        }
    }

    public void removePlayer(Player player) {
        synchronized (this.players) {
            try {
                this.players.invalidate(PlayersData.class);
            } catch (Exception var5) {
                Journeymap.getLogger().error("ExecutionException in removePlayer: " + LogFormatter.toString(var5));
            }
        }
    }

    public Map<String, EntityDTO> getVillagers(boolean forceRefresh) {
        synchronized (this.villagers) {
            Map var10000;
            try {
                if (forceRefresh) {
                    this.villagers.invalidateAll();
                }
                var10000 = (Map) this.villagers.get(VillagersData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getVillagers: " + LogFormatter.toString(var5));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public MapType getMapType(MapType.Name name, Integer vSlice, ResourceKey<Level> dimension) {
        vSlice = name != MapType.Name.underground ? null : vSlice;
        MapType mapType = (MapType) this.mapTypes.getIfPresent(MapType.toCacheKey(name, vSlice, dimension));
        if (mapType == null) {
            mapType = new MapType(name, vSlice, dimension);
            this.mapTypes.put(mapType.toCacheKey(), mapType);
        }
        return mapType;
    }

    public Collection<Waypoint> getWaypoints(boolean forceRefresh) {
        synchronized (this.waypoints) {
            return (Collection<Waypoint>) (WaypointsData.isManagerEnabled() ? WaypointStore.INSTANCE.getAll() : Collections.EMPTY_LIST);
        }
    }

    public Map<String, Object> getMessages(boolean forceRefresh) {
        synchronized (this.messages) {
            Map var10000;
            try {
                if (forceRefresh) {
                    this.messages.invalidateAll();
                }
                var10000 = (Map) this.messages.get(MessagesData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getMessages: " + LogFormatter.toString(var5));
                return Collections.EMPTY_MAP;
            }
            return var10000;
        }
    }

    public WorldData getWorld(boolean forceRefresh) {
        synchronized (this.world) {
            WorldData var10000;
            try {
                if (forceRefresh) {
                    this.world.invalidateAll();
                }
                var10000 = (WorldData) this.world.get(WorldData.class);
            } catch (ExecutionException var5) {
                Journeymap.getLogger().error("ExecutionException in getWorld: " + LogFormatter.toString(var5));
                return new WorldData();
            }
            return var10000;
        }
    }

    public void resetRadarCaches() {
        this.animals.invalidateAll();
        this.mobs.invalidateAll();
        this.players.invalidateAll();
        this.villagers.invalidateAll();
        this.entityDrawSteps.invalidateAll();
        this.entityDTOs.invalidateAll();
    }

    public DrawEntityStep getDrawEntityStep(LivingEntity entity) {
        synchronized (this.entityDrawSteps) {
            return (DrawEntityStep) this.entityDrawSteps.getUnchecked(entity);
        }
    }

    public EntityDTO getEntityDTO(LivingEntity entity) {
        synchronized (this.entityDTOs) {
            return (EntityDTO) this.entityDTOs.getUnchecked(entity);
        }
    }

    public List<BlockPos> getTriangulation(PolygonOverlay overlay) {
        synchronized (this.polygonTriangulation) {
            return (List<BlockPos>) this.polygonTriangulation.getUnchecked(overlay);
        }
    }

    public DrawPolygonStep getDrawPolygonStep(PolygonOverlay overlay) {
        synchronized (this.polygonDrawSteps) {
            return (DrawPolygonStep) this.polygonDrawSteps.getUnchecked(overlay);
        }
    }

    public void invalidatePolygon(PolygonOverlay overlay) {
        synchronized (this.polygonDrawSteps) {
            this.polygonDrawSteps.invalidate(overlay);
        }
    }

    public DrawImageStep getDrawImageStep(ImageOverlay overlay) {
        synchronized (this.imageDrawSteps) {
            return (DrawImageStep) this.imageDrawSteps.getUnchecked(overlay);
        }
    }

    public DrawMarkerStep getDrawMakerStep(MarkerOverlay overlay) {
        synchronized (this.markerDrawSteps) {
            return (DrawMarkerStep) this.markerDrawSteps.getUnchecked(overlay);
        }
    }

    public DrawWayPointStep getDrawWayPointStep(Waypoint waypoint) {
        synchronized (this.waypointDrawSteps) {
            return (DrawWayPointStep) this.waypointDrawSteps.getUnchecked(waypoint);
        }
    }

    public boolean hasBlockMD(BlockState aBlockState) {
        try {
            return this.blockMetadata.getIfPresent(aBlockState) != null;
        } catch (Exception var3) {
            return false;
        }
    }

    public BlockMD getBlockMD(BlockState blockState) {
        if (blockState != null) {
            try {
                return (BlockMD) this.blockMetadata.get(blockState);
            } catch (Exception var3) {
                Journeymap.getLogger().error("Error in getBlockMD() for {}: ", blockState, var3);
            }
        }
        return BlockMD.AIRBLOCK;
    }

    public int getBlockMDCount() {
        return this.blockMetadata.asMap().size();
    }

    public Set<BlockMD> getLoadedBlockMDs() {
        return Sets.newHashSet(this.blockMetadata.asMap().values());
    }

    public void resetBlockMetadata() {
        this.blockMetadata.invalidateAll();
    }

    public ChunkMD getChunkMD(BlockPos blockPos) {
        return this.getChunkMD(ChunkPos.asLong(blockPos.m_123341_() >> 4, blockPos.m_123343_() >> 4));
    }

    public ChunkMD getChunkMD(long coordLong) {
        synchronized (this.chunkMetadata) {
            Object var10000;
            try {
                ChunkPos coord = (ChunkPos) this.chunkCoordCache.get(coordLong);
                if (coord == null) {
                    int x = (int) (coordLong & 4294967295L);
                    int z = (int) (coordLong >>> 32 & 4294967295L);
                    coord = new ChunkPos(x, z);
                    this.chunkCoordCache.put(coordLong, coord);
                }
                Minecraft mc = Minecraft.getInstance();
                if (mc.level == null || mc.player == null || mc.level != mc.player.m_9236_()) {
                    return null;
                }
                ChunkMD chunkMD = (ChunkMD) this.chunkMetadata.getIfPresent(coord);
                if (chunkMD != null && chunkMD.hasChunk()) {
                    return chunkMD;
                }
                chunkMD = JMChunkLoader.getChunkMdFromMemory(mc.player.m_9236_(), coord.x, coord.z);
                if (chunkMD != null && chunkMD.hasChunk() && chunkMD.getChunk().getLevel().dimension() == mc.player.m_9236_().dimension()) {
                    this.chunkMetadata.put(coord, chunkMD);
                    return chunkMD;
                }
                if (chunkMD != null) {
                    this.chunkMetadata.invalidate(coord);
                }
                var10000 = null;
            } catch (Throwable var9) {
                Journeymap.getLogger().warn("Unexpected error getting ChunkMD from cache: ", var9);
                return null;
            }
            return (ChunkMD) var10000;
        }
    }

    public void addChunkMD(ChunkMD chunkMD) {
        synchronized (this.chunkMetadata) {
            this.chunkMetadata.put(chunkMD.getCoord(), chunkMD);
        }
    }

    public void removeChunkMD(ChunkMD chunkMD) {
        synchronized (this.chunkMetadata) {
            this.chunkMetadata.invalidate(chunkMD.getCoord());
            this.chunkCoordCache.remove(chunkMD.getLongCoord());
        }
    }

    public void invalidateChunkMDCache() {
        this.chunkMetadata.invalidateAll();
        this.chunkCoordCache.clear();
    }

    public void stopChunkMDRetention() {
        for (ChunkMD chunkMD : this.chunkMetadata.asMap().values()) {
            if (chunkMD != null) {
                chunkMD.stopChunkRetention();
            }
        }
    }

    public LoadingCache<RegionImageSet.Key, RegionImageSet> getRegionImageSets() {
        return this.regionImageSets;
    }

    public Cache<String, RegionCoord> getRegionCoords() {
        return this.regionCoords;
    }

    public void purge() {
        RegionImageCache.INSTANCE.flushToDisk(false);
        this.resetBlockMetadata();
        synchronized (this.managedCaches) {
            this.chunkCoordCache.clear();
            for (Cache cache : this.managedCaches.keySet()) {
                try {
                    cache.invalidateAll();
                } catch (Exception var6) {
                    Journeymap.getLogger().warn("Couldn't purge managed cache: " + cache);
                }
            }
        }
    }

    public String getDebugHtml() {
        StringBuffer sb = new StringBuffer();
        if (JourneymapClient.getInstance().getCoreProperties().recordCacheStats.get()) {
            this.appendDebugHtml(sb, "Managed Caches", this.managedCaches);
        } else {
            sb.append("<b>Cache stat recording disabled.  Set config/journeymap.core.config 'recordCacheStats' to 1.</b>");
        }
        return sb.toString();
    }

    private void appendDebugHtml(StringBuffer sb, String name, Map<Cache, String> cacheMap) {
        ArrayList<Entry<Cache, String>> list = new ArrayList(cacheMap.entrySet());
        Collections.sort(list, new Comparator<Entry<Cache, String>>() {

            public int compare(Entry<Cache, String> o1, Entry<Cache, String> o2) {
                return ((String) o1.getValue()).compareTo((String) o2.getValue());
            }
        });
        sb.append("<b>").append(name).append(":</b>");
        sb.append("<pre>");
        for (Entry<Cache, String> entry : list) {
            sb.append(this.toString((String) entry.getValue(), (Cache) entry.getKey()));
        }
        sb.append("</pre>");
    }

    private String toString(String label, Cache cache) {
        double avgLoadMillis = 0.0;
        CacheStats cacheStats = cache.stats();
        if (cacheStats.totalLoadTime() > 0L && cacheStats.loadSuccessCount() > 0L) {
            avgLoadMillis = (double) TimeUnit.NANOSECONDS.toMillis(cacheStats.totalLoadTime()) * 1.0 / (double) cacheStats.loadSuccessCount();
        }
        return String.format("%s<b>%20s:</b> Size: %9s, Hits: %9s, Misses: %9s, Loads: %9s, Errors: %9s, Avg Load Time: %1.2fms", LogFormatter.LINEBREAK, label, cache.size(), cacheStats.hitCount(), cacheStats.missCount(), cacheStats.loadCount(), cacheStats.loadExceptionCount(), avgLoadMillis);
    }
}