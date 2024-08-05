package net.minecraft.world.phys.shapes;

import it.unimi.dsi.fastutil.doubles.DoubleList;
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;

public final class CubeVoxelShape extends VoxelShape {

    protected CubeVoxelShape(DiscreteVoxelShape discreteVoxelShape0) {
        super(discreteVoxelShape0);
    }

    @Override
    protected DoubleList getCoords(Direction.Axis directionAxis0) {
        return new CubePointRange(this.f_83211_.getSize(directionAxis0));
    }

    @Override
    protected int findIndex(Direction.Axis directionAxis0, double double1) {
        int $$2 = this.f_83211_.getSize(directionAxis0);
        return Mth.floor(Mth.clamp(double1 * (double) $$2, -1.0, (double) $$2));
    }
}