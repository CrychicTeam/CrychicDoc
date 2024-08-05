package com.github.alexmodguy.alexscaves.mixin;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.server.entity.ACEntityRegistry;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.WeightedRandomList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ NaturalSpawner.class })
public class NaturalSpawnerMixin {

    @Inject(method = { "Lnet/minecraft/world/level/NaturalSpawner;spawnMobsForChunkGeneration(Lnet/minecraft/world/level/ServerLevelAccessor;Lnet/minecraft/core/Holder;Lnet/minecraft/world/level/ChunkPos;Lnet/minecraft/util/RandomSource;)V" }, remap = true, at = { @At("TAIL") })
    private static void ac_spawnMobsForChunkGeneration(ServerLevelAccessor level, Holder<Biome> surfaceBiome, ChunkPos chunkPos, RandomSource randomSource, CallbackInfo ci) {
        Holder<Biome> caveBiome = getCaveCreaturesBiome(level, chunkPos, randomSource);
        if (caveBiome != null) {
            MobSpawnSettings mobspawnsettings = caveBiome.value().getMobSettings();
            WeightedRandomList<MobSpawnSettings.SpawnerData> weightedrandomlist = mobspawnsettings.getMobs(ACEntityRegistry.CAVE_CREATURE);
            if (!weightedrandomlist.isEmpty()) {
                int i = chunkPos.getMinBlockX();
                int j = chunkPos.getMinBlockZ();
                while ((double) randomSource.nextFloat() < AlexsCaves.COMMON_CONFIG.caveCreatureSpawnCountModifier.get() * (double) mobspawnsettings.getCreatureProbability()) {
                    Optional<MobSpawnSettings.SpawnerData> optional = weightedrandomlist.getRandom(randomSource);
                    if (optional.isPresent()) {
                        MobSpawnSettings.SpawnerData mobspawnsettings$spawnerdata = (MobSpawnSettings.SpawnerData) optional.get();
                        int mobsToSpawn = 1 + mobspawnsettings$spawnerdata.maxCount - mobspawnsettings$spawnerdata.minCount;
                        int k = mobspawnsettings$spawnerdata.minCount + randomSource.nextInt(Math.max(mobsToSpawn, 1));
                        SpawnGroupData spawngroupdata = null;
                        int l = i + randomSource.nextInt(16);
                        int i1 = j + randomSource.nextInt(16);
                        int j1 = l;
                        int k1 = i1;
                        for (int l1 = 0; l1 < k; l1++) {
                            boolean flag = false;
                            for (int i2 = 0; !flag && i2 < 4; i2++) {
                                BlockPos blockpos = getCaveCreatureSpawnPos(level, randomSource, caveBiome, mobspawnsettings$spawnerdata.type, l, i1);
                                if (mobspawnsettings$spawnerdata.type.canSummon() && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.getPlacementType(mobspawnsettings$spawnerdata.type), level, blockpos, mobspawnsettings$spawnerdata.type)) {
                                    float f = mobspawnsettings$spawnerdata.type.getWidth();
                                    double d0 = Mth.clamp((double) l, (double) i + (double) f, (double) i + 16.0 - (double) f);
                                    double d1 = Mth.clamp((double) i1, (double) j + (double) f, (double) j + 16.0 - (double) f);
                                    if (!level.m_45772_(mobspawnsettings$spawnerdata.type.getAABB(d0, (double) blockpos.m_123342_(), d1)) || !SpawnPlacements.checkSpawnRules(mobspawnsettings$spawnerdata.type, level, MobSpawnType.CHUNK_GENERATION, BlockPos.containing(d0, (double) blockpos.m_123342_(), d1), level.m_213780_())) {
                                        continue;
                                    }
                                    Entity entity;
                                    try {
                                        entity = mobspawnsettings$spawnerdata.type.create(level.getLevel());
                                    } catch (Exception var30) {
                                        AlexsCaves.LOGGER.warn("Failed to create mob", var30);
                                        continue;
                                    }
                                    entity.moveTo(d0, (double) blockpos.m_123342_(), d1, randomSource.nextFloat() * 360.0F, 0.0F);
                                    if (entity instanceof Mob mob && mob.checkSpawnRules(level, MobSpawnType.CHUNK_GENERATION) && mob.checkSpawnObstruction(level)) {
                                        spawngroupdata = mob.finalizeSpawn(level, level.m_6436_(mob.m_20183_()), MobSpawnType.CHUNK_GENERATION, spawngroupdata, (CompoundTag) null);
                                        level.addFreshEntityWithPassengers(mob);
                                        flag = true;
                                    }
                                }
                                l += randomSource.nextInt(5) - randomSource.nextInt(5);
                                for (i1 += randomSource.nextInt(5) - randomSource.nextInt(5); l < i || l >= i + 16 || i1 < j || i1 >= j + 16; i1 = k1 + randomSource.nextInt(5) - randomSource.nextInt(5)) {
                                    l = j1 + randomSource.nextInt(5) - randomSource.nextInt(5);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private static Holder<Biome> getCaveCreaturesBiome(ServerLevelAccessor level, ChunkPos chunkPos, RandomSource random) {
        List<Holder<Biome>> cavesWithCreatures = new ArrayList();
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(chunkPos.getMiddleBlockX(), -1, chunkPos.getMiddleBlockZ());
        for (int i = 0; i < 5; i++) {
            int heightRange = level.m_6924_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, chunkPos.getMiddleBlockX(), chunkPos.getMiddleBlockZ()) - level.m_141937_();
            int height = level.m_141937_() + Math.round((float) heightRange * random.nextFloat());
            mutableBlockPos.setY(height);
            Holder<Biome> holder = level.m_204166_(mutableBlockPos);
            if (!((Biome) holder.get()).getMobSettings().getMobs(ACEntityRegistry.CAVE_CREATURE).isEmpty() && !cavesWithCreatures.contains(holder)) {
                cavesWithCreatures.add(holder);
            }
        }
        return cavesWithCreatures.isEmpty() ? null : Util.getRandom(cavesWithCreatures, random);
    }

    private static BlockPos getCaveCreatureSpawnPos(ServerLevelAccessor level, RandomSource random, Holder<Biome> checkAgainst, EntityType<?> type, int x, int z) {
        int safeWorldHeight = Math.max(level.m_6924_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, x, z), 0);
        int height = level.m_141937_() + random.nextInt(2 + safeWorldHeight);
        BlockPos.MutableBlockPos mutableBlockPos = new BlockPos.MutableBlockPos(x, height, z);
        while ((!level.m_8055_(mutableBlockPos).m_60795_() || level.m_204166_(mutableBlockPos) != checkAgainst) && mutableBlockPos.m_123342_() > level.m_141937_()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        while (level.m_8055_(mutableBlockPos).m_60795_() && mutableBlockPos.m_123342_() > level.m_141937_()) {
            mutableBlockPos.move(Direction.DOWN);
        }
        return mutableBlockPos.m_7494_().immutable();
    }
}