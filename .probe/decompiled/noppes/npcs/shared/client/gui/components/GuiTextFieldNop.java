package noppes.npcs.shared.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import noppes.npcs.shared.client.gui.listeners.ITextfieldListener;

public class GuiTextFieldNop extends EditBox {

    public boolean enabled = true;

    public boolean inMenu = true;

    public boolean numbersOnly = false;

    public ITextfieldListener listener;

    public int id;

    public int min = 0;

    public int max = Integer.MAX_VALUE;

    public int def = 0;

    private static GuiTextFieldNop activeTextfield = null;

    private final int[] allowedSpecialChars = new int[] { 14, 211, 203, 205 };

    public GuiTextFieldNop(int id, Screen parent, int i, int j, int k, int l, String s) {
        this(id, parent, i, j, k, l, Component.translatable(s != null ? s : ""));
    }

    public GuiTextFieldNop(int id, Screen parent, int i, int j, int k, int l, Component s) {
        super(Minecraft.getInstance().font, i, j, k, l, s);
        this.m_94199_(500);
        if (!s.getString().isEmpty()) {
            this.m_94144_(s.getString());
        }
        this.id = id;
        if (parent instanceof ITextfieldListener) {
            this.listener = (ITextfieldListener) parent;
        }
    }

    public static boolean isAnyActive() {
        return activeTextfield != null;
    }

    public static GuiTextFieldNop getActive() {
        return activeTextfield;
    }

    private boolean charAllowed(char c, int i) {
        if (this.numbersOnly && !Character.isDigit(c)) {
            for (int j : this.allowedSpecialChars) {
                if (j == i) {
                    return true;
                }
            }
            return false;
        } else {
            return true;
        }
    }

    @Override
    public boolean charTyped(char c, int i) {
        return !this.charAllowed(c, i) ? false : super.charTyped(c, i);
    }

    public boolean isEmpty() {
        return this.m_94155_().trim().length() == 0;
    }

    public int getInteger() {
        return Integer.parseInt(this.m_94155_());
    }

    public boolean isInteger() {
        try {
            Integer.parseInt(this.m_94155_());
            return true;
        } catch (NumberFormatException var2) {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double i, double j, int k) {
        if (!this.enabled) {
            return false;
        } else {
            boolean wasFocused = this.m_93696_();
            boolean flag = i >= (double) this.m_252754_() && i < (double) (this.m_252754_() + this.f_93618_) && j >= (double) this.m_252907_() && j < (double) (this.m_252907_() + this.f_93619_);
            this.m_93692_(flag);
            boolean clicked = super.m_6375_(i, j, k);
            if (!wasFocused && this.m_93696_()) {
                unfocus();
                activeTextfield = this;
            }
            if (wasFocused && !this.m_93696_()) {
                this.unFocused();
            }
            return clicked;
        }
    }

    public void unFocused() {
        if (this.numbersOnly) {
            if (!this.isEmpty() && this.isInteger()) {
                if (this.getInteger() < this.min) {
                    this.m_94144_(this.min + "");
                } else if (this.getInteger() > this.max) {
                    this.m_94144_(this.max + "");
                }
            } else {
                this.m_94144_(this.def + "");
            }
        }
        if (this.listener != null) {
            this.listener.unFocused(this);
        }
        if (this == activeTextfield) {
            activeTextfield = null;
        }
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int x, int y, float f) {
        if (this.enabled) {
            super.renderWidget(graphics, y, x, f);
        }
    }

    public void setMinMaxDefault(int i, int j, int k) {
        this.min = i;
        this.max = j;
        this.def = k;
    }

    public static void unfocus() {
        GuiTextFieldNop field = activeTextfield;
        activeTextfield = null;
        if (field != null) {
            field.unFocused();
        }
    }

    public GuiTextFieldNop setNumbersOnly() {
        this.numbersOnly = true;
        return this;
    }
}