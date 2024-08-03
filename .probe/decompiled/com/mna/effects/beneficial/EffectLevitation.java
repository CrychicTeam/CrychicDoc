package com.mna.effects.beneficial;

import com.mna.ManaAndArtifice;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;

public class EffectLevitation extends MobEffect implements INoCreeperLingering {

    public EffectLevitation() {
        super(MobEffectCategory.BENEFICIAL, 0);
        this.m_19472_(Attributes.FLYING_SPEED, "1954c4d7-bfe9-48d2-b3da-0331a0c5c347", 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player player) {
            MobEffectInstance activeEffect = player.m_21124_(this);
            if (!activeEffect.isInfiniteDuration() && activeEffect.getDuration() <= 5) {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            } else {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
                ManaAndArtifice.instance.proxy.setFlySpeed(player, 0.01F);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity living, AttributeMap attributemods, int p_111187_3_) {
        super.removeAttributeModifiers(living, attributemods, p_111187_3_);
        if (living instanceof ServerPlayer) {
            ManaAndArtifice.instance.proxy.setFlightEnabled((ServerPlayer) living, false);
        } else if (living instanceof Player && living.m_9236_().isClientSide()) {
            ManaAndArtifice.instance.proxy.setFlySpeed((Player) living, 0.05F);
        }
    }
}