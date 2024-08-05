package snownee.lychee.client.gui;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;
import snownee.lychee.util.Color;

public class UIRenderHelper {

    public static UIRenderHelper.CustomRenderTarget framebuffer;

    public static void init() {
        RenderSystem.recordRenderCall(() -> {
            Window mainWindow = Minecraft.getInstance().getWindow();
            framebuffer = UIRenderHelper.CustomRenderTarget.create(mainWindow);
        });
    }

    public static void updateWindowSize(Window mainWindow) {
        if (framebuffer != null) {
            framebuffer.m_83941_(mainWindow.getWidth(), mainWindow.getHeight(), Minecraft.ON_OSX);
        }
    }

    public static void drawFramebuffer(float alpha) {
        framebuffer.renderWithAlpha(alpha);
    }

    public static void swapAndBlitColor(RenderTarget src, RenderTarget dst) {
        GlStateManager._glBindFramebuffer(36008, src.frameBufferId);
        GlStateManager._glBindFramebuffer(36009, dst.frameBufferId);
        GlStateManager._glBlitFrameBuffer(0, 0, src.viewWidth, src.viewHeight, 0, 0, dst.viewWidth, dst.viewHeight, 16384, 9729);
        GlStateManager._glBindFramebuffer(36160, dst.frameBufferId);
    }

    public static void drawColoredTexture(GuiGraphics graphics, Color c, int x, int y, int tex_left, int tex_top, int width, int height) {
        drawColoredTexture(graphics, c, x, y, 0, (float) tex_left, (float) tex_top, width, height, 256, 256);
    }

    public static void drawColoredTexture(GuiGraphics graphics, Color c, int x, int y, int z, float tex_left, float tex_top, int width, int height, int sheet_width, int sheet_height) {
        drawColoredTexture(graphics, c, x, x + width, y, y + height, z, width, height, tex_left, tex_top, sheet_width, sheet_height);
    }

    public static void drawStretched(GuiGraphics graphics, int left, int top, int w, int h, int z, AllGuiTextures tex) {
        tex.bind();
        drawTexturedQuad(graphics.pose().last().pose(), Color.WHITE, left, left + w, top, top + h, z, (float) tex.startX / 256.0F, (float) (tex.startX + tex.width) / 256.0F, (float) tex.startY / 256.0F, (float) (tex.startY + tex.height) / 256.0F);
    }

    public static void drawCropped(GuiGraphics graphics, int left, int top, int w, int h, int z, AllGuiTextures tex) {
        tex.bind();
        drawTexturedQuad(graphics.pose().last().pose(), Color.WHITE, left, left + w, top, top + h, z, (float) tex.startX / 256.0F, (float) (tex.startX + w) / 256.0F, (float) tex.startY / 256.0F, (float) (tex.startY + h) / 256.0F);
    }

    private static void drawColoredTexture(GuiGraphics graphics, Color c, int left, int right, int top, int bot, int z, int tex_width, int tex_height, float tex_left, float tex_top, int sheet_width, int sheet_height) {
        drawTexturedQuad(graphics.pose().last().pose(), c, left, right, top, bot, z, (tex_left + 0.0F) / (float) sheet_width, (tex_left + (float) tex_width) / (float) sheet_width, (tex_top + 0.0F) / (float) sheet_height, (tex_top + (float) tex_height) / (float) sheet_height);
    }

    private static void drawTexturedQuad(Matrix4f m, Color c, int left, int right, int top, int bot, int z, float u1, float u2, float v1, float v2) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172814_);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.m_252986_(m, (float) left, (float) bot, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u1, v2).endVertex();
        bufferbuilder.m_252986_(m, (float) right, (float) bot, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u2, v2).endVertex();
        bufferbuilder.m_252986_(m, (float) right, (float) top, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u2, v1).endVertex();
        bufferbuilder.m_252986_(m, (float) left, (float) top, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u1, v1).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
    }

    public static void flipForGuiRender(PoseStack poseStack) {
        poseStack.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
    }

    public static class CustomRenderTarget extends RenderTarget {

        public CustomRenderTarget(boolean useDepth) {
            super(useDepth);
        }

        public static UIRenderHelper.CustomRenderTarget create(Window mainWindow) {
            UIRenderHelper.CustomRenderTarget framebuffer = new UIRenderHelper.CustomRenderTarget(true);
            framebuffer.m_83941_(mainWindow.getWidth(), mainWindow.getHeight(), Minecraft.ON_OSX);
            framebuffer.m_83931_(0.0F, 0.0F, 0.0F, 0.0F);
            framebuffer.enableStencil();
            return framebuffer;
        }

        public void renderWithAlpha(float alpha) {
            Window window = Minecraft.getInstance().getWindow();
            float vx = (float) window.getGuiScaledWidth();
            float vy = (float) window.getGuiScaledHeight();
            float tx = (float) this.f_83917_ / (float) this.f_83915_;
            float ty = (float) this.f_83918_ / (float) this.f_83916_;
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(() -> Minecraft.getInstance().gameRenderer.blitShader);
            RenderSystem.getShader().setSampler("DiffuseSampler", this.f_83923_);
            this.m_83956_();
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferbuilder.m_5483_(0.0, (double) vy, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.m_5483_((double) vx, (double) vy, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, 0.0F).endVertex();
            bufferbuilder.m_5483_((double) vx, 0.0, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, ty).endVertex();
            bufferbuilder.m_5483_(0.0, 0.0, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, ty).endVertex();
            tessellator.end();
            this.m_83963_();
        }
    }
}