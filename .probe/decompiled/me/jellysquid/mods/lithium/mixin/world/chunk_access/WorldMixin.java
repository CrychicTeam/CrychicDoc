package me.jellysquid.mods.lithium.mixin.world.chunk_access;

import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin({ Level.class })
public abstract class WorldMixin implements LevelAccessor {

    @Overwrite
    public LevelChunk getChunkAt(BlockPos pos) {
        return (LevelChunk) this.getChunk(pos);
    }

    @Override
    public ChunkAccess getChunk(BlockPos pos) {
        return this.m_6522_(SectionPos.blockToSectionCoord(pos.m_123341_()), SectionPos.blockToSectionCoord(pos.m_123343_()), ChunkStatus.FULL, true);
    }

    @Overwrite
    public LevelChunk getChunk(int chunkX, int chunkZ) {
        return (LevelChunk) this.m_6522_(chunkX, chunkZ, ChunkStatus.FULL, true);
    }

    @Override
    public ChunkAccess getChunk(int chunkX, int chunkZ, ChunkStatus status) {
        return this.m_6522_(chunkX, chunkZ, status, true);
    }

    @Override
    public BlockGetter getChunkForCollisions(int chunkX, int chunkZ) {
        return this.m_6522_(chunkX, chunkZ, ChunkStatus.FULL, false);
    }
}