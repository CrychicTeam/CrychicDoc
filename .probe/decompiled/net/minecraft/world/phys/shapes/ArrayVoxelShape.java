package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleArrayList;
import it.unimi.dsi.fastutil.doubles.DoubleList;
import java.util.Arrays;
import net.minecraft.Util;
import net.minecraft.core.Direction;

public class ArrayVoxelShape extends VoxelShape {

    private final DoubleList xs;

    private final DoubleList ys;

    private final DoubleList zs;

    protected ArrayVoxelShape(DiscreteVoxelShape discreteVoxelShape0, double[] double1, double[] double2, double[] double3) {
        this(discreteVoxelShape0, DoubleArrayList.wrap(Arrays.copyOf(double1, discreteVoxelShape0.getXSize() + 1)), DoubleArrayList.wrap(Arrays.copyOf(double2, discreteVoxelShape0.getYSize() + 1)), DoubleArrayList.wrap(Arrays.copyOf(double3, discreteVoxelShape0.getZSize() + 1)));
    }

    ArrayVoxelShape(DiscreteVoxelShape discreteVoxelShape0, DoubleList doubleList1, DoubleList doubleList2, DoubleList doubleList3) {
        super(discreteVoxelShape0);
        int $$4 = discreteVoxelShape0.getXSize() + 1;
        int $$5 = discreteVoxelShape0.getYSize() + 1;
        int $$6 = discreteVoxelShape0.getZSize() + 1;
        if ($$4 == doubleList1.size() && $$5 == doubleList2.size() && $$6 == doubleList3.size()) {
            this.xs = doubleList1;
            this.ys = doubleList2;
            this.zs = doubleList3;
        } else {
            throw (IllegalArgumentException) Util.pauseInIde(new IllegalArgumentException("Lengths of point arrays must be consistent with the size of the VoxelShape."));
        }
    }

    @Override
    protected DoubleList getCoords(Direction.Axis directionAxis0) {
        switch(directionAxis0) {
            case X:
                return this.xs;
            case Y:
                return this.ys;
            case Z:
                return this.zs;
            default:
                throw new IllegalArgumentException();
        }
    }
}