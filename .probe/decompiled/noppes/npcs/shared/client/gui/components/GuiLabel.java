package noppes.npcs.shared.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import noppes.npcs.client.CustomNpcResourceListener;

public class GuiLabel extends AbstractWidget implements GuiEventListener {

    public int id;

    private boolean centered = false;

    public boolean enabled = true;

    private boolean labelBgEnabled;

    private int textColor;

    private int backColor;

    private int ulColor;

    private int brColor;

    private int border;

    public GuiLabel(int id, Component label, int color, int x, int y, int width, int height) {
        super(x, y, width, height, label);
        this.id = id;
        this.textColor = color;
    }

    public GuiLabel(int id, String s, int x, int y) {
        this(id, Component.translatable(s), CustomNpcResourceListener.DefaultTextColor, x, y, 40, 0);
    }

    public GuiLabel(int id, String s, int x, int y, int color) {
        this(id, Component.translatable(s), color, x, y, 40, 0);
    }

    public GuiLabel(int id, String s, int x, int y, int width, int height) {
        this(id, Component.translatable(s), CustomNpcResourceListener.DefaultTextColor, x, y, width, height);
        this.centered = true;
    }

    public GuiLabel(int id, String s, int x, int y, int color, int width, int height) {
        this(id, Component.translatable(s), color, x, y, width, height);
        this.centered = true;
    }

    public void setColor(int color) {
        this.textColor = color;
    }

    public void setCentered(boolean bo) {
        this.centered = bo;
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.enabled) {
            this.drawBox(graphics);
            int i = this.m_252907_() + this.f_93619_ / 2 + this.border / 2;
            if (this.centered) {
                graphics.drawString(Minecraft.getInstance().font, this.m_6035_(), (int) ((float) this.m_252754_() + (float) (this.f_93618_ - Minecraft.getInstance().font.width(this.m_6035_())) / 2.0F), this.m_252907_(), this.textColor, false);
            } else {
                graphics.drawString(Minecraft.getInstance().font, this.m_6035_(), this.m_252754_(), this.m_252907_(), this.textColor, false);
            }
        }
    }

    @Override
    protected void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
    }

    protected void drawBox(GuiGraphics graphics) {
        if (this.labelBgEnabled) {
            int i = this.f_93618_ + this.border * 2;
            int j = this.f_93619_ + this.border * 2;
            int k = this.m_252754_() - this.border;
            int l = this.m_252907_() - this.border;
            graphics.fill(k, l, k + i, l + j, this.backColor);
            graphics.hLine(k, k + i, l, this.ulColor);
            graphics.hLine(k, k + i, l + j, this.brColor);
            graphics.hLine(k, l, l + j, this.ulColor);
            graphics.hLine(k + i, l, l + j, this.brColor);
        }
    }
}