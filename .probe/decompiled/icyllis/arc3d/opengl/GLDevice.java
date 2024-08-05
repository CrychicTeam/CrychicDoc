package icyllis.arc3d.opengl;

import icyllis.arc3d.core.ImageInfo;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.BackendFormat;
import icyllis.arc3d.engine.BackendRenderTarget;
import icyllis.arc3d.engine.BackendTexture;
import icyllis.arc3d.engine.ContextOptions;
import icyllis.arc3d.engine.CpuBufferPool;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.Engine;
import icyllis.arc3d.engine.FlushInfo;
import icyllis.arc3d.engine.GpuBuffer;
import icyllis.arc3d.engine.GpuBufferPool;
import icyllis.arc3d.engine.GpuDevice;
import icyllis.arc3d.engine.GpuRenderTarget;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.engine.GpuTexture;
import icyllis.arc3d.engine.IGpuSurface;
import icyllis.arc3d.engine.OpsRenderPass;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.TextureProxy;
import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLCapabilities;
import org.lwjgl.system.MemoryStack;
import org.lwjgl.system.MemoryUtil;

public final class GLDevice extends GpuDevice {

    private final GLCaps mCaps;

    private final GLCommandBuffer mMainCmdBuffer;

    private final GLResourceProvider mResourceProvider;

    private final GLPipelineStateCache mPipelineStateCache;

    private final CpuBufferPool mCpuBufferPool;

    private final GpuBufferPool mVertexPool;

    private final GpuBufferPool mInstancePool;

    private final GpuBufferPool mIndexPool;

    private GLOpsRenderPass mCachedOpsRenderPass;

    private final ArrayDeque<FlushInfo.FinishedCallback> mFinishedCallbacks = new ArrayDeque();

    private final LongArrayFIFOQueue mFinishedFences = new LongArrayFIFOQueue();

    static final GpuResource.UniqueID INVALID_UNIQUE_ID = new GpuResource.UniqueID();

    static final int BUFFER_TYPE_VERTEX = 0;

    static final int BUFFER_TYPE_INDEX = 1;

    static final int BUFFER_TYPE_XFER_SRC = 2;

    static final int BUFFER_TYPE_XFER_DST = 3;

    static final int BUFFER_TYPE_UNIFORM = 4;

    static final int BUFFER_TYPE_DRAW_INDIRECT = 5;

    private final GLDevice.HWBufferState[] mHWBufferStates = new GLDevice.HWBufferState[6];

    private final GpuResource.UniqueID[] mBoundUniformBuffers;

    private int mHWActiveTextureUnit;

    private final GpuResource.UniqueID[] mHWTextureStates;

    private final GLDevice.HWSamplerState[] mHWSamplerStates;

    private final int[] mCopySrcFramebuffer;

    private final int[] mCopyDstFramebuffer;

    private boolean mNeedsFlush;

    static int bufferUsageToType(int usage) {
        return Integer.numberOfTrailingZeros(usage);
    }

    private GLDevice(DirectContext context, GLCaps caps) {
        super(context, caps);
        this.mHWBufferStates[0] = new GLDevice.HWBufferState(34962);
        this.mHWBufferStates[1] = new GLDevice.HWBufferState(34963);
        this.mHWBufferStates[2] = new GLDevice.HWBufferState(35052);
        this.mHWBufferStates[3] = new GLDevice.HWBufferState(35051);
        this.mHWBufferStates[4] = new GLDevice.HWBufferState(35345);
        this.mHWBufferStates[5] = new GLDevice.HWBufferState(36671);
        this.mBoundUniformBuffers = new GpuResource.UniqueID[4];
        this.mCopySrcFramebuffer = new int[1];
        this.mCopyDstFramebuffer = new int[1];
        this.mCaps = caps;
        this.mMainCmdBuffer = new GLCommandBuffer(this);
        this.mResourceProvider = new GLResourceProvider(this, context);
        this.mPipelineStateCache = new GLPipelineStateCache(this, 256);
        this.mCpuBufferPool = new CpuBufferPool(6);
        this.mVertexPool = GpuBufferPool.makeVertexPool(this.mResourceProvider);
        this.mInstancePool = GpuBufferPool.makeInstancePool(this.mResourceProvider);
        this.mIndexPool = GpuBufferPool.makeIndexPool(this.mResourceProvider);
        int maxTextureUnits = caps.shaderCaps().mMaxFragmentSamplers;
        this.mHWTextureStates = new GpuResource.UniqueID[maxTextureUnits];
        this.mHWSamplerStates = new GLDevice.HWSamplerState[maxTextureUnits];
        for (int i = 0; i < maxTextureUnits; i++) {
            this.mHWSamplerStates[i] = new GLDevice.HWSamplerState();
        }
    }

