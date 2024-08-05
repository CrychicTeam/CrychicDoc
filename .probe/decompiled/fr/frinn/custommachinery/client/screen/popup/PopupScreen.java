package fr.frinn.custommachinery.client.screen.popup;

import fr.frinn.custommachinery.client.screen.BaseScreen;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.Tooltip;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.layouts.LayoutElement;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.Nullable;

public abstract class PopupScreen extends BaseScreen {

    public final BaseScreen parent;

    private boolean dragging;

    private double dragX;

    private double dragY;

    public PopupScreen(BaseScreen parent, int xSize, int ySize) {
        super(Component.literal("Popup"), xSize, ySize);
        this.parent = parent;
    }

    public void closed() {
    }

    public void move(int movedX, int movedY) {
        this.x += movedX;
        this.y += movedY;
        this.m_6702_().forEach(c -> {
            if (c instanceof LayoutElement widget) {
                widget.setPosition(widget.getX() + movedX, widget.getY() + movedY);
            }
        });
    }

    @Nullable
    public Tooltip getTooltip(int mouseX, int mouseY) {
        for (GuiEventListener listener : this.m_6702_()) {
            if (listener.isMouseOver((double) mouseX, (double) mouseY) && listener instanceof AbstractWidget widget) {
                return widget.getTooltip();
            }
        }
        return null;
    }

    @Override
    protected void init() {
        super.init();
        this.x = (this.f_96543_ - this.xSize) / 2;
        this.y = (this.f_96544_ - this.ySize) / 2;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if ((int) Math.abs(this.dragX) >= 1 || (int) Math.abs(this.dragY) >= 1) {
            int changedX = (int) this.dragX;
            int changedY = (int) this.dragY;
            this.move(changedX, changedY);
            this.dragX -= (double) changedX;
            this.dragY -= (double) changedY;
        }
        super.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics graphics) {
        blankBackground(graphics, this.x, this.y, this.xSize, this.ySize);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.mouseClicked(mouseX, mouseY, button)) {
            return true;
        } else if (this.isMouseOver(mouseX, mouseY) && mouseY < (double) (this.y + 20)) {
            this.dragging = true;
            return true;
        } else {
            this.m_7897_(false);
            return false;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragging = false;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.dragging) {
            this.dragX += deltaX;
            this.dragY += deltaY;
        }
        return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return this.parent.getPopupUnderMouse(mouseX, mouseY) == this;
    }
}