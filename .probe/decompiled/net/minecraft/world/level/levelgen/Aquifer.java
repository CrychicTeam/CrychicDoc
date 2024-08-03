package net.minecraft.world.level.levelgen;

import java.util.Arrays;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.OverworldBiomeBuilder;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.dimension.DimensionType;
import org.apache.commons.lang3.mutable.MutableDouble;

public interface Aquifer {

    static Aquifer create(NoiseChunk noiseChunk0, ChunkPos chunkPos1, NoiseRouter noiseRouter2, PositionalRandomFactory positionalRandomFactory3, int int4, int int5, Aquifer.FluidPicker aquiferFluidPicker6) {
        return new Aquifer.NoiseBasedAquifer(noiseChunk0, chunkPos1, noiseRouter2, positionalRandomFactory3, int4, int5, aquiferFluidPicker6);
    }

    static Aquifer createDisabled(final Aquifer.FluidPicker aquiferFluidPicker0) {
        return new Aquifer() {

            @Nullable
            @Override
            public BlockState computeSubstance(DensityFunction.FunctionContext p_208172_, double p_208173_) {
                return p_208173_ > 0.0 ? null : aquiferFluidPicker0.computeFluid(p_208172_.blockX(), p_208172_.blockY(), p_208172_.blockZ()).at(p_208172_.blockY());
            }

            @Override
            public boolean shouldScheduleFluidUpdate() {
                return false;
            }
        };
    }

    @Nullable
    BlockState computeSubstance(DensityFunction.FunctionContext var1, double var2);

    boolean shouldScheduleFluidUpdate();

    public interface FluidPicker {

        Aquifer.FluidStatus computeFluid(int var1, int var2, int var3);
    }

    public static final class FluidStatus {

        final int fluidLevel;

        final BlockState fluidType;

        public FluidStatus(int int0, BlockState blockState1) {
            this.fluidLevel = int0;
            this.fluidType = blockState1;
        }

        public BlockState at(int int0) {
            return int0 < this.fluidLevel ? this.fluidType : Blocks.AIR.defaultBlockState();
        }
    }

    public static class NoiseBasedAquifer implements Aquifer {

        private static final int X_RANGE = 10;

        private static final int Y_RANGE = 9;

        private static final int Z_RANGE = 10;

        private static final int X_SEPARATION = 6;

        private static final int Y_SEPARATION = 3;

        private static final int Z_SEPARATION = 6;

        private static final int X_SPACING = 16;

        private static final int Y_SPACING = 12;

        private static final int Z_SPACING = 16;

        private static final int MAX_REASONABLE_DISTANCE_TO_AQUIFER_CENTER = 11;

        private static final double FLOWING_UPDATE_SIMULARITY = similarity(Mth.square(10), Mth.square(12));

        private final NoiseChunk noiseChunk;

        private final DensityFunction barrierNoise;

        private final DensityFunction fluidLevelFloodednessNoise;

        private final DensityFunction fluidLevelSpreadNoise;

        private final DensityFunction lavaNoise;

        private final PositionalRandomFactory positionalRandomFactory;

        private final Aquifer.FluidStatus[] aquiferCache;

        private final long[] aquiferLocationCache;

        private final Aquifer.FluidPicker globalFluidPicker;

        private final DensityFunction erosion;

        private final DensityFunction depth;

        private boolean shouldScheduleFluidUpdate;

        private final int minGridX;

        private final int minGridY;

        private final int minGridZ;

        private final int gridSizeX;

        private final int gridSizeZ;

        private static final int[][] SURFACE_SAMPLING_OFFSETS_IN_CHUNKS = new int[][] { { 0, 0 }, { -2, -1 }, { -1, -1 }, { 0, -1 }, { 1, -1 }, { -3, 0 }, { -2, 0 }, { -1, 0 }, { 1, 0 }, { -2, 1 }, { -1, 1 }, { 0, 1 }, { 1, 1 } };

