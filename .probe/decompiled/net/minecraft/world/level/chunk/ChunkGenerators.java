package net.minecraft.world.level.chunk;

import com.mojang.serialization.Codec;
import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.DebugLevelSource;
import net.minecraft.world.level.levelgen.FlatLevelSource;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;

public class ChunkGenerators {

    public static Codec<? extends ChunkGenerator> bootstrap(Registry<Codec<? extends ChunkGenerator>> registryCodecExtendsChunkGenerator0) {
        Registry.register(registryCodecExtendsChunkGenerator0, "noise", NoiseBasedChunkGenerator.CODEC);
        Registry.register(registryCodecExtendsChunkGenerator0, "flat", FlatLevelSource.CODEC);
        return Registry.register(registryCodecExtendsChunkGenerator0, "debug", DebugLevelSource.CODEC);
    }
}