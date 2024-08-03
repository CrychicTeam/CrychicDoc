package snownee.jade.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.network.chat.Component;
import snownee.jade.util.SmoothChasingValue;

public class OptionsNav extends ObjectSelectionList<OptionsNav.Entry> {

    private final OptionsList options;

    private final SmoothChasingValue anchor;

    public OptionsNav(OptionsList options, int width, int height, int top, int bottom, int itemHeight) {
        super(Minecraft.getInstance(), width, height, top, bottom, itemHeight);
        this.options = options;
        this.anchor = new SmoothChasingValue();
        this.m_93471_(false);
        this.m_93496_(false);
    }

    @Override
    protected void renderList(GuiGraphics guiGraphics, int i, int j, float f) {
        super.m_239227_(guiGraphics, i, j, f);
        this.anchor.tick(f);
        if (!this.m_6702_().isEmpty()) {
            int top = (int) ((double) (this.f_93390_ + 4) - this.m_93517_() + (double) (this.anchor.value * (float) this.f_93387_) + (double) this.f_93395_);
            int left = this.m_5747_() + 2;
            guiGraphics.fill(left, top, left + 2, top + this.f_93387_ - 4, -1);
        }
    }

    public void addEntry(OptionsList.Title entry) {
        super.m_7085_(new OptionsNav.Entry(this, entry));
    }

    @Override
    public int getRowWidth() {
        return this.f_93388_;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.m_5747_() + this.getRowWidth() - 8;
    }

    public void refresh() {
        this.m_93516_();
        if (this.options.m_6702_().size() > 1) {
            for (OptionsList.Entry child : this.options.m_6702_()) {
                if (child instanceof OptionsList.Title titleEntry) {
                    this.addEntry(titleEntry);
                }
            }
        }
    }

    public static class Entry extends ObjectSelectionList.Entry<OptionsNav.Entry> {

        private final OptionsList.Title title;

        private final OptionsNav parent;

        public Entry(OptionsNav parent, OptionsList.Title title) {
            this.parent = parent;
            this.title = title;
        }

        @Override
        public void render(GuiGraphics guiGraphics, int index, int rowTop, int rowLeft, int width, int height, int mouseX, int mouseY, boolean hovered, float deltaTime) {
            guiGraphics.drawString(this.title.client.font, this.title.getTitle().getString(), rowLeft + 10, rowTop + height / 2 - 9 / 2, 16777215);
            if (this.parent.options.currentTitle == this.title) {
                if (!this.parent.m_5953_((double) mouseX, (double) mouseY)) {
                    this.parent.m_93498_(this);
                }
                this.parent.anchor.target((float) index);
            }
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (button == 0) {
                this.parent.options.showOnTop(this.title);
            }
            return true;
        }

        @Override
        public Component getNarration() {
            return this.title.narration;
        }
    }
}