        NoiseBasedAquifer(NoiseChunk noiseChunk0, ChunkPos chunkPos1, NoiseRouter noiseRouter2, PositionalRandomFactory positionalRandomFactory3, int int4, int int5, Aquifer.FluidPicker aquiferFluidPicker6) {
            this.noiseChunk = noiseChunk0;
            this.barrierNoise = noiseRouter2.barrierNoise();
            this.fluidLevelFloodednessNoise = noiseRouter2.fluidLevelFloodednessNoise();
            this.fluidLevelSpreadNoise = noiseRouter2.fluidLevelSpreadNoise();
            this.lavaNoise = noiseRouter2.lavaNoise();
            this.erosion = noiseRouter2.erosion();
            this.depth = noiseRouter2.depth();
            this.positionalRandomFactory = positionalRandomFactory3;
            this.minGridX = this.gridX(chunkPos1.getMinBlockX()) - 1;
            this.globalFluidPicker = aquiferFluidPicker6;
            int $$7 = this.gridX(chunkPos1.getMaxBlockX()) + 1;
            this.gridSizeX = $$7 - this.minGridX + 1;
            this.minGridY = this.gridY(int4) - 1;
            int $$8 = this.gridY(int4 + int5) + 1;
            int $$9 = $$8 - this.minGridY + 1;
            this.minGridZ = this.gridZ(chunkPos1.getMinBlockZ()) - 1;
            int $$10 = this.gridZ(chunkPos1.getMaxBlockZ()) + 1;
            this.gridSizeZ = $$10 - this.minGridZ + 1;
            int $$11 = this.gridSizeX * $$9 * this.gridSizeZ;
            this.aquiferCache = new Aquifer.FluidStatus[$$11];
            this.aquiferLocationCache = new long[$$11];
            Arrays.fill(this.aquiferLocationCache, Long.MAX_VALUE);
        }

        private int getIndex(int int0, int int1, int int2) {
            int $$3 = int0 - this.minGridX;
            int $$4 = int1 - this.minGridY;
            int $$5 = int2 - this.minGridZ;
            return ($$4 * this.gridSizeZ + $$5) * this.gridSizeX + $$3;
        }

