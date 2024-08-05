package icyllis.arc3d.engine;

import icyllis.arc3d.core.Rect2i;
import java.nio.ByteBuffer;
import java.util.Set;
import javax.annotation.Nullable;

public class OpFlushState implements MeshDrawTarget {

    private final GpuDevice mDevice;

    private OpsRenderPass mOpsRenderPass;

    public OpFlushState(GpuDevice device, ResourceProvider resourceProvider) {
        this.mDevice = device;
    }

    public GpuDevice getDevice() {
        return this.mDevice;
    }

    public final GraphicsPipelineState findOrCreateGraphicsPipelineState(PipelineInfo pipelineInfo) {
        return this.mDevice.getContext().findOrCreateGraphicsPipelineState(pipelineInfo);
    }

    @Override
    public long makeVertexSpace(Mesh mesh) {
        return this.mDevice.getVertexPool().makeSpace(mesh);
    }

    @Override
    public long makeInstanceSpace(Mesh mesh) {
        return this.mDevice.getInstancePool().makeSpace(mesh);
    }

    @Override
    public long makeIndexSpace(Mesh mesh) {
        return this.mDevice.getIndexPool().makeSpace(mesh);
    }

    @Nullable
    @Override
    public ByteBuffer makeVertexWriter(Mesh mesh) {
        return this.mDevice.getVertexPool().makeWriter(mesh);
    }

    @Nullable
    @Override
    public ByteBuffer makeInstanceWriter(Mesh mesh) {
        return this.mDevice.getInstancePool().makeWriter(mesh);
    }

    @Nullable
    @Override
    public ByteBuffer makeIndexWriter(Mesh mesh) {
        return this.mDevice.getIndexPool().makeWriter(mesh);
    }

    public OpsRenderPass getOpsRenderPass() {
        return this.mOpsRenderPass;
    }

    public OpsRenderPass beginOpsRenderPass(SurfaceView writeView, Rect2i contentBounds, byte colorOps, byte stencilOps, float[] clearColor, Set<TextureProxy> sampledTextures, int pipelineFlags) {
        assert this.mOpsRenderPass == null;
        OpsRenderPass opsRenderPass = this.mDevice.getOpsRenderPass(writeView, contentBounds, colorOps, stencilOps, clearColor, sampledTextures, pipelineFlags);
        if (opsRenderPass == null) {
            return null;
        } else {
            this.mOpsRenderPass = opsRenderPass;
            opsRenderPass.begin();
            return opsRenderPass;
        }
    }

    public void reset() {
    }
}