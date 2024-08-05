package net.minecraft.world.level.levelgen.feature;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.BuddingAmethystBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.levelgen.GeodeBlockSettings;
import net.minecraft.world.level.levelgen.GeodeCrackSettings;
import net.minecraft.world.level.levelgen.GeodeLayerSettings;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.feature.configurations.GeodeConfiguration;
import net.minecraft.world.level.levelgen.synth.NormalNoise;
import net.minecraft.world.level.material.FluidState;

public class GeodeFeature extends Feature<GeodeConfiguration> {

    private static final Direction[] DIRECTIONS = Direction.values();

    public GeodeFeature(Codec<GeodeConfiguration> codecGeodeConfiguration0) {
        super(codecGeodeConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<GeodeConfiguration> featurePlaceContextGeodeConfiguration0) {
        GeodeConfiguration $$1 = featurePlaceContextGeodeConfiguration0.config();
        RandomSource $$2 = featurePlaceContextGeodeConfiguration0.random();
        BlockPos $$3 = featurePlaceContextGeodeConfiguration0.origin();
        WorldGenLevel $$4 = featurePlaceContextGeodeConfiguration0.level();
        int $$5 = $$1.minGenOffset;
        int $$6 = $$1.maxGenOffset;
        List<Pair<BlockPos, Integer>> $$7 = Lists.newLinkedList();
        int $$8 = $$1.distributionPoints.sample($$2);
        WorldgenRandom $$9 = new WorldgenRandom(new LegacyRandomSource($$4.getSeed()));
        NormalNoise $$10 = NormalNoise.create($$9, -4, 1.0);
        List<BlockPos> $$11 = Lists.newLinkedList();
        double $$12 = (double) $$8 / (double) $$1.outerWallDistance.getMaxValue();
        GeodeLayerSettings $$13 = $$1.geodeLayerSettings;
        GeodeBlockSettings $$14 = $$1.geodeBlockSettings;
        GeodeCrackSettings $$15 = $$1.geodeCrackSettings;
        double $$16 = 1.0 / Math.sqrt($$13.filling);
        double $$17 = 1.0 / Math.sqrt($$13.innerLayer + $$12);
        double $$18 = 1.0 / Math.sqrt($$13.middleLayer + $$12);
        double $$19 = 1.0 / Math.sqrt($$13.outerLayer + $$12);
        double $$20 = 1.0 / Math.sqrt($$15.baseCrackSize + $$2.nextDouble() / 2.0 + ($$8 > 3 ? $$12 : 0.0));
        boolean $$21 = (double) $$2.nextFloat() < $$15.generateCrackChance;
        int $$22 = 0;
        for (int $$23 = 0; $$23 < $$8; $$23++) {
            int $$24 = $$1.outerWallDistance.sample($$2);
            int $$25 = $$1.outerWallDistance.sample($$2);
            int $$26 = $$1.outerWallDistance.sample($$2);
            BlockPos $$27 = $$3.offset($$24, $$25, $$26);
            BlockState $$28 = $$4.m_8055_($$27);
            if ($$28.m_60795_() || $$28.m_204336_(BlockTags.GEODE_INVALID_BLOCKS)) {
                if (++$$22 > $$1.invalidBlocksThreshold) {
                    return false;
                }
            }
            $$7.add(Pair.of($$27, $$1.pointOffset.sample($$2)));
        }
        if ($$21) {
            int $$29 = $$2.nextInt(4);
            int $$30 = $$8 * 2 + 1;
            if ($$29 == 0) {
                $$11.add($$3.offset($$30, 7, 0));
                $$11.add($$3.offset($$30, 5, 0));
                $$11.add($$3.offset($$30, 1, 0));
            } else if ($$29 == 1) {
                $$11.add($$3.offset(0, 7, $$30));
                $$11.add($$3.offset(0, 5, $$30));
                $$11.add($$3.offset(0, 1, $$30));
            } else if ($$29 == 2) {
                $$11.add($$3.offset($$30, 7, $$30));
                $$11.add($$3.offset($$30, 5, $$30));
                $$11.add($$3.offset($$30, 1, $$30));
            } else {
                $$11.add($$3.offset(0, 7, 0));
                $$11.add($$3.offset(0, 5, 0));
                $$11.add($$3.offset(0, 1, 0));
            }
        }
        List<BlockPos> $$31 = Lists.newArrayList();
        Predicate<BlockState> $$32 = m_204735_($$1.geodeBlockSettings.cannotReplace);
        for (BlockPos $$33 : BlockPos.betweenClosed($$3.offset($$5, $$5, $$5), $$3.offset($$6, $$6, $$6))) {
            double $$34 = $$10.getValue((double) $$33.m_123341_(), (double) $$33.m_123342_(), (double) $$33.m_123343_()) * $$1.noiseMultiplier;
            double $$35 = 0.0;
            double $$36 = 0.0;
            for (Pair<BlockPos, Integer> $$37 : $$7) {
                $$35 += Mth.invSqrt($$33.m_123331_((Vec3i) $$37.getFirst()) + (double) ((Integer) $$37.getSecond()).intValue()) + $$34;
            }
            for (BlockPos $$38 : $$11) {
                $$36 += Mth.invSqrt($$33.m_123331_($$38) + (double) $$15.crackPointOffset) + $$34;
            }
            if (!($$35 < $$19)) {
                if ($$21 && $$36 >= $$20 && $$35 < $$16) {
                    this.m_159742_($$4, $$33, Blocks.AIR.defaultBlockState(), $$32);
                    for (Direction $$39 : DIRECTIONS) {
                        BlockPos $$40 = $$33.relative($$39);
                        FluidState $$41 = $$4.m_6425_($$40);
                        if (!$$41.isEmpty()) {
                            $$4.m_186469_($$40, $$41.getType(), 0);
                        }
                    }
                } else if ($$35 >= $$16) {
                    this.m_159742_($$4, $$33, $$14.fillingProvider.getState($$2, $$33), $$32);
                } else if ($$35 >= $$17) {
                    boolean $$42 = (double) $$2.nextFloat() < $$1.useAlternateLayer0Chance;
                    if ($$42) {
                        this.m_159742_($$4, $$33, $$14.alternateInnerLayerProvider.getState($$2, $$33), $$32);
                    } else {
                        this.m_159742_($$4, $$33, $$14.innerLayerProvider.getState($$2, $$33), $$32);
                    }
                    if ((!$$1.placementsRequireLayer0Alternate || $$42) && (double) $$2.nextFloat() < $$1.usePotentialPlacementsChance) {
                        $$31.add($$33.immutable());
                    }
                } else if ($$35 >= $$18) {
                    this.m_159742_($$4, $$33, $$14.middleLayerProvider.getState($$2, $$33), $$32);
                } else if ($$35 >= $$19) {
                    this.m_159742_($$4, $$33, $$14.outerLayerProvider.getState($$2, $$33), $$32);
                }
            }
        }
        List<BlockState> $$43 = $$14.innerPlacements;
        for (BlockPos $$44 : $$31) {
            BlockState $$45 = Util.getRandom($$43, $$2);
            for (Direction $$46 : DIRECTIONS) {
                if ($$45.m_61138_(BlockStateProperties.FACING)) {
                    $$45 = (BlockState) $$45.m_61124_(BlockStateProperties.FACING, $$46);
                }
                BlockPos $$47 = $$44.relative($$46);
                BlockState $$48 = $$4.m_8055_($$47);
                if ($$45.m_61138_(BlockStateProperties.WATERLOGGED)) {
                    $$45 = (BlockState) $$45.m_61124_(BlockStateProperties.WATERLOGGED, $$48.m_60819_().isSource());
                }
                if (BuddingAmethystBlock.canClusterGrowAtState($$48)) {
                    this.m_159742_($$4, $$47, $$45, $$32);
                    break;
                }
            }
        }
        return true;
    }
}