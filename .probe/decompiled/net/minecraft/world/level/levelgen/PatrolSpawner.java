package net.minecraft.world.level.levelgen;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.PatrollingMonster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.NaturalSpawner;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.state.BlockState;

public class PatrolSpawner implements CustomSpawner {

    private int nextTick;

    @Override
    public int tick(ServerLevel serverLevel0, boolean boolean1, boolean boolean2) {
        if (!boolean1) {
            return 0;
        } else if (!serverLevel0.m_46469_().getBoolean(GameRules.RULE_DO_PATROL_SPAWNING)) {
            return 0;
        } else {
            RandomSource $$3 = serverLevel0.f_46441_;
            this.nextTick--;
            if (this.nextTick > 0) {
                return 0;
            } else {
                this.nextTick = this.nextTick + 12000 + $$3.nextInt(1200);
                long $$4 = serverLevel0.m_46468_() / 24000L;
                if ($$4 < 5L || !serverLevel0.m_46461_()) {
                    return 0;
                } else if ($$3.nextInt(5) != 0) {
                    return 0;
                } else {
                    int $$5 = serverLevel0.players().size();
                    if ($$5 < 1) {
                        return 0;
                    } else {
                        Player $$6 = (Player) serverLevel0.players().get($$3.nextInt($$5));
                        if ($$6.isSpectator()) {
                            return 0;
                        } else if (serverLevel0.isCloseToVillage($$6.m_20183_(), 2)) {
                            return 0;
                        } else {
                            int $$7 = (24 + $$3.nextInt(24)) * ($$3.nextBoolean() ? -1 : 1);
                            int $$8 = (24 + $$3.nextInt(24)) * ($$3.nextBoolean() ? -1 : 1);
                            BlockPos.MutableBlockPos $$9 = $$6.m_20183_().mutable().move($$7, 0, $$8);
                            int $$10 = 10;
                            if (!serverLevel0.m_151572_($$9.m_123341_() - 10, $$9.m_123343_() - 10, $$9.m_123341_() + 10, $$9.m_123343_() + 10)) {
                                return 0;
                            } else {
                                Holder<Biome> $$11 = serverLevel0.m_204166_($$9);
                                if ($$11.is(BiomeTags.WITHOUT_PATROL_SPAWNS)) {
                                    return 0;
                                } else {
                                    int $$12 = 0;
                                    int $$13 = (int) Math.ceil((double) serverLevel0.m_6436_($$9).getEffectiveDifficulty()) + 1;
                                    for (int $$14 = 0; $$14 < $$13; $$14++) {
                                        $$12++;
                                        $$9.setY(serverLevel0.m_5452_(Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, $$9).m_123342_());
                                        if ($$14 == 0) {
                                            if (!this.spawnPatrolMember(serverLevel0, $$9, $$3, true)) {
                                                break;
                                            }
                                        } else {
                                            this.spawnPatrolMember(serverLevel0, $$9, $$3, false);
                                        }
                                        $$9.setX($$9.m_123341_() + $$3.nextInt(5) - $$3.nextInt(5));
                                        $$9.setZ($$9.m_123343_() + $$3.nextInt(5) - $$3.nextInt(5));
                                    }
                                    return $$12;
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean spawnPatrolMember(ServerLevel serverLevel0, BlockPos blockPos1, RandomSource randomSource2, boolean boolean3) {
        BlockState $$4 = serverLevel0.m_8055_(blockPos1);
        if (!NaturalSpawner.isValidEmptySpawnBlock(serverLevel0, blockPos1, $$4, $$4.m_60819_(), EntityType.PILLAGER)) {
            return false;
        } else if (!PatrollingMonster.checkPatrollingMonsterSpawnRules(EntityType.PILLAGER, serverLevel0, MobSpawnType.PATROL, blockPos1, randomSource2)) {
            return false;
        } else {
            PatrollingMonster $$5 = EntityType.PILLAGER.create(serverLevel0);
            if ($$5 != null) {
                if (boolean3) {
                    $$5.setPatrolLeader(true);
                    $$5.findPatrolTarget();
                }
                $$5.m_6034_((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_());
                $$5.finalizeSpawn(serverLevel0, serverLevel0.m_6436_(blockPos1), MobSpawnType.PATROL, null, null);
                serverLevel0.m_47205_($$5);
                return true;
            } else {
                return false;
            }
        }
    }
}