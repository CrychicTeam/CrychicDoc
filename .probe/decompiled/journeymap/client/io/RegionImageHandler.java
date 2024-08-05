package journeymap.client.io;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import journeymap.client.Constants;
import journeymap.client.cartography.color.RGB;
import journeymap.client.model.MapType;
import journeymap.client.model.RegionCoord;
import journeymap.client.model.RegionImageCache;
import journeymap.client.render.map.TileDrawStep;
import journeymap.client.render.map.TileDrawStepCache;
import journeymap.client.texture.ImageUtil;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.world.level.ChunkPos;

public class RegionImageHandler {

    public static File getImageDir(RegionCoord rCoord, MapType mapType) {
        File dimDir = rCoord.dimDir.toFile();
        File subDir = null;
        if (mapType.isUnderground()) {
            subDir = new File(dimDir, Integer.toString(mapType.vSlice));
        } else {
            subDir = new File(dimDir, mapType.name());
        }
        if (!subDir.exists()) {
            subDir.mkdirs();
        }
        return subDir;
    }

    public static File getRegionImageFile(RegionCoord rCoord, MapType mapType) {
        StringBuffer sb = new StringBuffer();
        sb.append(rCoord.regionX).append(",").append(rCoord.regionZ).append(".png");
        return new File(getImageDir(rCoord, mapType), sb.toString());
    }

    public static NativeImage readRegionImage(File regionFile) {
        if (regionFile.canRead()) {
            try {
                return getImage(regionFile);
            } catch (Exception var4) {
                String error = "Region file produced error: " + regionFile + ": " + LogFormatter.toPartialString(var4);
                Journeymap.getLogger().error(error);
            }
        }
        return null;
    }

    public static NativeImage getImage(File file) {
        try {
            InputStream is = new FileInputStream(file.getPath());
            return NativeImage.read(is);
        } catch (IOException var3) {
            String error = "Could not get image from file: " + file + ": " + var3.getMessage();
            Journeymap.getLogger().error(error);
            return null;
        }
    }

    public static synchronized NativeImage getMergedChunks(File worldDir, ChunkPos startCoord, ChunkPos endCoord, MapType mapType, Boolean useCache, NativeImage image, Integer imageWidth, Integer imageHeight, boolean allowNullImage, boolean showGrid) {
        int scale = 1;
        scale = Math.max(scale, 1);
        int initialWidth = Math.min(512, (endCoord.x - startCoord.x + 1) * 16 / scale);
        int initialHeight = Math.min(512, (endCoord.z - startCoord.z + 1) * 16 / scale);
        image = new NativeImage(initialWidth, initialHeight, false);
        RegionImageCache cache = RegionImageCache.INSTANCE;
        RegionCoord rc = null;
        NativeImage regionImage = null;
        int rx1 = RegionCoord.getRegionPos(startCoord.x);
        int rx2 = RegionCoord.getRegionPos(endCoord.x);
        int rz1 = RegionCoord.getRegionPos(startCoord.z);
        int rz2 = RegionCoord.getRegionPos(endCoord.z);
        boolean imageDrawn = false;
        for (int rx = rx1; rx <= rx2; rx++) {
            for (int rz = rz1; rz <= rz2; rz++) {
                rc = new RegionCoord(worldDir, rx, rz, mapType.dimension);
                regionImage = cache.getRegionImageSet(rc).getImage(mapType);
                if (regionImage != null) {
                    int rminCx = Math.max(rc.getMinChunkX(), startCoord.x);
                    int rminCz = Math.max(rc.getMinChunkZ(), startCoord.z);
                    int rmaxCx = Math.min(rc.getMaxChunkX(), endCoord.x);
                    int rmaxCz = Math.min(rc.getMaxChunkZ(), endCoord.z);
                    int xoffset = rc.getMinChunkX() * 16;
                    int yoffset = rc.getMinChunkZ() * 16;
                    int sx1 = rminCx * 16 - xoffset;
                    int sy1 = rminCz * 16 - yoffset;
                    int sx2 = sx1 + (rmaxCx - rminCx + 1) * 16;
                    int sy2 = sy1 + (rmaxCz - rminCz + 1) * 16;
                    xoffset = startCoord.x * 16;
                    yoffset = startCoord.z * 16;
                    int dx1 = startCoord.x * 16 - xoffset;
                    int dy1 = startCoord.z * 16 - yoffset;
                    int dx2 = dx1 + (endCoord.x - startCoord.x + 1) * 16;
                    int dy2 = dy1 + (endCoord.z - startCoord.z + 1) * 16;
                    for (int x = 0; x < sx2; x++) {
                        for (int y = 0; y < sy2; y++) {
                            int pixel = regionImage.getPixelRGBA(x, y);
                            image.setPixelRGBA(x, y, pixel);
                        }
                    }
                    imageDrawn = true;
                }
            }
        }
        if (imageDrawn && showGrid) {
            int color;
            if (mapType.isDay()) {
                color = RGB.toArbg(0, 0.25F);
            } else {
                color = RGB.toArbg(8421504, 0.1F);
            }
            for (int y = 0; y < initialHeight; y += 16) {
                for (int x = 0; x < initialWidth; x++) {
                    image.blendPixel(x, y, color);
                }
            }
            for (int x = 0; x < initialWidth; x += 16) {
                for (int y = 0; y < initialHeight; y++) {
                    image.blendPixel(x, y, color);
                }
            }
        }
        if (allowNullImage && !imageDrawn) {
            return null;
        } else {
            return imageHeight == null || imageWidth == null || initialHeight == imageHeight && initialWidth == imageWidth ? image : ImageUtil.getSizedImage(imageWidth, imageHeight, image, true);
        }
    }

