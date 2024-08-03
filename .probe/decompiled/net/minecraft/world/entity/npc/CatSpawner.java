package net.minecraft.world.entity.npc;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.StructureTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.ai.village.poi.PoiManager;
import net.minecraft.world.entity.ai.village.poi.PoiTypes;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.phys.AABB;

public class CatSpawner implements CustomSpawner {

    private static final int TICK_DELAY = 1200;

    private int nextTick;

    @Override
    public int tick(ServerLevel serverLevel0, boolean boolean1, boolean boolean2) {
        if (boolean2 && serverLevel0.m_46469_().getBoolean(GameRules.RULE_DOMOBSPAWNING)) {
            this.nextTick--;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick = 1200;
                Player $$3 = serverLevel0.getRandomPlayer();
                if ($$3 == null) {
                    return 0;
                } else {
                    RandomSource $$4 = serverLevel0.f_46441_;
                    int $$5 = (8 + $$4.nextInt(24)) * ($$4.nextBoolean() ? -1 : 1);
                    int $$6 = (8 + $$4.nextInt(24)) * ($$4.nextBoolean() ? -1 : 1);
                    BlockPos $$7 = $$3.m_20183_().offset($$5, 0, $$6);
                    int $$8 = 10;
                    if (!serverLevel0.m_151572_($$7.m_123341_() - 10, $$7.m_123343_() - 10, $$7.m_123341_() + 10, $$7.m_123343_() + 10)) {
                        return 0;
                    } else {
                        if (NaturalSpawner.isSpawnPositionOk(SpawnPlacements.Type.ON_GROUND, serverLevel0, $$7, EntityType.CAT)) {
                            if (serverLevel0.isCloseToVillage($$7, 2)) {
                                return this.spawnInVillage(serverLevel0, $$7);
                            }
                            if (serverLevel0.structureManager().getStructureWithPieceAt($$7, StructureTags.CATS_SPAWN_IN).isValid()) {
                                return this.spawnInHut(serverLevel0, $$7);
                            }
                        }
                        return 0;
                    }
                }
            }
        } else {
            return 0;
        }
    }

    private int spawnInVillage(ServerLevel serverLevel0, BlockPos blockPos1) {
        int $$2 = 48;
        if (serverLevel0.getPoiManager().getCountInRange(p_219610_ -> p_219610_.is(PoiTypes.HOME), blockPos1, 48, PoiManager.Occupancy.IS_OCCUPIED) > 4L) {
            List<Cat> $$3 = serverLevel0.m_45976_(Cat.class, new AABB(blockPos1).inflate(48.0, 8.0, 48.0));
            if ($$3.size() < 5) {
                return this.spawnCat(blockPos1, serverLevel0);
            }
        }
        return 0;
    }

    private int spawnInHut(ServerLevel serverLevel0, BlockPos blockPos1) {
        int $$2 = 16;
        List<Cat> $$3 = serverLevel0.m_45976_(Cat.class, new AABB(blockPos1).inflate(16.0, 8.0, 16.0));
        return $$3.size() < 1 ? this.spawnCat(blockPos1, serverLevel0) : 0;
    }

    private int spawnCat(BlockPos blockPos0, ServerLevel serverLevel1) {
        Cat $$2 = EntityType.CAT.create(serverLevel1);
        if ($$2 == null) {
            return 0;
        } else {
            $$2.finalizeSpawn(serverLevel1, serverLevel1.m_6436_(blockPos0), MobSpawnType.NATURAL, null, null);
            $$2.m_20035_(blockPos0, 0.0F, 0.0F);
            serverLevel1.m_47205_($$2);
            return 1;
        }
    }
}