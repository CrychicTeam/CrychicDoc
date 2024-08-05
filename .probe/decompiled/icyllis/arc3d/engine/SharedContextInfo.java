package icyllis.arc3d.engine;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.SurfaceCharacterization;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class SharedContextInfo {

    private static final AtomicInteger sNextID = new AtomicInteger(1);

    private final int mBackend;

    private final ContextOptions mOptions;

    private final int mContextID;

    private volatile Caps mCaps;

    private volatile ThreadSafeCache mThreadSafeCache;

    private volatile PipelineStateCache mPipelineStateCache;

    private final AtomicBoolean mDiscarded = new AtomicBoolean(false);

    private static int createUniqueID() {
        int value;
        int newValue;
        do {
            value = sNextID.get();
            newValue = value == -1 ? 1 : value + 1;
        } while (!sNextID.weakCompareAndSetVolatile(value, newValue));
        return value;
    }

    SharedContextInfo(int backend, ContextOptions options) {
        this.mBackend = backend;
        this.mOptions = options;
        this.mContextID = createUniqueID();
    }

    @Nullable
    public SurfaceCharacterization createCharacterization(long cacheMaxResourceBytes, ImageInfo imageInfo, BackendFormat backendFormat, int origin, int sampleCount, boolean texturable, boolean mipmapped, boolean glWrapDefaultFramebuffer, boolean vkSupportInputAttachment, boolean vkSecondaryCommandBuffer, boolean isProtected) {
        assert this.mCaps != null;
        if (!texturable && mipmapped) {
            return null;
        } else if (this.mBackend != backendFormat.getBackend()) {
            return null;
        } else if (backendFormat.getBackend() != 0 && glWrapDefaultFramebuffer) {
            return null;
        } else if (backendFormat.getBackend() == 1 || !vkSupportInputAttachment && !vkSecondaryCommandBuffer) {
            if (imageInfo.width() >= 1 && imageInfo.width() <= this.mCaps.maxRenderTargetSize() && imageInfo.height() >= 1 && imageInfo.height() <= this.mCaps.maxRenderTargetSize()) {
                int colorType = imageInfo.colorType();
                if (!this.mCaps.isFormatCompatible(colorType, backendFormat)) {
                    return null;
                } else if (!this.mCaps.isFormatRenderable(colorType, backendFormat, sampleCount)) {
                    return null;
                } else {
                    sampleCount = this.mCaps.getRenderTargetSampleCount(sampleCount, backendFormat);
                    assert sampleCount > 0;
                    if (glWrapDefaultFramebuffer && texturable) {
                        return null;
                    } else if (texturable && !this.mCaps.isFormatTexturable(backendFormat)) {
                        return null;
                    } else {
                        return !vkSecondaryCommandBuffer || !texturable && !glWrapDefaultFramebuffer && !vkSupportInputAttachment ? new SurfaceCharacterization(this, cacheMaxResourceBytes, imageInfo, backendFormat, origin, sampleCount, texturable, mipmapped, glWrapDefaultFramebuffer, vkSupportInputAttachment, vkSecondaryCommandBuffer, isProtected) : null;
                    }
                }
            } else {
                return null;
            }
        } else {
            return null;
        }
    }

    @Nullable
    public BackendFormat getDefaultBackendFormat(int colorType, boolean renderable) {
        assert this.mCaps != null;
        colorType = Engine.colorTypeToPublic(colorType);
        BackendFormat format = this.mCaps.getDefaultBackendFormat(colorType, renderable);
        if (format == null) {
            return null;
        } else {
            assert !renderable || this.mCaps.isFormatRenderable(colorType, format, 1);
            return format;
        }
    }

    @Nullable
    public BackendFormat getCompressedBackendFormat(int compressionType) {
        assert this.mCaps != null;
        BackendFormat format = this.mCaps.getCompressedBackendFormat(compressionType);
        assert format == null || !format.isExternal() && this.mCaps.isFormatTexturable(format);
        return format;
    }

    public int getMaxSurfaceSampleCount(int colorType) {
        assert this.mCaps != null;
        colorType = Engine.colorTypeToPublic(colorType);
        BackendFormat format = this.mCaps.getDefaultBackendFormat(colorType, true);
        return format == null ? 0 : this.mCaps.getMaxRenderTargetSampleCount(format);
    }

    public boolean isValid() {
        return this.mCaps != null;
    }

    @Internal
    public boolean matches(Context c) {
        return c != null && this == c.mContextInfo;
    }

    @Internal
    public int getBackend() {
        return this.mBackend;
    }

    @Internal
    public ContextOptions getOptions() {
        return this.mOptions;
    }

    @Internal
    public int getContextID() {
        return this.mContextID;
    }

    @Internal
    public Caps getCaps() {
        return this.mCaps;
    }

    @Internal
    public ThreadSafeCache getThreadSafeCache() {
        return this.mThreadSafeCache;
    }

    @Internal
    public PipelineStateCache getPipelineStateCache() {
        return this.mPipelineStateCache;
    }

    void init(Caps caps, PipelineStateCache psc) {
        assert caps != null;
        this.mCaps = caps;
        this.mThreadSafeCache = new ThreadSafeCache();
        this.mPipelineStateCache = psc;
    }

    boolean discard() {
        return !this.mDiscarded.compareAndExchange(false, true);
    }

    boolean isDiscarded() {
        return this.mDiscarded.get();
    }

    public int hashCode() {
        return this.mContextID;
    }
}