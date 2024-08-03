package com.mna.effects.harmful;

import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.player.Player;

public class EffectDeath extends MobEffect implements INoCreeperLingering {

    public EffectDeath() {
        super(MobEffectCategory.HARMFUL, 0);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMapIn, int amplifier) {
        if (!(entity instanceof Player)) {
            if (entity.getPersistentData().contains("mna:deathflag")) {
                entity.hurt(entity.m_269291_().fellOutOfWorld(), Float.MAX_VALUE);
                if (entity.isAlive()) {
                    entity.remove(Entity.RemovalReason.KILLED);
                }
            }
        }
    }
}