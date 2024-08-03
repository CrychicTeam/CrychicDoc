package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityMoose;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

public class MooseAIJostle extends Goal {

    private static final TargetingConditions JOSTLE_PREDICATE = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight();

    protected EntityMoose targetMoose;

    private EntityMoose moose;

    private Level world;

    private float angle;

    public MooseAIJostle(EntityMoose moose) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.moose = moose;
        this.world = moose.m_9236_();
    }

    @Override
    public boolean canUse() {
        if (!this.moose.isJostling() && this.moose.isAntlered() && !this.moose.m_6162_() && this.moose.m_5448_() == null && this.moose.jostleCooldown <= 0) {
            if (this.moose.instantlyTriggerJostleAI || this.moose.m_217043_().nextInt(30) == 0) {
                this.moose.instantlyTriggerJostleAI = false;
                if (this.moose.getJostlingPartner() instanceof EntityMoose) {
                    this.targetMoose = (EntityMoose) this.moose.getJostlingPartner();
                    return this.targetMoose.jostleCooldown == 0;
                }
                EntityMoose possiblePartner = this.getNearbyMoose();
                if (possiblePartner != null) {
                    this.moose.setJostlingPartner(possiblePartner);
                    possiblePartner.setJostlingPartner(this.moose);
                    this.targetMoose = possiblePartner;
                    this.targetMoose.instantlyTriggerJostleAI = true;
                    return true;
                }
            }
            return false;
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        this.moose.jostleTimer = 0;
        this.angle = 0.0F;
        this.setJostleDirection(this.moose.m_217043_().nextBoolean());
    }

    public void setJostleDirection(boolean dir) {
        this.moose.jostleDirection = dir;
        this.targetMoose.jostleDirection = dir;
    }

    @Override
    public void stop() {
        this.moose.setJostling(false);
        this.moose.setJostlingPartner(null);
        this.moose.jostleTimer = 0;
        this.angle = 0.0F;
        this.moose.m_21573_().stop();
        if (this.targetMoose != null) {
            this.targetMoose.setJostling(false);
            this.targetMoose.setJostlingPartner(null);
            this.targetMoose.jostleTimer = 0;
            this.targetMoose = null;
        }
    }

    @Override
    public void tick() {
        if (this.targetMoose != null) {
            this.moose.m_21391_(this.targetMoose, 360.0F, 180.0F);
            this.moose.setJostling(true);
            float f = (float) (this.moose.m_20185_() - this.targetMoose.m_20185_());
            float f1 = Math.abs((float) (this.moose.m_20186_() - this.targetMoose.m_20186_()));
            float f2 = (float) (this.moose.m_20189_() - this.targetMoose.m_20189_());
            double distXZ = Math.sqrt((double) (f * f + f2 * f2));
            if (distXZ < 4.0) {
                this.moose.m_21573_().stop();
                this.moose.m_21566_().strafe(-0.5F, 0.0F);
            } else if (distXZ > 4.5) {
                this.moose.setJostling(false);
                this.moose.m_21573_().moveTo(this.targetMoose, 1.0);
            } else {
                this.moose.m_21391_(this.targetMoose, 360.0F, 180.0F);
                if (this.moose.jostleDirection) {
                    if (this.angle < 30.0F) {
                        this.angle++;
                    }
                    this.moose.m_21566_().strafe(0.0F, -0.2F);
                }
                if (!this.moose.jostleDirection) {
                    if (this.angle > -30.0F) {
                        this.angle--;
                    }
                    this.moose.m_21566_().strafe(0.0F, 0.2F);
                }
                if (this.moose.m_217043_().nextInt(55) == 0 && this.moose.m_20096_()) {
                    this.moose.pushBackJostling(this.targetMoose, 0.2F);
                }
                if (this.moose.m_217043_().nextInt(25) == 0 && this.moose.m_20096_()) {
                    this.moose.playJostleSound();
                }
                this.moose.setJostleAngle(this.angle);
                if (this.moose.jostleTimer % 60 == 0 || this.moose.m_217043_().nextInt(80) == 0) {
                    this.setJostleDirection(!this.moose.jostleDirection);
                }
                this.moose.jostleTimer++;
                this.targetMoose.jostleTimer++;
                if (this.moose.jostleTimer > 1000 || f1 > 2.0F) {
                    this.moose.f_19812_ = true;
                    if (this.moose.m_20096_()) {
                        this.moose.pushBackJostling(this.targetMoose, 0.9F);
                    }
                    if (this.targetMoose.m_20096_()) {
                        this.targetMoose.pushBackJostling(this.moose, 0.9F);
                    }
                    this.moose.jostleTimer = 0;
                    this.targetMoose.jostleTimer = 0;
                    this.moose.jostleCooldown = 500 + this.moose.m_217043_().nextInt(2000);
                    this.targetMoose.jostleTimer = 0;
                    this.targetMoose.jostleCooldown = 500 + this.targetMoose.m_217043_().nextInt(2000);
                    this.stop();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.moose.m_6162_() && this.moose.isAntlered() && this.moose.m_5448_() == null && this.targetMoose != null && this.targetMoose.isAntlered() && this.targetMoose.m_6084_() && this.moose.jostleCooldown == 0 && this.targetMoose.jostleCooldown == 0;
    }

    @Nullable
    private EntityMoose getNearbyMoose() {
        List<EntityMoose> listOfMeese = this.world.m_45971_(EntityMoose.class, JOSTLE_PREDICATE, this.moose, this.moose.m_20191_().inflate(16.0));
        double lvt_2_1_ = Double.MAX_VALUE;
        EntityMoose lvt_4_1_ = null;
        for (EntityMoose lvt_6_1_ : listOfMeese) {
            if (this.moose.canJostleWith(lvt_6_1_) && this.moose.m_20280_(lvt_6_1_) < lvt_2_1_) {
                lvt_4_1_ = lvt_6_1_;
                lvt_2_1_ = this.moose.m_20280_(lvt_6_1_);
            }
        }
        return lvt_4_1_;
    }
}