package com.simibubi.create.content.contraptions.elevator;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllShapes;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.foundation.block.IBE;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ElevatorPulleyBlock extends HorizontalKineticBlock implements IBE<ElevatorPulleyBlockEntity> {

    public ElevatorPulleyBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild()) {
            return InteractionResult.FAIL;
        } else if (player.m_6144_()) {
            return InteractionResult.FAIL;
        } else if (!player.m_21120_(handIn).isEmpty()) {
            return InteractionResult.PASS;
        } else {
            return worldIn.isClientSide ? InteractionResult.SUCCESS : this.onBlockEntityUse(worldIn, pos, be -> {
                be.clicked();
                return InteractionResult.SUCCESS;
            });
        }
    }

    @Override
    public BlockEntityType<? extends ElevatorPulleyBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends ElevatorPulleyBlockEntity>) AllBlockEntityTypes.ELEVATOR_PULLEY.get();
    }

    @Override
    public Direction.Axis getRotationAxis(BlockState state) {
        return ((Direction) state.m_61143_(HORIZONTAL_FACING)).getClockWise().getAxis();
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter worldIn, BlockPos pos, CollisionContext context) {
        return AllShapes.ELEVATOR_PULLEY.get((Direction) state.m_61143_(HORIZONTAL_FACING));
    }

    @Override
    public boolean hasShaftTowards(LevelReader world, BlockPos pos, BlockState state, Direction face) {
        return this.getRotationAxis(state) == face.getAxis();
    }

    @Override
    public Class<ElevatorPulleyBlockEntity> getBlockEntityClass() {
        return ElevatorPulleyBlockEntity.class;
    }
}