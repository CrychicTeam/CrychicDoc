package journeymap.client.model;

import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.util.StringJoiner;
import journeymap.client.io.RegionImageHandler;
import journeymap.client.texture.ComparableNativeImage;
import journeymap.client.texture.ImageUtil;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;

public class RegionImageSet extends ImageSet {

    protected final RegionImageSet.Key key;

    public RegionImageSet(RegionImageSet.Key key) {
        this.key = key;
    }

    @Override
    public ImageHolder getHolder(MapType mapType) {
        synchronized (this.imageHolders) {
            ImageHolder imageHolder = (ImageHolder) this.imageHolders.get(mapType);
            if (imageHolder == null || imageHolder.getImage().pixels == 0L) {
                File imageFile = RegionImageHandler.getRegionImageFile(this.getRegionCoord(), mapType);
                imageHolder = this.addHolder(mapType, imageFile);
            }
            return imageHolder;
        }
    }

    public ComparableNativeImage getChunkImage(ChunkMD chunkMd, MapType mapType) {
        RegionCoord regionCoord = this.getRegionCoord();
        NativeImage regionImage = this.getHolder(mapType).getImage();
        return ImageUtil.getComparableSubImage(regionCoord.getXOffset(chunkMd.getCoord().x), regionCoord.getZOffset(chunkMd.getCoord().z), 16, 16, regionImage, false);
    }

    public void setChunkImage(ChunkMD chunkMd, MapType mapType, ComparableNativeImage chunkImage) {
        ImageHolder holder = this.getHolder(mapType);
        boolean wasBlank = holder.blank;
        if (chunkImage.isChanged() || wasBlank) {
            RegionCoord regionCoord = this.getRegionCoord();
            holder.partialImageUpdate(chunkImage, regionCoord.getXOffset(chunkMd.getCoord().x), regionCoord.getZOffset(chunkMd.getCoord().z));
        }
        if (wasBlank) {
            holder.getTexture();
            holder.finishPartialImageUpdates();
            RegionImageCache.INSTANCE.getRegionImageSet(this.getRegionCoord());
        }
        chunkMd.setRendered(mapType);
    }

    public boolean hasChunkUpdates() {
        synchronized (this.imageHolders) {
            for (ImageHolder holder : this.imageHolders.values()) {
                if (holder.partialUpdate) {
                    return true;
                }
            }
            return false;
        }
    }

    public void finishChunkUpdates() {
        synchronized (this.imageHolders) {
            for (ImageHolder holder : this.imageHolders.values()) {
                holder.finishPartialImageUpdates();
            }
        }
    }

    public RegionCoord getRegionCoord() {
        return RegionCoord.fromRegionPos(this.key.worldDir, this.key.regionX, this.key.regionZ, this.key.dimension);
    }

    public long getOldestTimestamp() {
        long time = System.currentTimeMillis();
        synchronized (this.imageHolders) {
            for (ImageHolder holder : this.imageHolders.values()) {
                if (holder != null) {
                    time = Math.min(time, holder.getImageTimestamp());
                }
            }
            return time;
        }
    }

    @Override
    public int hashCode() {
        return this.key.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null) {
            return false;
        } else {
            return this.getClass() != obj.getClass() ? false : this.key.equals(((RegionImageSet) obj).key);
        }
    }

    @Override
    protected int getImageSize() {
        return 512;
    }

    public static class Key {

        private final File worldDir;

        private final int regionX;

        private final int regionZ;

        private final ResourceKey<Level> dimension;

        private Key(File worldDir, int regionX, int regionZ, ResourceKey<Level> dimension) {
            this.worldDir = worldDir;
            this.regionX = regionX;
            this.regionZ = regionZ;
            this.dimension = dimension;
        }

        public static RegionImageSet.Key from(RegionCoord rCoord) {
            return new RegionImageSet.Key(rCoord.worldDir, rCoord.regionX, rCoord.regionZ, rCoord.dimension);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                RegionImageSet.Key key = (RegionImageSet.Key) o;
                if (this.dimension != key.dimension) {
                    return false;
                } else if (this.regionX != key.regionX) {
                    return false;
                } else {
                    return this.regionZ != key.regionZ ? false : this.worldDir.equals(key.worldDir);
                }
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.worldDir.hashCode();
            result = 31 * result + this.regionX;
            result = 31 * result + this.regionZ;
            return 31 * result + this.dimension.hashCode();
        }

        public String toString() {
            return new StringJoiner(", ", RegionImageSet.Key.class.getSimpleName() + "[", "]").add("worldDir=" + this.worldDir).add("regionX=" + this.regionX).add("regionZ=" + this.regionZ).add("dimension=" + this.dimension).toString();
        }
    }
}