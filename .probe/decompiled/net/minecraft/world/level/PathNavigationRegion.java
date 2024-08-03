package net.minecraft.world.level;

import com.google.common.base.Suppliers;
import java.util.List;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.SectionPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkSource;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.VoxelShape;

public class PathNavigationRegion implements BlockGetter, CollisionGetter {

    protected final int centerX;

    protected final int centerZ;

    protected final ChunkAccess[][] chunks;

    protected boolean allEmpty;

    protected final Level level;

    private final Supplier<Holder<Biome>> plains;

    public PathNavigationRegion(Level level0, BlockPos blockPos1, BlockPos blockPos2) {
        this.level = level0;
        this.plains = Suppliers.memoize(() -> level0.registryAccess().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.PLAINS));
        this.centerX = SectionPos.blockToSectionCoord(blockPos1.m_123341_());
        this.centerZ = SectionPos.blockToSectionCoord(blockPos1.m_123343_());
        int $$3 = SectionPos.blockToSectionCoord(blockPos2.m_123341_());
        int $$4 = SectionPos.blockToSectionCoord(blockPos2.m_123343_());
        this.chunks = new ChunkAccess[$$3 - this.centerX + 1][$$4 - this.centerZ + 1];
        ChunkSource $$5 = level0.m_7726_();
        this.allEmpty = true;
        for (int $$6 = this.centerX; $$6 <= $$3; $$6++) {
            for (int $$7 = this.centerZ; $$7 <= $$4; $$7++) {
                this.chunks[$$6 - this.centerX][$$7 - this.centerZ] = $$5.getChunkNow($$6, $$7);
            }
        }
        for (int $$8 = SectionPos.blockToSectionCoord(blockPos1.m_123341_()); $$8 <= SectionPos.blockToSectionCoord(blockPos2.m_123341_()); $$8++) {
            for (int $$9 = SectionPos.blockToSectionCoord(blockPos1.m_123343_()); $$9 <= SectionPos.blockToSectionCoord(blockPos2.m_123343_()); $$9++) {
                ChunkAccess $$10 = this.chunks[$$8 - this.centerX][$$9 - this.centerZ];
                if ($$10 != null && !$$10.isYSpaceEmpty(blockPos1.m_123342_(), blockPos2.m_123342_())) {
                    this.allEmpty = false;
                    return;
                }
            }
        }
    }

    private ChunkAccess getChunk(BlockPos blockPos0) {
        return this.getChunk(SectionPos.blockToSectionCoord(blockPos0.m_123341_()), SectionPos.blockToSectionCoord(blockPos0.m_123343_()));
    }

    private ChunkAccess getChunk(int int0, int int1) {
        int $$2 = int0 - this.centerX;
        int $$3 = int1 - this.centerZ;
        if ($$2 >= 0 && $$2 < this.chunks.length && $$3 >= 0 && $$3 < this.chunks[$$2].length) {
            ChunkAccess $$4 = this.chunks[$$2][$$3];
            return (ChunkAccess) ($$4 != null ? $$4 : new EmptyLevelChunk(this.level, new ChunkPos(int0, int1), (Holder<Biome>) this.plains.get()));
        } else {
            return new EmptyLevelChunk(this.level, new ChunkPos(int0, int1), (Holder<Biome>) this.plains.get());
        }
    }

    @Override
    public WorldBorder getWorldBorder() {
        return this.level.getWorldBorder();
    }

    @Override
    public BlockGetter getChunkForCollisions(int int0, int int1) {
        return this.getChunk(int0, int1);
    }

    @Override
    public List<VoxelShape> getEntityCollisions(@Nullable Entity entity0, AABB aABB1) {
        return List.of();
    }

    @Nullable
    @Override
    public BlockEntity getBlockEntity(BlockPos blockPos0) {
        ChunkAccess $$1 = this.getChunk(blockPos0);
        return $$1.m_7702_(blockPos0);
    }

    @Override
    public BlockState getBlockState(BlockPos blockPos0) {
        if (this.m_151570_(blockPos0)) {
            return Blocks.AIR.defaultBlockState();
        } else {
            ChunkAccess $$1 = this.getChunk(blockPos0);
            return $$1.m_8055_(blockPos0);
        }
    }

    @Override
    public FluidState getFluidState(BlockPos blockPos0) {
        if (this.m_151570_(blockPos0)) {
            return Fluids.EMPTY.defaultFluidState();
        } else {
            ChunkAccess $$1 = this.getChunk(blockPos0);
            return $$1.m_6425_(blockPos0);
        }
    }

    @Override
    public int getMinBuildHeight() {
        return this.level.m_141937_();
    }

    @Override
    public int getHeight() {
        return this.level.m_141928_();
    }

    public ProfilerFiller getProfiler() {
        return this.level.getProfiler();
    }
}