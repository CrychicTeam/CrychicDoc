package dev.kosmx.playerAnim.api.layered.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.api.layered.IAnimation;
import dev.kosmx.playerAnim.api.layered.KeyframeAnimationPlayer;
import dev.kosmx.playerAnim.core.util.Vec3f;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

public class AdjustmentModifier extends AbstractModifier {

    public boolean enabled = true;

    protected Function<String, Optional<AdjustmentModifier.PartModifier>> source;

    protected int instructedFadeout = 0;

    private int remainingFadeout = 0;

    public AdjustmentModifier(Function<String, Optional<AdjustmentModifier.PartModifier>> source) {
        this.source = source;
    }

    protected float getFadeIn(float delta) {
        float fadeIn = 1.0F;
        IAnimation animation = this.getAnim();
        if (animation instanceof KeyframeAnimationPlayer) {
            KeyframeAnimationPlayer player = (KeyframeAnimationPlayer) this.anim;
            float currentTick = (float) player.getTick() + delta;
            fadeIn = currentTick / (float) player.getData().beginTick;
            fadeIn = Math.min(fadeIn, 1.0F);
        }
        return fadeIn;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.remainingFadeout > 0) {
            this.remainingFadeout--;
            if (this.remainingFadeout <= 0) {
                this.instructedFadeout = 0;
            }
        }
    }

    public void fadeOut(int fadeOut) {
        this.instructedFadeout = fadeOut;
        this.remainingFadeout = fadeOut + 1;
    }

    protected float getFadeOut(float delta) {
        float fadeOut = 1.0F;
        if (this.remainingFadeout > 0 && this.instructedFadeout > 0) {
            float current = Math.max((float) this.remainingFadeout - delta, 0.0F);
            fadeOut = current / (float) this.instructedFadeout;
            return Math.min(fadeOut, 1.0F);
        } else {
            IAnimation animation = this.getAnim();
            if (animation instanceof KeyframeAnimationPlayer) {
                KeyframeAnimationPlayer player = (KeyframeAnimationPlayer) this.anim;
                float currentTick = (float) player.getTick() + delta;
                float position = -1.0F * (currentTick - (float) player.getData().stopTick);
                float length = (float) (player.getData().stopTick - player.getData().endTick);
                if (length > 0.0F) {
                    fadeOut = position / length;
                    fadeOut = Math.min(fadeOut, 1.0F);
                }
            }
            return fadeOut;
        }
    }

    @Override
    public Vec3f get3DTransform(String modelName, TransformType type, float tickDelta, Vec3f value0) {
        if (!this.enabled) {
            return super.get3DTransform(modelName, type, tickDelta, value0);
        } else {
            Optional<AdjustmentModifier.PartModifier> partModifier = (Optional<AdjustmentModifier.PartModifier>) this.source.apply(modelName);
            float fade = this.getFadeIn(tickDelta) * this.getFadeOut(tickDelta);
            if (partModifier.isPresent()) {
                Vec3f modifiedVector = super.get3DTransform(modelName, type, tickDelta, value0);
                return this.transformVector(modifiedVector, type, (AdjustmentModifier.PartModifier) partModifier.get(), fade);
            } else {
                return super.get3DTransform(modelName, type, tickDelta, value0);
            }
        }
    }

    protected Vec3f transformVector(Vec3f vector, TransformType type, AdjustmentModifier.PartModifier partModifier, float fade) {
        switch(type) {
            case POSITION:
                return vector.add(partModifier.offset().scale(fade));
            case ROTATION:
                return vector.add(partModifier.rotation().scale(fade));
            case BEND:
            default:
                return vector;
        }
    }

    public static final class PartModifier {

        private final Vec3f rotation;

        private final Vec3f offset;

        public PartModifier(Vec3f rotation, Vec3f offset) {
            this.rotation = rotation;
            this.offset = offset;
        }

        public Vec3f rotation() {
            return this.rotation;
        }

        public Vec3f offset() {
            return this.offset;
        }

        public boolean equals(Object obj) {
            if (obj == this) {
                return true;
            } else if (obj != null && obj.getClass() == this.getClass()) {
                AdjustmentModifier.PartModifier that = (AdjustmentModifier.PartModifier) obj;
                return Objects.equals(this.rotation, that.rotation) && Objects.equals(this.offset, that.offset);
            } else {
                return false;
            }
        }

        public int hashCode() {
            return Objects.hash(new Object[] { this.rotation, this.offset });
        }

        public String toString() {
            return "PartModifier[rotation=" + this.rotation + ", offset=" + this.offset + ']';
        }
    }
}