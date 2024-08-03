package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.mojang.serialization.Codec;

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