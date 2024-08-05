package snownee.jade.gui.config;

import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipPositioner;
import org.joml.Vector2i;
import org.joml.Vector2ic;

public class BelowOrAboveListEntryTooltipPositioner implements ClientTooltipPositioner {

    private final OptionsList list;

    private final OptionsList.Entry entry;

    public BelowOrAboveListEntryTooltipPositioner(OptionsList list, OptionsList.Entry entry) {
        this.list = list;
        this.entry = entry;
    }

    @Override
    public Vector2ic positionTooltip(int i, int j, int mouseX, int mouseY, int m, int n) {
        Vector2i vector2i = new Vector2i();
        int index = this.list.m_6702_().indexOf(this.entry);
        if (index == -1) {
            vector2i.x = mouseX + 3;
            vector2i.y = mouseY + 3;
            return vector2i;
        } else {
            vector2i.x = this.entry.getTextX(this.list.getRowWidth());
            vector2i.y = this.list.getRowBottom(index) + 1;
            if (vector2i.y + n > j) {
                vector2i.y = this.list.getRowTop(index) - n - 1;
            }
            if (vector2i.x + m > i) {
                vector2i.x = Math.max(this.list.m_5747_() + this.list.getRowWidth() - m, 4);
            }
            return vector2i;
        }
    }
}