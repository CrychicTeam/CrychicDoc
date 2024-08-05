package com.github.alexthe666.iceandfire.pathfinding.raycoms;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormat;
import it.unimi.dsi.fastutil.objects.Object2ObjectLinkedOpenHashMap;
import java.util.LinkedList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderDispatcher;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.phys.AABB;
import org.joml.Matrix4f;

public class WorldRenderMacros extends UiRenderMacros {

    private static final int MAX_DEBUG_TEXT_RENDER_DIST_SQUARED = 1024;

    public static final RenderType LINES = WorldRenderMacros.RenderTypes.LINES;

    public static final RenderType LINES_WITH_WIDTH = WorldRenderMacros.RenderTypes.LINES_WITH_WIDTH;

    public static final RenderType GLINT_LINES = WorldRenderMacros.RenderTypes.GLINT_LINES;

    public static final RenderType GLINT_LINES_WITH_WIDTH = WorldRenderMacros.RenderTypes.GLINT_LINES_WITH_WIDTH;

    public static final RenderType COLORED_TRIANGLES = WorldRenderMacros.RenderTypes.COLORED_TRIANGLES;

    public static final RenderType COLORED_TRIANGLES_NC_ND = WorldRenderMacros.RenderTypes.COLORED_TRIANGLES_NC_ND;

    private static final LinkedList<RenderType> buffers = new LinkedList();

    private static MultiBufferSource.BufferSource bufferSource;

    public static void putBufferHead(RenderType bufferType) {
        buffers.addFirst(bufferType);
        bufferSource = null;
    }

    public static void putBufferTail(RenderType bufferType) {
        buffers.addLast(bufferType);
        bufferSource = null;
    }

    public static void putBufferBefore(RenderType bufferType, RenderType putBefore) {
        buffers.add(Math.max(0, buffers.indexOf(putBefore)), bufferType);
        bufferSource = null;
    }

    public static void putBufferAfter(RenderType bufferType, RenderType putAfter) {
        int index = buffers.indexOf(putAfter);
        if (index == -1) {
            buffers.add(bufferType);
        } else {
            buffers.add(index + 1, bufferType);
        }
        bufferSource = null;
    }

    public static MultiBufferSource.BufferSource getBufferSource() {
        if (bufferSource == null) {
            bufferSource = MultiBufferSource.immediateWithBuffers(Util.make(new Object2ObjectLinkedOpenHashMap(), map -> buffers.forEach(type -> map.put(type, new BufferBuilder(type.bufferSize())))), Tesselator.getInstance().getBuilder());
        }
        return bufferSource;
    }

    public static void renderBlackLineBox(MultiBufferSource.BufferSource buffer, PoseStack ps, BlockPos posA, BlockPos posB, float lineWidth) {
        renderLineBox(buffer.getBuffer(LINES_WITH_WIDTH), ps, posA, posB, 0, 0, 0, 255, lineWidth);
    }

    public static void renderRedGlintLineBox(MultiBufferSource.BufferSource buffer, PoseStack ps, BlockPos posA, BlockPos posB, float lineWidth) {
        renderLineBox(buffer.getBuffer(GLINT_LINES_WITH_WIDTH), ps, posA, posB, 255, 0, 0, 255, lineWidth);
    }

    public static void renderWhiteLineBox(MultiBufferSource.BufferSource buffer, PoseStack ps, BlockPos posA, BlockPos posB, float lineWidth) {
        renderLineBox(buffer.getBuffer(LINES_WITH_WIDTH), ps, posA, posB, 255, 255, 255, 255, lineWidth);
    }

    public static void renderLineAABB(VertexConsumer buffer, PoseStack ps, AABB aabb, int argbColor, float lineWidth) {
        renderLineAABB(buffer, ps, aabb, argbColor >> 16 & 0xFF, argbColor >> 8 & 0xFF, argbColor & 0xFF, argbColor >> 24 & 0xFF, lineWidth);
    }

