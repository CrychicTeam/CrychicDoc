package net.minecraft.client.gui.screens.inventory.tooltip;

import org.joml.Vector2i;
import org.joml.Vector2ic;

public class DefaultTooltipPositioner implements ClientTooltipPositioner {

    public static final ClientTooltipPositioner INSTANCE = new DefaultTooltipPositioner();

    private DefaultTooltipPositioner() {
    }

    @Override
    public Vector2ic positionTooltip(int int0, int int1, int int2, int int3, int int4, int int5) {
        Vector2i $$6 = new Vector2i(int2, int3).add(12, -12);
        this.positionTooltip(int0, int1, $$6, int4, int5);
        return $$6;
    }

    private void positionTooltip(int int0, int int1, Vector2i vectorI2, int int3, int int4) {
        if (vectorI2.x + int3 > int0) {
            vectorI2.x = Math.max(vectorI2.x - 24 - int3, 4);
        }
        int $$5 = int4 + 3;
        if (vectorI2.y + $$5 > int1) {
            vectorI2.y = int1 - $$5;
        }
    }
}