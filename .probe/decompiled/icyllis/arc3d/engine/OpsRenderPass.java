package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2fc;
import icyllis.arc3d.core.SharedPtr;

public abstract class OpsRenderPass {

    private static final int kConfigured_DrawPipelineStatus = 0;

    private static final int kNotConfigured_DrawPipelineStatus = 1;

    private static final int kFailedToBind_DrawPipelineStatus = 2;

    private int mDrawPipelineStatus = 1;

    protected GpuRenderTarget mRenderTarget;

    protected int mSurfaceOrigin;

    private TextureProxy[] mGeomTextures = new TextureProxy[1];

    public OpsRenderPass() {
        this(null, 0);
    }

    public OpsRenderPass(GpuRenderTarget fs, int origin) {
        this.mRenderTarget = fs;
        this.mSurfaceOrigin = origin;
    }

    public void begin() {
        this.mDrawPipelineStatus = 1;
    }

    public void end() {
    }

    public void clearColor(int left, int top, int right, int bottom, float red, float green, float blue, float alpha) {
        assert this.mRenderTarget != null;
        this.mDrawPipelineStatus = 1;
    }

    public void clearStencil(int left, int top, int right, int bottom, boolean insideMask) {
        assert this.mRenderTarget != null;
        this.mDrawPipelineStatus = 1;
    }

    public void bindPipeline(PipelineInfo pipelineInfo, GraphicsPipelineState pipelineState, Rect2fc drawBounds) {
        assert pipelineInfo.origin() == this.mSurfaceOrigin;
        if (!this.onBindPipeline(pipelineInfo, pipelineState, drawBounds)) {
            this.mDrawPipelineStatus = 2;
        } else {
            this.mDrawPipelineStatus = 0;
        }
    }

    public final void bindTexture(TextureProxy geomTexture) {
        this.mGeomTextures[0] = geomTexture;
        this.bindTextures(this.mGeomTextures);
        this.mGeomTextures[0] = null;
    }

    public final void bindTextures(TextureProxy[] geomTextures) {
    }

    public final void bindBuffers(GpuBuffer indexBuffer, GpuBuffer vertexBuffer, GpuBuffer instanceBuffer) {
        if (vertexBuffer == null && instanceBuffer == null) {
            this.mDrawPipelineStatus = 2;
        } else {
            if (this.mDrawPipelineStatus == 0) {
                this.onBindBuffers(indexBuffer, vertexBuffer, instanceBuffer);
            } else {
                assert this.mDrawPipelineStatus == 2;
            }
        }
    }

    public final void draw(int vertexCount, int baseVertex) {
        if (this.mDrawPipelineStatus == 0) {
            this.onDraw(vertexCount, baseVertex);
            this.getDevice().getStats().incNumDraws();
        } else {
            assert this.mDrawPipelineStatus == 2;
            this.getDevice().getStats().incNumFailedDraws();
        }
    }

    public final void drawIndexed(int indexCount, int baseIndex, int baseVertex) {
        if (this.mDrawPipelineStatus == 0) {
            this.onDrawIndexed(indexCount, baseIndex, baseVertex);
            this.getDevice().getStats().incNumDraws();
        } else {
            assert this.mDrawPipelineStatus == 2;
            this.getDevice().getStats().incNumFailedDraws();
        }
    }

    public final void drawInstanced(int instanceCount, int baseInstance, int vertexCount, int baseVertex) {
        if (this.mDrawPipelineStatus == 0) {
            this.onDrawInstanced(instanceCount, baseInstance, vertexCount, baseVertex);
            this.getDevice().getStats().incNumDraws();
        } else {
            assert this.mDrawPipelineStatus == 2;
            this.getDevice().getStats().incNumFailedDraws();
        }
    }

    public final void drawIndexedInstanced(int indexCount, int baseIndex, int instanceCount, int baseInstance, int baseVertex) {
        if (this.mDrawPipelineStatus == 0) {
            this.onDrawIndexedInstanced(indexCount, baseIndex, instanceCount, baseInstance, baseVertex);
            this.getDevice().getStats().incNumDraws();
        } else {
            assert this.mDrawPipelineStatus == 2;
            this.getDevice().getStats().incNumFailedDraws();
        }
    }

    protected void set(GpuRenderTarget rt, int origin) {
        assert this.mRenderTarget == null;
        this.mRenderTarget = rt;
        this.mSurfaceOrigin = origin;
    }

    protected abstract GpuDevice getDevice();

    protected abstract boolean onBindPipeline(PipelineInfo var1, GraphicsPipelineState var2, Rect2fc var3);

    protected abstract void onBindBuffers(@SharedPtr GpuBuffer var1, @SharedPtr GpuBuffer var2, @SharedPtr GpuBuffer var3);

    protected abstract void onDraw(int var1, int var2);

    protected abstract void onDrawIndexed(int var1, int var2, int var3);

    protected abstract void onDrawInstanced(int var1, int var2, int var3, int var4);

    protected abstract void onDrawIndexedInstanced(int var1, int var2, int var3, int var4, int var5);
}