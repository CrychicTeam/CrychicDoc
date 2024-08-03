package net.minecraftforge.event.entity.living;

import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class ShieldBlockEvent extends LivingEvent {

    private final DamageSource source;

    private final float originalBlocked;

    private float dmgBlocked;

    private boolean shieldTakesDamage = true;

    public ShieldBlockEvent(LivingEntity blocker, DamageSource source, float blocked) {
        super(blocker);
        this.source = source;
        this.originalBlocked = blocked;
        this.dmgBlocked = blocked;
    }

    public DamageSource getDamageSource() {
        return this.source;
    }

    public float getOriginalBlockedDamage() {
        return this.originalBlocked;
    }

    public float getBlockedDamage() {
        return this.dmgBlocked;
    }

    public boolean shieldTakesDamage() {
        return this.shieldTakesDamage;
    }

    public void setBlockedDamage(float blocked) {
        this.dmgBlocked = Mth.clamp(blocked, 0.0F, this.originalBlocked);
    }

    public void setShieldTakesDamage(boolean damage) {
        this.shieldTakesDamage = damage;
    }
}