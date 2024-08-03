package icyllis.arc3d.engine;

public abstract class GraphicsPipelineState {

    private final GpuDevice mDevice;

    public GraphicsPipelineState(GpuDevice device) {
        this.mDevice = device;
    }

    protected GpuDevice getDevice() {
        return this.mDevice;
    }
}