package journeymap.client.ui.waypoint;

import journeymap.client.Constants;
import journeymap.client.ui.component.OnOffButton;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;

class SortButton extends OnOffButton {

    final WaypointManagerItem.Sort sort;

    final String labelInactive;

    public SortButton(String label, WaypointManagerItem.Sort sort, Button.OnPress onPress) {
        super(String.format("%s %s", label, "▲"), String.format("%s %s", label, "▼"), sort.ascending, onPress);
        this.labelInactive = label;
        this.sort = sort;
    }

    @Override
    public void toggle() {
        this.sort.ascending = !this.sort.ascending;
        this.setActive(true);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float f) {
        super.m_88315_(graphics, mouseX, mouseY, f);
        super.drawUnderline(graphics.pose());
    }

    public void setActive(boolean active) {
        if (active) {
            this.setToggled(Boolean.valueOf(this.sort.ascending));
        } else {
            this.m_93666_(Constants.getStringTextComponent(String.format("%s %s", this.labelInactive, " ")));
        }
    }
}