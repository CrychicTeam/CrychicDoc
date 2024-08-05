package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.mehvahdjukaar.supplementaries.common.block.tiles.BookPileBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class BookPileHorizontalBlock extends BookPileBlock {

    private static final VoxelShape SHAPE_1_Z = Block.box(6.0, 0.0, 4.0, 10.0, 10.0, 12.0);

    private static final VoxelShape SHAPE_1_X = Block.box(4.0, 0.0, 6.0, 12.0, 10.0, 10.0);

    private static final VoxelShape SHAPE_2_Z = Block.box(3.0, 0.0, 4.0, 13.0, 10.0, 12.0);

    private static final VoxelShape SHAPE_2_X = Block.box(4.0, 0.0, 3.0, 12.0, 10.0, 13.0);

    private static final VoxelShape SHAPE_3_Z = Block.box(1.0, 0.0, 4.0, 15.0, 10.0, 12.0);

    private static final VoxelShape SHAPE_3_X = Block.box(4.0, 0.0, 1.0, 12.0, 10.0, 15.0);

    private static final VoxelShape SHAPE_4_Z = Block.box(0.0, 0.0, 4.0, 16.0, 10.0, 12.0);

    private static final VoxelShape SHAPE_4_X = Block.box(4.0, 0.0, 0.0, 12.0, 10.0, 16.0);

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public BookPileHorizontalBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(WATERLOGGED, false)).m_61124_(BOOKS, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState blockstate = context.m_43725_().getBlockState(context.getClickedPos());
        if (blockstate.m_60734_() instanceof BookPileBlock) {
            return (BlockState) blockstate.m_61124_(BOOKS, (Integer) blockstate.m_61143_(BOOKS) + 1);
        } else {
            FluidState fluidState = context.m_43725_().getFluidState(context.getClickedPos());
            boolean flag = fluidState.getType() == Fluids.WATER && fluidState.getAmount() == 8;
            return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, flag)).m_61124_(FACING, context.m_8125_().getOpposite());
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BookPileBlockTile(pPos, pState, true);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter world, BlockPos pos, CollisionContext context) {
        boolean x = ((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.X;
        return getVoxelShape(state, x);
    }

    private static VoxelShape getVoxelShape(BlockState state, boolean x) {
        return switch(state.m_61143_(BOOKS)) {
            case 2 ->
                x ? SHAPE_2_X : SHAPE_2_Z;
            case 3 ->
                x ? SHAPE_3_X : SHAPE_3_Z;
            case 4 ->
                x ? SHAPE_4_X : SHAPE_4_Z;
            default ->
                x ? SHAPE_1_X : SHAPE_1_Z;
        };
    }

    @Override
    protected int getBookIndex(BlockState state, BlockPos pos, Vec3 location) {
        Direction dir = (Direction) state.m_61143_(FACING);
        double dist = dir.getAxis() == Direction.Axis.Z ? location.x - (double) pos.m_123341_() : location.z - (double) pos.m_123343_();
        if (dir == Direction.NORTH || dir == Direction.EAST) {
            dist = 1.0 - dist;
        }
        dist -= (1.0 - getVoxelShape(state, false).bounds().getXsize()) / 2.0;
        double f = dist / 0.25;
        return Mth.clamp((int) f, 0, (Integer) state.m_61143_(BOOKS) - 1);
    }
}