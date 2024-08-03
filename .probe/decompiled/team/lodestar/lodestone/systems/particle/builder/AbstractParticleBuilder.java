package team.lodestar.lodestone.systems.particle.builder;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

public abstract class AbstractParticleBuilder<T extends SimpleParticleOptions> {

    double xMotion = 0.0;

    double yMotion = 0.0;

    double maxXSpeed = 0.0;

    double maxYSpeed = 0.0;

    double maxXOffset = 0.0;

    double maxYOffset = 0.0;

    public AbstractParticleBuilder<T> modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        dataConsumer.accept(this.getColorData());
        return this;
    }

    public AbstractParticleBuilder<T> setColorData(ColorParticleData colorData) {
        this.getParticleOptions().colorData = colorData;
        return this;
    }

    public ColorParticleData getColorData() {
        return this.getParticleOptions().colorData;
    }

    public AbstractParticleBuilder<T> setScaleData(GenericParticleData scaleData) {
        this.getParticleOptions().scaleData = scaleData;
        return this;
    }

    public GenericParticleData getScaleData() {
        return this.getParticleOptions().scaleData;
    }

    public AbstractParticleBuilder<T> setTransparencyData(GenericParticleData transparencyData) {
        this.getParticleOptions().transparencyData = transparencyData;
        return this;
    }

    public GenericParticleData getTransparencyData() {
        return this.getParticleOptions().transparencyData;
    }

    public AbstractParticleBuilder<T> setSpinData(SpinParticleData spinData) {
        this.getParticleOptions().spinData = spinData;
        return this;
    }

    public SpinParticleData getSpinData() {
        return this.getParticleOptions().spinData;
    }

    public AbstractParticleBuilder<T> multiplyGravity(float gravityMultiplier) {
        return this.modifyGravity(f -> () -> f * gravityMultiplier);
    }

    public AbstractParticleBuilder<T> modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        this.getParticleOptions().gravityStrengthSupplier = (Supplier<Float>) gravityReplacement.apply((Float) this.getParticleOptions().gravityStrengthSupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setGravityStrength(float gravity) {
        return this.setGravityStrength(() -> gravity);
    }

    public AbstractParticleBuilder<T> setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        this.getParticleOptions().gravityStrengthSupplier = gravityStrengthSupplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyFriction(float frictionMultiplier) {
        return this.modifyFriction(f -> () -> f * frictionMultiplier);
    }

    public AbstractParticleBuilder<T> modifyFriction(Function<Float, Supplier<Float>> frictionReplacement) {
        this.getParticleOptions().frictionStrengthSupplier = (Supplier<Float>) frictionReplacement.apply((Float) this.getParticleOptions().frictionStrengthSupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setFrictionStrength(float friction) {
        return this.setFrictionStrength(() -> friction);
    }

    public AbstractParticleBuilder<T> setFrictionStrength(Supplier<Float> frictionStrengthSupplier) {
        this.getParticleOptions().frictionStrengthSupplier = frictionStrengthSupplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifetime(float lifetimeMultiplier) {
        return this.modifyLifetime(i -> () -> (int) ((float) i.intValue() * lifetimeMultiplier));
    }

    public AbstractParticleBuilder<T> modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        this.getParticleOptions().lifetimeSupplier = (Supplier<Integer>) lifetimeReplacement.apply((Integer) this.getParticleOptions().lifetimeSupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setLifetime(int lifetime) {
        return this.setLifetime(() -> lifetime);
    }

    public AbstractParticleBuilder<T> setLifetime(Supplier<Integer> lifetimeSupplier) {
        this.getParticleOptions().lifetimeSupplier = lifetimeSupplier;
        return this;
    }

    public AbstractParticleBuilder<T> multiplyLifeDelay(float lifeDelayMultiplier) {
        return this.modifyLifeDelay(i -> () -> (int) ((float) i.intValue() * lifeDelayMultiplier));
    }

    public AbstractParticleBuilder<T> modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        this.getParticleOptions().lifeDelaySupplier = (Supplier<Integer>) lifeDelayReplacement.apply((Integer) this.getParticleOptions().lifeDelaySupplier.get());
        return this;
    }

    public AbstractParticleBuilder<T> setLifeDelay(int lifeDelay) {
        return this.setLifeDelay(() -> lifeDelay);
    }

    public AbstractParticleBuilder<T> setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        this.getParticleOptions().lifeDelaySupplier = lifeDelaySupplier;
        return this;
    }

    public AbstractParticleBuilder<T> setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        this.getParticleOptions().spritePicker = spritePicker;
        return this;
    }

    public AbstractParticleBuilder<T> setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        this.getParticleOptions().discardFunctionType = discardFunctionType;
        return this;
    }

    public abstract T getParticleOptions();
}