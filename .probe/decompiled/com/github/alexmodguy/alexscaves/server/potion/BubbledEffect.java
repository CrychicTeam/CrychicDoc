package com.github.alexmodguy.alexscaves.server.potion;

import com.github.alexmodguy.alexscaves.server.misc.ACTagRegistry;
import java.util.List;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectUtil;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec3;

public class BubbledEffect extends MobEffect {

    protected BubbledEffect() {
        super(MobEffectCategory.HARMFUL, 2209279);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int tick) {
        if (!entity.canBreatheUnderwater() && entity.getMobType() != MobType.WATER) {
            if (!MobEffectUtil.hasWaterBreathing(entity)) {
                if (entity instanceof Player player && player.getAbilities().invulnerable) {
                    return;
                }
                entity.m_20256_(entity.m_20184_().multiply(0.8F, 1.0, 0.8F));
                entity.m_20301_(Math.max(entity.m_20146_() - 2, -20));
                if (entity.m_20146_() <= -20) {
                    entity.m_20301_(0);
                    Vec3 vec3 = entity.m_20184_();
                    for (int i = 0; i < 8; i++) {
                        double d2 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        double d3 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        double d4 = entity.getRandom().nextDouble() - entity.getRandom().nextDouble();
                        entity.m_9236_().addParticle(ParticleTypes.BUBBLE, entity.m_20185_() + d2, entity.m_20186_() + d3, entity.m_20189_() + d4, vec3.x, vec3.y, vec3.z);
                    }
                    entity.hurt(entity.m_269291_().drown(), 2.0F);
                }
            }
        } else if (!entity.m_6095_().is(ACTagRegistry.RESISTS_BUBBLED)) {
            entity.m_20301_(entity.m_6062_());
            if (!entity.m_20096_()) {
                entity.m_20256_(entity.m_20184_().add(0.0, -0.08, 0.0));
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int i, int j) {
        return true;
    }

    public List<ItemStack> getCurativeItems() {
        return List.of();
    }
}