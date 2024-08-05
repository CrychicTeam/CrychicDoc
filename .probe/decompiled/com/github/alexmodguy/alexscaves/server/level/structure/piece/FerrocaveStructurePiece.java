package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.block.ACBlockRegistry;
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

public class FerrocaveStructurePiece extends AbstractCaveGenerationStructurePiece {

    public FerrocaveStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(ACStructurePieceRegistry.FERROCAVE.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius);
    }

    public FerrocaveStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.FERROCAVE.get(), tag);
    }

    public FerrocaveStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveAbove = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);
        carveAbove.set(cornerX, cornerY, cornerZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                for (int y = 15; y >= 0; y--) {
                    carve.set(cornerX + x, Mth.clamp(cornerY + y, level.m_141937_(), level.m_151558_()), cornerZ + z);
                    carveAbove.set(carve.m_123341_(), carve.m_123342_() + 1, carve.m_123343_());
                    if (this.inCircle(carve) && !this.checkedGetBlock(level, carve).m_60713_(Blocks.BEDROCK)) {
                        this.checkedSetBlock(level, carve, Blocks.CAVE_AIR.defaultBlockState());
                        this.surroundCornerOfLiquid(level, carve);
                    }
                }
            }
        }
    }

    private void surroundCornerOfLiquid(WorldGenLevel level, BlockPos.MutableBlockPos center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            offset.set(center);
            offset.move(dir);
            BlockState state = this.checkedGetBlock(level, offset);
            if (!state.m_60819_().isEmpty()) {
                this.checkedSetBlock(level, offset, ACBlockRegistry.GALENA.get().defaultBlockState());
            }
        }
    }

    private boolean inCircle(BlockPos carve) {
        float df1 = (ACMath.sampleNoise3D(carve.m_123341_(), carve.m_123342_(), carve.m_123343_(), 30.0F) + 1.0F) * 0.5F;
        float df2 = ACMath.sampleNoise3D(carve.m_123341_() - 1200, carve.m_123342_() + 100, carve.m_123343_() + 120, 10.0F) * 0.15F;
        float innerCircleOrb = ACMath.sampleNoise3D(carve.m_123341_() + 400, carve.m_123342_() + 40, carve.m_123343_() - 600, 20.0F);
        double df1Smooth = (double) ACMath.smin(df1 + df2, 1.0F, 0.1F);
        double yDist = (double) ACMath.smin(1.0F - (float) Math.abs(this.holeCenter.m_123342_() - carve.m_123342_()) / ((float) this.height * 0.5F), 1.0F, 0.3F);
        double distToCenter = carve.m_203202_((double) this.holeCenter.m_123341_(), (double) carve.m_123342_(), (double) this.holeCenter.m_123343_());
        double targetRadius = yDist * (double) this.radius * df1Smooth * (double) this.radius;
        return distToCenter < targetRadius && (innerCircleOrb > -0.5F || innerCircleOrb < -0.75F);
    }
}