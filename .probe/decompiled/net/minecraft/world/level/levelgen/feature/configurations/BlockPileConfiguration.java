package net.minecraft.world.level.levelgen.feature.configurations;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

public class BlockPileConfiguration implements FeatureConfiguration {

    public static final Codec<BlockPileConfiguration> CODEC = BlockStateProvider.CODEC.fieldOf("state_provider").xmap(BlockPileConfiguration::new, p_67545_ -> p_67545_.stateProvider).codec();

    public final BlockStateProvider stateProvider;

    public BlockPileConfiguration(BlockStateProvider blockStateProvider0) {
        this.stateProvider = blockStateProvider0;
    }
}