package dev.kosmx.playerAnim.api.layered.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.core.util.Ease;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractFadeModifier extends AbstractModifier {

    protected int time = 0;

    protected int length;

    @Nullable
    protected IAnimation beginAnimation;

    protected AbstractFadeModifier(int length) {
        this.length = length;
    }

    @Override
    public boolean isActive() {
        return super.isActive() || this.beginAnimation != null && this.beginAnimation.isActive();
    }

    @Override
    public boolean canRemove() {
        return this.length <= this.time;
    }

    @Override
    public void setupAnim(float tickDelta) {
        super.setupAnim(tickDelta);
        if (this.beginAnimation != null) {
            this.beginAnimation.setupAnim(tickDelta);
        }
    }

    @Override
    public void tick() {
        super.tick();
        if (this.beginAnimation != null) {
            this.beginAnimation.tick();
        }
        this.time++;
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        if (this.calculateProgress(tickDelta) > 1.0F) {
            return super.get3DTransform(modelName, type, tickDelta, value0);
        } else {
            Vec3f animatedVec = super.get3DTransform(modelName, type, tickDelta, value0);
            float a = this.getAlpha(modelName, type, this.calculateProgress(tickDelta));
            Vec3f source = this.beginAnimation != null && this.beginAnimation.isActive() ? this.beginAnimation.get3DTransform(modelName, type, tickDelta, value0) : value0;
            return animatedVec.scale(a).add(source.scale(1.0F - a));
        }
    }

    protected float calculateProgress(float f) {
        float actualTime = (float) this.time + f;
        return actualTime / (float) this.length;
    }

    protected abstract float getAlpha(String var1, TransformType var2, float var3);

    public static AbstractFadeModifier standardFadeIn(int length, Ease ease) {
        return new AbstractFadeModifier(length) {

            @Override
            protected float getAlpha(String modelName, TransformType type, float progress) {
                return ease.invoke(progress);
            }
        };
    }

    public static AbstractFadeModifier functionalFadeIn(int length, AbstractFadeModifier.EasingFunction function) {
        return new AbstractFadeModifier(length) {

            @Override
            protected float getAlpha(String modelName, TransformType type, float progress) {
                return function.ease(modelName, type, progress);
            }
        };
    }

    public void setBeginAnimation(@Nullable IAnimation beginAnimation) {
        this.beginAnimation = beginAnimation;
    }

    @FunctionalInterface
    public interface EasingFunction {

        float ease(String var1, TransformType var2, float var3);
    }
}