package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class AirborneEffect extends MobEffect {

    public static final float damage_per_amp = 0.5F;

    public AirborneEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (!livingEntity.f_19853_.isClientSide && livingEntity.f_19862_) {
            double d11 = livingEntity.m_20184_().horizontalDistance();
            float f1 = (float) (d11 * 10.0 - 1.0);
            if (f1 > 0.0F) {
                livingEntity.m_5496_(SoundEvents.HOSTILE_BIG_FALL, 2.0F, 1.5F);
                livingEntity.hurt(livingEntity.m_269291_().flyIntoWall(), getDamageFromLevel(pAmplifier + 1));
                livingEntity.removeEffect(MobEffectRegistry.AIRBORNE.get());
            }
        }
    }

    public static float getDamageFromLevel(int level) {
        return 4.0F + (float) level * 0.5F;
    }
}