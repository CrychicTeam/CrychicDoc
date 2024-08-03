package journeymap.client.ui.component;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.List;
import journeymap.client.ui.GuiHooks;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ScrollPaneScreen extends Screen {

    private static final int MAX_DISPLAY_SIZE = 6;

    protected ScrollPane scrollPane;

    protected Removable parent;

    private int paneWidth;

    private int paneHeight;

    public boolean visible = false;

    private int paneX;

    private int paneY;

    private List<DropDownItem> items;

    protected boolean renderDecorations = true;

    protected boolean renderSolidBackground = false;

    protected ScrollPaneScreen(Removable parent, List<DropDownItem> items, int paneWidth, int paneHeight, int paneX, int paneY) {
        super(Component.literal(""));
        this.parent = parent;
        this.paneWidth = paneWidth;
        this.paneHeight = paneHeight;
        this.paneX = paneX;
        this.paneY = paneY;
        this.items = items;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean paneClicked = this.scrollPane.m_6375_(mouseX, mouseY, button);
        DropDownItem pressed = (DropDownItem) this.scrollPane.mouseClicked((int) mouseX, (int) mouseX, button);
        this.scrollPane.f_93399_ = false;
        boolean mouseOver = this.scrollPane.isMouseOver(mouseX, mouseY);
        if (mouseOver) {
            this.scrollPane.updateScrollingState(mouseX, mouseY, button);
            if (pressed != null) {
                this.onClick(pressed);
                return true;
            } else {
                return super.m_6375_(mouseX, mouseY, button);
            }
        } else {
            this.onClose();
            return false;
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.scrollPane != null) {
            this.scrollPane.setDimensions(this.paneWidth - 6, this.paneHeight, 0, 0, this.paneX + 2, this.paneY);
            this.scrollPane.render(graphics, mouseX, mouseY, partialTicks);
            if (this.visible && this.scrollPane.isMouseOver((double) mouseX, (double) mouseY)) {
                this.renderTooltip(graphics, mouseX, mouseY);
            }
            super.render(graphics, mouseX, mouseY, partialTicks);
        }
    }

    private void renderTooltip(GuiGraphics graphics, int mouseX, int mouseY) {
        Button button = this.scrollPane.getButton(mouseX, mouseY);
        if (button != null) {
            graphics.renderTooltip(this.f_96541_.font, button.getFormattedTooltip(), mouseX, mouseY);
        }
    }

    public void renderBackground(PoseStack pMatrixStack, int pVOffset) {
    }

    public void setRenderSolidBackground(boolean renderSolidBackground) {
        this.renderSolidBackground = renderSolidBackground;
    }

    public boolean mouseOverPane(double x, double y) {
        return this.scrollPane.isMouseOver(x, y);
    }

    @Override
    public boolean mouseScrolled(double x, double y, double scroll) {
        return this.scrollPane.isMouseOver(x, y) ? this.scrollPane.m_6050_(x, y, scroll) : super.m_6050_(x, y, scroll);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.scrollPane.m_6348_(mouseX, mouseY, mouseButton);
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        this.scrollPane.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public void mouseMoved(double mouseX, double mouseY) {
        this.scrollPane.mouseMoved(mouseX, mouseY);
        super.m_94757_(mouseX, mouseY);
    }

    @Override
    public boolean keyReleased(int keyCode, int scanCode, int modifiers) {
        this.scrollPane.m_7920_(keyCode, scanCode, modifiers);
        return super.m_7920_(keyCode, scanCode, modifiers);
    }

    public void setPaneWidth(int paneWidth) {
        this.paneWidth = paneWidth;
    }

    public void setPaneHeight(int paneHeight) {
        this.paneHeight = paneHeight;
    }

    public int getPaneWidth() {
        return this.scrollPane != null ? this.scrollPane.getWidth() : this.paneWidth;
    }

    public int getPaneHeight() {
        return this.paneHeight;
    }

    public void setPaneX(int paneX) {
        this.paneX = paneX;
    }

    public void setPaneY(int paneY) {
        this.paneY = paneY;
    }

    public int getPaneX() {
        return this.paneX;
    }

    public int getPaneY() {
        return this.paneY;
    }

    public void setParent(Removable removable) {
        this.parent = removable;
    }

    public ScrollPane getScrollPane() {
        return this.scrollPane;
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public void setItems(List<DropDownItem> items) {
        this.items = items;
    }

    public void display() {
        this.scrollPane = new ScrollPane(null, Minecraft.getInstance(), 0, 0, this.items, ((DropDownItem) this.items.get(0)).m_93694_(), 2);
        this.scrollPane.setDrawPartialScrollable(false);
        this.scrollPane.m_93471_(false);
        this.scrollPane.setRenderDecorations(this.renderDecorations);
        this.scrollPane.setRenderSolidBackground(this.renderSolidBackground);
        this.visible = true;
        GuiHooks.pushGuiLayer(this);
    }

    public void onClick(DropDownItem pressed) {
        this.onClose();
    }

    @Override
    public void onClose() {
        this.visible = false;
        GuiHooks.popGuiLayer();
        this.parent.onRemove();
    }

    public void setRenderDecorations(boolean renderDecorations) {
        this.renderDecorations = renderDecorations;
    }
}