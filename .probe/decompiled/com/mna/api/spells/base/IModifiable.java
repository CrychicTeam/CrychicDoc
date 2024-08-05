package com.mna.api.spells.base;

import com.google.common.collect.ImmutableList;
import com.mna.api.spells.attributes.AttributeValuePair;

public interface IModifiable<T extends ISpellComponent> {

    ImmutableList<AttributeValuePair> getModifiableAttributes();
}