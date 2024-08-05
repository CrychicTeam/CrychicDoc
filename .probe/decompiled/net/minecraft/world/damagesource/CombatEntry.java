package net.minecraft.world.damagesource;

import javax.annotation.Nullable;

public record CombatEntry(DamageSource f_19250_, float f_19252_, @Nullable FallLocation f_289042_, float f_19255_) {

    private final DamageSource source;

    private final float damage;

    @Nullable
    private final FallLocation fallLocation;

    private final float fallDistance;

    public CombatEntry(DamageSource f_19250_, float f_19252_, @Nullable FallLocation f_289042_, float f_19255_) {
        this.source = f_19250_;
        this.damage = f_19252_;
        this.fallLocation = f_289042_;
        this.fallDistance = f_19255_;
    }
}