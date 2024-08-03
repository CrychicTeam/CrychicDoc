package team.lodestar.lodestone.systems.particle.data.spin;

import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;

public class SpinParticleData extends GenericParticleData {

    public final float spinOffset;

    protected SpinParticleData(float spinOffset, float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        super(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
        this.spinOffset = spinOffset;
    }

    public SpinParticleData copy() {
        return new SpinParticleData(this.spinOffset, this.startingValue, this.middleValue, this.endingValue, this.coefficient, this.startToMiddleEasing, this.middleToEndEasing).overrideValueMultiplier(this.valueMultiplier).overrideCoefficientMultiplier(this.coefficientMultiplier);
    }

    public SpinParticleData bake() {
        return new SpinParticleData(this.spinOffset, this.startingValue * this.valueMultiplier, this.middleValue * this.valueMultiplier, this.endingValue * this.valueMultiplier, this.coefficient * this.coefficientMultiplier, this.startToMiddleEasing, this.middleToEndEasing);
    }

    public SpinParticleData overrideValueMultiplier(float valueMultiplier) {
        return (SpinParticleData) super.overrideValueMultiplier(valueMultiplier);
    }

    public SpinParticleData overrideCoefficientMultiplier(float coefficientMultiplier) {
        return (SpinParticleData) super.overrideCoefficientMultiplier(coefficientMultiplier);
    }

    public static SpinParticleDataBuilder create(float value) {
        return new SpinParticleDataBuilder(value, value, -1.0F);
    }

    public static SpinParticleDataBuilder create(float startingValue, float endingValue) {
        return new SpinParticleDataBuilder(startingValue, endingValue, -1.0F);
    }

    public static SpinParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
        return new SpinParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float value) {
        value *= random.nextBoolean() ? 1.0F : -1.0F;
        return new SpinParticleDataBuilder(value, value, -1.0F);
    }

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float startingValue, float endingValue) {
        int direction = random.nextBoolean() ? 1 : -1;
        startingValue *= (float) direction;
        endingValue *= (float) direction;
        return new SpinParticleDataBuilder(startingValue, endingValue, -1.0F);
    }

    public static SpinParticleDataBuilder createRandomDirection(RandomSource random, float startingValue, float middleValue, float endingValue) {
        int direction = random.nextBoolean() ? 1 : -1;
        startingValue *= (float) direction;
        middleValue *= (float) direction;
        endingValue *= (float) direction;
        return new SpinParticleDataBuilder(startingValue, middleValue, endingValue);
    }
}