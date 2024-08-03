package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.FluidState;

public class SpringConfiguration implements FeatureConfiguration {

    public static final Codec<SpringConfiguration> CODEC = RecordCodecBuilder.create(p_68139_ -> p_68139_.group(FluidState.CODEC.fieldOf("state").forGetter(p_161205_ -> p_161205_.state), Codec.BOOL.fieldOf("requires_block_below").orElse(true).forGetter(p_161203_ -> p_161203_.requiresBlockBelow), Codec.INT.fieldOf("rock_count").orElse(4).forGetter(p_161201_ -> p_161201_.rockCount), Codec.INT.fieldOf("hole_count").orElse(1).forGetter(p_161199_ -> p_161199_.holeCount), RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("valid_blocks").forGetter(p_204854_ -> p_204854_.validBlocks)).apply(p_68139_, SpringConfiguration::new));

    public final FluidState state;

    public final boolean requiresBlockBelow;

    public final int rockCount;

    public final int holeCount;

    public final HolderSet<Block> validBlocks;

    public SpringConfiguration(FluidState fluidState0, boolean boolean1, int int2, int int3, HolderSet<Block> holderSetBlock4) {
        this.state = fluidState0;
        this.requiresBlockBelow = boolean1;
        this.rockCount = int2;
        this.holeCount = int3;
        this.validBlocks = holderSetBlock4;
    }
}