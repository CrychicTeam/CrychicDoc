package com.mna.api.spells.attributes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mna.api.ManaAndArtificeMod;
import com.mna.api.spells.base.ISpellComponent;
import com.mna.api.spells.config.ISpellConfigHelper;

public final class AttributeValuePair {

    private final Attribute attribute;

    private float value;

    private float min;

    private float max;

    private float step;

    private float stepComplexity;

    private float defaultValue;

    private float multiplier = 1.0F;

    public AttributeValuePair(Attribute attribute, float defaultValue, float min, float max, float step) {
        this(attribute, defaultValue, min, max, step, 1.0F);
    }

    public AttributeValuePair(Attribute attribute, float defaultValue, float min, float max, float step, float stepComplexity) {
        this.attribute = attribute;
        this.value = defaultValue;
        this.min = min;
        this.max = max;
        this.step = step;
        this.defaultValue = defaultValue;
        this.stepComplexity = stepComplexity;
    }

    public void lookupConfig(ISpellComponent instance) {
        this.min = ManaAndArtificeMod.getConfigHelper().getConfiguredValue(instance, this.attribute, ISpellConfigHelper.Value.MINIMUM, this.min);
        this.max = ManaAndArtificeMod.getConfigHelper().getConfiguredValue(instance, this.attribute, ISpellConfigHelper.Value.MAXIMUM, this.max);
        this.step = ManaAndArtificeMod.getConfigHelper().getConfiguredValue(instance, this.attribute, ISpellConfigHelper.Value.STEP, this.step);
        this.defaultValue = ManaAndArtificeMod.getConfigHelper().getConfiguredValue(instance, this.attribute, ISpellConfigHelper.Value.DEFAULT, this.defaultValue);
        this.stepComplexity = ManaAndArtificeMod.getConfigHelper().getConfiguredValue(instance, this.attribute, ISpellConfigHelper.Value.COMPLEXITY, this.stepComplexity);
    }

    public final Attribute getAttribute() {
        return this.attribute;
    }

    public final float getValue() {
        return this.value * this.multiplier;
    }

    public final float getValueWithoutMultipliers() {
        return this.value;
    }

    public final float getMultiplier() {
        return this.multiplier;
    }

    public final float getDefaultValue() {
        return this.defaultValue;
    }

    public final float getMaximum() {
        return this.max;
    }

    public final float getMinimum() {
        return this.min;
    }

    public final float getStepComplexity() {
        return this.stepComplexity;
    }

    public final void setValue(float value) {
        this.value = value;
    }

    public final void setMultiplier(float multiplier) {
        this.multiplier = multiplier;
    }

    public final float getStep() {
        return this.step;
    }

    public final float stepUp(float maxModifier) {
        this.value = this.value + this.step;
        if (this.value > this.max + maxModifier) {
            this.value = this.max + maxModifier;
        }
        return this.value;
    }

    public final float stepDown(float minModifier) {
        this.value = this.value - this.step;
        if (this.value < this.min - minModifier) {
            this.value = this.min - minModifier;
        }
        return this.value;
    }

    public final float stepUp() {
        return this.stepUp(0.0F);
    }

    public final float stepUpIgnoreMax() {
        this.value = this.value + this.step;
        return this.value;
    }

    public final float stepDown() {
        return this.stepDown(0.0F);
    }

    public AttributeValuePair deepCopy() {
        return new AttributeValuePair(this.attribute, this.defaultValue, this.min, this.max, this.step, this.stepComplexity);
    }

    public static ImmutableList<AttributeValuePair> deepCopy(ImmutableList<AttributeValuePair> source) {
        AttributeValuePair[] values = new AttributeValuePair[source.size()];
        int index = 0;
        UnmodifiableIterator var3 = source.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            values[index++] = pair.deepCopy();
        }
        return ImmutableList.copyOf(values);
    }
}