package com.github.alexmodguy.alexscaves.server.potion;

import java.util.UUID;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;

public class RageEffect extends MobEffect {

    private static final UUID RAGE_ATTACK_DAMAGE_UUID = UUID.fromString("1eaf83ff-7207-4596-b37a-d7a07b3ec4ff");

    protected RageEffect() {
        super(MobEffectCategory.NEUTRAL, 12201518);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int level) {
        AttributeInstance attributeinstance = entity.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attributeinstance != null) {
            float levelScale = (float) (1 + level) * 2.5F;
            float f = (1.0F - entity.getHealth() / entity.getMaxHealth()) * levelScale;
            this.removeRageModifier(entity);
            attributeinstance.addTransientModifier(new AttributeModifier(RAGE_ATTACK_DAMAGE_UUID, "Rage attack boost", (double) f, AttributeModifier.Operation.ADDITION));
        }
        if (!entity.m_9236_().isClientSide && entity instanceof Mob mob && mob.getTarget() == null && entity.f_19797_ % 10 == 0 && entity.getRandom().nextInt(2) == 0) {
            AABB aabb = mob.m_20191_().inflate(80.0);
            LivingEntity randomTarget = null;
            for (LivingEntity living : mob.m_9236_().m_6443_(LivingEntity.class, aabb, EntitySelector.LIVING_ENTITY_STILL_ALIVE)) {
                if ((randomTarget == null || randomTarget.m_20270_(mob) > living.m_20270_(mob) && mob.m_217043_().nextInt(2) == 0) && !mob.m_7306_(living) && !mob.m_7307_(living) && !living.m_7307_(mob) && mob.m_6779_(living)) {
                    randomTarget = living;
                }
            }
            if (randomTarget != null && !randomTarget.m_7306_(mob)) {
                mob.m_6703_(randomTarget);
                mob.setTarget(randomTarget);
                for (int i = 0; i < 3 + mob.m_217043_().nextInt(3); i++) {
                    ((ServerLevel) entity.m_9236_()).sendParticles(ParticleTypes.ANGRY_VILLAGER, mob.m_20208_(0.5), mob.m_20188_() + (double) (mob.m_217043_().nextFloat() * 0.2F), mob.m_20262_(0.5), 0, 0.0, 0.0, 0.0, 1.0);
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    protected void removeRageModifier(LivingEntity living) {
        AttributeInstance attributeinstance = living.getAttribute(Attributes.ATTACK_DAMAGE);
        if (attributeinstance != null && attributeinstance.getModifier(RAGE_ATTACK_DAMAGE_UUID) != null) {
            attributeinstance.removeModifier(RAGE_ATTACK_DAMAGE_UUID);
        }
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        super.addAttributeModifiers(entity, map, i);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int i) {
        super.removeAttributeModifiers(entity, map, i);
        this.removeRageModifier(entity);
    }
}