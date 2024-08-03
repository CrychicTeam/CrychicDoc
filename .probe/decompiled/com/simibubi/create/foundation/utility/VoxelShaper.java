package com.simibubi.create.foundation.utility;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.apache.commons.lang3.mutable.MutableObject;

public class VoxelShaper {

    private Map<Direction, VoxelShape> shapes = new HashMap();

    public VoxelShape get(Direction direction) {
        return (VoxelShape) this.shapes.get(direction);
    }

    public VoxelShape get(Direction.Axis axis) {
        return (VoxelShape) this.shapes.get(axisAsFace(axis));
    }

    public static VoxelShaper forHorizontal(VoxelShape shape, Direction facing) {
        return forDirectionsWithRotation(shape, facing, Direction.Plane.HORIZONTAL, new VoxelShaper.HorizontalRotationValues());
    }

    public static VoxelShaper forHorizontalAxis(VoxelShape shape, Direction.Axis along) {
        return forDirectionsWithRotation(shape, axisAsFace(along), Arrays.asList(Direction.SOUTH, Direction.EAST), new VoxelShaper.HorizontalRotationValues());
    }

    public static VoxelShaper forDirectional(VoxelShape shape, Direction facing) {
        return forDirectionsWithRotation(shape, facing, Arrays.asList(Iterate.directions), new VoxelShaper.DefaultRotationValues());
    }

    public static VoxelShaper forAxis(VoxelShape shape, Direction.Axis along) {
        return forDirectionsWithRotation(shape, axisAsFace(along), Arrays.asList(Direction.SOUTH, Direction.EAST, Direction.UP), new VoxelShaper.DefaultRotationValues());
    }

    public VoxelShaper withVerticalShapes(VoxelShape upShape) {
        this.shapes.put(Direction.UP, upShape);
        this.shapes.put(Direction.DOWN, rotatedCopy(upShape, new Vec3(180.0, 0.0, 0.0)));
        return this;
    }

    public VoxelShaper withShape(VoxelShape shape, Direction facing) {
        this.shapes.put(facing, shape);
        return this;
    }

    public static Direction axisAsFace(Direction.Axis axis) {
        return Direction.get(Direction.AxisDirection.POSITIVE, axis);
    }

    protected static float horizontalAngleFromDirection(Direction direction) {
        return (float) ((Math.max(direction.get2DDataValue(), 0) & 3) * 90);
    }

    protected static VoxelShaper forDirectionsWithRotation(VoxelShape shape, Direction facing, Iterable<Direction> directions, Function<Direction, Vec3> rotationValues) {
        VoxelShaper voxelShaper = new VoxelShaper();
        for (Direction dir : directions) {
            voxelShaper.shapes.put(dir, rotate(shape, facing, dir, rotationValues));
        }
        return voxelShaper;
    }

    protected static VoxelShape rotate(VoxelShape shape, Direction from, Direction to, Function<Direction, Vec3> usingValues) {
        return from == to ? shape : rotatedCopy(shape, ((Vec3) usingValues.apply(from)).reverse().add((Vec3) usingValues.apply(to)));
    }

    protected static VoxelShape rotatedCopy(VoxelShape shape, Vec3 rotation) {
        if (rotation.equals(Vec3.ZERO)) {
            return shape;
        } else {
            MutableObject<VoxelShape> result = new MutableObject(Shapes.empty());
            Vec3 center = new Vec3(8.0, 8.0, 8.0);
            shape.forAllBoxes((x1, y1, z1, x2, y2, z2) -> {
                Vec3 v1 = new Vec3(x1, y1, z1).scale(16.0).subtract(center);
                Vec3 v2 = new Vec3(x2, y2, z2).scale(16.0).subtract(center);
                v1 = VecHelper.rotate(v1, (double) ((float) rotation.x), Direction.Axis.X);
                v1 = VecHelper.rotate(v1, (double) ((float) rotation.y), Direction.Axis.Y);
                v1 = VecHelper.rotate(v1, (double) ((float) rotation.z), Direction.Axis.Z).add(center);
                v2 = VecHelper.rotate(v2, (double) ((float) rotation.x), Direction.Axis.X);
                v2 = VecHelper.rotate(v2, (double) ((float) rotation.y), Direction.Axis.Y);
                v2 = VecHelper.rotate(v2, (double) ((float) rotation.z), Direction.Axis.Z).add(center);
                VoxelShape rotated = blockBox(v1, v2);
                result.setValue(Shapes.or((VoxelShape) result.getValue(), rotated));
            });
            return (VoxelShape) result.getValue();
        }
    }

    protected static VoxelShape blockBox(Vec3 v1, Vec3 v2) {
        return Block.box(Math.min(v1.x, v2.x), Math.min(v1.y, v2.y), Math.min(v1.z, v2.z), Math.max(v1.x, v2.x), Math.max(v1.y, v2.y), Math.max(v1.z, v2.z));
    }

    protected static class DefaultRotationValues implements Function<Direction, Vec3> {

        public Vec3 apply(Direction direction) {
            return new Vec3(direction == Direction.UP ? 0.0 : (double) (Direction.Plane.VERTICAL.test(direction) ? 180 : 90), (double) (-VoxelShaper.horizontalAngleFromDirection(direction)), 0.0);
        }
    }

    protected static class HorizontalRotationValues implements Function<Direction, Vec3> {

        public Vec3 apply(Direction direction) {
            return new Vec3(0.0, (double) (-VoxelShaper.horizontalAngleFromDirection(direction)), 0.0);
        }
    }
}