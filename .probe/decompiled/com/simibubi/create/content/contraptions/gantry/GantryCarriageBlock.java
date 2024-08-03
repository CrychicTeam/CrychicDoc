package com.simibubi.create.content.contraptions.gantry;

import com.simibubi.create.AllBlockEntityTypes;
import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.IRotate;
import com.simibubi.create.content.kinetics.gantry.GantryShaftBlock;
import com.simibubi.create.foundation.block.IBE;
import com.simibubi.create.foundation.utility.Iterate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class GantryCarriageBlock extends DirectionalAxisKineticBlock implements IBE<GantryCarriageBlockEntity> {

    public GantryCarriageBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader world, BlockPos pos) {
        Direction direction = (Direction) state.m_61143_(FACING);
        BlockState shaft = world.m_8055_(pos.relative(direction.getOpposite()));
        return AllBlocks.GANTRY_SHAFT.has(shaft) && ((Direction) shaft.m_61143_(GantryShaftBlock.FACING)).getAxis() != direction.getAxis();
    }

    @Override
    public void updateIndirectNeighbourShapes(BlockState stateIn, LevelAccessor worldIn, BlockPos pos, int flags, int count) {
        super.m_7742_(stateIn, worldIn, pos, flags, count);
        this.withBlockEntityDo(worldIn, pos, GantryCarriageBlockEntity::checkValidGantryShaft);
    }

    @Override
    public void onPlace(BlockState state, Level worldIn, BlockPos pos, BlockState oldState, boolean isMoving) {
        super.m_6807_(state, worldIn, pos, oldState, isMoving);
    }

    @Override
    protected Direction getFacingForPlacement(BlockPlaceContext context) {
        return context.m_43719_();
    }

    @Override
    public InteractionResult use(BlockState state, Level worldIn, BlockPos pos, Player player, InteractionHand handIn, BlockHitResult hit) {
        if (!player.mayBuild() || player.m_6144_()) {
            return InteractionResult.PASS;
        } else if (player.m_21120_(handIn).isEmpty()) {
            this.withBlockEntityDo(worldIn, pos, be -> be.checkValidGantryShaft());
            return InteractionResult.SUCCESS;
        } else {
            return InteractionResult.PASS;
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState stateForPlacement = super.getStateForPlacement(context);
        Direction opposite = ((Direction) stateForPlacement.m_61143_(FACING)).getOpposite();
        return this.cycleAxisIfNecessary(stateForPlacement, opposite, context.m_43725_().getBlockState(context.getClickedPos().relative(opposite)));
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block p_220069_4_, BlockPos updatePos, boolean p_220069_6_) {
        if (updatePos.equals(pos.relative(((Direction) state.m_61143_(FACING)).getOpposite())) && !this.canSurvive(state, world, pos)) {
            world.m_46961_(pos, true);
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState otherState, LevelAccessor world, BlockPos pos, BlockPos p_196271_6_) {
        return state.m_61143_(FACING) != direction.getOpposite() ? state : this.cycleAxisIfNecessary(state, direction, otherState);
    }

    protected BlockState cycleAxisIfNecessary(BlockState state, Direction direction, BlockState otherState) {
        if (!AllBlocks.GANTRY_SHAFT.has(otherState)) {
            return state;
        } else if (((Direction) otherState.m_61143_(GantryShaftBlock.FACING)).getAxis() == direction.getAxis()) {
            return state;
        } else {
            return isValidGantryShaftAxis(state, otherState) ? state : (BlockState) state.m_61122_(AXIS_ALONG_FIRST_COORDINATE);
        }
    }

    public static boolean isValidGantryShaftAxis(BlockState pinionState, BlockState gantryState) {
        return getValidGantryShaftAxis(pinionState) == ((Direction) gantryState.m_61143_(GantryShaftBlock.FACING)).getAxis();
    }

    public static Direction.Axis getValidGantryShaftAxis(BlockState state) {
        if (!(state.m_60734_() instanceof GantryCarriageBlock)) {
            return Direction.Axis.Y;
        } else {
            IRotate block = (IRotate) state.m_60734_();
            Direction.Axis rotationAxis = block.getRotationAxis(state);
            Direction.Axis facingAxis = ((Direction) state.m_61143_(FACING)).getAxis();
            for (Direction.Axis axis : Iterate.axes) {
                if (axis != rotationAxis && axis != facingAxis) {
                    return axis;
                }
            }
            return Direction.Axis.Y;
        }
    }

    public static Direction.Axis getValidGantryPinionAxis(BlockState state, Direction.Axis shaftAxis) {
        Direction.Axis facingAxis = ((Direction) state.m_61143_(FACING)).getAxis();
        for (Direction.Axis axis : Iterate.axes) {
            if (axis != shaftAxis && axis != facingAxis) {
                return axis;
            }
        }
        return Direction.Axis.Y;
    }

    @Override
    public Class<GantryCarriageBlockEntity> getBlockEntityClass() {
        return GantryCarriageBlockEntity.class;
    }

    @Override
    public BlockEntityType<? extends GantryCarriageBlockEntity> getBlockEntityType() {
        return (BlockEntityType<? extends GantryCarriageBlockEntity>) AllBlockEntityTypes.GANTRY_PINION.get();
    }
}