package org.violetmoon.zeta.client;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexSorting;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.ForgeHooksClient;
import org.joml.Matrix4f;
import org.violetmoon.zeta.client.event.play.ZRenderTick;
import org.violetmoon.zeta.event.bus.PlayEvent;

@Deprecated
public class TopLayerTooltipHandler {

    private List<Component> tooltip;

    private int tooltipX;

    private int tooltipY;

    @PlayEvent
    public void renderTick(ZRenderTick event) {
        if (this.tooltip != null && event.isEndPhase()) {
            Minecraft mc = Minecraft.getInstance();
            Screen screen = mc.screen;
            Matrix4f projectionMatrix = RenderSystem.getProjectionMatrix();
            VertexSorting vertexSorting = RenderSystem.getVertexSorting();
            Window window = mc.getWindow();
            Matrix4f matrix4f = new Matrix4f().setOrtho(0.0F, (float) ((double) window.getWidth() / window.getGuiScale()), (float) ((double) window.getHeight() / window.getGuiScale()), 0.0F, 1000.0F, ForgeHooksClient.getGuiFarPlane());
            RenderSystem.setProjectionMatrix(matrix4f, VertexSorting.ORTHOGRAPHIC_Z);
            PoseStack posestack = RenderSystem.getModelViewStack();
            posestack.pushPose();
            posestack.setIdentity();
            posestack.translate(0.0, 0.0, (double) (1000.0F - ForgeHooksClient.getGuiFarPlane()));
            RenderSystem.applyModelViewMatrix();
            GuiGraphics guiGraphics = new GuiGraphics(mc, mc.renderBuffers().bufferSource());
            if (screen != null) {
                guiGraphics.renderTooltip(mc.font, this.tooltip, Optional.empty(), this.tooltipX, this.tooltipY);
            }
            guiGraphics.flush();
            posestack.popPose();
            RenderSystem.applyModelViewMatrix();
            RenderSystem.setProjectionMatrix(projectionMatrix, vertexSorting);
            this.tooltip = null;
        }
    }

    public void setTooltip(List<String> tooltip, int x, int y) {
        this.tooltip = (List<Component>) tooltip.stream().map(Component::m_237113_).collect(Collectors.toList());
        this.tooltipX = x;
        this.tooltipY = y;
    }
}