package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingHealEvent extends LivingEvent {

    private float amount;

    public LivingHealEvent(LivingEntity entity, float amount) {
        super(entity);
        this.setAmount(amount);
    }

    public float getAmount() {
        return this.amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}