package se.mickelus.mutil.gui;

import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class GuiButton extends GuiClickable {

    private final GuiStringOutline textElement;

    private boolean enabled = true;

    private Component disabledTooltip;

    public GuiButton(int x, int y, int width, int height, String text, Runnable onClick) {
        super(x, y, width, height, onClick);
        this.textElement = new GuiStringOutline(0, (height - 8) / 2, text);
        this.addChild(this.textElement);
    }

    public GuiButton(int x, int y, String text, Runnable onClick) {
        this(x, y, Minecraft.getInstance().font.width(text), 10, text, onClick);
    }

    public GuiButton(int x, int y, int width, int height, String text, Runnable onClick, Component disabledTooltip) {
        this(x, y, width, height, text, onClick);
        this.disabledTooltip = disabledTooltip;
    }

    @Override
    public boolean onMouseClick(int x, int y, int button) {
        return this.enabled && super.onMouseClick(x, y, button);
    }

    private void updateColor() {
        if (!this.enabled) {
            this.textElement.setColor(8355711);
        } else if (this.hasFocus()) {
            this.textElement.setColor(16777164);
        } else {
            this.textElement.setColor(16777215);
        }
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        this.updateColor();
    }

    public void setText(String text) {
        this.textElement.setString(text);
        this.setWidth(Minecraft.getInstance().font.width(text));
    }

    @Override
    protected void onFocus() {
        this.updateColor();
    }

    @Override
    protected void onBlur() {
        this.updateColor();
    }

    @Override
    public List<Component> getTooltipLines() {
        return !this.enabled && this.disabledTooltip != null && this.hasFocus() ? Collections.singletonList(this.disabledTooltip) : null;
    }
}