package com.simibubi.create.content.logistics.tunnel;

import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeltTunnelShapes {

    private static VoxelShape block = Block.box(0.0, -5.0, 0.0, 16.0, 16.0, 16.0);

    private static VoxelShaper opening = VoxelShaper.forHorizontal(Block.box(2.0, -5.0, 14.0, 14.0, 10.0, 16.0), Direction.SOUTH);

    private static final VoxelShaper STRAIGHT = VoxelShaper.forHorizontalAxis(Shapes.join(block, Shapes.or(opening.get(Direction.SOUTH), opening.get(Direction.NORTH)), BooleanOp.NOT_SAME), Direction.Axis.Z);

    private static final VoxelShaper TEE = VoxelShaper.forHorizontal(Shapes.join(block, Shapes.or(opening.get(Direction.NORTH), opening.get(Direction.WEST), opening.get(Direction.EAST)), BooleanOp.NOT_SAME), Direction.SOUTH);

    private static final VoxelShape CROSS = Shapes.join(block, Shapes.or(opening.get(Direction.SOUTH), opening.get(Direction.NORTH), opening.get(Direction.WEST), opening.get(Direction.EAST)), BooleanOp.NOT_SAME);

    public static VoxelShape getShape(BlockState state) {
        BeltTunnelBlock.Shape shape = (BeltTunnelBlock.Shape) state.m_61143_(BeltTunnelBlock.SHAPE);
        Direction.Axis axis = (Direction.Axis) state.m_61143_(BeltTunnelBlock.HORIZONTAL_AXIS);
        if (shape == BeltTunnelBlock.Shape.CROSS) {
            return CROSS;
        } else if (BeltTunnelBlock.isStraight(state)) {
            return STRAIGHT.get(axis);
        } else if (shape == BeltTunnelBlock.Shape.T_LEFT) {
            return TEE.get(axis == Direction.Axis.Z ? Direction.EAST : Direction.NORTH);
        } else {
            return shape == BeltTunnelBlock.Shape.T_RIGHT ? TEE.get(axis == Direction.Axis.Z ? Direction.WEST : Direction.SOUTH) : Shapes.block();
        }
    }
}