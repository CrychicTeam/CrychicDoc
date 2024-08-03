package de.keksuccino.konkrete.gui.screens.popup;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.konkrete.gui.content.AdvancedButton;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;

public abstract class Popup {

    private boolean displayed = false;

    private int alpha;

    private List<AdvancedButton> buttons = new ArrayList();

    public Popup(int backgroundAlpha) {
        this.alpha = backgroundAlpha;
    }

    public void render(GuiGraphics graphics, int mouseX, int mouseY, Screen renderIn) {
        if (this.isDisplayed()) {
            RenderSystem.enableBlend();
            graphics.fill(0, 0, renderIn.width, renderIn.height, new Color(0, 0, 0, this.alpha).getRGB());
            RenderSystem.disableBlend();
        }
    }

    public boolean isDisplayed() {
        return this.displayed;
    }

    public void setDisplayed(boolean b) {
        this.displayed = b;
    }

    public List<AdvancedButton> getButtons() {
        return this.buttons;
    }

    protected void addButton(AdvancedButton b) {
        if (!this.buttons.contains(b)) {
            this.buttons.add(b);
            b.ignoreBlockedInput = true;
            this.colorizePopupButton(b);
        }
    }

    protected void removeButton(AdvancedButton b) {
        if (this.buttons.contains(b)) {
            this.buttons.remove(b);
        }
    }

    protected void renderButtons(GuiGraphics graphics, int mouseX, int mouseY) {
        for (AdvancedButton b : this.buttons) {
            b.m_88315_(graphics, mouseX, mouseY, Minecraft.getInstance().getFrameTime());
        }
    }

    protected void colorizePopupButton(AdvancedButton b) {
        b.setBackgroundColor(new Color(102, 102, 153), new Color(133, 133, 173), new Color(163, 163, 194), new Color(163, 163, 194), 1);
    }
}