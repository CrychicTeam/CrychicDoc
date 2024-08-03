package com.mna.api.spells.base;

import com.google.common.collect.ImmutableList;
import com.mna.api.spells.attributes.Attribute;

public interface IModifiedSpellPart<T extends ISpellComponent & IModifiable<T>> {

    T getPart();

    ImmutableList<Attribute> getContainedAttributes();

    float getValue(Attribute var1);

    float getValueWithoutMultipliers(Attribute var1);

    float getMultiplier(Attribute var1);

    boolean setValue(Attribute var1, float var2);

    boolean setMultiplier(Attribute var1, float var2);

    boolean resetMultiplier(Attribute var1);

    float getDefaultValue(Attribute var1);

    float getStep(Attribute var1);

    float getStepComplexity(Attribute var1);

    void resetValueToDefault(Attribute var1);

    float stepUp(Attribute var1, float var2);

    float stepDown(Attribute var1, float var2);

    float getMinimumValue(Attribute var1);

    float getMaximumValue(Attribute var1);

    float stepUpIgnoreMax(Attribute var1);

    boolean isSame(IModifiedSpellPart<T> var1);
}