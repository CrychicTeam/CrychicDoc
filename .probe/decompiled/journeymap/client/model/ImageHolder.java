package journeymap.client.model;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.NativeImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
import journeymap.client.io.RegionImageHandler;
import journeymap.client.log.StatTimer;
import journeymap.client.task.main.ExpireTextureTask;
import journeymap.client.texture.ImageUtil;
import journeymap.client.texture.RegionTexture;
import journeymap.common.Journeymap;
import journeymap.common.log.LogFormatter;
import net.minecraft.Util;
import net.minecraft.world.level.ChunkPos;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

public class ImageHolder {

    static final Logger logger = Journeymap.getLogger();

    final MapType mapType;

    final Path imagePath;

    final int imageSize;

    boolean blank = true;

    boolean dirty = true;

    boolean partialUpdate;

    private volatile ReentrantLock writeLock = new ReentrantLock();

    private volatile RegionTexture texture;

    private boolean debug;

    private HashSet<ChunkPos> updatedChunks = new HashSet();

    ImageHolder(MapType mapType, File imageFile, int imageSize) {
        this.mapType = mapType;
        this.imagePath = imageFile.toPath();
        this.imageSize = imageSize;
        this.debug = logger.isEnabled(Level.DEBUG);
        this.getTexture();
    }

    File getFile() {
        return this.imagePath.toFile();
    }

    MapType getMapType() {
        return this.mapType;
    }

    NativeImage getImage() {
        return this.texture.getNativeImage();
    }

    void setImage(NativeImage image) {
        this.texture.setNativeImage(image, true);
        this.setDirty();
    }

    void partialImageUpdate(NativeImage imagePart, int startX, int startY) {
        this.writeLock.lock();
        StatTimer timer = StatTimer.get("ImageHolder.partialImageUpdate", 5, 500);
        timer.start();
        try {
            if (this.texture != null) {
                this.blank = false;
                int width = imagePart.getWidth();
                int height = imagePart.getHeight();
                for (int y = 0; y < height; y++) {
                    for (int x = 0; x < width; x++) {
                        this.texture.getNativeImage().setPixelRGBA(x + startX, y + startY, imagePart.getPixelRGBA(x, y));
                    }
                }
                this.partialUpdate = true;
                this.updatedChunks.add(new ChunkPos(startX, startY));
            } else {
                logger.warn(this + " can't partialImageUpdate without a texture.");
            }
        } finally {
            timer.stop();
            this.writeLock.unlock();
        }
    }

    void finishPartialImageUpdates() {
        this.writeLock.lock();
        try {
            if (this.partialUpdate && !this.updatedChunks.isEmpty()) {
                NativeImage textureImage = this.texture.getNativeImage();
                this.texture.setNativeImage(textureImage, true, this.updatedChunks);
                this.setDirty();
                this.partialUpdate = false;
                this.updatedChunks.clear();
            }
        } finally {
            this.writeLock.unlock();
        }
    }

    public boolean hasTexture() {
        return this.texture != null && !this.texture.isDefunct();
    }

    public RegionTexture getTexture() {
        if (!this.hasTexture()) {
            if (!this.imagePath.toFile().exists()) {
                File temp = new File(this.imagePath + ".new");
                if (temp.exists()) {
                    Journeymap.getLogger().warn("Recovered image file: " + temp);
                    temp.renameTo(this.imagePath.toFile());
                }
            }
            NativeImage image = RegionImageHandler.readRegionImage(this.imagePath.toFile());
            if (image != null && image.pixels != 0L && image.getWidth() == this.imageSize && image.getHeight() == this.imageSize) {
                this.blank = false;
            } else {
                image = ImageUtil.getNewBlankImage(this.imageSize, this.imageSize);
                this.blank = true;
                this.dirty = false;
            }
            this.texture = new RegionTexture(image, this.imagePath.toString());
        }
        return this.texture;
    }

    private void setDirty() {
        this.dirty = true;
    }

    boolean isDirty() {
        return this.dirty;
    }

    protected boolean writeToDisk(boolean async) {
        if (this.blank || this.texture == null || !this.texture.hasImage()) {
            return false;
        } else if (async) {
            try {
                Util.ioPool().execute(this::writeNextIO);
            } catch (Exception var4) {
                Journeymap.getLogger().warn("Failed to write image async: " + this);
            }
            return true;
        } else {
            int tries = 0;
            boolean success;
            for (success = false; tries < 5; tries++) {
                if (!this.writeNextIO()) {
                    success = true;
                    break;
                }
            }
            if (!success) {
                Journeymap.getLogger().warn("Couldn't write file after 5 tries: " + this);
            }
            return success;
        }
    }

    public boolean writeNextIO() {
        if (this.texture != null && this.texture.hasImage()) {
            try {
                if (this.writeLock.tryLock(250L, TimeUnit.MILLISECONDS)) {
                    this.writeImageToFile();
                    this.writeLock.unlock();
                    return false;
                } else {
                    logger.warn("Couldn't get write lock for file: " + this.writeLock + " for " + this);
                    return true;
                }
            } catch (InterruptedException var2) {
                logger.warn("Timeout waiting for write lock  " + this.writeLock + " for " + this);
                return true;
            }
        } else {
            return false;
        }
    }

    private void writeImageToFile() {
        File imageFile = this.imagePath.toFile();
        try {
            NativeImage image = this.texture.getNativeImage();
            if (image != null) {
                if (!imageFile.exists()) {
                    imageFile.getParentFile().mkdirs();
                }
                File temp = new File(imageFile.getParentFile(), imageFile.getName() + ".new");
                image.writeToFile(temp);
                if (imageFile.exists() && !imageFile.delete()) {
                    logger.warn("Couldn't delete old file " + imageFile.getName());
                }
                if (temp.renameTo(imageFile)) {
                    this.dirty = false;
                } else {
                    logger.warn("Couldn't rename temp file to " + imageFile.getName());
                }
                if (this.debug) {
                    logger.debug("Wrote to disk: " + imageFile);
                }
            }
        } catch (IOException var5) {
            IOException e = var5;
            if (imageFile.exists()) {
                try {
                    logger.error("IOException updating file, will delete and retry: " + this + ": " + LogFormatter.toPartialString(e));
                    imageFile.delete();
                    this.writeImageToFile();
                } catch (Throwable var4) {
                    logger.error("Exception after delete/retry: " + this + ": " + LogFormatter.toPartialString(var5));
                }
            } else {
                logger.error("IOException creating file: " + this + ": " + LogFormatter.toPartialString(var5));
            }
        } catch (Throwable var6) {
            logger.error("Exception writing to disk: " + this + ": " + LogFormatter.toPartialString(var6));
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("mapType", this.mapType).add("textureId", this.texture == null ? null : this.texture.isBound() ? this.texture.getTextureId() : -1).add("dirty", this.dirty).add("imagePath", this.imagePath).toString();
    }

    public void clear() {
        this.writeLock.lock();
        ExpireTextureTask.queue(this.texture);
        this.texture = null;
        this.writeLock.unlock();
    }

    public long getImageTimestamp() {
        return this.texture != null ? this.texture.getLastImageUpdate() : 0L;
    }
}