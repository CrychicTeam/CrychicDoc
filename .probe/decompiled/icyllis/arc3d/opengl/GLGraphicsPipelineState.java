package icyllis.arc3d.opengl;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GeometryProcessor;
import icyllis.arc3d.engine.GpuBuffer;
import icyllis.arc3d.engine.GraphicsPipelineState;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.PipelineStateCache;
import icyllis.arc3d.engine.TextureProxy;
import icyllis.arc3d.engine.shading.UniformHandler;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class GLGraphicsPipelineState extends GraphicsPipelineState {

    @SharedPtr
    private GLProgram mProgram;

    @SharedPtr
    private GLVertexArray mVertexArray;

    private GLUniformDataManager mDataManager;

    private GeometryProcessor.ProgramImpl mGPImpl;

    private int mNumTextureSamplers;

    private CompletableFuture<GLPipelineStateBuilder> mAsyncWork;

    GLGraphicsPipelineState(GLDevice device, CompletableFuture<GLPipelineStateBuilder> asyncWork) {
        super(device);
        this.mAsyncWork = asyncWork;
    }

    void init(@SharedPtr GLProgram program, @SharedPtr GLVertexArray vertexArray, List<UniformHandler.UniformInfo> uniforms, int uniformSize, List<UniformHandler.UniformInfo> samplers, GeometryProcessor.ProgramImpl gpImpl) {
        this.mProgram = program;
        this.mVertexArray = vertexArray;
        this.mGPImpl = gpImpl;
        this.mDataManager = new GLUniformDataManager(uniforms, uniformSize);
        this.mNumTextureSamplers = samplers.size();
    }

    public void discard() {
        if (this.mAsyncWork != null) {
            this.mAsyncWork.cancel(true);
            this.mAsyncWork = null;
        }
        if (this.mProgram != null) {
            this.mProgram.discard();
            if (this.mVertexArray.unique()) {
                this.mVertexArray.discard();
            }
        }
    }

    public void release() {
        this.mProgram = RefCnt.move(this.mProgram);
        this.mVertexArray = RefCnt.move(this.mVertexArray);
        this.mDataManager = RefCnt.move(this.mDataManager);
    }

    private void checkAsyncWork() {
        boolean success = ((GLPipelineStateBuilder) this.mAsyncWork.join()).finish(this);
        PipelineStateCache.Stats stats = this.getDevice().getPipelineStateCache().getStats();
        if (success) {
            stats.incNumCompilationSuccesses();
        } else {
            stats.incNumCompilationFailures();
        }
        this.mAsyncWork = null;
    }

    public boolean bindPipeline(GLCommandBuffer commandBuffer) {
        if (this.mAsyncWork != null) {
            this.checkAsyncWork();
        }
        if (this.mProgram != null) {
            assert this.mVertexArray != null;
            commandBuffer.bindPipeline(this.mProgram, this.mVertexArray);
            return true;
        } else {
            return false;
        }
    }

    public boolean bindUniforms(GLCommandBuffer commandBuffer, PipelineInfo pipelineInfo, int width, int height) {
        this.mDataManager.setProjection(0, width, height, pipelineInfo.origin() == 1);
        this.mGPImpl.setData(this.mDataManager, pipelineInfo.geomProc());
        return this.mDataManager.bindAndUploadUniforms(this.getDevice(), commandBuffer);
    }

    public boolean bindTextures(GLCommandBuffer commandBuffer, PipelineInfo pipelineInfo, TextureProxy[] geomTextures) {
        int unit = 0;
        int i = 0;
        for (int n = pipelineInfo.geomProc().numTextureSamplers(); i < n; i++) {
            GLTexture texture = (GLTexture) geomTextures[i].getGpuTexture();
            commandBuffer.bindTexture(unit++, texture, pipelineInfo.geomProc().textureSamplerState(i), pipelineInfo.geomProc().textureSamplerSwizzle(i));
        }
        assert unit == this.mNumTextureSamplers;
        return true;
    }

    public void bindBuffers(@Nullable GpuBuffer indexBuffer, @Nullable GpuBuffer vertexBuffer, long vertexOffset, @Nullable GpuBuffer instanceBuffer, long instanceOffset) {
        if (indexBuffer != null) {
            this.bindIndexBuffer((GLBuffer) indexBuffer);
        }
        if (vertexBuffer != null) {
            this.bindVertexBuffer((GLBuffer) vertexBuffer, vertexOffset);
        }
        if (instanceBuffer != null) {
            this.bindInstanceBuffer((GLBuffer) instanceBuffer, instanceOffset);
        }
    }

    public int getVertexStride() {
        return this.mVertexArray.getVertexStride();
    }

    public int getInstanceStride() {
        return this.mVertexArray.getInstanceStride();
    }

    public void bindIndexBuffer(@Nonnull GLBuffer buffer) {
        if (this.mVertexArray != null) {
            this.mVertexArray.bindIndexBuffer(buffer);
        }
    }

    public void bindVertexBuffer(@Nonnull GLBuffer buffer, long offset) {
        if (this.mVertexArray != null) {
            this.mVertexArray.bindVertexBuffer(buffer, offset);
        }
    }

    public void bindInstanceBuffer(@Nonnull GLBuffer buffer, long offset) {
        if (this.mVertexArray != null) {
            this.mVertexArray.bindInstanceBuffer(buffer, offset);
        }
    }

    protected GLDevice getDevice() {
        return (GLDevice) super.getDevice();
    }
}