package com.simibubi.create.content.kinetics.gauge;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.utility.VoxelShaper;
import java.util.Arrays;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class GaugeShaper extends VoxelShaper {

    private VoxelShaper axisFalse;

    private VoxelShaper axisTrue;

    static GaugeShaper make() {
        GaugeShaper shaper = new GaugeShaper();
        shaper.axisFalse = forDirectional(AllShapes.GAUGE_SHAPE_UP, Direction.UP);
        shaper.axisTrue = forDirectional(rotatedCopy(AllShapes.GAUGE_SHAPE_UP, new Vec3(0.0, 90.0, 0.0)), Direction.UP);
        Arrays.asList(Direction.EAST, Direction.WEST).forEach(direction -> {
            VoxelShape mem = shaper.axisFalse.get(direction);
            shaper.axisFalse.withShape(shaper.axisTrue.get(direction), direction);
            shaper.axisTrue.withShape(mem, direction);
        });
        return shaper;
    }

    public VoxelShape get(Direction direction, boolean axisAlong) {
        return (axisAlong ? this.axisTrue : this.axisFalse).get(direction);
    }
}