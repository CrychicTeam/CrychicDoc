package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneBaseEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.DeepOneMageEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class DeepOneWanderGoal extends Goal {

    private BlockPos goal = null;

    private DeepOneBaseEntity mob;

    private int chance;

    private double speed;

    private boolean groundTarget = false;

    public DeepOneWanderGoal(DeepOneBaseEntity mob, int chance, double speed) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.mob = mob;
        this.chance = chance;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.m_5448_();
        return this.mob.m_20072_() && (target == null || !target.isAlive()) && (this.chance == 0 || this.mob.m_217043_().nextInt(this.chance) == 0) && !this.mob.isTradingLocked();
    }

    @Override
    public boolean canContinueToUse() {
        return this.goal != null && !this.mob.m_21573_().isDone() && this.mob.m_217043_().nextInt(200) != 0 && !this.mob.isTradingLocked();
    }

    @Override
    public void start() {
        this.groundTarget = this.mob.m_20096_() ? this.mob.m_217043_().nextFloat() < 0.7F : this.mob.m_217043_().nextFloat() < 0.2F;
        this.goal = this.findSwimToPos();
    }

    @Override
    public void tick() {
        this.mob.m_21573_().moveTo((double) this.goal.m_123341_(), (double) this.goal.m_123342_(), (double) this.goal.m_123343_(), this.speed);
        if (this.groundTarget) {
            if (this.mob.m_20096_()) {
                this.mob.setDeepOneSwimming(false);
            } else if (this.mob.m_20238_(Vec3.atCenterOf(this.goal)) < 4.0) {
                this.mob.m_20256_(this.mob.m_20184_().scale(0.8).add(0.0, -0.1F, 0.0));
            }
        }
        if (!this.groundTarget) {
            this.mob.setDeepOneSwimming(true);
        }
    }

    public boolean isTargetBlocked(Vec3 target) {
        Vec3 Vector3d = new Vec3(this.mob.m_20185_(), this.mob.m_20188_(), this.mob.m_20189_());
        return this.mob.m_9236_().m_45547_(new ClipContext(Vector3d, target, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, this.mob)).getType() != HitResult.Type.MISS;
    }

    public BlockPos findSwimToPos() {
        BlockPos around = this.mob.m_20183_();
        BlockPos.MutableBlockPos move = new BlockPos.MutableBlockPos();
        move.set(this.mob.m_20185_(), this.mob.m_20186_(), this.mob.m_20189_());
        while (move.m_123342_() < this.mob.m_9236_().m_151558_() && this.mob.m_9236_().getFluidState(move).is(FluidTags.WATER)) {
            move.move(0, 5, 0);
        }
        int surfaceY = move.m_123342_();
        around = around.atY(Math.max(surfaceY - 40, around.m_123342_()));
        int range = 18;
        for (int i = 0; i < 15; i++) {
            BlockPos blockPos = around.offset(this.mob.m_217043_().nextInt(range) - range / 2, this.mob.m_217043_().nextInt(range) - range / 2, this.mob.m_217043_().nextInt(range) - range / 2);
            if (this.mob.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && !this.isTargetBlocked(Vec3.atCenterOf(blockPos)) && blockPos.m_123342_() > this.mob.m_9236_().m_141937_() + 1) {
                if (this.groundTarget) {
                    while (this.groundTarget && this.mob.m_9236_().getFluidState(blockPos.below()).is(FluidTags.WATER) && blockPos.m_123342_() > this.mob.m_9236_().m_141937_()) {
                        blockPos = blockPos.below();
                    }
                    if (this.mob instanceof DeepOneMageEntity) {
                        blockPos = blockPos.above(1 + this.mob.m_217043_().nextInt(2));
                    }
                }
                return blockPos;
            }
        }
        return around;
    }
}