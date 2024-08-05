package org.violetmoon.zeta.api;

import java.util.Set;
import java.util.function.BooleanSupplier;
import net.minecraft.resources.ResourceLocation;

public interface IAdvancementModifier {

    Set<ResourceLocation> getTargets();

    boolean apply(ResourceLocation var1, IMutableAdvancement var2);

    default IAdvancementModifier setCondition(BooleanSupplier cond) {
        return this;
    }

    default boolean isActive() {
        return true;
    }
}