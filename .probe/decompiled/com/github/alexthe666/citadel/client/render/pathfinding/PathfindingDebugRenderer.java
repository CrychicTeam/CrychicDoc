package com.github.alexthe666.citadel.client.render.pathfinding;

import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.entity.pathfinding.raycoms.MNode;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.ConcurrentModificationException;
import java.util.HashSet;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;
import org.joml.Matrix4f;

public class PathfindingDebugRenderer {

    public static final RenderBuffers renderBuffers = new RenderBuffers();

    private static final MultiBufferSource.BufferSource renderBuffer = renderBuffers.bufferSource();

    public static Set<MNode> lastDebugNodesVisited = new HashSet();

    public static Set<MNode> lastDebugNodesNotVisited = new HashSet();

    public static Set<MNode> lastDebugNodesPath = new HashSet();

    public static void render(WorldEventContext ctx) {
        try {
            for (MNode n : lastDebugNodesVisited) {
                debugDrawNode(n, -65536, ctx);
            }
            for (MNode n : lastDebugNodesNotVisited) {
                debugDrawNode(n, -16776961, ctx);
            }
            for (MNode n : lastDebugNodesPath) {
                if (n.isReachedByWorker()) {
                    debugDrawNode(n, -39424, ctx);
                } else {
                    debugDrawNode(n, -16711936, ctx);
                }
            }
        } catch (ConcurrentModificationException var3) {
            Citadel.LOGGER.catching(var3);
        }
    }

    private static void debugDrawNode(MNode n, int argbColor, WorldEventContext ctx) {
        ctx.poseStack.pushPose();
        ctx.poseStack.translate((double) n.pos.m_123341_() + 0.375, (double) n.pos.m_123342_() + 0.375, (double) n.pos.m_123343_() + 0.375);
        Entity entity = Minecraft.getInstance().getCameraEntity();
        if (n.pos.m_123314_(entity.blockPosition(), 5.0)) {
            renderDebugText(n, ctx);
        }
        ctx.poseStack.scale(0.25F, 0.25F, 0.25F);
        WorldRenderMacros.renderBox(ctx.bufferSource, ctx.poseStack, BlockPos.ZERO, BlockPos.ZERO, argbColor);
        if (n.parent != null) {
            Matrix4f lineMatrix = ctx.poseStack.last().pose();
            float pdx = (float) (n.parent.pos.m_123341_() - n.pos.m_123341_()) + 0.125F;
            float pdy = (float) (n.parent.pos.m_123342_() - n.pos.m_123342_()) + 0.125F;
            float pdz = (float) (n.parent.pos.m_123343_() - n.pos.m_123343_()) + 0.125F;
            VertexConsumer buffer = ctx.bufferSource.getBuffer(WorldRenderMacros.LINES);
            buffer.vertex(lineMatrix, 0.5F, 0.5F, 0.5F).color(0.75F, 0.75F, 0.75F, 1.0F).endVertex();
            buffer.vertex(lineMatrix, pdx / 0.25F, pdy / 0.25F, pdz / 0.25F).color(0.75F, 0.75F, 0.75F, 1.0F).endVertex();
        }
        ctx.poseStack.popPose();
    }

    private static void renderDebugText(@NotNull MNode n, WorldEventContext ctx) {
        Font fontrenderer = Minecraft.getInstance().font;
        String s1 = String.format("F: %.3f [%d]", n.getCost(), n.getCounterAdded());
        String s2 = String.format("G: %.3f [%d]", n.getScore(), n.getCounterVisited());
        int i = Math.max(fontrenderer.width(s1), fontrenderer.width(s2)) / 2;
        ctx.poseStack.pushPose();
        ctx.poseStack.translate(0.0F, 0.75F, 0.0F);
        ctx.poseStack.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
        ctx.poseStack.scale(-0.014F, -0.014F, 0.014F);
        ctx.poseStack.translate(0.0F, 18.0F, 0.0F);
        Matrix4f mat = ctx.poseStack.last().pose();
        WorldRenderMacros.renderFillRectangle(ctx.bufferSource, ctx.poseStack, -i - 1, -5, 0, 2 * i + 2, 17, 2130706432);
        ctx.poseStack.translate(0.0F, -5.0F, -0.1F);
        fontrenderer.drawInBatch(s1, (float) (-fontrenderer.width(s1)) / 2.0F, 1.0F, -1, false, mat, ctx.bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        ctx.poseStack.translate(0.0F, 8.0F, -0.1F);
        fontrenderer.drawInBatch(s2, (float) (-fontrenderer.width(s2)) / 2.0F, 1.0F, -1, false, mat, ctx.bufferSource, Font.DisplayMode.NORMAL, 0, 15728880);
        ctx.poseStack.popPose();
    }
}