    @Nullable
    public static GLDevice make(DirectContext context, ContextOptions options) {
        GLCapabilities capabilities;
        try {
            capabilities = (GLCapabilities) Objects.requireNonNullElseGet(GL.getCapabilities(), GL::createCapabilities);
        } catch (Exception var7) {
            try {
                capabilities = GL.createCapabilities();
            } catch (Exception var6) {
                var6.printStackTrace(context.getErrorWriter());
                return null;
            }
        }
        try {
            GLCaps caps = new GLCaps(options, capabilities);
            return new GLDevice(context, caps);
        } catch (Exception var5) {
            var5.printStackTrace(context.getErrorWriter());
            return null;
        }
    }

    public GLCaps getCaps() {
        return this.mCaps;
    }

    @Override
    public void disconnect(boolean cleanup) {
        super.disconnect(cleanup);
        this.mVertexPool.reset();
        this.mInstancePool.reset();
        this.mIndexPool.reset();
        this.mCpuBufferPool.releaseAll();
        this.mMainCmdBuffer.resetStates(-1);
        if (cleanup) {
            this.mPipelineStateCache.release();
            this.mResourceProvider.release();
        } else {
            this.mPipelineStateCache.discard();
            this.mResourceProvider.discard();
        }
        this.callAllFinishedCallbacks(cleanup);
    }

    @Override
    protected void handleDirtyContext(int state) {
        super.handleDirtyContext(state);
    }

    public GLCommandBuffer currentCommandBuffer() {
        return this.mMainCmdBuffer;
    }

    public GLResourceProvider getResourceProvider() {
        return this.mResourceProvider;
    }

    public GLPipelineStateCache getPipelineStateCache() {
        return this.mPipelineStateCache;
    }

    public CpuBufferPool getCpuBufferPool() {
        return this.mCpuBufferPool;
    }

    @Override
    public GpuBufferPool getVertexPool() {
        return this.mVertexPool;
    }

    @Override
    public GpuBufferPool getInstancePool() {
        return this.mInstancePool;
    }

    @Override
    public GpuBufferPool getIndexPool() {
        return this.mIndexPool;
    }

    @Override
    protected void onResetContext(int resetBits) {
        this.currentCommandBuffer().resetStates(resetBits);
        if ((resetBits & 2) != 0) {
            GLCore.glPixelStorei(3314, 0);
            GLCore.glPixelStorei(3330, 0);
        }
        if ((resetBits & 4) != 0) {
            this.mHWBufferStates[0].mBoundBufferUniqueID = INVALID_UNIQUE_ID;
            this.mHWBufferStates[1].mBoundBufferUniqueID = INVALID_UNIQUE_ID;
        }
        if ((resetBits & 8) != 0) {
            Arrays.fill(this.mHWTextureStates, INVALID_UNIQUE_ID);
            for (GLDevice.HWSamplerState ss : this.mHWSamplerStates) {
                ss.mSamplerState = 0;
                ss.mBoundSampler = RefCnt.move(ss.mBoundSampler);
            }
        }
        this.mHWActiveTextureUnit = -1;
        if ((resetBits & 32) != 0) {
            GLCore.glDisable(2848);
            GLCore.glDisable(2881);
            GLCore.glDisable(3024);
            GLCore.glEnable(32925);
        }
        if ((resetBits & 64) != 0) {
            GLCore.glDisable(3058);
        }
        if ((resetBits & 256) != 0) {
            GLCore.glDisable(2929);
            GLCore.glDepthMask(false);
            GLCore.glDisable(32823);
            GLCore.glDisable(2884);
            GLCore.glFrontFace(2305);
            GLCore.glLineWidth(1.0F);
            GLCore.glPointSize(1.0F);
            GLCore.glDisable(34370);
        }
    }

    public void forceResetContext(int state) {
        this.markContextDirty(state);
        this.handleDirtyContext(state);
    }

