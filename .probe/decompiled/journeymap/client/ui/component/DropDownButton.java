package journeymap.client.ui.component;

import com.mojang.blaze3d.platform.Window;
import java.util.List;
import journeymap.client.render.draw.DrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class DropDownButton extends Button implements Removable, SelectableParent {

    private static final int MAX_DISPLAY_SIZE = 6;

    private boolean visible = false;

    protected DropDownItem selected;

    protected List<DropDownItem> items;

    protected int paneWidth;

    protected net.minecraft.client.gui.components.Button.OnPress onPress;

    protected DropDownButton.Glyph activeGlyph = DropDownButton.Glyph.DOWN;

    protected int panelMargin = 2;

    protected ScrollPaneScreen paneScreen;

    protected int buttonBuffer = 50;

    public DropDownButton(String label, net.minecraft.client.gui.components.Button.OnPress onPress) {
        super(label, emptyPressable());
        this.onPress = onPress;
    }

    public void setItems(List<DropDownItem> items) {
        this.items = items;
        this.paneWidth = this.getWidth();
        this.paneScreen = new ScrollPaneScreen(this, items, this.paneWidth, this.getPaneHeight(), this.m_252754_(), this.getPanelLocation());
    }

    protected void setRenderSolidBackground(boolean value) {
        this.paneScreen.setRenderSolidBackground(value);
    }

    protected void setRenderDecorations(boolean value) {
        this.paneScreen.setRenderDecorations(value);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean buttonClicked = super.mouseClicked(mouseX, mouseY, button);
        if (buttonClicked && !this.visible) {
            this.visible = true;
            this.setEnabled(false);
            this.paneScreen.setPaneX(this.m_252754_());
            this.paneScreen.setPaneY(this.getPanelLocation());
            this.paneScreen.display();
            return true;
        } else {
            return super.mouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.render(graphics, mouseX, mouseY, partialTicks);
        DrawUtil.drawLabel(graphics, this.visible ? this.activeGlyph.getCode() : DropDownButton.Glyph.CLOSED.getCode(), (double) (this.m_252754_() + 16), (double) (this.getMiddleY() - 1), DrawUtil.HAlign.Left, DrawUtil.VAlign.Middle, 0, 0.0F, this.visible ? this.disabledLabelColor : this.getLabelColor(), this.visible ? 0.25F : 1.0F, 1.5, false);
    }

    protected int getPanelLocation() {
        Window window = Minecraft.getInstance().getWindow();
        int panelHeight = this.getPaneHeight();
        int windowBottom = window.getGuiScaledHeight();
        if (super.m_252907_() - (this.panelMargin + panelHeight) <= 0) {
            this.activeGlyph = DropDownButton.Glyph.DOWN;
            return super.m_252907_() + this.panelMargin + this.f_93619_;
        } else if (super.m_252907_() + this.panelMargin + panelHeight > windowBottom) {
            this.activeGlyph = DropDownButton.Glyph.UP;
            return super.m_252907_() - panelHeight - this.panelMargin;
        } else {
            this.activeGlyph = DropDownButton.Glyph.DOWN;
            return super.m_252907_() + this.panelMargin + this.f_93619_;
        }
    }

    protected int getPaneHeight() {
        int size = Math.min(this.items.size(), 6);
        return size * (((DropDownItem) this.items.get(0)).m_93694_() + 5);
    }

    @Override
    public void onRemove() {
        this.visible = false;
        this.setEnabled(true);
    }

    @Override
    public int getWidth() {
        int width = 0;
        if (this.items != null) {
            Font fontRenderer = Minecraft.getInstance().font;
            for (DropDownItem item : this.items) {
                width = Math.max(width, fontRenderer.width(this.getLabel(item)));
            }
            this.f_93618_ = width + this.buttonBuffer;
        }
        return this.f_93618_;
    }

    protected String getLabel(DropDownItem item) {
        return item.getLabel();
    }

    @Override
    public void setSelected(DropDownItem selected) {
        this.selected = selected;
        this.onPress.onPress(selected);
        this.m_93666_(Component.literal(selected.getLabel()));
    }

    protected DropDownItem getSelected() {
        return this.selected;
    }

    protected String getSelectedLabel() {
        return this.getLabel(this.selected);
    }

    public static enum Glyph {

        CLOSED("▶"), UP("▲"), DOWN("▼");

        String code;

        private Glyph(String code) {
            this.code = code;
        }

        String getCode() {
            return this.code;
        }

        public String toString() {
            return this.code;
        }
    }
}