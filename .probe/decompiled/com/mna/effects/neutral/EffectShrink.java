package com.mna.effects.neutral;

import com.mna.effects.EffectInit;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.network.PacketDistributor;

public class EffectShrink extends SimpleNeutralEffect {

    public EffectShrink() {
        this.m_19472_(Attributes.MAX_HEALTH, "7a3200de-96c6-41c6-80a4-c9a5493b1bed", -0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        MobEffectInstance mobEffectInstance = pLivingEntity.getEffect(EffectInit.REDUCE.get());
        PacketDistributor.TRACKING_ENTITY.with(() -> pLivingEntity).send(new ClientboundUpdateMobEffectPacket(pLivingEntity.m_19879_(), mobEffectInstance));
        float healthPct = pLivingEntity.getHealth() / pLivingEntity.getMaxHealth();
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth() * healthPct);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        PacketDistributor.TRACKING_ENTITY.with(() -> pLivingEntity).send(new ClientboundRemoveMobEffectPacket(pLivingEntity.m_19879_(), EffectInit.REDUCE.get()));
        float healthPct = pLivingEntity.getHealth() / pLivingEntity.getMaxHealth();
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth() * healthPct);
    }
}