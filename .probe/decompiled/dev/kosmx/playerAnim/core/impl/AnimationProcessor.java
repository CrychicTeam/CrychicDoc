package dev.kosmx.playerAnim.core.impl;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Pair;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public class AnimationProcessor {

    private final IAnimation animation;

    private float tickDelta = 0.0F;

    public AnimationProcessor(IAnimation animation) {
        this.animation = animation;
    }

    public void tick() {
        this.animation.tick();
    }

    public boolean isActive() {
        return this.animation.isActive();
    }

    public Vec3f get3DTransform(String modelName, TransformType type, Vec3f value0) {
        return this.animation.get3DTransform(modelName, type, this.tickDelta, value0);
    }

    public void setTickDelta(float tickDelta) {
        this.tickDelta = tickDelta;
        this.animation.setupAnim(tickDelta);
    }

    public boolean isFirstPersonAnimationDisabled() {
        return !this.animation.getFirstPersonMode(this.tickDelta).isEnabled();
    }

    @NotNull
    public FirstPersonMode getFirstPersonMode() {
        return this.animation.getFirstPersonMode(this.tickDelta);
    }

    @NotNull
    public FirstPersonConfiguration getFirstPersonConfiguration() {
        return this.animation.getFirstPersonConfiguration(this.tickDelta);
    }

    public Pair<Float, Float> getBend(String modelName) {
        Vec3f bendVec = this.get3DTransform(modelName, TransformType.BEND, Vec3f.ZERO);
        return new Pair<>(bendVec.getX(), bendVec.getY());
    }
}