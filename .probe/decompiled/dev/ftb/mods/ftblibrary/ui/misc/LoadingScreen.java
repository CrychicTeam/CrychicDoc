package dev.ftb.mods.ftblibrary.ui.misc;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.ftb.mods.ftblibrary.icon.Color4I;
import dev.ftb.mods.ftblibrary.ui.BaseScreen;
import dev.ftb.mods.ftblibrary.ui.GuiHelper;
import dev.ftb.mods.ftblibrary.ui.Theme;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;

public class LoadingScreen extends BaseScreen {

    private boolean startedLoading = false;

    private boolean isLoading = true;

    private Component[] title;

    private float timer;

    public LoadingScreen() {
        this.setSize(128, 128);
        this.title = new Component[0];
    }

    public LoadingScreen(Component t) {
        this.setSize(128, 128);
        this.title = new Component[] { t };
    }

    @Override
    public void addWidgets() {
    }

    @Override
    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        if (!this.startedLoading) {
            this.startLoading();
            this.startedLoading = true;
        }
        if (this.isLoading()) {
            GuiHelper.drawHollowRect(graphics, x + this.width / 2 - 48, y + this.height / 2 - 8, 96, 16, Color4I.WHITE, true);
            int x1 = x + this.width / 2 - 48;
            int y1 = y + this.height / 2 - 8;
            int w1 = 96;
            int h1 = 16;
            Color4I col = Color4I.WHITE;
            RenderSystem.setShader(GameRenderer::m_172811_);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            GuiHelper.addRectToBuffer(graphics, buffer, x1, y1 + 1, 1, h1 - 2, col);
            GuiHelper.addRectToBuffer(graphics, buffer, x1 + w1 - 1, y1 + 1, 1, h1 - 2, col);
            GuiHelper.addRectToBuffer(graphics, buffer, x1 + 1, y1, w1 - 2, 1, col);
            GuiHelper.addRectToBuffer(graphics, buffer, x1 + 1, y1 + h1 - 1, w1 - 2, 1, col);
            x1++;
            y1++;
            w1 -= 2;
            h1 -= 2;
            this.timer = this.timer + Minecraft.getInstance().getDeltaFrameTime();
            this.timer %= (float) h1 * 2.0F;
            for (int oy = 0; oy < h1; oy++) {
                for (int ox = 0; ox < w1; ox++) {
                    int index = ox + oy + (int) this.timer;
                    if (index % (h1 * 2) < h1) {
                        col = Color4I.WHITE.withAlpha(200 - index % h1 * 9);
                        GuiHelper.addRectToBuffer(graphics, buffer, x1 + ox, y1 + oy, 1, 1, col);
                    }
                }
            }
            tesselator.end();
            Component[] s = this.getText();
            if (s.length > 0) {
                for (int i = 0; i < s.length; i++) {
                    theme.drawString(graphics, s[i], x + this.width / 2, y - 26 + i * 12, 4);
                }
            }
        } else {
            this.closeGui();
            this.finishLoading();
        }
    }

    public synchronized Component[] getText() {
        return this.title;
    }

    public synchronized void setText(Component... s) {
        this.title = s;
    }

    public synchronized void setFinished() {
        this.isLoading = false;
    }

    public void startLoading() {
    }

    public synchronized boolean isLoading() {
        return this.isLoading;
    }

    public void finishLoading() {
    }
}