package net.minecraft.world.level.levelgen.structure.templatesystem;

import com.mojang.serialization.Codec;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.Half;

public class BlockAgeProcessor extends StructureProcessor {

    public static final Codec<BlockAgeProcessor> CODEC = Codec.FLOAT.fieldOf("mossiness").xmap(BlockAgeProcessor::new, p_74023_ -> p_74023_.mossiness).codec();

    private static final float PROBABILITY_OF_REPLACING_FULL_BLOCK = 0.5F;

    private static final float PROBABILITY_OF_REPLACING_STAIRS = 0.5F;

    private static final float PROBABILITY_OF_REPLACING_OBSIDIAN = 0.15F;

    private static final BlockState[] NON_MOSSY_REPLACEMENTS = new BlockState[] { Blocks.STONE_SLAB.defaultBlockState(), Blocks.STONE_BRICK_SLAB.defaultBlockState() };

    private final float mossiness;

    public BlockAgeProcessor(float float0) {
        this.mossiness = float0;
    }

    @Nullable
    @Override
    public StructureTemplate.StructureBlockInfo processBlock(LevelReader levelReader0, BlockPos blockPos1, BlockPos blockPos2, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo3, StructureTemplate.StructureBlockInfo structureTemplateStructureBlockInfo4, StructurePlaceSettings structurePlaceSettings5) {
        RandomSource $$6 = structurePlaceSettings5.getRandom(structureTemplateStructureBlockInfo4.pos());
        BlockState $$7 = structureTemplateStructureBlockInfo4.state();
        BlockPos $$8 = structureTemplateStructureBlockInfo4.pos();
        BlockState $$9 = null;
        if ($$7.m_60713_(Blocks.STONE_BRICKS) || $$7.m_60713_(Blocks.STONE) || $$7.m_60713_(Blocks.CHISELED_STONE_BRICKS)) {
            $$9 = this.maybeReplaceFullStoneBlock($$6);
        } else if ($$7.m_204336_(BlockTags.STAIRS)) {
            $$9 = this.maybeReplaceStairs($$6, structureTemplateStructureBlockInfo4.state());
        } else if ($$7.m_204336_(BlockTags.SLABS)) {
            $$9 = this.maybeReplaceSlab($$6);
        } else if ($$7.m_204336_(BlockTags.WALLS)) {
            $$9 = this.maybeReplaceWall($$6);
        } else if ($$7.m_60713_(Blocks.OBSIDIAN)) {
            $$9 = this.maybeReplaceObsidian($$6);
        }
        return $$9 != null ? new StructureTemplate.StructureBlockInfo($$8, $$9, structureTemplateStructureBlockInfo4.nbt()) : structureTemplateStructureBlockInfo4;
    }

    @Nullable
    private BlockState maybeReplaceFullStoneBlock(RandomSource randomSource0) {
        if (randomSource0.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] $$1 = new BlockState[] { Blocks.CRACKED_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(randomSource0, Blocks.STONE_BRICK_STAIRS) };
            BlockState[] $$2 = new BlockState[] { Blocks.MOSSY_STONE_BRICKS.defaultBlockState(), getRandomFacingStairs(randomSource0, Blocks.MOSSY_STONE_BRICK_STAIRS) };
            return this.getRandomBlock(randomSource0, $$1, $$2);
        }
    }

    @Nullable
    private BlockState maybeReplaceStairs(RandomSource randomSource0, BlockState blockState1) {
        Direction $$2 = (Direction) blockState1.m_61143_(StairBlock.FACING);
        Half $$3 = (Half) blockState1.m_61143_(StairBlock.HALF);
        if (randomSource0.nextFloat() >= 0.5F) {
            return null;
        } else {
            BlockState[] $$4 = new BlockState[] { (BlockState) ((BlockState) Blocks.MOSSY_STONE_BRICK_STAIRS.defaultBlockState().m_61124_(StairBlock.FACING, $$2)).m_61124_(StairBlock.HALF, $$3), Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() };
            return this.getRandomBlock(randomSource0, NON_MOSSY_REPLACEMENTS, $$4);
        }
    }

    @Nullable
    private BlockState maybeReplaceSlab(RandomSource randomSource0) {
        return randomSource0.nextFloat() < this.mossiness ? Blocks.MOSSY_STONE_BRICK_SLAB.defaultBlockState() : null;
    }

    @Nullable
    private BlockState maybeReplaceWall(RandomSource randomSource0) {
        return randomSource0.nextFloat() < this.mossiness ? Blocks.MOSSY_STONE_BRICK_WALL.defaultBlockState() : null;
    }

    @Nullable
    private BlockState maybeReplaceObsidian(RandomSource randomSource0) {
        return randomSource0.nextFloat() < 0.15F ? Blocks.CRYING_OBSIDIAN.defaultBlockState() : null;
    }

    private static BlockState getRandomFacingStairs(RandomSource randomSource0, Block block1) {
        return (BlockState) ((BlockState) block1.defaultBlockState().m_61124_(StairBlock.FACING, Direction.Plane.HORIZONTAL.getRandomDirection(randomSource0))).m_61124_(StairBlock.HALF, (Half) Util.getRandom(Half.values(), randomSource0));
    }

    private BlockState getRandomBlock(RandomSource randomSource0, BlockState[] blockState1, BlockState[] blockState2) {
        return randomSource0.nextFloat() < this.mossiness ? getRandomBlock(randomSource0, blockState2) : getRandomBlock(randomSource0, blockState1);
    }

    private static BlockState getRandomBlock(RandomSource randomSource0, BlockState[] blockState1) {
        return blockState1[randomSource0.nextInt(blockState1.length)];
    }

    @Override
    protected StructureProcessorType<?> getType() {
        return StructureProcessorType.BLOCK_AGE;
    }
}