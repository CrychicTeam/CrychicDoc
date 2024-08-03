package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.api.util.Utils;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.registries.SoundRegistry;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class AbyssalShroudEffect extends MagicMobEffect {

    public AbyssalShroudEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(8L);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(8L);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (!livingEntity.m_9236_().isClientSide && !damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION) && !damageSource.is(DamageTypeTags.IS_FALL) && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            RandomSource random = livingEntity.getRandom();
            Level level = livingEntity.m_9236_();
            Vec3 sideStep = new Vec3(random.nextBoolean() ? 1.0 : -1.0, 0.0, -0.25);
            sideStep.yRot(livingEntity.m_146908_());
            particleCloud(livingEntity);
            Vec3 ground = livingEntity.m_20182_().add(sideStep);
            ground = Utils.moveToRelativeGroundLevel(level, ground, 2, 1);
            if (livingEntity.m_20159_()) {
                livingEntity.stopRiding();
            }
            if (!level.getBlockState(BlockPos.containing(ground).below()).m_60795_()) {
                livingEntity.m_6021_(ground.x, ground.y, ground.z);
                particleCloud(livingEntity);
            }
            if (damageSource.getEntity() != null) {
                livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, damageSource.getEntity().getEyePosition().subtract(0.0, 0.15, 0.0));
            }
            level.playSound(null, livingEntity.m_20185_(), livingEntity.m_20186_(), livingEntity.m_20189_(), SoundRegistry.ABYSSAL_TELEPORT.get(), SoundSource.AMBIENT, 1.0F, 0.9F + random.nextFloat() * 0.2F);
            return true;
        } else {
            return false;
        }
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.m_20182_().add(0.0, (double) (entity.m_20206_() / 2.0F), 0.0);
        MagicManager.spawnParticles(entity.m_9236_(), ParticleTypes.SMOKE, pos.x, pos.y, pos.z, 70, (double) (entity.m_20205_() / 4.0F), (double) (entity.m_20206_() / 5.0F), (double) (entity.m_20205_() / 4.0F), 0.035, false);
    }

    public static void ambientParticles(ClientLevel level, LivingEntity entity) {
        Vec3 backwards = entity.m_20156_().scale(0.003).reverse().add(0.0, 0.02, 0.0);
        RandomSource random = entity.getRandom();
        for (int i = 0; i < 2; i++) {
            Vec3 motion = new Vec3((double) (random.nextFloat() * 2.0F - 1.0F), (double) (random.nextFloat() * 2.0F - 1.0F), (double) (random.nextFloat() * 2.0F - 1.0F));
            motion = motion.scale(0.04F).add(backwards);
            level.addParticle(ParticleTypes.SMOKE, entity.m_20208_(0.4F), entity.m_20187_(), entity.m_20262_(0.4F), motion.x, motion.y, motion.z);
        }
    }
}