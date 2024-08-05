package net.minecraft.world.entity.npc;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.horse.TraderLlama;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.storage.ServerLevelData;

public class WanderingTraderSpawner implements CustomSpawner {

    private static final int DEFAULT_TICK_DELAY = 1200;

    public static final int DEFAULT_SPAWN_DELAY = 24000;

    private static final int MIN_SPAWN_CHANCE = 25;

    private static final int MAX_SPAWN_CHANCE = 75;

    private static final int SPAWN_CHANCE_INCREASE = 25;

    private static final int SPAWN_ONE_IN_X_CHANCE = 10;

    private static final int NUMBER_OF_SPAWN_ATTEMPTS = 10;

    private final RandomSource random = RandomSource.create();

    private final ServerLevelData serverLevelData;

    private int tickDelay;

    private int spawnDelay;

    private int spawnChance;

    public WanderingTraderSpawner(ServerLevelData serverLevelData0) {
        this.serverLevelData = serverLevelData0;
        this.tickDelay = 1200;
        this.spawnDelay = serverLevelData0.getWanderingTraderSpawnDelay();
        this.spawnChance = serverLevelData0.getWanderingTraderSpawnChance();
        if (this.spawnDelay == 0 && this.spawnChance == 0) {
            this.spawnDelay = 24000;
            serverLevelData0.setWanderingTraderSpawnDelay(this.spawnDelay);
            this.spawnChance = 25;
            serverLevelData0.setWanderingTraderSpawnChance(this.spawnChance);
        }
    }

    @Override
    public int tick(ServerLevel serverLevel0, boolean boolean1, boolean boolean2) {
        if (!serverLevel0.m_46469_().getBoolean(GameRules.RULE_DO_TRADER_SPAWNING)) {
            return 0;
        } else if (--this.tickDelay > 0) {
            return 0;
        } else {
            this.tickDelay = 1200;
            this.spawnDelay -= 1200;
            this.serverLevelData.setWanderingTraderSpawnDelay(this.spawnDelay);
            if (this.spawnDelay > 0) {
                return 0;
            } else {
                this.spawnDelay = 24000;
                if (!serverLevel0.m_46469_().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
                    return 0;
                } else {
                    int $$3 = this.spawnChance;
                    this.spawnChance = Mth.clamp(this.spawnChance + 25, 25, 75);
                    this.serverLevelData.setWanderingTraderSpawnChance(this.spawnChance);
                    if (this.random.nextInt(100) > $$3) {
                        return 0;
                    } else if (this.spawn(serverLevel0)) {
                        this.spawnChance = 25;
                        return 1;
                    } else {
                        return 0;
                    }
                }
            }
        }
    }

    private boolean spawn(ServerLevel serverLevel0) {
        Player $$1 = serverLevel0.getRandomPlayer();
        if ($$1 == null) {
            return true;
        } else if (this.random.nextInt(10) != 0) {
            return false;
        } else {
            BlockPos $$2 = $$1.m_20183_();
            int $$3 = 48;
            PoiManager $$4 = serverLevel0.getPoiManager();
            Optional<BlockPos> $$5 = $$4.find(p_219713_ -> p_219713_.is(PoiTypes.MEETING), p_219711_ -> true, $$2, 48, PoiManager.Occupancy.ANY);
            BlockPos $$6 = (BlockPos) $$5.orElse($$2);
            BlockPos $$7 = this.findSpawnPositionNear(serverLevel0, $$6, 48);
            if ($$7 != null && this.hasEnoughSpace(serverLevel0, $$7)) {
                if (serverLevel0.m_204166_($$7).is(BiomeTags.WITHOUT_WANDERING_TRADER_SPAWNS)) {
                    return false;
                }
                WanderingTrader $$8 = EntityType.WANDERING_TRADER.spawn(serverLevel0, $$7, MobSpawnType.EVENT);
                if ($$8 != null) {
                    for (int $$9 = 0; $$9 < 2; $$9++) {
                        this.tryToSpawnLlamaFor(serverLevel0, $$8, 4);
                    }
                    this.serverLevelData.setWanderingTraderId($$8.m_20148_());
                    $$8.setDespawnDelay(48000);
                    $$8.setWanderTarget($$6);
                    $$8.m_21446_($$6, 16);
                    return true;
                }
            }
            return false;
        }
    }

    private void tryToSpawnLlamaFor(ServerLevel serverLevel0, WanderingTrader wanderingTrader1, int int2) {
        BlockPos $$3 = this.findSpawnPositionNear(serverLevel0, wanderingTrader1.m_20183_(), int2);
        if ($$3 != null) {
            TraderLlama $$4 = EntityType.TRADER_LLAMA.spawn(serverLevel0, $$3, MobSpawnType.EVENT);
            if ($$4 != null) {
                $$4.m_21463_(wanderingTrader1, true);
            }
        }
    }

    @Nullable
    private BlockPos findSpawnPositionNear(LevelReader levelReader0, BlockPos blockPos1, int int2) {
        BlockPos $$3 = null;
        for (int $$4 = 0; $$4 < 10; $$4++) {
            int $$5 = blockPos1.m_123341_() + this.random.nextInt(int2 * 2) - int2;
            int $$6 = blockPos1.m_123343_() + this.random.nextInt(int2 * 2) - int2;
            int $$7 = levelReader0.getHeight(Heightmap.Types.WORLD_SURFACE, $$5, $$6);
            BlockPos $$8 = new BlockPos($$5, $$7, $$6);
            if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, levelReader0, $$8, EntityType.WANDERING_TRADER)) {
                $$3 = $$8;
                break;
            }
        }
        return $$3;
    }

    private boolean hasEnoughSpace(BlockGetter blockGetter0, BlockPos blockPos1) {
        for (BlockPos $$2 : BlockPos.betweenClosed(blockPos1, blockPos1.offset(1, 2, 1))) {
            if (!blockGetter0.getBlockState($$2).m_60812_(blockGetter0, $$2).isEmpty()) {
                return false;
            }
        }
        return true;
    }
}