package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;

public abstract class StructureCondition {

    public static final StructureCondition ALWAYS_TRUE = new AlwaysTrueCondition();

    public abstract StructureConditionType<?> type();

    public abstract boolean passes(StructureContext var1);
}