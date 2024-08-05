package net.minecraft.client.renderer.chunk;

import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.chunk.LevelChunk;

public class RenderRegionCache {

    private final Long2ObjectMap<RenderRegionCache.ChunkInfo> chunkInfoCache = new Long2ObjectOpenHashMap();

    @Nullable
    public RenderChunkRegion createRegion(Level level0, BlockPos blockPos1, BlockPos blockPos2, int int3) {
        int $$4 = SectionPos.blockToSectionCoord(blockPos1.m_123341_() - int3);
        int $$5 = SectionPos.blockToSectionCoord(blockPos1.m_123343_() - int3);
        int $$6 = SectionPos.blockToSectionCoord(blockPos2.m_123341_() + int3);
        int $$7 = SectionPos.blockToSectionCoord(blockPos2.m_123343_() + int3);
        RenderRegionCache.ChunkInfo[][] $$8 = new RenderRegionCache.ChunkInfo[$$6 - $$4 + 1][$$7 - $$5 + 1];
        for (int $$9 = $$4; $$9 <= $$6; $$9++) {
            for (int $$10 = $$5; $$10 <= $$7; $$10++) {
                $$8[$$9 - $$4][$$10 - $$5] = (RenderRegionCache.ChunkInfo) this.chunkInfoCache.computeIfAbsent(ChunkPos.asLong($$9, $$10), p_200464_ -> new RenderRegionCache.ChunkInfo(level0.getChunk(ChunkPos.getX(p_200464_), ChunkPos.getZ(p_200464_))));
            }
        }
        if (isAllEmpty(blockPos1, blockPos2, $$4, $$5, $$8)) {
            return null;
        } else {
            RenderChunk[][] $$11 = new RenderChunk[$$6 - $$4 + 1][$$7 - $$5 + 1];
            for (int $$12 = $$4; $$12 <= $$6; $$12++) {
                for (int $$13 = $$5; $$13 <= $$7; $$13++) {
                    $$11[$$12 - $$4][$$13 - $$5] = $$8[$$12 - $$4][$$13 - $$5].renderChunk();
                }
            }
            return new RenderChunkRegion(level0, $$4, $$5, $$11);
        }
    }

    private static boolean isAllEmpty(BlockPos blockPos0, BlockPos blockPos1, int int2, int int3, RenderRegionCache.ChunkInfo[][] renderRegionCacheChunkInfo4) {
        int $$5 = SectionPos.blockToSectionCoord(blockPos0.m_123341_());
        int $$6 = SectionPos.blockToSectionCoord(blockPos0.m_123343_());
        int $$7 = SectionPos.blockToSectionCoord(blockPos1.m_123341_());
        int $$8 = SectionPos.blockToSectionCoord(blockPos1.m_123343_());
        for (int $$9 = $$5; $$9 <= $$7; $$9++) {
            for (int $$10 = $$6; $$10 <= $$8; $$10++) {
                LevelChunk $$11 = renderRegionCacheChunkInfo4[$$9 - int2][$$10 - int3].chunk();
                if (!$$11.m_5566_(blockPos0.m_123342_(), blockPos1.m_123342_())) {
                    return false;
                }
            }
        }
        return true;
    }

    static final class ChunkInfo {

        private final LevelChunk chunk;

        @Nullable
        private RenderChunk renderChunk;

        ChunkInfo(LevelChunk levelChunk0) {
            this.chunk = levelChunk0;
        }

        public LevelChunk chunk() {
            return this.chunk;
        }

        public RenderChunk renderChunk() {
            if (this.renderChunk == null) {
                this.renderChunk = new RenderChunk(this.chunk);
            }
            return this.renderChunk;
        }
    }
}