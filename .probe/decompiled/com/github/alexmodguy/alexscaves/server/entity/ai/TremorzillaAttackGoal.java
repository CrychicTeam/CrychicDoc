package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.TremorzillaEntity;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class TremorzillaAttackGoal extends Goal {

    private TremorzillaEntity tremorzilla;

    private Vec3 lastNavToPos;

    public TremorzillaAttackGoal(TremorzillaEntity tremorzilla) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.tremorzilla = tremorzilla;
    }

    @Override
    public boolean canUse() {
        LivingEntity target = this.tremorzilla.m_5448_();
        return target != null && target.isAlive() && !this.tremorzilla.isDancing();
    }

    @Override
    public void start() {
        this.lastNavToPos = null;
    }

    @Override
    public void stop() {
    }

    @Override
    public void tick() {
        LivingEntity target = this.tremorzilla.m_5448_();
        if (target != null) {
            double dist = (double) this.tremorzilla.m_20270_(target);
            float combinedDist = this.tremorzilla.m_20205_() + target.m_20205_();
            if (!this.tremorzilla.isFiring()) {
                if (this.tremorzilla.isPowered() && !this.tremorzilla.wantsToUseBeamFromServer && this.tremorzilla.m_217043_().nextInt(100) == 0 && !this.tremorzilla.m_6162_() && !this.tremorzilla.m_21825_()) {
                    this.tremorzilla.wantsToUseBeamFromServer = true;
                }
                if (!this.tremorzilla.wantsToUseBeamFromServer && this.tremorzilla.getAnimation() != TremorzillaEntity.ANIMATION_RIGHT_TAIL && this.tremorzilla.getAnimation() != TremorzillaEntity.ANIMATION_LEFT_TAIL && this.tremorzilla.getAnimation() != TremorzillaEntity.ANIMATION_LEFT_STOMP && this.tremorzilla.getAnimation() != TremorzillaEntity.ANIMATION_RIGHT_STOMP) {
                    this.tremorzilla.m_21563_().setLookAt(target.m_20185_(), target.m_20188_(), target.m_20189_(), 1.0F, (float) this.tremorzilla.m_8132_());
                }
                if (this.lastNavToPos == null || this.tremorzilla.m_21573_().isDone() && dist > (double) combinedDist + 1.0 || this.lastNavToPos.distanceTo(target.m_20182_()) > (double) this.tremorzilla.m_20205_() - 1.0) {
                    this.tremorzilla.m_21573_().moveTo(target, 1.0);
                }
            }
            if (dist < (double) combinedDist + 3.0 && !this.tremorzilla.isFiring() && this.tremorzilla.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                float decision = this.tremorzilla.m_217043_().nextFloat();
                if ((double) decision < 0.25) {
                    this.tryAnimation(this.tremorzilla.m_217043_().nextBoolean() ? TremorzillaEntity.ANIMATION_LEFT_SCRATCH : TremorzillaEntity.ANIMATION_RIGHT_SCRATCH);
                } else if ((double) decision < 0.5 && !this.tremorzilla.m_6069_() && !this.tremorzilla.m_6162_()) {
                    this.tryAnimation(this.tremorzilla.m_217043_().nextBoolean() ? TremorzillaEntity.ANIMATION_LEFT_STOMP : TremorzillaEntity.ANIMATION_RIGHT_STOMP);
                } else if ((double) decision < 0.75 && !this.tremorzilla.m_6069_() && !this.tremorzilla.m_6162_()) {
                    this.tryAnimation(this.tremorzilla.m_217043_().nextBoolean() ? TremorzillaEntity.ANIMATION_LEFT_TAIL : TremorzillaEntity.ANIMATION_RIGHT_TAIL);
                } else {
                    this.tryAnimation(TremorzillaEntity.ANIMATION_BITE);
                }
            }
            if (!this.tremorzilla.wantsToUseBeamFromServer && !this.tremorzilla.m_6162_()) {
                this.tremorzilla.tryRoar();
            }
        }
    }

    private boolean tryAnimation(Animation animation) {
        if (this.tremorzilla.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.tremorzilla.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }
}