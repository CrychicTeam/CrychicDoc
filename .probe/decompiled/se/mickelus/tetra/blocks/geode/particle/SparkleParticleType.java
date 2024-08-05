package se.mickelus.tetra.blocks.geode.particle;

import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.registries.ObjectHolder;

public class SparkleParticleType {

    public static final String identifier = "sparkle";

    @ObjectHolder(registryName = "particle_type", value = "tetra:sparkle")
    public static SimpleParticleType instance;
}