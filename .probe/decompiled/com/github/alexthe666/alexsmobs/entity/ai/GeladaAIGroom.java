package com.github.alexthe666.alexsmobs.entity.ai;

import com.github.alexthe666.alexsmobs.entity.EntityGeladaMonkey;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.ai.goal.Goal;

public class GeladaAIGroom extends Goal {

    private final EntityGeladaMonkey monkey;

    private int groomTime = 0;

    private int groomCooldown = 220;

    private EntityGeladaMonkey beingGroomed;

    public GeladaAIGroom(EntityGeladaMonkey monkey) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.monkey = monkey;
    }

    @Override
    public boolean canUse() {
        if (this.groomCooldown > 0) {
            this.groomCooldown--;
            return false;
        } else {
            this.groomCooldown = 200 + this.monkey.m_217043_().nextInt(1000);
            EntityGeladaMonkey nearestMonkey = null;
            for (EntityGeladaMonkey entity : this.monkey.m_9236_().m_45976_(EntityGeladaMonkey.class, this.monkey.m_20191_().inflate(15.0))) {
                if (entity.m_19879_() != this.monkey.m_19879_() && this.monkey.canBeGroomed() && (nearestMonkey == null || this.monkey.m_20270_(nearestMonkey) > this.monkey.m_20270_(entity))) {
                    nearestMonkey = entity;
                }
            }
            this.beingGroomed = nearestMonkey;
            return this.beingGroomed != null;
        }
    }

    @Override
    public boolean canContinueToUse() {
        return this.beingGroomed != null && this.beingGroomed.m_6084_() && !this.beingGroomed.shouldStopBeingGroomed() && this.groomTime < 200 && (this.beingGroomed.groomerID == -1 || this.beingGroomed.groomerID == this.monkey.m_19879_());
    }

    @Override
    public void stop() {
        this.groomTime = 0;
        this.monkey.isGrooming = false;
        if (this.beingGroomed != null) {
            this.beingGroomed.groomerID = -1;
        }
        this.beingGroomed = null;
    }

    @Override
    public void tick() {
        double dist = (double) this.monkey.m_20270_(this.beingGroomed);
        if (dist < (double) (this.monkey.m_20205_() + 0.5F)) {
            this.monkey.isGrooming = true;
            this.beingGroomed.groomerID = this.monkey.m_19879_();
            this.monkey.setSitting(true);
            this.groomTime++;
            if (this.groomTime % 50 == 0) {
                this.monkey.m_5634_(1.0F);
            }
            if (this.monkey.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                this.monkey.setAnimation(EntityGeladaMonkey.ANIMATION_GROOM);
            }
            this.monkey.m_21573_().stop();
            this.monkey.m_21391_(this.beingGroomed, 360.0F, 360.0F);
        } else {
            this.monkey.isGrooming = false;
            this.beingGroomed.groomerID = -1;
            this.monkey.setSitting(false);
            this.monkey.m_21573_().moveTo(this.beingGroomed, 1.0);
        }
    }
}