package icyllis.arc3d.engine;

import java.util.concurrent.atomic.AtomicInteger;
import javax.annotation.concurrent.ThreadSafe;

@ThreadSafe
public abstract class PipelineStateCache {

    protected final PipelineStateCache.Stats mStats = new PipelineStateCache.Stats();

    public abstract GraphicsPipelineState findOrCreateGraphicsPipelineState(PipelineDesc var1, PipelineInfo var2);

    protected abstract void close();

    public final PipelineStateCache.Stats getStats() {
        return this.mStats;
    }

    public static class Stats {

        private final AtomicInteger mShaderCompilations = new AtomicInteger();

        private final AtomicInteger mNumInlineCompilationFailures = new AtomicInteger();

        private final AtomicInteger mNumPreCompilationFailures = new AtomicInteger();

        private final AtomicInteger mNumCompilationFailures = new AtomicInteger();

        private final AtomicInteger mNumPartialCompilationSuccesses = new AtomicInteger();

        private final AtomicInteger mNumCompilationSuccesses = new AtomicInteger();

        public int shaderCompilations() {
            return this.mShaderCompilations.get();
        }

        public void incShaderCompilations() {
            this.mShaderCompilations.getAndIncrement();
        }

        public int numInlineCompilationFailures() {
            return this.mNumInlineCompilationFailures.get();
        }

        public void incNumInlineCompilationFailures() {
            this.mNumInlineCompilationFailures.getAndIncrement();
        }

        public int numPreCompilationFailures() {
            return this.mNumPreCompilationFailures.get();
        }

        public void incNumPreCompilationFailures() {
            this.mNumPreCompilationFailures.getAndIncrement();
        }

        public int numCompilationFailures() {
            return this.mNumCompilationFailures.get();
        }

        public void incNumCompilationFailures() {
            this.mNumCompilationFailures.getAndIncrement();
        }

        public int numPartialCompilationSuccesses() {
            return this.mNumPartialCompilationSuccesses.get();
        }

        public void incNumPartialCompilationSuccesses() {
            this.mNumPartialCompilationSuccesses.getAndIncrement();
        }

        public int numCompilationSuccesses() {
            return this.mNumCompilationSuccesses.get();
        }

        public void incNumCompilationSuccesses() {
            this.mNumCompilationSuccesses.getAndIncrement();
        }

        public String toString() {
            return "PipelineStateCache.Stats{shaderCompilations=" + this.mShaderCompilations + ", numInlineCompilationFailures=" + this.mNumInlineCompilationFailures + ", numPreCompilationFailures=" + this.mNumPreCompilationFailures + ", numCompilationFailures=" + this.mNumCompilationFailures + ", numPartialCompilationSuccesses=" + this.mNumPartialCompilationSuccesses + ", numCompilationSuccesses=" + this.mNumCompilationSuccesses + "}";
        }
    }
}