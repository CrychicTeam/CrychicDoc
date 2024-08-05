package net.minecraft.world.level.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class RedstoneWallTorchBlock extends RedstoneTorchBlock {

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;

    public static final BooleanProperty LIT = RedstoneTorchBlock.LIT;

    protected RedstoneWallTorchBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(LIT, true));
    }

    @Override
    public String getDescriptionId() {
        return this.m_5456_().getDescriptionId();
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return WallTorchBlock.getShape(blockState0);
    }

    @Override
    public boolean canSurvive(BlockState blockState0, LevelReader levelReader1, BlockPos blockPos2) {
        return Blocks.WALL_TORCH.m_7898_(blockState0, levelReader1, blockPos2);
    }

    @Override
    public BlockState updateShape(BlockState blockState0, Direction direction1, BlockState blockState2, LevelAccessor levelAccessor3, BlockPos blockPos4, BlockPos blockPos5) {
        return Blocks.WALL_TORCH.m_7417_(blockState0, direction1, blockState2, levelAccessor3, blockPos4, blockPos5);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        BlockState $$1 = Blocks.WALL_TORCH.getStateForPlacement(blockPlaceContext0);
        return $$1 == null ? null : (BlockState) this.m_49966_().m_61124_(FACING, (Direction) $$1.m_61143_(FACING));
    }

    @Override
    public void animateTick(BlockState blockState0, Level level1, BlockPos blockPos2, RandomSource randomSource3) {
        if ((Boolean) blockState0.m_61143_(LIT)) {
            Direction $$4 = ((Direction) blockState0.m_61143_(FACING)).getOpposite();
            double $$5 = 0.27;
            double $$6 = (double) blockPos2.m_123341_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2 + 0.27 * (double) $$4.getStepX();
            double $$7 = (double) blockPos2.m_123342_() + 0.7 + (randomSource3.nextDouble() - 0.5) * 0.2 + 0.22;
            double $$8 = (double) blockPos2.m_123343_() + 0.5 + (randomSource3.nextDouble() - 0.5) * 0.2 + 0.27 * (double) $$4.getStepZ();
            level1.addParticle(this.f_57488_, $$6, $$7, $$8, 0.0, 0.0, 0.0);
        }
    }

    @Override
    protected boolean hasNeighborSignal(Level level0, BlockPos blockPos1, BlockState blockState2) {
        Direction $$3 = ((Direction) blockState2.m_61143_(FACING)).getOpposite();
        return level0.m_276987_(blockPos1.relative($$3), $$3);
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return blockState0.m_61143_(LIT) && blockState0.m_61143_(FACING) != direction3 ? 15 : 0;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return Blocks.WALL_TORCH.m_6843_(blockState0, rotation1);
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return Blocks.WALL_TORCH.m_6943_(blockState0, mirror1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(FACING, LIT);
    }
}