package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.capabilities.magic.MagicManager;
import io.redspace.ironsspellbooks.capabilities.magic.SyncedSpellData;
import io.redspace.ironsspellbooks.datagen.DamageTypeTagGenerator;
import io.redspace.ironsspellbooks.registries.MobEffectRegistry;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class EvasionEffect extends CustomDescriptionMobEffect {

    public EvasionEffect(MobEffectCategory mobEffectCategory, int color) {
        super(mobEffectCategory, color);
    }

    @Override
    public Component getDescriptionLine(MobEffectInstance instance) {
        int amp = instance.getAmplifier() + 1;
        return Component.translatable("tooltip.irons_spellbooks.evasion_description", amp).withStyle(ChatFormatting.BLUE);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().removeEffects(2L);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(2L);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().setEvasionHitsRemaining(pAmplifier);
    }

    public static boolean doEffect(LivingEntity livingEntity, DamageSource damageSource) {
        if (!livingEntity.f_19853_.isClientSide && !damageSource.is(DamageTypeTags.IS_FALL) && !damageSource.is(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.is(DamageTypeTagGenerator.BYPASS_EVASION)) {
            SyncedSpellData data = MagicData.getPlayerMagicData(livingEntity).getSyncedData();
            data.subtractEvasionHit();
            if (data.getEvasionHitsRemaining() < 0) {
                livingEntity.removeEffect(MobEffectRegistry.EVASION.get());
            }
            double d0 = livingEntity.m_20185_();
            double d1 = livingEntity.m_20186_();
            double d2 = livingEntity.m_20189_();
            double maxRadius = 18.0;
            Level level = livingEntity.f_19853_;
            RandomSource random = livingEntity.getRandom();
            for (int i = 0; i < 16; i++) {
                double minRadius = maxRadius / 2.0;
                Vec3 vec = new Vec3((double) random.nextInt((int) minRadius, (int) maxRadius), 0.0, 0.0);
                int degrees = random.nextInt(360);
                vec = vec.yRot((float) degrees);
                double x = d0 + vec.x;
                double y = Mth.clamp(livingEntity.m_20186_() + ((double) livingEntity.getRandom().nextInt((int) maxRadius) - maxRadius / 2.0), (double) level.m_141937_(), (double) (level.m_141937_() + ((ServerLevel) level).getLogicalHeight() - 1));
                double z = d2 + vec.z;
                if (livingEntity.m_20159_()) {
                    livingEntity.stopRiding();
                }
                if (livingEntity.randomTeleport(x, y, z, true)) {
                    if (damageSource.getEntity() != null) {
                        livingEntity.lookAt(EntityAnchorArgument.Anchor.EYES, damageSource.getEntity().getEyePosition());
                    }
                    level.playSound((Player) null, d0, d1, d2, SoundEvents.ENDERMAN_TELEPORT, SoundSource.PLAYERS, 1.0F, 1.0F);
                    livingEntity.m_5496_(SoundEvents.ENDERMAN_TELEPORT, 2.0F, 1.0F);
                    break;
                }
                if (maxRadius > 2.0) {
                    maxRadius--;
                }
            }
            particleCloud(livingEntity);
            return true;
        } else {
            return false;
        }
    }

    private static void particleCloud(LivingEntity entity) {
        Vec3 pos = entity.m_20182_().add(0.0, (double) (entity.m_20206_() / 2.0F), 0.0);
        MagicManager.spawnParticles(entity.f_19853_, ParticleTypes.PORTAL, pos.x, pos.y, pos.z, 70, (double) (entity.m_20205_() / 4.0F), (double) (entity.m_20206_() / 5.0F), (double) (entity.m_20205_() / 4.0F), 0.035, false);
    }
}