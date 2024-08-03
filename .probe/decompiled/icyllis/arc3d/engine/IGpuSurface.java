package icyllis.arc3d.engine;

public interface IGpuSurface extends ISurface {

    int getSurfaceFlags();

    default GpuTexture asTexture() {
        return null;
    }

    default GpuRenderTarget asRenderTarget() {
        return null;
    }
}