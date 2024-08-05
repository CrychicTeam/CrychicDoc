package dev.shadowsoffire.attributeslib.util;

import org.jetbrains.annotations.ApiStatus.Internal;

@Internal
public interface IAttributeManager {

    boolean areAttributesUpdating();

    void setAttributesUpdating(boolean var1);
}