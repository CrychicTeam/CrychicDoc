package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class FlaxBlockUpper extends Block {

    private static final VoxelShape[] SHAPES_TOP = new VoxelShape[] { Block.box(2.0, 0.0, 2.0, 14.0, 3.0, 14.0), Block.box(1.0, 0.0, 1.0, 15.0, 7.0, 15.0), Block.box(1.0, 0.0, 1.0, 15.0, 11.0, 15.0), Block.box(1.0, 0.0, 1.0, 15.0, 16.0, 15.0) };

    public static final EnumProperty<DoubleBlockHalf> HALF = BlockStateProperties.DOUBLE_BLOCK_HALF;

    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;

    public FlaxBlockUpper(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return SHAPES_TOP[state.m_61143_(AGE)];
    }

    @Override
    public BlockState updateShape(BlockState stateIn, Direction facing, BlockState facingState, LevelAccessor worldIn, BlockPos currentPos, BlockPos facingPos) {
        if (facing == Direction.DOWN && this.isValidLowerStage(facingState)) {
            int ageBelow = (Integer) facingState.m_61143_(FlaxBlock.f_52244_);
            if (ageBelow >= 4) {
                int targetAge = ageBelow - 4;
                if ((Integer) stateIn.m_61143_(AGE) != targetAge) {
                    return (BlockState) stateIn.m_61124_(AGE, targetAge);
                }
            }
            return Blocks.AIR.defaultBlockState();
        } else {
            return super.m_7417_(stateIn, facing, facingState, worldIn, currentPos, facingPos);
        }
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader worldIn, BlockPos pos) {
        return this.isValidLowerStage(worldIn.m_8055_(pos.below()));
    }

    public boolean isValidLowerStage(BlockState state) {
        return state.m_60734_() instanceof FlaxBlock;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(AGE);
    }
}