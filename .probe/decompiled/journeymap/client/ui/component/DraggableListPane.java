package journeymap.client.ui.component;

import java.awt.Color;
import java.util.List;
import journeymap.client.render.draw.DrawUtil;
import journeymap.client.ui.option.CategorySlot;
import journeymap.client.ui.option.SlotMetadata;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;

public class DraggableListPane<T extends Slot> extends ScrollListPane<T> {

    private boolean clicked = false;

    private Integer frameColor = new Color(-6250336).getRGB();

    private boolean dragging = false;

    private boolean didDrag = false;

    private int mouseDragOffsetX = 0;

    private int mouseDragOffsetY = 0;

    public DraggableListPane(JmUI parent, Minecraft mc, int width, int height, int x, int y) {
        super(parent, mc, width, height, 0, height, 20);
        this.f_93393_ = x;
        this.f_93392_ = x + width;
        this.f_93390_ = y;
        this.f_93391_ = y + height;
    }

    @Override
    public void render(GuiGraphics graphics, int pMouseX, int pMouseY, float pPartialTicks) {
        super.render(graphics, pMouseX, pMouseY, pPartialTicks);
    }

    @Override
    public void setSlots(List<T> slots) {
        int newWidth = this.f_93388_;
        int newHeight = this.f_93389_;
        super.setSlots(slots);
        for (Slot slot : this.getRootSlots()) {
            if (slot instanceof CategorySlot) {
                newHeight = ((CategorySlot) slot).getAllChildMetadata().size() * 25;
                for (SlotMetadata child : ((CategorySlot) slot).getAllChildMetadata()) {
                    String name = child.getName();
                    int sWidth = Minecraft.getInstance().font.width(name) * 2;
                    newWidth = Math.max(sWidth, newWidth);
                }
            }
        }
        this.updateSize(newWidth, newHeight, this.f_93390_, this.f_93391_);
    }

    @Override
    public void updateSize(int width, int height, int top, int bottom) {
        this.f_93388_ = width;
        this.f_93389_ = height;
        this.f_93390_ = top;
        this.f_93391_ = top + height;
        this.f_93392_ = this.f_93393_ + width;
        this.scrollbarX = this.f_93388_ - this.hpad + this.f_93393_;
        this.listWidth = this.f_93388_ - this.hpad * 4;
    }

    @Override
    protected void renderBackground(GuiGraphics graphics) {
        if (this.clicked) {
            graphics.fillGradient(this.f_93393_, this.f_93390_, this.f_93392_, this.f_93391_, -1072689136, -804253680);
            float alpha = 1.0F;
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.f_93393_ - 1), (double) (this.f_93390_ - 1), (double) (this.f_93388_ + 2), 1.0, this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.f_93393_ - 1), (double) (this.f_93390_ + this.f_93389_), (double) (this.f_93388_ + 2), 1.0, this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.f_93393_ - 1), (double) (this.f_93390_ - 1), 1.0, (double) (this.f_93389_ + 1), this.frameColor, alpha);
            DrawUtil.drawRectangle(graphics.pose(), (double) (this.f_93388_ + this.f_93393_), (double) (this.f_93390_ - 1), 1.0, (double) (this.f_93389_ + 2), this.frameColor, alpha);
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        boolean paneClicked = false;
        if (super.m_5953_(pMouseX, pMouseY) && !(this.m_93412_(pMouseX, pMouseY) instanceof CategorySlot)) {
            paneClicked = super.mouseClicked(pMouseX, pMouseY, pButton);
        } else if (super.m_5953_(pMouseX, pMouseY) && this.m_93412_(pMouseX, pMouseY) instanceof CategorySlot) {
            this.mouseDragOffsetX = (int) (pMouseX - (double) this.f_93393_);
            this.mouseDragOffsetY = (int) (pMouseY - (double) this.f_93390_);
            this.dragging = true;
        }
        return paneClicked;
    }

    @Override
    public boolean mouseDragged(double pMouseX, double pMouseY, int pButton, double pDragX, double pDragY) {
        if (this.dragging) {
            this.didDrag = true;
            int posX = (int) (pMouseX - (double) this.mouseDragOffsetX);
            int posY = (int) (pMouseY - (double) this.mouseDragOffsetY);
            this.updatePosition(posX, posY);
        }
        return super.mouseDragged(pMouseX, pMouseY, pButton, pDragX, pDragY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        if (this.dragging && this.didDrag) {
            this.didDrag = false;
            this.dragging = false;
        } else if (super.m_5953_(mouseX, mouseY) && this.m_93412_(mouseX, mouseY) instanceof CategorySlot) {
            this.didDrag = false;
            this.dragging = false;
            super.mouseClicked(mouseX, mouseY, mouseButton);
            CategorySlot slot = (CategorySlot) this.m_93412_(mouseX, mouseY);
            this.clicked = slot.isSelected();
        }
        return super.mouseReleased(mouseX, mouseY, mouseButton);
    }

    public void updatePosition(int x, int y) {
        this.f_93390_ = y;
        this.f_93391_ = this.f_93390_ + this.f_93389_;
        this.f_93393_ = x;
        this.f_93392_ = this.f_93393_ + this.f_93388_;
        this.scrollbarX = this.f_93388_ - this.hpad + this.f_93393_;
        this.listWidth = this.f_93388_ - this.hpad * 4;
    }
}