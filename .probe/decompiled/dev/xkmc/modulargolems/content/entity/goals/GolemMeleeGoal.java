package dev.xkmc.modulargolems.content.entity.goals;

import dev.xkmc.modulargolems.compat.materials.cataclysm.NetheriteMonstrosityEarthquakeModifier;
import dev.xkmc.modulargolems.content.entity.common.AbstractGolemEntity;
import dev.xkmc.modulargolems.content.entity.common.GolemFlags;
import dev.xkmc.modulargolems.init.data.MGConfig;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;

public class GolemMeleeGoal extends MeleeAttackGoal {

    private final AbstractGolemEntity<?, ?> golem;

    private double lastDist;

    private double timeNoMovement;

    private boolean earthQuake = false;

    private static double getDistance(double a0, double a1, double b0, double b1) {
        if (a1 < b0) {
            return b0 - a1;
        } else {
            return b1 < a0 ? a0 - b1 : 0.0;
        }
    }

    public static double calculateDistSqr(AbstractGolemEntity<?, ?> golem, LivingEntity target) {
        AABB aabb0 = golem.m_20191_();
        AABB aabb1 = target.m_20191_();
        double x = getDistance(aabb0.minX, aabb0.maxX, aabb1.minX, aabb1.maxX);
        double y = getDistance(aabb0.minY, aabb0.maxY, aabb1.minY, aabb1.maxY);
        double z = getDistance(aabb0.minZ, aabb0.maxZ, aabb1.minZ, aabb1.maxZ);
        return x * x + y * y + z * z;
    }

    public static int getTargetResetTime() {
        return MGConfig.COMMON.targetResetTime.get();
    }

    public static double getTargetDistanceDelta() {
        return MGConfig.COMMON.targetResetNoMovementRange.get();
    }

    public GolemMeleeGoal(AbstractGolemEntity<?, ?> entity) {
        super(entity, 1.0, true);
        this.golem = entity;
    }

    @Override
    protected int adjustedTickDelay(int tick) {
        double speed = this.f_25540_.m_21133_(Attributes.ATTACK_SPEED);
        return (int) Math.ceil((double) super.m_183277_(tick) / Math.min(1.0, speed));
    }

    @Override
    public double getAttackReachSqr(LivingEntity pAttackTarget) {
        double val = this.f_25540_.m_21133_(ForgeMod.ENTITY_REACH.get());
        return val * val;
    }

    public boolean canReachTarget(LivingEntity le) {
        return this.getAttackReachSqr(le) >= this.f_25540_.m_262793_(le);
    }

    @Override
    public void tick() {
        if (this.m_25564_() && this.golem.m_5448_() != null) {
            this.timeNoMovement++;
        }
        super.tick();
    }

    @Override
    protected void checkAndPerformAttack(LivingEntity target, double distSqr) {
        if (this.m_25564_()) {
            double dist = Math.sqrt(distSqr);
            if (dist < this.lastDist - getTargetDistanceDelta()) {
                this.lastDist = dist;
                this.timeNoMovement = 0.0;
            }
        }
        this.doRealAttack(target, distSqr);
        if (!this.m_25564_()) {
            this.lastDist = 1000.0;
            this.timeNoMovement = 0.0;
        } else if (this.timeNoMovement > (double) getTargetResetTime()) {
            this.golem.resetTarget(null);
            this.lastDist = 1000.0;
            this.timeNoMovement = 0.0;
        }
    }

    protected void doRealAttack(LivingEntity target, double distSqr) {
        if (this.m_25564_() && this.golem.hasFlag(GolemFlags.EARTH_QUAKE) && !this.golem.m_20069_() && this.golem.m_20096_()) {
            if (this.earthQuake) {
                this.earthQuake = false;
                this.m_25563_();
                NetheriteMonstrosityEarthquakeModifier.performEarthQuake(this.golem);
                return;
            }
            double d0 = this.getAttackReachSqr(target);
            if (d0 < distSqr && distSqr <= d0 + 5.0) {
                this.golem.m_246865_(new Vec3(0.0, 1.0, 0.0));
                this.golem.f_19812_ = true;
                this.earthQuake = true;
                return;
            }
        }
        if (!this.earthQuake || this.golem.m_20096_()) {
            super.checkAndPerformAttack(target, distSqr);
        }
    }
}