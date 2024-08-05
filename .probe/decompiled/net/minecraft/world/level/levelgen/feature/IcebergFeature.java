package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.BlockStateConfiguration;

public class IcebergFeature extends Feature<BlockStateConfiguration> {

    public IcebergFeature(Codec<BlockStateConfiguration> codecBlockStateConfiguration0) {
        super(codecBlockStateConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<BlockStateConfiguration> featurePlaceContextBlockStateConfiguration0) {
        BlockPos $$1 = featurePlaceContextBlockStateConfiguration0.origin();
        WorldGenLevel $$2 = featurePlaceContextBlockStateConfiguration0.level();
        $$1 = new BlockPos($$1.m_123341_(), featurePlaceContextBlockStateConfiguration0.chunkGenerator().getSeaLevel(), $$1.m_123343_());
        RandomSource $$3 = featurePlaceContextBlockStateConfiguration0.random();
        boolean $$4 = $$3.nextDouble() > 0.7;
        BlockState $$5 = featurePlaceContextBlockStateConfiguration0.config().state;
        double $$6 = $$3.nextDouble() * 2.0 * Math.PI;
        int $$7 = 11 - $$3.nextInt(5);
        int $$8 = 3 + $$3.nextInt(3);
        boolean $$9 = $$3.nextDouble() > 0.7;
        int $$10 = 11;
        int $$11 = $$9 ? $$3.nextInt(6) + 6 : $$3.nextInt(15) + 3;
        if (!$$9 && $$3.nextDouble() > 0.9) {
            $$11 += $$3.nextInt(19) + 7;
        }
        int $$12 = Math.min($$11 + $$3.nextInt(11), 18);
        int $$13 = Math.min($$11 + $$3.nextInt(7) - $$3.nextInt(5), 11);
        int $$14 = $$9 ? $$7 : 11;
        for (int $$15 = -$$14; $$15 < $$14; $$15++) {
            for (int $$16 = -$$14; $$16 < $$14; $$16++) {
                for (int $$17 = 0; $$17 < $$11; $$17++) {
                    int $$18 = $$9 ? this.heightDependentRadiusEllipse($$17, $$11, $$13) : this.heightDependentRadiusRound($$3, $$17, $$11, $$13);
                    if ($$9 || $$15 < $$18) {
                        this.generateIcebergBlock($$2, $$3, $$1, $$11, $$15, $$17, $$16, $$18, $$14, $$9, $$8, $$6, $$4, $$5);
                    }
                }
            }
        }
        this.smooth($$2, $$1, $$13, $$11, $$9, $$7);
        for (int $$19 = -$$14; $$19 < $$14; $$19++) {
            for (int $$20 = -$$14; $$20 < $$14; $$20++) {
                for (int $$21 = -1; $$21 > -$$12; $$21--) {
                    int $$22 = $$9 ? Mth.ceil((float) $$14 * (1.0F - (float) Math.pow((double) $$21, 2.0) / ((float) $$12 * 8.0F))) : $$14;
                    int $$23 = this.heightDependentRadiusSteep($$3, -$$21, $$12, $$13);
                    if ($$19 < $$23) {
                        this.generateIcebergBlock($$2, $$3, $$1, $$12, $$19, $$21, $$20, $$23, $$22, $$9, $$8, $$6, $$4, $$5);
                    }
                }
            }
        }
        boolean $$24 = $$9 ? $$3.nextDouble() > 0.1 : $$3.nextDouble() > 0.7;
        if ($$24) {
            this.generateCutOut($$3, $$2, $$13, $$11, $$1, $$9, $$7, $$6, $$8);
        }
        return true;
    }

    private void generateCutOut(RandomSource randomSource0, LevelAccessor levelAccessor1, int int2, int int3, BlockPos blockPos4, boolean boolean5, int int6, double double7, int int8) {
        int $$9 = randomSource0.nextBoolean() ? -1 : 1;
        int $$10 = randomSource0.nextBoolean() ? -1 : 1;
        int $$11 = randomSource0.nextInt(Math.max(int2 / 2 - 2, 1));
        if (randomSource0.nextBoolean()) {
            $$11 = int2 / 2 + 1 - randomSource0.nextInt(Math.max(int2 - int2 / 2 - 1, 1));
        }
        int $$12 = randomSource0.nextInt(Math.max(int2 / 2 - 2, 1));
        if (randomSource0.nextBoolean()) {
            $$12 = int2 / 2 + 1 - randomSource0.nextInt(Math.max(int2 - int2 / 2 - 1, 1));
        }
        if (boolean5) {
            $$11 = $$12 = randomSource0.nextInt(Math.max(int6 - 5, 1));
        }
        BlockPos $$13 = new BlockPos($$9 * $$11, 0, $$10 * $$12);
        double $$14 = boolean5 ? double7 + (Math.PI / 2) : randomSource0.nextDouble() * 2.0 * Math.PI;
        for (int $$15 = 0; $$15 < int3 - 3; $$15++) {
            int $$16 = this.heightDependentRadiusRound(randomSource0, $$15, int3, int2);
            this.carve($$16, $$15, blockPos4, levelAccessor1, false, $$14, $$13, int6, int8);
        }
        for (int $$17 = -1; $$17 > -int3 + randomSource0.nextInt(5); $$17--) {
            int $$18 = this.heightDependentRadiusSteep(randomSource0, -$$17, int3, int2);
            this.carve($$18, $$17, blockPos4, levelAccessor1, true, $$14, $$13, int6, int8);
        }
    }

    private void carve(int int0, int int1, BlockPos blockPos2, LevelAccessor levelAccessor3, boolean boolean4, double double5, BlockPos blockPos6, int int7, int int8) {
        int $$9 = int0 + 1 + int7 / 3;
        int $$10 = Math.min(int0 - 3, 3) + int8 / 2 - 1;
        for (int $$11 = -$$9; $$11 < $$9; $$11++) {
            for (int $$12 = -$$9; $$12 < $$9; $$12++) {
                double $$13 = this.signedDistanceEllipse($$11, $$12, blockPos6, $$9, $$10, double5);
                if ($$13 < 0.0) {
                    BlockPos $$14 = blockPos2.offset($$11, int1, $$12);
                    BlockState $$15 = levelAccessor3.m_8055_($$14);
                    if (isIcebergState($$15) || $$15.m_60713_(Blocks.SNOW_BLOCK)) {
                        if (boolean4) {
                            this.m_5974_(levelAccessor3, $$14, Blocks.WATER.defaultBlockState());
                        } else {
                            this.m_5974_(levelAccessor3, $$14, Blocks.AIR.defaultBlockState());
                            this.removeFloatingSnowLayer(levelAccessor3, $$14);
                        }
                    }
                }
            }
        }
    }

    private void removeFloatingSnowLayer(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        if (levelAccessor0.m_8055_(blockPos1.above()).m_60713_(Blocks.SNOW)) {
            this.m_5974_(levelAccessor0, blockPos1.above(), Blocks.AIR.defaultBlockState());
        }
    }

    private void generateIcebergBlock(LevelAccessor levelAccessor0, RandomSource randomSource1, BlockPos blockPos2, int int3, int int4, int int5, int int6, int int7, int int8, boolean boolean9, int int10, double double11, boolean boolean12, BlockState blockState13) {
        double $$14 = boolean9 ? this.signedDistanceEllipse(int4, int6, BlockPos.ZERO, int8, this.getEllipseC(int5, int3, int10), double11) : this.signedDistanceCircle(int4, int6, BlockPos.ZERO, int7, randomSource1);
        if ($$14 < 0.0) {
            BlockPos $$15 = blockPos2.offset(int4, int5, int6);
            double $$16 = boolean9 ? -0.5 : (double) (-6 - randomSource1.nextInt(3));
            if ($$14 > $$16 && randomSource1.nextDouble() > 0.9) {
                return;
            }
            this.setIcebergBlock($$15, levelAccessor0, randomSource1, int3 - int5, int3, boolean9, boolean12, blockState13);
        }
    }

    private void setIcebergBlock(BlockPos blockPos0, LevelAccessor levelAccessor1, RandomSource randomSource2, int int3, int int4, boolean boolean5, boolean boolean6, BlockState blockState7) {
        BlockState $$8 = levelAccessor1.m_8055_(blockPos0);
        if ($$8.m_60795_() || $$8.m_60713_(Blocks.SNOW_BLOCK) || $$8.m_60713_(Blocks.ICE) || $$8.m_60713_(Blocks.WATER)) {
            boolean $$9 = !boolean5 || randomSource2.nextDouble() > 0.05;
            int $$10 = boolean5 ? 3 : 2;
            if (boolean6 && !$$8.m_60713_(Blocks.WATER) && (double) int3 <= (double) randomSource2.nextInt(Math.max(1, int4 / $$10)) + (double) int4 * 0.6 && $$9) {
                this.m_5974_(levelAccessor1, blockPos0, Blocks.SNOW_BLOCK.defaultBlockState());
            } else {
                this.m_5974_(levelAccessor1, blockPos0, blockState7);
            }
        }
    }

    private int getEllipseC(int int0, int int1, int int2) {
        int $$3 = int2;
        if (int0 > 0 && int1 - int0 <= 3) {
            $$3 = int2 - (4 - (int1 - int0));
        }
        return $$3;
    }

    private double signedDistanceCircle(int int0, int int1, BlockPos blockPos2, int int3, RandomSource randomSource4) {
        float $$5 = 10.0F * Mth.clamp(randomSource4.nextFloat(), 0.2F, 0.8F) / (float) int3;
        return (double) $$5 + Math.pow((double) (int0 - blockPos2.m_123341_()), 2.0) + Math.pow((double) (int1 - blockPos2.m_123343_()), 2.0) - Math.pow((double) int3, 2.0);
    }

    private double signedDistanceEllipse(int int0, int int1, BlockPos blockPos2, int int3, int int4, double double5) {
        return Math.pow(((double) (int0 - blockPos2.m_123341_()) * Math.cos(double5) - (double) (int1 - blockPos2.m_123343_()) * Math.sin(double5)) / (double) int3, 2.0) + Math.pow(((double) (int0 - blockPos2.m_123341_()) * Math.sin(double5) + (double) (int1 - blockPos2.m_123343_()) * Math.cos(double5)) / (double) int4, 2.0) - 1.0;
    }

    private int heightDependentRadiusRound(RandomSource randomSource0, int int1, int int2, int int3) {
        float $$4 = 3.5F - randomSource0.nextFloat();
        float $$5 = (1.0F - (float) Math.pow((double) int1, 2.0) / ((float) int2 * $$4)) * (float) int3;
        if (int2 > 15 + randomSource0.nextInt(5)) {
            int $$6 = int1 < 3 + randomSource0.nextInt(6) ? int1 / 2 : int1;
            $$5 = (1.0F - (float) $$6 / ((float) int2 * $$4 * 0.4F)) * (float) int3;
        }
        return Mth.ceil($$5 / 2.0F);
    }

    private int heightDependentRadiusEllipse(int int0, int int1, int int2) {
        float $$3 = 1.0F;
        float $$4 = (1.0F - (float) Math.pow((double) int0, 2.0) / ((float) int1 * 1.0F)) * (float) int2;
        return Mth.ceil($$4 / 2.0F);
    }

    private int heightDependentRadiusSteep(RandomSource randomSource0, int int1, int int2, int int3) {
        float $$4 = 1.0F + randomSource0.nextFloat() / 2.0F;
        float $$5 = (1.0F - (float) int1 / ((float) int2 * $$4)) * (float) int3;
        return Mth.ceil($$5 / 2.0F);
    }

    private static boolean isIcebergState(BlockState blockState0) {
        return blockState0.m_60713_(Blocks.PACKED_ICE) || blockState0.m_60713_(Blocks.SNOW_BLOCK) || blockState0.m_60713_(Blocks.BLUE_ICE);
    }

    private boolean belowIsAir(BlockGetter blockGetter0, BlockPos blockPos1) {
        return blockGetter0.getBlockState(blockPos1.below()).m_60795_();
    }

    private void smooth(LevelAccessor levelAccessor0, BlockPos blockPos1, int int2, int int3, boolean boolean4, int int5) {
        int $$6 = boolean4 ? int5 : int2 / 2;
        for (int $$7 = -$$6; $$7 <= $$6; $$7++) {
            for (int $$8 = -$$6; $$8 <= $$6; $$8++) {
                for (int $$9 = 0; $$9 <= int3; $$9++) {
                    BlockPos $$10 = blockPos1.offset($$7, $$9, $$8);
                    BlockState $$11 = levelAccessor0.m_8055_($$10);
                    if (isIcebergState($$11) || $$11.m_60713_(Blocks.SNOW)) {
                        if (this.belowIsAir(levelAccessor0, $$10)) {
                            this.m_5974_(levelAccessor0, $$10, Blocks.AIR.defaultBlockState());
                            this.m_5974_(levelAccessor0, $$10.above(), Blocks.AIR.defaultBlockState());
                        } else if (isIcebergState($$11)) {
                            BlockState[] $$12 = new BlockState[] { levelAccessor0.m_8055_($$10.west()), levelAccessor0.m_8055_($$10.east()), levelAccessor0.m_8055_($$10.north()), levelAccessor0.m_8055_($$10.south()) };
                            int $$13 = 0;
                            for (BlockState $$14 : $$12) {
                                if (!isIcebergState($$14)) {
                                    $$13++;
                                }
                            }
                            if ($$13 >= 3) {
                                this.m_5974_(levelAccessor0, $$10, Blocks.AIR.defaultBlockState());
                            }
                        }
                    }
                }
            }
        }
    }
}