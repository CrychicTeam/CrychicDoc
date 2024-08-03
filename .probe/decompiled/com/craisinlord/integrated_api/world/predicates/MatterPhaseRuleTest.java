package com.craisinlord.integrated_api.world.predicates;

import com.craisinlord.integrated_api.modinit.IAPredicates;
import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Locale;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public class MatterPhaseRuleTest extends RuleTest {

    public static final Codec<MatterPhaseRuleTest> CODEC = RecordCodecBuilder.create(instance -> instance.group(StringRepresentable.fromEnum(MatterPhaseRuleTest.MATTER_PHASE::values).fieldOf("phase_to_test_for").stable().forGetter(ruletest -> ruletest.phaseToTestFor), Codec.BOOL.fieldOf("invert_condition").orElse(false).forGetter(ruletest -> ruletest.invertCondition)).apply(instance, instance.stable(MatterPhaseRuleTest::new)));

    private final MatterPhaseRuleTest.MATTER_PHASE phaseToTestFor;

    private final boolean invertCondition;

    private MatterPhaseRuleTest(MatterPhaseRuleTest.MATTER_PHASE phaseToTestFor, boolean invertCondition) {
        this.phaseToTestFor = phaseToTestFor;
        this.invertCondition = invertCondition;
    }

    @Override
    public boolean test(BlockState state, RandomSource random) {
        boolean phaseMatch = false;
        switch(this.phaseToTestFor) {
            case AIR:
                if (state.m_60795_()) {
                    phaseMatch = true;
                }
                break;
            case LIQUID:
                if (!state.m_60819_().isEmpty()) {
                    phaseMatch = true;
                }
                break;
            case SOLID:
                if (!state.m_60795_() && state.m_60819_().isEmpty() && state.m_60815_()) {
                    phaseMatch = true;
                }
                break;
            case AIR_RAIL_OR_CHAIN:
                if (state.m_60795_() || state.m_60713_(Blocks.CHAIN) || state.m_60713_(Blocks.RAIL)) {
                    phaseMatch = true;
                }
                break;
            case LIQUID_RAIL_OR_CHAIN:
                if (!state.m_60819_().isEmpty() || state.m_60713_(Blocks.CHAIN) || state.m_60713_(Blocks.RAIL)) {
                    phaseMatch = true;
                }
        }
        if (this.invertCondition) {
            phaseMatch = !phaseMatch;
        }
        return phaseMatch;
    }

    @Override
    protected RuleTestType<?> getType() {
        return IAPredicates.MATTER_PHASE_RULE_TEST.get();
    }

    public static enum MATTER_PHASE implements StringRepresentable {

        SOLID("SOLID"), LIQUID("LIQUID"), AIR("AIR"), AIR_RAIL_OR_CHAIN("AIR_RAIL_OR_CHAIN"), LIQUID_RAIL_OR_CHAIN("LIQUID_RAIL_OR_CHAIN");

        private final String name;

        private static final Map<String, MatterPhaseRuleTest.MATTER_PHASE> BY_NAME = Util.make(Maps.newHashMap(), hashMap -> {
            MatterPhaseRuleTest.MATTER_PHASE[] var1 = values();
            for (MatterPhaseRuleTest.MATTER_PHASE type : var1) {
                hashMap.put(type.name, type);
            }
        });

        private MATTER_PHASE(String name) {
            this.name = name;
        }

        public static MatterPhaseRuleTest.MATTER_PHASE byName(String name) {
            return (MatterPhaseRuleTest.MATTER_PHASE) BY_NAME.get(name.toUpperCase(Locale.ROOT));
        }

        @Override
        public String getSerializedName() {
            return this.name;
        }
    }
}