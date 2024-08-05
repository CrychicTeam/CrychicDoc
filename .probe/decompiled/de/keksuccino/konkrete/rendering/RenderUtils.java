package de.keksuccino.konkrete.rendering;

import com.mojang.blaze3d.platform.NativeImage;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.DynamicTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class RenderUtils {

    private static ResourceLocation WHITE = null;

    private static ResourceLocation BLANK = null;

    public static ResourceLocation getWhiteImageResource() {
        if (WHITE != null) {
            return WHITE;
        } else if (Minecraft.getInstance().getTextureManager() == null) {
            return null;
        } else {
            NativeImage i = new NativeImage(1, 1, true);
            i.setPixelRGBA(0, 0, Color.WHITE.getRGB());
            ResourceLocation r = Minecraft.getInstance().getTextureManager().register("whiteback", new DynamicTexture(i));
            WHITE = r;
            return r;
        }
    }

    public static ResourceLocation getBlankImageResource() {
        if (BLANK != null) {
            return BLANK;
        } else if (Minecraft.getInstance().getTextureManager() == null) {
            return null;
        } else {
            NativeImage i = new NativeImage(1, 1, true);
            i.setPixelRGBA(0, 0, new Color(255, 255, 255, 0).getRGB());
            ResourceLocation r = Minecraft.getInstance().getTextureManager().register("blankback", new DynamicTexture(i));
            BLANK = r;
            return r;
        }
    }

    public static void setScale(GuiGraphics graphics, float scale) {
        setScale(graphics.pose(), scale);
    }

    public static void setScale(PoseStack graphics, float scale) {
        graphics.pushPose();
        graphics.scale(scale, scale, scale);
    }

    public static void postScale(GuiGraphics graphics) {
        postScale(graphics.pose());
    }

    public static void postScale(PoseStack graphics) {
        graphics.popPose();
    }

    public static void doubleBlit(double x, double y, float f1, float f2, int w, int h) {
        innerDoubleBlit(x, x + (double) w, y, y + (double) h, 0, (f1 + 0.0F) / (float) w, (f1 + (float) w) / (float) w, (f2 + 0.0F) / (float) h, (f2 + (float) h) / (float) h);
    }

    public static void innerDoubleBlit(double x, double xEnd, double y, double yEnd, int z, float f1, float f2, float f3, float f4) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferbuilder.m_5483_(x, yEnd, (double) z).uv(f1, f4).endVertex();
        bufferbuilder.m_5483_(xEnd, yEnd, (double) z).uv(f2, f4).endVertex();
        bufferbuilder.m_5483_(xEnd, y, (double) z).uv(f2, f3).endVertex();
        bufferbuilder.m_5483_(x, y, (double) z).uv(f1, f3).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }

    public static Color getColorFromHexString(String hex) {
        try {
            hex = hex.replace("#", "");
            if (hex.length() == 6) {
                return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16));
            }
            if (hex.length() == 8) {
                return new Color(Integer.valueOf(hex.substring(0, 2), 16), Integer.valueOf(hex.substring(2, 4), 16), Integer.valueOf(hex.substring(4, 6), 16), Integer.valueOf(hex.substring(6, 8), 16));
            }
        } catch (Exception var2) {
            var2.printStackTrace();
        }
        return null;
    }

    public static void setZLevelPre(GuiGraphics graphics, int zLevel) {
        setZLevelPre(graphics.pose(), zLevel);
    }

    public static void setZLevelPre(PoseStack graphics, int zLevel) {
        RenderSystem.disableDepthTest();
        graphics.pushPose();
        graphics.translate(0.0, 0.0, (double) zLevel);
    }

    public static void setZLevelPost(GuiGraphics graphics) {
        setZLevelPost(graphics.pose());
    }

    public static void setZLevelPost(PoseStack graphics) {
        graphics.popPose();
        RenderSystem.enableDepthTest();
    }

    public static void bindTexture(ResourceLocation texture, boolean depthTest) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.enableBlend();
        if (depthTest) {
            RenderSystem.enableDepthTest();
        }
    }

    public static void bindTexture(ResourceLocation texture) {
        bindTexture(texture, false);
    }

    public static void fill(GuiGraphics graphics, float minX, float minY, float maxX, float maxY, int color, float opacity) {
        fill(graphics.pose(), minX, minY, maxX, maxY, color, opacity);
    }

    public static void fill(PoseStack graphics, float minX, float minY, float maxX, float maxY, int color, float opacity) {
        Matrix4f graphics4f = graphics.last().pose();
        if (minX < maxX) {
            float i = minX;
            minX = maxX;
            maxX = i;
        }
        if (minY < maxY) {
            float j = minY;
            minY = maxY;
            maxY = j;
        }
        float r = (float) (color >> 16 & 0xFF) / 255.0F;
        float g = (float) (color >> 8 & 0xFF) / 255.0F;
        float b = (float) (color & 0xFF) / 255.0F;
        float a = (float) (color >> 24 & 0xFF) / 255.0F;
        a *= opacity;
        BufferBuilder bb = Tesselator.getInstance().getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.setShader(GameRenderer::m_172811_);
        bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bb.m_252986_(graphics4f, minX, maxY, 0.0F).color(r, g, b, a).endVertex();
        bb.m_252986_(graphics4f, maxX, maxY, 0.0F).color(r, g, b, a).endVertex();
        bb.m_252986_(graphics4f, maxX, minY, 0.0F).color(r, g, b, a).endVertex();
        bb.m_252986_(graphics4f, minX, minY, 0.0F).color(r, g, b, a).endVertex();
        BufferUploader.drawWithShader(bb.end());
        RenderSystem.disableBlend();
    }
}