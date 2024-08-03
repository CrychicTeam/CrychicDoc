package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
import com.github.alexmodguy.alexscaves.server.block.fluid.ACFluidRegistry;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;
import net.minecraft.world.level.material.FluidState;

public class AcidPitStructurePiece extends AbstractCaveGenerationStructurePiece {

    public AcidPitStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(ACStructurePieceRegistry.ACID_PIT.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius);
    }

    public AcidPitStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.ACID_PIT.get(), tag);
    }

    public AcidPitStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        boolean flag = false;
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveAbove = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);
        carveAbove.set(cornerX, cornerY, cornerZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y >= 0; y--) {
                    carve.set(cornerX + x, Mth.clamp(cornerY + y, level.m_141937_(), level.m_151558_()), cornerZ + z);
                    carveAbove.set(carve.m_123341_(), carve.m_123342_() + 1, carve.m_123343_());
                    float widthSimplexNoise1 = Math.min(ACMath.sampleNoise3D(carve.m_123341_(), carve.m_123342_(), carve.m_123343_(), (float) this.radius) - 0.5F, 1.0F) * 0.94F;
                    float heightSimplexNoise1 = ACMath.sampleNoise3D(carve.m_123341_() + 440, 0, carve.m_123343_() - 440, 20.0F) * 0.5F + 0.5F;
                    double yDist = (double) ACMath.smin(1.0F - (float) Math.abs(this.holeCenter.m_123342_() - carve.m_123342_()) / ((float) this.height * heightSimplexNoise1), 0.7F, 0.3F);
                    double distToCenter = carve.m_203202_((double) this.holeCenter.m_123341_(), (double) carve.m_123342_(), (double) this.holeCenter.m_123343_());
                    double targetRadius = yDist * (double) ((float) this.radius + widthSimplexNoise1 * (float) this.radius) * (double) this.radius;
                    double acidRadius = targetRadius - targetRadius * 0.25;
                    if (distToCenter <= targetRadius) {
                        FluidState fluidState = this.checkedGetBlock(level, carve).m_60819_();
                        flag = true;
                        if (this.isPillarBlocking(carve, yDist)) {
                            if (!fluidState.isEmpty()) {
                                this.checkedSetBlock(level, carve, ACBlockRegistry.RADROCK.get().defaultBlockState());
                            }
                        } else if (carve.m_123342_() < -10) {
                            this.checkedSetBlock(level, carve, ACBlockRegistry.ACID.get().m_49966_());
                            this.surroundCornerLiquid(level, carve);
                        } else {
                            if (this.isTouchingNonAcidLiquid(level, carve)) {
                                this.surroundCornerMud(level, carve);
                                this.checkedSetBlock(level, carve, Blocks.MUD.defaultBlockState());
                            }
                            this.checkedSetBlock(level, carve, Blocks.CAVE_AIR.defaultBlockState());
                        }
                    }
                }
            }
        }
        if (flag) {
            this.replaceBiomes(level, ACBiomeRegistry.TOXIC_CAVES, 20);
        }
    }

    private boolean isPillarBlocking(BlockPos.MutableBlockPos carve, double yDist) {
        float sample = ACMath.sampleNoise3D(carve.m_123341_(), 0, carve.m_123343_(), 40.0F) + ACMath.sampleNoise3D(carve.m_123341_() - 440, 0, carve.m_123343_() + 412, 15.0F) * 0.2F + ACMath.sampleNoise3D(carve.m_123341_() - 100, carve.m_123342_(), carve.m_123343_() - 400, 100.0F) * 0.9F + 0.6F;
        float f = ACMath.smin((float) yDist / 0.67F, 1.0F, 0.2F) + 1.0F;
        return sample >= 0.35F * f && sample <= ACMath.smin(1.0F, (float) yDist / 0.67F + 0.35F, 0.2F) * f;
    }

    private void surroundCornerLiquid(WorldGenLevel level, BlockPos.MutableBlockPos center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : ACMath.NOT_UP_DIRECTIONS) {
            offset.set(center);
            offset.move(dir);
            BlockState state = this.checkedGetBlockIgnoreY(level, offset);
            if (!state.m_60819_().is(ACFluidRegistry.ACID_FLUID_SOURCE.get())) {
                this.checkedSetBlock(level, offset, Blocks.MUD.defaultBlockState());
            }
        }
    }

    private void surroundCornerMud(WorldGenLevel level, BlockPos.MutableBlockPos center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            offset.set(center);
            offset.move(dir);
            BlockState state = this.checkedGetBlock(level, offset);
            if (!state.m_60819_().isEmpty() && !state.m_60819_().is(ACFluidRegistry.ACID_FLUID_SOURCE.get())) {
                this.checkedSetBlock(level, offset, Blocks.MUD.defaultBlockState());
            }
        }
    }

    private boolean isTouchingNonAcidLiquid(WorldGenLevel level, BlockPos.MutableBlockPos center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            offset.set(center);
            offset.move(dir);
            FluidState state = this.checkedGetBlock(level, offset).m_60819_();
            if (!state.isEmpty() && !state.is(ACFluidRegistry.ACID_FLUID_SOURCE.get())) {
                return true;
            }
        }
        FluidState state = this.checkedGetBlock(level, center).m_60819_();
        return !state.isEmpty() && !state.is(ACFluidRegistry.ACID_FLUID_SOURCE.get());
    }
}