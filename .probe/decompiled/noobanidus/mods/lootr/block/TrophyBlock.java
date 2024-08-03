package noobanidus.mods.lootr.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TrophyBlock extends Block {

    private static final VoxelShape EAST_WEST = Block.box(1.5, 0.0, 4.0, 14.5, 14.5, 12.0);

    private static final VoxelShape NORTH_SOUTH = Block.box(4.0, 0.0, 1.5, 12.0, 14.5, 14.5);

    public TrophyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(HorizontalDirectionalBlock.FACING, context.m_8125_().getOpposite());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HorizontalDirectionalBlock.FACING);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        Direction facing = (Direction) state.m_61143_(HorizontalDirectionalBlock.FACING);
        return facing != Direction.EAST && facing != Direction.WEST ? NORTH_SOUTH : EAST_WEST;
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return (BlockState) blockState0.m_61124_(HorizontalDirectionalBlock.FACING, rotation1.rotate((Direction) blockState0.m_61143_(HorizontalDirectionalBlock.FACING)));
    }

    @Override
    public BlockState mirror(BlockState blockState0, Mirror mirror1) {
        return blockState0.m_60717_(mirror1.getRotation((Direction) blockState0.m_61143_(HorizontalDirectionalBlock.FACING)));
    }
}