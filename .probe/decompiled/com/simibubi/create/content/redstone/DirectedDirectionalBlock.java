package com.simibubi.create.content.redstone;

import com.simibubi.create.content.contraptions.ITransformableBlock;
import com.simibubi.create.content.contraptions.StructureTransform;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import javax.annotation.Nullable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.AttachFace;
import net.minecraft.world.level.block.state.properties.EnumProperty;

public class DirectedDirectionalBlock extends HorizontalDirectionalBlock implements IWrenchable, ITransformableBlock {

    public static final EnumProperty<AttachFace> TARGET = EnumProperty.create("target", AttachFace.class);

    public DirectedDirectionalBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(TARGET, AttachFace.WALL));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.m_7926_(pBuilder.add(TARGET, f_54117_));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction[] var2 = pContext.getNearestLookingDirections();
        int var3 = var2.length;
        byte var4 = 0;
        if (var4 < var3) {
            Direction direction = var2[var4];
            BlockState blockstate;
            if (direction.getAxis() == Direction.Axis.Y) {
                blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(TARGET, direction == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR)).m_61124_(f_54117_, pContext.m_8125_());
            } else {
                blockstate = (BlockState) ((BlockState) this.m_49966_().m_61124_(TARGET, AttachFace.WALL)).m_61124_(f_54117_, direction.getOpposite());
            }
            return blockstate;
        } else {
            return null;
        }
    }

    public static Direction getTargetDirection(BlockState pState) {
        switch((AttachFace) pState.m_61143_(TARGET)) {
            case CEILING:
                return Direction.UP;
            case FLOOR:
                return Direction.DOWN;
            default:
                return (Direction) pState.m_61143_(f_54117_);
        }
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        if (targetedFace.getAxis() == Direction.Axis.Y) {
            return IWrenchable.super.getRotatedBlockState(originalState, targetedFace);
        } else {
            Direction targetDirection = getTargetDirection(originalState);
            Direction newFacing = targetDirection.getClockWise(targetedFace.getAxis());
            if (targetedFace.getAxisDirection() == Direction.AxisDirection.NEGATIVE) {
                newFacing = newFacing.getOpposite();
            }
            return newFacing.getAxis() == Direction.Axis.Y ? (BlockState) originalState.m_61124_(TARGET, newFacing == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR) : (BlockState) ((BlockState) originalState.m_61124_(TARGET, AttachFace.WALL)).m_61124_(f_54117_, newFacing);
        }
    }

    @Override
    public BlockState transform(BlockState state, StructureTransform transform) {
        if (transform.mirror != null) {
            state = this.m_6943_(state, transform.mirror);
        }
        if (transform.rotationAxis == Direction.Axis.Y) {
            return this.m_6843_(state, transform.rotation);
        } else {
            Direction targetDirection = getTargetDirection(state);
            Direction newFacing = transform.rotateFacing(targetDirection);
            return newFacing.getAxis() == Direction.Axis.Y ? (BlockState) state.m_61124_(TARGET, newFacing == Direction.UP ? AttachFace.CEILING : AttachFace.FLOOR) : (BlockState) ((BlockState) state.m_61124_(TARGET, AttachFace.WALL)).m_61124_(f_54117_, newFacing);
        }
    }
}