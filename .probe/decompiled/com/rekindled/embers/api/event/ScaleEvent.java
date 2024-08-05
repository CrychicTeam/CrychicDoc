package com.rekindled.embers.api.event;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Event;

public class ScaleEvent extends Event {

    private LivingEntity entity;

    private double scalePassRate = 0.0;

    private double scaleDamageRate = 1.0;

    private float damage;

    private DamageSource source;

    public ScaleEvent(LivingEntity entity, float damage, DamageSource source, double scaleDamageRate, double scalePassRate) {
        this.entity = entity;
        this.scalePassRate = scalePassRate;
        this.scaleDamageRate = scaleDamageRate;
        this.damage = damage;
        this.source = source;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public double getDamage() {
        return (double) this.damage;
    }

    public DamageSource getDamageSource() {
        return this.source;
    }

    public double getScalePassRate() {
        return this.scalePassRate;
    }

    public void setScalePassRate(double scalePassRate) {
        this.scalePassRate = scalePassRate;
    }

    public double getScaleDamageRate() {
        return this.scaleDamageRate;
    }

    public void setScaleDamageRate(double scaleDamageRate) {
        this.scaleDamageRate = scaleDamageRate;
    }
}