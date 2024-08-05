package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import java.util.List;

public class AnyOfCondition extends StructureCondition {

    public static final Codec<AnyOfCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(StructureConditionType.CONDITION_CODEC.listOf().fieldOf("conditions").forGetter(condition -> condition.conditions)).apply(builder, AnyOfCondition::new));

    private final List<StructureCondition> conditions;

    public AnyOfCondition(List<StructureCondition> conditions) {
        this.conditions = conditions;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.ANY_OF;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        return this.conditions.stream().anyMatch(condition -> condition.passes(ctx));
    }
}