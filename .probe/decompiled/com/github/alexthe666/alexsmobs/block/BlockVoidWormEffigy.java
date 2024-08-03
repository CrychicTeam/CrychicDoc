package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockVoidWormEffigy extends Block {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    private static final VoxelShape UP_SHAPE = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 7.0, 16.0), Block.box(4.0, 6.0, 4.0, 12.0, 16.0, 12.0));

    private static final VoxelShape DOWN_SHAPE = Shapes.or(Block.box(0.0, 9.0, 0.0, 16.0, 16.0, 16.0), Block.box(4.0, 0.0, 4.0, 12.0, 10.0, 12.0));

    private static final VoxelShape SOUTH_SHAPE = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 7.0), Block.box(4.0, 4.0, 6.0, 12.0, 12.0, 16.0));

    private static final VoxelShape NORTH_SHAPE = Shapes.or(Block.box(0.0, 0.0, 9.0, 16.0, 16.0, 16.0), Block.box(4.0, 4.0, 0.0, 12.0, 12.0, 10.0));

    private static final VoxelShape EAST_SHAPE = Shapes.or(Block.box(0.0, 0.0, 0.0, 7.0, 16.0, 16.0), Block.box(6.0, 4.0, 4.0, 16.0, 12.0, 12.0));

    private static final VoxelShape WEST_SHAPE = Shapes.or(Block.box(9.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.box(0.0, 4.0, 4.0, 10.0, 12.0, 12.0));

    public BlockVoidWormEffigy() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PINK).requiresCorrectToolForDrops().strength(1.5F));
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.m_43719_());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(FACING, rot.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Override
    public VoxelShape getShape(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, CollisionContext collisionContext3) {
        return switch((Direction) blockState0.m_61143_(FACING)) {
            case NORTH ->
                NORTH_SHAPE;
            case SOUTH ->
                SOUTH_SHAPE;
            case EAST ->
                EAST_SHAPE;
            case WEST ->
                WEST_SHAPE;
            case UP ->
                UP_SHAPE;
            default ->
                DOWN_SHAPE;
        };
    }
}