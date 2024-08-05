package journeymap.client.ui.component;

import java.util.Objects;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;

public class OptionsScrollListPane<T extends Slot> extends ScrollListPane<T> {

    public OptionsScrollListPane(JmUI parent, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(parent, mc, width, height, top, bottom, slotHeight);
    }

    @Override
    public void renderList(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        int count = this.m_5773_();
        for (int slotIndex = 0; slotIndex < count; slotIndex++) {
            int y = this.m_7610_(slotIndex);
            int l = this.m_7610_(slotIndex) + this.f_93387_;
            if (l >= this.f_93390_ && y <= this.f_93391_) {
                int slightHeight = this.f_93387_ - 4;
                AbstractSelectionList.Entry<?> entry = this.m_93500_(slotIndex);
                int listWidth = this.m_5759_();
                int x = this.m_5747_();
                if (y >= this.f_93390_ && y + this.f_93387_ <= this.f_93391_) {
                    entry.render(graphics, slotIndex, y, x, listWidth, slightHeight, mouseX, mouseY, Objects.equals(this.m_168795_(), entry), partialTicks);
                }
            }
        }
    }
}