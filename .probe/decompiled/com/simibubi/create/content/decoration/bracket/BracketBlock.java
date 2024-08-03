package com.simibubi.create.content.decoration.bracket;

import com.simibubi.create.content.fluids.FluidPropagator;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.content.kinetics.simpleRelays.AbstractSimpleShaftBlock;
import com.simibubi.create.content.kinetics.simpleRelays.CogWheelBlock;
import com.simibubi.create.foundation.block.WrenchableDirectionalBlock;
import com.simibubi.create.foundation.utility.Lang;
import java.util.Optional;
import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class BracketBlock extends WrenchableDirectionalBlock {

    public static final BooleanProperty AXIS_ALONG_FIRST_COORDINATE = DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE;

    public static final EnumProperty<BracketBlock.BracketType> TYPE = EnumProperty.create("type", BracketBlock.BracketType.class);

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder.add(AXIS_ALONG_FIRST_COORDINATE).add(TYPE));
    }

    public BracketBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public Optional<BlockState> getSuitableBracket(BlockState blockState, Direction direction) {
        return blockState.m_60734_() instanceof AbstractSimpleShaftBlock ? this.getSuitableBracket((Direction.Axis) blockState.m_61143_(RotatedPillarKineticBlock.AXIS), direction, blockState.m_60734_() instanceof CogWheelBlock ? BracketBlock.BracketType.COG : BracketBlock.BracketType.SHAFT) : this.getSuitableBracket(FluidPropagator.getStraightPipeAxis(blockState), direction, BracketBlock.BracketType.PIPE);
    }

    private Optional<BlockState> getSuitableBracket(Direction.Axis targetBlockAxis, Direction direction, BracketBlock.BracketType type) {
        Direction.Axis axis = direction.getAxis();
        if (targetBlockAxis != null && targetBlockAxis != axis) {
            boolean alongFirst = axis != Direction.Axis.Z ? targetBlockAxis == Direction.Axis.Z : targetBlockAxis == Direction.Axis.Y;
            return Optional.of((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(TYPE, type)).m_61124_(f_52588_, direction)).m_61124_(AXIS_ALONG_FIRST_COORDINATE, !alongFirst));
        } else {
            return Optional.empty();
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        if (rot.ordinal() % 2 == 1) {
            state = (BlockState) state.m_61122_(AXIS_ALONG_FIRST_COORDINATE);
        }
        return super.rotate(state, rot);
    }

    public static enum BracketType implements StringRepresentable {

        PIPE, COG, SHAFT;

        @Override
        public String getSerializedName() {
            return Lang.asId(this.name());
        }
    }
}