package net.minecraft.server.level;

import javax.annotation.Nullable;
import net.minecraft.SharedConstants;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.levelgen.Heightmap;

public class PlayerRespawnLogic {

    @Nullable
    protected static BlockPos getOverworldRespawnPos(ServerLevel serverLevel0, int int1, int int2) {
        boolean $$3 = serverLevel0.m_6042_().hasCeiling();
        LevelChunk $$4 = serverLevel0.m_6325_(SectionPos.blockToSectionCoord(int1), SectionPos.blockToSectionCoord(int2));
        int $$5 = $$3 ? serverLevel0.getChunkSource().getGenerator().getSpawnHeight(serverLevel0) : $$4.m_5885_(Heightmap.Types.MOTION_BLOCKING, int1 & 15, int2 & 15);
        if ($$5 < serverLevel0.m_141937_()) {
            return null;
        } else {
            int $$6 = $$4.m_5885_(Heightmap.Types.WORLD_SURFACE, int1 & 15, int2 & 15);
            if ($$6 <= $$5 && $$6 > $$4.m_5885_(Heightmap.Types.OCEAN_FLOOR, int1 & 15, int2 & 15)) {
                return null;
            } else {
                BlockPos.MutableBlockPos $$7 = new BlockPos.MutableBlockPos();
                for (int $$8 = $$5 + 1; $$8 >= serverLevel0.m_141937_(); $$8--) {
                    $$7.set(int1, $$8, int2);
                    BlockState $$9 = serverLevel0.m_8055_($$7);
                    if (!$$9.m_60819_().isEmpty()) {
                        break;
                    }
                    if (Block.isFaceFull($$9.m_60812_(serverLevel0, $$7), Direction.UP)) {
                        return $$7.m_7494_().immutable();
                    }
                }
                return null;
            }
        }
    }

    @Nullable
    public static BlockPos getSpawnPosInChunk(ServerLevel serverLevel0, ChunkPos chunkPos1) {
        if (SharedConstants.debugVoidTerrain(chunkPos1)) {
            return null;
        } else {
            for (int $$2 = chunkPos1.getMinBlockX(); $$2 <= chunkPos1.getMaxBlockX(); $$2++) {
                for (int $$3 = chunkPos1.getMinBlockZ(); $$3 <= chunkPos1.getMaxBlockZ(); $$3++) {
                    BlockPos $$4 = getOverworldRespawnPos(serverLevel0, $$2, $$3);
                    if ($$4 != null) {
                        return $$4;
                    }
                }
            }
            return null;
        }
    }
}