package org.violetmoon.zeta.api;

import net.minecraft.advancements.Criterion;

public interface IMutableAdvancement {

    void addRequiredCriterion(String var1, Criterion var2);

    void addOrCriterion(String var1, Criterion var2);

    Criterion getCriterion(String var1);
}