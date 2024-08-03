package noppes.npcs.shared.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import noppes.npcs.shared.client.gui.listeners.IGuiInterface;

public class GuiButtonNop extends Button {

    public boolean shown = true;

    public IGuiInterface gui;

    protected String[] display;

    private int displayValue = 0;

    public int id;

    protected static final Button.OnPress clicked = button -> {
        GuiButtonNop b = (GuiButtonNop) button;
        b.gui.buttonEvent(b);
    };

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, String s) {
        super(j, k, 200, 20, Component.translatable(s), clicked, Button.DEFAULT_NARRATION);
        this.id = i;
        this.gui = gui;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, String[] display, int val) {
        this(gui, i, j, k, display[val]);
        this.display = display;
        this.displayValue = val;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, String string) {
        super(j, k, l, m, Component.translatable(string), clicked, Button.DEFAULT_NARRATION);
        this.id = i;
        this.gui = gui;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, String string, Button.OnPress clicked) {
        super(j, k, l, m, Component.translatable(string), clicked, Button.DEFAULT_NARRATION);
        this.id = i;
        this.gui = gui;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, String string, boolean enabled) {
        this(gui, i, j, k, l, m, string);
        this.f_93623_ = enabled;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, String[] display, int val) {
        this(gui, i, j, k, l, m, val, display);
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, int val, String... display) {
        this(gui, i, j, k, l, m, display.length == 0 ? "" : display[val % display.length]);
        this.display = display;
        this.displayValue = display.length == 0 ? 0 : val % display.length;
    }

    public GuiButtonNop(IGuiInterface gui, int i, int j, int k, int l, int m, Button.OnPress clicked, int val, String... display) {
        this(gui, i, j, k, l, m, display.length == 0 ? "" : display[val % display.length], clicked);
        this.display = display;
        this.displayValue = display.length == 0 ? 0 : val % display.length;
    }

    public void setDisplayText(String text) {
        this.m_93666_(Component.translatable(text));
    }

    public int getValue() {
        return this.displayValue;
    }

    public void clicked() {
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.shown) {
            super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        }
    }

    @Override
    public void onClick(double x, double y) {
        if (!this.gui.hasSubGui()) {
            if (this.display != null) {
                this.setDisplay((this.displayValue + 1) % this.display.length);
            }
            super.onPress();
        }
    }

    public void setDisplay(int value) {
        this.displayValue = value;
        this.setDisplayText(this.display[value]);
    }

    public void setEnabled(boolean bo) {
        this.f_93623_ = bo;
    }
}