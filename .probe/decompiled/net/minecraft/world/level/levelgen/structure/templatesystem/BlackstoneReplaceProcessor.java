package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.google.common.collect.Maps;
import com.mojang.serialization.Codec;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.SlabType;

public class BlackstoneReplaceProcessor extends StructureProcessor {

    public static final Codec<BlackstoneReplaceProcessor> CODEC = Codec.unit(() -> BlackstoneReplaceProcessor.INSTANCE);

    public static final BlackstoneReplaceProcessor INSTANCE = new BlackstoneReplaceProcessor();

    private final Map<Block, Block> replacements = Util.make(Maps.newHashMap(), p_74007_ -> {
        p_74007_.put(Blocks.COBBLESTONE, Blocks.BLACKSTONE);
        p_74007_.put(Blocks.MOSSY_COBBLESTONE, Blocks.BLACKSTONE);
        p_74007_.put(Blocks.STONE, Blocks.POLISHED_BLACKSTONE);
        p_74007_.put(Blocks.STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        p_74007_.put(Blocks.MOSSY_STONE_BRICKS, Blocks.POLISHED_BLACKSTONE_BRICKS);
        p_74007_.put(Blocks.COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        p_74007_.put(Blocks.MOSSY_COBBLESTONE_STAIRS, Blocks.BLACKSTONE_STAIRS);
        p_74007_.put(Blocks.STONE_STAIRS, Blocks.POLISHED_BLACKSTONE_STAIRS);
        p_74007_.put(Blocks.STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        p_74007_.put(Blocks.MOSSY_STONE_BRICK_STAIRS, Blocks.POLISHED_BLACKSTONE_BRICK_STAIRS);
        p_74007_.put(Blocks.COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
        p_74007_.put(Blocks.MOSSY_COBBLESTONE_SLAB, Blocks.BLACKSTONE_SLAB);
        p_74007_.put(Blocks.SMOOTH_STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        p_74007_.put(Blocks.STONE_SLAB, Blocks.POLISHED_BLACKSTONE_SLAB);
        p_74007_.put(Blocks.STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        p_74007_.put(Blocks.MOSSY_STONE_BRICK_SLAB, Blocks.POLISHED_BLACKSTONE_BRICK_SLAB);
        p_74007_.put(Blocks.STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        p_74007_.put(Blocks.MOSSY_STONE_BRICK_WALL, Blocks.POLISHED_BLACKSTONE_BRICK_WALL);
        p_74007_.put(Blocks.COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
        p_74007_.put(Blocks.MOSSY_COBBLESTONE_WALL, Blocks.BLACKSTONE_WALL);
        p_74007_.put(Blocks.CHISELED_STONE_BRICKS, Blocks.CHISELED_POLISHED_BLACKSTONE);
        p_74007_.put(Blocks.CRACKED_STONE_BRICKS, Blocks.CRACKED_POLISHED_BLACKSTONE_BRICKS);
        p_74007_.put(Blocks.IRON_BARS, Blocks.CHAIN);
    });

    private BlackstoneReplaceProcessor() {
    }

    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        Block $$6 = (Block) this.replacements.get(structureTemplateStructureBlockInfo4.state().m_60734_());
        if ($$6 == null) {
            return structureTemplateStructureBlockInfo4;
        } else {
            BlockState $$7 = structureTemplateStructureBlockInfo4.state();
            BlockState $$8 = $$6.defaultBlockState();
            if ($$7.m_61138_(StairBlock.FACING)) {
                $$8 = (BlockState) $$8.m_61124_(StairBlock.FACING, (Direction) $$7.m_61143_(StairBlock.FACING));
            }
            if ($$7.m_61138_(StairBlock.HALF)) {
                $$8 = (BlockState) $$8.m_61124_(StairBlock.HALF, (Half) $$7.m_61143_(StairBlock.HALF));
            }
            if ($$7.m_61138_(SlabBlock.TYPE)) {
                $$8 = (BlockState) $$8.m_61124_(SlabBlock.TYPE, (SlabType) $$7.m_61143_(SlabBlock.TYPE));
            }
            return new StructureTemplate.StructureBlockInfo(structureTemplateStructureBlockInfo4.pos(), $$8, structureTemplateStructureBlockInfo4.nbt());
        }
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLACKSTONE_REPLACE;
    }
}