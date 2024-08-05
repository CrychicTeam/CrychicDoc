package dev.xkmc.l2archery.content.feature.core;

import dev.xkmc.l2archery.content.item.IBowConfig;

public record CompoundBowConfig(IBowConfig config, StatFeature feature) implements IBowConfig {

    @Override
    public float speed() {
        return this.config.speed() * this.feature.speed();
    }

    @Override
    public float fov() {
        float old = 1.0F / (1.0F - this.config.fov());
        float next = old * this.feature.fov();
        next = Math.min(10.0F, next);
        return 1.0F - 1.0F / next;
    }

    @Override
    public int pull_time() {
        return this.config.pull_time() * this.feature.pull_time();
    }

    @Override
    public int fov_time() {
        return this.config.fov_time() + this.feature.fov_time();
    }

    @Override
    public PotionArrowFeature getEffects() {
        return this.config.getEffects();
    }

    @Override
    public float damage() {
        return this.config.damage() * this.feature.damage();
    }

    @Override
    public int punch() {
        return this.config.punch() + this.feature.punch();
    }
}