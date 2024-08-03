package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;

public abstract class StructureCondition {

    public static final StructureCondition ALWAYS_TRUE = new AlwaysTrueCondition();

    public abstract StructureConditionType<?> type();

    public abstract boolean passes(StructureContext var1);
}