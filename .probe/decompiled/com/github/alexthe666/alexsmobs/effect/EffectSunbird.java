package com.github.alexthe666.alexsmobs.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;

public class EffectSunbird extends MobEffect {

    public final boolean curse;

    public EffectSunbird(boolean curse) {
        super(curse ? MobEffectCategory.HARMFUL : MobEffectCategory.BENEFICIAL, 16771769);
        this.curse = curse;
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (this.curse) {
            if (entity.isFallFlying() && entity instanceof Player) {
                ((Player) entity).stopFallFlying();
            }
            boolean forceFall = false;
            if (entity instanceof Player player && (!player.isCreative() || !player.getAbilities().flying)) {
                forceFall = true;
            }
            if ((forceFall || !(entity instanceof Player)) && !entity.m_20096_()) {
                entity.m_20256_(entity.m_20184_().add(0.0, -0.2F, 0.0));
            }
        } else {
            entity.f_19789_ = 0.0F;
            if (entity.isFallFlying()) {
                if (entity.m_146909_() < -10.0F) {
                    float pitchMulti = Math.abs(entity.m_146909_()) / 90.0F;
                    entity.m_20256_(entity.m_20184_().add(0.0, 0.02 + (double) pitchMulti * 0.02, 0.0));
                }
            } else if (!entity.m_20096_() && !entity.m_6047_()) {
                Vec3 vector3d = entity.m_20184_();
                if (vector3d.y < 0.0) {
                    entity.m_20256_(vector3d.multiply(1.0, 0.6, 1.0));
                }
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration > 0;
    }

    @Override
    public String getDescriptionId() {
        return this.curse ? "alexsmobs.potion.sunbird_curse" : "alexsmobs.potion.sunbird_blessing";
    }
}