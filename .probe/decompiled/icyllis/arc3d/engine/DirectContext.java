package icyllis.arc3d.engine;

import icyllis.arc3d.opengl.GLDevice;
import icyllis.arc3d.vulkan.VkBackendContext;
import javax.annotation.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class DirectContext extends RecordingContext {

    private GpuDevice mDevice;

    private ResourceCache mResourceCache;

    private ResourceProvider mResourceProvider;

    private DirectContext(int backend, ContextOptions options) {
        super(new SharedContextInfo(backend, options));
    }

    @Nullable
    public static DirectContext makeOpenGL() {
        return makeOpenGL(new ContextOptions());
    }

    @Nullable
    public static DirectContext makeOpenGL(ContextOptions options) {
        DirectContext context = new DirectContext(0, options);
        context.mDevice = GLDevice.make(context, options);
        if (context.init()) {
            return context;
        } else {
            context.unref();
            return null;
        }
    }

    @Nullable
    public static DirectContext makeVulkan(VkBackendContext backendContext) {
        return makeVulkan(backendContext, new ContextOptions());
    }

    @Nullable
    public static DirectContext makeVulkan(VkBackendContext backendContext, ContextOptions options) {
        return null;
    }

    public void resetContext(int state) {
        this.checkOwnerThread();
        this.mDevice.markContextDirty(state);
    }

    @Internal
    public GpuDevice getDevice() {
        return this.mDevice;
    }

    @Internal
    public ResourceCache getResourceCache() {
        return this.mResourceCache;
    }

    @Internal
    public ResourceProvider getResourceProvider() {
        return this.mResourceProvider;
    }

    @Override
    protected boolean init() {
        assert this.isOwnerThread();
        if (this.mDevice == null) {
            return false;
        } else {
            this.mContextInfo.init(this.mDevice.getCaps(), this.mDevice.getPipelineStateCache());
            if (!super.init()) {
                return false;
            } else {
                assert this.getThreadSafeCache() != null;
                this.mResourceCache = new ResourceCache(this.getContextID());
                this.mResourceCache.setSurfaceProvider(this.getSurfaceProvider());
                this.mResourceCache.setThreadSafeCache(this.getThreadSafeCache());
                this.mResourceProvider = this.mDevice.getResourceProvider();
                return true;
            }
        }
    }

    @Override
    protected void deallocate() {
        super.deallocate();
        if (this.mResourceCache != null) {
            this.mResourceCache.releaseAll();
        }
        this.mDevice.disconnect(true);
    }
}