package team.lodestar.lodestone.systems.particle.data.color;

import team.lodestar.lodestone.systems.easing.Easing;

public class ColorParticleDataBuilder {

    protected float r1;

    protected float g1;

    protected float b1;

    protected float r2;

    protected float g2;

    protected float b2;

    protected float colorCoefficient = 1.0F;

    protected Easing colorCurveEasing = Easing.LINEAR;

    protected ColorParticleDataBuilder(float r1, float g1, float b1, float r2, float g2, float b2) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
    }

    public ColorParticleDataBuilder setCoefficient(float coefficient) {
        this.colorCoefficient = coefficient;
        return this;
    }

    public ColorParticleDataBuilder setEasing(Easing easing) {
        this.colorCurveEasing = easing;
        return this;
    }

    public ColorParticleData build() {
        return new ColorParticleData(this.r1, this.g1, this.b1, this.r2, this.g2, this.b2, this.colorCoefficient, this.colorCurveEasing);
    }
}