package com.github.alexthe666.citadel.server.tick;

import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifier;
import com.github.alexthe666.citadel.server.tick.modifier.TickRateModifierType;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.entity.Entity;

public abstract class TickRateTracker {

    public List<TickRateModifier> tickRateModifierList = new ArrayList();

    public List<Entity> specialTickRateEntities = new ArrayList();

    private long masterTickCount;

    public void masterTick() {
        this.masterTickCount++;
        this.specialTickRateEntities.forEach(this::tickBlockedEntity);
        this.specialTickRateEntities.removeIf(this::hasNormalTickRate);
        for (TickRateModifier modifier : this.tickRateModifierList) {
            modifier.masterTick();
        }
        if (!this.tickRateModifierList.isEmpty() && this.tickRateModifierList.removeIf(TickRateModifier::doRemove)) {
            this.sync();
        }
    }

    protected void sync() {
    }

    public boolean hasModifiersActive() {
        return !this.tickRateModifierList.isEmpty();
    }

    public CompoundTag toTag() {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (!modifier.doRemove()) {
                list.add(modifier.toTag());
            }
        }
        tag.put("TickRateModifiers", list);
        return tag;
    }

    public void fromTag(CompoundTag tag) {
        if (tag.contains("TickRateModifiers")) {
            ListTag list = tag.getList("TickRateModifiers", 10);
            for (int i = 0; i < list.size(); i++) {
                CompoundTag tag1 = list.getCompound(i);
                TickRateModifier modifier = TickRateModifier.fromTag(tag1);
                if (!modifier.doRemove()) {
                    this.tickRateModifierList.add(modifier);
                }
            }
        }
    }

    public long getDayTimeIncrement(long timeIn) {
        float f = 1.0F;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType() == TickRateModifierType.CELESTIAL) {
                f *= modifier.getTickRateMultiplier();
            }
        }
        if (f < 1.0F && f > 0.0F) {
            int inverse = (int) (1.0F / f);
            return this.masterTickCount % (long) inverse == 0L ? timeIn : 0L;
        } else {
            return (long) ((float) timeIn * f);
        }
    }

    public float getEntityTickLengthModifier(Entity entity) {
        float f = 1.0F;
        for (TickRateModifier modifier : this.tickRateModifierList) {
            if (modifier.getType().isLocal() && modifier.appliesTo(entity.level(), entity.getX(), entity.getY(), entity.getZ())) {
                f *= modifier.getTickRateMultiplier();
            }
        }
        return f;
    }

    public boolean hasNormalTickRate(Entity entity) {
        return this.getEntityTickLengthModifier(entity) == 1.0F;
    }

    public boolean isTickingHandled(Entity entity) {
        return this.specialTickRateEntities.contains(entity);
    }

    public void addTickBlockedEntity(Entity entity) {
        if (!this.isTickingHandled(entity)) {
            this.specialTickRateEntities.add(entity);
        }
    }

    protected void tickBlockedEntity(Entity entity) {
        this.tickEntityAtCustomRate(entity);
    }

    protected abstract void tickEntityAtCustomRate(Entity var1);
}