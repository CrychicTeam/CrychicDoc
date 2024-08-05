package net.minecraftforge.event.entity.player;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.eventbus.api.Event.HasResult;

@HasResult
public class CriticalHitEvent extends PlayerEvent {

    private float damageModifier;

    private final float oldDamageModifier;

    private final Entity target;

    private final boolean vanillaCritical;

    public CriticalHitEvent(Player player, Entity target, float damageModifier, boolean vanillaCritical) {
        super(player);
        this.target = target;
        this.damageModifier = damageModifier;
        this.oldDamageModifier = damageModifier;
        this.vanillaCritical = vanillaCritical;
    }

    public Entity getTarget() {
        return this.target;
    }

    public void setDamageModifier(float mod) {
        this.damageModifier = mod;
    }

    public float getDamageModifier() {
        return this.damageModifier;
    }

    public float getOldDamageModifier() {
        return this.oldDamageModifier;
    }

    public boolean isVanillaCritical() {
        return this.vanillaCritical;
    }
}