    @Nullable
    @Override
    protected GpuTexture onCreateTexture(int width, int height, BackendFormat format, int mipLevelCount, int sampleCount, int surfaceFlags) {
        assert mipLevelCount > 0 && sampleCount > 0;
        if ((surfaceFlags & 16) != 0) {
            return null;
        } else if (format.isExternal()) {
            return null;
        } else {
            this.handleDirtyContext(8);
            int glFormat = format.getGLFormat();
            int texture = this.createTexture(width, height, glFormat, mipLevelCount);
            if (texture == 0) {
                return null;
            } else {
                Function<GLTexture, GLRenderTarget> target = null;
                if ((surfaceFlags & 8) != 0) {
                    target = this.createRTObjects(texture, width, height, glFormat, sampleCount);
                    if (target == null) {
                        GLCore.glDeleteTextures(texture);
                        return null;
                    }
                }
                GLTextureInfo info = new GLTextureInfo();
                info.handle = texture;
                info.format = format.getGLFormat();
                info.levels = mipLevelCount;
                return (GpuTexture) (target == null ? new GLTexture(this, width, height, info, format, (surfaceFlags & 1) != 0, true) : new GLRenderTexture(this, width, height, info, format, (surfaceFlags & 1) != 0, target));
            }
        }
    }

    @Nullable
    @Override
    protected GpuTexture onWrapRenderableBackendTexture(BackendTexture texture, int sampleCount, boolean ownership) {
        if (texture.isProtected()) {
            return null;
        } else {
            GLTextureInfo info = new GLTextureInfo();
            if (texture.getGLTextureInfo(info) && info.handle != 0 && info.format != 0) {
                int format = info.format;
                if (!GLCore.glFormatIsSupported(format)) {
                    return null;
                } else {
                    this.handleDirtyContext(8);
                    assert this.mCaps.isFormatRenderable(format, sampleCount);
                    assert this.mCaps.isFormatTexturable(format);
                    sampleCount = this.mCaps.getRenderTargetSampleCount(sampleCount, format);
                    assert sampleCount > 0;
                    Function<GLTexture, GLRenderTarget> objects = this.createRTObjects(info.handle, texture.getWidth(), texture.getHeight(), format, sampleCount);
                    if (objects != null) {
                    }
                    return null;
                }
            } else {
                return null;
            }
        }
    }

    @Nullable
    @Override
    public GpuRenderTarget onWrapBackendRenderTarget(BackendRenderTarget backendRenderTarget) {
        GLFramebufferInfo info = new GLFramebufferInfo();
        if (!backendRenderTarget.getGLFramebufferInfo(info)) {
            return null;
        } else if (backendRenderTarget.isProtected()) {
            return null;
        } else if (!this.mCaps.isFormatRenderable(info.mFormat, backendRenderTarget.getSampleCount())) {
            return null;
        } else {
            int actualSamplerCount = this.mCaps.getRenderTargetSampleCount(backendRenderTarget.getSampleCount(), info.mFormat);
            return GLRenderTarget.makeWrapped(this, backendRenderTarget.getWidth(), backendRenderTarget.getHeight(), info.mFormat, actualSamplerCount, info.mFramebuffer, backendRenderTarget.getStencilBits(), false);
        }
    }

