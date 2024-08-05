package com.mna.effects.beneficial;

import com.mna.capabilities.entity.MAPFX;
import com.mna.effects.interfaces.INoCreeperLingering;
import com.mna.effects.particles.EffectWithCustomClientParticles;
import com.mna.tools.EntityUtil;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EffectTrueInvisibility extends EffectWithCustomClientParticles implements INoCreeperLingering {

    public EffectTrueInvisibility() {
        super(MobEffectCategory.BENEFICIAL, 0, MAPFX.Flag.CANCEL_RENDER);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        if (entityLivingBaseIn instanceof Player) {
            EntityUtil.removeInvisibility(entityLivingBaseIn);
        }
    }

    @Override
    public void applyEffectTick(LivingEntity entityLivingBaseIn, int amplifier) {
        entityLivingBaseIn.m_6842_(true);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }
}