package journeymap.client.render.map;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.RemovalListener;
import com.google.common.cache.RemovalNotification;
import java.io.File;
import java.util.concurrent.TimeUnit;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.common.Journeymap;
import org.apache.logging.log4j.Logger;

public class TileDrawStepCache {

    private final Logger logger = Journeymap.getLogger();

    private final Cache<String, TileDrawStep> drawStepCache = CacheBuilder.newBuilder().expireAfterAccess(30L, TimeUnit.SECONDS).removalListener(new RemovalListener<String, TileDrawStep>() {

        public void onRemoval(RemovalNotification<String, TileDrawStep> notification) {
            TileDrawStep oldDrawStep = (TileDrawStep) notification.getValue();
            if (oldDrawStep != null) {
                oldDrawStep.clearTexture();
            }
        }
    }).build();

    private File worldDir;

    private MapType mapType;

    private TileDrawStepCache() {
    }

    public static Cache<String, TileDrawStep> instance() {
        return TileDrawStepCache.Holder.INSTANCE.drawStepCache;
    }

    public static TileDrawStep getOrCreate(MapType mapType, RegionCoord regionCoord, Integer zoom, boolean highQuality, int sx1, int sy1, int sx2, int sy2) {
        return TileDrawStepCache.Holder.INSTANCE._getOrCreate(mapType, regionCoord, zoom, highQuality, sx1, sy1, sx2, sy2);
    }

    public static void clear() {
        instance().invalidateAll();
    }

    public static void setContext(File worldDir, MapType mapType) {
        if (!worldDir.equals(TileDrawStepCache.Holder.INSTANCE.worldDir)) {
            instance().invalidateAll();
        }
        TileDrawStepCache.Holder.INSTANCE.worldDir = worldDir;
        TileDrawStepCache.Holder.INSTANCE.mapType = mapType;
    }

    public static long size() {
        return instance().size();
    }

    private TileDrawStep _getOrCreate(MapType mapType, RegionCoord regionCoord, Integer zoom, boolean highQuality, int sx1, int sy1, int sx2, int sy2) {
        this.checkWorldChange(regionCoord);
        String key = TileDrawStep.toCacheKey(regionCoord, mapType, zoom, highQuality, sx1, sy1, sx2, sy2);
        TileDrawStep tileDrawStep = (TileDrawStep) this.drawStepCache.getIfPresent(key);
        if (tileDrawStep == null) {
            tileDrawStep = new TileDrawStep(regionCoord, mapType, zoom, highQuality, sx1, sy1, sx2, sy2);
            this.drawStepCache.put(key, tileDrawStep);
        }
        return tileDrawStep;
    }

    private void checkWorldChange(RegionCoord regionCoord) {
        if (!regionCoord.worldDir.equals(this.worldDir)) {
            this.drawStepCache.invalidateAll();
            RegionImageCache.INSTANCE.clear();
        }
    }

    private static class Holder {

        private static final TileDrawStepCache INSTANCE = new TileDrawStepCache();
    }
}