package com.mna.effects.neutral;

import com.mna.effects.EffectInit;
import net.minecraft.network.protocol.game.ClientboundRemoveMobEffectPacket;
import net.minecraft.network.protocol.game.ClientboundUpdateMobEffectPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.PacketDistributor;

public class EffectGrow extends SimpleNeutralEffect {

    public EffectGrow() {
        this.m_19472_(Attributes.MAX_HEALTH, "264a6b95-63ed-47f2-b7cd-47f13b2d1e53", 0.05F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(Attributes.MOVEMENT_SPEED, "ac600a2b-ec4c-49cc-8a85-3331f5799227", 0.0069375, AttributeModifier.Operation.ADDITION);
        this.m_19472_(Attributes.KNOCKBACK_RESISTANCE, "a9a77b3e-ec50-4813-be2a-204ace0f2fec", 0.111F, AttributeModifier.Operation.ADDITION);
        this.m_19472_(Attributes.ATTACK_KNOCKBACK, "189f5b92-0bdf-4b63-bc08-a50e85f24c57", 0.111F, AttributeModifier.Operation.MULTIPLY_TOTAL);
        this.m_19472_(ForgeMod.BLOCK_REACH.get(), "b37cdf69-27ea-488a-b012-ef21359fcc90", 0.222F, AttributeModifier.Operation.ADDITION);
        this.m_19472_(ForgeMod.ENTITY_REACH.get(), "29721638-2e59-479f-b9f6-d79665240142", 0.222F, AttributeModifier.Operation.ADDITION);
        this.m_19472_(ForgeMod.STEP_HEIGHT_ADDITION.get(), "60288c04-9c19-4fd7-802f-b70cb55ed220", 0.111F, AttributeModifier.Operation.ADDITION);
    }

    @Override
    public void addAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        MobEffectInstance mobEffectInstance = pLivingEntity.getEffect(EffectInit.ENLARGE.get());
        PacketDistributor.TRACKING_ENTITY.with(() -> pLivingEntity).send(new ClientboundUpdateMobEffectPacket(pLivingEntity.m_19879_(), mobEffectInstance));
        float healthPct = pLivingEntity.getHealth() / pLivingEntity.getMaxHealth();
        super.m_6385_(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth() * healthPct);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {
        PacketDistributor.TRACKING_ENTITY.with(() -> pLivingEntity).send(new ClientboundRemoveMobEffectPacket(pLivingEntity.m_19879_(), EffectInit.ENLARGE.get()));
        float healthPct = pLivingEntity.getHealth() / pLivingEntity.getMaxHealth();
        super.m_6386_(pLivingEntity, pAttributeMap, pAmplifier);
        pLivingEntity.setHealth(pLivingEntity.getMaxHealth() * healthPct);
    }
}