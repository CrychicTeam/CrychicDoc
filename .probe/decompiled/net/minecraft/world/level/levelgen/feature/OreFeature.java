package net.minecraft.world.level.levelgen.feature;

import com.mojang.serialization.Codec;
import java.util.BitSet;
import java.util.function.Function;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.BulkSectionAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;

public class OreFeature extends Feature<OreConfiguration> {

    public OreFeature(Codec<OreConfiguration> codecOreConfiguration0) {
        super(codecOreConfiguration0);
    }

    @Override
    public boolean place(FeaturePlaceContext<OreConfiguration> featurePlaceContextOreConfiguration0) {
        RandomSource $$1 = featurePlaceContextOreConfiguration0.random();
        BlockPos $$2 = featurePlaceContextOreConfiguration0.origin();
        WorldGenLevel $$3 = featurePlaceContextOreConfiguration0.level();
        OreConfiguration $$4 = featurePlaceContextOreConfiguration0.config();
        float $$5 = $$1.nextFloat() * (float) Math.PI;
        float $$6 = (float) $$4.size / 8.0F;
        int $$7 = Mth.ceil(((float) $$4.size / 16.0F * 2.0F + 1.0F) / 2.0F);
        double $$8 = (double) $$2.m_123341_() + Math.sin((double) $$5) * (double) $$6;
        double $$9 = (double) $$2.m_123341_() - Math.sin((double) $$5) * (double) $$6;
        double $$10 = (double) $$2.m_123343_() + Math.cos((double) $$5) * (double) $$6;
        double $$11 = (double) $$2.m_123343_() - Math.cos((double) $$5) * (double) $$6;
        int $$12 = 2;
        double $$13 = (double) ($$2.m_123342_() + $$1.nextInt(3) - 2);
        double $$14 = (double) ($$2.m_123342_() + $$1.nextInt(3) - 2);
        int $$15 = $$2.m_123341_() - Mth.ceil($$6) - $$7;
        int $$16 = $$2.m_123342_() - 2 - $$7;
        int $$17 = $$2.m_123343_() - Mth.ceil($$6) - $$7;
        int $$18 = 2 * (Mth.ceil($$6) + $$7);
        int $$19 = 2 * (2 + $$7);
        for (int $$20 = $$15; $$20 <= $$15 + $$18; $$20++) {
            for (int $$21 = $$17; $$21 <= $$17 + $$18; $$21++) {
                if ($$16 <= $$3.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, $$20, $$21)) {
                    return this.doPlace($$3, $$1, $$4, $$8, $$9, $$10, $$11, $$13, $$14, $$15, $$16, $$17, $$18, $$19);
                }
            }
        }
        return false;
    }

    protected boolean doPlace(WorldGenLevel worldGenLevel0, RandomSource randomSource1, OreConfiguration oreConfiguration2, double double3, double double4, double double5, double double6, double double7, double double8, int int9, int int10, int int11, int int12, int int13) {
        int $$14 = 0;
        BitSet $$15 = new BitSet(int12 * int13 * int12);
        BlockPos.MutableBlockPos $$16 = new BlockPos.MutableBlockPos();
        int $$17 = oreConfiguration2.size;
        double[] $$18 = new double[$$17 * 4];
        for (int $$19 = 0; $$19 < $$17; $$19++) {
            float $$20 = (float) $$19 / (float) $$17;
            double $$21 = Mth.lerp((double) $$20, double3, double4);
            double $$22 = Mth.lerp((double) $$20, double7, double8);
            double $$23 = Mth.lerp((double) $$20, double5, double6);
            double $$24 = randomSource1.nextDouble() * (double) $$17 / 16.0;
            double $$25 = ((double) (Mth.sin((float) Math.PI * $$20) + 1.0F) * $$24 + 1.0) / 2.0;
            $$18[$$19 * 4 + 0] = $$21;
            $$18[$$19 * 4 + 1] = $$22;
            $$18[$$19 * 4 + 2] = $$23;
            $$18[$$19 * 4 + 3] = $$25;
        }
        for (int $$26 = 0; $$26 < $$17 - 1; $$26++) {
            if (!($$18[$$26 * 4 + 3] <= 0.0)) {
                for (int $$27 = $$26 + 1; $$27 < $$17; $$27++) {
                    if (!($$18[$$27 * 4 + 3] <= 0.0)) {
                        double $$28 = $$18[$$26 * 4 + 0] - $$18[$$27 * 4 + 0];
                        double $$29 = $$18[$$26 * 4 + 1] - $$18[$$27 * 4 + 1];
                        double $$30 = $$18[$$26 * 4 + 2] - $$18[$$27 * 4 + 2];
                        double $$31 = $$18[$$26 * 4 + 3] - $$18[$$27 * 4 + 3];
                        if ($$31 * $$31 > $$28 * $$28 + $$29 * $$29 + $$30 * $$30) {
                            if ($$31 > 0.0) {
                                $$18[$$27 * 4 + 3] = -1.0;
                            } else {
                                $$18[$$26 * 4 + 3] = -1.0;
                            }
                        }
                    }
                }
            }
        }
        try (BulkSectionAccess $$32 = new BulkSectionAccess(worldGenLevel0)) {
            for (int $$33 = 0; $$33 < $$17; $$33++) {
                double $$34 = $$18[$$33 * 4 + 3];
                if (!($$34 < 0.0)) {
                    double $$35 = $$18[$$33 * 4 + 0];
                    double $$36 = $$18[$$33 * 4 + 1];
                    double $$37 = $$18[$$33 * 4 + 2];
                    int $$38 = Math.max(Mth.floor($$35 - $$34), int9);
                    int $$39 = Math.max(Mth.floor($$36 - $$34), int10);
                    int $$40 = Math.max(Mth.floor($$37 - $$34), int11);
                    int $$41 = Math.max(Mth.floor($$35 + $$34), $$38);
                    int $$42 = Math.max(Mth.floor($$36 + $$34), $$39);
                    int $$43 = Math.max(Mth.floor($$37 + $$34), $$40);
                    for (int $$44 = $$38; $$44 <= $$41; $$44++) {
                        double $$45 = ((double) $$44 + 0.5 - $$35) / $$34;
                        if ($$45 * $$45 < 1.0) {
                            for (int $$46 = $$39; $$46 <= $$42; $$46++) {
                                double $$47 = ((double) $$46 + 0.5 - $$36) / $$34;
                                if ($$45 * $$45 + $$47 * $$47 < 1.0) {
                                    for (int $$48 = $$40; $$48 <= $$43; $$48++) {
                                        double $$49 = ((double) $$48 + 0.5 - $$37) / $$34;
                                        if ($$45 * $$45 + $$47 * $$47 + $$49 * $$49 < 1.0 && !worldGenLevel0.m_151562_($$46)) {
                                            int $$50 = $$44 - int9 + ($$46 - int10) * int12 + ($$48 - int11) * int12 * int13;
                                            if (!$$15.get($$50)) {
                                                $$15.set($$50);
                                                $$16.set($$44, $$46, $$48);
                                                if (worldGenLevel0.ensureCanWrite($$16)) {
                                                    LevelChunkSection $$51 = $$32.getSection($$16);
                                                    if ($$51 != null) {
                                                        int $$52 = SectionPos.sectionRelative($$44);
                                                        int $$53 = SectionPos.sectionRelative($$46);
                                                        int $$54 = SectionPos.sectionRelative($$48);
                                                        BlockState $$55 = $$51.getBlockState($$52, $$53, $$54);
                                                        for (OreConfiguration.TargetBlockState $$56 : oreConfiguration2.targetStates) {
                                                            if (canPlaceOre($$55, $$32::m_156110_, randomSource1, oreConfiguration2, $$56, $$16)) {
                                                                $$51.setBlockState($$52, $$53, $$54, $$56.state, false);
                                                                $$14++;
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return $$14 > 0;
    }

    public static boolean canPlaceOre(BlockState blockState0, Function<BlockPos, BlockState> functionBlockPosBlockState1, RandomSource randomSource2, OreConfiguration oreConfiguration3, OreConfiguration.TargetBlockState oreConfigurationTargetBlockState4, BlockPos.MutableBlockPos blockPosMutableBlockPos5) {
        if (!oreConfigurationTargetBlockState4.target.test(blockState0, randomSource2)) {
            return false;
        } else {
            return shouldSkipAirCheck(randomSource2, oreConfiguration3.discardChanceOnAirExposure) ? true : !m_159750_(functionBlockPosBlockState1, blockPosMutableBlockPos5);
        }
    }

    protected static boolean shouldSkipAirCheck(RandomSource randomSource0, float float1) {
        if (float1 <= 0.0F) {
            return true;
        } else {
            return float1 >= 1.0F ? false : randomSource0.nextFloat() >= float1;
        }
    }
}