package com.github.alexmodguy.alexscaves.server.entity.ai;

import com.github.alexmodguy.alexscaves.server.entity.living.GrottoceratopsEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACSoundRegistry;
import com.github.alexthe666.citadel.animation.Animation;
import com.github.alexthe666.citadel.animation.IAnimatedEntity;
import java.util.EnumSet;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.Goal;

public class GrottoceratopsMeleeGoal extends Goal {

    private GrottoceratopsEntity grottoceratops;

    private float startTailYaw = 0.0F;

    public GrottoceratopsMeleeGoal(GrottoceratopsEntity grottoceratops) {
        this.m_7021_(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
        this.grottoceratops = grottoceratops;
    }

    @Override
    public boolean canUse() {
        return this.grottoceratops.m_5448_() != null && this.grottoceratops.m_5448_().isAlive();
    }

    @Override
    public void stop() {
        this.startTailYaw = 0.0F;
    }

    @Override
    public void tick() {
        LivingEntity target = this.grottoceratops.m_5448_();
        if (target != null) {
            double dist = (double) this.grottoceratops.m_20270_(target);
            if (dist < (double) (this.grottoceratops.m_20205_() + target.m_20205_()) + 3.0 && this.grottoceratops.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
                if (this.grottoceratops.m_217043_().nextBoolean()) {
                    this.tryAnimation(GrottoceratopsEntity.ANIMATION_MELEE_RAM);
                } else {
                    boolean left = this.grottoceratops.m_217043_().nextBoolean();
                    this.startTailYaw = this.grottoceratops.m_146908_() + (float) (left ? 30 : -30);
                    this.tryAnimation(left ? GrottoceratopsEntity.ANIMATION_MELEE_TAIL_1 : GrottoceratopsEntity.ANIMATION_MELEE_TAIL_2);
                }
            }
            if (this.grottoceratops.getAnimation() == GrottoceratopsEntity.ANIMATION_MELEE_RAM && this.grottoceratops.getAnimationTick() > 10 && this.grottoceratops.getAnimationTick() <= 12) {
                this.checkAndDealDamage(target, 1.0F);
            }
            if (this.grottoceratops.getAnimation() != GrottoceratopsEntity.ANIMATION_MELEE_TAIL_1 && this.grottoceratops.getAnimation() != GrottoceratopsEntity.ANIMATION_MELEE_TAIL_2) {
                this.grottoceratops.m_7618_(EntityAnchorArgument.Anchor.EYES, target.m_146892_());
                this.grottoceratops.m_21573_().moveTo(target, 1.35);
            } else if (this.grottoceratops.getAnimationTick() > 10 && this.grottoceratops.getAnimationTick() <= 12) {
                this.checkAndDealDamage(target, 1.5F);
            }
        }
    }

    private void checkAndDealDamage(LivingEntity target, float multiplier) {
        if (this.grottoceratops.m_142582_(target) && (double) this.grottoceratops.m_20270_(target) < (double) (this.grottoceratops.m_20205_() + target.m_20205_()) + 1.0) {
            this.grottoceratops.m_216990_(ACSoundRegistry.GROTTOCERATOPS_ATTACK.get());
            target.hurt(target.m_269291_().mobAttack(this.grottoceratops), (float) this.grottoceratops.m_21051_(Attributes.ATTACK_DAMAGE).getValue() * multiplier);
            target.knockback(0.8 + 0.5 * (double) multiplier, this.grottoceratops.m_20185_() - target.m_20185_(), this.grottoceratops.m_20189_() - target.m_20189_());
        }
    }

    private boolean tryAnimation(Animation animation) {
        if (this.grottoceratops.getAnimation() == IAnimatedEntity.NO_ANIMATION) {
            this.grottoceratops.setAnimation(animation);
            return true;
        } else {
            return false;
        }
    }
}