package team.lodestar.lodestone.systems.particle.data;

import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class GenericParticleData {

    public final float startingValue;

    public final float middleValue;

    public final float endingValue;

    public final float coefficient;

    public final Easing startToMiddleEasing;

    public final Easing middleToEndEasing;

    public float valueMultiplier = 1.0F;

    public float coefficientMultiplier = 1.0F;

    protected GenericParticleData(float startingValue, float middleValue, float endingValue, float coefficient, Easing startToMiddleEasing, Easing middleToEndEasing) {
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
        this.coefficient = coefficient;
        this.startToMiddleEasing = startToMiddleEasing;
        this.middleToEndEasing = middleToEndEasing;
    }

    public GenericParticleData copy() {
        return new GenericParticleData(this.startingValue, this.middleValue, this.endingValue, this.coefficient, this.startToMiddleEasing, this.middleToEndEasing).overrideValueMultiplier(this.valueMultiplier).overrideCoefficientMultiplier(this.coefficientMultiplier);
    }

    public GenericParticleData bake() {
        return new GenericParticleData(this.startingValue * this.valueMultiplier, this.middleValue * this.valueMultiplier, this.endingValue * this.valueMultiplier, this.coefficient * this.coefficientMultiplier, this.startToMiddleEasing, this.middleToEndEasing);
    }

    public GenericParticleData multiplyCoefficient(float coefficientMultiplier) {
        this.coefficientMultiplier *= coefficientMultiplier;
        return this;
    }

    public GenericParticleData multiplyValue(float valueMultiplier) {
        this.valueMultiplier *= valueMultiplier;
        return this;
    }

    public GenericParticleData overrideCoefficientMultiplier(float coefficientMultiplier) {
        this.coefficientMultiplier = coefficientMultiplier;
        return this;
    }

    public GenericParticleData overrideValueMultiplier(float valueMultiplier) {
        this.valueMultiplier = valueMultiplier;
        return this;
    }

    public boolean isTrinary() {
        return this.endingValue != -1.0F;
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp(age * this.coefficient * this.coefficientMultiplier / lifetime, 0.0F, 1.0F);
    }

    public float getValue(float age, float lifetime) {
        float progress = this.getProgress(age, lifetime);
        float result;
        if (this.isTrinary()) {
            if (progress >= 0.5F) {
                result = Mth.lerp(this.middleToEndEasing.ease(progress - 0.5F, 0.0F, 1.0F, 0.5F), this.middleValue, this.endingValue);
            } else {
                result = Mth.lerp(this.startToMiddleEasing.ease(progress, 0.0F, 1.0F, 0.5F), this.startingValue, this.middleValue);
            }
        } else {
            result = Mth.lerp(this.startToMiddleEasing.ease(progress, 0.0F, 1.0F, 1.0F), this.startingValue, this.middleValue);
        }
        return result * this.valueMultiplier;
    }

    public static GenericParticleDataBuilder create(float value) {
        return new GenericParticleDataBuilder(value, value, -1.0F);
    }

    public static GenericParticleDataBuilder create(float startingValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, endingValue, -1.0F);
    }

    public static GenericParticleDataBuilder create(float startingValue, float middleValue, float endingValue) {
        return new GenericParticleDataBuilder(startingValue, middleValue, endingValue);
    }

    public static GenericParticleData constrictTransparency(GenericParticleData data) {
        float startingValue = Mth.clamp(data.startingValue, 0.0F, 1.0F);
        float middleValue = Mth.clamp(data.middleValue, 0.0F, 1.0F);
        float endingValue = data.endingValue == -1.0F ? -1.0F : Mth.clamp(data.endingValue, 0.0F, 1.0F);
        float coefficient = data.coefficient;
        Easing startToMiddleEasing = data.startToMiddleEasing;
        Easing middleToEndEasing = data.middleToEndEasing;
        return new GenericParticleData(startingValue, middleValue, endingValue, coefficient, startToMiddleEasing, middleToEndEasing);
    }
}