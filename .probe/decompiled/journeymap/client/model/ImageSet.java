package journeymap.client.model;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import journeymap.common.Journeymap;

public abstract class ImageSet {

    protected final Map<MapType, ImageHolder> imageHolders = Collections.synchronizedMap(new HashMap(8));

    protected abstract ImageHolder getHolder(MapType var1);

    public abstract int hashCode();

    public abstract boolean equals(Object var1);

    public NativeImage getImage(MapType mapType) {
        return this.getHolder(mapType).getImage();
    }

    public int writeToDiskAsync(boolean force) {
        return this.writeToDisk(force, true);
    }

    public int writeToDisk(boolean force) {
        return this.writeToDisk(force, false);
    }

    private int writeToDisk(boolean force, boolean async) {
        long now = System.currentTimeMillis();
        int count = 0;
        try {
            synchronized (this.imageHolders) {
                for (ImageHolder imageHolder : this.imageHolders.values()) {
                    if (imageHolder.isDirty() && (force || now - imageHolder.getImageTimestamp() > 10000L)) {
                        imageHolder.writeToDisk(async);
                        count++;
                    }
                }
            }
        } catch (Throwable var11) {
            Journeymap.getLogger().error("Error writing ImageSet to disk: " + var11);
        }
        return count;
    }

    public boolean updatedSince(MapType mapType, long time) {
        synchronized (this.imageHolders) {
            if (mapType == null) {
                for (ImageHolder holder : this.imageHolders.values()) {
                    if (holder != null && holder.getImageTimestamp() >= time) {
                        return true;
                    }
                }
            } else {
                ImageHolder imageHolder = (ImageHolder) this.imageHolders.get(mapType);
                if (imageHolder != null && imageHolder.getImageTimestamp() >= time) {
                    return true;
                }
            }
            return false;
        }
    }

    public void clear() {
        synchronized (this.imageHolders) {
            for (ImageHolder imageHolder : this.imageHolders.values()) {
                imageHolder.clear();
            }
            this.imageHolders.clear();
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("imageHolders", this.imageHolders.entrySet()).toString();
    }

    protected abstract int getImageSize();

    protected ImageHolder addHolder(MapType mapType, File imageFile) {
        return this.addHolder(new ImageHolder(mapType, imageFile, this.getImageSize()));
    }

    protected ImageHolder addHolder(ImageHolder imageHolder) {
        this.imageHolders.put(imageHolder.mapType, imageHolder);
        return imageHolder;
    }
}