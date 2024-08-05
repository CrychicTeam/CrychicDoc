package com.simibubi.create.content.equipment.zapper.terrainzapper;

import com.simibubi.create.foundation.utility.Lang;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.tuple.Pair;

public class CylinderBrush extends ShapedBrush {

    public static final int MAX_RADIUS = 8;

    public static final int MAX_HEIGHT = 8;

    private Map<Pair<Integer, Integer>, List<BlockPos>> cachedBrushes = new HashMap();

    public CylinderBrush() {
        super(2);
        for (int i = 0; i <= 8; i++) {
            int radius = i;
            List<BlockPos> positions = (List<BlockPos>) BlockPos.betweenClosedStream(BlockPos.ZERO.offset(-i - 1, 0, -i - 1), BlockPos.ZERO.offset(i + 1, 0, i + 1)).map(BlockPos::new).filter(px -> VecHelper.getCenterOf(px).distanceTo(VecHelper.getCenterOf(BlockPos.ZERO)) < (double) ((float) radius + 0.42F)).collect(Collectors.toList());
            for (int h = 0; h <= 8; h++) {
                List<BlockPos> stackedPositions = new ArrayList();
                for (int layer = 0; layer < h; layer++) {
                    int yOffset = layer - h / 2;
                    for (BlockPos p : positions) {
                        stackedPositions.add(p.above(yOffset));
                    }
                }
                this.cachedBrushes.put(Pair.of(i, h), stackedPositions);
            }
        }
    }

    @Override
    public BlockPos getOffset(Vec3 ray, Direction face, PlacementOptions option) {
        if (option == PlacementOptions.Merged) {
            return BlockPos.ZERO;
        } else {
            int offset = option == PlacementOptions.Attached ? 0 : -1;
            boolean negative = face.getAxisDirection() == Direction.AxisDirection.NEGATIVE;
            int yOffset = option == PlacementOptions.Attached ? (negative ? 1 : 2) : (negative ? 0 : -1);
            int r = this.param0 + 1 + offset;
            int y = (this.param1 + (this.param1 == 0 ? 0 : yOffset)) / 2;
            return BlockPos.ZERO.relative(face, (face.getAxis().isVertical() ? y : r) * (option == PlacementOptions.Attached ? 1 : -1));
        }
    }

    @Override
    int getMax(int paramIndex) {
        return paramIndex == 0 ? 8 : 8;
    }

    @Override
    int getMin(int paramIndex) {
        return paramIndex == 0 ? 0 : 1;
    }

    @Override
    Component getParamLabel(int paramIndex) {
        return (Component) (paramIndex == 0 ? Lang.translateDirect("generic.radius") : super.getParamLabel(paramIndex));
    }

    @Override
    public List<BlockPos> getIncludedPositions() {
        return (List<BlockPos>) this.cachedBrushes.get(Pair.of(this.param0, this.param1));
    }
}