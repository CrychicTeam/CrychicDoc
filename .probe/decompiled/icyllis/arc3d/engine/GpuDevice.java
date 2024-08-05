package icyllis.arc3d.engine;

import icyllis.arc3d.compiler.ShaderCompiler;
import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.MathUtil;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.SharedPtr;
import java.util.ArrayList;
import java.util.Set;
import javax.annotation.Nullable;

public abstract class GpuDevice implements Engine {

    protected final DirectContext mContext;

    protected final Caps mCaps;

    protected final ShaderCompiler mCompiler;

    protected final GpuDevice.Stats mStats = new GpuDevice.Stats();

    private final ArrayList<FlushInfo.SubmittedCallback> mSubmittedCallbacks = new ArrayList();

    private int mResetBits = -1;

    protected GpuDevice(DirectContext context, Caps caps) {
        assert context != null && caps != null;
        this.mContext = context;
        this.mCaps = caps;
        this.mCompiler = new ShaderCompiler();
    }

    public final DirectContext getContext() {
        return this.mContext;
    }

    public Caps getCaps() {
        return this.mCaps;
    }

    public final ShaderCompiler getShaderCompiler() {
        return this.mCompiler;
    }

    public abstract ResourceProvider getResourceProvider();

    public abstract PipelineStateCache getPipelineStateCache();

    public void disconnect(boolean cleanup) {
    }

    public final GpuDevice.Stats getStats() {
        return this.mStats;
    }

    public final void markContextDirty(int state) {
        this.mResetBits |= state;
    }

    protected void handleDirtyContext(int state) {
        int dirtyBits = this.mResetBits & state;
        if (dirtyBits != 0) {
            this.onResetContext(dirtyBits);
            this.mResetBits &= ~dirtyBits;
        }
    }

    protected void onResetContext(int resetBits) {
    }

    public abstract GpuBufferPool getVertexPool();

    public abstract GpuBufferPool getInstancePool();

    public abstract GpuBufferPool getIndexPool();

    @Nullable
    @SharedPtr
    public final GpuTexture createTexture(int width, int height, BackendFormat format, int sampleCount, int surfaceFlags, String label) {
        if (format.isCompressed()) {
            return null;
        } else if (!this.mCaps.validateSurfaceParams(width, height, format, sampleCount, surfaceFlags)) {
            return null;
        } else {
            int maxMipLevel = (surfaceFlags & 4) != 0 ? MathUtil.floorLog2(Math.max(width, height)) : 0;
            int mipLevelCount = maxMipLevel + 1;
            if ((surfaceFlags & 8) != 0) {
                sampleCount = this.mCaps.getRenderTargetSampleCount(sampleCount, format);
            }
            assert sampleCount > 0 && sampleCount <= 64;
            GpuTexture texture = this.onCreateTexture(width, height, format, mipLevelCount, sampleCount, surfaceFlags);
            if (texture != null) {
                assert texture.getBackendFormat() == format;
                assert (surfaceFlags & 8) == 0 || texture.asRenderTarget() != null;
                if (label != null) {
                    texture.setLabel(label);
                }
                this.mStats.incTextureCreates();
            }
            return texture;
        }
    }

    @Nullable
    @SharedPtr
    protected abstract GpuTexture onCreateTexture(int var1, int var2, BackendFormat var3, int var4, int var5, int var6);

    @Nullable
    @SharedPtr
    public GpuTexture wrapRenderableBackendTexture(BackendTexture texture, int sampleCount, boolean ownership) {
        if (sampleCount < 1) {
            return null;
        } else {
            Caps caps = this.mCaps;
            if (!caps.isFormatTexturable(texture.getBackendFormat()) || !caps.isFormatRenderable(texture.getBackendFormat(), sampleCount)) {
                return null;
            } else {
                return texture.getWidth() <= caps.maxRenderTargetSize() && texture.getHeight() <= caps.maxRenderTargetSize() ? this.onWrapRenderableBackendTexture(texture, sampleCount, ownership) : null;
            }
        }
    }

    @Nullable
    @SharedPtr
    protected abstract GpuTexture onWrapRenderableBackendTexture(BackendTexture var1, int var2, boolean var3);

    @Nullable
    @SharedPtr
    public GpuRenderTarget wrapBackendRenderTarget(BackendRenderTarget backendRenderTarget) {
        return !this.getCaps().isFormatRenderable(backendRenderTarget.getBackendFormat(), backendRenderTarget.getSampleCount()) ? null : this.onWrapBackendRenderTarget(backendRenderTarget);
    }

    @Nullable
    @SharedPtr
    public abstract GpuRenderTarget onWrapBackendRenderTarget(BackendRenderTarget var1);

