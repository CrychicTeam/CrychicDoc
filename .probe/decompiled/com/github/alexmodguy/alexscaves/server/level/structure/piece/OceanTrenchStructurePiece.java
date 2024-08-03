package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.mutable.MutableBoolean;

public class OceanTrenchStructurePiece extends AbstractCaveGenerationStructurePiece {

    private BlockState water = Fluids.WATER.m_76145_().createLegacyBlock();

    private static final Direction[] WALL_DIRECTIONS = new Direction[] { Direction.DOWN, Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };

    public OceanTrenchStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(ACStructurePieceRegistry.OCEAN_TRENCH.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius, -64, 100);
    }

    public OceanTrenchStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.OCEAN_TRENCH.get(), tag);
    }

    public OceanTrenchStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        int seaLevel = chunkGen.getSeaLevel();
        boolean flag = false;
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveCliff = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);
        carveCliff.set(cornerX, cornerY, cornerZ);
        carveBelow.set(cornerX, cornerY, cornerZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                MutableBoolean doFloor = new MutableBoolean(false);
                int carveX = cornerX + x;
                int carveZ = cornerZ + z;
                int priorHeight = this.getSeafloorHeight(level, carveX, carveZ);
                float seaFloorExtra = (1.0F + ACMath.sampleNoise2D(carveX - 800, carveZ - 212, 20.0F)) * 5.0F;
                int minY = (int) ((float) (level.m_141937_() + 2) + seaFloorExtra);
                for (int y = priorHeight + 3; y >= minY; y--) {
                    carve.set(carveX, Mth.clamp(y, minY, level.m_151558_()), carveZ);
                    if (carve.m_123342_() <= seaLevel - 2 && this.shouldDig(level, carve, seaLevel, priorHeight)) {
                        if (this.isSeaMountBlocking(carve)) {
                            BlockState prior = this.checkedGetBlock(level, carve);
                            if (!prior.m_60713_(Blocks.BEDROCK)) {
                                this.checkedSetBlock(level, carve, Blocks.TUFF.defaultBlockState());
                            }
                        } else {
                            flag = true;
                            carveBelow.set(carve.m_123341_(), carve.m_123342_() - 1, carve.m_123343_());
                            this.setWater(level, carve, priorHeight);
                            doFloor.setTrue();
                        }
                    }
                }
                if (doFloor.isTrue()) {
                    this.decorateFloor(level, random, carveBelow, seaLevel);
                    for (Direction direction : WALL_DIRECTIONS) {
                        carveCliff.set(carveX, carveBelow.m_123342_() + 1, carveZ);
                        carveCliff.move(direction);
                        BlockState state = level.m_8055_(carveCliff.m_175288_(this.holeCenter.m_123342_()));
                        if (!this.shouldDig(level, carveCliff, seaLevel, priorHeight) && !state.m_204336_(ACTagRegistry.TRENCH_GENERATION_IGNORES) && !state.m_60819_().is(Fluids.WATER)) {
                            BlockPos.MutableBlockPos wallPos = new BlockPos.MutableBlockPos(carveCliff.m_123341_(), level.m_141937_() + 1, carveCliff.m_123343_());
                            boolean seaMountBeneath = false;
                            while (wallPos.m_123342_() < priorHeight - 2) {
                                wallPos.move(0, 1, 0);
                                if (!seaMountBeneath || !level.m_8055_(wallPos).m_60819_().is(Fluids.WATER)) {
                                    this.setWallBlock(level, wallPos, priorHeight);
                                }
                                if (this.isSeaMountBlocking(wallPos)) {
                                    seaMountBeneath = true;
                                }
                            }
                        }
                    }
                }
            }
            if (flag) {
                this.replaceBiomes(level, ACBiomeRegistry.ABYSSAL_CHASM, 16);
            }
        }
    }

    private int getSeafloorHeight(WorldGenLevel level, int x, int z) {
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, level.m_6924_(Heightmap.Types.OCEAN_FLOOR_WG, x, z), z);
        int yPrev = mutableBlockPos.m_123342_();
        mutableBlockPos.setY(level.m_5736_() + 5);
        boolean inFrozenOcean = level.m_204166_(mutableBlockPos).is(ACTagRegistry.TRENCH_IGNORES_STONE_IN);
        mutableBlockPos.setY(yPrev);
        while (this.ignoreHeight(level, inFrozenOcean, this.checkedGetBlock(level, mutableBlockPos), mutableBlockPos) && mutableBlockPos.m_123342_() >= -64) {
            mutableBlockPos.move(0, -1, 0);
        }
        return mutableBlockPos.m_123342_();
    }

    private boolean ignoreHeight(WorldGenLevel level, boolean inFrozenOcean, BlockState blockState, BlockPos.MutableBlockPos mutableBlockPos) {
        return blockState.m_60795_() || blockState.m_204336_(ACTagRegistry.TRENCH_GENERATION_IGNORES) || !blockState.m_60819_().isEmpty() || inFrozenOcean && blockState.m_204336_(BlockTags.OVERWORLD_CARVER_REPLACEABLES) && mutableBlockPos.m_123342_() > level.m_5736_() - 5;
    }

    private void setWallBlock(WorldGenLevel level, BlockPos carve, int priorHeight) {
        BlockState prior = this.checkedGetBlock(level, carve);
        if (!prior.m_60713_(Blocks.BEDROCK) && !prior.m_204336_(ACTagRegistry.TRENCH_GENERATION_IGNORES) && !this.isSeaMountBlocking(carve)) {
            int dist = priorHeight - carve.m_123342_();
            int layerOffset = level.m_213780_().nextInt(2);
            BlockState toSet;
            if (prior.m_60713_(Blocks.LAVA)) {
                toSet = Blocks.MAGMA_BLOCK.defaultBlockState();
            } else if (dist <= 5 + layerOffset) {
                toSet = carve.m_123342_() < 0 ? Blocks.DEEPSLATE.defaultBlockState() : Blocks.STONE.defaultBlockState();
            } else if (dist <= 12 + layerOffset) {
                toSet = Blocks.DEEPSLATE.defaultBlockState();
            } else {
                toSet = ACBlockRegistry.ABYSSMARINE.get().defaultBlockState();
            }
            level.m_7731_(carve, toSet, 128);
        }
    }

    private double getRadiusSq(BlockPos.MutableBlockPos carve) {
        float simplex1 = ACMath.sampleNoise2D(carve.m_123341_(), carve.m_123343_(), 30.0F);
        float simplex2 = ACMath.sampleNoise2D(carve.m_123341_() + 1000, carve.m_123343_() - 1000, 100.0F);
        float widthSimplexNoise1 = 0.8F + 0.2F * (1.0F + simplex1 + simplex2) * 0.5F;
        return (double) (widthSimplexNoise1 * (float) this.radius * (float) this.radius);
    }

    private boolean shouldDig(WorldGenLevel level, BlockPos.MutableBlockPos carve, int seaLevel, int priorHeight) {
        double yDist = this.calcYDist(level, carve, seaLevel, priorHeight);
        double distToCenter = carve.m_203202_((double) this.holeCenter.m_123341_(), (double) (carve.m_123342_() - 1), (double) this.holeCenter.m_123343_());
        double radiusXZ = this.getRadiusSq(carve);
        double cornerAmount = radiusXZ - distToCenter;
        if (cornerAmount > 0.0 && cornerAmount <= 100.0) {
            yDist *= (double) ((float) (cornerAmount / 100.0));
        }
        double targetRadius = yDist * radiusXZ;
        return distToCenter <= targetRadius;
    }

    private boolean isSeaMountBlocking(BlockPos carve) {
        int bottomedY = carve.m_123342_() + 64;
        float heightTarget = 20.0F + ACMath.sampleNoise3D(carve.m_123341_() - 440, 0, carve.m_123343_() + 412, 30.0F) * 10.0F + ACMath.sampleNoise3D(carve.m_123341_() - 110, 0, carve.m_123343_() + 110, 10.0F) * 3.0F;
        float heightScale = (heightTarget - (float) bottomedY) / (heightTarget + 15.0F);
        float sample = ACMath.sampleNoise3D(carve.m_123341_(), 0, carve.m_123343_(), 50.0F) + ACMath.sampleNoise3D(carve.m_123341_() - 440, 0, carve.m_123343_() + 412, 11.0F) * 0.2F + ACMath.sampleNoise3D(carve.m_123341_() - 100, 0, carve.m_123343_() - 400, 100.0F) * 0.3F - 0.1F;
        return sample >= 0.4F * Math.max(0.0F, 1.0F - heightScale);
    }

    private void setWater(WorldGenLevel level, BlockPos.MutableBlockPos center, int priorHeight) {
        this.checkedSetBlock(level, center, this.water);
    }

    private double calcYDist(WorldGenLevel level, BlockPos.MutableBlockPos carve, int seaLevel, int priorHeight) {
        int j = -64 - carve.m_123342_();
        if (carve.m_123342_() < seaLevel + 1 && j <= 0 && priorHeight < seaLevel - 3) {
            float belowSeaBy = ACMath.smin((float) (seaLevel - priorHeight) / 120.0F, 1.0F, 0.2F);
            float bedrockCloseness = ACMath.smin((float) Math.abs(j) / 50.0F - 0.1F, 1.0F, 0.2F);
            float df1 = ACMath.sampleNoise3D(carve.m_123341_(), 0, carve.m_123343_(), 100.0F) * 0.6F;
            float df2 = ACMath.sampleNoise3D(carve.m_123341_() - 450, 0, carve.m_123343_() + 450, 300.0F) * 0.25F;
            return (double) (ACMath.smin(belowSeaBy * (bedrockCloseness - df1) - df2, 0.9F, 0.2F) - df2);
        } else {
            return 0.0;
        }
    }

    private void decorateFloor(WorldGenLevel level, RandomSource rand, BlockPos.MutableBlockPos muckAt, int seaLevel) {
        if (!this.isSeaMountBlocking(muckAt) && muckAt.m_123342_() < seaLevel - 32) {
            this.checkedSetBlock(level, muckAt, ACBlockRegistry.MUCK.get().defaultBlockState());
            for (int i = 0; i < 1 + rand.nextInt(2); i++) {
                muckAt.move(0, -1, 0);
                BlockState at = this.checkedGetBlock(level, muckAt);
                if (at.m_204336_(ACTagRegistry.UNMOVEABLE) || at.m_204336_(ACTagRegistry.TRENCH_GENERATION_IGNORES)) {
                    break;
                }
                if (!at.m_60819_().isEmpty() && !at.m_60819_().is(FluidTags.WATER)) {
                    this.checkedSetBlock(level, muckAt, Blocks.DEEPSLATE.defaultBlockState());
                } else {
                    this.checkedSetBlock(level, muckAt, ACBlockRegistry.MUCK.get().defaultBlockState());
                }
            }
        }
    }
}