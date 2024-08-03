package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.GrowingPlantHeadBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class WeepingVinesFeature extends Feature<NoneFeatureConfiguration> {

    private static final Direction[] DIRECTIONS = Direction.values();

    public WeepingVinesFeature(Codec<NoneFeatureConfiguration> codecNoneFeatureConfiguration0) {
        super(codecNoneFeatureConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> featurePlaceContextNoneFeatureConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextNoneFeatureConfiguration0.level();
        BlockPos $$2 = featurePlaceContextNoneFeatureConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextNoneFeatureConfiguration0.random();
        if (!$$1.m_46859_($$2)) {
            return false;
        } else {
            BlockState $$4 = $$1.m_8055_($$2.above());
            if (!$$4.m_60713_(Blocks.NETHERRACK) && !$$4.m_60713_(Blocks.NETHER_WART_BLOCK)) {
                return false;
            } else {
                this.placeRoofNetherWart($$1, $$3, $$2);
                this.placeRoofWeepingVines($$1, $$3, $$2);
                return true;
            }
        }
    }

    private void placeRoofNetherWart(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2) {
        levelAccessor0.m_7731_(blockPos2, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos $$4 = new BlockPos.MutableBlockPos();
        for (int $$5 = 0; $$5 < 200; $$5++) {
            $$3.setWithOffset(blockPos2, randomSource1.nextInt(6) - randomSource1.nextInt(6), randomSource1.nextInt(2) - randomSource1.nextInt(5), randomSource1.nextInt(6) - randomSource1.nextInt(6));
            if (levelAccessor0.m_46859_($$3)) {
                int $$6 = 0;
                for (Direction $$7 : DIRECTIONS) {
                    BlockState $$8 = levelAccessor0.m_8055_($$4.setWithOffset($$3, $$7));
                    if ($$8.m_60713_(Blocks.NETHERRACK) || $$8.m_60713_(Blocks.NETHER_WART_BLOCK)) {
                        $$6++;
                    }
                    if ($$6 > 1) {
                        break;
                    }
                }
                if ($$6 == 1) {
                    levelAccessor0.m_7731_($$3, Blocks.NETHER_WART_BLOCK.defaultBlockState(), 2);
                }
            }
        }
    }

    private void placeRoofWeepingVines(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2) {
        BlockPos.MutableBlockPos $$3 = new BlockPos.MutableBlockPos();
        for (int $$4 = 0; $$4 < 100; $$4++) {
            $$3.setWithOffset(blockPos2, randomSource1.nextInt(8) - randomSource1.nextInt(8), randomSource1.nextInt(2) - randomSource1.nextInt(7), randomSource1.nextInt(8) - randomSource1.nextInt(8));
            if (levelAccessor0.m_46859_($$3)) {
                BlockState $$5 = levelAccessor0.m_8055_($$3.m_7494_());
                if ($$5.m_60713_(Blocks.NETHERRACK) || $$5.m_60713_(Blocks.NETHER_WART_BLOCK)) {
                    int $$6 = Mth.nextInt(randomSource1, 1, 8);
                    if (randomSource1.nextInt(6) == 0) {
                        $$6 *= 2;
                    }
                    if (randomSource1.nextInt(5) == 0) {
                        $$6 = 1;
                    }
                    int $$7 = 17;
                    int $$8 = 25;
                    placeWeepingVinesColumn(levelAccessor0, randomSource1, $$3, $$6, 17, 25);
                }
            }
        }
    }

    public static void placeWeepingVinesColumn(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos.MutableBlockPos blockPosMutableBlockPos2, int int3, int int4, int int5) {
        for (int $$6 = 0; $$6 <= int3; $$6++) {
            if (levelAccessor0.m_46859_(blockPosMutableBlockPos2)) {
                if ($$6 == int3 || !levelAccessor0.m_46859_(blockPosMutableBlockPos2.m_7495_())) {
                    levelAccessor0.m_7731_(blockPosMutableBlockPos2, (BlockState) Blocks.WEEPING_VINES.defaultBlockState().m_61124_(GrowingPlantHeadBlock.AGE, Mth.nextInt(randomSource1, int4, int5)), 2);
                    break;
                }
                levelAccessor0.m_7731_(blockPosMutableBlockPos2, Blocks.WEEPING_VINES_PLANT.defaultBlockState(), 2);
            }
            blockPosMutableBlockPos2.move(Direction.DOWN);
        }
    }
}