package journeymap.client.model;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.annotation.ParametersAreNonnullByDefault;
import journeymap.client.data.DataCache;
import journeymap.client.io.FileHandler;
import journeymap.client.io.RegionImageHandler;
import journeymap.common.Journeymap;
import net.minecraft.client.Minecraft;
import net.minecraft.world.level.chunk.LevelChunk;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public enum RegionImageCache {

    INSTANCE;

    public long firstFileFlushIntervalSecs = 5L;

    public long flushFileIntervalSecs = 60L;

    public long textureCacheAgeSecs = 30L;

    static final Logger logger = Journeymap.getLogger();

    private volatile long lastFlush = System.currentTimeMillis() + TimeUnit.SECONDS.toMillis(this.firstFileFlushIntervalSecs);

    public LoadingCache<RegionImageSet.Key, RegionImageSet> initRegionImageSetsCache(CacheBuilder<Object, Object> builder) {
        return builder.expireAfterAccess(this.textureCacheAgeSecs, TimeUnit.SECONDS).removalListener(new RemovalListener<RegionImageSet.Key, RegionImageSet>() {

            @ParametersAreNonnullByDefault
            public void onRemoval(RemovalNotification<RegionImageSet.Key, RegionImageSet> notification) {
                RegionImageSet regionImageSet = (RegionImageSet) notification.getValue();
                if (regionImageSet != null) {
                    int count = regionImageSet.writeToDisk(false);
                    if (count > 0 && Journeymap.getLogger().isDebugEnabled()) {
                        Journeymap.getLogger().debug("Wrote to disk before removal from cache: " + regionImageSet);
                    }
                    regionImageSet.clear();
                }
            }
        }).build(new CacheLoader<RegionImageSet.Key, RegionImageSet>() {

            @ParametersAreNonnullByDefault
            public RegionImageSet load(RegionImageSet.Key key) throws Exception {
                return new RegionImageSet(key);
            }
        });
    }

    public RegionImageSet getRegionImageSet(ChunkMD chunkMd, MapType mapType) {
        if (chunkMd.hasChunk()) {
            Minecraft mc = Minecraft.getInstance();
            LevelChunk chunk = chunkMd.getChunk();
            RegionCoord rCoord = RegionCoord.fromChunkPos(FileHandler.getJMWorldDir(mc), mapType, chunk.m_7697_().x, chunk.m_7697_().z);
            return this.getRegionImageSet(rCoord);
        } else {
            return null;
        }
    }

    public RegionImageSet getRegionImageSet(RegionCoord rCoord) {
        return (RegionImageSet) DataCache.INSTANCE.getRegionImageSets().getUnchecked(RegionImageSet.Key.from(rCoord));
    }

    public RegionImageSet getRegionImageSet(RegionImageSet.Key rCoordKey) {
        return (RegionImageSet) DataCache.INSTANCE.getRegionImageSets().getUnchecked(rCoordKey);
    }

    private Collection<RegionImageSet> getRegionImageSets() {
        return DataCache.INSTANCE.getRegionImageSets().asMap().values();
    }

    public void updateTextures(boolean forceFlush, boolean async) {
        for (RegionImageSet regionImageSet : this.getRegionImageSets()) {
            regionImageSet.finishChunkUpdates();
        }
        if (forceFlush || this.lastFlush + TimeUnit.SECONDS.toMillis(this.flushFileIntervalSecs) < System.currentTimeMillis()) {
            if (!forceFlush && logger.isEnabled(Level.DEBUG)) {
                logger.debug("RegionImageCache auto-flushing");
            }
            if (async) {
                this.flushToDiskAsync(false);
            } else {
                this.flushToDisk(false);
            }
        }
    }

    public void flushToDiskAsync(boolean force) {
        int count = 0;
        for (RegionImageSet regionImageSet : this.getRegionImageSets()) {
            count += regionImageSet.writeToDiskAsync(force);
        }
        this.lastFlush = System.currentTimeMillis();
    }

    public void flushToDisk(boolean force) {
        for (RegionImageSet regionImageSet : this.getRegionImageSets()) {
            regionImageSet.writeToDisk(force);
        }
        this.lastFlush = System.currentTimeMillis();
    }

    public long getLastFlush() {
        return this.lastFlush;
    }

    public List<RegionCoord> getChangedSince(MapType mapType, long time) {
        ArrayList<RegionCoord> list = new ArrayList();
        for (RegionImageSet regionImageSet : this.getRegionImageSets()) {
            if (regionImageSet.updatedSince(mapType, time)) {
                list.add(regionImageSet.getRegionCoord());
            }
        }
        if (logger.isEnabled(Level.DEBUG)) {
            logger.debug("Dirty regions: " + list.size() + " of " + DataCache.INSTANCE.getRegionImageSets().size());
        }
        return list;
    }

    public boolean isDirtySince(RegionCoord rc, MapType mapType, long time) {
        RegionImageSet ris = this.getRegionImageSet(rc);
        return ris == null ? false : ris.updatedSince(mapType, time);
    }

    public void clear() {
        for (RegionImageSet regionImageSet : this.getRegionImageSets()) {
            regionImageSet.clear();
        }
        DataCache.INSTANCE.getRegionImageSets().invalidateAll();
        DataCache.INSTANCE.getRegionImageSets().cleanUp();
    }

    public boolean deleteMap(MapState state, boolean allDims) {
        RegionCoord fakeRc = new RegionCoord(state.getWorldDir(), 0, 0, state.getDimension());
        File imageDir = RegionImageHandler.getImageDir(fakeRc, MapType.day(state.getDimension())).getParentFile();
        File[] dirs;
        if (allDims) {
            dirs = imageDir.getParentFile().listFiles(new FilenameFilter() {

                public boolean accept(File dir, String name) {
                    return dir.isDirectory() && !"waypoints".equals(name);
                }
            });
        } else {
            dirs = new File[] { imageDir };
        }
        if (dirs != null && dirs.length > 0) {
            this.clear();
            boolean result = true;
            for (File dir : dirs) {
                if (dir.exists()) {
                    FileHandler.delete(dir);
                    logger.info(String.format("Deleted image directory %s: %s", dir, !dir.exists()));
                    if (dir.exists()) {
                        result = false;
                    }
                }
            }
            logger.info("Done deleting directories");
            return result;
        } else {
            logger.info("Found no DIM directories in " + imageDir);
            return true;
        }
    }
}