    @Override
    protected boolean onWritePixels(GpuTexture texture, int x, int y, int width, int height, int dstColorType, int srcColorType, int rowBytes, long pixels) {
        assert !texture.isExternal();
        assert !texture.getBackendFormat().isCompressed();
        GLTexture glTexture = (GLTexture) texture;
        int glFormat = glTexture.getGLFormat();
        assert this.mCaps.isFormatTexturable(glFormat);
        int srcFormat = this.mCaps.getPixelsExternalFormat(glFormat, dstColorType, srcColorType, true);
        if (srcFormat == 0) {
            return false;
        } else {
            int srcType = this.mCaps.getPixelsExternalType(glFormat, dstColorType, srcColorType);
            if (srcType == 0) {
                return false;
            } else {
                this.handleDirtyContext(10);
                boolean dsa = this.mCaps.hasDSASupport();
                int texName = glTexture.getHandle();
                int boundTexture = 0;
                if (!dsa) {
                    boundTexture = GLCore.glGetInteger(32873);
                    if (texName != boundTexture) {
                        GLCore.glBindTexture(3553, texName);
                    }
                }
                GLTextureParameters parameters = glTexture.getParameters();
                if (parameters.baseMipmapLevel != 0) {
                    if (dsa) {
                        GLCore.glTextureParameteri(texName, 33084, 0);
                    } else {
                        GLCore.glTexParameteri(3553, 33084, 0);
                    }
                    parameters.baseMipmapLevel = 0;
                }
                int maxLevel = glTexture.getMaxMipmapLevel();
                if (parameters.maxMipmapLevel != maxLevel) {
                    if (dsa) {
                        GLCore.glTextureParameteri(texName, 33085, maxLevel);
                    } else {
                        GLCore.glTexParameteri(3553, 33085, maxLevel);
                    }
                    parameters.maxMipmapLevel = maxLevel;
                }
                assert x >= 0 && y >= 0 && width > 0 && height > 0;
                assert pixels != 0L;
                int bpp = ImageInfo.bytesPerPixel(srcColorType);
                int trimRowBytes = width * bpp;
                if (rowBytes != trimRowBytes) {
                    int rowLength = rowBytes / bpp;
                    GLCore.glPixelStorei(3314, rowLength);
                } else {
                    GLCore.glPixelStorei(3314, 0);
                }
                GLCore.glPixelStorei(3315, 0);
                GLCore.glPixelStorei(3316, 0);
                GLCore.glPixelStorei(3317, 1);
                if (dsa) {
                    GLCore.glTextureSubImage2D(texName, 0, x, y, width, height, srcFormat, srcType, pixels);
                } else {
                    GLCore.glTexSubImage2D(3553, 0, x, y, width, height, srcFormat, srcType, pixels);
                }
                if (!dsa && texName != boundTexture) {
                    GLCore.glBindTexture(3553, boundTexture);
                }
                return true;
            }
        }
    }

    @Override
    protected boolean onGenerateMipmaps(GpuTexture texture) {
        GLTexture glTexture = (GLTexture) texture;
        if (this.mCaps.hasDSASupport()) {
            GLCore.glGenerateTextureMipmap(glTexture.getHandle());
        } else {
            int texName = glTexture.getHandle();
            int boundTexture = GLCore.glGetInteger(32873);
            if (texName != boundTexture) {
                GLCore.glBindTexture(3553, texName);
            }
            GLCore.glGenerateMipmap(3553);
            if (texName != boundTexture) {
                GLCore.glBindTexture(3553, boundTexture);
            }
        }
        return true;
    }

