package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;

public class SliceShape extends VoxelShape {

    private final VoxelShape delegate;

    private final Direction.Axis axis;

    private static final DoubleList SLICE_COORDS = new CubePointRange(1);

    public SliceShape(VoxelShape voxelShape0, Direction.Axis directionAxis1, int int2) {
        super(makeSlice(voxelShape0.shape, directionAxis1, int2));
        this.delegate = voxelShape0;
        this.axis = directionAxis1;
    }

    private static DiscreteVoxelShape makeSlice(DiscreteVoxelShape discreteVoxelShape0, Direction.Axis directionAxis1, int int2) {
        return new SubShape(discreteVoxelShape0, directionAxis1.choose(int2, 0, 0), directionAxis1.choose(0, int2, 0), directionAxis1.choose(0, 0, int2), directionAxis1.choose(int2 + 1, discreteVoxelShape0.xSize, discreteVoxelShape0.xSize), directionAxis1.choose(discreteVoxelShape0.ySize, int2 + 1, discreteVoxelShape0.ySize), directionAxis1.choose(discreteVoxelShape0.zSize, discreteVoxelShape0.zSize, int2 + 1));
    }

    @Override
    protected DoubleList getCoords(Direction.Axis directionAxis0) {
        return directionAxis0 == this.axis ? SLICE_COORDS : this.delegate.getCoords(directionAxis0);
    }
}