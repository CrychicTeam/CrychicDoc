package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class NotCondition extends StructureCondition {

    public static final Codec<NotCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(StructureConditionType.CONDITION_CODEC.fieldOf("condition").forGetter(condition -> condition.condition)).apply(builder, NotCondition::new));

    private final StructureCondition condition;

    public NotCondition(StructureCondition condition) {
        this.condition = condition;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.NOT;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return !this.condition.passes(ctx);
    }
}