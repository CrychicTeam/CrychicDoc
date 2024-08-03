package team.lodestar.lodestone.systems.particle.data.color;

import java.awt.Color;
import net.minecraft.util.Mth;
import team.lodestar.lodestone.systems.easing.Easing;

public class ColorParticleData {

    public final float r1;

    public final float g1;

    public final float b1;

    public final float r2;

    public final float g2;

    public final float b2;

    public final float colorCoefficient;

    public final Easing colorCurveEasing;

    public float coefficientMultiplier = 1.0F;

    protected ColorParticleData(float r1, float g1, float b1, float r2, float g2, float b2, float colorCoefficient, Easing colorCurveEasing) {
        this.r1 = r1;
        this.g1 = g1;
        this.b1 = b1;
        this.r2 = r2;
        this.g2 = g2;
        this.b2 = b2;
        this.colorCoefficient = colorCoefficient;
        this.colorCurveEasing = colorCurveEasing;
    }

    public ColorParticleData multiplyCoefficient(float coefficientMultiplier) {
        this.coefficientMultiplier *= coefficientMultiplier;
        return this;
    }

    public ColorParticleData overrideCoefficientMultiplier(float coefficientMultiplier) {
        this.coefficientMultiplier = coefficientMultiplier;
        return this;
    }

    public float getProgress(float age, float lifetime) {
        return Mth.clamp(age * this.colorCoefficient * this.coefficientMultiplier / lifetime, 0.0F, 1.0F);
    }

    public ColorParticleDataBuilder copy() {
        return create(this.r1, this.g1, this.b1, this.r2, this.g2, this.b2).setCoefficient(this.colorCoefficient).setEasing(this.colorCurveEasing);
    }

    public static ColorParticleDataBuilder create(float r1, float g1, float b1, float r2, float g2, float b2) {
        return new ColorParticleDataBuilder(r1, g1, b1, r2, g2, b2);
    }

    public static ColorParticleDataBuilder create(float r, float g, float b) {
        return new ColorParticleDataBuilder(r, g, b, r, g, b);
    }

    public static ColorParticleDataBuilder create(Color start, Color end) {
        return create((float) start.getRed() / 255.0F, (float) start.getGreen() / 255.0F, (float) start.getBlue() / 255.0F, (float) end.getRed() / 255.0F, (float) end.getGreen() / 255.0F, (float) end.getBlue() / 255.0F);
    }

    public static ColorParticleDataBuilder create(Color color) {
        return create((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F);
    }
}