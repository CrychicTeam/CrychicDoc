package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.Optional;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.BaseCoralWallFanBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SeaPickleBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public abstract class CoralFeature extends Feature<NoneFeatureConfiguration> {

    public CoralFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        RandomSource $$1 = featurePlaceContextNoneFeatureConfiguration0.random();
        WorldGenLevel $$2 = featurePlaceContextNoneFeatureConfiguration0.level();
        BlockPos $$3 = featurePlaceContextNoneFeatureConfiguration0.origin();
        Optional<Block> $$4 = BuiltInRegistries.BLOCK.m_203431_(BlockTags.CORAL_BLOCKS).flatMap(p_224980_ -> p_224980_.m_213653_($$1)).map(Holder::m_203334_);
        return $$4.isEmpty() ? false : this.placeFeature($$2, $$1, $$3, ((Block) $$4.get()).defaultBlockState());
    }

    protected abstract boolean placeFeature(LevelAccessor var1, RandomSource var2, BlockPos var3, BlockState var4);

    protected boolean placeCoralBlock(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3) {
        BlockPos $$4 = blockPos2.above();
        BlockState $$5 = levelAccessor0.m_8055_(blockPos2);
        if (($$5.m_60713_(Blocks.WATER) || $$5.m_204336_(BlockTags.CORALS)) && levelAccessor0.m_8055_($$4).m_60713_(Blocks.WATER)) {
            levelAccessor0.m_7731_(blockPos2, blockState3, 3);
            if (randomSource1.nextFloat() < 0.25F) {
                BuiltInRegistries.BLOCK.m_203431_(BlockTags.CORALS).flatMap(p_224972_ -> p_224972_.m_213653_(randomSource1)).map(Holder::m_203334_).ifPresent(p_204720_ -> levelAccessor0.m_7731_($$4, p_204720_.defaultBlockState(), 2));
            } else if (randomSource1.nextFloat() < 0.05F) {
                levelAccessor0.m_7731_($$4, (BlockState) Blocks.SEA_PICKLE.defaultBlockState().m_61124_(SeaPickleBlock.PICKLES, randomSource1.nextInt(4) + 1), 2);
            }
            for (Direction $$6 : Direction.Plane.HORIZONTAL) {
                if (randomSource1.nextFloat() < 0.2F) {
                    BlockPos $$7 = blockPos2.relative($$6);
                    if (levelAccessor0.m_8055_($$7).m_60713_(Blocks.WATER)) {
                        BuiltInRegistries.BLOCK.m_203431_(BlockTags.WALL_CORALS).flatMap(p_224965_ -> p_224965_.m_213653_(randomSource1)).map(Holder::m_203334_).ifPresent(p_204725_ -> {
                            BlockState $$4x = p_204725_.defaultBlockState();
                            if ($$4x.m_61138_(BaseCoralWallFanBlock.FACING)) {
                                $$4x = (BlockState) $$4x.m_61124_(BaseCoralWallFanBlock.FACING, $$6);
                            }
                            levelAccessor0.m_7731_($$7, $$4x, 2);
                        });
                    }
                }
            }
            return true;
        } else {
            return false;
        }
    }
}