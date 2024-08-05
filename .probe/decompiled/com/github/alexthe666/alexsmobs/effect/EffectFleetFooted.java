package com.github.alexthe666.alexsmobs.effect;

import java.util.UUID;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

public class EffectFleetFooted extends MobEffect {

    private static final UUID SPRINT_JUMP_SPEED_MODIFIER = UUID.fromString("7E0292F2-9434-48D5-A29F-9583AF7DF29A");

    private static final AttributeModifier SPRINT_JUMP_SPEED_BONUS = new AttributeModifier(SPRINT_JUMP_SPEED_MODIFIER, "fleetfooted speed bonus", 0.2F, AttributeModifier.Operation.ADDITION);

    private int lastDuration = -1;

    private int removeEffectAfter = 0;

    public EffectFleetFooted() {
        super(MobEffectCategory.BENEFICIAL, 6837313);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        AttributeInstance modifiableattributeinstance = entity.getAttribute(Attributes.MOVEMENT_SPEED);
        boolean applyEffect = entity.m_20142_() && !entity.m_20096_() && this.lastDuration > 2;
        if (this.removeEffectAfter > 0) {
            this.removeEffectAfter--;
        }
        if (applyEffect) {
            if (!modifiableattributeinstance.hasModifier(SPRINT_JUMP_SPEED_BONUS)) {
                modifiableattributeinstance.addPermanentModifier(SPRINT_JUMP_SPEED_BONUS);
            }
            this.removeEffectAfter = 5;
        }
        if (this.removeEffectAfter <= 0 || this.lastDuration < 2) {
            modifiableattributeinstance.removeModifier(SPRINT_JUMP_SPEED_BONUS);
        }
    }

    @Override
    public void removeAttributeModifiers(LivingEntity livingEntity, AttributeMap attributeMap, int level) {
        AttributeInstance modifiableattributeinstance = livingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if (modifiableattributeinstance != null && modifiableattributeinstance.hasModifier(SPRINT_JUMP_SPEED_BONUS)) {
            modifiableattributeinstance.removeModifier(SPRINT_JUMP_SPEED_BONUS);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        this.lastDuration = duration;
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.fleet_footed";
    }
}