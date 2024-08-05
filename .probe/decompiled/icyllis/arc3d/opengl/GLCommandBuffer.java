package icyllis.arc3d.opengl;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.CommandBuffer;
import icyllis.arc3d.engine.SamplerState;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public final class GLCommandBuffer extends CommandBuffer {

    private static final int TriState_Disabled = 0;

    private static final int TriState_Enabled = 1;

    private static final int TriState_Unknown = 2;

    private final GLDevice mDevice;

    private int mHWViewportWidth;

    private int mHWViewportHeight;

    private int mHWScissorTest;

    private int mHWColorWrite;

    private int mHWScissorX;

    private int mHWScissorY;

    private int mHWScissorWidth;

    private int mHWScissorHeight;

    private int mHWFramebuffer;

    @SharedPtr
    private GLRenderTarget mHWRenderTarget;

    @SharedPtr
    private GLProgram mHWProgram;

    @SharedPtr
    private GLVertexArray mHWVertexArray;

    private boolean mHWVertexArrayInvalid;

    @SharedPtr
    private final GLUniformBuffer[] mBoundUniformBuffers;

    private long mSubmitFence;

    GLCommandBuffer(GLDevice device) {
        this.mDevice = device;
        this.mBoundUniformBuffers = new GLUniformBuffer[4];
    }

    void submit() {
        this.mSubmitFence = GLCore.glFenceSync(37143, 0);
    }

    void checkFinishedAndReset() {
        if (this.mSubmitFence != 0L) {
            int status = GLCore.glClientWaitSync(this.mSubmitFence, 0, 0L);
            if (status == 37148 || status == 37146) {
                GLCore.glDeleteSync(this.mSubmitFence);
            }
        }
    }

    void resetStates(int states) {
        if ((states & 1) != 0) {
            this.mHWFramebuffer = -1;
            this.mHWRenderTarget = RefCnt.move(this.mHWRenderTarget);
        }
        if ((states & 4) != 0) {
            this.mHWProgram = RefCnt.move(this.mHWProgram);
            this.mHWVertexArray = RefCnt.move(this.mHWVertexArray);
            this.mHWVertexArrayInvalid = true;
            for (int i = 0; i < this.mBoundUniformBuffers.length; i++) {
                this.mBoundUniformBuffers[i] = RefCnt.move(this.mBoundUniformBuffers[i]);
            }
        }
        if ((states & 128) != 0) {
            this.mHWScissorTest = 2;
            this.mHWScissorX = -1;
            this.mHWScissorY = -1;
            this.mHWScissorWidth = -1;
            this.mHWScissorHeight = -1;
            this.mHWViewportWidth = -1;
            this.mHWViewportHeight = -1;
        }
        if ((states & 256) != 0) {
            this.mHWColorWrite = 2;
        }
    }

    public void flushViewport(int width, int height) {
        assert width >= 0 && height >= 0;
        if (width != this.mHWViewportWidth || height != this.mHWViewportHeight) {
            GLCore.glViewportIndexedf(0, 0.0F, 0.0F, (float) width, (float) height);
            GLCore.glDepthRangeIndexed(0, 0.0, 1.0);
            this.mHWViewportWidth = width;
            this.mHWViewportHeight = height;
        }
    }

    public void flushScissorRect(int width, int height, int origin, int scissorLeft, int scissorTop, int scissorRight, int scissorBottom) {
        assert width >= 0 && height >= 0;
        int scissorWidth = scissorRight - scissorLeft;
        int scissorHeight = scissorBottom - scissorTop;
        assert scissorLeft >= 0 && scissorTop >= 0 && scissorWidth >= 0 && scissorWidth <= width && scissorHeight >= 0 && scissorHeight <= height;
        int scissorY;
        if (origin == 0) {
            scissorY = scissorTop;
        } else {
            assert origin == 1;
            scissorY = height - scissorBottom;
        }
        assert scissorY >= 0;
        if (scissorLeft != this.mHWScissorX || scissorY != this.mHWScissorY || scissorWidth != this.mHWScissorWidth || scissorHeight != this.mHWScissorHeight) {
            GLCore.glScissorIndexed(0, scissorLeft, scissorY, scissorWidth, scissorHeight);
            this.mHWScissorX = scissorLeft;
            this.mHWScissorY = scissorY;
            this.mHWScissorWidth = scissorWidth;
            this.mHWScissorHeight = scissorHeight;
        }
    }

    public void flushScissorTest(boolean enable) {
        if (enable) {
            if (this.mHWScissorTest != 1) {
                GLCore.glEnablei(3089, 0);
                this.mHWScissorTest = 1;
            }
        } else if (this.mHWScissorTest != 0) {
            GLCore.glDisablei(3089, 0);
            this.mHWScissorTest = 0;
        }
    }

    public void flushColorWrite(boolean enable) {
        if (enable) {
            if (this.mHWColorWrite != 1) {
                GLCore.glColorMaski(0, true, true, true, true);
                this.mHWColorWrite = 1;
            }
        } else if (this.mHWColorWrite != 0) {
            GLCore.glColorMaski(0, false, false, false, false);
            this.mHWColorWrite = 0;
        }
    }

    public void bindFramebuffer(int framebuffer) {
        GLCore.glBindFramebuffer(36160, framebuffer);
        this.mHWRenderTarget = null;
    }

    public void flushRenderTarget(GLRenderTarget target) {
        if (target == null) {
            this.mHWRenderTarget = RefCnt.move(this.mHWRenderTarget);
        } else {
            int framebuffer = target.getSampleFramebuffer();
            if (this.mHWFramebuffer != framebuffer || this.mHWRenderTarget != target) {
                GLCore.glBindFramebuffer(36160, framebuffer);
                this.mHWFramebuffer = framebuffer;
                this.mHWRenderTarget = RefCnt.create(this.mHWRenderTarget, target);
                this.flushViewport(target.getWidth(), target.getHeight());
            }
            target.bindStencil();
        }
    }

    public void bindPipeline(@Nonnull GLProgram program, @Nonnull GLVertexArray vertexArray) {
        if (this.mHWProgram != program) {
            GLCore.glUseProgram(program.getProgram());
            this.bindVertexArray(vertexArray);
            this.mHWProgram = RefCnt.create(this.mHWProgram, program);
        }
    }

    public void bindVertexArray(@Nullable GLVertexArray vertexArray) {
        if (this.mHWVertexArrayInvalid || this.mHWVertexArray != vertexArray) {
            GLCore.glBindVertexArray(vertexArray == null ? 0 : vertexArray.getHandle());
            this.mHWVertexArray = RefCnt.create(this.mHWVertexArray, vertexArray);
            this.mHWVertexArrayInvalid = false;
        }
    }

    public void bindUniformBuffer(@Nonnull GLUniformBuffer uniformBuffer) {
        int index = uniformBuffer.getBinding();
        if (this.mBoundUniformBuffers[index] != uniformBuffer) {
            GLCore.glBindBufferBase(35345, index, uniformBuffer.getHandle());
            this.mBoundUniformBuffers[index] = RefCnt.create(this.mBoundUniformBuffers[index], uniformBuffer);
        }
    }

    public void bindTexture(int binding, GLTexture texture, int samplerState, short readSwizzle) {
        assert texture != null;
        if (SamplerState.isMipmapped(samplerState)) {
            if (!texture.isMipmapped()) {
                assert !SamplerState.isAnisotropy(samplerState);
                samplerState = SamplerState.resetMipmapMode(samplerState);
            } else {
                assert !texture.isMipmapsDirty();
            }
        }
        this.mDevice.bindTexture(binding, texture, samplerState, readSwizzle);
    }
}