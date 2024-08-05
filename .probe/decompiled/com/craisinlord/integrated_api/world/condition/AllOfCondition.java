package com.craisinlord.integrated_api.world.condition;

import com.craisinlord.integrated_api.world.structures.context.StructureContext;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.List;

public class AllOfCondition extends StructureCondition {

    public static final Codec<AllOfCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(StructureConditionType.CONDITION_CODEC.listOf().fieldOf("conditions").forGetter(condition -> condition.conditions)).apply(builder, AllOfCondition::new));

    private final List<StructureCondition> conditions;

    public AllOfCondition(List<StructureCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ALL_OF;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return this.conditions.stream().allMatch(condition -> condition.passes(ctx));
    }
}