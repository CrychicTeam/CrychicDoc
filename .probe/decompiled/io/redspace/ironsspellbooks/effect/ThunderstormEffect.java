package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.registry.SpellRegistry;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.entity.spells.LightningStrike;
import javax.annotation.Nullable;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;

@EventBusSubscriber
public class ThunderstormEffect extends MagicMobEffect {

    public ThunderstormEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration % 40 == 0;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int pAmplifier) {
        int radiusSqr = 400;
        entity.f_19853_.m_6443_(LivingEntity.class, entity.m_20191_().inflate(20.0, 12.0, 20.0), livingEntity -> livingEntity != entity && this.horizontalDistanceSqr(livingEntity, entity) < (float) radiusSqr && livingEntity.isPickable() && !livingEntity.m_5833_() && !Utils.shouldHealEntity(entity, livingEntity) && Utils.hasLineOfSight(entity.f_19853_, entity, livingEntity, false)).forEach(targetEntity -> {
            LightningStrike lightningStrike = new LightningStrike(entity.f_19853_);
            lightningStrike.m_5602_(entity);
            lightningStrike.setDamage(getDamageFromAmplifier(pAmplifier, entity));
            lightningStrike.m_146884_(targetEntity.m_20182_());
            entity.f_19853_.m_7967_(lightningStrike);
        });
    }

    private float horizontalDistanceSqr(LivingEntity livingEntity, LivingEntity entity2) {
        double dx = livingEntity.m_20185_() - entity2.m_20185_();
        double dz = livingEntity.m_20189_() - entity2.m_20189_();
        return (float) (dx * dx + dz * dz);
    }

    public static float getDamageFromAmplifier(int effectAmplifier, @Nullable LivingEntity caster) {
        float power = caster == null ? 1.0F : SpellRegistry.THUNDERSTORM_SPELL.get().getEntityPowerMultiplier(caster);
        return (float) (effectAmplifier - 7) * power + 7.0F;
    }
}