package dev.shadowsoffire.attributeslib.api;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraftforge.eventbus.api.Event;

public class AttributeChangedValueEvent extends Event {

    protected LivingEntity entity;

    protected AttributeInstance attrInst;

    protected double oldValue;

    protected double newValue;

    public AttributeChangedValueEvent(LivingEntity entity, AttributeInstance attrInst, double oldValue, double newValue) {
        this.entity = entity;
        this.attrInst = attrInst;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public LivingEntity getEntity() {
        return this.entity;
    }

    public AttributeInstance getAttributeInstance() {
        return this.attrInst;
    }

    public double getOldValue() {
        return this.oldValue;
    }

    public double getNewValue() {
        return this.newValue;
    }
}