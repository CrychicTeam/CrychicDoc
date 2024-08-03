package dev.shadowsoffire.placebo.util;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import org.joml.Matrix4f;

public class ClientUtil {

    public static void colorBlit(PoseStack pMatrixStack, float pX, float pY, float pUOffset, float pVOffset, float pUWidth, float pVHeight, int color) {
        innerBlit(pMatrixStack, pX, pX + pUWidth, pY, pY + pVHeight, 0, pUWidth, pVHeight, pUOffset, pVOffset, 256, 256, color);
    }

    public static void colorBlit(PoseStack pMatrixStack, int pX, int pY, int pBlitOffset, int pWidth, int pHeight, TextureAtlasSprite pSprite, int color) {
        innerBlit(pMatrixStack.last().pose(), (float) pX, (float) (pX + pWidth), (float) pY, (float) (pY + pHeight), pBlitOffset, pSprite.getU0(), pSprite.getU1(), pSprite.getV0(), pSprite.getV1(), color);
    }

    public static void colorBlit(PoseStack pPoseStack, int pX, int pY, int pWidth, int pHeight, float pUOffset, float pVOffset, int pUWidth, int pVHeight, int pTextureWidth, int pTextureHeight, int color) {
        innerBlit(pPoseStack, (float) pX, (float) (pX + pWidth), (float) pY, (float) (pY + pHeight), 0, (float) pUWidth, (float) pVHeight, pUOffset, pVOffset, pTextureWidth, pTextureHeight, color);
    }

    public static void innerBlit(PoseStack pMatrixStack, float pX1, float pX2, float pY1, float pY2, int pBlitOffset, float pUWidth, float pVHeight, float pUOffset, float pVOffset, int pTextureWidth, int pTextureHeight, int color) {
        innerBlit(pMatrixStack.last().pose(), pX1, pX2, pY1, pY2, pBlitOffset, (pUOffset + 0.0F) / (float) pTextureWidth, (pUOffset + pUWidth) / (float) pTextureWidth, (pVOffset + 0.0F) / (float) pTextureHeight, (pVOffset + pVHeight) / (float) pTextureHeight, color);
    }

    public static void innerBlit(Matrix4f pMatrix, float pX1, float pX2, float pY1, float pY2, int pBlitOffset, float pMinU, float pMaxU, float pMinV, float pMaxV, int color) {
        RenderSystem.setShader(GameRenderer::m_172814_);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        int a = color >> 24 & 0xFF;
        int r = color >> 16 & 0xFF;
        int g = color >> 8 & 0xFF;
        int b = color & 0xFF;
        if (a == 0) {
            a = 255;
        }
        bufferbuilder.m_252986_(pMatrix, pX1, pY2, (float) pBlitOffset).color(r, g, b, a).uv(pMinU, pMaxV).endVertex();
        bufferbuilder.m_252986_(pMatrix, pX2, pY2, (float) pBlitOffset).color(r, g, b, a).uv(pMaxU, pMaxV).endVertex();
        bufferbuilder.m_252986_(pMatrix, pX2, pY1, (float) pBlitOffset).color(r, g, b, a).uv(pMaxU, pMinV).endVertex();
        bufferbuilder.m_252986_(pMatrix, pX1, pY1, (float) pBlitOffset).color(r, g, b, a).uv(pMinU, pMinV).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
    }
}