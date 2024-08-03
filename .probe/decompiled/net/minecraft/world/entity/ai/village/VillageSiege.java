package net.minecraft.world.entity.ai.village;

import com.mojang.logging.LogUtils;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BiomeTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.CustomSpawner;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.Vec3;
import org.slf4j.Logger;

public class VillageSiege implements CustomSpawner {

    private static final Logger LOGGER = LogUtils.getLogger();

    private boolean hasSetupSiege;

    private VillageSiege.State siegeState = VillageSiege.State.SIEGE_DONE;

    private int zombiesToSpawn;

    private int nextSpawnTime;

    private int spawnX;

    private int spawnY;

    private int spawnZ;

    @Override
    public int tick(ServerLevel serverLevel0, boolean boolean1, boolean boolean2) {
        if (!serverLevel0.m_46461_() && boolean1) {
            float $$3 = serverLevel0.m_46942_(0.0F);
            if ((double) $$3 == 0.5) {
                this.siegeState = serverLevel0.f_46441_.nextInt(10) == 0 ? VillageSiege.State.SIEGE_TONIGHT : VillageSiege.State.SIEGE_DONE;
            }
            if (this.siegeState == VillageSiege.State.SIEGE_DONE) {
                return 0;
            } else {
                if (!this.hasSetupSiege) {
                    if (!this.tryToSetupSiege(serverLevel0)) {
                        return 0;
                    }
                    this.hasSetupSiege = true;
                }
                if (this.nextSpawnTime > 0) {
                    this.nextSpawnTime--;
                    return 0;
                } else {
                    this.nextSpawnTime = 2;
                    if (this.zombiesToSpawn > 0) {
                        this.trySpawn(serverLevel0);
                        this.zombiesToSpawn--;
                    } else {
                        this.siegeState = VillageSiege.State.SIEGE_DONE;
                    }
                    return 1;
                }
            }
        } else {
            this.siegeState = VillageSiege.State.SIEGE_DONE;
            this.hasSetupSiege = false;
            return 0;
        }
    }

    private boolean tryToSetupSiege(ServerLevel serverLevel0) {
        for (Player $$1 : serverLevel0.players()) {
            if (!$$1.isSpectator()) {
                BlockPos $$2 = $$1.m_20183_();
                if (serverLevel0.isVillage($$2) && !serverLevel0.m_204166_($$2).is(BiomeTags.WITHOUT_ZOMBIE_SIEGES)) {
                    for (int $$3 = 0; $$3 < 10; $$3++) {
                        float $$4 = serverLevel0.f_46441_.nextFloat() * (float) (Math.PI * 2);
                        this.spawnX = $$2.m_123341_() + Mth.floor(Mth.cos($$4) * 32.0F);
                        this.spawnY = $$2.m_123342_();
                        this.spawnZ = $$2.m_123343_() + Mth.floor(Mth.sin($$4) * 32.0F);
                        if (this.findRandomSpawnPos(serverLevel0, new BlockPos(this.spawnX, this.spawnY, this.spawnZ)) != null) {
                            this.nextSpawnTime = 0;
                            this.zombiesToSpawn = 20;
                            break;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    private void trySpawn(ServerLevel serverLevel0) {
        Vec3 $$1 = this.findRandomSpawnPos(serverLevel0, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
        if ($$1 != null) {
            Zombie $$2;
            try {
                $$2 = new Zombie(serverLevel0);
                $$2.finalizeSpawn(serverLevel0, serverLevel0.m_6436_($$2.m_20183_()), MobSpawnType.EVENT, null, null);
            } catch (Exception var5) {
                LOGGER.warn("Failed to create zombie for village siege at {}", $$1, var5);
                return;
            }
            $$2.m_7678_($$1.x, $$1.y, $$1.z, serverLevel0.f_46441_.nextFloat() * 360.0F, 0.0F);
            serverLevel0.m_47205_($$2);
        }
    }

    @Nullable
    private Vec3 findRandomSpawnPos(ServerLevel serverLevel0, BlockPos blockPos1) {
        for (int $$2 = 0; $$2 < 10; $$2++) {
            int $$3 = blockPos1.m_123341_() + serverLevel0.f_46441_.nextInt(16) - 8;
            int $$4 = blockPos1.m_123343_() + serverLevel0.f_46441_.nextInt(16) - 8;
            int $$5 = serverLevel0.m_6924_(Heightmap.Types.WORLD_SURFACE, $$3, $$4);
            BlockPos $$6 = new BlockPos($$3, $$5, $$4);
            if (serverLevel0.isVillage($$6) && Monster.checkMonsterSpawnRules(EntityType.ZOMBIE, serverLevel0, MobSpawnType.EVENT, $$6, serverLevel0.f_46441_)) {
                return Vec3.atBottomCenterOf($$6);
            }
        }
        return null;
    }

    static enum State {

        SIEGE_CAN_ACTIVATE, SIEGE_TONIGHT, SIEGE_DONE
    }
}