    @Override
    protected boolean onCopySurface(IGpuSurface src, int srcL, int srcT, int srcR, int srcB, IGpuSurface dst, int dstL, int dstT, int dstR, int dstB, int filter) {
        int srcWidth = srcR - srcL;
        int srcHeight = srcB - srcT;
        int dstWidth = dstR - dstL;
        int dstHeight = dstB - dstT;
        if (srcWidth == dstWidth && srcHeight == dstHeight) {
            if (this.mCaps.hasCopyImageSupport() && src.asTexture() instanceof GLTexture srcTex && dst.asTexture() instanceof GLTexture dstTex && this.mCaps.canCopyImage(srcTex.getGLFormat(), 1, dstTex.getGLFormat(), 1)) {
                GLCore.glCopyImageSubData(srcTex.getHandle(), 3553, 0, srcL, srcT, 0, dstTex.getHandle(), 3553, 0, dstL, dstT, 0, srcWidth, srcHeight, 1);
                return true;
            }
            if (src.asTexture() instanceof GLTexture srcTex && dst.asTexture() instanceof GLTexture dstTex && this.mCaps.canCopyTexSubImage(srcTex.getGLFormat(), dstTex.getGLFormat())) {
                int dstTexName = dstTex.getHandle();
                int boundTexture = GLCore.glGetInteger(32873);
                if (dstTexName != boundTexture) {
                    GLCore.glBindTexture(3553, dstTexName);
                }
                int[] framebuffer = this.mCopySrcFramebuffer;
                if (framebuffer[0] == 0) {
                    GLCore.glGenFramebuffers(framebuffer);
                }
                int boundFramebuffer = GLCore.glGetInteger(36010);
                GLCore.glBindFramebuffer(36008, framebuffer[0]);
                GLCore.glFramebufferTexture(36008, 36064, srcTex.getHandle(), 0);
                GLCore.glCopyTexSubImage2D(3553, 0, dstL, dstT, srcL, srcT, srcWidth, srcHeight);
                GLCore.glFramebufferTexture(36008, 36064, 0, 0);
                GLCore.glBindFramebuffer(36008, boundFramebuffer);
                if (dstTexName != boundTexture) {
                    GLCore.glBindTexture(3553, boundTexture);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    protected OpsRenderPass onGetOpsRenderPass(SurfaceView writeView, Rect2i contentBounds, byte colorOps, byte stencilOps, float[] clearColor, Set<TextureProxy> sampledTextures, int pipelineFlags) {
        this.mStats.incRenderPasses();
        if (this.mCachedOpsRenderPass == null) {
            this.mCachedOpsRenderPass = new GLOpsRenderPass(this);
        }
        return this.mCachedOpsRenderPass.set(writeView.getSurface().getGpuRenderTarget(), contentBounds, writeView.getOrigin(), colorOps, stencilOps, clearColor);
    }

    public GLCommandBuffer beginRenderPass(GLRenderTarget fs, byte colorOps, byte stencilOps, float[] clearColor) {
        this.handleDirtyContext(1);
        GLCommandBuffer cmdBuffer = this.currentCommandBuffer();
        boolean colorLoadClear = Engine.LoadStoreOps.loadOp(colorOps) == 1;
        boolean stencilLoadClear = Engine.LoadStoreOps.loadOp(stencilOps) == 1;
        if (colorLoadClear || stencilLoadClear) {
            int framebuffer = fs.getSampleFramebuffer();
            cmdBuffer.flushScissorTest(false);
            if (colorLoadClear) {
                cmdBuffer.flushColorWrite(true);
                GLCore.glClearNamedFramebufferfv(framebuffer, 6144, 0, clearColor);
            }
            if (stencilLoadClear) {
                GLCore.glStencilMask(-1);
                GLCore.glClearNamedFramebufferfi(framebuffer, 34041, 0, 1.0F, 0);
            }
        }
        cmdBuffer.flushRenderTarget(fs);
        return cmdBuffer;
    }

    public void endRenderPass(GLRenderTarget fs, byte colorOps, byte stencilOps) {
        this.handleDirtyContext(1);
        boolean colorStoreDiscard = Engine.LoadStoreOps.storeOp(colorOps) == 1;
        boolean stencilStoreDiscard = Engine.LoadStoreOps.storeOp(stencilOps) == 1;
        if (colorStoreDiscard || stencilStoreDiscard) {
            int framebuffer = fs.getSampleFramebuffer();
            MemoryStack stack = MemoryStack.stackPush();
            try {
                long pAttachments = stack.nmalloc(4, 8);
                int numAttachments = 0;
                if (colorStoreDiscard) {
                    int attachment = fs.getSampleFramebuffer() == 0 ? 6144 : '賠';
                    MemoryUtil.memPutInt(pAttachments, attachment);
                    numAttachments++;
                }
                if (stencilStoreDiscard) {
                    int attachment = fs.getSampleFramebuffer() == 0 ? 6146 : '贠';
                    MemoryUtil.memPutInt(pAttachments + (long) (numAttachments << 2), attachment);
                    numAttachments++;
                }
                GLCore.nglInvalidateNamedFramebufferData(framebuffer, numAttachments, pAttachments);
            } catch (Throwable var13) {
                if (stack != null) {
                    try {
                        stack.close();
                    } catch (Throwable var12) {
                        var13.addSuppressed(var12);
                    }
                }
                throw var13;
            }
            if (stack != null) {
                stack.close();
            }
        }
    }

    @Nullable
    @Override
    protected GpuBuffer onCreateBuffer(int size, int flags) {
        this.handleDirtyContext(4);
        return GLBuffer.make(this, size, flags);
    }

    @Override
    protected void onResolveRenderTarget(GpuRenderTarget renderTarget, int resolveLeft, int resolveTop, int resolveRight, int resolveBottom) {
        GLRenderTarget glRenderTarget = (GLRenderTarget) renderTarget;
        int framebuffer = glRenderTarget.getSampleFramebuffer();
        int resolveFramebuffer = glRenderTarget.getResolveFramebuffer();
        assert framebuffer != 0 && framebuffer != resolveFramebuffer;
        this.currentCommandBuffer().flushScissorTest(false);
        GLCore.glBlitNamedFramebuffer(framebuffer, resolveFramebuffer, resolveLeft, resolveTop, resolveRight, resolveBottom, resolveLeft, resolveTop, resolveRight, resolveBottom, 16384, 9728);
    }

    private void flush(boolean forceFlush) {
        if (this.mNeedsFlush || forceFlush) {
            GLCore.glFlush();
            this.mNeedsFlush = false;
        }
    }

    @Override
    public long insertFence() {
        long fence = GLCore.glFenceSync(37143, 0);
        this.mNeedsFlush = true;
        return fence;
    }

    @Override
    public boolean checkFence(long fence) {
        int result = GLCore.glClientWaitSync(fence, 0, 0L);
        return result == 37148 || result == 37146;
    }

    @Override
    public void deleteFence(long fence) {
        GLCore.glDeleteSync(fence);
    }

    @Override
    public void addFinishedCallback(FlushInfo.FinishedCallback callback) {
        this.mFinishedCallbacks.addLast(callback);
        this.mFinishedFences.enqueue(this.insertFence());
        assert this.mFinishedCallbacks.size() == this.mFinishedFences.size();
    }

    @Override
    public void checkFinishedCallbacks() {
        while (!this.mFinishedCallbacks.isEmpty() && this.checkFence(this.mFinishedFences.firstLong())) {
            this.deleteFence(this.mFinishedFences.dequeueLong());
            ((FlushInfo.FinishedCallback) this.mFinishedCallbacks.removeFirst()).onFinished();
        }
        assert this.mFinishedCallbacks.size() == this.mFinishedFences.size();
    }

    private void callAllFinishedCallbacks(boolean cleanup) {
        while (!this.mFinishedCallbacks.isEmpty()) {
            if (cleanup) {
                this.deleteFence(this.mFinishedFences.dequeueLong());
            }
            ((FlushInfo.FinishedCallback) this.mFinishedCallbacks.removeFirst()).onFinished();
        }
        if (!cleanup) {
            this.mFinishedFences.clear();
        } else {
            assert this.mFinishedFences.isEmpty();
        }
    }

    @Override
    public void waitForQueue() {
        GLCore.glFinish();
    }

    public int bindBuffer(@Nonnull GLBuffer buffer) {
        assert !this.getCaps().hasDSASupport();
        this.handleDirtyContext(4);
        int type = bufferUsageToType(buffer.getUsage());
        if (type == 1) {
            this.currentCommandBuffer().bindVertexArray(null);
        }
        GLDevice.HWBufferState bufferState = this.mHWBufferStates[type];
        if (bufferState.mBoundBufferUniqueID != buffer.getUniqueID()) {
            GLCore.glBindBuffer(bufferState.mTarget, buffer.getHandle());
            bufferState.mBoundBufferUniqueID = buffer.getUniqueID();
        }
        return bufferState.mTarget;
    }

    public void bindIndexBufferInPipe(@Nonnull GLBuffer buffer) {
        assert !this.getCaps().hasDSASupport() || this.getCaps().dsaElementBufferBroken();
        assert bufferUsageToType(buffer.getUsage()) == 1;
        GLDevice.HWBufferState bufferState = this.mHWBufferStates[1];
        assert bufferState.mTarget == 34963;
        GLCore.glBindBuffer(34963, buffer.getHandle());
        bufferState.mBoundBufferUniqueID = buffer.getUniqueID();
    }

    public int bindBufferForSetup(int usage, int buffer) {
        assert !this.getCaps().hasDSASupport();
        this.handleDirtyContext(4);
        int type = bufferUsageToType(usage);
        if (type == 1) {
            this.currentCommandBuffer().bindVertexArray(null);
        }
        GLDevice.HWBufferState bufferState = this.mHWBufferStates[type];
        GLCore.glBindBuffer(bufferState.mTarget, buffer);
        bufferState.mBoundBufferUniqueID = INVALID_UNIQUE_ID;
        return bufferState.mTarget;
    }

    public void bindTexture(int bindingUnit, GLTexture texture, int samplerState, short readSwizzle) {
        boolean dsa = this.mCaps.hasDSASupport();
        if (this.mHWTextureStates[bindingUnit] != texture.getUniqueID()) {
            if (dsa) {
                GLCore.glBindTextureUnit(bindingUnit, texture.getHandle());
            } else {
                this.setTextureUnit(bindingUnit);
                GLCore.glBindTexture(3553, texture.getHandle());
            }
            this.mHWTextureStates[bindingUnit] = texture.getUniqueID();
        }
        GLDevice.HWSamplerState state = this.mHWSamplerStates[bindingUnit];
        if (state.mSamplerState != samplerState) {
            GLSampler sampler = samplerState != 0 ? this.mResourceProvider.findOrCreateCompatibleSampler(samplerState) : null;
            GLCore.glBindSampler(bindingUnit, sampler != null ? sampler.getHandle() : 0);
            state.mBoundSampler = RefCnt.move(state.mBoundSampler, sampler);
        }
        GLTextureParameters parameters = texture.getParameters();
        if (parameters.baseMipmapLevel != 0) {
            if (dsa) {
                GLCore.glTextureParameteri(texture.getHandle(), 33084, 0);
            } else {
                GLCore.glTexParameteri(3553, 33084, 0);
            }
            parameters.baseMipmapLevel = 0;
        }
        int maxLevel = texture.getMaxMipmapLevel();
        if (parameters.maxMipmapLevel != maxLevel) {
            if (dsa) {
                GLCore.glTextureParameteri(texture.getHandle(), 33085, maxLevel);
            } else {
                GLCore.glTexParameteri(3553, 33085, maxLevel);
            }
            parameters.maxMipmapLevel = maxLevel;
        }
        for (int i = 0; i < 4; i++) {
            int swiz = switch(readSwizzle & 15) {
                case 0 ->
                    6403;
                case 1 ->
                    6404;
                case 2 ->
                    6405;
                case 3 ->
                    6406;
                case 4 ->
                    0;
                case 5 ->
                    1;
                default ->
                    throw new AssertionError(readSwizzle);
            };
            if (parameters.getSwizzle(i) != swiz) {
                parameters.setSwizzle(i, swiz);
                int channel = 36418 + i;
                if (dsa) {
                    GLCore.glTextureParameteri(texture.getHandle(), channel, swiz);
                } else {
                    GLCore.glTexParameteri(3553, channel, swiz);
                }
            }
            readSwizzle = (short) (readSwizzle >> 4);
        }
    }

    public void setTextureUnit(int unit) {
        assert unit >= 0 && unit < this.mHWTextureStates.length;
        if (unit != this.mHWActiveTextureUnit) {
            GLCore.glActiveTexture(33984 + unit);
            this.mHWActiveTextureUnit = unit;
        }
    }

    private int createTexture(int width, int height, int format, int levels) {
        assert GLCore.glFormatIsSupported(format);
        assert !GLCore.glFormatIsCompressed(format);
        int internalFormat = this.mCaps.getTextureInternalFormat(format);
        if (internalFormat == 0) {
            return 0;
        } else {
            assert this.mCaps.isFormatTexturable(format);
            int texture;
            if (this.mCaps.hasDSASupport()) {
                assert this.mCaps.isTextureStorageCompatible(format);
                texture = GLCore.glCreateTextures(3553);
                if (texture == 0) {
                    return 0;
                }
                if (this.mCaps.skipErrorChecks()) {
                    GLCore.glTextureStorage2D(texture, levels, internalFormat, width, height);
                } else {
                    GLCore.glClearErrors();
                    GLCore.glTextureStorage2D(texture, levels, internalFormat, width, height);
                    if (GLCore.glGetError() != 0) {
                        GLCore.glDeleteTextures(texture);
                        return 0;
                    }
                }
            } else {
                texture = GLCore.glGenTextures();
                if (texture == 0) {
                    return 0;
                }
                int boundTexture = GLCore.glGetInteger(32873);
                GLCore.glBindTexture(3553, texture);
                byte var19;
                try {
                    if (this.mCaps.isTextureStorageCompatible(format)) {
                        if (this.mCaps.skipErrorChecks()) {
                            GLCore.glTexStorage2D(3553, levels, internalFormat, width, height);
                            return texture;
                        }
                        GLCore.glClearErrors();
                        GLCore.glTexStorage2D(3553, levels, internalFormat, width, height);
                        if (GLCore.glGetError() == 0) {
                            return texture;
                        }
                        GLCore.glDeleteTextures(texture);
                        return 0;
                    }
                    int externalFormat = this.mCaps.getFormatDefaultExternalFormat(format);
                    int externalType = this.mCaps.getFormatDefaultExternalType(format);
                    boolean checks = !this.mCaps.skipErrorChecks();
                    int error = 0;
                    if (checks) {
                        GLCore.glClearErrors();
                    }
                    for (int level = 0; level < levels; level++) {
                        int currentWidth = Math.max(1, width >> level);
                        int currentHeight = Math.max(1, height >> level);
                        GLCore.nglTexImage2D(3553, level, internalFormat, currentWidth, currentHeight, 0, externalFormat, externalType, 0L);
                        if (checks) {
                            error |= GLCore.glGetError();
                        }
                    }
                    if (error == 0) {
                        return texture;
                    }
                    GLCore.glDeleteTextures(texture);
                    var19 = 0;
                } finally {
                    GLCore.glBindTexture(3553, boundTexture);
                }
                return var19;
            }
            return texture;
        }
    }

    @Nullable
    private Function<GLTexture, GLRenderTarget> createRTObjects(int texture, int width, int height, int format, int samples) {
        assert texture != 0;
        assert GLCore.glFormatIsSupported(format);
        assert !GLCore.glFormatIsCompressed(format);
        int framebuffer = GLCore.glGenFramebuffers();
        if (framebuffer == 0) {
            return null;
        } else {
            int msaaFramebuffer;
            GLAttachment msaaColorBuffer;
            if (samples <= 1) {
                msaaFramebuffer = 0;
                msaaColorBuffer = null;
            } else {
                msaaFramebuffer = GLCore.glGenFramebuffers();
                if (msaaFramebuffer == 0) {
                    GLCore.glDeleteFramebuffers(framebuffer);
                    return null;
                }
                msaaColorBuffer = GLAttachment.makeColor(this, width, height, samples, format);
                if (msaaColorBuffer == null) {
                    GLCore.glDeleteFramebuffers(framebuffer);
                    GLCore.glDeleteFramebuffers(msaaFramebuffer);
                    return null;
                }
                this.currentCommandBuffer().bindFramebuffer(msaaFramebuffer);
                GLCore.glFramebufferRenderbuffer(36160, 36064, 36161, msaaColorBuffer.getRenderbufferID());
                if (!this.mCaps.skipErrorChecks()) {
                    int status = GLCore.glCheckFramebufferStatus(36160);
                    if (status != 36053) {
                        GLCore.glDeleteFramebuffers(framebuffer);
                        GLCore.glDeleteFramebuffers(msaaFramebuffer);
                        msaaColorBuffer.unref();
                        return null;
                    }
                }
            }
            this.currentCommandBuffer().bindFramebuffer(framebuffer);
            GLCore.glFramebufferTexture(36160, 36064, texture, 0);
            if (!this.mCaps.skipErrorChecks()) {
                int status = GLCore.glCheckFramebufferStatus(36160);
                if (status != 36053) {
                    GLCore.glDeleteFramebuffers(framebuffer);
                    GLCore.glDeleteFramebuffers(msaaFramebuffer);
                    if (msaaColorBuffer != null) {
                        msaaColorBuffer.unref();
                    }
                    return null;
                }
            }
            return colorBuffer -> new GLRenderTarget(this, colorBuffer.getWidth(), colorBuffer.getHeight(), colorBuffer.getGLFormat(), samples, framebuffer, msaaFramebuffer, colorBuffer, msaaColorBuffer);
        }
    }

    static {
        assert 0 == bufferUsageToType(1);
        assert 1 == bufferUsageToType(2);
        assert 2 == bufferUsageToType(4);
        assert 3 == bufferUsageToType(8);
        assert 4 == bufferUsageToType(16);
        assert 5 == bufferUsageToType(32);
    }

    static final class HWBufferState {

        final int mTarget;

        GpuResource.UniqueID mBoundBufferUniqueID;

        HWBufferState(int target) {
            this.mTarget = target;
        }
    }

    static final class HWSamplerState {

        int mSamplerState = 0;

        @SharedPtr
        GLSampler mBoundSampler = null;
    }
}