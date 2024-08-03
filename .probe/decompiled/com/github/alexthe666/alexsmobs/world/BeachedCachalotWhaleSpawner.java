package com.github.alexthe666.alexsmobs.world;

import com.github.alexthe666.alexsmobs.config.AMConfig;
import com.github.alexthe666.alexsmobs.config.BiomeConfig;
import com.github.alexthe666.alexsmobs.entity.AMEntityRegistry;
import com.github.alexthe666.alexsmobs.entity.EntityCachalotWhale;
import java.util.Random;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;

public class BeachedCachalotWhaleSpawner {

    private final Random random = new Random();

    private final ServerLevel world;

    private int timer;

    private int delay;

    private int chance;

    public BeachedCachalotWhaleSpawner(ServerLevel p_i50177_1_) {
        this.world = p_i50177_1_;
        this.timer = 1200;
        AMWorldData worldinfo = AMWorldData.get(p_i50177_1_);
        this.delay = worldinfo.getBeachedCachalotSpawnDelay();
        this.chance = worldinfo.getBeachedCachalotSpawnChance();
        if (this.delay == 0 && this.chance == 0) {
            this.delay = AMConfig.beachedCachalotWhaleSpawnDelay;
            worldinfo.setBeachedCachalotSpawnDelay(this.delay);
            this.chance = 25;
            worldinfo.setBeachedCachalotSpawnChance(this.chance);
        }
    }

    public void tick() {
        if (AMConfig.beachedCachalotWhales && --this.timer <= 0 && this.world.m_46470_()) {
            this.timer = 1200;
            AMWorldData worldinfo = AMWorldData.get(this.world);
            this.delay -= 1200;
            if (this.delay < 0) {
                this.delay = 0;
            }
            worldinfo.setBeachedCachalotSpawnDelay(this.delay);
            if (this.delay <= 0) {
                this.delay = AMConfig.beachedCachalotWhaleSpawnDelay;
                if (this.world.m_46469_().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    int i = this.chance;
                    this.chance = Mth.clamp(this.chance + AMConfig.beachedCachalotWhaleSpawnChance, 5, 100);
                    worldinfo.setBeachedCachalotSpawnChance(this.chance);
                    if (this.random.nextInt(100) <= i && this.attemptSpawnWhale()) {
                        this.chance = AMConfig.beachedCachalotWhaleSpawnChance;
                    }
                }
            }
        }
    }

    private boolean attemptSpawnWhale() {
        Player playerentity = this.world.getRandomPlayer();
        if (playerentity == null) {
            return true;
        } else if (this.random.nextInt(5) != 0) {
            return false;
        } else {
            BlockPos blockpos = playerentity.m_20183_();
            BlockPos blockpos2 = this.func_221244_a(blockpos, 84);
            if (blockpos2 != null && this.func_226559_a_(blockpos2) && blockpos2.m_123331_(blockpos) > 225.0) {
                BlockPos upPos = new BlockPos(blockpos2.m_123341_(), blockpos2.m_123342_() + 2, blockpos2.m_123343_());
                EntityCachalotWhale whale = AMEntityRegistry.CACHALOT_WHALE.get().create(this.world);
                whale.m_7678_((double) upPos.m_123341_() + 0.5, (double) upPos.m_123342_() + 0.5, (double) upPos.m_123343_() + 0.5, this.random.nextFloat() * 360.0F - 180.0F, 0.0F);
                whale.finalizeSpawn(this.world, this.world.m_6436_(upPos), MobSpawnType.SPAWNER, null, null);
                whale.setBeached(true);
                AMWorldData worldinfo = AMWorldData.get(this.world);
                worldinfo.setBeachedCachalotID(whale.m_20148_());
                whale.m_21446_(upPos, 16);
                whale.setDespawnBeach(true);
                this.world.addFreshEntity(whale);
                return true;
            } else {
                return false;
            }
        }
    }

    @Nullable
    private BlockPos func_221244_a(BlockPos p_221244_1_, int p_221244_2_) {
        BlockPos blockpos = null;
        for (int i = 0; i < 10; i++) {
            int j = p_221244_1_.m_123341_() + this.random.nextInt(p_221244_2_ * 2) - p_221244_2_;
            int k = p_221244_1_.m_123343_() + this.random.nextInt(p_221244_2_ * 2) - p_221244_2_;
            int l = this.world.m_6924_(Heightmap.Types.WORLD_SURFACE, j, k);
            BlockPos blockpos1 = new BlockPos(j, l, k);
            if (AMWorldRegistry.testBiome(BiomeConfig.cachalot_whale_beached_spawns, this.world.m_204166_(blockpos1)) && NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, this.world, blockpos1, EntityType.WANDERING_TRADER)) {
                blockpos = blockpos1;
                break;
            }
        }
        return blockpos;
    }

    private boolean func_226559_a_(BlockPos p_226559_1_) {
        for (BlockPos blockpos : BlockPos.betweenClosed(p_226559_1_, p_226559_1_.offset(1, 2, 1))) {
            if (!this.world.m_8055_(blockpos).m_60816_(this.world, blockpos).isEmpty() || !this.world.m_6425_(blockpos).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}