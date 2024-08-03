package com.simibubi.create.content.kinetics.belt;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BeltShapes {

    private static final VoxelShape SLOPE_DESC_PART = makeSlopePart(false);

    private static final VoxelShape SLOPE_ASC_PART = makeSlopePart(true);

    private static final VoxelShape SIDEWAYS_FULL_PART = makeSidewaysFull();

    private static final VoxelShape SIDEWAYS_END_PART = makeSidewaysEnding();

    private static final VoxelShape FLAT_FULL_PART = makeFlatFull();

    private static final VoxelShape FLAT_END_PART = makeFlatEnding();

    private static final VoxelShape SOUTH_MASK = Block.box(0.0, -5.0, 8.0, 16.0, 21.0, 16.0);

    private static final VoxelShape NORTH_MASK = Block.box(0.0, -5.0, 0.0, 16.0, 21.0, 8.0);

    private static final VoxelShaper VERTICAL_FULL = BeltShapes.VerticalBeltShaper.make(FLAT_FULL_PART);

    private static final VoxelShaper VERTICAL_END = BeltShapes.VerticalBeltShaper.make(compose(FLAT_END_PART, FLAT_FULL_PART));

    private static final VoxelShaper VERTICAL_START = BeltShapes.VerticalBeltShaper.make(compose(FLAT_FULL_PART, FLAT_END_PART));

    private static final VoxelShaper FLAT_FULL = VoxelShaper.forHorizontalAxis(FLAT_FULL_PART, Direction.Axis.Z);

    private static final VoxelShaper FLAT_END = VoxelShaper.forHorizontal(compose(FLAT_END_PART, FLAT_FULL_PART), Direction.SOUTH);

    private static final VoxelShaper FLAT_START = VoxelShaper.forHorizontal(compose(FLAT_FULL_PART, FLAT_END_PART), Direction.SOUTH);

    private static final VoxelShaper SIDE_FULL = VoxelShaper.forHorizontalAxis(SIDEWAYS_FULL_PART, Direction.Axis.Z);

    private static final VoxelShaper SIDE_END = VoxelShaper.forHorizontal(compose(SIDEWAYS_END_PART, SIDEWAYS_FULL_PART), Direction.SOUTH);

    private static final VoxelShaper SIDE_START = VoxelShaper.forHorizontal(compose(SIDEWAYS_FULL_PART, SIDEWAYS_END_PART), Direction.SOUTH);

    private static final VoxelShaper SLOPE_DESC = VoxelShaper.forHorizontal(SLOPE_DESC_PART, Direction.SOUTH);

    private static final VoxelShaper SLOPE_ASC = VoxelShaper.forHorizontal(SLOPE_ASC_PART, Direction.SOUTH);

    private static final VoxelShaper SLOPE_DESC_END = VoxelShaper.forHorizontal(compose(FLAT_END_PART, SLOPE_DESC_PART), Direction.SOUTH);

    private static final VoxelShaper SLOPE_DESC_START = VoxelShaper.forHorizontal(compose(SLOPE_DESC_PART, FLAT_END_PART), Direction.SOUTH);

    private static final VoxelShaper SLOPE_ASC_END = VoxelShaper.forHorizontal(compose(FLAT_END_PART, SLOPE_ASC_PART), Direction.SOUTH);

    private static final VoxelShaper SLOPE_ASC_START = VoxelShaper.forHorizontal(compose(SLOPE_ASC_PART, FLAT_END_PART), Direction.SOUTH);

    private static final VoxelShaper PARTIAL_CASING = VoxelShaper.forHorizontal(Block.box(0.0, 0.0, 5.0, 16.0, 11.0, 16.0), Direction.SOUTH);

    static Map<BlockState, VoxelShape> cache = new HashMap();

    static Map<BlockState, VoxelShape> collisionCache = new HashMap();

    private static VoxelShape compose(VoxelShape southPart, VoxelShape northPart) {
        return Shapes.or(Shapes.joinUnoptimized(SOUTH_MASK, southPart, BooleanOp.AND), Shapes.joinUnoptimized(NORTH_MASK, northPart, BooleanOp.AND));
    }

    private static VoxelShape makeSlopePart(boolean ascendingInstead) {
        VoxelShape slice = Block.box(1.0, 0.0, 15.0, 15.0, 11.0, 16.0);
        VoxelShape result = Shapes.empty();
        for (int i = 0; i < 16; i++) {
            int yOffset = ascendingInstead ? 10 - i : i - 5;
            result = Shapes.or(result, slice.move(0.0, (double) ((float) yOffset / 16.0F), (double) ((float) (-i) / 16.0F)));
        }
        return result;
    }

    private static VoxelShape makeFlatEnding() {
        return Shapes.or(Block.box(1.0, 4.0, 0.0, 15.0, 12.0, 16.0), Block.box(1.0, 3.0, 1.0, 15.0, 13.0, 15.0));
    }

    private static VoxelShape makeFlatFull() {
        return Block.box(1.0, 3.0, 0.0, 15.0, 13.0, 16.0);
    }

    private static VoxelShape makeSidewaysEnding() {
        return Shapes.or(Block.box(4.0, 1.0, 0.0, 12.0, 15.0, 16.0), Block.box(3.0, 1.0, 1.0, 13.0, 15.0, 15.0));
    }

    private static VoxelShape makeSidewaysFull() {
        return Block.box(3.0, 1.0, 0.0, 13.0, 15.0, 16.0);
    }

    public static VoxelShape getShape(BlockState state) {
        if (cache.containsKey(state)) {
            return (VoxelShape) cache.get(state);
        } else {
            VoxelShape createdShape = Shapes.or(getBeltShape(state), getCasingShape(state));
            cache.put(state, createdShape);
            return createdShape;
        }
    }

    public static VoxelShape getCollisionShape(BlockState state) {
        if (collisionCache.containsKey(state)) {
            return (VoxelShape) collisionCache.get(state);
        } else {
            VoxelShape createdShape = Shapes.joinUnoptimized(AllShapes.BELT_COLLISION_MASK, getShape(state), BooleanOp.AND);
            collisionCache.put(state, createdShape);
            return createdShape;
        }
    }

    private static VoxelShape getBeltShape(BlockState state) {
        Direction facing = (Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING);
        Direction.Axis axis = facing.getAxis();
        BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
        BeltSlope slope = (BeltSlope) state.m_61143_(BeltBlock.SLOPE);
        if (slope == BeltSlope.VERTICAL) {
            return part != BeltPart.MIDDLE && part != BeltPart.PULLEY ? (part == BeltPart.START ? VERTICAL_START : VERTICAL_END).get(facing) : VERTICAL_FULL.get(axis);
        } else if (slope == BeltSlope.HORIZONTAL) {
            return part != BeltPart.MIDDLE && part != BeltPart.PULLEY ? (part == BeltPart.START ? FLAT_START : FLAT_END).get(facing) : FLAT_FULL.get(axis);
        } else if (slope == BeltSlope.SIDEWAYS) {
            return part != BeltPart.MIDDLE && part != BeltPart.PULLEY ? (part == BeltPart.START ? SIDE_START : SIDE_END).get(facing) : SIDE_FULL.get(axis);
        } else if (part == BeltPart.MIDDLE || part == BeltPart.PULLEY) {
            return (slope == BeltSlope.DOWNWARD ? SLOPE_DESC : SLOPE_ASC).get(facing);
        } else if (part == BeltPart.START) {
            return (slope == BeltSlope.DOWNWARD ? SLOPE_DESC_START : SLOPE_ASC_START).get(facing);
        } else {
            return part == BeltPart.END ? (slope == BeltSlope.DOWNWARD ? SLOPE_DESC_END : SLOPE_ASC_END).get(facing) : Shapes.empty();
        }
    }

    private static VoxelShape getCasingShape(BlockState state) {
        if (!(Boolean) state.m_61143_(BeltBlock.CASING)) {
            return Shapes.empty();
        } else {
            Direction facing = (Direction) state.m_61143_(BeltBlock.HORIZONTAL_FACING);
            BeltPart part = (BeltPart) state.m_61143_(BeltBlock.PART);
            BeltSlope slope = (BeltSlope) state.m_61143_(BeltBlock.SLOPE);
            if (slope == BeltSlope.VERTICAL) {
                return Shapes.empty();
            } else if (slope == BeltSlope.SIDEWAYS) {
                return Shapes.empty();
            } else if (slope == BeltSlope.HORIZONTAL) {
                return AllShapes.CASING_11PX.get(Direction.UP);
            } else if (part != BeltPart.MIDDLE && part != BeltPart.PULLEY) {
                if (part == BeltPart.START) {
                    return slope == BeltSlope.UPWARD ? AllShapes.CASING_11PX.get(Direction.UP) : PARTIAL_CASING.get(facing.getOpposite());
                } else if (part == BeltPart.END) {
                    return slope == BeltSlope.DOWNWARD ? AllShapes.CASING_11PX.get(Direction.UP) : PARTIAL_CASING.get(facing);
                } else {
                    return Shapes.block();
                }
            } else {
                return PARTIAL_CASING.get(slope == BeltSlope.UPWARD ? facing : facing.getOpposite());
            }
        }
    }

    private static class VerticalBeltShaper extends VoxelShaper {

        public static VoxelShaper make(VoxelShape southBeltShape) {
            return forDirectionsWithRotation(rotatedCopy(southBeltShape, new Vec3(-90.0, 0.0, 0.0)), Direction.SOUTH, Direction.Plane.HORIZONTAL, direction -> new Vec3(direction.getAxisDirection() == Direction.AxisDirection.POSITIVE ? 0.0 : 180.0, (double) (-direction.toYRot()), 0.0));
        }
    }
}