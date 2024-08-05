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
import org.apache.commons.lang3.mutable.MutableBoolean;

public class ForlornCanyonStructurePiece extends AbstractCaveGenerationStructurePiece {

    public ForlornCanyonStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(ACStructurePieceRegistry.FORLORN_CANYON.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius);
    }

    public ForlornCanyonStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.FORLORN_CANYON.get(), tag);
    }

    public ForlornCanyonStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                MutableBoolean doFloor = new MutableBoolean(false);
                for (int y = 15; y >= 0; y--) {
                    carve.set(cornerX + x, Mth.clamp(cornerY + y, level.m_141937_(), level.m_151558_()), cornerZ + z);
                    if (this.inCircle(carve) && !this.checkedGetBlock(level, carve).m_60713_(Blocks.BEDROCK)) {
                        this.checkedSetBlock(level, carve, Blocks.CAVE_AIR.defaultBlockState());
                        this.surroundCornerOfLiquid(level, carve);
                        carveBelow.set(carve.m_123341_(), carve.m_123342_() - 1, carve.m_123343_());
                        doFloor.setTrue();
                    } else if (doFloor.isTrue()) {
                        break;
                    }
                }
                if (doFloor.isTrue() && !this.checkedGetBlock(level, carveBelow).m_60795_()) {
                    this.decorateFloor(level, random, carveBelow);
                    doFloor.setFalse();
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
                this.checkedSetBlock(level, offset, ACBlockRegistry.GUANOSTONE.get().defaultBlockState());
            }
        }
    }

    private boolean inCircle(BlockPos.MutableBlockPos carve) {
        float pillarNoise = (ACMath.sampleNoise3D(carve.m_123341_(), (int) ((float) carve.m_123342_() * 0.4F), carve.m_123343_(), 30.0F) + 1.0F) * 0.5F;
        float verticalNoise = (ACMath.sampleNoise2D(carve.m_123341_(), carve.m_123343_(), 50.0F) + 1.0F) * 0.2F - (ACMath.smin(ACMath.sampleNoise2D(carve.m_123341_(), carve.m_123343_(), 20.0F), -0.5F, 0.1F) + 0.5F) * 0.7F;
        double distToCenter = carve.m_203202_((double) this.holeCenter.m_123341_(), (double) carve.m_123342_(), (double) this.holeCenter.m_123343_());
        float f = this.getHeightOf(carve);
        float f1 = (float) Math.pow((double) this.canyonStep(f, 10), 2.5);
        float rawHeight = (float) Math.abs(this.holeCenter.m_123342_() - carve.m_123342_()) / ((float) this.height * 0.5F);
        float reverseRawHeight = 1.0F - rawHeight;
        double yDist = (double) ACMath.smin((float) Math.pow((double) reverseRawHeight, 0.3F), 1.0F, 0.1F);
        double targetRadius = yDist * (double) ((float) this.radius * pillarNoise * f1) * (double) this.radius;
        return distToCenter < targetRadius && rawHeight < 1.0F - verticalNoise;
    }

    private float getHeightOf(BlockPos.MutableBlockPos carve) {
        int halfHeight = this.height / 2;
        return carve.m_123342_() <= this.holeCenter.m_123342_() + halfHeight + 1 && carve.m_123342_() >= this.holeCenter.m_123342_() - halfHeight ? 1.0F - (float) (this.holeCenter.m_123342_() + halfHeight - carve.m_123342_()) / (float) (this.height * 2) : 0.0F;
    }

    private float canyonStep(float heightScale, int scaleTo) {
        int clampTo100 = (int) (heightScale * (float) scaleTo * (float) scaleTo);
        return Mth.clamp((float) Math.round((float) clampTo100 / (float) scaleTo) / (float) scaleTo, 0.0F, 1.0F);
    }

    private void decorateFloor(WorldGenLevel level, RandomSource rand, BlockPos.MutableBlockPos carveBelow) {
        float floorNoise = (ACMath.sampleNoise2D(carveBelow.m_123341_(), carveBelow.m_123343_(), 50.0F) + 1.0F) * 0.5F;
        this.checkedSetBlock(level, carveBelow, Blocks.PACKED_MUD.defaultBlockState());
        for (int i = 0; (double) i < Math.ceil((double) (floorNoise * 3.0F)); i++) {
            carveBelow.move(0, 1, 0);
            this.checkedSetBlock(level, carveBelow, Blocks.PACKED_MUD.defaultBlockState());
        }
    }
}