    public boolean writePixels(GpuTexture texture, int x, int y, int width, int height, int dstColorType, int srcColorType, int rowBytes, long pixels) {
        assert texture != null;
        if (x < 0 || y < 0 || width <= 0 || height <= 0) {
            return false;
        } else if (texture.isReadOnly()) {
            return false;
        } else {
            assert texture.getWidth() > 0 && texture.getHeight() > 0;
            if (x + width <= texture.getWidth() && y + height <= texture.getHeight()) {
                int bpp = ImageInfo.bytesPerPixel(srcColorType);
                int minRowBytes = width * bpp;
                if (rowBytes < minRowBytes) {
                    return false;
                } else if (rowBytes % bpp != 0) {
                    return false;
                } else if (pixels == 0L) {
                    return true;
                } else if (!this.onWritePixels(texture, x, y, width, height, dstColorType, srcColorType, rowBytes, pixels)) {
                    return false;
                } else {
                    if (texture.isMipmapped()) {
                        texture.setMipmapsDirty(true);
                    }
                    this.mStats.incTextureUploads();
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    protected abstract boolean onWritePixels(GpuTexture var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, long var9);

    public final boolean generateMipmaps(GpuTexture texture) {
        assert texture != null;
        assert texture.isMipmapped();
        if (!texture.isMipmapsDirty()) {
            return true;
        } else if (texture.isReadOnly()) {
            return false;
        } else if (this.onGenerateMipmaps(texture)) {
            texture.setMipmapsDirty(false);
            return true;
        } else {
            return false;
        }
    }

    protected abstract boolean onGenerateMipmaps(GpuTexture var1);

    public final boolean copySurface(IGpuSurface src, int srcX, int srcY, IGpuSurface dst, int dstX, int dstY, int width, int height) {
        return this.copySurface(src, srcX, srcY, srcX + width, srcY + height, dst, dstX, dstY, dstX + width, dstY + height, 0);
    }

    public final boolean copySurface(IGpuSurface src, int srcL, int srcT, int srcR, int srcB, IGpuSurface dst, int dstL, int dstT, int dstR, int dstB, int filter) {
        return (dst.getSurfaceFlags() & 32) != 0 ? false : this.onCopySurface(src, srcL, srcT, srcR, srcB, dst, dstL, dstT, dstR, dstB, filter);
    }

    protected abstract boolean onCopySurface(IGpuSurface var1, int var2, int var3, int var4, int var5, IGpuSurface var6, int var7, int var8, int var9, int var10, int var11);

    @Nullable
    public final OpsRenderPass getOpsRenderPass(SurfaceView writeView, Rect2i contentBounds, byte colorOps, byte stencilOps, float[] clearColor, Set<TextureProxy> sampledTextures, int pipelineFlags) {
        this.mStats.incRenderPasses();
        return this.onGetOpsRenderPass(writeView, contentBounds, colorOps, stencilOps, clearColor, sampledTextures, pipelineFlags);
    }

    protected abstract OpsRenderPass onGetOpsRenderPass(SurfaceView var1, Rect2i var2, byte var3, byte var4, float[] var5, Set<TextureProxy> var6, int var7);

    public void resolveRenderTarget(GpuRenderTarget renderTarget, int resolveLeft, int resolveTop, int resolveRight, int resolveBottom) {
        assert renderTarget != null;
        this.onResolveRenderTarget(renderTarget, resolveLeft, resolveTop, resolveRight, resolveBottom);
    }

    protected abstract void onResolveRenderTarget(GpuRenderTarget var1, int var2, int var3, int var4, int var5);

    @Nullable
    @SharedPtr
    public final GpuBuffer createBuffer(int size, int flags) {
        if (size <= 0) {
            new Throwable("RHICreateBuffer, invalid size: " + size).printStackTrace(this.getContext().getErrorWriter());
            return null;
        } else {
            return (flags & 12) != 0 && (flags & 64) != 0 ? null : this.onCreateBuffer(size, flags);
        }
    }

    @Nullable
    @SharedPtr
    protected abstract GpuBuffer onCreateBuffer(int var1, int var2);

    public abstract long insertFence();

    public abstract boolean checkFence(long var1);

    public abstract void deleteFence(long var1);

    public abstract void addFinishedCallback(FlushInfo.FinishedCallback var1);

    public abstract void checkFinishedCallbacks();

    public abstract void waitForQueue();

    static {
        assert 0 == Engine.LoadStoreOps.make((byte) 0, (byte) 0);
        assert 1 == Engine.LoadStoreOps.make((byte) 1, (byte) 0);
        assert 2 == Engine.LoadStoreOps.make((byte) 2, (byte) 0);
        assert 16 == Engine.LoadStoreOps.make((byte) 0, (byte) 1);
        assert 17 == Engine.LoadStoreOps.make((byte) 1, (byte) 1);
        assert 18 == Engine.LoadStoreOps.make((byte) 2, (byte) 1);
    }

    public static final class Stats {

        private long mTextureCreates = 0L;

        private long mTextureUploads = 0L;

        private long mTransfersToTexture = 0L;

        private long mTransfersFromSurface = 0L;

        private long mStencilAttachmentCreates = 0L;

        private long mMSAAAttachmentCreates = 0L;

        private long mNumDraws = 0L;

        private long mNumFailedDraws = 0L;

        private long mNumSubmitToGpus = 0L;

        private long mNumScratchTexturesReused = 0L;

        private long mNumScratchMSAAAttachmentsReused = 0L;

        private long mRenderPasses = 0L;

        public void reset() {
            this.mTextureCreates = 0L;
            this.mTextureUploads = 0L;
            this.mTransfersToTexture = 0L;
            this.mTransfersFromSurface = 0L;
            this.mStencilAttachmentCreates = 0L;
            this.mMSAAAttachmentCreates = 0L;
            this.mNumDraws = 0L;
            this.mNumFailedDraws = 0L;
            this.mNumSubmitToGpus = 0L;
            this.mNumScratchTexturesReused = 0L;
            this.mNumScratchMSAAAttachmentsReused = 0L;
            this.mRenderPasses = 0L;
        }

        public long numTextureCreates() {
            return this.mTextureCreates;
        }

        public void incTextureCreates() {
            this.mTextureCreates++;
        }

        public long numTextureUploads() {
            return this.mTextureUploads;
        }

        public void incTextureUploads() {
            this.mTextureUploads++;
        }

        public long numTransfersToTexture() {
            return this.mTransfersToTexture;
        }

        public void incTransfersToTexture() {
            this.mTransfersToTexture++;
        }

        public long numTransfersFromSurface() {
            return this.mTransfersFromSurface;
        }

        public void incTransfersFromSurface() {
            this.mTransfersFromSurface++;
        }

        public long numStencilAttachmentCreates() {
            return this.mStencilAttachmentCreates;
        }

        public void incStencilAttachmentCreates() {
            this.mStencilAttachmentCreates++;
        }

        public long msaaAttachmentCreates() {
            return this.mMSAAAttachmentCreates;
        }

        public void incMSAAAttachmentCreates() {
            this.mMSAAAttachmentCreates++;
        }

        public long numDraws() {
            return this.mNumDraws;
        }

        public void incNumDraws() {
            this.mNumDraws++;
        }

        public void incNumDraws(int increment) {
            this.mNumDraws += (long) increment;
        }

        public long numFailedDraws() {
            return this.mNumFailedDraws;
        }

        public void incNumFailedDraws() {
            this.mNumFailedDraws++;
        }

        public long numSubmitToGpus() {
            return this.mNumSubmitToGpus;
        }

        public void incNumSubmitToGpus() {
            this.mNumSubmitToGpus++;
        }

        public long numScratchTexturesReused() {
            return this.mNumScratchTexturesReused;
        }

        public void incNumScratchTexturesReused() {
            this.mNumScratchTexturesReused++;
        }

        public long numScratchMSAAAttachmentsReused() {
            return this.mNumScratchMSAAAttachmentsReused;
        }

        public void incNumScratchMSAAAttachmentsReused() {
            this.mNumScratchMSAAAttachmentsReused++;
        }

        public long numRenderPasses() {
            return this.mRenderPasses;
        }

        public void incRenderPasses() {
            this.mRenderPasses++;
        }

        public String toString() {
            return "Device.Stats{mTextureCreates=" + this.mTextureCreates + ", mTextureUploads=" + this.mTextureUploads + ", mTransfersToTexture=" + this.mTransfersToTexture + ", mTransfersFromSurface=" + this.mTransfersFromSurface + ", mStencilAttachmentCreates=" + this.mStencilAttachmentCreates + ", mMSAAAttachmentCreates=" + this.mMSAAAttachmentCreates + ", mNumDraws=" + this.mNumDraws + ", mNumFailedDraws=" + this.mNumFailedDraws + ", mNumSubmitToGpus=" + this.mNumSubmitToGpus + ", mNumScratchTexturesReused=" + this.mNumScratchTexturesReused + ", mNumScratchMSAAAttachmentsReused=" + this.mNumScratchMSAAAttachmentsReused + ", mRenderPasses=" + this.mRenderPasses + "}";
        }
    }
}