package com.github.alexthe666.iceandfire.entity.ai;

import java.util.Comparator;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AquaticAIFindWaterTarget extends Goal {

    protected AquaticAIFindWaterTarget.Sorter fleePosSorter;

    private final Mob mob;

    public AquaticAIFindWaterTarget(Mob mob, int range, boolean avoidAttacker) {
        this.mob = mob;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.fleePosSorter = new AquaticAIFindWaterTarget.Sorter(mob);
    }

    @Override
    public boolean canUse() {
        if (this.mob.m_20069_() && !this.mob.m_20159_() && !this.mob.m_20160_()) {
            Path path = this.mob.getNavigation().getPath();
            if (this.mob.m_217043_().nextFloat() < 0.15F || path != null && path.getEndNode() != null && this.mob.m_20275_((double) path.getEndNode().x, (double) path.getEndNode().y, (double) path.getEndNode().z) < 3.0) {
                if (path != null && path.getEndNode() != null || !this.mob.getNavigation().isDone() && !this.isDirectPathBetweenPoints(this.mob, this.mob.m_20182_(), new Vec3((double) path.getEndNode().x, (double) path.getEndNode().y, (double) path.getEndNode().z))) {
                    this.mob.getNavigation().stop();
                }
                if (this.mob.getNavigation().isDone()) {
                    BlockPos vec3 = this.findWaterTarget();
                    if (vec3 != null) {
                        this.mob.getNavigation().moveTo((double) vec3.m_123341_(), (double) vec3.m_123342_(), (double) vec3.m_123343_(), 1.0);
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

    public BlockPos findWaterTarget() {
        BlockPos blockpos = BlockPos.containing((double) this.mob.m_146903_(), this.mob.m_20191_().minY, (double) this.mob.m_146907_());
        if (this.mob.getTarget() != null && this.mob.getTarget().isAlive()) {
            return this.mob.getTarget().m_20183_();
        } else {
            for (int i = 0; i < 10; i++) {
                BlockPos blockpos1 = blockpos.offset(this.mob.m_217043_().nextInt(20) - 10, this.mob.m_217043_().nextInt(6) - 3, this.mob.m_217043_().nextInt(20) - 10);
                if (this.mob.m_9236_().getBlockState(blockpos1).m_60713_(Blocks.WATER)) {
                    return blockpos1;
                }
            }
            return null;
        }
    }

    public boolean isDirectPathBetweenPoints(Entity entity, Vec3 vec1, Vec3 vec2) {
        return this.mob.m_9236_().m_45547_(new ClipContext(vec1, vec2, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, entity)).getType() == HitResult.Type.MISS;
    }

    public class Sorter implements Comparator<BlockPos> {

        private BlockPos pos;

        public Sorter(Entity theEntityIn) {
            this.pos = theEntityIn.blockPosition();
        }

        public int compare(BlockPos p_compare_1_, BlockPos p_compare_2_) {
            this.pos = AquaticAIFindWaterTarget.this.mob.m_20183_();
            double d0 = this.pos.m_123331_(p_compare_1_);
            double d1 = this.pos.m_123331_(p_compare_2_);
            return Double.compare(d1, d0);
        }
    }
}