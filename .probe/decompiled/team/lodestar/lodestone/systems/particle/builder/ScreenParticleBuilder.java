package team.lodestar.lodestone.systems.particle.builder;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import team.lodestar.lodestone.handlers.screenparticle.ScreenParticleHandler;
import team.lodestar.lodestone.systems.particle.SimpleParticleOptions;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;
import team.lodestar.lodestone.systems.particle.render_types.LodestoneScreenParticleRenderType;
import team.lodestar.lodestone.systems.particle.screen.GenericScreenParticle;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleHolder;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleOptions;
import team.lodestar.lodestone.systems.particle.screen.ScreenParticleType;

public class ScreenParticleBuilder extends AbstractParticleBuilder<ScreenParticleOptions> {

    private static final Random RANDOM = new Random();

    final ScreenParticleType<?> type;

    final ScreenParticleOptions options;

    final ScreenParticleHolder target;

    public static ScreenParticleBuilder create(ScreenParticleType<?> type, ScreenParticleHolder target) {
        return new ScreenParticleBuilder(type, target);
    }

    protected ScreenParticleBuilder(ScreenParticleType<?> type, ScreenParticleHolder target) {
        this.type = type;
        this.options = new ScreenParticleOptions(type);
        this.target = target;
    }

    public ScreenParticleOptions getParticleOptions() {
        return this.options;
    }

    public ScreenParticleBuilder modifyData(Supplier<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept((GenericParticleData) dataType.get());
        return this;
    }

    public ScreenParticleBuilder modifyData(Optional<GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataType.ifPresent(dataConsumer);
        return this;
    }

    public ScreenParticleBuilder modifyData(Function<ScreenParticleBuilder, GenericParticleData> dataType, Consumer<GenericParticleData> dataConsumer) {
        dataConsumer.accept((GenericParticleData) dataType.apply(this));
        return this;
    }

    public ScreenParticleBuilder modifyDataOptional(Function<ScreenParticleBuilder, Optional<GenericParticleData>> dataType, Consumer<GenericParticleData> dataConsumer) {
        return this.modifyData((Optional<GenericParticleData>) dataType.apply(this), dataConsumer);
    }

    public final ScreenParticleBuilder modifyData(Collection<Supplier<GenericParticleData>> dataTypes, Consumer<GenericParticleData> dataConsumer) {
        for (Supplier<GenericParticleData> dataFunction : dataTypes) {
            dataConsumer.accept((GenericParticleData) dataFunction.get());
        }
        return this;
    }

    public ScreenParticleBuilder setDiscardFunction(SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType) {
        this.options.discardFunctionType = discardFunctionType;
        return this;
    }

    public ScreenParticleBuilder setSpritePicker(SimpleParticleOptions.ParticleSpritePicker spritePicker) {
        this.options.spritePicker = spritePicker;
        return this;
    }

    public ScreenParticleBuilder setRenderType(LodestoneScreenParticleRenderType renderType) {
        this.options.renderType = renderType;
        return this;
    }

    public ScreenParticleBuilder setRandomMotion(double maxSpeed) {
        return this.setRandomMotion(maxSpeed, maxSpeed);
    }

    public ScreenParticleBuilder setRandomMotion(double maxXSpeed, double maxYSpeed) {
        this.maxXSpeed = maxXSpeed;
        this.maxYSpeed = maxYSpeed;
        return this;
    }

    public ScreenParticleBuilder addMotion(double vx, double vy) {
        this.xMotion += vx;
        this.yMotion += vy;
        return this;
    }

    public ScreenParticleBuilder setMotion(double vx, double vy) {
        this.xMotion = vx;
        this.yMotion = vy;
        return this;
    }

    public ScreenParticleBuilder setRandomOffset(double maxDistance) {
        return this.setRandomOffset(maxDistance, maxDistance);
    }

    public ScreenParticleBuilder setRandomOffset(double maxXDist, double maxYDist) {
        this.maxXOffset = maxXDist;
        this.maxYOffset = maxYDist;
        return this;
    }

    public ScreenParticleBuilder act(Consumer<ScreenParticleBuilder> particleBuilderConsumer) {
        particleBuilderConsumer.accept(this);
        return this;
    }

    public ScreenParticleBuilder addActor(Consumer<GenericScreenParticle> particleActor) {
        this.options.actor = particleActor;
        return this;
    }

