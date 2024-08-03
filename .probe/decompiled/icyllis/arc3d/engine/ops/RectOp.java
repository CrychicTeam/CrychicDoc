package icyllis.arc3d.engine.ops;

import icyllis.arc3d.core.Matrix;
import icyllis.arc3d.core.Matrixc;
import icyllis.arc3d.core.Rect2f;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.GpuBuffer;
import icyllis.arc3d.engine.MeshDrawTarget;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.OpsRenderPass;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.SurfaceView;
import icyllis.arc3d.engine.geom.SDFRectGeoProc;
import java.nio.ByteBuffer;
import javax.annotation.Nonnull;
import org.lwjgl.system.MemoryUtil;

public class RectOp extends MeshDrawOp {

    private GpuBuffer mVertexBuffer;

    private int mBaseVertex;

    private GpuBuffer mInstanceBuffer;

    private int mBaseInstance;

    private final int mColor;

    private final Rect2f mLocalRect;

    private final float mStrokeRadius;

    private final float mStrokePos;

    private final Matrix mViewMatrix;

    private final int mGPFlags;

    private int mNumInstances = 1;

    public RectOp(int argb, Rect2f localRect, float strokeRadius, float strokePos, Matrixc viewMatrix, boolean stroke, boolean aa) {
        this.mColor = argb;
        this.mLocalRect = localRect;
        this.mStrokeRadius = strokeRadius;
        this.mStrokePos = strokePos;
        if (!viewMatrix.isIdentity()) {
            this.mViewMatrix = new Matrix(viewMatrix);
        } else {
            this.mViewMatrix = null;
        }
        int gpFlags = 0;
        if (aa) {
            gpFlags |= 1;
        }
        if (stroke) {
            gpFlags |= 2;
        }
        if (this.mViewMatrix != null) {
            gpFlags |= 4;
        }
        this.mGPFlags = gpFlags;
        if (this.mViewMatrix != null) {
            this.mViewMatrix.mapRect(localRect, this);
        } else {
            this.set(localRect);
        }
        this.setBoundsFlags(aa, false);
    }

    @Override
    protected boolean onMayChain(@Nonnull Op __) {
        RectOp op = (RectOp) __;
        if (op.mGPFlags == this.mGPFlags) {
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
    @Override
    protected PipelineInfo onCreatePipelineInfo(SurfaceView writeView, int pipelineFlags) {
        return new PipelineInfo(writeView, new SDFRectGeoProc(this.mGPFlags), null, null, null, null, pipelineFlags);
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
                    RectOp op = (RectOp) it;
                    int color = op.mColor;
                    instanceData.put((byte) (color >> 16));
                    instanceData.put((byte) (color >> 8));
                    instanceData.put((byte) color);
                    instanceData.put((byte) (color >> 24));
                    instanceData.putFloat(op.mLocalRect.width() / 2.0F);
                    instanceData.putFloat(op.mLocalRect.centerX());
                    instanceData.putFloat(op.mLocalRect.height() / 2.0F);
                    instanceData.putFloat(op.mLocalRect.centerY());
                    if ((op.mGPFlags & 2) != 0) {
                        instanceData.putFloat(op.mStrokeRadius).putFloat(op.mStrokePos);
                    }
                    if ((op.mGPFlags & 4) != 0) {
                        op.mViewMatrix.store(MemoryUtil.memAddress(instanceData));
                        instanceData.position(instanceData.position() + 36);
                    }
                }
            }
        }
    }
}