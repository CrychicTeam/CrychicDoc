package com.github.alexthe666.alexsmobs.block;

import com.github.alexthe666.alexsmobs.entity.EntityGust;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.Vec3;

public class BlockGustmaker extends Block {

    public static final DirectionProperty FACING = DirectionalBlock.FACING;

    public static final BooleanProperty TRIGGERED = BlockStateProperties.TRIGGERED;

    public BlockGustmaker() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(1.5F));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(FACING, Direction.NORTH)).m_61124_(TRIGGERED, false));
    }

    public static Vec3 getDispensePosition(BlockPos coords, Direction dir) {
        double d0 = (double) coords.m_123341_() + 0.5 + 0.7 * (double) dir.getStepX();
        double d1 = (double) coords.m_123342_() + 0.15 + 0.7 * (double) dir.getStepY();
        double d2 = (double) coords.m_123343_() + 0.5 + 0.7 * (double) dir.getStepZ();
        return new Vec3(d0, d1, d2);
    }

    @Override
    public void neighborChanged(BlockState state, Level worldIn, BlockPos pos, Block blockIn, BlockPos fromPos, boolean isMoving) {
        this.tickGustmaker(state, worldIn, pos, false);
    }

    @Override
    public void tick(BlockState state, ServerLevel worldIn, BlockPos pos, RandomSource rand) {
        this.tickGustmaker(state, worldIn, pos, true);
    }

    public void tickGustmaker(BlockState state, Level worldIn, BlockPos pos, boolean tickOff) {
        boolean flag = worldIn.m_276867_(pos) || worldIn.m_276867_(pos.below()) || worldIn.m_276867_(pos.above());
        boolean flag1 = (Boolean) state.m_61143_(TRIGGERED);
        if (flag && !flag1) {
            if (worldIn.isLoaded(pos)) {
                Vec3 dispensePosition = getDispensePosition(pos, (Direction) state.m_61143_(FACING));
                Vec3 gustDir = Vec3.atLowerCornerOf(((Direction) state.m_61143_(FACING)).getNormal()).multiply(0.1, 0.1, 0.1);
                EntityGust gust = new EntityGust(worldIn);
                gust.setGustDir((float) gustDir.x, (float) gustDir.y, (float) gustDir.z);
                gust.m_6034_(dispensePosition.x, dispensePosition.y, dispensePosition.z);
                if (((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.Y) {
                    gust.setVertical(true);
                }
                if (!worldIn.isClientSide) {
                    worldIn.m_7967_(gust);
                }
            }
            worldIn.setBlock(pos, (BlockState) state.m_61124_(TRIGGERED, true), 2);
            worldIn.m_186460_(pos, this, 20);
        } else if (flag1 && tickOff) {
            worldIn.m_186460_(pos, this, 20);
            worldIn.setBlock(pos, (BlockState) state.m_61124_(TRIGGERED, false), 2);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(FACING, context.getNearestLookingDirection().getOpposite());
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
        builder.add(FACING, TRIGGERED);
    }
}