package journeymap.client.ui.component;

import java.util.List;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.SlotMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSelectionList;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.util.FormattedCharSequence;

public class ScrollListPane<T extends Slot> extends AbstractSelectionList {

    private final JmUI parent;

    public SlotMetadata lastTooltipMetadata;

    public List<FormattedCharSequence> lastTooltip;

    public long lastTooltipTime;

    public long hoverDelay = 400L;

    protected int hpad = 12;

    private List<T> rootSlots;

    private SlotMetadata lastPressed;

    protected int lastClickedIndex;

    protected int scrollbarX;

    protected int listWidth;

    private boolean alignTop;

    public ScrollListPane(JmUI parent, Minecraft mc, int width, int height, int top, int bottom, int slotHeight) {
        super(mc, width, height, top, bottom, slotHeight);
        this.parent = parent;
        this.updateSize(width, height, top, bottom);
    }

    public void setHover(boolean hover) {
        for (Slot slot : this.rootSlots) {
            slot.displayHover(hover);
        }
    }

    @Override
    public void updateSize(int width, int height, int top, int bottom) {
        super.updateSize(width, height, top, bottom);
        this.scrollbarX = this.f_93388_ - this.hpad;
        this.listWidth = this.f_93388_ - this.hpad * 4;
    }

    public void setSlots(List<T> slots) {
        this.rootSlots = slots;
        this.updateSlots();
    }

    public List<T> getRootSlots() {
        return this.rootSlots;
    }

    public void updateSlots() {
        int sizeBefore = this.m_5773_();
        this.m_6702_().clear();
        int columnWidth = 0;
        for (Slot slot : this.rootSlots) {
            columnWidth = Math.max(columnWidth, slot.getColumnWidth());
            super.addEntry(slot);
            List<? extends Slot> childSlots = slot.getChildSlots(this.listWidth, columnWidth);
            if (childSlots != null && !childSlots.isEmpty()) {
                for (Slot child : childSlots) {
                    super.addEntry(child);
                }
            }
        }
        int sizeAfter = this.m_5773_();
        if (sizeBefore < sizeAfter) {
            this.m_93429_(-(sizeAfter * this.f_93387_));
            this.m_93429_(this.lastClickedIndex * this.f_93387_);
        }
    }

    public void scrollTo(Slot slot) {
        this.m_93429_(-(this.m_6702_().size() * this.f_93389_));
        this.m_93429_(this.m_6702_().indexOf(slot) * this.f_93389_);
    }

    @Override
    public boolean isSelectedItem(int index) {
        return super.isSelectedItem(index);
    }

    @Override
    protected int getRowTop(int slotIndex) {
        return super.getRowTop(slotIndex);
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        try {
            super.setRenderBackground(false);
            Slot slot = (Slot) this.m_93412_((double) mouseX, (double) mouseY);
            if (slot != null) {
                SlotMetadata tooltipMetadata = slot.getCurrentTooltip();
                if (tooltipMetadata != null && !tooltipMetadata.getTooltip().equals(this.lastTooltip)) {
                    this.lastTooltipMetadata = tooltipMetadata;
                    this.lastTooltip = tooltipMetadata.getTooltip();
                    this.lastTooltipTime = System.currentTimeMillis();
                }
            }
        } catch (Throwable var7) {
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderList(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderList(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public int getRowWidth() {
        return this.listWidth;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean clicked = super.mouseClicked(mouseX, mouseY, button);
        if (super.isMouseOver(mouseX, mouseY)) {
            Slot slot = (Slot) this.m_93412_(mouseX, mouseY);
            if (slot == null) {
                return false;
            }
            this.lastClickedIndex = this.m_6702_().indexOf(slot);
            this.lastPressed = slot.getLastPressed();
            if (slot instanceof CategorySlot) {
                this.updateSlots();
            }
        }
        return clicked;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        for (int slotIndex = 0; slotIndex < this.m_5773_(); slotIndex++) {
            if (slotIndex == this.lastClickedIndex) {
                Slot slot = this.getSlot(this.lastClickedIndex);
                if (slot.m_6348_(mouseX, mouseY, mouseButton)) {
                    this.lastPressed = null;
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        if (this.m_6702_().isEmpty()) {
            return false;
        } else {
            return this.lastClickedIndex > -1 && this.getSlot(this.lastClickedIndex) != null && this.getSlot(this.lastClickedIndex).m_7979_(mouseX, mouseY, button, mouseDX, mouseDY) ? true : super.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        }
    }

    public Slot getSlot(int index) {
        return super.getItemCount() > index ? (Slot) this.m_6702_().get(index) : null;
    }

    public SlotMetadata getLastPressed() {
        return this.lastPressed;
    }

    public void resetLastPressed() {
        this.lastPressed = null;
    }

    public Slot getLastPressedParentSlot() {
        if (this.lastPressed != null) {
            for (Slot slot : this.rootSlots) {
                if (slot.contains(this.lastPressed)) {
                    return slot;
                }
            }
        }
        return null;
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        for (int slotIndex = 0; slotIndex < this.m_5773_(); slotIndex++) {
            if (slotIndex == this.lastClickedIndex) {
                return this.getSlot(this.lastClickedIndex).charTyped(typedChar, keyCode);
            }
        }
        return false;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        for (int slotIndex = 0; slotIndex < this.m_5773_(); slotIndex++) {
            if (slotIndex == this.lastClickedIndex) {
                return this.getSlot(this.lastClickedIndex).keyPressed(key, value, modifier);
            }
        }
        return false;
    }

    @Override
    protected int getScrollbarPosition() {
        return this.scrollbarX;
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        graphics.fillGradient(0, 0, this.f_93388_, this.f_93389_, -1072689136, -804253680);
    }

    @Override
    protected int getMaxPosition() {
        int contentHeight = super.getMaxPosition();
        if (this.alignTop) {
            contentHeight = Math.max(this.getBottom() - this.getTop() - 4, contentHeight);
        }
        return contentHeight;
    }

    public int getTop() {
        return this.f_93390_;
    }

    public int getBottom() {
        return this.f_93391_;
    }

    public int getHeight() {
        return this.f_93389_;
    }

    public void setAlignTop(boolean alignTop) {
        this.alignTop = alignTop;
    }

    @Override
    public void updateNarration(NarrationElementOutput narrationElementOutput) {
    }
}