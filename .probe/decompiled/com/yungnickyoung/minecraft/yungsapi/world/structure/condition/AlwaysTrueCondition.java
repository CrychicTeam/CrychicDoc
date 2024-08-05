package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;

public class AlwaysTrueCondition extends StructureCondition {

    private static final AlwaysTrueCondition INSTANCE = new AlwaysTrueCondition();

    public static final Codec<AlwaysTrueCondition> CODEC = Codec.unit(() -> INSTANCE);

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ALWAYS_TRUE;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return true;
    }
}