package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.Passthrough;
import net.minecraft.world.level.levelgen.structure.templatesystem.rule.blockentity.RuleBlockEntityModifier;

public class ProcessorRule {

    public static final Passthrough DEFAULT_BLOCK_ENTITY_MODIFIER = Passthrough.INSTANCE;

    public static final Codec<ProcessorRule> CODEC = RecordCodecBuilder.create(p_277334_ -> p_277334_.group(RuleTest.CODEC.fieldOf("input_predicate").forGetter(p_163747_ -> p_163747_.inputPredicate), RuleTest.CODEC.fieldOf("location_predicate").forGetter(p_163745_ -> p_163745_.locPredicate), PosRuleTest.CODEC.optionalFieldOf("position_predicate", PosAlwaysTrueTest.INSTANCE).forGetter(p_163743_ -> p_163743_.posPredicate), BlockState.CODEC.fieldOf("output_state").forGetter(p_163741_ -> p_163741_.outputState), RuleBlockEntityModifier.CODEC.optionalFieldOf("block_entity_modifier", DEFAULT_BLOCK_ENTITY_MODIFIER).forGetter(p_277333_ -> p_277333_.blockEntityModifier)).apply(p_277334_, ProcessorRule::new));

    private final RuleTest inputPredicate;

    private final RuleTest locPredicate;

    private final PosRuleTest posPredicate;

    private final BlockState outputState;

    private final RuleBlockEntityModifier blockEntityModifier;

    public ProcessorRule(RuleTest ruleTest0, RuleTest ruleTest1, BlockState blockState2) {
        this(ruleTest0, ruleTest1, PosAlwaysTrueTest.INSTANCE, blockState2);
    }

    public ProcessorRule(RuleTest ruleTest0, RuleTest ruleTest1, PosRuleTest posRuleTest2, BlockState blockState3) {
        this(ruleTest0, ruleTest1, posRuleTest2, blockState3, DEFAULT_BLOCK_ENTITY_MODIFIER);
    }

    public ProcessorRule(RuleTest ruleTest0, RuleTest ruleTest1, PosRuleTest posRuleTest2, BlockState blockState3, RuleBlockEntityModifier ruleBlockEntityModifier4) {
        this.inputPredicate = ruleTest0;
        this.locPredicate = ruleTest1;
        this.posPredicate = posRuleTest2;
        this.outputState = blockState3;
        this.blockEntityModifier = ruleBlockEntityModifier4;
    }

    public boolean test(BlockState blockState0, BlockState blockState1, BlockPos blockPos2, BlockPos blockPos3, BlockPos blockPos4, RandomSource randomSource5) {
        return this.inputPredicate.test(blockState0, randomSource5) && this.locPredicate.test(blockState1, randomSource5) && this.posPredicate.test(blockPos2, blockPos3, blockPos4, randomSource5);
    }

    public BlockState getOutputState() {
        return this.outputState;
    }

    @Nullable
    public CompoundTag getOutputTag(RandomSource randomSource0, @Nullable CompoundTag compoundTag1) {
        return this.blockEntityModifier.apply(randomSource0, compoundTag1);
    }
}