package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.item.SubmarineEntity;
import com.github.alexmodguy.alexscaves.server.entity.living.HullbreakerEntity;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import java.util.List;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

public class HullbreakerInspectMobGoal extends Goal {

    private HullbreakerEntity entity;

    private Vec3 startCirclingAt;

    private LivingEntity inspectingTarget;

    private boolean clockwise;

    private int phaseTime;

    private int maxPhaseTime;

    private boolean staring = false;

    public HullbreakerInspectMobGoal(HullbreakerEntity hullbreaker) {
        this.entity = hullbreaker;
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.entity.m_5448_();
        long worldTime = this.entity.m_9236_().getGameTime() % 10L;
        if (this.entity.m_217043_().nextInt(60) == 0 || worldTime == 0L || target != null && target.isAlive()) {
            AABB aabb = this.entity.m_20191_().inflate(80.0);
            List<LivingEntity> list = this.entity.m_9236_().m_6443_(LivingEntity.class, aabb, HullbreakerEntity.GLOWING_TARGET);
            if (list.isEmpty()) {
                return false;
            } else {
                LivingEntity closest = null;
                for (LivingEntity mob : list) {
                    if ((closest == null || mob.m_20280_(this.entity) < closest.m_20280_(this.entity)) && this.entity.m_142582_(mob)) {
                        closest = mob;
                    }
                }
                this.inspectingTarget = closest;
                return this.inspectingTarget != null;
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean canContinueToUse() {
        LivingEntity target = this.entity.m_5448_();
        return this.inspectingTarget != null && this.inspectingTarget.isAlive() && (target == null || !target.isAlive());
    }

    @Override
    public void start() {
        this.staring = true;
        this.clockwise = this.entity.m_217043_().nextBoolean();
        this.phaseTime = 0;
        this.maxPhaseTime = 60 + 40 * Math.min(0, 5 - this.entity.getInterestLevel());
    }

    @Override
    public void tick() {
        double distance = (double) this.entity.m_20270_(this.inspectingTarget);
        if (this.entity.getInterestLevel() >= 5 && HullbreakerEntity.GLOWING_TARGET.test(this.inspectingTarget)) {
            if (!(this.inspectingTarget instanceof Player) || !((Player) this.inspectingTarget).isCreative()) {
                this.entity.m_6710_(this.inspectingTarget);
            }
            this.inspectingTarget = null;
        } else {
            if (this.entity.m_217043_().nextInt(20) == 0 && !HullbreakerEntity.GLOWING_TARGET.test(this.inspectingTarget)) {
                this.inspectingTarget = null;
            } else {
                if (this.entity.getAnimation() == HullbreakerEntity.ANIMATION_PUZZLE && this.entity.getAnimationTick() > 50) {
                    this.phaseTime = this.maxPhaseTime;
                }
                if (this.phaseTime++ > this.maxPhaseTime) {
                    this.entity.setInterestLevel(this.entity.getInterestLevel() + 1);
                    this.staring = this.entity.m_217043_().nextBoolean() && !this.staring;
                    this.phaseTime = 0;
                    this.startCirclingAt = this.inspectingTarget.m_146892_();
                    this.maxPhaseTime = this.staring ? 120 : 120 + 80 * Math.min(0, 5 - this.entity.getInterestLevel());
                }
                if (this.staring) {
                    this.entity.m_7618_(EntityAnchorArgument.Anchor.EYES, this.inspectingTarget.m_146892_());
                    if (this.isPreyWatching() && distance < 18.0) {
                        if (this.entity.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                            this.entity.setAnimation(HullbreakerEntity.ANIMATION_PUZZLE);
                        }
                        this.entity.m_21573_().stop();
                    } else if (this.entity.m_21573_().isDone()) {
                        Vec3 frontVision = this.inspectingTarget.m_146892_().add(this.inspectingTarget.m_20154_().scale(12.0));
                        this.entity.m_21573_().moveTo(frontVision.x, frontVision.y, frontVision.z, 1.0);
                    }
                } else {
                    if (this.startCirclingAt == null) {
                        this.startCirclingAt = this.inspectingTarget.m_146892_();
                    }
                    Vec3 circle = this.orbitAroundPos(this.inspectingTarget.m_146892_(), (float) (12 + Math.min(0, 5 - this.entity.getInterestLevel()) * 3));
                    this.entity.m_21573_().moveTo(circle.x, circle.y, circle.z, 1.4F);
                    this.entity.m_5616_(this.entity.f_20883_ + (float) (this.clockwise ? 30 : -30));
                }
                SubmarineEntity.alertSubmarineMountOf(this.inspectingTarget);
            }
        }
    }

    public boolean isPreyWatching() {
        if (!(this.inspectingTarget instanceof Player)) {
            return true;
        } else {
            Entity lowestPrey = this.inspectingTarget.m_20201_();
            Vec3 vec3 = new Vec3(this.entity.m_20185_(), this.entity.m_20188_(), this.entity.m_20189_());
            Vec3 vec31 = new Vec3(lowestPrey.getX(), lowestPrey.getEyeY(), lowestPrey.getZ());
            return vec31.distanceTo(vec3) > 128.0 ? false : this.entity.m_9236_().m_45547_(new ClipContext(vec3, vec31, ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, lowestPrey)).getType() == HitResult.Type.MISS;
        }
    }

    @Override
    public void stop() {
        LivingEntity target = this.entity.m_5448_();
        if (target == null || !target.isAlive()) {
            this.entity.setInterestLevel(0);
        }
    }

    public Vec3 orbitAroundPos(Vec3 target, float circleDistance) {
        float angle = 3.0F * (float) (Math.PI * (double) (this.clockwise ? -this.phaseTime : this.phaseTime) / (double) ((float) this.maxPhaseTime));
        double extraX = (double) (circleDistance * Mth.sin(angle));
        double extraZ = (double) (circleDistance * Mth.cos(angle));
        return this.startCirclingAt.add(extraX, -1.0, extraZ);
    }
}