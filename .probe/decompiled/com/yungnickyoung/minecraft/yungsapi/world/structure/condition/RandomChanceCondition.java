package com.yungnickyoung.minecraft.yungsapi.world.structure.condition;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yungnickyoung.minecraft.yungsapi.YungsApiCommon;
import com.yungnickyoung.minecraft.yungsapi.world.structure.context.StructureContext;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.RandomSource;

public class RandomChanceCondition extends StructureCondition {

    public static final Codec<RandomChanceCondition> CODEC = RecordCodecBuilder.create(builder -> builder.group(ExtraCodecs.POSITIVE_FLOAT.fieldOf("chance").forGetter(condition -> condition.chance)).apply(builder, RandomChanceCondition::new));

    public final float chance;

    public RandomChanceCondition(float chance) {
        this.chance = chance;
    }

    @Override
    public StructureConditionType<?> type() {
        return StructureConditionType.RANDOM_CHANCE;
    }

    @Override
    public boolean passes(StructureContext ctx) {
        RandomSource random = ctx.random();
        if (random == null) {
            YungsApiCommon.LOGGER.error("Missing required field 'random' for random_chance condition!");
            return false;
        } else {
            return random.nextFloat() < this.chance;
        }
    }
}