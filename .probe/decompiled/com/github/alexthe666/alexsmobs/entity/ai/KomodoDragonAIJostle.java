package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityKomodoDragon;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

public class KomodoDragonAIJostle extends Goal {

    private static final TargetingConditions JOSTLE_PREDICATE = TargetingConditions.forNonCombat().range(16.0).ignoreLineOfSight();

    protected EntityKomodoDragon targetKomodoDragon;

    private final EntityKomodoDragon komodo;

    private final Level world;

    private float angle;

    public KomodoDragonAIJostle(EntityKomodoDragon moose) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK, Goal.Flag.TARGET));
        this.komodo = moose;
        this.world = moose.m_9236_();
    }

    @Override
    public boolean canUse() {
        if (!this.komodo.isJostling() && !this.komodo.m_27593_() && !this.komodo.m_21827_() && !this.komodo.m_20160_() && !this.komodo.shouldFollow() && !this.komodo.m_20159_() && !this.komodo.m_6162_() && this.komodo.m_5448_() == null && this.komodo.jostleCooldown <= 0) {
            if (this.komodo.instantlyTriggerJostleAI || this.komodo.m_217043_().nextInt(30) == 0) {
                this.komodo.instantlyTriggerJostleAI = false;
                if (this.komodo.getJostlingPartner() instanceof EntityKomodoDragon) {
                    this.targetKomodoDragon = (EntityKomodoDragon) this.komodo.getJostlingPartner();
                    return this.targetKomodoDragon.jostleCooldown == 0;
                }
                EntityKomodoDragon possiblePartner = this.getNearbyKomodoDragon();
                if (possiblePartner != null) {
                    this.komodo.setJostlingPartner(possiblePartner);
                    possiblePartner.setJostlingPartner(this.komodo);
                    this.targetKomodoDragon = possiblePartner;
                    this.targetKomodoDragon.instantlyTriggerJostleAI = true;
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
        this.komodo.jostleTimer = 0;
        this.angle = 0.0F;
        this.setJostleDirection(this.komodo.m_217043_().nextBoolean());
    }

    public void setJostleDirection(boolean dir) {
        this.komodo.jostleDirection = dir;
        this.targetKomodoDragon.jostleDirection = dir;
    }

    @Override
    public void stop() {
        this.komodo.setJostling(false);
        this.komodo.setJostlingPartner(null);
        this.komodo.jostleTimer = 0;
        this.angle = 0.0F;
        this.komodo.m_21573_().stop();
        if (this.targetKomodoDragon != null) {
            this.targetKomodoDragon.setJostling(false);
            this.targetKomodoDragon.setJostlingPartner(null);
            this.targetKomodoDragon.jostleTimer = 0;
            this.targetKomodoDragon = null;
        }
    }

    @Override
    public void tick() {
        if (this.targetKomodoDragon != null) {
            this.komodo.m_21391_(this.targetKomodoDragon, 360.0F, 180.0F);
            this.komodo.setJostling(true);
            float x = (float) (this.komodo.m_20185_() - this.targetKomodoDragon.m_20185_());
            float y = Math.abs((float) (this.komodo.m_20186_() - this.targetKomodoDragon.m_20186_()));
            float z = (float) (this.komodo.m_20189_() - this.targetKomodoDragon.m_20189_());
            double distXZ = Math.sqrt((double) (x * x + z * z));
            if (distXZ < 1.8F) {
                this.komodo.m_21573_().stop();
                this.komodo.m_21566_().strafe(-0.5F, 0.0F);
            } else if (distXZ > 2.4F) {
                this.komodo.setJostling(false);
                this.komodo.m_21573_().moveTo(this.targetKomodoDragon, 1.0);
            } else {
                this.komodo.m_21391_(this.targetKomodoDragon, 360.0F, 180.0F);
                float f = this.komodo.m_217043_().nextFloat() - 0.5F;
                if (this.komodo.jostleDirection) {
                    if (this.angle < 10.0F) {
                        this.angle++;
                    } else {
                        this.komodo.jostleDirection = false;
                    }
                    this.komodo.m_21566_().strafe(f * 1.0F, -0.4F);
                }
                if (!this.komodo.jostleDirection) {
                    if (this.angle > -10.0F) {
                        this.angle--;
                    } else {
                        this.komodo.jostleDirection = true;
                    }
                    this.komodo.m_21566_().strafe(f * 1.0F, 0.4F);
                }
                if (this.komodo.m_217043_().nextInt(15) == 0 && this.komodo.m_20096_()) {
                    this.komodo.pushBackJostling(this.targetKomodoDragon, 0.1F);
                }
                this.komodo.nextJostleAngleFromServer = this.angle;
                this.komodo.jostleTimer++;
                this.targetKomodoDragon.jostleTimer++;
                if (this.komodo.jostleTimer > 500 || y > 2.0F) {
                    this.komodo.f_19812_ = true;
                    if (this.komodo.m_20096_()) {
                        this.komodo.pushBackJostling(this.targetKomodoDragon, 0.4F);
                    }
                    if (this.targetKomodoDragon.m_20096_()) {
                        this.targetKomodoDragon.pushBackJostling(this.komodo, 0.4F);
                    }
                    this.komodo.jostleTimer = 0;
                    this.targetKomodoDragon.jostleTimer = 0;
                    this.komodo.jostleCooldown = 700 + this.komodo.m_217043_().nextInt(2000);
                    this.targetKomodoDragon.jostleTimer = 0;
                    this.targetKomodoDragon.jostleCooldown = 700 + this.targetKomodoDragon.m_217043_().nextInt(2000);
                    this.stop();
                }
            }
        }
    }

    @Override
    public boolean canContinueToUse() {
        return !this.komodo.m_6162_() && !this.komodo.m_27593_() && !this.komodo.m_20160_() && !this.komodo.m_21827_() && this.komodo.m_5448_() == null && this.targetKomodoDragon != null && this.targetKomodoDragon.m_6084_() && this.komodo.jostleCooldown == 0 && this.targetKomodoDragon.jostleCooldown == 0;
    }

    @Nullable
    private EntityKomodoDragon getNearbyKomodoDragon() {
        List<EntityKomodoDragon> komodoDragons = this.world.m_45971_(EntityKomodoDragon.class, JOSTLE_PREDICATE, this.komodo, this.komodo.m_20191_().inflate(16.0));
        double lvt_2_1_ = Double.MAX_VALUE;
        EntityKomodoDragon lvt_4_1_ = null;
        for (EntityKomodoDragon lvt_6_1_ : komodoDragons) {
            if (this.komodo.canJostleWith(lvt_6_1_) && this.komodo.m_20280_(lvt_6_1_) < lvt_2_1_) {
                lvt_4_1_ = lvt_6_1_;
                lvt_2_1_ = this.komodo.m_20280_(lvt_6_1_);
            }
        }
        return lvt_4_1_;
    }
}