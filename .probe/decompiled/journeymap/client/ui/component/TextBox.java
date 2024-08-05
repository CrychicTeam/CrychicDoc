package journeymap.client.ui.component;

import info.journeymap.shaded.org.jetbrains.annotations.NotNull;
import java.awt.Color;
import journeymap.client.Constants;
import net.minecraft.Util;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.util.Mth;

public class TextBox extends EditBox {

    protected final String numericRegex;

    protected final boolean numeric;

    protected final boolean allowNegative;

    protected int minLength;

    protected Integer clampMin;

    protected Integer clampMax;

    private long lastClick = 0L;

    public TextBox(Object text, Font fontRenderer, int width, int height) {
        this(text, fontRenderer, width, height, false, false);
    }

    public TextBox(Object text, Font fontRenderer, int width, int height, boolean isNumeric, boolean negative) {
        super(fontRenderer, 0, 0, width, height, Constants.getStringTextComponent(text.toString()));
        this.m_94199_(256);
        this.m_94144_(text.toString());
        this.numeric = isNumeric;
        this.allowNegative = negative;
        String regex = null;
        if (this.numeric) {
            if (this.allowNegative) {
                regex = "[^-?\\d]";
            } else {
                regex = "[^\\d]";
            }
        }
        this.numericRegex = regex;
    }

    public void setClamp(Integer min, Integer max) {
        this.clampMin = min;
        this.clampMax = max;
    }

    public void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    @Override
    public void insertText(@NotNull String par1Str) {
        if (this.numeric) {
            String fixed = par1Str.replaceAll(this.numericRegex, "");
            if (this.allowNegative) {
                par1Str = "-".equals(fixed) && this.m_94207_() != 0 && !this.isAllSelected() ? "" : fixed;
            }
        }
        super.insertText(par1Str);
    }

    public void setText(Object object) {
        super.setValue(object.toString());
    }

    public boolean isNumeric() {
        return this.numeric;
    }

    public boolean hasMinLength() {
        String text = this.m_94155_();
        int textLen = text == null ? 0 : text.length();
        return this.minLength <= textLen;
    }

    @Override
    public boolean charTyped(char c, int key) {
        boolean typed = false;
        if (this.m_198029_()) {
            typed = super.charTyped(c, key);
            if (this.numeric && typed) {
                this.clamp();
            }
        }
        return typed;
    }

    @Override
    public boolean keyPressed(int key, int value, int modifier) {
        boolean pressed = false;
        if (this.m_198029_()) {
            pressed = super.keyPressed(key, value, modifier);
        }
        return pressed;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int mouseButton) {
        return super.m_6348_(mouseX, mouseY, mouseButton);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseDX, double mouseDY) {
        if (this.m_5953_(mouseX, mouseY)) {
            if (this.m_93696_() && button == 0) {
                int i = Mth.floor(mouseX) - super.m_252754_();
                if (this.m_94219_()) {
                    i -= 4;
                }
                String s = this.f_94092_.plainSubstrByWidth(this.m_94155_().substring(this.f_94100_), this.m_94210_());
                this.m_94208_(this.f_94092_.plainSubstrByWidth(s, i).length() + this.f_94100_);
                return true;
            } else {
                return super.m_7979_(mouseX, mouseY, button, mouseDX, mouseDY);
            }
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int mouseButton) {
        super.setHighlightPos(super.getCursorPosition());
        if (this.m_5953_(mouseX, mouseY) && super.m_6375_(mouseX, mouseY, mouseButton)) {
            long sysTime = Util.getMillis();
            boolean doubleClick = sysTime - this.lastClick < 200L;
            this.lastClick = sysTime;
            if (doubleClick) {
                this.selectAll();
            }
            this.setFocused(true);
            return true;
        } else {
            this.setFocused(false);
            return false;
        }
    }

    @Override
    public void setFocused(boolean focused) {
        super.setFocused(focused);
    }

    public void selectAll() {
        this.m_94201_();
        this.m_94208_(0);
    }

    public boolean isAllSelected() {
        return this.m_94155_().equals(this.m_94173_());
    }

    @Override
    public void setCursorPosition(int position) {
        super.setCursorPosition(position);
    }

    @Override
    public boolean isHovered() {
        return this.f_93622_;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.m_88315_(graphics, mouseX, mouseY, partialTicks);
        if (this.m_94213_() && !this.hasMinLength()) {
            int red = Color.red.getRGB();
            int x1 = this.getX() - 1;
            int y1 = this.getY() - 1;
            int x2 = x1 + this.getWidth() + 1;
            int y2 = y1 + this.getHeight() + 1;
            graphics.fill(x1, y1, x2, y1 + 1, red);
            graphics.fill(x1, y2, x2, y2 + 1, red);
            graphics.fill(x1, y1, x1 + 1, y2, red);
            graphics.fill(x2, y1, x2 + 1, y2, red);
        }
    }

    public Integer clamp() {
        return this.clamp(this.m_94155_());
    }

    public Integer clamp(String text) {
        if (!this.numeric) {
            return null;
        } else {
            int val;
            if (this.clampMin != null) {
                if (text == null || text.length() == 0 || text.equals("-")) {
                    return null;
                }
                try {
                    val = Math.max(this.clampMin, Integer.parseInt(text));
                } catch (Exception var6) {
                    return this.clampMin;
                }
                if (this.clampMax != null) {
                    try {
                        val = Math.min(this.clampMax, val);
                    } catch (Exception var5) {
                        return this.clampMax;
                    }
                }
            } else {
                try {
                    val = Integer.parseInt(text);
                } catch (Exception var4) {
                    return null;
                }
            }
            if (val != Integer.parseInt(text)) {
                this.setText(val);
            }
            return val;
        }
    }

    @Override
    public int getX() {
        return super.m_252754_();
    }

    @Override
    public void setX(int x) {
        super.m_252865_(x);
    }

    @Override
    public int getY() {
        return super.m_252907_();
    }

    @Override
    public void setY(int y) {
        super.m_253211_(y);
    }

    @Override
    public int getWidth() {
        return this.f_93618_;
    }

    @Override
    public void setWidth(int w) {
        this.f_93618_ = w;
    }

    @Override
    public int getHeight() {
        return this.f_93619_;
    }

    public void setHeight(int h) {
        this.f_93619_ = h;
    }

    public int getCenterX() {
        return this.getX() + this.getWidth() / 2;
    }

    public int getMiddleY() {
        return this.getY() + this.getHeight() / 2;
    }

    public int getBottomY() {
        return this.getY() + this.getHeight();
    }

    public int getRightX() {
        return this.getX() + this.getWidth();
    }
}