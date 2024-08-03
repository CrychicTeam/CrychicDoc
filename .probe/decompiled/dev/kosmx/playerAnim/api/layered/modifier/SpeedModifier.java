package dev.kosmx.playerAnim.api.layered.modifier;

import dev.kosmx.playerAnim.api.TransformType;
import dev.kosmx.playerAnim.core.util.Vec3f;
import org.jetbrains.annotations.NotNull;

public class SpeedModifier extends AbstractModifier {

    public float speed = 1.0F;

    private float delta = 0.0F;

    private float shiftedDelta = 0.0F;

    public SpeedModifier(float speed) {
        if (!Float.isFinite(speed)) {
            throw new IllegalArgumentException("Speed must be a finite number");
        } else {
            this.speed = speed;
        }
    }

    @Override
    public void tick() {
        float delta = 1.0F - this.delta;
        this.delta = 0.0F;
        this.step(delta);
    }

    @Override
    public void setupAnim(float tickDelta) {
        float delta = tickDelta - this.delta;
        this.delta = tickDelta;
        this.step(delta);
    }

    protected void step(float delta) {
        delta *= this.speed;
        delta += this.shiftedDelta;
        while (delta > 1.0F) {
            delta--;
            super.tick();
        }
        super.setupAnim(delta);
        this.shiftedDelta = delta;
    }

    @NotNull
    @Override
    public Vec3f get3DTransform(@NotNull String modelName, @NotNull TransformType type, float tickDelta, @NotNull Vec3f value0) {
        return super.get3DTransform(modelName, type, this.shiftedDelta, value0);
    }

    public SpeedModifier() {
    }
}