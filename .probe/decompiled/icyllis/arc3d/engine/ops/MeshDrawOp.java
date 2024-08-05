package icyllis.arc3d.engine.ops;

import icyllis.arc3d.engine.GraphicsPipelineState;
import icyllis.arc3d.engine.Mesh;
import icyllis.arc3d.engine.MeshDrawTarget;
import icyllis.arc3d.engine.OpFlushState;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.RecordingContext;
import icyllis.arc3d.engine.SurfaceView;
import javax.annotation.Nonnull;

public abstract class MeshDrawOp extends DrawOp implements Mesh {

    private PipelineInfo mPipelineInfo;

    private GraphicsPipelineState mPipelineState;

    public PipelineInfo getPipelineInfo() {
        return this.mPipelineInfo;
    }

    public GraphicsPipelineState getPipelineState() {
        return this.mPipelineState;
    }

    @Override
    public int getVertexSize() {
        return this.mPipelineInfo.geomProc().vertexStride();
    }

    @Override
    public int getInstanceSize() {
        return this.mPipelineInfo.geomProc().instanceStride();
    }

    @Override
    public void onPrePrepare(RecordingContext context, SurfaceView writeView, int pipelineFlags) {
        assert this.mPipelineInfo == null;
        this.mPipelineInfo = this.onCreatePipelineInfo(writeView, pipelineFlags);
        this.mPipelineState = context.findOrCreateGraphicsPipelineState(this.mPipelineInfo);
    }

    @Override
    public final void onPrepare(OpFlushState state, SurfaceView writeView, int pipelineFlags) {
        if (this.mPipelineInfo == null) {
            this.mPipelineInfo = this.onCreatePipelineInfo(writeView, pipelineFlags);
        }
        if (this.mPipelineState == null) {
            this.mPipelineState = state.findOrCreateGraphicsPipelineState(this.mPipelineInfo);
        }
        this.onPrepareDraws(state);
    }

    @Nonnull
    protected abstract PipelineInfo onCreatePipelineInfo(SurfaceView var1, int var2);

    protected abstract void onPrepareDraws(MeshDrawTarget var1);
}