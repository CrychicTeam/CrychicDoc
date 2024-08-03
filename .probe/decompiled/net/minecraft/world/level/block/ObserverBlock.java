package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class ObserverBlock extends DirectionalBlock {

    public static final BooleanProperty POWERED = BlockStateProperties.POWERED;

    public ObserverBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_52588_, Direction.SOUTH)).m_61124_(POWERED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(f_52588_, POWERED);
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(f_52588_, rotation1.rotate((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(f_52588_)));
    }

    @Override
    public void tick(BlockState blockState0, ServerLevel serverLevel1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(POWERED)) {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(POWERED, false), 2);
        } else {
            serverLevel1.m_7731_(blockPos2, (BlockState) blockState0.m_61124_(POWERED, true), 2);
            serverLevel1.m_186460_(blockPos2, this, 2);
        }
        this.updateNeighborsInFront(serverLevel1, blockPos2, blockState0);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        if (blockState0.m_61143_(f_52588_) == direction1 && !(Boolean) blockState0.m_61143_(POWERED)) {
            this.startSignal(levelAccessor3, blockPos4);
        }
        return super.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    private void startSignal(LevelAccessor levelAccessor0, BlockPos blockPos1) {
        if (!levelAccessor0.m_5776_() && !levelAccessor0.getBlockTicks().m_183582_(blockPos1, this)) {
            levelAccessor0.scheduleTick(blockPos1, this, 2);
        }
    }

    protected void updateNeighborsInFront(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = (Direction) blockState2.m_61143_(f_52588_);
        BlockPos $$4 = blockPos1.relative($$3.getOpposite());
        level0.neighborChanged($$4, this, blockPos1);
        level0.updateNeighborsAtExceptFromFacing($$4, this, $$3);
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public int getDirectSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_60746_(blockGetter1, blockPos2, direction3);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(POWERED) && blockState0.m_61143_(f_52588_) == direction3 ? 15 : 0;
    }

    @Override
    public void onPlace(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if (!level1.isClientSide() && (Boolean) blockState0.m_61143_(POWERED) && !level1.m_183326_().m_183582_(blockPos2, this)) {
                BlockState $$5 = (BlockState) blockState0.m_61124_(POWERED, false);
                level1.setBlock(blockPos2, $$5, 18);
                this.updateNeighborsInFront(level1, blockPos2, $$5);
            }
        }
    }

    @Override
    public void onRemove(BlockState blockState0, Level level1, BlockPos blockPos2, BlockState blockState3, boolean boolean4) {
        if (!blockState0.m_60713_(blockState3.m_60734_())) {
            if (!level1.isClientSide && (Boolean) blockState0.m_61143_(POWERED) && level1.m_183326_().m_183582_(blockPos2, this)) {
                this.updateNeighborsInFront(level1, blockPos2, (BlockState) blockState0.m_61124_(POWERED, false));
            }
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(f_52588_, blockPlaceContext0.getNearestLookingDirection().getOpposite().getOpposite());
    }
}