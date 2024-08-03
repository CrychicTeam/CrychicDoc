package icyllis.arc3d.opengl;

import icyllis.arc3d.core.Rect2fc;
import icyllis.arc3d.core.Rect2i;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GpuBuffer;
import icyllis.arc3d.engine.GpuRenderTarget;
import icyllis.arc3d.engine.GpuResource;
import icyllis.arc3d.engine.GraphicsPipelineState;
import icyllis.arc3d.engine.OpsRenderPass;
import icyllis.arc3d.engine.PipelineInfo;

public final class GLOpsRenderPass extends OpsRenderPass {

    private final GLDevice mDevice;

    private GLCommandBuffer mCmdBuffer;

    private GLGraphicsPipelineState mPipelineState;

    private byte mColorOps;

    private byte mStencilOps;

    private float[] mClearColor;

    @SharedPtr
    private GpuBuffer mActiveIndexBuffer;

    @SharedPtr
    private GpuBuffer mActiveVertexBuffer;

    @SharedPtr
    private GpuBuffer mActiveInstanceBuffer;

    private int mPrimitiveType;

    public GLOpsRenderPass(GLDevice device) {
        this.mDevice = device;
    }

    protected GLDevice getDevice() {
        return this.mDevice;
    }

    public GLOpsRenderPass set(GpuRenderTarget rt, Rect2i bounds, int origin, byte colorOps, byte stencilOps, float[] clearColor) {
        this.set(rt, origin);
        this.mColorOps = colorOps;
        this.mStencilOps = stencilOps;
        this.mClearColor = clearColor;
        return this;
    }

    @Override
    public void begin() {
        super.begin();
        GLRenderTarget glRenderTarget = (GLRenderTarget) this.mRenderTarget;
        this.mCmdBuffer = this.mDevice.beginRenderPass(glRenderTarget, this.mColorOps, this.mStencilOps, this.mClearColor);
    }

    @Override
    public void end() {
        this.mActiveIndexBuffer = GpuResource.move(this.mActiveIndexBuffer);
        this.mActiveVertexBuffer = GpuResource.move(this.mActiveVertexBuffer);
        this.mActiveInstanceBuffer = GpuResource.move(this.mActiveInstanceBuffer);
        GLRenderTarget glRenderTarget = (GLRenderTarget) this.mRenderTarget;
        this.mDevice.endRenderPass(glRenderTarget, this.mColorOps, this.mStencilOps);
        super.end();
    }

    @Override
    protected boolean onBindPipeline(PipelineInfo pipelineInfo, GraphicsPipelineState pipelineState, Rect2fc drawBounds) {
        this.mActiveIndexBuffer = GpuResource.move(this.mActiveIndexBuffer);
        this.mActiveVertexBuffer = GpuResource.move(this.mActiveVertexBuffer);
        this.mActiveInstanceBuffer = GpuResource.move(this.mActiveInstanceBuffer);
        this.mPipelineState = (GLGraphicsPipelineState) pipelineState;
        if (this.mPipelineState == null) {
            return false;
        } else {
            this.mPrimitiveType = switch(pipelineInfo.primitiveType()) {
                case 0 ->
                    0;
                case 1 ->
                    1;
                case 2 ->
                    3;
                case 3 ->
                    4;
                case 4 ->
                    5;
                default ->
                    throw new AssertionError();
            };
            return !this.mPipelineState.bindPipeline(this.mCmdBuffer) ? false : this.mPipelineState.bindUniforms(this.mCmdBuffer, pipelineInfo, this.mRenderTarget.getWidth(), this.mRenderTarget.getHeight());
        }
    }

    @Override
    public void clearColor(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        super.clearColor(left, top, right, bottom, red, green, blue, alpha);
    }

    @Override
    public void clearStencil(int left, int top, int right, int bottom, boolean insideMask) {
        super.clearStencil(left, top, right, bottom, insideMask);
    }

    @Override
    protected void onBindBuffers(GpuBuffer indexBuffer, GpuBuffer vertexBuffer, GpuBuffer instanceBuffer) {
        assert this.mPipelineState != null;
        if (this.mDevice.getCaps().hasBaseInstanceSupport()) {
            this.mPipelineState.bindBuffers(indexBuffer, vertexBuffer, 0L, instanceBuffer, 0L);
        } else {
            this.mPipelineState.bindBuffers(indexBuffer, vertexBuffer, 0L, null, 0L);
        }
        this.mActiveIndexBuffer = GpuResource.create(this.mActiveIndexBuffer, indexBuffer);
        this.mActiveVertexBuffer = GpuResource.create(this.mActiveVertexBuffer, vertexBuffer);
        this.mActiveInstanceBuffer = GpuResource.create(this.mActiveInstanceBuffer, instanceBuffer);
    }

    @Override
    protected void onDraw(int vertexCount, int baseVertex) {
        GLCore.glDrawArrays(this.mPrimitiveType, baseVertex, vertexCount);
    }

    @Override
    protected void onDrawIndexed(int indexCount, int baseIndex, int baseVertex) {
        GLCore.nglDrawElementsBaseVertex(this.mPrimitiveType, indexCount, 5123, (long) baseIndex, baseVertex);
    }

    @Override
    protected void onDrawInstanced(int instanceCount, int baseInstance, int vertexCount, int baseVertex) {
        if (this.mDevice.getCaps().hasBaseInstanceSupport()) {
            GLCore.glDrawArraysInstancedBaseInstance(this.mPrimitiveType, baseVertex, vertexCount, instanceCount, baseInstance);
        } else {
            long instanceOffset = (long) baseInstance * (long) this.mPipelineState.getInstanceStride();
            this.mPipelineState.bindInstanceBuffer((GLBuffer) this.mActiveInstanceBuffer, instanceOffset);
            GLCore.glDrawArraysInstanced(this.mPrimitiveType, baseVertex, vertexCount, instanceCount);
        }
    }

    @Override
    protected void onDrawIndexedInstanced(int indexCount, int baseIndex, int instanceCount, int baseInstance, int baseVertex) {
        if (this.mDevice.getCaps().hasBaseInstanceSupport()) {
            GLCore.nglDrawElementsInstancedBaseVertexBaseInstance(this.mPrimitiveType, indexCount, 5123, (long) baseIndex, instanceCount, baseVertex, baseInstance);
        } else {
            long instanceOffset = (long) baseInstance * (long) this.mPipelineState.getInstanceStride();
            this.mPipelineState.bindInstanceBuffer((GLBuffer) this.mActiveInstanceBuffer, instanceOffset);
            GLCore.glDrawElementsInstancedBaseVertex(this.mPrimitiveType, indexCount, 5123, (long) baseIndex, instanceCount, baseVertex);
        }
    }
}