        @Nullable
        @Override
        public BlockState computeSubstance(DensityFunction.FunctionContext densityFunctionFunctionContext0, double double1) {
            int $$2 = densityFunctionFunctionContext0.blockX();
            int $$3 = densityFunctionFunctionContext0.blockY();
            int $$4 = densityFunctionFunctionContext0.blockZ();
            if (double1 > 0.0) {
                this.shouldScheduleFluidUpdate = false;
                return null;
            } else {
                Aquifer.FluidStatus $$5 = this.globalFluidPicker.computeFluid($$2, $$3, $$4);
                if ($$5.at($$3).m_60713_(Blocks.LAVA)) {
                    this.shouldScheduleFluidUpdate = false;
                    return Blocks.LAVA.defaultBlockState();
                } else {
                    int $$6 = Math.floorDiv($$2 - 5, 16);
                    int $$7 = Math.floorDiv($$3 + 1, 12);
                    int $$8 = Math.floorDiv($$4 - 5, 16);
                    int $$9 = Integer.MAX_VALUE;
                    int $$10 = Integer.MAX_VALUE;
                    int $$11 = Integer.MAX_VALUE;
                    long $$12 = 0L;
                    long $$13 = 0L;
                    long $$14 = 0L;
                    for (int $$15 = 0; $$15 <= 1; $$15++) {
                        for (int $$16 = -1; $$16 <= 1; $$16++) {
                            for (int $$17 = 0; $$17 <= 1; $$17++) {
                                int $$18 = $$6 + $$15;
                                int $$19 = $$7 + $$16;
                                int $$20 = $$8 + $$17;
                                int $$21 = this.getIndex($$18, $$19, $$20);
                                long $$22 = this.aquiferLocationCache[$$21];
                                long $$23;
                                if ($$22 != Long.MAX_VALUE) {
                                    $$23 = $$22;
                                } else {
                                    RandomSource $$24 = this.positionalRandomFactory.at($$18, $$19, $$20);
                                    $$23 = BlockPos.asLong($$18 * 16 + $$24.nextInt(10), $$19 * 12 + $$24.nextInt(9), $$20 * 16 + $$24.nextInt(10));
                                    this.aquiferLocationCache[$$21] = $$23;
                                }
                                int $$26 = BlockPos.getX($$23) - $$2;
                                int $$27 = BlockPos.getY($$23) - $$3;
                                int $$28 = BlockPos.getZ($$23) - $$4;
                                int $$29 = $$26 * $$26 + $$27 * $$27 + $$28 * $$28;
                                if ($$9 >= $$29) {
                                    $$14 = $$13;
                                    $$13 = $$12;
                                    $$12 = $$23;
                                    $$11 = $$10;
                                    $$10 = $$9;
                                    $$9 = $$29;
                                } else if ($$10 >= $$29) {
                                    $$14 = $$13;
                                    $$13 = $$23;
                                    $$11 = $$10;
                                    $$10 = $$29;
                                } else if ($$11 >= $$29) {
                                    $$14 = $$23;
                                    $$11 = $$29;
                                }
                            }
                        }
                    }
                    Aquifer.FluidStatus $$30 = this.getAquiferStatus($$12);
                    double $$31 = similarity($$9, $$10);
                    BlockState $$32 = $$30.at($$3);
                    if ($$31 <= 0.0) {
                        this.shouldScheduleFluidUpdate = $$31 >= FLOWING_UPDATE_SIMULARITY;
                        return $$32;
                    } else if ($$32.m_60713_(Blocks.WATER) && this.globalFluidPicker.computeFluid($$2, $$3 - 1, $$4).at($$3 - 1).m_60713_(Blocks.LAVA)) {
                        this.shouldScheduleFluidUpdate = true;
                        return $$32;
                    } else {
                        MutableDouble $$34 = new MutableDouble(Double.NaN);
                        Aquifer.FluidStatus $$35 = this.getAquiferStatus($$13);
                        double $$36 = $$31 * this.calculatePressure(densityFunctionFunctionContext0, $$34, $$30, $$35);
                        if (double1 + $$36 > 0.0) {
                            this.shouldScheduleFluidUpdate = false;
                            return null;
                        } else {
                            Aquifer.FluidStatus $$37 = this.getAquiferStatus($$14);
                            double $$38 = similarity($$9, $$11);
                            if ($$38 > 0.0) {
                                double $$39 = $$31 * $$38 * this.calculatePressure(densityFunctionFunctionContext0, $$34, $$30, $$37);
                                if (double1 + $$39 > 0.0) {
                                    this.shouldScheduleFluidUpdate = false;
                                    return null;
                                }
                            }
                            double $$40 = similarity($$10, $$11);
                            if ($$40 > 0.0) {
                                double $$41 = $$31 * $$40 * this.calculatePressure(densityFunctionFunctionContext0, $$34, $$35, $$37);
                                if (double1 + $$41 > 0.0) {
                                    this.shouldScheduleFluidUpdate = false;
                                    return null;
                                }
                            }
                            this.shouldScheduleFluidUpdate = true;
                            return $$32;
                        }
                    }
                }
            }
        }

        @Override
        public boolean shouldScheduleFluidUpdate() {
            return this.shouldScheduleFluidUpdate;
        }

        private static double similarity(int int0, int int1) {
            double $$2 = 25.0;
            return 1.0 - (double) Math.abs(int1 - int0) / 25.0;
        }

