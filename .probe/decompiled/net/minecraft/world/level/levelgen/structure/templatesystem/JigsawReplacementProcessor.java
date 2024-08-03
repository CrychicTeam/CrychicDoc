package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.commands.arguments.blocks.BlockStateParser;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import org.slf4j.Logger;

public class JigsawReplacementProcessor extends StructureProcessor {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Codec<JigsawReplacementProcessor> CODEC = Codec.unit(() -> JigsawReplacementProcessor.INSTANCE);

    public static final JigsawReplacementProcessor INSTANCE = new JigsawReplacementProcessor();

    private JigsawReplacementProcessor() {
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        BlockState $$6 = structureTemplateStructureBlockInfo4.state();
        if ($$6.m_60713_(Blocks.JIGSAW)) {
            if (structureTemplateStructureBlockInfo4.nbt() == null) {
                LOGGER.warn("Jigsaw block at {} is missing nbt, will not replace", blockPos1);
                return structureTemplateStructureBlockInfo4;
            } else {
                String $$7 = structureTemplateStructureBlockInfo4.nbt().getString("final_state");
                BlockState $$9;
                try {
                    BlockStateParser.BlockResult $$8 = BlockStateParser.parseForBlock(levelReader0.holderLookup(Registries.BLOCK), $$7, true);
                    $$9 = $$8.blockState();
                } catch (CommandSyntaxException var11) {
                    throw new RuntimeException(var11);
                }
                return $$9.m_60713_(Blocks.STRUCTURE_VOID) ? null : new StructureTemplate.StructureBlockInfo(structureTemplateStructureBlockInfo4.pos(), $$9, null);
            }
        } else {
            return structureTemplateStructureBlockInfo4;
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.JIGSAW_REPLACEMENT;
    }
}