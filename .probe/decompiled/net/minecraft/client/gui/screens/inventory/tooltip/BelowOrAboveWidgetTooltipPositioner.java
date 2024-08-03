package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.components.AbstractWidget;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class BelowOrAboveWidgetTooltipPositioner implements ClientTooltipPositioner {

    private final AbstractWidget widget;

    public BelowOrAboveWidgetTooltipPositioner(AbstractWidget abstractWidget0) {
        this.widget = abstractWidget0;
    }

    @Override
    public Vector2ic positionTooltip(int int0, int int1, int int2, int int3, int int4, int int5) {
        Vector2i $$6 = new Vector2i();
        $$6.x = this.widget.getX() + 3;
        $$6.y = this.widget.getY() + this.widget.getHeight() + 3 + 1;
        if ($$6.y + int5 + 3 > int1) {
            $$6.y = this.widget.getY() - int5 - 3 - 1;
        }
        if ($$6.x + int4 > int0) {
            $$6.x = Math.max(this.widget.getX() + this.widget.getWidth() - int4 - 3, 4);
        }
        return $$6;
    }
}