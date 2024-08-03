package com.rekindled.embers.util;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.rekindled.embers.EmbersClientEvents;
import com.rekindled.embers.render.EmbersRenderTypes;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.joml.Matrix4f;

@OnlyIn(Dist.CLIENT)
public class RenderUtil {

    public static void renderWavyEmberLine(BufferBuilder b, double x1, double y1, double x2, double y2, double thickness) {
        renderWavyEmberLine(b, x1, y1, x2, y2, thickness, 1.0, new Color(255, 64, 16));
    }

    public static void renderWavyEmberLine(BufferBuilder b, double x1, double y1, double x2, double y2, double thickness, double density, Color color) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        double angleRads = Math.atan2(y2 - y1, x2 - x1);
        double dist = Math.sqrt(dx * dx + dy * dy);
        double orthoX = Math.cos(angleRads + (Math.PI / 2));
        double orthoY = Math.sin(angleRads + (Math.PI / 2));
        for (int i = 0; i <= 10; i++) {
            float coeff = (float) i / 10.0F;
            double thickCoeff = Math.min(1.0, (double) (1.4F * Mth.sqrt(2.0F * (0.5F - Math.abs(coeff - 0.5F)))));
            double tx = x1 * (double) (1.0F - coeff) + x2 * (double) coeff;
            double ty = (double) NoiseGenUtil.interpolate((float) y1, (float) y2, coeff);
            float tick = Minecraft.getInstance().getPartialTick() + (float) EmbersClientEvents.ticks;
            int offX = (int) (6.0F * tick);
            int offZ = (int) (6.0F * tick);
            float sine = (float) Math.sin((double) coeff * Math.PI * 2.0 + (double) (0.25F * tick)) + 0.25F * (float) Math.sin((double) coeff * Math.PI * 3.47F + (double) (0.25F * tick));
            float sineOff = (4.0F + (float) thickness) / 3.0F;
            float densityCoeff = (float) (0.5 + 0.5 * Math.sin((double) coeff * Math.PI * 2.0 * dist * 0.01 + (double) tick * 0.2));
            float minusDensity = (float) density * densityCoeff * EmberGenUtil.getEmberDensity(1L, offX + (int) (tx - thickness * orthoX * thickCoeff), offZ + (int) (ty - thickness * orthoY * thickCoeff));
            float plusDensity = (float) density * densityCoeff * EmberGenUtil.getEmberDensity(1L, offX + (int) (tx - thickness * orthoX * thickCoeff), offZ + (int) (ty - thickness * orthoY * thickCoeff));
            b.m_5483_(tx - thickness * (double) (0.5F + minusDensity) * orthoX * thickCoeff - thickCoeff * orthoX * (double) sine * (double) sineOff, ty - thickness * (double) (0.5F + minusDensity) * orthoY * thickCoeff - thickCoeff * orthoY * (double) sine * (double) sineOff, 0.0).color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) Math.pow((double) (0.5F * (float) Math.max(0.0, thickCoeff - 0.4F) * minusDensity), 1.0)).endVertex();
            b.m_5483_(tx + thickness * (double) (0.5F + plusDensity) * orthoX * thickCoeff - thickCoeff * orthoX * (double) sine * (double) sineOff, ty + thickness * (double) (0.5F + plusDensity) * orthoY * thickCoeff - thickCoeff * orthoY * (double) sine * (double) sineOff, 0.0).color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, (float) Math.pow((double) (0.5F * (float) Math.max(0.0, thickCoeff - 0.4F) * plusDensity), 1.0)).endVertex();
        }
    }

    public static void renderHighlightCircle(BufferBuilder b, double x1, double y1, double thickness) {
        renderHighlightCircle(b, x1, y1, thickness, 0.0, new Color(255, 64, 16));
    }

    public static void renderHighlightCircle(BufferBuilder b, double x1, double y1, double thickness, double z, Color color) {
        for (int i = 0; i < 40; i++) {
            float coeff = (float) i / 40.0F;
            int i2 = i + 1;
            if (i2 == 40) {
                i2 = 0;
            }
            float coeff2 = (float) i2 / 40.0F;
            double angle = (Math.PI * 2) * (double) coeff;
            double angle2 = (Math.PI * 2) * (double) coeff2;
            float tick = Minecraft.getInstance().getPartialTick() + (float) EmbersClientEvents.ticks;
            float density1 = EmberGenUtil.getEmberDensity(4L, (int) x1 + (int) (480.0 * angle), (int) y1 + 4 * (int) tick + (int) (4.0 * thickness));
            float density2 = EmberGenUtil.getEmberDensity(4L, (int) x1 + (int) (480.0 * angle2), (int) y1 + 4 * (int) tick + (int) (4.0 * thickness));
            double tx = x1 + Math.sin(angle + (double) (0.03125F * tick)) * (thickness - thickness * 0.5 * (double) density1);
            double ty = y1 + Math.cos(angle + (double) (0.03125F * tick)) * (thickness - thickness * 0.5 * (double) density1);
            double tx2 = x1 + Math.sin(angle2 + (double) (0.03125F * tick)) * (thickness - thickness * 0.5 * (double) density2);
            double ty2 = y1 + Math.cos(angle2 + (double) (0.03125F * tick)) * (thickness - thickness * 0.5 * (double) density2);
            b.m_5483_(x1, y1, z).color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, 1.0F).endVertex();
            b.m_5483_(tx, ty, z).color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, 0.0F).endVertex();
            b.m_5483_(tx2, ty2, z).color((float) color.getRed() / 255.0F, (float) color.getGreen() / 255.0F, (float) color.getBlue() / 255.0F, 0.0F).endVertex();
        }
    }

    public static void drawColorRectBatched(PoseStack pose, MultiBufferSource.BufferSource bufferSource, float x, float y, float zLevel, float widthIn, float heightIn, float r1, float g1, float b1, float a1, float r2, float g2, float b2, float a2, float r3, float g3, float b3, float a3, float r4, float g4, float b4, float a4) {
        Matrix4f matrix4f = pose.last().pose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(EmbersRenderTypes.GLOW_GUI);
        vertexconsumer.vertex(matrix4f, x + 0.0F, y + heightIn, zLevel).color(r1, g1, b1, a1).endVertex();
        vertexconsumer.vertex(matrix4f, x + widthIn, y + heightIn, zLevel).color(r2, g2, b2, a2).endVertex();
        vertexconsumer.vertex(matrix4f, x + widthIn, y + 0.0F, zLevel).color(r3, g3, b3, a3).endVertex();
        vertexconsumer.vertex(matrix4f, x + 0.0F, y + 0.0F, zLevel).color(r4, g4, b4, a4).endVertex();
    }

    public static void drawHeatBarEnd(PoseStack pose, MultiBufferSource.BufferSource bufferSource, float x, float y, float zLevel, float widthIn, float heightIn, float minU, float minV, float maxU, float maxV) {
        Matrix4f matrix4f = pose.last().pose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(EmbersRenderTypes.HEAT_BAR_ENDS);
        vertexconsumer.vertex(matrix4f, x + 0.0F, y + 0.0F, zLevel).uv(minU, minV).endVertex();
        vertexconsumer.vertex(matrix4f, x + 0.0F, y + heightIn, zLevel).uv(minU, maxV).endVertex();
        vertexconsumer.vertex(matrix4f, x + widthIn, y + heightIn, zLevel).uv(maxU, maxV).endVertex();
        vertexconsumer.vertex(matrix4f, x + widthIn, y + 0.0F, zLevel).uv(maxU, minV).endVertex();
    }

    public static void renderAlchemyCircle(VertexConsumer buf, Matrix4f matrix4f, float x, float y, float z, float r, float g, float b, float a, float radius, float angle) {
        float sign = 1.0F;
        int lightx = 15728880;
        int lighty = 15728880;
        for (float i = 0.0F; i < 360.0F; i += 10.0F) {
            float tx = (float) Math.sin(Math.toRadians((double) (i + angle)));
            float tz = (float) Math.cos(Math.toRadians((double) (i + angle)));
            float tx2 = (float) Math.sin(Math.toRadians((double) (i + angle + 10.0F)));
            float tz2 = (float) Math.cos(Math.toRadians((double) (i + angle + 10.0F)));
            buf.vertex(matrix4f, x + radius * tx, y, z + radius * tz).uv(0.0F, 0.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
            buf.vertex(matrix4f, x + (radius + 0.25F) * tx, y, z + (radius + 0.25F) * tz).uv(0.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
            buf.vertex(matrix4f, x + (radius + 0.25F) * tx2, y, z + (radius + 0.25F) * tz2).uv(1.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
            buf.vertex(matrix4f, x + radius * tx2, y, z + radius * tz2).uv(1.0F, 0.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        }
        float ax = (float) (((double) radius + 0.24) * Math.sin(Math.toRadians((double) (0.0F + angle))));
        float az = (float) (((double) radius + 0.24) * Math.cos(Math.toRadians((double) (0.0F + angle))));
        float adx = (float) (0.1875 * Math.cos(Math.toRadians((double) (0.0F + angle))));
        float adz = (float) (0.1875 * -Math.sin(Math.toRadians((double) (0.0F + angle))));
        float bx = (float) (((double) radius + 0.24) * Math.sin(Math.toRadians((double) (120.0F + angle))));
        float bz = (float) (((double) radius + 0.24) * Math.cos(Math.toRadians((double) (120.0F + angle))));
        float bdx = (float) (0.1875 * Math.cos(Math.toRadians((double) (120.0F + angle))));
        float bdz = (float) (0.1875 * -Math.sin(Math.toRadians((double) (120.0F + angle))));
        float cx = (float) (((double) radius + 0.24) * Math.sin(Math.toRadians((double) (240.0F + angle))));
        float cz = (float) (((double) radius + 0.24) * Math.cos(Math.toRadians((double) (240.0F + angle))));
        float cdx = (float) (0.1875 * Math.cos(Math.toRadians((double) (240.0F + angle))));
        float cdz = (float) (0.1875 * -Math.sin(Math.toRadians((double) (240.0F + angle))));
        buf.vertex(matrix4f, x + (ax - adx), y + 5.0E-5F * sign, z + (az - adz)).uv(0.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + ax + adx, y + 5.0E-5F * sign, z + az + adz).uv(0.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + (bx - bdx), y + 5.0E-5F * sign, z + (bz - bdz)).uv(1.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + bx + bdx, y + 5.0E-5F * sign, z + bz + bdz).uv(1.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + (bx - bdx), y + 1.0E-4F * sign, z + (bz - bdz)).uv(0.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + bx + bdx, y + 1.0E-4F * sign, z + bz + bdz).uv(0.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + (cx - cdx), y + 1.0E-4F * sign, z + (cz - cdz)).uv(1.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + cx + cdx, y + 1.0E-4F * sign, z + cz + cdz).uv(1.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + (ax - adx), y + 1.5E-4F * sign, z + (az - adz)).uv(0.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + ax + adx, y + 1.5E-4F * sign, z + az + adz).uv(0.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + (cx - cdx), y + 1.5E-4F * sign, z + (cz - cdz)).uv(1.0F, 1.0F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
        buf.vertex(matrix4f, x + cx + cdx, y + 1.5E-4F * sign, z + cz + cdz).uv(1.0F, 0.5F).uv2(lightx, lighty).color(r, g, b, a).endVertex();
    }
}