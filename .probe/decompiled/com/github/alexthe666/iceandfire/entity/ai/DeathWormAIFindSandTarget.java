package com.github.alexthe666.iceandfire.entity.ai;

import com.github.alexthe666.iceandfire.entity.EntityDeathWorm;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;

public class DeathWormAIFindSandTarget extends Goal {

    private final EntityDeathWorm mob;

    private int range;

    public DeathWormAIFindSandTarget(EntityDeathWorm mob, int range) {
        this.mob = mob;
        this.range = range;
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_5448_() != null) {
            return false;
        } else if (this.mob.isInSand() && !this.mob.m_20159_() && !this.mob.m_20160_()) {
            if (this.mob.m_217043_().nextFloat() < 0.5F) {
                Path path = this.mob.m_21573_().getPath();
                if (path != null) {
                    this.mob.m_21573_().stop();
                }
                if (this.mob.m_21573_().isDone()) {
                    BlockPos vec3 = this.findSandTarget();
                    if (vec3 != null) {
                        this.mob.m_21566_().setWantedPosition((double) vec3.m_123341_(), (double) vec3.m_123342_(), (double) vec3.m_123343_(), 1.0);
                        return true;
                    }
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }

    public BlockPos findSandTarget() {
        if (this.mob.m_5448_() != null && this.mob.m_5448_().isAlive()) {
            BlockPos blockpos1 = this.mob.m_5448_().m_20183_();
            return new BlockPos(blockpos1.m_123341_(), blockpos1.m_123342_() - 1, blockpos1.m_123343_());
        } else {
            List<BlockPos> sand = new ArrayList();
            if (this.mob.m_21824_() && this.mob.getWormHome() != null) {
                this.range = 25;
                for (int x = this.mob.getWormHome().m_123341_() - this.range; x < this.mob.getWormHome().m_123341_() + this.range; x++) {
                    for (int y = this.mob.getWormHome().m_123342_() - this.range; y < this.mob.getWormHome().m_123342_() + this.range; y++) {
                        for (int z = this.mob.getWormHome().m_123343_() - this.range; z < this.mob.getWormHome().m_123343_() + this.range; z++) {
                            if (this.mob.m_9236_().getBlockState(new BlockPos(x, y, z)).m_204336_(BlockTags.SAND) && this.isDirectPathBetweenPoints(this.mob, this.mob.m_20182_(), new Vec3((double) x, (double) y, (double) z))) {
                                sand.add(new BlockPos(x, y, z));
                            }
                        }
                    }
                }
            } else {
                for (int x = (int) this.mob.m_20185_() - this.range; x < (int) this.mob.m_20185_() + this.range; x++) {
                    for (int y = (int) this.mob.m_20186_() - this.range; y < (int) this.mob.m_20186_() + this.range; y++) {
                        for (int zx = (int) this.mob.m_20189_() - this.range; zx < (int) this.mob.m_20189_() + this.range; zx++) {
                            if (this.mob.m_9236_().getBlockState(new BlockPos(x, y, zx)).m_204336_(BlockTags.SAND) && this.isDirectPathBetweenPoints(this.mob, this.mob.m_20182_(), new Vec3((double) x, (double) y, (double) zx))) {
                                sand.add(new BlockPos(x, y, zx));
                            }
                        }
                    }
                }
            }
            return !sand.isEmpty() ? (BlockPos) sand.get(this.mob.m_217043_().nextInt(sand.size())) : null;
        }
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        return true;
    }
}