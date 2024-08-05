package icyllis.arc3d.opengl;

import icyllis.arc3d.engine.Caps;
import icyllis.arc3d.engine.Key;
import icyllis.arc3d.engine.PipelineDesc;
import icyllis.arc3d.engine.PipelineInfo;
import icyllis.arc3d.engine.PipelineStateCache;
import java.util.concurrent.ConcurrentHashMap;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.jetbrains.annotations.VisibleForTesting;

public class GLPipelineStateCache extends PipelineStateCache {

    private final GLDevice mDevice;

    private final int mCacheSize;

    private final ConcurrentHashMap<Key, GLGraphicsPipelineState> mCache;

    @VisibleForTesting
    public GLPipelineStateCache(GLDevice device, int cacheSize) {
        this.mDevice = device;
        this.mCacheSize = cacheSize;
        this.mCache = new ConcurrentHashMap(cacheSize, 0.5F);
    }

    public void discard() {
        this.mCache.values().forEach(GLGraphicsPipelineState::discard);
        this.release();
    }

    public void release() {
        this.mCache.values().forEach(GLGraphicsPipelineState::release);
        this.mCache.clear();
    }

    @Nullable
    public GLGraphicsPipelineState findOrCreateGraphicsPipelineState(PipelineDesc desc, PipelineInfo pipelineInfo) {
        if (desc.isEmpty()) {
            Caps caps = this.mDevice.getCaps();
            caps.makeDesc(desc, null, pipelineInfo);
        }
        assert !desc.isEmpty();
        return this.findOrCreateGraphicsPipelineStateImpl(desc, pipelineInfo);
    }

    @Nonnull
    private GLGraphicsPipelineState findOrCreateGraphicsPipelineStateImpl(PipelineDesc desc, PipelineInfo pipelineInfo) {
        GLGraphicsPipelineState existing = (GLGraphicsPipelineState) this.mCache.get(desc);
        if (existing != null) {
            return existing;
        } else {
            desc = new PipelineDesc(desc);
            GLGraphicsPipelineState newPipelineState = GLPipelineStateBuilder.createGraphicsPipelineState(this.mDevice, desc, pipelineInfo);
            existing = (GLGraphicsPipelineState) this.mCache.putIfAbsent(desc.toStorageKey(), newPipelineState);
            if (existing != null) {
                newPipelineState.discard();
                return existing;
            } else {
                return newPipelineState;
            }
        }
    }

    @Override
    public void close() {
        assert this.mCache.isEmpty();
    }
}