    public static void renderLineAABB(VertexConsumer buffer, PoseStack ps, AABB aabb, int red, int green, int blue, int alpha, float lineWidth) {
        renderLineBox(buffer, ps, (float) aabb.minX, (float) aabb.minY, (float) aabb.minZ, (float) aabb.maxX, (float) aabb.maxY, (float) aabb.maxZ, red, green, blue, alpha, lineWidth);
    }

    public static void renderLineBox(VertexConsumer buffer, PoseStack ps, BlockPos pos, int argbColor, float lineWidth) {
        renderLineBox(buffer, ps, pos, pos, argbColor >> 16 & 0xFF, argbColor >> 8 & 0xFF, argbColor & 0xFF, argbColor >> 24 & 0xFF, lineWidth);
    }

    public static void renderLineBox(VertexConsumer buffer, PoseStack ps, BlockPos posA, BlockPos posB, int argbColor, float lineWidth) {
        renderLineBox(buffer, ps, posA, posB, argbColor >> 16 & 0xFF, argbColor >> 8 & 0xFF, argbColor & 0xFF, argbColor >> 24 & 0xFF, lineWidth);
    }

    public static void renderLineBox(VertexConsumer buffer, PoseStack ps, BlockPos posA, BlockPos posB, int red, int green, int blue, int alpha, float lineWidth) {
        renderLineBox(buffer, ps, (float) Math.min(posA.m_123341_(), posB.m_123341_()), (float) Math.min(posA.m_123342_(), posB.m_123342_()), (float) Math.min(posA.m_123343_(), posB.m_123343_()), (float) (Math.max(posA.m_123341_(), posB.m_123341_()) + 1), (float) (Math.max(posA.m_123342_(), posB.m_123342_()) + 1), (float) (Math.max(posA.m_123343_(), posB.m_123343_()) + 1), red, green, blue, alpha, lineWidth);
    }

    public static void renderLineBox(VertexConsumer buffer, PoseStack ps, float minX, float minY, float minZ, float maxX, float maxY, float maxZ, int red, int green, int blue, int alpha, float lineWidth) {
        if (alpha != 0) {
            float halfLine = lineWidth / 2.0F;
            minX -= halfLine;
            minY -= halfLine;
            minZ -= halfLine;
            float minX2 = minX + lineWidth;
            float minY2 = minY + lineWidth;
            float minZ2 = minZ + lineWidth;
            maxX += halfLine;
            maxY += halfLine;
            maxZ += halfLine;
            float maxX2 = maxX - lineWidth;
            float maxY2 = maxY - lineWidth;
            float maxZ2 = maxZ - lineWidth;
            Matrix4f m = ps.last().pose();
            buffer.defaultColor(red, green, blue, alpha);
            populateRenderLineBox(minX, minY, minZ, minX2, minY2, minZ2, maxX, maxY, maxZ, maxX2, maxY2, maxZ2, m, buffer);
            buffer.unsetDefaultColor();
        }
    }

