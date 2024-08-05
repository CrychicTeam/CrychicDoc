package dev.shadowsoffire.attributeslib.mobfx;

import dev.shadowsoffire.attributeslib.api.ALObjects;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.AABB;

public class DetonationEffect extends MobEffect {

    public DetonationEffect() {
        super(MobEffectCategory.HARMFUL, 16766976);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap map, int amp) {
        super.removeAttributeModifiers(entity, map, amp);
        int ticks = entity.m_20094_();
        if (ticks > 0) {
            entity.m_7311_(0);
            entity.hurt(entity.m_9236_().damageSources().source(ALObjects.DamageTypes.BLEEDING), (float) ((1 + amp) * ticks) / 14.0F);
            ServerLevel level = (ServerLevel) entity.m_9236_();
            AABB bb = entity.m_20191_();
            level.sendParticles(ParticleTypes.FLAME, entity.m_20185_(), entity.m_20186_(), entity.m_20189_(), 100, bb.getXsize(), bb.getYsize(), bb.getZsize(), 0.25);
            level.m_6269_(null, entity, SoundEvents.DRAGON_FIREBALL_EXPLODE, SoundSource.HOSTILE, 1.0F, 1.2F);
        }
    }
}