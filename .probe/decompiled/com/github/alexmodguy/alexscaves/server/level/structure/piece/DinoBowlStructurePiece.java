package com.github.alexmodguy.alexscaves.server.level.structure.piece;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import com.github.alexmodguy.alexscaves.server.misc.VoronoiGenerator;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
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

public class DinoBowlStructurePiece extends AbstractCaveGenerationStructurePiece {

    private VoronoiGenerator voronoiGenerator;

    public DinoBowlStructurePiece(BlockPos chunkCorner, BlockPos holeCenter, int bowlHeight, int bowlRadius) {
        super(ACStructurePieceRegistry.DINO_BOWL.get(), chunkCorner, holeCenter, bowlHeight, bowlRadius);
    }

    public DinoBowlStructurePiece(CompoundTag tag) {
        super(ACStructurePieceRegistry.DINO_BOWL.get(), tag);
    }

    public DinoBowlStructurePiece(StructurePieceSerializationContext structurePieceSerializationContext, CompoundTag tag) {
        this(tag);
    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager featureManager, ChunkGenerator chunkGen, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos blockPos) {
        if (this.voronoiGenerator == null) {
            this.voronoiGenerator = new VoronoiGenerator(level.getSeed());
            this.voronoiGenerator.setOffsetAmount(0.6F);
        }
        int cornerX = this.chunkCorner.m_123341_();
        int cornerY = this.chunkCorner.m_123342_();
        int cornerZ = this.chunkCorner.m_123343_();
        boolean flag = false;
        BlockPos.MutableBlockPos carve = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveAbove = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos carveBelow = new BlockPos.MutableBlockPos();
        carve.set(cornerX, cornerY, cornerZ);
        carveAbove.set(cornerX, cornerY, cornerZ);
        carveBelow.set(cornerX, cornerY, cornerZ);
        for (int x = 0; x < 16; x++) {
            for (int z = 0; z < 16; z++) {
                MutableBoolean doFloor = new MutableBoolean(false);
                for (int y = 15; y >= 0; y--) {
                    carve.set(cornerX + x, Mth.clamp(cornerY + y, level.m_141937_(), level.m_151558_()), cornerZ + z);
                    if (this.inCircle(carve) && !this.checkedGetBlock(level, carve).m_60713_(Blocks.BEDROCK)) {
                        flag = true;
                        this.checkedSetBlock(level, carve, Blocks.CAVE_AIR.defaultBlockState());
                        this.surroundCornerOfLiquid(level, carve);
                        carveBelow.set(carve.m_123341_(), carve.m_123342_() - 1, carve.m_123343_());
                        doFloor.setTrue();
                    }
                }
                if (doFloor.isTrue()) {
                    BlockState floor = this.checkedGetBlock(level, carveBelow);
                    if (!floor.m_60795_() && !floor.m_204336_(ACTagRegistry.VOLCANO_BLOCKS)) {
                        this.decorateFloor(level, random, carveBelow.immutable());
                    }
                    doFloor.setFalse();
                }
            }
        }
        if (flag) {
            this.replaceBiomes(level, ACBiomeRegistry.PRIMORDIAL_CAVES, 32);
        }
    }

    private void surroundCornerOfLiquid(WorldGenLevel level, Vec3i center) {
        BlockPos.MutableBlockPos offset = new BlockPos.MutableBlockPos();
        for (Direction dir : Direction.values()) {
            offset.set(center);
            offset.move(dir);
            BlockState state = this.checkedGetBlock(level, offset);
            if (!state.m_60819_().isEmpty()) {
                this.checkedSetBlock(level, offset, Blocks.SANDSTONE.defaultBlockState());
            }
        }
    }

    private boolean inCircle(BlockPos carve) {
        float wallNoise = (ACMath.sampleNoise3D(carve.m_123341_(), (int) ((float) carve.m_123342_() * 0.1F), carve.m_123343_(), 40.0F) + 1.0F) * 0.5F;
        double yDist = (double) ACMath.smin(1.0F - (float) Math.abs(this.holeCenter.m_123342_() - carve.m_123342_()) / ((float) this.height * 0.5F), 1.0F, 0.3F);
        double distToCenter = carve.m_203202_((double) this.holeCenter.m_123341_(), (double) carve.m_123342_(), (double) this.holeCenter.m_123343_());
        double targetRadius = yDist * (double) ((float) this.radius * wallNoise) * (double) this.radius;
        return distToCenter < targetRadius;
    }

    private void decorateFloor(WorldGenLevel level, RandomSource rand, BlockPos carveBelow) {
        BlockState grass = Blocks.GRASS_BLOCK.defaultBlockState();
        BlockState dirt = Blocks.DIRT.defaultBlockState();
        this.checkedSetBlock(level, carveBelow, grass);
        for (int i = 0; i < 1 + rand.nextInt(2); i++) {
            carveBelow = carveBelow.below();
            this.checkedSetBlock(level, carveBelow, dirt);
        }
    }
}