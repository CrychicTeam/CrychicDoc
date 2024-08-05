package com.simibubi.create.content.contraptions.chassis;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.AllSpriteShifts;
import com.simibubi.create.foundation.block.connected.CTSpriteShiftEntry;
import com.simibubi.create.foundation.block.connected.ConnectedTextureBehaviour;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import org.jetbrains.annotations.Nullable;

public class LinearChassisBlock extends AbstractChassisBlock {

    public static final BooleanProperty STICKY_TOP = BooleanProperty.create("sticky_top");

    public static final BooleanProperty STICKY_BOTTOM = BooleanProperty.create("sticky_bottom");

    public LinearChassisBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(STICKY_TOP, false)).m_61124_(STICKY_BOTTOM, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(STICKY_TOP, STICKY_BOTTOM);
        super.m_7926_(builder);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockPos placedOnPos = context.getClickedPos().relative(context.m_43719_().getOpposite());
        BlockState blockState = context.m_43725_().getBlockState(placedOnPos);
        if (context.m_43723_() != null && context.m_43723_().m_6144_()) {
            return super.m_5573_(context);
        } else {
            return isChassis(blockState) ? (BlockState) this.m_49966_().m_61124_(f_55923_, (Direction.Axis) blockState.m_61143_(f_55923_)) : (BlockState) this.m_49966_().m_61124_(f_55923_, context.getNearestLookingDirection().getAxis());
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction side, BlockState other, LevelAccessor p_196271_4_, BlockPos p_196271_5_, BlockPos p_196271_6_) {
        BooleanProperty property = this.getGlueableSide(state, side);
        return property != null && sameKind(state, other) && state.m_61143_(f_55923_) == other.m_61143_(f_55923_) ? (BlockState) state.m_61124_(property, false) : state;
    }

    @Override
    public BooleanProperty getGlueableSide(BlockState state, Direction face) {
        if (face.getAxis() != state.m_61143_(f_55923_)) {
            return null;
        } else {
            return face.getAxisDirection() == Direction.AxisDirection.POSITIVE ? STICKY_TOP : STICKY_BOTTOM;
        }
    }

    @Override
    protected boolean glueAllowedOnSide(BlockGetter world, BlockPos pos, BlockState state, Direction side) {
        BlockState other = world.getBlockState(pos.relative(side));
        return !sameKind(other, state) || state.m_61143_(f_55923_) != other.m_61143_(f_55923_);
    }

    public static boolean isChassis(BlockState state) {
        return AllBlocks.LINEAR_CHASSIS.has(state) || AllBlocks.SECONDARY_LINEAR_CHASSIS.has(state);
    }

    public static boolean sameKind(BlockState state1, BlockState state2) {
        return state1.m_60734_() == state2.m_60734_();
    }

    public static class ChassisCTBehaviour extends ConnectedTextureBehaviour.Base {

        @Override
        public CTSpriteShiftEntry getShift(BlockState state, Direction direction, @Nullable TextureAtlasSprite sprite) {
            Block block = state.m_60734_();
            BooleanProperty glueableSide = ((LinearChassisBlock) block).getGlueableSide(state, direction);
            if (glueableSide == null) {
                return AllBlocks.LINEAR_CHASSIS.has(state) ? AllSpriteShifts.CHASSIS_SIDE : AllSpriteShifts.SECONDARY_CHASSIS_SIDE;
            } else {
                return state.m_61143_(glueableSide) ? AllSpriteShifts.CHASSIS_STICKY : AllSpriteShifts.CHASSIS;
            }
        }

        @Override
        protected Direction getUpDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
            return face.getAxis() == axis ? super.getUpDirection(reader, pos, state, face) : Direction.get(Direction.AxisDirection.POSITIVE, axis);
        }

        @Override
        protected Direction getRightDirection(BlockAndTintGetter reader, BlockPos pos, BlockState state, Direction face) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
            return axis != face.getAxis() && axis.isHorizontal() ? (face.getAxis().isHorizontal() ? Direction.DOWN : (axis == Direction.Axis.X ? Direction.NORTH : Direction.EAST)) : super.getRightDirection(reader, pos, state, face);
        }

        @Override
        protected boolean reverseUVsHorizontally(BlockState state, Direction face) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
            boolean side = face.getAxis() != axis;
            return side && axis == Direction.Axis.X && face.getAxis().isHorizontal() ? true : super.reverseUVsHorizontally(state, face);
        }

        @Override
        protected boolean reverseUVsVertically(BlockState state, Direction face) {
            return super.reverseUVsVertically(state, face);
        }

        @Override
        public boolean reverseUVs(BlockState state, Direction face) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
            boolean end = face.getAxis() == axis;
            if (end && axis.isHorizontal() && face.getAxisDirection() == Direction.AxisDirection.POSITIVE) {
                return true;
            } else {
                return !end && axis.isHorizontal() && face == Direction.DOWN ? true : super.reverseUVs(state, face);
            }
        }

        @Override
        public boolean connectsTo(BlockState state, BlockState other, BlockAndTintGetter reader, BlockPos pos, BlockPos otherPos, Direction face) {
            Direction.Axis axis = (Direction.Axis) state.m_61143_(RotatedPillarBlock.AXIS);
            boolean superConnect = face.getAxis() == axis ? super.connectsTo(state, other, reader, pos, otherPos, face) : LinearChassisBlock.sameKind(state, other);
            return superConnect && axis == other.m_61143_(RotatedPillarBlock.AXIS);
        }
    }
}