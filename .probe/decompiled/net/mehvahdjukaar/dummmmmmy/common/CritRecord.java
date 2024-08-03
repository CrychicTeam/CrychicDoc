package net.mehvahdjukaar.dummmmmmy.common;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;

public class CritRecord {

    private final Entity critter;

    private final float multiplier;

    private DamageSource source;

    public CritRecord(Entity critter, float mult) {
        this.critter = critter;
        this.multiplier = mult;
    }

    public void addSource(DamageSource source) {
        this.source = source;
    }

    public boolean canCompleteWith(DamageSource source) {
        return source != null && (source.getEntity() == this.critter || source.getDirectEntity() == this.critter);
    }

    public boolean matches(DamageSource source) {
        return this.source == source;
    }

    public float getMultiplier() {
        return this.multiplier;
    }
}