        private double calculatePressure(DensityFunction.FunctionContext densityFunctionFunctionContext0, MutableDouble mutableDouble1, Aquifer.FluidStatus aquiferFluidStatus2, Aquifer.FluidStatus aquiferFluidStatus3) {
            int $$4 = densityFunctionFunctionContext0.blockY();
            BlockState $$5 = aquiferFluidStatus2.at($$4);
            BlockState $$6 = aquiferFluidStatus3.at($$4);
            if ((!$$5.m_60713_(Blocks.LAVA) || !$$6.m_60713_(Blocks.WATER)) && (!$$5.m_60713_(Blocks.WATER) || !$$6.m_60713_(Blocks.LAVA))) {
                int $$7 = Math.abs(aquiferFluidStatus2.fluidLevel - aquiferFluidStatus3.fluidLevel);
                if ($$7 == 0) {
                    return 0.0;
                } else {
                    double $$8 = 0.5 * (double) (aquiferFluidStatus2.fluidLevel + aquiferFluidStatus3.fluidLevel);
                    double $$9 = (double) $$4 + 0.5 - $$8;
                    double $$10 = (double) $$7 / 2.0;
                    double $$11 = 0.0;
                    double $$12 = 2.5;
                    double $$13 = 1.5;
                    double $$14 = 3.0;
                    double $$15 = 10.0;
                    double $$16 = 3.0;
                    double $$17 = $$10 - Math.abs($$9);
                    double $$19;
                    if ($$9 > 0.0) {
                        double $$18 = 0.0 + $$17;
                        if ($$18 > 0.0) {
                            $$19 = $$18 / 1.5;
                        } else {
                            $$19 = $$18 / 2.5;
                        }
                    } else {
                        double $$21 = 3.0 + $$17;
                        if ($$21 > 0.0) {
                            $$19 = $$21 / 3.0;
                        } else {
                            $$19 = $$21 / 10.0;
                        }
                    }
                    double $$24 = 2.0;
                    double $$28;
                    if (!($$19 < -2.0) && !($$19 > 2.0)) {
                        double $$26 = mutableDouble1.getValue();
                        if (Double.isNaN($$26)) {
                            double $$27 = this.barrierNoise.compute(densityFunctionFunctionContext0);
                            mutableDouble1.setValue($$27);
                            $$28 = $$27;
                        } else {
                            $$28 = $$26;
                        }
                    } else {
                        $$28 = 0.0;
                    }
                    return 2.0 * ($$28 + $$19);
                }
            } else {
                return 2.0;
            }
        }

        private int gridX(int int0) {
            return Math.floorDiv(int0, 16);
        }

        private int gridY(int int0) {
            return Math.floorDiv(int0, 12);
        }

        private int gridZ(int int0) {
            return Math.floorDiv(int0, 16);
        }

        private Aquifer.FluidStatus getAquiferStatus(long long0) {
            int $$1 = BlockPos.getX(long0);
            int $$2 = BlockPos.getY(long0);
            int $$3 = BlockPos.getZ(long0);
            int $$4 = this.gridX($$1);
            int $$5 = this.gridY($$2);
            int $$6 = this.gridZ($$3);
            int $$7 = this.getIndex($$4, $$5, $$6);
            Aquifer.FluidStatus $$8 = this.aquiferCache[$$7];
            if ($$8 != null) {
                return $$8;
            } else {
                Aquifer.FluidStatus $$9 = this.computeFluid($$1, $$2, $$3);
                this.aquiferCache[$$7] = $$9;
                return $$9;
            }
        }

