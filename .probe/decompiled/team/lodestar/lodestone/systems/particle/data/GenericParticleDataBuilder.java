package team.lodestar.lodestone.systems.particle.data;

import team.lodestar.lodestone.systems.easing.Easing;

public class GenericParticleDataBuilder {

    protected float startingValue;

    protected float middleValue;

    protected float endingValue;

    protected float coefficient = 1.0F;

    protected Easing startToMiddleEasing = Easing.LINEAR;

    protected Easing middleToEndEasing = Easing.LINEAR;

    protected GenericParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
        this.startingValue = startingValue;
        this.middleValue = middleValue;
        this.endingValue = endingValue;
    }

    public GenericParticleDataBuilder setCoefficient(float coefficient) {
        this.coefficient = coefficient;
        return this;
    }

    public GenericParticleDataBuilder setEasing(Easing easing) {
        this.startToMiddleEasing = easing;
        return this;
    }

    public GenericParticleDataBuilder setEasing(Easing easing, Easing middleToEndEasing) {
        this.startToMiddleEasing = easing;
        this.middleToEndEasing = easing;
        return this;
    }

    public GenericParticleData build() {
        return new GenericParticleData(this.startingValue, this.middleValue, this.endingValue, this.coefficient, this.startToMiddleEasing, this.middleToEndEasing);
    }
}