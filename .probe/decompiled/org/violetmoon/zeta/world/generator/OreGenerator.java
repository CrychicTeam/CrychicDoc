package org.violetmoon.zeta.world.generator;

import java.util.BitSet;
import java.util.function.BooleanSupplier;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.WorldGenRegion;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import org.violetmoon.zeta.config.type.DimensionConfig;
import org.violetmoon.zeta.config.type.OrePocketConfig;

public class OreGenerator extends Generator {

    public static final Predicate<BlockState> STONE_MATCHER = state -> {
        if (state == null) {
            return false;
        } else {
            Block block = state.m_60734_();
            return block == Blocks.STONE || block == Blocks.DEEPSLATE;
        }
    };

    public static final Predicate<BlockState> NETHERRACK_MATCHER = state -> {
        if (state == null) {
            return false;
        } else {
            Block block = state.m_60734_();
            return block == Blocks.NETHERRACK;
        }
    };

    public static final Predicate<BlockState> ENDSTONE_MATCHER = state -> {
        if (state == null) {
            return false;
        } else {
            Block block = state.m_60734_();
            return block == Blocks.END_STONE;
        }
    };

    public static final Predicate<BlockState> ALL_DIMS_STONE_MATCHER = STONE_MATCHER.or(NETHERRACK_MATCHER).or(ENDSTONE_MATCHER);

    private final OrePocketConfig oreConfig;

    private final BlockState placeState;

    private final Predicate<BlockState> matcher;

    public OreGenerator(DimensionConfig dimConfig, OrePocketConfig oreConfig, BlockState placeState, Predicate<BlockState> matcher, BooleanSupplier condition) {
        super(dimConfig, condition);
        this.oreConfig = oreConfig;
        this.placeState = placeState;
        this.matcher = matcher;
    }

    @Override
    public void generateChunk(WorldGenRegion worldIn, ChunkGenerator generator, RandomSource rand, BlockPos pos) {
        this.oreConfig.forEach(pos, rand, npos -> this.place(worldIn, rand, npos));
    }

    public boolean place(LevelAccessor worldIn, RandomSource rand, BlockPos pos) {
        float angle = rand.nextFloat() * (float) Math.PI;
        float factor = (float) this.oreConfig.clusterSize / 8.0F;
        int minFactor = Mth.ceil(((float) this.oreConfig.clusterSize / 16.0F * 2.0F + 1.0F) / 2.0F);
        double x1 = (double) ((float) pos.m_123341_() + Mth.sin(angle) * factor);
        double x2 = (double) ((float) pos.m_123341_() - Mth.sin(angle) * factor);
        double z1 = (double) ((float) pos.m_123343_() + Mth.cos(angle) * factor);
        double z2 = (double) ((float) pos.m_123343_() - Mth.cos(angle) * factor);
        double y1 = (double) (pos.m_123342_() + rand.nextInt(3) - 2);
        double y2 = (double) (pos.m_123342_() + rand.nextInt(3) - 2);
        int maxX = pos.m_123341_() - Mth.ceil(factor) - minFactor;
        int maxY = pos.m_123342_() - 2 - minFactor;
        int maxZ = pos.m_123343_() - Mth.ceil(factor) - minFactor;
        int searchSize = 2 * (Mth.ceil(factor) + minFactor);
        int secondarySearchSize = 2 * (2 + minFactor);
        Heightmap.Types hm = worldIn instanceof WorldGenRegion ? Heightmap.Types.OCEAN_FLOOR_WG : Heightmap.Types.WORLD_SURFACE;
        for (int x = maxX; x <= maxX + searchSize; x++) {
            for (int z = maxZ; z <= maxZ + searchSize; z++) {
                if (maxY <= worldIn.m_6924_(hm, x, z)) {
                    return this.doPlace(worldIn, rand, x1, x2, z1, z2, y1, y2, maxX, maxY, maxZ, searchSize, secondarySearchSize);
                }
            }
        }
        return false;
    }

