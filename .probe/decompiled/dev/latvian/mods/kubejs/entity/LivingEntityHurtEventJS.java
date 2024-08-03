package dev.latvian.mods.kubejs.entity;

import dev.latvian.mods.kubejs.typings.Info;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;

@Info("Invoked before an entity is hurt by a damage source.\n")
public class LivingEntityHurtEventJS extends LivingEntityEventJS {

    private final LivingEntity entity;

    private final DamageSource source;

    private final float amount;

    public LivingEntityHurtEventJS(LivingEntity entity, DamageSource source, float amount) {
        this.entity = entity;
        this.source = source;
        this.amount = amount;
    }

    @Info("The entity that was hurt.")
    @Override
    public LivingEntity getEntity() {
        return this.entity;
    }

    @Info("The damage source.")
    public DamageSource getSource() {
        return this.source;
    }

    @Info("The amount of damage.")
    public float getDamage() {
        return this.amount;
    }
}