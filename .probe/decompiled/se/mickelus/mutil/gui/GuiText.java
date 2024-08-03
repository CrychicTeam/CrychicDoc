package se.mickelus.mutil.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class GuiText extends GuiElement {

    Font fontRenderer;

    String string;

    int color = 16777215;

    public GuiText(int x, int y, int width, String string) {
        super(x, y, width, 0);
        this.fontRenderer = Minecraft.getInstance().font;
        this.setString(string);
    }

    public void setString(String string) {
        this.string = string.replace("\\n", "\n");
        this.height = this.fontRenderer.wordWrapHeight(this.string, this.width);
    }

    public void setColor(int color) {
        this.color = color;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        renderText(graphics, this.fontRenderer, this.string, refX + this.x, refY + this.y, this.width, this.color, opacity);
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }

    protected static void renderText(GuiGraphics graphics, Font fontRenderer, String string, int x, int y, int width, int color, float opacity) {
        for (FormattedCharSequence line : fontRenderer.split(Component.literal(string), width)) {
            graphics.drawString(fontRenderer, line, x, y, colorWithOpacity(color, opacity));
            y += 9;
        }
    }
}