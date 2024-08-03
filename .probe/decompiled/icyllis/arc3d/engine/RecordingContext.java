package icyllis.arc3d.engine;

import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public sealed class RecordingContext extends Context permits DirectContext {

    private final Thread mOwnerThread;

    private final SurfaceProvider mSurfaceProvider;

    private RenderTaskManager mRenderTaskManager;

    private final PipelineDesc mLookupDesc = new PipelineDesc();

    protected RecordingContext(SharedContextInfo context) {
        super(context);
        this.mOwnerThread = Thread.currentThread();
        this.mSurfaceProvider = new SurfaceProvider(this);
    }

    @Nullable
    public static RecordingContext makeRecording(SharedContextInfo context) {
        RecordingContext rContext = new RecordingContext(context);
        if (rContext.init()) {
            return rContext;
        } else {
            rContext.unref();
            return null;
        }
    }

    public boolean isDiscarded() {
        return this.mContextInfo.isDiscarded();
    }

    public final boolean isImageCompatible(int colorType) {
        return this.getDefaultBackendFormat(colorType, false) != null;
    }

    public final boolean isSurfaceCompatible(int colorType) {
        colorType = Engine.colorTypeToPublic(colorType);
        return 13 != colorType && 20 != colorType && 21 != colorType && 14 != colorType && 15 != colorType && 22 != colorType ? this.getMaxSurfaceSampleCount(colorType) > 0 : false;
    }

    public final int getMaxTextureSize() {
        return this.getCaps().mMaxTextureSize;
    }

    public final int getMaxRenderTargetSize() {
        return this.getCaps().mMaxRenderTargetSize;
    }

    @Internal
    public final SurfaceProvider getSurfaceProvider() {
        return this.mSurfaceProvider;
    }

    @Internal
    public final RenderTaskManager getRenderTaskManager() {
        return this.mRenderTaskManager;
    }

    @Internal
    public final ThreadSafeCache getThreadSafeCache() {
        return this.mContextInfo.getThreadSafeCache();
    }

    @Internal
    public final PipelineStateCache getPipelineStateCache() {
        return this.mContextInfo.getPipelineStateCache();
    }

    @Internal
    public final GraphicsPipelineState findOrCreateGraphicsPipelineState(PipelineInfo pipelineInfo) {
        this.mLookupDesc.clear();
        return this.getPipelineStateCache().findOrCreateGraphicsPipelineState(this.mLookupDesc, pipelineInfo);
    }

    @Override
    protected boolean init() {
        if (!super.init()) {
            return false;
        } else {
            if (this.mRenderTaskManager != null) {
                this.mRenderTaskManager.destroy();
            }
            this.mRenderTaskManager = new RenderTaskManager(this);
            return true;
        }
    }

    protected void discard() {
        if (this.mContextInfo.discard() && this.mRenderTaskManager != null) {
            throw new AssertionError();
        } else {
            if (this.mRenderTaskManager != null) {
                this.mRenderTaskManager.destroy();
            }
            this.mRenderTaskManager = null;
        }
    }

    @Override
    protected void deallocate() {
        if (this.mRenderTaskManager != null) {
            this.mRenderTaskManager.destroy();
        }
        this.mRenderTaskManager = null;
    }

    public final Thread getOwnerThread() {
        return this.mOwnerThread;
    }

    public final boolean isOwnerThread() {
        return Thread.currentThread() == this.mOwnerThread;
    }

    public final void checkOwnerThread() {
        if (Thread.currentThread() != this.mOwnerThread) {
            throw new IllegalStateException("Method expected to call from " + this.mOwnerThread + ", current " + Thread.currentThread() + ", deferred " + !(this instanceof DirectContext));
        }
    }
}