package dev.kosmx.playerAnim.api.layered;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonConfiguration;
import dev.kosmx.playerAnim.api.firstPerson.FirstPersonMode;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnimationContainer<T extends IAnimation> implements IAnimation {

    @Nullable
    protected T anim;

    public AnimationContainer(@Nullable T anim) {
        this.anim = anim;
    }

    public AnimationContainer() {
        this.anim = null;
    }

    public void setAnim(@Nullable T newAnim) {
        this.anim = newAnim;
    }

    @Nullable
    public T getAnim() {
        return this.anim;
    }

    @Override
    public boolean isActive() {
        return this.anim != null && this.anim.isActive();
    }

    @Override
    public void tick() {
        if (this.anim != null) {
            this.anim.tick();
        }
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        return this.anim == null ? value0 : this.anim.get3DTransform(modelName, type, tickDelta, value0);
    }

    @Override
    public void setupAnim(float tickDelta) {
        if (this.anim != null) {
            this.anim.setupAnim(tickDelta);
        }
    }

    @NotNull
    @Override
    public FirstPersonMode getFirstPersonMode(float tickDelta) {
        return this.anim != null ? this.anim.getFirstPersonMode(tickDelta) : FirstPersonMode.NONE;
    }

    @NotNull
    @Override
    public FirstPersonConfiguration getFirstPersonConfiguration(float tickDelta) {
        return this.anim != null ? this.anim.getFirstPersonConfiguration(tickDelta) : IAnimation.super.getFirstPersonConfiguration(tickDelta);
    }
}