    public static synchronized List<TileDrawStep> getTileDrawSteps(File worldDir, ChunkPos startCoord, ChunkPos endCoord, MapType mapType, Integer zoom, boolean highQuality) {
        boolean isUnderground = mapType.isUnderground();
        int rx1 = RegionCoord.getRegionPos(startCoord.x);
        int rx2 = RegionCoord.getRegionPos(endCoord.x);
        int rz1 = RegionCoord.getRegionPos(startCoord.z);
        int rz2 = RegionCoord.getRegionPos(endCoord.z);
        List<TileDrawStep> drawSteps = new ArrayList();
        for (int rx = rx1; rx <= rx2; rx++) {
            for (int rz = rz1; rz <= rz2; rz++) {
                RegionCoord rc = new RegionCoord(worldDir, rx, rz, mapType.dimension);
                int rminCx = Math.max(rc.getMinChunkX(), startCoord.x);
                int rminCz = Math.max(rc.getMinChunkZ(), startCoord.z);
                int rmaxCx = Math.min(rc.getMaxChunkX(), endCoord.x);
                int rmaxCz = Math.min(rc.getMaxChunkZ(), endCoord.z);
                int xoffset = rc.getMinChunkX() * 16;
                int yoffset = rc.getMinChunkZ() * 16;
                int sx1 = rminCx * 16 - xoffset;
                int sy1 = rminCz * 16 - yoffset;
                int sx2 = sx1 + (rmaxCx - rminCx + 1) * 16;
                int sy2 = sy1 + (rmaxCz - rminCz + 1) * 16;
                drawSteps.add(TileDrawStepCache.getOrCreate(mapType, rc, zoom, highQuality, sx1, sy1, sx2, sy2));
            }
        }
        return drawSteps;
    }

    public static File getBlank512x512ImageFile() {
        File dataDir = new File(FileHandler.MinecraftDirectory, Constants.DATA_DIR);
        File tmpFile = new File(dataDir, "blank512x512.png");
        if (!tmpFile.canRead()) {
            try (NativeImage image = ImageUtil.getNewBlankImage(512, 512)) {
                dataDir.mkdirs();
                image.writeToFile(tmpFile);
                tmpFile.setReadOnly();
                tmpFile.deleteOnExit();
            } catch (IOException var7) {
                Journeymap.getLogger().error("Could not create blank temp file " + tmpFile + ": " + LogFormatter.toString(var7));
            }
        }
        return tmpFile;
    }
}