package team.lodestar.lodestone.systems.particle;

import java.awt.Color;
import java.util.function.Supplier;
import team.lodestar.lodestone.systems.particle.data.GenericParticleData;
import team.lodestar.lodestone.systems.particle.data.color.ColorParticleData;
import team.lodestar.lodestone.systems.particle.data.spin.SpinParticleData;

public abstract class SimpleParticleOptions {

    public static final ColorParticleData DEFAULT_COLOR = ColorParticleData.create(Color.WHITE, Color.WHITE).build();

    public static final SpinParticleData DEFAULT_SPIN = SpinParticleData.create(0.0F).build();

    public static final GenericParticleData DEFAULT_GENERIC = GenericParticleData.create(1.0F, 0.0F).build();

    public SimpleParticleOptions.ParticleSpritePicker spritePicker = SimpleParticleOptions.ParticleSpritePicker.FIRST_INDEX;

    public SimpleParticleOptions.ParticleDiscardFunctionType discardFunctionType = SimpleParticleOptions.ParticleDiscardFunctionType.INVISIBLE;

    public ColorParticleData colorData = DEFAULT_COLOR;

    public GenericParticleData transparencyData = DEFAULT_GENERIC;

    public GenericParticleData scaleData = DEFAULT_GENERIC;

    public SpinParticleData spinData = DEFAULT_SPIN;

    public Supplier<Integer> lifetimeSupplier = () -> 20;

    public Supplier<Integer> lifeDelaySupplier = () -> 0;

    public Supplier<Float> gravityStrengthSupplier = () -> 0.0F;

    public Supplier<Float> frictionStrengthSupplier = () -> 1.0F;

    public static enum ParticleDiscardFunctionType {

        NONE, INVISIBLE, ENDING_CURVE_INVISIBLE
    }

    public static enum ParticleSpritePicker {

        FIRST_INDEX, LAST_INDEX, WITH_AGE, RANDOM_SPRITE
    }
}