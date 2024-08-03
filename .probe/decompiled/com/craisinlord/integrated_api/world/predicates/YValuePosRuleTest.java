package com.craisinlord.integrated_api.world.predicates;

import com.craisinlord.integrated_api.modinit.IAPredicates;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;

public class YValuePosRuleTest extends PosRuleTest {

    public static final Codec<YValuePosRuleTest> CODEC = RecordCodecBuilder.create(instance -> instance.group(Codec.INT.fieldOf("min_y_value").forGetter(ruleTest -> ruleTest.minYValue), Codec.INT.fieldOf("max_y_value").forGetter(ruleTest -> ruleTest.maxYValue)).apply(instance, YValuePosRuleTest::new));

    private final int minYValue;

    private final int maxYValue;

    public YValuePosRuleTest(int minYValue, int maxYValue) {
        if (minYValue > maxYValue) {
            throw new IllegalArgumentException("Invalid range: [" + minYValue + "," + maxYValue + "]");
        } else {
            this.minYValue = minYValue;
            this.maxYValue = maxYValue;
        }
    }

    @Override
    public boolean test(BlockPos blockPos, BlockPos blockPos2, BlockPos blockPos3, RandomSource random) {
        return blockPos2.m_123342_() >= this.minYValue && blockPos2.m_123342_() <= this.maxYValue;
    }

    @Override
    protected PosRuleTestType<?> getType() {
        return IAPredicates.Y_VALUE_POS_RULE_TEST.get();
    }
}