        private Aquifer.FluidStatus computeFluid(int int0, int int1, int int2) {
            Aquifer.FluidStatus $$3 = this.globalFluidPicker.computeFluid(int0, int1, int2);
            int $$4 = Integer.MAX_VALUE;
            int $$5 = int1 + 12;
            int $$6 = int1 - 12;
            boolean $$7 = false;
            for (int[] $$8 : SURFACE_SAMPLING_OFFSETS_IN_CHUNKS) {
                int $$9 = int0 + SectionPos.sectionToBlockCoord($$8[0]);
                int $$10 = int2 + SectionPos.sectionToBlockCoord($$8[1]);
                int $$11 = this.noiseChunk.preliminarySurfaceLevel($$9, $$10);
                int $$12 = $$11 + 8;
                boolean $$13 = $$8[0] == 0 && $$8[1] == 0;
                if ($$13 && $$6 > $$12) {
                    return $$3;
                }
                boolean $$14 = $$5 > $$12;
                if ($$14 || $$13) {
                    Aquifer.FluidStatus $$15 = this.globalFluidPicker.computeFluid($$9, $$12, $$10);
                    if (!$$15.at($$12).m_60795_()) {
                        if ($$13) {
                            $$7 = true;
                        }
                        if ($$14) {
                            return $$15;
                        }
                    }
                }
                $$4 = Math.min($$4, $$11);
            }
            int $$16 = this.computeSurfaceLevel(int0, int1, int2, $$3, $$4, $$7);
            return new Aquifer.FluidStatus($$16, this.computeFluidType(int0, int1, int2, $$3, $$16));
        }

        private int computeSurfaceLevel(int int0, int int1, int int2, Aquifer.FluidStatus aquiferFluidStatus3, int int4, boolean boolean5) {
            DensityFunction.SinglePointContext $$6 = new DensityFunction.SinglePointContext(int0, int1, int2);
            double $$7;
            double $$8;
            if (OverworldBiomeBuilder.isDeepDarkRegion(this.erosion, this.depth, $$6)) {
                $$7 = -1.0;
                $$8 = -1.0;
            } else {
                int $$9 = int4 + 8 - int1;
                int $$10 = 64;
                double $$11 = boolean5 ? Mth.clampedMap((double) $$9, 0.0, 64.0, 1.0, 0.0) : 0.0;
                double $$12 = Mth.clamp(this.fluidLevelFloodednessNoise.compute($$6), -1.0, 1.0);
                double $$13 = Mth.map($$11, 1.0, 0.0, -0.3, 0.8);
                double $$14 = Mth.map($$11, 1.0, 0.0, -0.8, 0.4);
                $$7 = $$12 - $$14;
                $$8 = $$12 - $$13;
            }
            int $$17;
            if ($$8 > 0.0) {
                $$17 = aquiferFluidStatus3.fluidLevel;
            } else if ($$7 > 0.0) {
                $$17 = this.computeRandomizedFluidSurfaceLevel(int0, int1, int2, int4);
            } else {
                $$17 = DimensionType.WAY_BELOW_MIN_Y;
            }
            return $$17;
        }

        private int computeRandomizedFluidSurfaceLevel(int int0, int int1, int int2, int int3) {
            int $$4 = 16;
            int $$5 = 40;
            int $$6 = Math.floorDiv(int0, 16);
            int $$7 = Math.floorDiv(int1, 40);
            int $$8 = Math.floorDiv(int2, 16);
            int $$9 = $$7 * 40 + 20;
            int $$10 = 10;
            double $$11 = this.fluidLevelSpreadNoise.compute(new DensityFunction.SinglePointContext($$6, $$7, $$8)) * 10.0;
            int $$12 = Mth.quantize($$11, 3);
            int $$13 = $$9 + $$12;
            return Math.min(int3, $$13);
        }

        private BlockState computeFluidType(int int0, int int1, int int2, Aquifer.FluidStatus aquiferFluidStatus3, int int4) {
            BlockState $$5 = aquiferFluidStatus3.fluidType;
            if (int4 <= -10 && int4 != DimensionType.WAY_BELOW_MIN_Y && aquiferFluidStatus3.fluidType != Blocks.LAVA.defaultBlockState()) {
                int $$6 = 64;
                int $$7 = 40;
                int $$8 = Math.floorDiv(int0, 64);
                int $$9 = Math.floorDiv(int1, 40);
                int $$10 = Math.floorDiv(int2, 64);
                double $$11 = this.lavaNoise.compute(new DensityFunction.SinglePointContext($$8, $$9, $$10));
                if (Math.abs($$11) > 0.3) {
                    $$5 = Blocks.LAVA.defaultBlockState();
                }
            }
            return $$5;
        }
    }
}