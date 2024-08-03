package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import java.util.Optional;
import net.minecraft.util.ExtraCodecs;

public class DepthCondition extends StructureCondition {

    public static final Codec<DepthCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("min_required_depth").forGetter(condition -> condition.minRequiredDepth), ExtraCodecs.NON_NEGATIVE_INT.optionalFieldOf("max_possible_depth").forGetter(condition -> condition.maxPossibleDepth)).apply(builder, DepthCondition::new));

    public final Optional<Integer> minRequiredDepth;

    public final Optional<Integer> maxPossibleDepth;

    public DepthCondition(Optional<Integer> minRequiredDepth, Optional<Integer> maxPossibleDepth) {
        this.minRequiredDepth = minRequiredDepth;
        this.maxPossibleDepth = maxPossibleDepth;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.DEPTH;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        int depth = ctx.depth();
        boolean isAtMinRequiredDepth = this.minRequiredDepth.isEmpty() || (Integer) this.minRequiredDepth.get() <= depth;
        boolean isAtMaxAllowableDepth = this.maxPossibleDepth.isEmpty() || (Integer) this.maxPossibleDepth.get() >= depth;
        return isAtMinRequiredDepth && isAtMaxAllowableDepth;
    }
}