package journeymap.client.texture;

import com.google.common.base.MoreObjects;
import com.mojang.blaze3d.platform.NativeImage;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.ReentrantLock;
import journeymap.client.log.JMLogger;
import journeymap.client.render.RenderWrapper;
import journeymap.client.task.main.ExpireTextureTask;
import journeymap.common.Journeymap;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.level.ChunkPos;

public class RegionTexture extends DynamicTextureImpl implements Texture {

    protected final ReentrantLock bufferLock = new ReentrantLock();

    protected HashSet<ChunkPos> dirtyChunks = new HashSet();

    protected List<WeakReference<RegionTexture.Listener>> listeners = new ArrayList(0);

    protected long lastImageUpdate;

    protected String description;

    protected boolean bindNeeded;

    protected long lastBound;

    protected NativeImage image;

    protected NativeImage renderTarget;

    protected int width;

    protected int height;

    public RegionTexture(NativeImage pixels, String description) {
        super(pixels);
        this.setNativeImage(pixels, true);
        this.description = description;
    }

    @Override
    public void bind() {
        if (this.bindNeeded && this.bufferLock.tryLock()) {
            try {
                if (this.f_117950_ == -1) {
                    this.f_117950_ = this.m_117963_();
                }
                super.m_117966_();
                if (this.lastBound != 0L && !this.dirtyChunks.isEmpty()) {
                    for (ChunkPos pos : this.dirtyChunks) {
                        try (NativeImage chunkImage = ImageUtil.getSubImage(pos.x, pos.z, 16, 16, this.image, false)) {
                            chunkImage.upload(0, pos.x, pos.z, 0, 0, 16, 16, false, false);
                        }
                    }
                    int err;
                    while ((err = RenderWrapper.getError()) != 0) {
                        JMLogger.logOnce("GL Error in RegionTexture after upload: " + err + " in " + this);
                    }
                    this.dirtyChunks.clear();
                    this.lastBound = System.currentTimeMillis();
                    this.bindNeeded = false;
                    return;
                }
                RenderWrapper.texParameter(3553, 33085, 0);
                RenderWrapper.texParameter(3553, 33082, 0);
                RenderWrapper.texParameter(3553, 33083, 0);
                RenderWrapper.texParameter(3553, 34049, 0);
                RenderWrapper.texImage2D(3553, 0, this.renderTarget.format().glFormat(), this.renderTarget.getWidth(), this.renderTarget.getHeight(), 0, 6408, 5121, null);
                this.renderTarget.upload(0, 0, 0, false);
                this.bindNeeded = false;
                this.lastBound = System.currentTimeMillis();
            } catch (Throwable var13) {
                Journeymap.getLogger().warn("Can't bind texture: ", var13);
                return;
            } finally {
                this.bufferLock.unlock();
            }
        }
    }

    public long getLastImageUpdate() {
        return this.lastImageUpdate;
    }

    public boolean isBound() {
        return this.f_117950_ != -1;
    }

    public boolean isDefunct() {
        return this.image == null && this.f_117950_ == -1 || this.image.pixels == 0L;
    }

    public NativeImage getSubImage(int x, int y, int height, int width) {
        return ImageUtil.getSubImage(x, y, width, height, this.image, false);
    }

    @Override
    public void setNativeImage(NativeImage image, boolean retainImage) {
        if (image != null) {
            try {
                this.bufferLock.lock();
                if (this.renderTarget == null || this.renderTarget.pixels == 0L) {
                    this.renderTarget = new NativeImage(image.getWidth(), image.getHeight(), false);
                }
                this.renderTarget.copyFrom(image);
                this.handleImage(image, retainImage);
                this.bindNeeded = true;
            } finally {
                this.bufferLock.unlock();
            }
            this.lastImageUpdate = System.currentTimeMillis();
            this.notifyListeners();
        }
    }

