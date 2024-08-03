package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.util.ParticleHelper;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;

public class AscensionEffect extends MagicMobEffect {

    public AscensionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(livingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(livingEntity).getSyncedData().removeEffects(16L);
        livingEntity.m_183634_();
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(16L);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        pLivingEntity.m_183634_();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    public static void ambientParticles(ClientLevel level, LivingEntity entity) {
        RandomSource random = entity.getRandom();
        for (int i = 0; i < 2; i++) {
            Vec3 motion = new Vec3((double) (random.nextFloat() * 2.0F - 1.0F), (double) (random.nextFloat() * 2.0F - 1.0F), (double) (random.nextFloat() * 2.0F - 1.0F));
            motion = motion.scale(0.04F);
            level.addParticle(ParticleHelper.ELECTRICITY, entity.m_20208_(0.4F), entity.m_20187_(), entity.m_20262_(0.4F), motion.x, motion.y, motion.z);
        }
    }
}