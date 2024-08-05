package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;

public class HugeFungusFeature extends Feature<HugeFungusConfiguration> {

    private static final float HUGE_PROBABILITY = 0.06F;

    public HugeFungusFeature(Codec<HugeFungusConfiguration> codecHugeFungusConfiguration0) {
        super(codecHugeFungusConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<HugeFungusConfiguration> featurePlaceContextHugeFungusConfiguration0) {
        WorldGenLevel $$1 = featurePlaceContextHugeFungusConfiguration0.level();
        BlockPos $$2 = featurePlaceContextHugeFungusConfiguration0.origin();
        RandomSource $$3 = featurePlaceContextHugeFungusConfiguration0.random();
        ChunkGenerator $$4 = featurePlaceContextHugeFungusConfiguration0.chunkGenerator();
        HugeFungusConfiguration $$5 = featurePlaceContextHugeFungusConfiguration0.config();
        Block $$6 = $$5.validBaseState.m_60734_();
        BlockPos $$7 = null;
        BlockState $$8 = $$1.m_8055_($$2.below());
        if ($$8.m_60713_($$6)) {
            $$7 = $$2;
        }
        if ($$7 == null) {
            return false;
        } else {
            int $$9 = Mth.nextInt($$3, 4, 13);
            if ($$3.nextInt(12) == 0) {
                $$9 *= 2;
            }
            if (!$$5.planted) {
                int $$10 = $$4.getGenDepth();
                if ($$7.m_123342_() + $$9 + 1 >= $$10) {
                    return false;
                }
            }
            boolean $$11 = !$$5.planted && $$3.nextFloat() < 0.06F;
            $$1.m_7731_($$2, Blocks.AIR.defaultBlockState(), 4);
            this.placeStem($$1, $$3, $$5, $$7, $$9, $$11);
            this.placeHat($$1, $$3, $$5, $$7, $$9, $$11);
            return true;
        }
    }

    private static boolean isReplaceable(WorldGenLevel worldGenLevel0, BlockPos blockPos1, HugeFungusConfiguration hugeFungusConfiguration2, boolean boolean3) {
        if (worldGenLevel0.m_7433_(blockPos1, BlockBehaviour.BlockStateBase::m_247087_)) {
            return true;
        } else {
            return boolean3 ? hugeFungusConfiguration2.replaceableBlocks.test(worldGenLevel0, blockPos1) : false;
        }
    }

    private void placeStem(WorldGenLevel worldGenLevel0, RandomSource randomSource1, HugeFungusConfiguration hugeFungusConfiguration2, BlockPos blockPos3, int int4, boolean boolean5) {
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        BlockState $$7 = hugeFungusConfiguration2.stemState;
        int $$8 = boolean5 ? 1 : 0;
        for (int $$9 = -$$8; $$9 <= $$8; $$9++) {
            for (int $$10 = -$$8; $$10 <= $$8; $$10++) {
                boolean $$11 = boolean5 && Mth.abs($$9) == $$8 && Mth.abs($$10) == $$8;
                for (int $$12 = 0; $$12 < int4; $$12++) {
                    $$6.setWithOffset(blockPos3, $$9, $$12, $$10);
                    if (isReplaceable(worldGenLevel0, $$6, hugeFungusConfiguration2, true)) {
                        if (hugeFungusConfiguration2.planted) {
                            if (!worldGenLevel0.m_8055_($$6.m_7495_()).m_60795_()) {
                                worldGenLevel0.m_46961_($$6, true);
                            }
                            worldGenLevel0.m_7731_($$6, $$7, 3);
                        } else if ($$11) {
                            if (randomSource1.nextFloat() < 0.1F) {
                                this.m_5974_(worldGenLevel0, $$6, $$7);
                            }
                        } else {
                            this.m_5974_(worldGenLevel0, $$6, $$7);
                        }
                    }
                }
            }
        }
    }

    private void placeHat(WorldGenLevel worldGenLevel0, RandomSource randomSource1, HugeFungusConfiguration hugeFungusConfiguration2, BlockPos blockPos3, int int4, boolean boolean5) {
        BlockPos.MutableBlockPos $$6 = new BlockPos.MutableBlockPos();
        boolean $$7 = hugeFungusConfiguration2.hatState.m_60713_(Blocks.NETHER_WART_BLOCK);
        int $$8 = Math.min(randomSource1.nextInt(1 + int4 / 3) + 5, int4);
        int $$9 = int4 - $$8;
        for (int $$10 = $$9; $$10 <= int4; $$10++) {
            int $$11 = $$10 < int4 - randomSource1.nextInt(3) ? 2 : 1;
            if ($$8 > 8 && $$10 < $$9 + 4) {
                $$11 = 3;
            }
            if (boolean5) {
                $$11++;
            }
            for (int $$12 = -$$11; $$12 <= $$11; $$12++) {
                for (int $$13 = -$$11; $$13 <= $$11; $$13++) {
                    boolean $$14 = $$12 == -$$11 || $$12 == $$11;
                    boolean $$15 = $$13 == -$$11 || $$13 == $$11;
                    boolean $$16 = !$$14 && !$$15 && $$10 != int4;
                    boolean $$17 = $$14 && $$15;
                    boolean $$18 = $$10 < $$9 + 3;
                    $$6.setWithOffset(blockPos3, $$12, $$10, $$13);
                    if (isReplaceable(worldGenLevel0, $$6, hugeFungusConfiguration2, false)) {
                        if (hugeFungusConfiguration2.planted && !worldGenLevel0.m_8055_($$6.m_7495_()).m_60795_()) {
                            worldGenLevel0.m_46961_($$6, true);
                        }
                        if ($$18) {
                            if (!$$16) {
                                this.placeHatDropBlock(worldGenLevel0, randomSource1, $$6, hugeFungusConfiguration2.hatState, $$7);
                            }
                        } else if ($$16) {
                            this.placeHatBlock(worldGenLevel0, randomSource1, hugeFungusConfiguration2, $$6, 0.1F, 0.2F, $$7 ? 0.1F : 0.0F);
                        } else if ($$17) {
                            this.placeHatBlock(worldGenLevel0, randomSource1, hugeFungusConfiguration2, $$6, 0.01F, 0.7F, $$7 ? 0.083F : 0.0F);
                        } else {
                            this.placeHatBlock(worldGenLevel0, randomSource1, hugeFungusConfiguration2, $$6, 5.0E-4F, 0.98F, $$7 ? 0.07F : 0.0F);
                        }
                    }
                }
            }
        }
    }

    private void placeHatBlock(LevelAccessor levelAccessor0, RandomSource randomSource1, HugeFungusConfiguration hugeFungusConfiguration2, BlockPos.MutableBlockPos blockPosMutableBlockPos3, float float4, float float5, float float6) {
        if (randomSource1.nextFloat() < float4) {
            this.m_5974_(levelAccessor0, blockPosMutableBlockPos3, hugeFungusConfiguration2.decorState);
        } else if (randomSource1.nextFloat() < float5) {
            this.m_5974_(levelAccessor0, blockPosMutableBlockPos3, hugeFungusConfiguration2.hatState);
            if (randomSource1.nextFloat() < float6) {
                tryPlaceWeepingVines(blockPosMutableBlockPos3, levelAccessor0, randomSource1);
            }
        }
    }

    private void placeHatDropBlock(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (levelAccessor0.m_8055_(blockPos2.below()).m_60713_(blockState3.m_60734_())) {
            this.m_5974_(levelAccessor0, blockPos2, blockState3);
        } else if ((double) randomSource1.nextFloat() < 0.15) {
            this.m_5974_(levelAccessor0, blockPos2, blockState3);
            if (boolean4 && randomSource1.nextInt(11) == 0) {
                tryPlaceWeepingVines(blockPos2, levelAccessor0, randomSource1);
            }
        }
    }

    private static void tryPlaceWeepingVines(BlockPos blockPos0, LevelAccessor levelAccessor1, RandomSource randomSource2) {
        BlockPos.MutableBlockPos $$3 = blockPos0.mutable().move(Direction.DOWN);
        if (levelAccessor1.m_46859_($$3)) {
            int $$4 = Mth.nextInt(randomSource2, 1, 5);
            if (randomSource2.nextInt(7) == 0) {
                $$4 *= 2;
            }
            int $$5 = 23;
            int $$6 = 25;
            WeepingVinesFeature.placeWeepingVinesColumn(levelAccessor1, randomSource2, $$3, $$4, 23, 25);
        }
    }
}