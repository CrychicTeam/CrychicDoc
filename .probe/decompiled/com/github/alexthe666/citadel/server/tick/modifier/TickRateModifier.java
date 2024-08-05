package com.github.alexthe666.citadel.server.tick.modifier;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;

public abstract class TickRateModifier {

    private TickRateModifierType type;

    private float maxDuration;

    private float duration;

    private float tickRateMultiplier;

    public TickRateModifier(TickRateModifierType type, int maxDuration, float tickRateMultiplier) {
        this.type = type;
        this.maxDuration = (float) maxDuration;
        this.tickRateMultiplier = tickRateMultiplier;
    }

    public TickRateModifier(CompoundTag tag) {
        this.type = TickRateModifierType.fromId(tag.getInt("TickRateType"));
        this.maxDuration = tag.getFloat("MaxDuration");
        this.duration = tag.getFloat("Duration");
        this.tickRateMultiplier = tag.getFloat("SpeedMultiplier");
    }

    public TickRateModifierType getType() {
        return this.type;
    }

    public float getMaxDuration() {
        return this.maxDuration;
    }

    public float getTickRateMultiplier() {
        return this.tickRateMultiplier;
    }

    public void setMaxDuration(float maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setTickRateMultiplier(float tickRateMultiplier) {
        this.tickRateMultiplier = tickRateMultiplier;
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        tag.putInt("TickRateType", this.type.toId());
        tag.putFloat("MaxDuration", this.maxDuration);
        tag.putFloat("Duration", this.duration);
        tag.putFloat("SpeedMultiplier", this.tickRateMultiplier);
        return tag;
    }

    public static TickRateModifier fromTag(CompoundTag tag) {
        TickRateModifierType typeFromNbt = TickRateModifierType.fromId(tag.getInt("TickRateType"));
        try {
            return (TickRateModifier) typeFromNbt.getTickRateClass().getConstructor(CompoundTag.class).newInstance(tag);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public boolean isGlobal() {
        return this.type.isLocal();
    }

    public void masterTick() {
        this.duration++;
    }

    public boolean doRemove() {
        float f = this.tickRateMultiplier != 0.0F && this.getType() != TickRateModifierType.CELESTIAL ? 1.0F / this.tickRateMultiplier : 1.0F;
        return this.duration >= this.maxDuration * f;
    }

    public abstract boolean appliesTo(Level var1, double var2, double var4, double var6);
}