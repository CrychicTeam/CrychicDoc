package net.minecraft.world.level.levelgen.feature.trunkplacers;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;
import java.util.Objects;
import java.util.function.BiConsumer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FoliagePlacer;

public class FancyTrunkPlacer extends TrunkPlacer {

    public static final Codec<FancyTrunkPlacer> CODEC = RecordCodecBuilder.create(p_70136_ -> m_70305_(p_70136_).apply(p_70136_, FancyTrunkPlacer::new));

    private static final double TRUNK_HEIGHT_SCALE = 0.618;

    private static final double CLUSTER_DENSITY_MAGIC = 1.382;

    private static final double BRANCH_SLOPE = 0.381;

    private static final double BRANCH_LENGTH_MAGIC = 0.328;

    public FancyTrunkPlacer(int int0, int int1, int int2) {
        super(int0, int1, int2);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return TrunkPlacerType.FANCY_TRUNK_PLACER;
    }

    @Override
    public List<FoliagePlacer.FoliageAttachment> placeTrunk(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, TreeConfiguration treeConfiguration5) {
        int $$6 = 5;
        int $$7 = int3 + 2;
        int $$8 = Mth.floor((double) $$7 * 0.618);
        m_226169_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4.below(), treeConfiguration5);
        double $$9 = 1.0;
        int $$10 = Math.min(1, Mth.floor(1.382 + Math.pow(1.0 * (double) $$7 / 13.0, 2.0)));
        int $$11 = blockPos4.m_123342_() + $$8;
        int $$12 = $$7 - 5;
        List<FancyTrunkPlacer.FoliageCoords> $$13 = Lists.newArrayList();
        $$13.add(new FancyTrunkPlacer.FoliageCoords(blockPos4.above($$12), $$11));
        for (; $$12 >= 0; $$12--) {
            float $$14 = treeShape($$7, $$12);
            if (!($$14 < 0.0F)) {
                for (int $$15 = 0; $$15 < $$10; $$15++) {
                    double $$16 = 1.0;
                    double $$17 = 1.0 * (double) $$14 * ((double) randomSource2.nextFloat() + 0.328);
                    double $$18 = (double) (randomSource2.nextFloat() * 2.0F) * Math.PI;
                    double $$19 = $$17 * Math.sin($$18) + 0.5;
                    double $$20 = $$17 * Math.cos($$18) + 0.5;
                    BlockPos $$21 = blockPos4.offset(Mth.floor($$19), $$12 - 1, Mth.floor($$20));
                    BlockPos $$22 = $$21.above(5);
                    if (this.makeLimb(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$21, $$22, false, treeConfiguration5)) {
                        int $$23 = blockPos4.m_123341_() - $$21.m_123341_();
                        int $$24 = blockPos4.m_123343_() - $$21.m_123343_();
                        double $$25 = (double) $$21.m_123342_() - Math.sqrt((double) ($$23 * $$23 + $$24 * $$24)) * 0.381;
                        int $$26 = $$25 > (double) $$11 ? $$11 : (int) $$25;
                        BlockPos $$27 = new BlockPos(blockPos4.m_123341_(), $$26, blockPos4.m_123343_());
                        if (this.makeLimb(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$27, $$21, false, treeConfiguration5)) {
                            $$13.add(new FancyTrunkPlacer.FoliageCoords($$21, $$27.m_123342_()));
                        }
                    }
                }
            }
        }
        this.makeLimb(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, blockPos4, blockPos4.above($$8), true, treeConfiguration5);
        this.makeBranches(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$7, blockPos4, $$13, treeConfiguration5);
        List<FoliagePlacer.FoliageAttachment> $$28 = Lists.newArrayList();
        for (FancyTrunkPlacer.FoliageCoords $$29 : $$13) {
            if (this.trimBranches($$7, $$29.getBranchBase() - blockPos4.m_123342_())) {
                $$28.add($$29.attachment);
            }
        }
        return $$28;
    }

    private boolean makeLimb(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, BlockPos blockPos3, BlockPos blockPos4, boolean boolean5, TreeConfiguration treeConfiguration6) {
        if (!boolean5 && Objects.equals(blockPos3, blockPos4)) {
            return true;
        } else {
            BlockPos $$7 = blockPos4.offset(-blockPos3.m_123341_(), -blockPos3.m_123342_(), -blockPos3.m_123343_());
            int $$8 = this.getSteps($$7);
            float $$9 = (float) $$7.m_123341_() / (float) $$8;
            float $$10 = (float) $$7.m_123342_() / (float) $$8;
            float $$11 = (float) $$7.m_123343_() / (float) $$8;
            for (int $$12 = 0; $$12 <= $$8; $$12++) {
                BlockPos $$13 = blockPos3.offset(Mth.floor(0.5F + (float) $$12 * $$9), Mth.floor(0.5F + (float) $$12 * $$10), Mth.floor(0.5F + (float) $$12 * $$11));
                if (boolean5) {
                    this.m_226175_(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$13, treeConfiguration6, p_161826_ -> (BlockState) p_161826_.m_263224_(RotatedPillarBlock.AXIS, this.getLogAxis(blockPos3, $$13)));
                } else if (!this.m_226184_(levelSimulatedReader0, $$13)) {
                    return false;
                }
            }
            return true;
        }
    }

    private int getSteps(BlockPos blockPos0) {
        int $$1 = Mth.abs(blockPos0.m_123341_());
        int $$2 = Mth.abs(blockPos0.m_123342_());
        int $$3 = Mth.abs(blockPos0.m_123343_());
        return Math.max($$1, Math.max($$2, $$3));
    }

    private Direction.Axis getLogAxis(BlockPos blockPos0, BlockPos blockPos1) {
        Direction.Axis $$2 = Direction.Axis.Y;
        int $$3 = Math.abs(blockPos1.m_123341_() - blockPos0.m_123341_());
        int $$4 = Math.abs(blockPos1.m_123343_() - blockPos0.m_123343_());
        int $$5 = Math.max($$3, $$4);
        if ($$5 > 0) {
            if ($$3 == $$5) {
                $$2 = Direction.Axis.X;
            } else {
                $$2 = Direction.Axis.Z;
            }
        }
        return $$2;
    }

    private boolean trimBranches(int int0, int int1) {
        return (double) int1 >= (double) int0 * 0.2;
    }

    private void makeBranches(LevelSimulatedReader levelSimulatedReader0, BiConsumer<BlockPos, BlockState> biConsumerBlockPosBlockState1, RandomSource randomSource2, int int3, BlockPos blockPos4, List<FancyTrunkPlacer.FoliageCoords> listFancyTrunkPlacerFoliageCoords5, TreeConfiguration treeConfiguration6) {
        for (FancyTrunkPlacer.FoliageCoords $$7 : listFancyTrunkPlacerFoliageCoords5) {
            int $$8 = $$7.getBranchBase();
            BlockPos $$9 = new BlockPos(blockPos4.m_123341_(), $$8, blockPos4.m_123343_());
            if (!$$9.equals($$7.attachment.pos()) && this.trimBranches(int3, $$8 - blockPos4.m_123342_())) {
                this.makeLimb(levelSimulatedReader0, biConsumerBlockPosBlockState1, randomSource2, $$9, $$7.attachment.pos(), true, treeConfiguration6);
            }
        }
    }

    private static float treeShape(int int0, int int1) {
        if ((float) int1 < (float) int0 * 0.3F) {
            return -1.0F;
        } else {
            float $$2 = (float) int0 / 2.0F;
            float $$3 = $$2 - (float) int1;
            float $$4 = Mth.sqrt($$2 * $$2 - $$3 * $$3);
            if ($$3 == 0.0F) {
                $$4 = $$2;
            } else if (Math.abs($$3) >= $$2) {
                return 0.0F;
            }
            return $$4 * 0.5F;
        }
    }

    static class FoliageCoords {

        final FoliagePlacer.FoliageAttachment attachment;

        private final int branchBase;

        public FoliageCoords(BlockPos blockPos0, int int1) {
            this.attachment = new FoliagePlacer.FoliageAttachment(blockPos0, 0, false);
            this.branchBase = int1;
        }

        public int getBranchBase() {
            return this.branchBase;
        }
    }
}