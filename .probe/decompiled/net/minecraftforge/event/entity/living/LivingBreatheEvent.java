package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.ApiStatus.Internal;

public class LivingBreatheEvent extends LivingEvent {

    private boolean canBreathe;

    private boolean canRefillAir;

    private int consumeAirAmount;

    private int refillAirAmount;

    @Deprecated(forRemoval = true, since = "1.20.1")
    @Internal
    public LivingBreatheEvent(LivingEntity entity, boolean canBreathe, int consumeAirAmount, int refillAirAmount) {
        this(entity, canBreathe, consumeAirAmount, refillAirAmount, canBreathe);
    }

    @Internal
    public LivingBreatheEvent(LivingEntity entity, boolean canBreathe, int consumeAirAmount, int refillAirAmount, boolean canRefillAir) {
        super(entity);
        this.canBreathe = canBreathe;
        this.canRefillAir = canRefillAir;
        this.consumeAirAmount = Math.max(consumeAirAmount, 0);
        this.refillAirAmount = Math.max(refillAirAmount, 0);
    }

    public boolean canBreathe() {
        return this.canBreathe;
    }

    public void setCanBreathe(boolean canBreathe) {
        this.canBreathe = canBreathe;
    }

    public boolean canRefillAir() {
        return this.canRefillAir;
    }

    public void setCanRefillAir(boolean canRefillAir) {
        this.canRefillAir = canRefillAir;
    }

    public int getConsumeAirAmount() {
        return this.consumeAirAmount;
    }

    public void setConsumeAirAmount(int consumeAirAmount) {
        this.consumeAirAmount = Math.max(consumeAirAmount, 0);
    }

    public int getRefillAirAmount() {
        return this.refillAirAmount;
    }

    public void setRefillAirAmount(int refillAirAmount) {
        this.refillAirAmount = Math.max(refillAirAmount, 0);
    }
}