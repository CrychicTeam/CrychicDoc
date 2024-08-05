package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.ForsakenEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.level.pathfinder.WalkNodeEvaluator;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;

public class ForsakenRandomlyJumpGoal extends Goal {

    private ForsakenEntity entity;

    private BlockPos jumpTarget = null;

    private boolean hasPreformedJump = false;

    public ForsakenRandomlyJumpGoal(ForsakenEntity entity) {
        this.entity = entity;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.m_5448_();
        if (this.entity.m_20096_() && (target == null || !target.isAlive()) && this.entity.m_217043_().nextInt(140) == 0 && this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            BlockPos findTarget = this.findJumpTarget();
            if (findTarget != null) {
                this.jumpTarget = findTarget;
                return true;
            }
        }
        return false;
    }

    private BlockPos findJumpTarget() {
        Vec3 vec3 = DefaultRandomPos.getPos(this.entity, 25, 10);
        if (vec3 != null) {
            BlockPos blockpos = BlockPos.containing(vec3);
            AABB aabb = this.entity.m_20191_().move(vec3.add(0.5, 1.0, 0.5).subtract(this.entity.m_20182_()));
            if (this.entity.m_9236_().getBlockState(blockpos.below()).m_60804_(this.entity.m_9236_(), blockpos.below()) && this.entity.m_21439_(WalkNodeEvaluator.getBlockPathTypeStatic(this.entity.m_9236_(), blockpos.mutable())) == 0.0F && this.entity.m_9236_().m_5450_(this.entity, Shapes.create(aabb))) {
                return blockpos;
            }
        }
        return null;
    }

    @Override
    public void start() {
        this.hasPreformedJump = false;
        this.entity.m_21573_().stop();
        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.entity.setAnimation(ForsakenEntity.ANIMATION_PREPARE_JUMP);
        }
        this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, Vec3.atCenterOf(this.jumpTarget));
    }

    @Override
    public boolean canContinueToUse() {
        return (this.entity.getAnimation() == ForsakenEntity.ANIMATION_PREPARE_JUMP || this.entity.isLeaping()) && this.jumpTarget != null;
    }

    @Override
    public void tick() {
        if (this.entity.isLeaping() && !this.hasPreformedJump) {
            this.hasPreformedJump = true;
            Vec3 vec3 = this.entity.m_20184_();
            Vec3 vec31 = new Vec3((double) ((float) this.jumpTarget.m_123341_() + 0.5F) - this.entity.m_20185_(), 0.0, (double) ((float) this.jumpTarget.m_123343_() + 0.5F) - this.entity.m_20189_());
            if (vec31.length() > 100.0) {
                vec31 = vec3.normalize().scale(100.0);
            }
            if (vec31.lengthSqr() > 1.0E-7) {
                vec31 = vec31.scale(0.155F).add(vec3.scale(0.2));
            }
            this.entity.m_20334_(vec31.x, 0.2F + vec31.length() * 0.3F, vec31.z);
        }
    }
}