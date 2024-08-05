package com.mna.api.spells.attributes;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import java.util.Collection;

public class ModifiedSpellAttributes {

    private final ImmutableList<AttributeValuePair> attributeValues;

    public ModifiedSpellAttributes(Collection<AttributeValuePair> modified) {
        this.attributeValues = ImmutableList.copyOf(modified);
    }

    public float GetModifiedAttribute(Attribute attribute, float defaultValue) {
        UnmodifiableIterator var3 = this.attributeValues.iterator();
        while (var3.hasNext()) {
            AttributeValuePair pair = (AttributeValuePair) var3.next();
            if (pair.getAttribute() == attribute) {
                return pair.getValue();
            }
        }
        return defaultValue;
    }
}