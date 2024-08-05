package dev.xkmc.modulargolems.content.entity.ranged;

import dev.xkmc.modulargolems.content.entity.goals.GolemMeleeGoal;
import dev.xkmc.modulargolems.content.entity.humanoid.HumanoidGolemEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.RangedAttackGoal;

public class GolemTridentAttackGoal extends RangedAttackGoal {

    private final HumanoidGolemEntity golem;

    private final GolemMeleeGoal melee;

    public GolemTridentAttackGoal(HumanoidGolemEntity pRangedAttackMob, double pSpeedModifier, int pAttackInterval, float pAttackRadius, GolemMeleeGoal melee) {
        super(pRangedAttackMob, pSpeedModifier, pAttackInterval, pAttackRadius);
        this.golem = pRangedAttackMob;
        this.melee = melee;
    }

    @Override
    public boolean canUse() {
        LivingEntity livingentity = this.golem.m_5448_();
        if (livingentity != null && super.canUse()) {
            double d0 = this.golem.m_20275_(livingentity.m_20185_(), livingentity.m_20186_(), livingentity.m_20189_());
            if (this.melee.canReachTarget(livingentity)) {
                return false;
            } else {
                InteractionHand hand = this.golem.getWeaponHand();
                return GolemShooterHelper.isValidThrowableWeapon(this.golem, this.golem.m_21120_(hand), hand).isThrowable();
            }
        } else {
            return false;
        }
    }

    @Override
    public void start() {
        super.m_8056_();
        this.golem.m_21561_(true);
        this.golem.m_6672_(this.golem.getWeaponHand());
    }

    @Override
    public void stop() {
        super.stop();
        this.golem.m_5810_();
        this.golem.m_21561_(false);
    }
}