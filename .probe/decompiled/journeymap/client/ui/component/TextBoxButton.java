package journeymap.client.ui.component;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class TextBoxButton extends Button {

    protected TextBox textBox;

    public TextBoxButton(String text) {
        super(text);
    }

    public TextBoxButton(Object text, Font fontRenderer, int width, int height) {
        this(text, fontRenderer, width, height, false, false);
    }

    public TextBoxButton(Object text, Font fontRenderer, int width, int height, boolean isNumeric, boolean negative) {
        super(text.toString());
        this.textBox = new TextBox(text, fontRenderer, width, height - 4, isNumeric, negative);
    }

    public String getText() {
        return this.textBox.m_94155_();
    }

    public String getSelectedText() {
        return this.textBox.m_94173_();
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.textBox.setMinLength(1);
        this.textBox.setX(this.m_252754_());
        this.textBox.setY(this.m_252907_());
        this.textBox.render(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        this.textBox.mouseReleased(mouseX, mouseY, mouseButton);
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        this.textBox.mouseDragged(mouseX, mouseY, button, mouseDX, mouseDY);
        return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        return this.textBox.mouseClicked(mouseX, mouseY, 0);
    }

    @Override
    public boolean charTyped(char typedChar, int keyCode) {
        return this.textBox.charTyped(typedChar, keyCode);
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        return this.textBox.keyPressed(key, value, modifier);
    }

    @Override
    public boolean isFocused() {
        return this.textBox.m_93696_();
    }

    @Override
    public boolean isActive() {
        return this.textBox.m_142518_();
    }

    @Override
    public void setFocused(boolean focused) {
        this.textBox.setFocused(focused);
    }

    @Override
    public boolean isHoveredOrFocused() {
        return this.textBox.isHovered() || this.textBox.m_93696_();
    }

    @Override
    public void setVisible(boolean visible) {
        this.textBox.m_94194_(visible);
        super.setVisible(visible);
    }

    @Override
    public int getCenterX() {
        return this.textBox.getCenterX();
    }

    @Override
    public int getRightX() {
        return this.textBox.getRightX();
    }

    @Override
    public int getBottomY() {
        return this.textBox.getBottomY();
    }

    @Override
    public int getMiddleY() {
        return this.textBox.getMiddleY();
    }

    @Override
    public int getWidth() {
        return this.textBox != null ? this.textBox.getWidth() : this.f_93618_;
    }

    @Override
    public void setWidth(int width) {
        if (this.textBox != null) {
            this.textBox.setWidth(width);
        }
        this.f_93618_ = width;
    }

    @Override
    public int getHeight() {
        return this.textBox != null ? this.textBox.getHeight() : this.f_93619_;
    }

    public void setText(String text) {
        this.textBox.m_94144_(text);
    }
}