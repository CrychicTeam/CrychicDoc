package se.mickelus.tetra.client.particle;

import com.mojang.serialization.Codec;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.registries.ObjectHolder;

public class SweepingStrikeParticleType extends ParticleType<SweepingStrikeParticleOption> {

    public static final String identifier = "sweeping_strike";

    @ObjectHolder(registryName = "particle_type", value = "tetra:sweeping_strike")
    public static ParticleType<SweepingStrikeParticleOption> instance;

    public SweepingStrikeParticleType() {
        super(true, SweepingStrikeParticleOption.DESERIALIZER);
    }

    @Override
    public Codec<SweepingStrikeParticleOption> codec() {
        return SweepingStrikeParticleOption.CODEC;
    }
}