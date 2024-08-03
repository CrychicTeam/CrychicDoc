package com.simibubi.create.content.kinetics.clock;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CuckooClockBlock extends HorizontalKineticBlock implements IBE<CuckooClockBlockEntity> {

    private boolean mysterious;

    public static CuckooClockBlock regular(BlockBehaviour.Properties properties) {
        return new CuckooClockBlock(false, properties);
    }

    public static CuckooClockBlock mysterious(BlockBehaviour.Properties properties) {
        return new CuckooClockBlock(true, properties);
    }

    protected CuckooClockBlock(boolean mysterious, BlockBehaviour.Properties properties) {
        super(properties);
        this.mysterious = mysterious;
    }

    @Override
    public VoxelShape getShape(BlockState p_220053_1_, BlockGetter p_220053_2_, BlockPos p_220053_3_, CollisionContext p_220053_4_) {
        return AllShapes.CUCKOO_CLOCK;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Direction preferred = this.getPreferredHorizontalFacing(context);
        return preferred != null ? (BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, preferred.getOpposite()) : (BlockState) this.m_49966_().m_61124_(HORIZONTAL_FACING, context.m_8125_().getOpposite());
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return face == ((Direction) state.m_61143_(HORIZONTAL_FACING)).getOpposite();
    }

    public static boolean containsSurprise(BlockState state) {
        Block block = state.m_60734_();
        return block instanceof CuckooClockBlock && ((CuckooClockBlock) block).mysterious;
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getAxis();
    }

    @Override
    public boolean isPathfindable(BlockState state, BlockGetter reader, BlockPos pos, PathComputationType type) {
        return false;
    }

    @Override
    public Class<CuckooClockBlockEntity> getBlockEntityClass() {
        return CuckooClockBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends CuckooClockBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends CuckooClockBlockEntity>) AllBlockEntityTypes.CUCKOO_CLOCK.get();
    }
}