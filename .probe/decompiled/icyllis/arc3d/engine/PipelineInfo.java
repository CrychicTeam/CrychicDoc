package icyllis.arc3d.engine;

import javax.annotation.concurrent.Immutable;

@Immutable
public final class PipelineInfo {

    public static final int kNone_Flag = 0;

    public static final int kConservativeRaster_Flag = 1;

    public static final int kWireframe_Flag = 2;

    public static final int kSnapToPixels_Flag = 4;

    public static final int kHasScissorClip_Flag = 8;

    public static final int kHasStencilClip_Flag = 16;

    public static final int kRenderPassBlendBarrier_Flag = 32;

    private final BackendFormat mBackendFormat;

    private final int mSampleCount;

    private final int mOrigin;

    private final short mWriteSwizzle;

    private final GeometryProcessor mGeomProc;

    private final UserStencilSettings mUserStencilSettings;

    private final int mFlags;

    private boolean mNeedsStencil;

    private boolean mTargetHasVkResolveAttachmentWithInput;

    public PipelineInfo(SurfaceView writeView, GeometryProcessor geomProc, TransferProcessor xferProc, FragmentProcessor colorFragProc, FragmentProcessor coverageFragProc, UserStencilSettings userStencilSettings, int pipelineFlags) {
        assert writeView != null;
        this.mBackendFormat = writeView.getSurface().getBackendFormat();
        this.mSampleCount = writeView.getSurface().getSampleCount();
        this.mOrigin = writeView.getOrigin();
        this.mWriteSwizzle = writeView.getSwizzle();
        this.mGeomProc = geomProc;
        this.mUserStencilSettings = userStencilSettings;
        this.mFlags = pipelineFlags;
    }

    public UserStencilSettings userStencilSettings() {
        return this.mUserStencilSettings;
    }

    public BackendFormat backendFormat() {
        return this.mBackendFormat;
    }

    public int origin() {
        return this.mOrigin;
    }

    public short writeSwizzle() {
        return this.mWriteSwizzle;
    }

    public int sampleCount() {
        return this.mSampleCount;
    }

    public GeometryProcessor geomProc() {
        return this.mGeomProc;
    }

    public byte primitiveType() {
        return this.mGeomProc.primitiveType();
    }

    public boolean hasScissorClip() {
        return (this.mFlags & 8) != 0;
    }

    public boolean hasStencilClip() {
        return (this.mFlags & 16) != 0;
    }

    public boolean isStencilEnabled() {
        return this.mUserStencilSettings != null || this.hasStencilClip();
    }

    public boolean needsBlendBarrier() {
        return (this.mFlags & 32) != 0;
    }
}