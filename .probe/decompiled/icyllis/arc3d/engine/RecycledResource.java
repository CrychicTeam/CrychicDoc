package icyllis.arc3d.engine;

public abstract class RecycledResource extends ManagedResource {

    public RecycledResource(GpuDevice device) {
        super(device);
    }

    public final void recycle() {
        if (this.unique()) {
            this.onRecycle();
        } else {
            this.unref();
        }
    }

    public abstract void onRecycle();
}