    public static void populateRenderLineBox(float minX, float minY, float minZ, float minX2, float minY2, float minZ2, float maxX, float maxY, float maxZ, float maxX2, float maxY2, float maxZ2, Matrix4f m, VertexConsumer buf) {
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY2, minZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX2, minY, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX2, minY, minZ2).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, minX2, minY, minZ2).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY, minZ2).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX2, minY, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX, minY2, minZ2).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, minZ2).endVertex();
        buf.vertex(m, minX, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ).endVertex();
        buf.vertex(m, minX2, minY2, maxZ2).endVertex();
        buf.vertex(m, minX2, minY2, maxZ).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, minZ2).endVertex();
        buf.vertex(m, maxX2, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX, minY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, minZ2).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ).endVertex();
        buf.vertex(m, minX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, minZ2).endVertex();
        buf.vertex(m, maxX, maxY2, maxZ2).endVertex();
        buf.vertex(m, maxX2, maxY2, maxZ2).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX2, maxY, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY, minZ2).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, minX2, maxY, maxZ2).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
    }

    public static void renderBox(MultiBufferSource.BufferSource buffer, PoseStack ps, BlockPos posA, BlockPos posB, int argbColor) {
        renderBox(buffer.getBuffer(COLORED_TRIANGLES), ps, posA, posB, argbColor >> 16 & 0xFF, argbColor >> 8 & 0xFF, argbColor & 0xFF, argbColor >> 24 & 0xFF);
    }

    public static void renderBox(VertexConsumer buffer, PoseStack ps, BlockPos posA, BlockPos posB, int red, int green, int blue, int alpha) {
        if (alpha != 0) {
            float minX = (float) Math.min(posA.m_123341_(), posB.m_123341_());
            float minY = (float) Math.min(posA.m_123342_(), posB.m_123342_());
            float minZ = (float) Math.min(posA.m_123343_(), posB.m_123343_());
            float maxX = (float) (Math.max(posA.m_123341_(), posB.m_123341_()) + 1);
            float maxY = (float) (Math.max(posA.m_123342_(), posB.m_123342_()) + 1);
            float maxZ = (float) (Math.max(posA.m_123343_(), posB.m_123343_()) + 1);
            Matrix4f m = ps.last().pose();
            buffer.defaultColor(red, green, blue, alpha);
            populateCuboid(minX, minY, minZ, maxX, maxY, maxZ, m, buffer);
            buffer.unsetDefaultColor();
        }
    }

    public static void populateCuboid(float minX, float minY, float minZ, float maxX, float maxY, float maxZ, Matrix4f m, VertexConsumer buf) {
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, minX, minY, minZ).endVertex();
        buf.vertex(m, minX, minY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, maxZ).endVertex();
        buf.vertex(m, minX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, minY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, minY, maxZ).endVertex();
        buf.vertex(m, maxX, maxY, minZ).endVertex();
        buf.vertex(m, maxX, maxY, maxZ).endVertex();
    }

    public static void renderFillRectangle(MultiBufferSource.BufferSource buffer, PoseStack ps, int x, int y, int z, int w, int h, int argbColor) {
        populateRectangle(x, y, z, w, h, argbColor >> 16 & 0xFF, argbColor >> 8 & 0xFF, argbColor & 0xFF, argbColor >> 24 & 0xFF, buffer.getBuffer(COLORED_TRIANGLES_NC_ND), ps.last().pose());
    }

    public static void populateRectangle(int x, int y, int z, int w, int h, int red, int green, int blue, int alpha, VertexConsumer buffer, Matrix4f m) {
        if (alpha != 0) {
            buffer.vertex(m, (float) x, (float) y, (float) z).color(red, green, blue, alpha).endVertex();
            buffer.vertex(m, (float) x, (float) (y + h), (float) z).color(red, green, blue, alpha).endVertex();
            buffer.vertex(m, (float) (x + w), (float) (y + h), (float) z).color(red, green, blue, alpha).endVertex();
            buffer.vertex(m, (float) x, (float) y, (float) z).color(red, green, blue, alpha).endVertex();
            buffer.vertex(m, (float) (x + w), (float) (y + h), (float) z).color(red, green, blue, alpha).endVertex();
            buffer.vertex(m, (float) (x + w), (float) y, (float) z).color(red, green, blue, alpha).endVertex();
        }
    }

    public static void renderDebugText(BlockPos pos, List<String> text, PoseStack matrixStack, boolean forceWhite, int mergeEveryXListElements, MultiBufferSource buffer) {
        if (mergeEveryXListElements < 1) {
            throw new IllegalArgumentException("mergeEveryXListElements is less than 1");
        } else {
            EntityRenderDispatcher erm = Minecraft.getInstance().getEntityRenderDispatcher();
            int cap = text.size();
            if (cap > 0 && erm.distanceToSqr((double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_()) <= 1024.0) {
                Font fontrenderer = Minecraft.getInstance().font;
                matrixStack.pushPose();
                matrixStack.translate((double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.75, (double) pos.m_123343_() + 0.5);
                matrixStack.mulPose(erm.cameraOrientation());
                matrixStack.scale(-0.014F, -0.014F, 0.014F);
                matrixStack.translate(0.0, 18.0, 0.0);
                float backgroundTextOpacity = Minecraft.getInstance().options.getBackgroundOpacity(0.25F);
                int alphaMask = (int) (backgroundTextOpacity * 255.0F) << 24;
                Matrix4f rawPosMatrix = matrixStack.last().pose();
                for (int i = 0; i < cap; i += mergeEveryXListElements) {
                    MutableComponent renderText = Component.literal(mergeEveryXListElements == 1 ? (String) text.get(i) : text.subList(i, Math.min(i + mergeEveryXListElements, cap)).toString());
                    float textCenterShift = (float) (-fontrenderer.width(renderText) / 2);
                    fontrenderer.drawInBatch(renderText, textCenterShift, 0.0F, forceWhite ? -1 : 553648127, false, rawPosMatrix, buffer, Font.DisplayMode.SEE_THROUGH, alphaMask, 15728880);
                    if (!forceWhite) {
                        fontrenderer.drawInBatch(renderText, textCenterShift, 0.0F, -1, false, rawPosMatrix, buffer, Font.DisplayMode.NORMAL, 0, 15728880);
                    }
                    matrixStack.translate(0.0, (double) (9 + 1), 0.0);
                }
                matrixStack.popPose();
            }
        }
    }

    static {
        putBufferTail(COLORED_TRIANGLES);
        putBufferTail(LINES);
        putBufferTail(LINES_WITH_WIDTH);
        putBufferTail(GLINT_LINES);
        putBufferTail(GLINT_LINES_WITH_WIDTH);
        putBufferTail(COLORED_TRIANGLES_NC_ND);
    }

    public static class AlwaysDepthTestStateShard extends RenderStateShard.DepthTestStateShard {

        public static final RenderStateShard.DepthTestStateShard ALWAYS_DEPTH_TEST = new WorldRenderMacros.AlwaysDepthTestStateShard();

        private AlwaysDepthTestStateShard() {
            super("true_always", -1);
            this.f_110131_ = () -> {
                RenderSystem.enableDepthTest();
                RenderSystem.depthFunc(519);
            };
        }
    }

    private static final class RenderTypes extends RenderType {

        private static final RenderType GLINT_LINES = m_173215_("structurize_glint_lines", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES, 4096, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110137_).setDepthTestState(f_110111_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110115_).createCompositeState(false));

        private static final RenderType GLINT_LINES_WITH_WIDTH = m_173215_("structurize_glint_lines_with_width", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 8192, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110137_).setDepthTestState(WorldRenderMacros.AlwaysDepthTestStateShard.ALWAYS_DEPTH_TEST).setCullState(f_110158_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110114_).createCompositeState(false));

        private static final RenderType LINES = m_173215_("structurize_lines", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.DEBUG_LINES, 16384, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110139_).setDepthTestState(f_110113_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110115_).createCompositeState(false));

        private static final RenderType LINES_WITH_WIDTH = m_173215_("structurize_lines_with_width", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 8192, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110139_).setDepthTestState(f_110113_).setCullState(f_110158_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110114_).createCompositeState(false));

        private static final RenderType COLORED_TRIANGLES = m_173215_("structurize_colored_triangles", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 8192, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110139_).setDepthTestState(f_110113_).setCullState(f_110158_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110114_).createCompositeState(false));

        private static final RenderType COLORED_TRIANGLES_NC_ND = m_173215_("structurize_colored_triangles_nc_nd", DefaultVertexFormat.POSITION_COLOR, VertexFormat.Mode.TRIANGLES, 4096, false, false, RenderType.CompositeState.builder().setTextureState(f_110147_).setShaderState(f_173104_).setTransparencyState(f_110139_).setDepthTestState(f_110111_).setCullState(f_110110_).setLightmapState(f_110153_).setOverlayState(f_110155_).setLayeringState(f_110117_).setOutputState(f_110123_).setTexturingState(f_110148_).setWriteMaskState(f_110115_).createCompositeState(false));

        private RenderTypes(String nameIn, VertexFormat formatIn, VertexFormat.Mode drawModeIn, int bufferSizeIn, boolean useDelegateIn, boolean needsSortingIn, Runnable setupTaskIn, Runnable clearTaskIn) {
            super(nameIn, formatIn, drawModeIn, bufferSizeIn, useDelegateIn, needsSortingIn, setupTaskIn, clearTaskIn);
            throw new IllegalStateException();
        }
    }
}