package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingFallEvent extends LivingEvent {

    private float distance;

    private float damageMultiplier;

    public LivingFallEvent(LivingEntity entity, float distance, float damageMultiplier) {
        super(entity);
        this.setDistance(distance);
        this.setDamageMultiplier(damageMultiplier);
    }

    public float getDistance() {
        return this.distance;
    }

    public void setDistance(float distance) {
        this.distance = distance;
    }

    public float getDamageMultiplier() {
        return this.damageMultiplier;
    }

    public void setDamageMultiplier(float damageMultiplier) {
        this.damageMultiplier = damageMultiplier;
    }
}