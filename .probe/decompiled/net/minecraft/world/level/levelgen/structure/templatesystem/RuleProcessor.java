package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.UnmodifiableIterator;
import com.mojang.serialization.Codec;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;

public class RuleProcessor extends StructureProcessor {

    public static final Codec<RuleProcessor> CODEC = ProcessorRule.CODEC.listOf().fieldOf("rules").xmap(RuleProcessor::new, p_74306_ -> p_74306_.rules).codec();

    private final ImmutableList<ProcessorRule> rules;

    public RuleProcessor(List<? extends ProcessorRule> listExtendsProcessorRule0) {
        this.rules = ImmutableList.copyOf(listExtendsProcessorRule0);
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        RandomSource $$6 = RandomSource.create(Mth.getSeed(structureTemplateStructureBlockInfo4.pos()));
        BlockState $$7 = levelReader0.m_8055_(structureTemplateStructureBlockInfo4.pos());
        UnmodifiableIterator var9 = this.rules.iterator();
        while (var9.hasNext()) {
            ProcessorRule $$8 = (ProcessorRule) var9.next();
            if ($$8.test(structureTemplateStructureBlockInfo4.state(), $$7, structureTemplateStructureBlockInfo3.pos(), structureTemplateStructureBlockInfo4.pos(), blockPos2, $$6)) {
                return new StructureTemplate.StructureBlockInfo(structureTemplateStructureBlockInfo4.pos(), $$8.getOutputState(), $$8.getOutputTag($$6, structureTemplateStructureBlockInfo4.nbt()));
            }
        }
        return structureTemplateStructureBlockInfo4;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.RULE;
    }
}