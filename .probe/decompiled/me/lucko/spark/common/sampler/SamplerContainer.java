package me.lucko.spark.common.sampler;

import java.util.concurrent.atomic.AtomicReference;

public class SamplerContainer implements AutoCloseable {

    private final AtomicReference<Sampler> activeSampler = new AtomicReference();

    public Sampler getActiveSampler() {
        return (Sampler) this.activeSampler.get();
    }

    public void setActiveSampler(Sampler sampler) {
        if (!this.activeSampler.compareAndSet(null, sampler)) {
            throw new IllegalStateException("Attempted to set active sampler when another was already active!");
        }
    }

    public void unsetActiveSampler(Sampler sampler) {
        this.activeSampler.compareAndSet(sampler, null);
    }

    public void stopActiveSampler(boolean cancelled) {
        Sampler sampler = (Sampler) this.activeSampler.getAndSet(null);
        if (sampler != null) {
            sampler.stop(cancelled);
        }
    }

    public void close() {
        this.stopActiveSampler(true);
    }
}