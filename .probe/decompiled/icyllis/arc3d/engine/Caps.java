package icyllis.arc3d.engine;

import icyllis.arc3d.core.ImageInfo;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public abstract class Caps {

    protected final ShaderCaps mShaderCaps = new ShaderCaps();

    protected boolean mAnisotropySupport = false;

    protected boolean mGpuTracingSupport = false;

    protected boolean mConservativeRasterSupport = false;

    protected boolean mTransferPixelsToRowBytesSupport = false;

    protected boolean mMustSyncGpuDuringDiscard = true;

    protected boolean mTextureBarrierSupport = false;

    protected boolean mDynamicStateArrayGeometryProcessorTextureSupport = false;

    protected Caps.BlendEquationSupport mBlendEquationSupport = Caps.BlendEquationSupport.BASIC;

    protected int mMapBufferFlags;

    protected int mMaxRenderTargetSize = 1;

    protected int mMaxPreferredRenderTargetSize = 1;

    protected int mMaxVertexAttributes = 0;

    protected int mMaxTextureSize = 1;

    protected int mInternalMultisampleCount = 0;

    protected int mMaxPushConstantsSize = 0;

    protected final DriverBugWorkarounds mDriverBugWorkarounds = new DriverBugWorkarounds();

    public Caps(ContextOptions options) {
        this.mDriverBugWorkarounds.applyOverrides(options.mDriverBugWorkarounds);
    }

    public final ShaderCaps shaderCaps() {
        return this.mShaderCaps;
    }

    public final boolean npotTextureTileSupport() {
        return true;
    }

    public final boolean mipmapSupport() {
        return true;
    }

    public final boolean hasAnisotropySupport() {
        return this.mAnisotropySupport;
    }

    public final boolean gpuTracingSupport() {
        return this.mGpuTracingSupport;
    }

    public final boolean oversizedStencilSupport() {
        return true;
    }

    public final boolean textureBarrierSupport() {
        return true;
    }

    public final boolean sampleLocationsSupport() {
        return true;
    }

    public final boolean drawInstancedSupport() {
        return true;
    }

    public final boolean conservativeRasterSupport() {
        return this.mConservativeRasterSupport;
    }

    public final boolean wireframeSupport() {
        return true;
    }

    public final boolean msaaResolvesAutomatically() {
        return false;
    }

    public final boolean preferDiscardableMSAAAttachment() {
        return false;
    }

    public final boolean halfFloatVertexAttributeSupport() {
        return true;
    }

    public final boolean usePrimitiveRestart() {
        return false;
    }

    public final boolean preferClientSideDynamicBuffers() {
        return false;
    }

    public final boolean preferFullscreenClears() {
        return false;
    }

    public final boolean discardStencilValuesAfterRenderPass() {
        return this.preferFullscreenClears();
    }

    public final boolean twoSidedStencilRefsAndMasksMustMatch() {
        return false;
    }

    public final boolean preferVRAMUseOverFlushes() {
        return true;
    }

    public final boolean avoidStencilBuffers() {
        return false;
    }

    public final boolean avoidWritePixelsFastPath() {
        return false;
    }

    public final boolean requiresManualFBBarrierAfterTessellatedStencilDraw() {
        return false;
    }

    public final boolean nativeDrawIndexedIndirectIsBroken() {
        return false;
    }

    public final Caps.BlendEquationSupport blendEquationSupport() {
        return this.mBlendEquationSupport;
    }

    public final boolean advancedBlendEquationSupport() {
        return this.mBlendEquationSupport != Caps.BlendEquationSupport.BASIC;
    }

    public final boolean advancedCoherentBlendEquationSupport() {
        return this.mBlendEquationSupport == Caps.BlendEquationSupport.ADVANCED_COHERENT;
    }

    public final boolean shouldCollapseSrcOverToSrcWhenAble() {
        return false;
    }

    public final boolean mustSyncGpuDuringDiscard() {
        return this.mMustSyncGpuDuringDiscard;
    }

    public final boolean supportsTextureBarrier() {
        return this.mTextureBarrierSupport;
    }

    public final boolean reducedShaderMode() {
        return this.mShaderCaps.mReducedShaderMode;
    }

    public final boolean reuseScratchTextures() {
        return true;
    }

    public final boolean reuseScratchBuffers() {
        return true;
    }

    public final int maxVertexAttributes() {
        return this.mMaxVertexAttributes;
    }

    public final int maxRenderTargetSize() {
        return this.mMaxRenderTargetSize;
    }

    public final int maxPreferredRenderTargetSize() {
        return this.mMaxPreferredRenderTargetSize;
    }

    public final int maxTextureSize() {
        return this.mMaxTextureSize;
    }

    public final int maxPushConstantsSize() {
        return this.mMaxPushConstantsSize;
    }

    public final int transferBufferAlignment() {
        return 1;
    }

    public abstract boolean isFormatTexturable(BackendFormat var1);

    public abstract int getMaxRenderTargetSampleCount(BackendFormat var1);

    public final int getInternalMultisampleCount(BackendFormat format) {
        return Math.min(this.mInternalMultisampleCount, this.getMaxRenderTargetSampleCount(format));
    }

    public abstract boolean isFormatRenderable(int var1, BackendFormat var2, int var3);

    public abstract boolean isFormatRenderable(BackendFormat var1, int var2);

    public abstract int getRenderTargetSampleCount(int var1, BackendFormat var2);

    public abstract long getSupportedWriteColorType(int var1, BackendFormat var2, int var3);

    public final long getSupportedReadColorType(int srcColorType, BackendFormat srcFormat, int dstColorType) {
        long read = this.onSupportedReadColorType(srcColorType, srcFormat, dstColorType);
        int colorType = (int) (read & 4294967295L);
        long transferOffsetAlignment = read >>> 32;
        if (colorType == 5) {
            transferOffsetAlignment = 0L;
        }
        int channelFlags = ImageInfo.colorTypeChannelFlags(colorType);
        if ((channelFlags == 15 || channelFlags == 7 || channelFlags == 8 || channelFlags == 16) && ImageInfo.bytesPerPixel(colorType) == 4) {
            switch((int) (transferOffsetAlignment & 3L)) {
                case 0:
                    break;
                case 2:
                    transferOffsetAlignment *= 2L;
                    break;
                default:
                    transferOffsetAlignment *= 4L;
            }
        }
        return (long) colorType | transferOffsetAlignment << 32;
    }

    protected abstract long onSupportedReadColorType(int var1, BackendFormat var2, int var3);

    public final boolean writePixelsRowBytesSupport() {
        return true;
    }

    public final boolean transferPixelsToRowBytesSupport() {
        return this.mTransferPixelsToRowBytesSupport;
    }

    public final boolean readPixelsRowBytesSupport() {
        return true;
    }

    public final boolean transferFromSurfaceToBufferSupport() {
        return true;
    }

    public final boolean transferFromBufferToTextureSupport() {
        return true;
    }

    public final boolean mustClearUploadedBufferData() {
        return false;
    }

    public final boolean shouldInitializeTextures() {
        return false;
    }

    public final boolean fenceSyncSupport() {
        return true;
    }

    public final boolean semaphoreSupport() {
        return true;
    }

    public final boolean crossContextTextureSupport() {
        return true;
    }

    public final boolean dynamicStateArrayGeometryProcessorTextureSupport() {
        return this.mDynamicStateArrayGeometryProcessorTextureSupport;
    }

    public final boolean performPartialClearsAsDraws() {
        return false;
    }

    public final boolean performColorClearsAsDraws() {
        return false;
    }

    public final boolean avoidLargeIndexBufferDraws() {
        return false;
    }

    public final boolean performStencilClearsAsDraws() {
        return false;
    }

    public final boolean disableTessellationPathRenderer() {
        return false;
    }

    public final boolean clampToBorderSupport() {
        return true;
    }

    public final boolean validateSurfaceParams(int width, int height, BackendFormat format, int sampleCount, int surfaceFlags) {
        if (width < 1 || height < 1) {
            return false;
        } else if (!this.isFormatTexturable(format)) {
            return false;
        } else if ((surfaceFlags & 8) != 0) {
            int maxSize = this.maxRenderTargetSize();
            return width <= maxSize && height <= maxSize ? this.isFormatRenderable(format, sampleCount) : false;
        } else {
            int maxSize = this.maxTextureSize();
            return width <= maxSize && height <= maxSize ? sampleCount == 1 : false;
        }
    }

    public final boolean validateAttachmentParams(int width, int height, BackendFormat format, int sampleCount) {
        if (width >= 1 && height >= 1) {
            int maxSize = this.maxRenderTargetSize();
            return width <= maxSize && height <= maxSize ? this.isFormatRenderable(format, sampleCount) : false;
        } else {
            return false;
        }
    }

    public final boolean isFormatCompatible(int colorType, BackendFormat format) {
        if (colorType == 0) {
            return false;
        } else {
            int compression = format.getCompressionType();
            return compression != 0 ? colorType == (DataUtils.compressionTypeIsOpaque(compression) ? 5 : 6) : this.onFormatCompatible(colorType, format);
        }
    }

    protected abstract boolean onFormatCompatible(int var1, BackendFormat var2);

    @Nullable
    public final BackendFormat getDefaultBackendFormat(int colorType, boolean renderable) {
        if (colorType == 0) {
            return null;
        } else {
            BackendFormat format = this.onGetDefaultBackendFormat(colorType);
            if (format == null || !this.isFormatTexturable(format)) {
                return null;
            } else if (!this.isFormatCompatible(colorType, format)) {
                return null;
            } else if ((this.getSupportedWriteColorType(colorType, format, colorType) & 4294967295L) == 0L) {
                return null;
            } else {
                return renderable && !this.isFormatRenderable(colorType, format, 1) ? null : format;
            }
        }
    }

    @Nullable
    protected abstract BackendFormat onGetDefaultBackendFormat(int var1);

    @Nullable
    public abstract BackendFormat getCompressedBackendFormat(int var1);

    @Nonnull
    public abstract PipelineDesc makeDesc(PipelineDesc var1, GpuRenderTarget var2, PipelineInfo var3);

    public final short getReadSwizzle(BackendFormat format, int colorType) {
        int compression = format.getCompressionType();
        if (compression != 0) {
            if (colorType == 5 || colorType == 6) {
                return 12816;
            } else {
                assert false;
                return 12816;
            }
        } else {
            return this.onGetReadSwizzle(format, colorType);
        }
    }

    protected abstract short onGetReadSwizzle(BackendFormat var1, int var2);

    public abstract short getWriteSwizzle(BackendFormat var1, int var2);

    public final DriverBugWorkarounds workarounds() {
        return this.mDriverBugWorkarounds;
    }

    protected final void finishInitialization(ContextOptions options) {
        this.mShaderCaps.applyOptionsOverrides(options);
        this.onApplyOptionsOverrides(options);
        this.mInternalMultisampleCount = options.mInternalMultisampleCount;
        this.mMaxRenderTargetSize = Math.min(this.mMaxRenderTargetSize, this.mMaxTextureSize);
        this.mMaxPreferredRenderTargetSize = Math.min(this.mMaxPreferredRenderTargetSize, this.mMaxRenderTargetSize);
    }

    protected void onApplyOptionsOverrides(ContextOptions options) {
        this.mDriverBugWorkarounds.applyOverrides(options.mDriverBugWorkarounds);
    }

    public static enum BlendEquationSupport {

        BASIC, ADVANCED, ADVANCED_COHERENT
    }
}