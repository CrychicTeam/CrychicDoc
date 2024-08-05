package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

public class PlanarSightEffect extends MagicMobEffect {

    public PlanarSightEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(128L);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(128L);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity livingEntity, int pAmplifier) {
        if (livingEntity.f_19853_.isClientSide && livingEntity == Minecraft.getInstance().player) {
            for (int i = 0; i < 3; i++) {
                Vec3 pos = new Vec3(Utils.getRandomScaled(16.0), Utils.getRandomScaled(5.0) + 5.0, Utils.getRandomScaled(16.0)).add(livingEntity.m_20182_());
                Vec3 random = new Vec3(Utils.getRandomScaled(0.08F), Utils.getRandomScaled(0.08F), Utils.getRandomScaled(0.08F));
                livingEntity.f_19853_.addParticle(ParticleTypes.WHITE_ASH, pos.x, pos.y, pos.z, random.x, random.y, random.z);
            }
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class EcholocationBlindnessFogFunction implements FogRenderer.MobEffectFogFunction {

        @Override
        public MobEffect getMobEffect() {
            return MobEffectRegistry.PLANAR_SIGHT.get();
        }

        @Override
        public void setupFog(FogRenderer.FogData fogData, LivingEntity entity, MobEffectInstance mobEffectInstance, float float0, float float1) {
            float f = 160.0F;
            if (fogData.mode == FogRenderer.FogMode.FOG_SKY) {
                fogData.start = 0.0F;
                fogData.end = f * 0.25F;
            } else {
                fogData.start = -f * 0.5F;
                fogData.end = f;
            }
        }
    }
}