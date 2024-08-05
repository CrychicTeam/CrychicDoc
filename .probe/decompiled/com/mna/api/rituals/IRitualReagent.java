package com.mna.api.rituals;

import net.minecraft.resources.ResourceLocation;

public interface IRitualReagent {

    boolean isOptional();

    boolean shouldConsumeReagent();

    boolean isManualReturn();

    boolean isDynamic();

    boolean isDynamicSource();

    ResourceLocation getResourceLocation();

    void setResourceLocation(ResourceLocation var1);

    boolean isEmpty();
}