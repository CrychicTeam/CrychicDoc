package com.mrcrayfish.configured.client.screen;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mrcrayfish.configured.client.screen.widget.IconButton;
import com.mrcrayfish.configured.client.util.ScreenUtil;
import java.util.List;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;

public class ConfirmationScreen extends Screen implements IBackgroundTexture {

    private static final int FADE_LENGTH = 4;

    private static final int BRIGHTNESS = 32;

    private static final int MESSAGE_PADDING = 10;

    private final Screen parent;

    private final Component message;

    private final ConfirmationScreen.Icon icon;

    private final Function<Boolean, Boolean> handler;

    private Component positiveText = CommonComponents.GUI_YES;

    private Component negativeText = CommonComponents.GUI_NO;

    private ResourceLocation background = Screen.BACKGROUND_LOCATION;

    private int startY;

    private int endY;

    public ConfirmationScreen(Screen parent, Component message, ConfirmationScreen.Icon icon, Function<Boolean, Boolean> handler) {
        super(message);
        this.parent = parent;
        this.message = message;
        this.icon = icon;
        this.handler = handler;
    }

    @Override
    protected void init() {
        List<FormattedCharSequence> lines = this.f_96547_.split(this.message, 300);
        this.startY = this.f_96544_ / 2 - 10 - lines.size() * (9 + 2) / 2 - 10 - 1;
        this.endY = this.startY + lines.size() * (9 + 2) + 20;
        int offset = this.negativeText != null ? 105 : 50;
        this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 - offset, this.endY + 10, 100, 20, this.positiveText, button -> {
            if ((Boolean) this.handler.apply(true)) {
                this.f_96541_.setScreen(this.parent);
            }
        }));
        if (this.negativeText != null) {
            this.m_142416_(ScreenUtil.button(this.f_96543_ / 2 + 5, this.endY + 10, 100, 20, this.negativeText, button -> {
                if ((Boolean) this.handler.apply(false)) {
                    this.f_96541_.setScreen(this.parent);
                }
            }));
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        List<FormattedCharSequence> lines = this.f_96547_.split(this.message, 300);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        graphics.blit(IconButton.ICONS, this.f_96543_ / 2 - 10, this.startY - 30, 20, 20, (float) this.icon.u(), (float) this.icon.v(), 10, 10, 64, 64);
        drawListBackground(0.0, (double) this.f_96543_, (double) this.startY, (double) this.endY);
        for (int i = 0; i < lines.size(); i++) {
            int lineWidth = this.f_96547_.width((FormattedCharSequence) lines.get(i));
            graphics.drawString(this.f_96547_, (FormattedCharSequence) lines.get(i), this.f_96543_ / 2 - lineWidth / 2, this.startY + 10 + i * (9 + 2) + 1, 16777215);
        }
    }

    @Override
    public ResourceLocation getBackgroundTexture() {
        return this.background;
    }

    public void setPositiveText(Component positiveText) {
        this.positiveText = positiveText;
    }

    public void setNegativeText(@Nullable Component negativeText) {
        this.negativeText = negativeText;
    }

    public ConfirmationScreen setBackground(ResourceLocation background) {
        this.background = background;
        return this;
    }

    public static void drawListBackground(double startX, double endX, double startY, double endY) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder buffer = tesselator.getBuilder();
        RenderSystem.setShader(GameRenderer::m_172820_);
        RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        buffer.m_5483_(startX, endY, 0.0).uv((float) startX / 32.0F, (float) endY / 32.0F).color(32, 32, 32, 255).endVertex();
        buffer.m_5483_(endX, endY, 0.0).uv((float) endX / 32.0F, (float) endY / 32.0F).color(32, 32, 32, 255).endVertex();
        buffer.m_5483_(endX, startY, 0.0).uv((float) endX / 32.0F, (float) startY / 32.0F).color(32, 32, 32, 255).endVertex();
        buffer.m_5483_(startX, startY, 0.0).uv((float) startX / 32.0F, (float) startY / 32.0F).color(32, 32, 32, 255).endVertex();
        tesselator.end();
        RenderSystem.depthFunc(515);
        RenderSystem.disableDepthTest();
        RenderSystem.enableBlend();
        RenderSystem.blendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ZERO, GlStateManager.DestFactor.ONE);
        RenderSystem.setShader(GameRenderer::m_172811_);
        buffer.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        buffer.m_5483_(startX, startY + 4.0, 0.0).color(0, 0, 0, 0).endVertex();
        buffer.m_5483_(endX, startY + 4.0, 0.0).color(0, 0, 0, 0).endVertex();
        buffer.m_5483_(endX, startY, 0.0).color(0, 0, 0, 255).endVertex();
        buffer.m_5483_(startX, startY, 0.0).color(0, 0, 0, 255).endVertex();
        buffer.m_5483_(startX, endY, 0.0).color(0, 0, 0, 255).endVertex();
        buffer.m_5483_(endX, endY, 0.0).color(0, 0, 0, 255).endVertex();
        buffer.m_5483_(endX, endY - 4.0, 0.0).color(0, 0, 0, 0).endVertex();
        buffer.m_5483_(startX, endY - 4.0, 0.0).color(0, 0, 0, 0).endVertex();
        tesselator.end();
    }

    public static enum Icon {

        INFO(11, 44), WARNING(0, 11), ERROR(11, 11);

        private final int u;

        private final int v;

        private Icon(int u, int v) {
            this.u = u;
            this.v = v;
        }

        public int u() {
            return this.u;
        }

        public int v() {
            return this.v;
        }
    }
}