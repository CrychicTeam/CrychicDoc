package icyllis.arc3d.opengl;

import icyllis.arc3d.core.RefCnt;
import icyllis.arc3d.core.SharedPtr;
import icyllis.arc3d.engine.DirectContext;
import icyllis.arc3d.engine.ResourceProvider;
import it.unimi.dsi.fastutil.ints.Int2ObjectLinkedOpenHashMap;
import javax.annotation.Nullable;

public final class GLResourceProvider extends ResourceProvider {

    private static final int SAMPLER_CACHE_SIZE = 32;

    private final GLDevice mDevice;

    private final Int2ObjectLinkedOpenHashMap<GLSampler> mSamplerCache = new Int2ObjectLinkedOpenHashMap(32);

    GLResourceProvider(GLDevice device, DirectContext context) {
        super(device, context);
        this.mDevice = device;
    }

    void discard() {
        this.mSamplerCache.values().forEach(GLSampler::discard);
        this.release();
    }

    void release() {
        this.mSamplerCache.values().forEach(RefCnt::unref);
        this.mSamplerCache.clear();
    }

    @Nullable
    @SharedPtr
    public GLSampler findOrCreateCompatibleSampler(int samplerState) {
        GLSampler sampler = (GLSampler) this.mSamplerCache.getAndMoveToFirst(samplerState);
        if (sampler == null) {
            sampler = GLSampler.create(this.mDevice, samplerState);
            if (sampler == null) {
                return null;
            }
            while (this.mSamplerCache.size() >= 32) {
                ((GLSampler) this.mSamplerCache.removeLast()).unref();
            }
            this.mSamplerCache.putAndMoveToFirst(samplerState, sampler);
        }
        sampler.ref();
        return sampler;
    }
}