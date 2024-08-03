package snownee.kiwi.util;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.UnaryOperator;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.joml.Vector3d;

public final class VoxelUtil {

    private static final Vector3d fromOrigin = new Vector3d(-0.5, -0.5, -0.5);

    private VoxelUtil() {
    }

    public static AABB rotate(AABB box, Direction side) {
        switch(side) {
            case DOWN:
                return box;
            case UP:
                return new AABB(box.minX, -box.minY, -box.minZ, box.maxX, -box.maxY, -box.maxZ);
            case NORTH:
                return new AABB(box.minX, -box.minZ, box.minY, box.maxX, -box.maxZ, box.maxY);
            case SOUTH:
                return new AABB(-box.minX, -box.minZ, -box.minY, -box.maxX, -box.maxZ, -box.maxY);
            case WEST:
                return new AABB(box.minY, -box.minZ, -box.minX, box.maxY, -box.maxZ, -box.maxX);
            case EAST:
                return new AABB(-box.minY, -box.minZ, box.minX, -box.maxY, -box.maxZ, box.maxX);
            default:
                return box;
        }
    }

    public static AABB rotate(AABB box, Rotation rotation) {
        switch(rotation) {
            case NONE:
                return box;
            case CLOCKWISE_90:
                return new AABB(-box.minZ, box.minY, box.minX, -box.maxZ, box.maxY, box.maxX);
            case CLOCKWISE_180:
                return new AABB(-box.minX, box.minY, -box.minZ, -box.maxX, box.maxY, -box.maxZ);
            case COUNTERCLOCKWISE_90:
                return new AABB(box.minZ, box.minY, -box.minX, box.maxZ, box.maxY, -box.maxX);
            default:
                return box;
        }
    }

    public static AABB rotateHorizontal(AABB box, Direction side) {
        switch(side) {
            case NORTH:
                return rotate(box, Rotation.NONE);
            case SOUTH:
                return rotate(box, Rotation.CLOCKWISE_180);
            case WEST:
                return rotate(box, Rotation.COUNTERCLOCKWISE_90);
            case EAST:
                return rotate(box, Rotation.CLOCKWISE_90);
            default:
                return box;
        }
    }

    public static VoxelShape rotate(VoxelShape shape, Direction side) {
        return !shape.isEmpty() && shape != Shapes.block() ? rotate(shape, box -> rotate(box, side)) : shape;
    }

    public static VoxelShape rotate(VoxelShape shape, Rotation rotation) {
        return rotate(shape, box -> rotate(box, rotation));
    }

    public static VoxelShape rotateHorizontal(VoxelShape shape, Direction side) {
        return !shape.isEmpty() && shape != Shapes.block() ? rotate(shape, box -> rotateHorizontal(box, side)) : shape;
    }

    public static VoxelShape rotate(VoxelShape shape, UnaryOperator<AABB> rotateFunction) {
        List<VoxelShape> rotatedPieces = new ArrayList();
        for (AABB sourceBoundingBox : shape.toAabbs()) {
            rotatedPieces.add(Shapes.create(((AABB) rotateFunction.apply(sourceBoundingBox.move(fromOrigin.x, fromOrigin.y, fromOrigin.z))).move(-fromOrigin.x, -fromOrigin.z, -fromOrigin.z)));
        }
        return combine(rotatedPieces);
    }

    public static VoxelShape combine(VoxelShape... shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape combine(Collection<VoxelShape> shapes) {
        return batchCombine(Shapes.empty(), BooleanOp.OR, true, shapes);
    }

    public static VoxelShape exclude(VoxelShape... shapes) {
        return batchCombine(Shapes.block(), BooleanOp.ONLY_FIRST, true, shapes);
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, Collection<VoxelShape> shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }

    public static VoxelShape batchCombine(VoxelShape initial, BooleanOp function, boolean simplify, VoxelShape... shapes) {
        VoxelShape combinedShape = initial;
        for (VoxelShape shape : shapes) {
            combinedShape = Shapes.joinUnoptimized(combinedShape, shape, function);
        }
        return simplify ? combinedShape.optimize() : combinedShape;
    }

    public static void setShape(VoxelShape shape, VoxelShape[] dest, boolean verticalAxis) {
        setShape(shape, dest, verticalAxis, false);
    }

    public static void setShape(VoxelShape shape, VoxelShape[] dest, boolean verticalAxis, boolean invert) {
        Direction[] dirs = verticalAxis ? EnumUtil.DIRECTIONS : EnumUtil.HORIZONTAL_DIRECTIONS;
        for (Direction side : dirs) {
            dest[verticalAxis ? side.ordinal() : side.ordinal() - 2] = verticalAxis ? rotate(shape, invert ? side.getOpposite() : side) : rotateHorizontal(shape, side);
        }
    }

    public static void setShape(VoxelShape shape, VoxelShape[] dest) {
        setShape(shape, dest, false, false);
    }

    public static boolean isIsotropicHorizontally(VoxelShape shape) {
        if (!shape.isEmpty() && shape != Shapes.block()) {
            VoxelShape rotated = rotateHorizontal(shape, Direction.EAST);
            return !Shapes.joinIsNotEmpty(shape, rotated, BooleanOp.ONLY_FIRST);
        } else {
            return true;
        }
    }
}