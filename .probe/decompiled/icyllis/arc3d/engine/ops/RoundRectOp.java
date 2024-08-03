package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Matrix;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GpuBuffer;
import icyllis.arc3d.engine.MeshDrawTarget;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.OpsRenderPass;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.geom.SDFRoundRectGeoProc;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

public class RoundRectOp extends MeshDrawOp {

    private GpuBuffer mVertexBuffer;

    private int mBaseVertex;

    private GpuBuffer mInstanceBuffer;

    private int mBaseInstance;

    private float[] mColor;

    private Rect2f mLocalRect;

    private float mCornerRadius;

    private float mStrokeRadius;

    private Matrix mViewMatrix;

    private boolean mStroke;

    private int mNumInstances = 1;

    public RoundRectOp(float[] color, Rect2f localRect, float cornerRadius, float strokeRadius, Matrix viewMatrix, boolean stroke) {
        this.mColor = color;
        this.mLocalRect = localRect;
        this.mCornerRadius = cornerRadius;
        this.mStrokeRadius = strokeRadius;
        this.mViewMatrix = viewMatrix;
        this.mStroke = stroke;
        viewMatrix.mapRect(localRect, this);
    }

    @Override
    protected boolean onMayChain(@Nonnull Op __) {
        RoundRectOp op = (RoundRectOp) __;
        if (op.mStroke == this.mStroke) {
            this.mNumInstances++;
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onExecute(OpFlushState state, Rect2f chainBounds) {
        OpsRenderPass opsRenderPass = state.getOpsRenderPass();
        opsRenderPass.bindPipeline(this.getPipelineInfo(), this.getPipelineState(), chainBounds);
        opsRenderPass.bindTextures(null);
        opsRenderPass.bindBuffers(null, this.mVertexBuffer, this.mInstanceBuffer);
        opsRenderPass.drawInstanced(this.getInstanceCount(), this.mBaseInstance, this.getVertexCount(), this.mBaseVertex);
    }

    @Nonnull
    @NotNull
    @Override
    protected PipelineInfo onCreatePipelineInfo(SurfaceView writeView, int pipelineFlags) {
        return new PipelineInfo(writeView, new SDFRoundRectGeoProc(this.mStroke), null, null, null, null, pipelineFlags);
    }

    @Override
    public int getVertexCount() {
        return 4;
    }

    @Override
    public int getInstanceCount() {
        return this.mNumInstances;
    }

    @Override
    public void setVertexBuffer(@SharedPtr GpuBuffer buffer, int baseVertex, int actualVertexCount) {
        assert this.mVertexBuffer == null;
        this.mVertexBuffer = buffer;
        this.mBaseVertex = baseVertex;
    }

    @Override
    public void setInstanceBuffer(@SharedPtr GpuBuffer buffer, int baseInstance, int actualInstanceCount) {
        assert this.mInstanceBuffer == null;
        this.mInstanceBuffer = buffer;
        this.mBaseInstance = baseInstance;
    }

    @Override
    protected void onPrepareDraws(MeshDrawTarget target) {
        ByteBuffer vertexData = target.makeVertexWriter(this);
        if (vertexData != null) {
            vertexData.putFloat(-1.0F).putFloat(1.0F);
            vertexData.putFloat(1.0F).putFloat(1.0F);
            vertexData.putFloat(-1.0F).putFloat(-1.0F);
            vertexData.putFloat(1.0F).putFloat(-1.0F);
            ByteBuffer instanceData = target.makeInstanceWriter(this);
            if (instanceData != null) {
                for (Op it = this; it != null; it = it.nextInChain()) {
                    RoundRectOp op = (RoundRectOp) it;
                    instanceData.putFloat(op.mColor[0]);
                    instanceData.putFloat(op.mColor[1]);
                    instanceData.putFloat(op.mColor[2]);
                    instanceData.putFloat(op.mColor[3]);
                    instanceData.putFloat(op.mLocalRect.width() / 2.0F);
                    instanceData.putFloat(op.mLocalRect.centerX());
                    instanceData.putFloat(op.mLocalRect.height() / 2.0F);
                    instanceData.putFloat(op.mLocalRect.centerY());
                    instanceData.putFloat(op.mCornerRadius).putFloat(op.mStrokeRadius);
                    op.mViewMatrix.store(MemoryUtil.memAddress(instanceData));
                    instanceData.position(instanceData.position() + 36);
                }
            }
        }
    }
}