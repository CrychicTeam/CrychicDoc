package com.mna.effects.harmful;

import com.mna.ManaAndArtifice;
import com.mna.effects.interfaces.INoCreeperLingering;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EffectEntangle extends MobEffect implements INoCreeperLingering {

    public EffectEntangle() {
        super(MobEffectCategory.HARMFUL, 0);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "845ba178-29e0-4845-84ec-69a11b705380", -0.95, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        super.applyEffectTick(entity, amplifier);
        if (entity instanceof Player player) {
            ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
        }
        Vec3 scaledVel = entity.m_20184_().scale(0.05F);
        entity.m_20334_(scaledVel.x, entity.m_20184_().y > 0.0 ? scaledVel.y : entity.m_20184_().y - 0.01F, scaledVel.z);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap attributeMapIn, int amplifier) {
        super.addAttributeModifiers(entity, attributeMapIn, amplifier);
        if (entity instanceof Player player) {
            if (player.getAbilities().mayfly && !player.isCreative()) {
                player.getPersistentData().putBoolean("ma_entangle_did_allow_flight", true);
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, false);
            } else {
                player.getPersistentData().putBoolean("ma_entangle_did_allow_flight", false);
            }
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entity, attributeMapIn, amplifier);
        if (entity instanceof Player player) {
            if (player.getPersistentData().getBoolean("ma_entangle_did_allow_flight")) {
                ManaAndArtifice.instance.proxy.setFlightEnabled(player, true);
            }
            player.getPersistentData().remove("ma_entangle_did_allow_flight");
        }
    }
}