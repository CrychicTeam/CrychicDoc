package io.redspace.ironsspellbooks.effect;

import io.redspace.ironsspellbooks.api.magic.MagicData;
import io.redspace.ironsspellbooks.damage.DamageSources;
import io.redspace.ironsspellbooks.damage.ISSDamageTypes;
import io.redspace.ironsspellbooks.player.ClientMagicData;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class HeartstopEffect extends MagicMobEffect {

    private int duration;

    public HeartstopEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData.getPlayerMagicData(pLivingEntity).getSyncedData().addEffects(4L);
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        MagicData playerMagicData = MagicData.getPlayerMagicData(pLivingEntity);
        playerMagicData.getSyncedData().removeEffects(4L);
        if (pLivingEntity.f_19797_ > 60) {
            pLivingEntity.hurt(DamageSources.get(pLivingEntity.f_19853_, ISSDamageTypes.HEARTSTOP), playerMagicData.getSyncedData().getHeartstopAccumulatedDamage());
        } else {
            pLivingEntity.kill();
        }
        playerMagicData.getSyncedData().setHeartstopAccumulatedDamage(0.0F);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if (pLivingEntity.f_19853_.isClientSide && pLivingEntity instanceof Player player) {
            float damage = ClientMagicData.getSyncedSpellData(player).getHeartstopAccumulatedDamage();
            float f = 1.0F - Mth.clamp(damage / player.m_21223_(), 0.0F, 1.0F);
            int i = (int) (10.0F + 30.0F * f);
            if (this.duration % Math.max(i, 1) == 0) {
                player.playSound(SoundEvents.WARDEN_HEARTBEAT, 1.0F, 0.85F);
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        this.duration = pDuration;
        return true;
    }
}