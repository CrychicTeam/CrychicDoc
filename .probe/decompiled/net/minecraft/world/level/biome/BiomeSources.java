package net.minecraft.world.level.biome;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;

public class BiomeSources {

    public static Codec<? extends BiomeSource> bootstrap(Registry<Codec<? extends BiomeSource>> registryCodecExtendsBiomeSource0) {
        Registry.register(registryCodecExtendsBiomeSource0, "fixed", FixedBiomeSource.CODEC);
        Registry.register(registryCodecExtendsBiomeSource0, "multi_noise", MultiNoiseBiomeSource.CODEC);
        Registry.register(registryCodecExtendsBiomeSource0, "checkerboard", CheckerboardColumnBiomeSource.CODEC);
        return Registry.register(registryCodecExtendsBiomeSource0, "the_end", TheEndBiomeSource.CODEC);
    }
}