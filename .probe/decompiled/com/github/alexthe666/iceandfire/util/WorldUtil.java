package com.github.alexthe666.iceandfire.util;

import com.github.alexthe666.iceandfire.world.IafWorldData;
import com.github.alexthe666.iceandfire.world.IafWorldRegistry;
import com.mojang.datafixers.util.Either;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Difficulty;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.dimension.BuiltinDimensionTypes;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;

public class WorldUtil {

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

    public static void markChunkDirty(Level world, BlockPos pos) {
        if (isBlockLoaded(world, pos)) {
            world.getChunk(pos.m_123341_() >> 4, pos.m_123343_() >> 4).m_8092_(true);
            BlockState state = world.getBlockState(pos);
            world.sendBlockUpdated(pos, state, state, 3);
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

    public static boolean isAABBLoaded(Level world, AABB box) {
        return isChunkLoaded(world, (int) box.minX >> 4, (int) box.minZ >> 4) && isChunkLoaded(world, (int) box.maxX >> 4, (int) box.maxZ >> 4);
    }

    public static boolean isPastTime(Level world, int pastTime) {
        return world.getDayTime() % 24000L <= (long) pastTime;
    }

    public static boolean isOverworldType(@NotNull Level world) {
        return isOfWorldType(world, BuiltinDimensionTypes.OVERWORLD);
    }

    public static boolean isNetherType(@NotNull Level world) {
        return isOfWorldType(world, BuiltinDimensionTypes.NETHER);
    }

    public static boolean isOfWorldType(@NotNull Level world, @NotNull ResourceKey<DimensionType> type) {
        RegistryAccess dynRegistries = world.registryAccess();
        ResourceLocation loc = ((Registry) dynRegistries.registry(Registries.DIMENSION_TYPE).get()).getKey(world.dimensionType());
        if (loc == null) {
            return world.isClientSide ? world.dimensionType().effectsLocation().equals(type.location()) : false;
        } else {
            ResourceKey<DimensionType> regKey = ResourceKey.create(Registries.DIMENSION_TYPE, loc);
            return regKey == type;
        }
    }

    public static boolean isPeaceful(@NotNull Level world) {
        return !world.getLevelData().getGameRules().getBoolean(GameRules.RULE_DOMOBSPAWNING) || world.m_46791_().equals(Difficulty.PEACEFUL);
    }

    public static int getDimensionMaxHeight(DimensionType dimensionType) {
        return dimensionType.logicalHeight() + dimensionType.minY();
    }

    public static int getDimensionMinHeight(DimensionType dimensionType) {
        return dimensionType.minY();
    }

    public static boolean isInWorldHeight(int yBlock, Level world) {
        DimensionType dimensionType = world.dimensionType();
        return yBlock > getDimensionMinHeight(dimensionType) && yBlock < getDimensionMaxHeight(dimensionType);
    }

    public static boolean canGenerate(int configChance, WorldGenLevel level, RandomSource random, BlockPos origin, String id, boolean checkFluid) {
        return canGenerate(configChance, level, random, origin, id, IafWorldData.FeatureType.SURFACE, checkFluid);
    }

    public static boolean canGenerate(int configChance, WorldGenLevel level, RandomSource random, BlockPos origin, String id, IafWorldData.FeatureType type, boolean checkFluid) {
        boolean canGenerate = random.nextInt(configChance) == 0 && IafWorldRegistry.isFarEnoughFromSpawn(level, origin) && IafWorldRegistry.isFarEnoughFromDangerousGen(level, origin, id, type);
        return canGenerate && checkFluid && !level.m_6425_(origin.below()).isEmpty() ? false : canGenerate;
    }
}