package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;

public class BlockRotProcessor extends StructureProcessor {

    public static final Codec<BlockRotProcessor> CODEC = RecordCodecBuilder.create(p_259016_ -> p_259016_.group(RegistryCodecs.homogeneousList(Registries.BLOCK).optionalFieldOf("rottable_blocks").forGetter(p_230291_ -> p_230291_.rottableBlocks), Codec.floatRange(0.0F, 1.0F).fieldOf("integrity").forGetter(p_230289_ -> p_230289_.integrity)).apply(p_259016_, BlockRotProcessor::new));

    private final Optional<HolderSet<Block>> rottableBlocks;

    private final float integrity;

    public BlockRotProcessor(HolderSet<Block> holderSetBlock0, float float1) {
        this(Optional.of(holderSetBlock0), float1);
    }

    public BlockRotProcessor(float float0) {
        this(Optional.empty(), float0);
    }

    private BlockRotProcessor(Optional<HolderSet<Block>> optionalHolderSetBlock0, float float1) {
        this.integrity = float1;
        this.rottableBlocks = optionalHolderSetBlock0;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        RandomSource $$6 = structurePlaceSettings5.getRandom(structureTemplateStructureBlockInfo4.pos());
        return (!this.rottableBlocks.isPresent() || structureTemplateStructureBlockInfo3.state().m_204341_((HolderSet) this.rottableBlocks.get())) && !($$6.nextFloat() <= this.integrity) ? null : structureTemplateStructureBlockInfo4;
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_ROT;
    }
}