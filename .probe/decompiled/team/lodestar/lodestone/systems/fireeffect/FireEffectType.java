package team.lodestar.lodestone.systems.fireeffect;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.Entity;

public class FireEffectType {

    public final String id;

    protected final int damage;

    protected final int tickInterval;

    public FireEffectType(String id, int damage, int tickInterval) {
        this.id = id;
        this.damage = damage;
        this.tickInterval = tickInterval;
    }

    public int getDamage(FireEffectInstance instance) {
        return this.damage;
    }

    public int getTickInterval(FireEffectInstance instance) {
        return this.tickInterval;
    }

    public void extinguish(FireEffectInstance instance, Entity target) {
        instance.duration = 0;
        target.level().playSound(null, target.blockPosition(), SoundEvents.GENERIC_EXTINGUISH_FIRE, target.getSoundSource(), 0.7F, 1.6F + (target.level().getRandom().nextFloat() - target.level().getRandom().nextFloat()) * 0.4F);
    }

    public void tick(FireEffectInstance instance, Entity target) {
        target.hurt(target.level().damageSources().onFire(), (float) this.getDamage(instance));
    }

    public boolean isValid(FireEffectInstance instance) {
        return instance.duration > 0;
    }
}