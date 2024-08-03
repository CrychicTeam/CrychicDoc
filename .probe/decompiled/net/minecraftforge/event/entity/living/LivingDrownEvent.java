package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;
import org.jetbrains.annotations.ApiStatus.Internal;

@Cancelable
public class LivingDrownEvent extends LivingEvent {

    private boolean isDrowning;

    private float damageAmount;

    private int bubbleCount;

    @Internal
    public LivingDrownEvent(LivingEntity entity, boolean isDrowning, float damageAmount, int bubbleCount) {
        super(entity);
        this.isDrowning = isDrowning;
        this.damageAmount = damageAmount;
        this.bubbleCount = bubbleCount;
    }

    @Deprecated(forRemoval = true, since = "1.20.1")
    @Internal
    public LivingDrownEvent(LivingEntity entity, boolean isDrowning) {
        this(entity, isDrowning, 2.0F, 8);
    }

    public boolean isDrowning() {
        return this.isDrowning;
    }

    public void setDrowning(boolean isDrowning) {
        this.isDrowning = isDrowning;
    }

    public float getDamageAmount() {
        return this.damageAmount;
    }

    public void setDamageAmount(float damageAmount) {
        this.damageAmount = damageAmount;
    }

    public int getBubbleCount() {
        return this.bubbleCount;
    }

    public void setBubbleCount(int bubbleCount) {
        this.bubbleCount = bubbleCount;
    }

    public void setCanceled(boolean cancel) {
        super.setCanceled(cancel);
    }
}