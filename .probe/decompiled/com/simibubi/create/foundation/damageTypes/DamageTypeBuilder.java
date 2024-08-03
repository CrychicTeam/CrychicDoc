package com.simibubi.create.foundation.damageTypes;

import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class DamageTypeBuilder {

    protected final ResourceKey<DamageType> key;

    protected String msgId;

    protected DamageScaling scaling;

    protected float exhaustion = 0.0F;

    protected DamageEffects effects;

    protected DeathMessageType deathMessageType;

    public DamageTypeBuilder(ResourceKey<DamageType> key) {
        this.key = key;
    }

    public DamageTypeBuilder msgId(String msgId) {
        this.msgId = msgId;
        return this;
    }

    public DamageTypeBuilder simpleMsgId() {
        return this.msgId(this.key.location().getNamespace() + "." + this.key.location().getPath());
    }

    public DamageTypeBuilder scaling(DamageScaling scaling) {
        this.scaling = scaling;
        return this;
    }

    public DamageTypeBuilder exhaustion(float exhaustion) {
        this.exhaustion = exhaustion;
        return this;
    }

    public DamageTypeBuilder effects(DamageEffects effects) {
        this.effects = effects;
        return this;
    }

    public DamageTypeBuilder deathMessageType(DeathMessageType deathMessageType) {
        this.deathMessageType = deathMessageType;
        return this;
    }

    public DamageType build() {
        if (this.msgId == null) {
            this.simpleMsgId();
        }
        if (this.scaling == null) {
            this.scaling(DamageScaling.WHEN_CAUSED_BY_LIVING_NON_PLAYER);
        }
        if (this.effects == null) {
            this.effects(DamageEffects.HURT);
        }
        if (this.deathMessageType == null) {
            this.deathMessageType(DeathMessageType.DEFAULT);
        }
        return new DamageType(this.msgId, this.scaling, this.exhaustion, this.effects, this.deathMessageType);
    }

    public DamageType register(BootstapContext<DamageType> ctx) {
        DamageType type = this.build();
        ctx.register(this.key, type);
        return type;
    }
}