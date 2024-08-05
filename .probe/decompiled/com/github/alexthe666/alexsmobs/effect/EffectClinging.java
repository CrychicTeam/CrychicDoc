package com.github.alexthe666.alexsmobs.effect;

import com.github.alexthe666.alexsmobs.misc.AMBlockPos;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.level.block.state.BlockState;

public class EffectClinging extends MobEffect {

    public EffectClinging() {
        super(MobEffectCategory.BENEFICIAL, 12405579);
    }

    private static BlockPos getPositionUnderneath(Entity e) {
        return AMBlockPos.fromCoords(e.getX(), e.getBoundingBox().maxY + 1.51F, e.getZ());
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        entity.m_6210_();
        entity.m_20242_(false);
        if (isUpsideDown(entity)) {
            entity.f_19789_ = 0.0F;
            if (!entity.m_6144_()) {
                if (!entity.f_19862_) {
                    entity.m_20256_(entity.m_20184_().add(0.0, 0.3F, 0.0));
                }
                entity.m_20256_(entity.m_20184_().multiply(0.998F, 1.0, 0.998F));
            }
        }
    }

    public static boolean isUpsideDown(LivingEntity entity) {
        BlockPos pos = getPositionUnderneath(entity);
        BlockState ground = entity.m_9236_().getBlockState(pos);
        return (entity.f_19863_ || ground.m_60783_(entity.m_9236_(), pos, Direction.DOWN)) && !entity.m_20096_();
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entityLivingBaseIn, AttributeMap attributeMapIn, int amplifier) {
        super.removeAttributeModifiers(entityLivingBaseIn, attributeMapIn, amplifier);
        entityLivingBaseIn.m_6210_();
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return "alexsmobs.potion.clinging";
    }
}