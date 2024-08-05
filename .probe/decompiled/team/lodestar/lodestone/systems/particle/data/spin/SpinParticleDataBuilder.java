package team.lodestar.lodestone.systems.particle.data.spin;

import net.minecraft.util.RandomSource;
import team.lodestar.lodestone.systems.easing.Easing;
import team.lodestar.lodestone.systems.particle.data.GenericParticleDataBuilder;

public class SpinParticleDataBuilder extends GenericParticleDataBuilder {

    protected float spinOffset;

    protected SpinParticleDataBuilder(float startingValue, float middleValue, float endingValue) {
        super(startingValue, middleValue, endingValue);
    }

    public SpinParticleDataBuilder setSpinOffset(float spinOffset) {
        this.spinOffset = spinOffset;
        return this;
    }

    public SpinParticleDataBuilder randomSpinOffset(RandomSource random) {
        this.spinOffset = random.nextFloat() * 6.28F;
        return this;
    }

    public SpinParticleDataBuilder setCoefficient(float coefficient) {
        return (SpinParticleDataBuilder) super.setCoefficient(coefficient);
    }

    public SpinParticleDataBuilder setEasing(Easing easing) {
        return (SpinParticleDataBuilder) super.setEasing(easing);
    }

    public SpinParticleDataBuilder setEasing(Easing easing, Easing middleToEndEasing) {
        return (SpinParticleDataBuilder) super.setEasing(easing, middleToEndEasing);
    }

    public SpinParticleData build() {
        return new SpinParticleData(this.spinOffset, this.startingValue, this.middleValue, this.endingValue, this.coefficient, this.startToMiddleEasing, this.middleToEndEasing);
    }
}