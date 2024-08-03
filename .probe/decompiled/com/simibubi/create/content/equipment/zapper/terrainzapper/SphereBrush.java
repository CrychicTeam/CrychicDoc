package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class SphereBrush extends ShapedBrush {

    public static final int MAX_RADIUS = 10;

    private Map<Integer, List<BlockPos>> cachedBrushes = new HashMap();

    public SphereBrush() {
        super(1);
        for (int i = 0; i <= 10; i++) {
            int radius = i;
            List<BlockPos> positions = (List<BlockPos>) BlockPos.betweenClosedStream(BlockPos.ZERO.offset(-i - 1, -i - 1, -i - 1), BlockPos.ZERO.offset(i + 1, i + 1, i + 1)).map(BlockPos::new).filter(p -> VecHelper.getCenterOf(p).distanceTo(VecHelper.getCenterOf(BlockPos.ZERO)) < (double) ((float) radius + 0.5F)).collect(Collectors.toList());
            this.cachedBrushes.put(i, positions);
        }
    }

    @Override
    public BlockPos getOffset(Vec3 ray, Direction face, PlacementOptions option) {
        if (option == PlacementOptions.Merged) {
            return BlockPos.ZERO;
        } else {
            int offset = option == PlacementOptions.Attached ? 0 : -1;
            int r = this.param0 + 1 + offset;
            return BlockPos.ZERO.relative(face, r * (option == PlacementOptions.Attached ? 1 : -1));
        }
    }

    @Override
    int getMax(int paramIndex) {
        return 10;
    }

    @Override
    Component getParamLabel(int paramIndex) {
        return Lang.translateDirect("generic.radius");
    }

    @Override
    List<BlockPos> getIncludedPositions() {
        return (List<BlockPos>) this.cachedBrushes.get(this.param0);
    }
}