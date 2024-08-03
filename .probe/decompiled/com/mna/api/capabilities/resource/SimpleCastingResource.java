package com.mna.api.capabilities.resource;

import java.util.HashMap;
import net.minecraft.world.entity.LivingEntity;

public abstract class SimpleCastingResource implements ICastingResource {

    protected int ticks_for_regeneration;

    protected float amount;

    protected float maximum_baseline;

    protected SyncStatus sync_status;

    protected HashMap<String, Float> maximumModifiers = new HashMap();

    protected HashMap<String, Float> regenerationModifiers = new HashMap();

    public SimpleCastingResource(int ticks_for_regeneration) {
        this.ticks_for_regeneration = ticks_for_regeneration;
    }

    @Override
    public SyncStatus getSyncStatus() {
        return this.sync_status;
    }

    @Override
    public void clearSyncStatus() {
        this.sync_status = SyncStatus.NOT_NEEDED;
    }

    @Override
    public void setNeedsSync() {
        this.sync_status = SyncStatus.IMMEDIATE;
    }

    @Override
    public float getAmount() {
        return this.amount;
    }

    @Override
    public void setAmount(float amount) {
        if (this.amount != amount) {
            this.amount = amount;
            if (this.amount > this.getMaxAmount()) {
                this.amount = this.getMaxAmount();
            } else if (this.amount < 0.0F) {
                this.amount = 0.0F;
            }
            if (this.sync_status == SyncStatus.NOT_NEEDED) {
                this.sync_status = SyncStatus.LAZY;
            }
        }
    }

    @Override
    public void consume(LivingEntity caster, float amount) {
        this.setAmount(this.getAmount() - amount);
    }

    @Override
    public boolean hasEnough(LivingEntity caster, float amount) {
        return this.hasEnoughAbsolute(caster, amount);
    }

    @Override
    public boolean hasEnoughAbsolute(LivingEntity caster, float amount) {
        return this.getAmount() >= amount;
    }

    @Override
    public void restore(float amount) {
        this.setAmount(this.getAmount() + amount);
    }

    @Override
    public float getMaxAmount() {
        float calculatedAmount = this.getMaxAmountBaseline();
        for (Float f : this.maximumModifiers.values()) {
            calculatedAmount += f;
        }
        return calculatedAmount;
    }

    @Override
    public float getMaxAmountBaseline() {
        return this.maximum_baseline;
    }

    @Override
    public void setMaxAmount(float amount) {
        if (this.maximum_baseline != amount) {
            this.maximum_baseline = amount;
            if (this.maximum_baseline < 0.0F) {
                this.maximum_baseline = 0.0F;
            }
            if (this.amount > this.getMaxAmount()) {
                this.amount = this.getMaxAmount();
            }
            this.sync_status = SyncStatus.IMMEDIATE;
        }
    }

    @Override
    public void addModifier(String id, float amount) {
        if (!this.maximumModifiers.containsKey(id) || (Float) this.maximumModifiers.get(id) != amount) {
            this.maximumModifiers.put(id, amount);
            this.sync_status = SyncStatus.IMMEDIATE;
        }
    }

    @Override
    public HashMap<String, Float> getModifiers() {
        return this.maximumModifiers;
    }

    @Override
    public void removeModifier(String id) {
        if (this.maximumModifiers.containsKey(id)) {
            this.maximumModifiers.remove(id);
        }
        this.sync_status = SyncStatus.IMMEDIATE;
    }

    @Override
    public void clearModifiers() {
        this.maximumModifiers.clear();
    }

    @Override
    public int getRegenerationRate(LivingEntity caster) {
        return (int) ((float) this.ticks_for_regeneration * this.getRegenerationModifier(caster));
    }

    @Override
    public void setRegenerationRate(int ticksForFullRegen) {
        this.ticks_for_regeneration = ticksForFullRegen;
    }

    @Override
    public void addRegenerationModifier(String id, float pct) {
        if (!this.regenerationModifiers.containsKey(id) || (Float) this.regenerationModifiers.get(id) != pct) {
            this.regenerationModifiers.put(id, pct);
            this.sync_status = SyncStatus.IMMEDIATE;
        }
    }

    @Override
    public HashMap<String, Float> getRegenerationModifiers() {
        return this.regenerationModifiers;
    }

    @Override
    public void removeRegenerationModifier(String id) {
        if (this.regenerationModifiers.containsKey(id)) {
            this.regenerationModifiers.remove(id);
        }
        this.sync_status = SyncStatus.IMMEDIATE;
    }

    @Override
    public void clearRegenerationModifiers() {
        this.regenerationModifiers.clear();
    }

    @Override
    public float getRegenerationModifier(LivingEntity caster) {
        float mod = 1.0F;
        for (float value : this.regenerationModifiers.values()) {
            mod += mod * value;
        }
        return mod;
    }
}