    public ScreenParticleBuilder spawn(double x, double y) {
        double yaw = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xSpeed = (double) RANDOM.nextFloat() * this.maxXSpeed;
        double ySpeed = (double) RANDOM.nextFloat() * this.maxYSpeed;
        this.xMotion = this.xMotion + Math.sin(yaw) * Math.cos(pitch) * xSpeed;
        this.yMotion = this.yMotion + Math.sin(pitch) * ySpeed;
        double yaw2 = (double) RANDOM.nextFloat() * Math.PI * 2.0;
        double pitch2 = (double) RANDOM.nextFloat() * Math.PI - (Math.PI / 2);
        double xDist = (double) RANDOM.nextFloat() * this.maxXOffset;
        double yDist = (double) RANDOM.nextFloat() * this.maxYOffset;
        double xPos = Math.sin(yaw2) * Math.cos(pitch2) * xDist;
        double yPos = Math.sin(pitch2) * yDist;
        ScreenParticleHandler.addParticle(this.target, this.options, x + xPos, y + yPos, this.xMotion, this.yMotion);
        return this;
    }

    public ScreenParticleBuilder repeat(double x, double y, int n) {
        for (int i = 0; i < n; i++) {
            this.spawn(x, y);
        }
        return this;
    }

    public ScreenParticleBuilder spawnOnStack(double xOffset, double yOffset) {
        this.options.tracksStack = true;
        this.options.stackTrackXOffset = xOffset;
        this.options.stackTrackYOffset = yOffset;
        this.spawn((double) ScreenParticleHandler.currentItemX + xOffset, (double) ScreenParticleHandler.currentItemY + yOffset);
        return this;
    }

    public ScreenParticleBuilder repeatOnStack(double xOffset, double yOffset, int n) {
        for (int i = 0; i < n; i++) {
            this.spawnOnStack(xOffset, yOffset);
        }
        return this;
    }

    public ScreenParticleBuilder modifyColorData(Consumer<ColorParticleData> dataConsumer) {
        return (ScreenParticleBuilder) super.modifyColorData(dataConsumer);
    }

    public ScreenParticleBuilder setColorData(ColorParticleData colorData) {
        return (ScreenParticleBuilder) super.setColorData(colorData);
    }

    public ScreenParticleBuilder setScaleData(GenericParticleData scaleData) {
        return (ScreenParticleBuilder) super.setScaleData(scaleData);
    }

    public ScreenParticleBuilder setTransparencyData(GenericParticleData transparencyData) {
        return (ScreenParticleBuilder) super.setTransparencyData(transparencyData);
    }

    public ScreenParticleBuilder setSpinData(SpinParticleData spinData) {
        return (ScreenParticleBuilder) super.setSpinData(spinData);
    }

    public ScreenParticleBuilder multiplyGravity(float gravityMultiplier) {
        return (ScreenParticleBuilder) super.multiplyGravity(gravityMultiplier);
    }

    public ScreenParticleBuilder modifyGravity(Function<Float, Supplier<Float>> gravityReplacement) {
        return (ScreenParticleBuilder) super.modifyGravity(gravityReplacement);
    }

    public ScreenParticleBuilder setGravityStrength(float gravity) {
        return (ScreenParticleBuilder) super.setGravityStrength(gravity);
    }

    public ScreenParticleBuilder setGravityStrength(Supplier<Float> gravityStrengthSupplier) {
        return (ScreenParticleBuilder) super.setGravityStrength(gravityStrengthSupplier);
    }

    public ScreenParticleBuilder multiplyLifetime(float lifetimeMultiplier) {
        return (ScreenParticleBuilder) super.multiplyLifetime(lifetimeMultiplier);
    }

    public ScreenParticleBuilder modifyLifetime(Function<Integer, Supplier<Integer>> lifetimeReplacement) {
        return (ScreenParticleBuilder) super.modifyLifetime(lifetimeReplacement);
    }

    public ScreenParticleBuilder setLifetime(int lifetime) {
        return (ScreenParticleBuilder) super.setLifetime(lifetime);
    }

    public ScreenParticleBuilder setLifetime(Supplier<Integer> lifetimeSupplier) {
        return (ScreenParticleBuilder) super.setLifetime(lifetimeSupplier);
    }

    public ScreenParticleBuilder multiplyLifeDelay(float lifeDelayMultiplier) {
        return (ScreenParticleBuilder) super.multiplyLifeDelay(lifeDelayMultiplier);
    }

    public ScreenParticleBuilder modifyLifeDelay(Function<Integer, Supplier<Integer>> lifeDelayReplacement) {
        return (ScreenParticleBuilder) super.modifyLifeDelay(lifeDelayReplacement);
    }

    public ScreenParticleBuilder setLifeDelay(int lifeDelay) {
        return (ScreenParticleBuilder) super.setLifeDelay(lifeDelay);
    }

    public ScreenParticleBuilder setLifeDelay(Supplier<Integer> lifeDelaySupplier) {
        return (ScreenParticleBuilder) super.setLifeDelay(lifeDelaySupplier);
    }
}