package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.Mth;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class MenuTooltipPositioner implements ClientTooltipPositioner {

    private static final int MARGIN = 5;

    private static final int MOUSE_OFFSET_X = 12;

    public static final int MAX_OVERLAP_WITH_WIDGET = 3;

    public static final int MAX_DISTANCE_TO_WIDGET = 5;

    private final AbstractWidget widget;

    public MenuTooltipPositioner(AbstractWidget abstractWidget0) {
        this.widget = abstractWidget0;
    }

    @Override
    public Vector2ic positionTooltip(int int0, int int1, int int2, int int3, int int4, int int5) {
        Vector2i $$6 = new Vector2i(int2 + 12, int3);
        if ($$6.x + int4 > int0 - 5) {
            $$6.x = Math.max(int2 - 12 - int4, 9);
        }
        $$6.y += 3;
        int $$7 = int5 + 3 + 3;
        int $$8 = this.widget.getY() + this.widget.getHeight() + 3 + getOffset(0, 0, this.widget.getHeight());
        int $$9 = int1 - 5;
        if ($$8 + $$7 <= $$9) {
            $$6.y = $$6.y + getOffset($$6.y, this.widget.getY(), this.widget.getHeight());
        } else {
            $$6.y = $$6.y - ($$7 + getOffset($$6.y, this.widget.getY() + this.widget.getHeight(), this.widget.getHeight()));
        }
        return $$6;
    }

    private static int getOffset(int int0, int int1, int int2) {
        int $$3 = Math.min(Math.abs(int0 - int1), int2);
        return Math.round(Mth.lerp((float) $$3 / (float) int2, (float) (int2 - 3), 5.0F));
    }
}