    protected boolean doPlace(LevelAccessor worldIn, RandomSource random, double x1, double x2, double z1, double z2, double y1, double y2, int maxX, int maxY, int maxZ, int searchSize, int secondarySearchSize) {
        int blocksPlaced = 0;
        BitSet bitset = new BitSet(searchSize * secondarySearchSize * searchSize);
        BlockPos.MutableBlockPos pos = new BlockPos.MutableBlockPos();
        double[] clusterInfo = new double[this.oreConfig.clusterSize * 4];
        for (int clusterSize = 0; clusterSize < this.oreConfig.clusterSize; clusterSize++) {
            float clusterSizeRatio = (float) clusterSize / (float) this.oreConfig.clusterSize;
            double x = Mth.lerp((double) clusterSizeRatio, x1, x2);
            double y = Mth.lerp((double) clusterSizeRatio, y1, y2);
            double z = Mth.lerp((double) clusterSizeRatio, z1, z2);
            double randSize = random.nextDouble() * (double) this.oreConfig.clusterSize / 16.0;
            double size = ((double) (Mth.sin((float) Math.PI * clusterSizeRatio) + 1.0F) * randSize + 1.0) / 2.0;
            clusterInfo[clusterSize * 4] = x;
            clusterInfo[clusterSize * 4 + 1] = y;
            clusterInfo[clusterSize * 4 + 2] = z;
            clusterInfo[clusterSize * 4 + 3] = size;
        }
        for (int size1 = 0; size1 < this.oreConfig.clusterSize - 1; size1++) {
            if (!(clusterInfo[size1 * 4 + 3] <= 0.0)) {
                for (int size2 = size1 + 1; size2 < this.oreConfig.clusterSize; size2++) {
                    if (!(clusterInfo[size2 * 4 + 3] <= 0.0)) {
                        double dX = clusterInfo[size1 * 4] - clusterInfo[size2 * 4];
                        double dY = clusterInfo[size1 * 4 + 1] - clusterInfo[size2 * 4 + 1];
                        double dZ = clusterInfo[size1 * 4 + 2] - clusterInfo[size2 * 4 + 2];
                        double dSize = clusterInfo[size1 * 4 + 3] - clusterInfo[size2 * 4 + 3];
                        if (dSize * dSize > dX * dX + dY * dY + dZ * dZ) {
                            if (dSize > 0.0) {
                                clusterInfo[size2 * 4 + 3] = -1.0;
                            } else {
                                clusterInfo[size1 * 4 + 3] = -1.0;
                            }
                        }
                    }
                }
            }
        }
        for (int clusterSize = 0; clusterSize < this.oreConfig.clusterSize; clusterSize++) {
            double size = clusterInfo[clusterSize * 4 + 3];
            if (size >= 0.0) {
                double x = clusterInfo[clusterSize * 4];
                double y = clusterInfo[clusterSize * 4 + 1];
                double z = clusterInfo[clusterSize * 4 + 2];
                int clusterMinX = Math.max(Mth.floor(x - size), maxX);
                int clusterMinY = Math.max(Mth.floor(y - size), maxY);
                int clusterMinZ = Math.max(Mth.floor(z - size), maxZ);
                int clusterMaxX = Math.max(Mth.floor(x + size), clusterMinX);
                int clusterMaxY = Math.max(Mth.floor(y + size), clusterMinY);
                int clusterMaxZ = Math.max(Mth.floor(z + size), clusterMinZ);
                for (int clusterX = clusterMinX; clusterX <= clusterMaxX; clusterX++) {
                    double xSize = ((double) clusterX + 0.5 - x) / size;
                    if (xSize * xSize < 1.0) {
                        for (int clusterY = clusterMinY; clusterY <= clusterMaxY; clusterY++) {
                            double ySize = ((double) clusterY + 0.5 - y) / size;
                            if (xSize * xSize + ySize * ySize < 1.0) {
                                for (int clusterZ = clusterMinZ; clusterZ <= clusterMaxZ; clusterZ++) {
                                    double zSize = ((double) clusterZ + 0.5 - z) / size;
                                    if (xSize * xSize + ySize * ySize + zSize * zSize < 1.0) {
                                        int index = clusterX - maxX + (clusterY - maxY) * searchSize + (clusterZ - maxZ) * searchSize * secondarySearchSize;
                                        if (!bitset.get(index)) {
                                            bitset.set(index);
                                            pos.set(clusterX, clusterY, clusterZ);
                                            if (this.matcher.test(worldIn.m_8055_(pos))) {
                                                worldIn.m_7731_(pos, this.placeState, 2);
                                                blocksPlaced++;
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
        return blocksPlaced > 0;
    }
}