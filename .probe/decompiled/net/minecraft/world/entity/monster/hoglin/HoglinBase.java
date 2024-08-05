package net.minecraft.world.entity.monster.hoglin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.Vec3;

public interface HoglinBase {

    int ATTACK_ANIMATION_DURATION = 10;

    int getAttackAnimationRemainingTicks();

    static boolean hurtAndThrowTarget(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        float $$2 = (float) livingEntity0.getAttributeValue(Attributes.ATTACK_DAMAGE);
        float $$3;
        if (!livingEntity0.isBaby() && (int) $$2 > 0) {
            $$3 = $$2 / 2.0F + (float) livingEntity0.m_9236_().random.nextInt((int) $$2);
        } else {
            $$3 = $$2;
        }
        boolean $$5 = livingEntity1.hurt(livingEntity0.m_269291_().mobAttack(livingEntity0), $$3);
        if ($$5) {
            livingEntity0.m_19970_(livingEntity0, livingEntity1);
            if (!livingEntity0.isBaby()) {
                throwTarget(livingEntity0, livingEntity1);
            }
        }
        return $$5;
    }

    static void throwTarget(LivingEntity livingEntity0, LivingEntity livingEntity1) {
        double $$2 = livingEntity0.getAttributeValue(Attributes.ATTACK_KNOCKBACK);
        double $$3 = livingEntity1.getAttributeValue(Attributes.KNOCKBACK_RESISTANCE);
        double $$4 = $$2 - $$3;
        if (!($$4 <= 0.0)) {
            double $$5 = livingEntity1.m_20185_() - livingEntity0.m_20185_();
            double $$6 = livingEntity1.m_20189_() - livingEntity0.m_20189_();
            float $$7 = (float) (livingEntity0.m_9236_().random.nextInt(21) - 10);
            double $$8 = $$4 * (double) (livingEntity0.m_9236_().random.nextFloat() * 0.5F + 0.2F);
            Vec3 $$9 = new Vec3($$5, 0.0, $$6).normalize().scale($$8).yRot($$7);
            double $$10 = $$4 * (double) livingEntity0.m_9236_().random.nextFloat() * 0.5;
            livingEntity1.m_5997_($$9.x, $$10, $$9.z);
            livingEntity1.f_19864_ = true;
        }
    }
}