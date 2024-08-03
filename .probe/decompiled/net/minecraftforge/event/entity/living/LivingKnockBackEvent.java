package net.minecraftforge.event.entity.living;

import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.eventbus.api.Cancelable;

@Cancelable
public class LivingKnockBackEvent extends LivingEvent {

    protected float strength;

    protected double ratioX;

    protected double ratioZ;

    protected final float originalStrength;

    protected final double originalRatioX;

    protected final double originalRatioZ;

    public LivingKnockBackEvent(LivingEntity target, float strength, double ratioX, double ratioZ) {
        super(target);
        this.strength = this.originalStrength = strength;
        this.ratioX = this.originalRatioX = ratioX;
        this.ratioZ = this.originalRatioZ = ratioZ;
    }

    public float getStrength() {
        return this.strength;
    }

    public double getRatioX() {
        return this.ratioX;
    }

    public double getRatioZ() {
        return this.ratioZ;
    }

    public float getOriginalStrength() {
        return this.originalStrength;
    }

    public double getOriginalRatioX() {
        return this.originalRatioX;
    }

    public double getOriginalRatioZ() {
        return this.originalRatioZ;
    }

    public void setStrength(float strength) {
        this.strength = strength;
    }

    public void setRatioX(double ratioX) {
        this.ratioX = ratioX;
    }

    public void setRatioZ(double ratioZ) {
        this.ratioZ = ratioZ;
    }
}