package dev.xkmc.l2complements.content.entity.fireball;

import dev.xkmc.l2complements.init.registrate.LCEffects;
import dev.xkmc.l2complements.init.registrate.LCEntities;
import dev.xkmc.l2library.base.effects.EffectUtil;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

public class SoulFireball extends BaseFireball<SoulFireball> {

    public SoulFireball(EntityType<SoulFireball> type, Level level) {
        super(type, level);
    }

    public SoulFireball(double x, double y, double z, double vx, double vy, double vz, Level level) {
        super((EntityType<SoulFireball>) LCEntities.ETFB_SOUL.get(), x, y, z, vx, vy, vz, level);
    }

    public SoulFireball(LivingEntity owner, double vx, double vy, double vz, Level level) {
        super((EntityType<SoulFireball>) LCEntities.ETFB_SOUL.get(), owner, vx, vy, vz, level);
    }

    @Override
    protected void onHitEntity(Entity target) {
        if (target instanceof LivingEntity le) {
            EffectUtil.addEffect(le, new MobEffectInstance((MobEffect) LCEffects.FLAME.get(), 60), EffectUtil.AddReason.NONE, this.m_19749_());
        }
        target.hurt(this.m_9236_().damageSources().fireball(this, this.m_19749_()), 6.0F);
    }
}