    public void setNativeImage(NativeImage image, boolean retainImage, HashSet<ChunkPos> updatedChunks) {
        if (updatedChunks.size() > 15) {
            this.setNativeImage(image, retainImage);
        } else {
            this.handleImage(image, retainImage);
        }
        this.dirtyChunks.addAll(updatedChunks);
        this.lastImageUpdate = System.currentTimeMillis();
        this.notifyListeners();
    }

    private void handleImage(NativeImage image, boolean retainImage) {
        this.bindNeeded = true;
        try {
            this.bufferLock.lock();
            this.width = image.getWidth();
            this.height = image.getHeight();
            if (retainImage) {
                if (this.image == null) {
                    this.image = image;
                } else if (image.pixels != this.image.pixels) {
                    this.image.copyFrom(image);
                }
            }
            if (image.pixels != this.image.pixels) {
                image.close();
            }
        } finally {
            this.bufferLock.unlock();
        }
    }

    public Set<ChunkPos> getDirtyAreas() {
        return this.dirtyChunks;
    }

    public void addListener(RegionTexture.Listener addedListener) {
        Iterator<WeakReference<RegionTexture.Listener>> iter = this.listeners.iterator();
        while (iter.hasNext()) {
            WeakReference<RegionTexture.Listener> ref = (WeakReference<RegionTexture.Listener>) iter.next();
            RegionTexture.Listener<?> listener = (RegionTexture.Listener<?>) ref.get();
            if (listener == null) {
                iter.remove();
            } else if (addedListener == listener) {
                return;
            }
        }
        this.listeners.add(new WeakReference(addedListener));
    }

    protected void notifyListeners() {
        Iterator<WeakReference<RegionTexture.Listener>> iter = this.listeners.iterator();
        while (iter.hasNext()) {
            WeakReference<RegionTexture.Listener> ref = (WeakReference<RegionTexture.Listener>) iter.next();
            RegionTexture.Listener listener = (RegionTexture.Listener) ref.get();
            if (listener == null) {
                iter.remove();
            } else {
                listener.textureImageUpdated(this);
            }
        }
    }

    public String toString() {
        return MoreObjects.toStringHelper(this).add("glid", this.f_117950_).add("description", this.description).add("lastImageUpdate", this.lastImageUpdate).toString();
    }

    public boolean bindNeeded() {
        return this.bindNeeded;
    }

    @Override
    public int getWidth() {
        return this.image.getWidth();
    }

    @Override
    public int getHeight() {
        return this.image.getHeight();
    }

    @Override
    public void setDisplayWidth(int width) {
    }

    @Override
    public void setDisplayHeight(int height) {
    }

    @Override
    public Texture getScaledImage(float drawScale) {
        return null;
    }

    @Override
    public int getTextureId() {
        return this.f_117950_;
    }

    @Override
    public boolean hasImage() {
        return this.image != null && this.image.pixels > 0L;
    }

    @Override
    public void remove() {
        this.bufferLock.lock();
        ImageUtil.clearAndClose(this.image);
        ImageUtil.clearAndClose(this.renderTarget);
        this.bufferLock.unlock();
        this.bindNeeded = false;
        this.image = null;
        this.renderTarget = null;
        this.lastImageUpdate = 0L;
        this.lastBound = 0L;
        this.f_117950_ = -1;
    }

    @Override
    public void close() {
        if (this.isBound()) {
            ExpireTextureTask.queue(this.f_117950_);
        }
        this.image.close();
    }

    @Override
    public void setNativeImage(NativeImage image) {
        this.setNativeImage(image, true);
    }

    @Override
    public NativeImage getNativeImage() {
        return this.image;
    }

    @Override
    public float getAlpha() {
        return 0.0F;
    }

    @Override
    public void setAlpha(float alpha) {
    }

    @Override
    public void load(ResourceManager pResourceManager) {
    }

    public interface Listener<T extends Texture> {

        void textureImageUpdated(T var1);
    }
}