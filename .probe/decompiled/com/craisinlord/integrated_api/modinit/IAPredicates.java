package com.craisinlord.integrated_api.modinit;

import com.craisinlord.integrated_api.modinit.registry.RegistryEntry;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistries;
import com.craisinlord.integrated_api.modinit.registry.ResourcefulRegistry;
import com.craisinlord.integrated_api.world.predicates.MatterPhaseRuleTest;
import com.craisinlord.integrated_api.world.predicates.PieceOriginAxisAlignedLinearPosRuleTest;
import com.craisinlord.integrated_api.world.predicates.YValuePosRuleTest;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.templatesystem.PosRuleTestType;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTestType;

public final class IAPredicates {

    public static final ResourcefulRegistry<RuleTestType<?>> RULE_TEST = ResourcefulRegistries.create(BuiltInRegistries.RULE_TEST, "integrated_api");

    public static final ResourcefulRegistry<PosRuleTestType<?>> POS_RULE_TEST = ResourcefulRegistries.create(BuiltInRegistries.POS_RULE_TEST, "integrated_api");

    public static final RegistryEntry<RuleTestType<MatterPhaseRuleTest>> MATTER_PHASE_RULE_TEST = RULE_TEST.register("matter_phase_rule_test", () -> () -> MatterPhaseRuleTest.CODEC);

    public static final RegistryEntry<PosRuleTestType<PieceOriginAxisAlignedLinearPosRuleTest>> PIECE_ORIGIN_AXIS_ALIGNED_LINEAR_POS_RULE_TEST = POS_RULE_TEST.register("piece_origin_axis_aligned_linear_pos_rule_test", () -> () -> PieceOriginAxisAlignedLinearPosRuleTest.CODEC);

    public static final RegistryEntry<PosRuleTestType<YValuePosRuleTest>> Y_VALUE_POS_RULE_TEST = POS_RULE_TEST.register("y_value_pos_rule_test", () -> () -> YValuePosRuleTest.CODEC);
}