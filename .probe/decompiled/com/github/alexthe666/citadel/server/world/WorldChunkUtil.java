package com.github.alexthe666.citadel.server.world;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkStatus;

public class WorldChunkUtil {

    public static boolean isBlockLoaded(LevelAccessor world, BlockPos pos) {
        return isChunkLoaded(world, pos.m_123341_() >> 4, pos.m_123343_() >> 4);
    }

    public static boolean isChunkLoaded(LevelAccessor world, int x, int z) {
        if (world.getChunkSource() instanceof ServerChunkCache) {
            ChunkHolder holder = ((ServerChunkCache) world.getChunkSource()).chunkMap.getVisibleChunkIfPresent(ChunkPos.asLong(x, z));
            return holder != null ? ((Either) holder.getFullChunkFuture().getNow(ChunkHolder.UNLOADED_LEVEL_CHUNK)).left().isPresent() : false;
        } else {
            return world.m_6522_(x, z, ChunkStatus.FULL, false) != null;
        }
    }

    public static boolean isChunkLoaded(LevelAccessor world, ChunkPos pos) {
        return isChunkLoaded(world, pos.x, pos.z);
    }

    public static boolean isEntityBlockLoaded(LevelAccessor world, BlockPos pos) {
        return isEntityChunkLoaded(world, pos.m_123341_() >> 4, pos.m_123343_() >> 4);
    }

    public static boolean isEntityChunkLoaded(LevelAccessor world, int x, int z) {
        return isEntityChunkLoaded(world, new ChunkPos(x, z));
    }

    public static boolean isEntityChunkLoaded(LevelAccessor world, ChunkPos pos) {
        return !(world instanceof ServerLevel) ? isChunkLoaded(world, pos) : isChunkLoaded(world, pos) && ((ServerLevel) world).isPositionEntityTicking(pos.getWorldPosition());
    }
}