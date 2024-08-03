package malte0811.ferritecore.impl;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import net.minecraft.Util;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.core.Direction;

public class ModelSidesImpl {

    private static final Direction[] SIDES = Direction.values();

    private static final Map<Direction, List<BakedQuad>> EMPTY = Util.make(new EnumMap(Direction.class), m -> {
        for (Direction side : SIDES) {
            m.put(side, List.of());
        }
    });

    public static List<BakedQuad> minimizeUnculled(List<BakedQuad> quads) {
        return List.copyOf(quads);
    }

    public static Map<Direction, List<BakedQuad>> minimizeCulled(Map<Direction, List<BakedQuad>> quadsBySide) {
        if (quadsBySide.isEmpty()) {
            return quadsBySide;
        } else {
            boolean allEmpty = true;
            for (Direction face : SIDES) {
                List<BakedQuad> sideQuads = (List<BakedQuad>) quadsBySide.get(face);
                quadsBySide.put(face, List.copyOf(sideQuads));
                allEmpty &= sideQuads.isEmpty();
            }
            return allEmpty ? EMPTY : quadsBySide;
        }
    }
}