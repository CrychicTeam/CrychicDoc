package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockEnderResidue extends AbstractGlassBlock {

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public static final BooleanProperty SLOW_DECAY = BooleanProperty.create("slow_decay");

    public BlockEnderResidue() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_LIGHT_BLUE).noOcclusion().hasPostProcess((i, j, k) -> true).emissiveRendering((i, j, k) -> true).lightLevel(i -> 3).strength(0.2F).sound(SoundType.AMETHYST).randomTicks().noOcclusion());
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(AGE, 0)).m_61124_(SLOW_DECAY, false));
    }

    @Override
    public void randomTick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        this.tick(blockState0, serverLevel1, blockPos2, randomSource3);
    }

    @Override
    public void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (random.nextInt(state.m_61143_(SLOW_DECAY) ? 15 : 5) == 0) {
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
            for (Direction direction : Direction.values()) {
                blockpos$mutableblockpos.setWithOffset(pos, direction);
                BlockState blockstate = level.m_8055_(blockpos$mutableblockpos);
                if (blockstate.m_60713_(this) && !this.incrementAge(blockstate, level, blockpos$mutableblockpos)) {
                    level.m_186460_(blockpos$mutableblockpos, this, Mth.nextInt(random, 20, 40));
                }
            }
            this.incrementAge(state, level, pos);
        } else {
            level.m_186460_(pos, this, Mth.nextInt(random, 20, 40));
        }
    }

    private boolean incrementAge(BlockState state, Level level, BlockPos pos) {
        int i = (Integer) state.m_61143_(AGE);
        if (i < 3) {
            level.setBlock(pos, (BlockState) state.m_61124_(AGE, i + 1), 2);
            return false;
        } else {
            level.setBlock(pos, Blocks.AIR.defaultBlockState(), 2);
            return true;
        }
    }

    @Override
    public void neighborChanged(BlockState blockState0, Level level1, BlockPos blockPos2, Block block3, BlockPos blockPos4, boolean boolean5) {
        super.m_6861_(blockState0, level1, blockPos2, block3, blockPos4, boolean5);
    }

    private boolean fewerNeigboursThan(BlockGetter blockGetter0, BlockPos blockPos1, int int2) {
        int i = 0;
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        for (Direction direction : Direction.values()) {
            blockpos$mutableblockpos.setWithOffset(blockPos1, direction);
            if (blockGetter0.getBlockState(blockpos$mutableblockpos).m_60713_(this)) {
                if (++i >= int2) {
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(AGE, SLOW_DECAY);
    }

    @Override
    public ItemStack getCloneItemStack(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        return ItemStack.EMPTY;
    }
}