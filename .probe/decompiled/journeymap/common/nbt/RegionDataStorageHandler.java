package journeymap.common.nbt;

import java.io.File;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringJoiner;
import javax.annotation.Nullable;
import journeymap.client.JourneymapClient;
import journeymap.client.io.FileHandler;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageSet;
import journeymap.client.task.multi.MapRegionTask;
import journeymap.common.nbt.cache.CacheStorage;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public class RegionDataStorageHandler {

    private final Map<RegionDataStorageHandler.Key, RegionData> SYNC_DATA_MAP = new LinkedHashMap();

    private final Map<MapType, CacheStorage> STORAGE_MAP = new LinkedHashMap();

    private RegionData lastHandler;

    private static RegionDataStorageHandler INSTANCE;

    public static RegionDataStorageHandler getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new RegionDataStorageHandler();
        }
        return INSTANCE;
    }

    private File getDir(RegionCoord rCoord) {
        File dimDir = rCoord.dimDir.toFile();
        if (!dimDir.exists()) {
            dimDir.mkdirs();
        }
        return dimDir;
    }

    @Nullable
    private Path getFile(RegionCoord regionCoord, MapType mapType) {
        File directory = this.getDir(regionCoord);
        if (mapType.isUnderground() || Level.NETHER.equals(mapType.dimension)) {
            if (mapType.vSlice == null) {
                return null;
            }
            directory = new File(directory, mapType.vSlice.toString());
        }
        if (mapType.isTopo() && !Level.NETHER.equals(mapType.dimension)) {
            directory = new File(directory, mapType.name());
        }
        File cacheDirectory = new File(directory, "cache");
        if (!cacheDirectory.exists()) {
            cacheDirectory.mkdirs();
        }
        return cacheDirectory.toPath();
    }

    public RegionData getRegionDataAsyncNoCache(BlockPos blockPos, MapType mapType) {
        RegionCoord regionCoord = RegionCoord.fromChunkPos(FileHandler.getJMWorldDir(Minecraft.getInstance()), mapType, blockPos.m_123341_() >> 4, blockPos.m_123343_() >> 4);
        return this.getRegionDataAsyncNoCache(regionCoord, mapType);
    }

    public RegionData getRegionDataAsyncNoCache(RegionCoord regionCoord, MapType mapType) {
        if (!MapRegionTask.active && JourneymapClient.getInstance().getCoreProperties().dataCachingEnabled.get()) {
            RegionDataStorageHandler.Key key = new RegionDataStorageHandler.Key(regionCoord, mapType);
            if (this.SYNC_DATA_MAP.containsKey(key)) {
                this.lastHandler = (RegionData) this.SYNC_DATA_MAP.get(key);
            } else {
                this.lastHandler = this.getRegionData(key);
            }
            return this.lastHandler;
        } else {
            return null;
        }
    }

    public RegionData getRegionData(RegionDataStorageHandler.Key key) {
        RegionData data = (RegionData) this.SYNC_DATA_MAP.get(key);
        if (data == null) {
            data = new RegionData(key, this.getCacheStorageSync(key.rCoord, key.mapType));
            this.SYNC_DATA_MAP.put(key, data);
        }
        return data;
    }

    private CacheStorage getCacheStorageSync(RegionCoord regionCoord, MapType mapType) {
        CacheStorage storage = (CacheStorage) this.STORAGE_MAP.get(mapType);
        if (storage == null) {
            storage = new CacheStorage(this.getFile(regionCoord, mapType), false);
            this.STORAGE_MAP.put(mapType, storage);
        }
        return storage;
    }

    public void flushDataCache() {
        this.STORAGE_MAP.values().forEach(CacheStorage::flushWorker);
        this.STORAGE_MAP.clear();
        this.SYNC_DATA_MAP.clear();
    }

    public void deleteCache() {
        this.flushDataCache();
    }

    public static class Key {

        final RegionCoord rCoord;

        final MapType mapType;

        public Key(RegionCoord rCoord, MapType mapType) {
            this.rCoord = rCoord;
            this.mapType = mapType;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                RegionDataStorageHandler.Key key = (RegionDataStorageHandler.Key) o;
                if (!this.mapType.dimension.equals(key.mapType.dimension)) {
                    return false;
                } else if (this.mapType.isUnderground() != key.mapType.isUnderground()) {
                    return false;
                } else if (this.mapType.isUnderground() && !this.mapType.vSlice.equals(key.mapType.vSlice)) {
                    return false;
                } else if (this.mapType.isTopo() != key.mapType.isTopo()) {
                    return false;
                } else if (this.mapType.isSurfaceType() != key.mapType.isSurfaceType()) {
                    return false;
                } else if (this.rCoord.regionX != key.rCoord.regionX) {
                    return false;
                } else {
                    return this.rCoord.worldDir != key.rCoord.worldDir ? false : this.rCoord.regionZ == key.rCoord.regionZ;
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = 31 + this.rCoord.regionX;
            result = 31 * result + this.rCoord.regionZ;
            return 31 * result + this.mapType.dimension.hashCode();
        }

        public String toString() {
            return new StringJoiner(", ", RegionImageSet.Key.class.getSimpleName() + "[", "]").add("isUnderground=" + this.mapType.isUnderground()).add("isSurfaceType=" + this.mapType.isSurfaceType()).add("isTopo=" + this.mapType.isTopo()).add("rCoord=" + this.rCoord).add("regionX=" + this.rCoord.regionX).add("regionZ=" + this.rCoord.regionZ).add("dimension=" + this.mapType.dimension).toString();
        }
    }
}