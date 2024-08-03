package com.github.alexmodguy.alexscaves.server.entity.ai;

import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class AnimalRandomlySwimGoal extends Goal {

    private BlockPos goal = null;

    private Mob mob;

    private int range;

    private int chance;

    private int belowSeaLevel;

    private double speed;

    public AnimalRandomlySwimGoal(Mob mob, int chance, int range, int belowSeaLevel, double speed) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE));
        this.mob = mob;
        this.chance = chance;
        this.range = range;
        this.belowSeaLevel = belowSeaLevel;
        this.speed = speed;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.mob.getTarget();
        return this.mob.m_20072_() && (target == null || !target.isAlive()) && (this.chance == 0 || this.mob.m_217043_().nextInt(this.chance) == 0);
    }

    @Override
    public boolean canContinueToUse() {
        return this.goal != null && this.mob.m_20238_(Vec3.atCenterOf(this.goal)) > 30.0 && !this.mob.getNavigation().isDone();
    }

    @Override
    public void start() {
        this.goal = this.findSwimToPos();
        this.mob.getNavigation().moveTo((double) this.goal.m_123341_(), (double) this.goal.m_123342_(), (double) this.goal.m_123343_(), this.speed);
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
        around = around.atY(Math.min(surfaceY - this.belowSeaLevel, around.m_123342_()));
        for (int i = 0; i < 15; i++) {
            BlockPos blockPos = around.offset(this.mob.m_217043_().nextInt(this.range) - this.range / 2, this.mob.m_217043_().nextInt(this.range) - this.range / 2, this.mob.m_217043_().nextInt(this.range) - this.range / 2);
            if (this.mob.m_9236_().getFluidState(blockPos).is(FluidTags.WATER) && !this.isTargetBlocked(Vec3.atCenterOf(blockPos)) && blockPos.m_123342_() > this.mob.m_9236_().m_141937_() + 1) {
                return blockPos;
            }
        }
        return around;
    }
}