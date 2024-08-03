package me.jellysquid.mods.lithium.common.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.DiscreteVoxelShape;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

public class VoxelShapeMatchesAnywhere {

    public static void cuboidMatchesAnywhere(VoxelShape shapeA, VoxelShape shapeB, BooleanOp predicate, CallbackInfoReturnable<Boolean> cir) {
        if (shapeA instanceof VoxelShapeSimpleCube && shapeB instanceof VoxelShapeSimpleCube) {
            if (((VoxelShapeSimpleCube) shapeA).isTiny || ((VoxelShapeSimpleCube) shapeB).isTiny) {
                return;
            }
            if (predicate.apply(true, true)) {
                if (intersects((VoxelShapeSimpleCube) shapeA, (VoxelShapeSimpleCube) shapeB)) {
                    cir.setReturnValue(true);
                    return;
                }
                cir.setReturnValue(predicate.apply(true, false) || predicate.apply(false, true));
            } else {
                if (predicate.apply(true, false) && exceedsShape((VoxelShapeSimpleCube) shapeA, (VoxelShapeSimpleCube) shapeB)) {
                    cir.setReturnValue(true);
                    return;
                }
                if (predicate.apply(false, true) && exceedsShape((VoxelShapeSimpleCube) shapeB, (VoxelShapeSimpleCube) shapeA)) {
                    cir.setReturnValue(true);
                    return;
                }
            }
            cir.setReturnValue(false);
        } else if (shapeA instanceof VoxelShapeSimpleCube || shapeB instanceof VoxelShapeSimpleCube) {
            VoxelShapeSimpleCube simpleCube = (VoxelShapeSimpleCube) (shapeA instanceof VoxelShapeSimpleCube ? shapeA : shapeB);
            VoxelShape otherShape = simpleCube == shapeA ? shapeB : shapeA;
            if (simpleCube.isTiny || isTiny(otherShape)) {
                return;
            }
            boolean acceptSimpleCubeAlone = predicate.apply(shapeA == simpleCube, shapeB == simpleCube);
            if (acceptSimpleCubeAlone && exceedsCube(simpleCube, otherShape.min(Direction.Axis.X), otherShape.min(Direction.Axis.Y), otherShape.min(Direction.Axis.Z), otherShape.max(Direction.Axis.X), otherShape.max(Direction.Axis.Y), otherShape.max(Direction.Axis.Z))) {
                cir.setReturnValue(true);
                return;
            }
            boolean acceptAnd = predicate.apply(true, true);
            boolean acceptOtherShapeAlone = predicate.apply(shapeA == otherShape, shapeB == otherShape);
            DiscreteVoxelShape voxelSet = otherShape.shape;
            DoubleList pointPositionsX = otherShape.getCoords(Direction.Axis.X);
            DoubleList pointPositionsY = otherShape.getCoords(Direction.Axis.Y);
            DoubleList pointPositionsZ = otherShape.getCoords(Direction.Axis.Z);
            int xMax = voxelSet.lastFull(Direction.Axis.X);
            int yMax = voxelSet.lastFull(Direction.Axis.Y);
            int zMax = voxelSet.lastFull(Direction.Axis.Z);
            double simpleCubeMaxX = simpleCube.max(Direction.Axis.X);
            double simpleCubeMinX = simpleCube.min(Direction.Axis.X);
            double simpleCubeMaxY = simpleCube.max(Direction.Axis.Y);
            double simpleCubeMinY = simpleCube.min(Direction.Axis.Y);
            double simpleCubeMaxZ = simpleCube.max(Direction.Axis.Z);
            double simpleCubeMinZ = simpleCube.min(Direction.Axis.Z);
            for (int x = voxelSet.firstFull(Direction.Axis.X); x < xMax; x++) {
                boolean simpleCubeIntersectsXSlice = simpleCubeMaxX - 1.0E-7 > pointPositionsX.getDouble(x) && simpleCubeMinX < pointPositionsX.getDouble(x + 1) - 1.0E-7;
                if (acceptOtherShapeAlone || simpleCubeIntersectsXSlice) {
                    boolean xSliceExceedsCube = acceptOtherShapeAlone && (!(simpleCubeMaxX >= pointPositionsX.getDouble(x + 1) - 1.0E-7) || !(simpleCubeMinX - 1.0E-7 <= pointPositionsX.getDouble(x)));
                    for (int y = voxelSet.firstFull(Direction.Axis.Y); y < yMax; y++) {
                        boolean simpleCubeIntersectsYSlice = simpleCubeMaxY - 1.0E-7 > pointPositionsY.getDouble(y) && simpleCubeMinY < pointPositionsY.getDouble(y + 1) - 1.0E-7;
                        if (acceptOtherShapeAlone || simpleCubeIntersectsYSlice) {
                            boolean ySliceExceedsCube = acceptOtherShapeAlone && (!(simpleCubeMaxY >= pointPositionsY.getDouble(y + 1) - 1.0E-7) || !(simpleCubeMinY - 1.0E-7 <= pointPositionsY.getDouble(y)));
                            for (int z = voxelSet.firstFull(Direction.Axis.Z); z < zMax; z++) {
                                boolean simpleCubeIntersectsZSlice = simpleCubeMaxZ - 1.0E-7 > pointPositionsZ.getDouble(z) && simpleCubeMinZ < pointPositionsZ.getDouble(z + 1) - 1.0E-7;
                                if (acceptOtherShapeAlone || simpleCubeIntersectsZSlice) {
                                    boolean zSliceExceedsCube = acceptOtherShapeAlone && (!(simpleCubeMaxZ >= pointPositionsZ.getDouble(z + 1) - 1.0E-7) || !(simpleCubeMinZ - 1.0E-7 <= pointPositionsZ.getDouble(z)));
                                    boolean o = voxelSet.isFullWide(x, y, z);
                                    boolean s = simpleCubeIntersectsXSlice && simpleCubeIntersectsYSlice && simpleCubeIntersectsZSlice;
                                    if (acceptAnd && o && s || acceptSimpleCubeAlone && !o && s || acceptOtherShapeAlone && o && (xSliceExceedsCube || ySliceExceedsCube || zSliceExceedsCube)) {
                                        cir.setReturnValue(true);
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
            cir.setReturnValue(false);
        }
    }

    private static boolean isTiny(VoxelShape shapeA) {
        return shapeA.min(Direction.Axis.X) > shapeA.max(Direction.Axis.X) - 3.0E-7 || shapeA.min(Direction.Axis.Y) > shapeA.max(Direction.Axis.Y) - 3.0E-7 || shapeA.min(Direction.Axis.Z) > shapeA.max(Direction.Axis.Z) - 3.0E-7;
    }

    private static boolean exceedsCube(VoxelShapeSimpleCube a, double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        return a.min(Direction.Axis.X) < minX - 1.0E-7 || a.max(Direction.Axis.X) > maxX + 1.0E-7 || a.min(Direction.Axis.Y) < minY - 1.0E-7 || a.max(Direction.Axis.Y) > maxY + 1.0E-7 || a.min(Direction.Axis.Z) < minZ - 1.0E-7 || a.max(Direction.Axis.Z) > maxZ + 1.0E-7;
    }

    private static boolean exceedsShape(VoxelShapeSimpleCube a, VoxelShapeSimpleCube b) {
        return a.min(Direction.Axis.X) < b.min(Direction.Axis.X) - 1.0E-7 || a.max(Direction.Axis.X) > b.max(Direction.Axis.X) + 1.0E-7 || a.min(Direction.Axis.Y) < b.min(Direction.Axis.Y) - 1.0E-7 || a.max(Direction.Axis.Y) > b.max(Direction.Axis.Y) + 1.0E-7 || a.min(Direction.Axis.Z) < b.min(Direction.Axis.Z) - 1.0E-7 || a.max(Direction.Axis.Z) > b.max(Direction.Axis.Z) + 1.0E-7;
    }

    private static boolean intersects(VoxelShapeSimpleCube a, VoxelShapeSimpleCube b) {
        return a.min(Direction.Axis.X) < b.max(Direction.Axis.X) - 1.0E-7 && a.max(Direction.Axis.X) > b.min(Direction.Axis.X) + 1.0E-7 && a.min(Direction.Axis.Y) < b.max(Direction.Axis.Y) - 1.0E-7 && a.max(Direction.Axis.Y) > b.min(Direction.Axis.Y) + 1.0E-7 && a.min(Direction.Axis.Z) < b.max(Direction.Axis.Z) - 1.0E-7 && a.max(Direction.Axis.Z) > b.min(Direction.Axis.Z) + 1.0E-7;
    }
}