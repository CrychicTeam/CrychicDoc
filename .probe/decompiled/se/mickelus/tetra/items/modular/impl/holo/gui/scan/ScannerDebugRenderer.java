package se.mickelus.tetra.items.modular.impl.holo.gui.scan;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@ParametersAreNonnullByDefault
public class ScannerDebugRenderer {

    private final ScannerOverlayGui overlayGui;

    public ScannerDebugRenderer(ScannerOverlayGui overlayGui) {
        this.overlayGui = overlayGui;
    }

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onRenderWorld(RenderLevelStageEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.isCreative()) {
            PoseStack matrixStack = event.getPoseStack();
            VertexConsumer vertexBuilder = Minecraft.getInstance().renderBuffers().bufferSource().getBuffer(RenderType.lines());
            Vec3 eyePos = Minecraft.getInstance().player.m_20299_(event.getPartialTick());
            RenderSystem.lineWidth(3.0F);
            if (this.overlayGui.upHighlight != null) {
                this.drawDebugBox(this.overlayGui.upHighlight, eyePos, matrixStack, vertexBuilder, 1.0F, 0.0F, 0.0F, 0.5F);
            }
            if (this.overlayGui.midHighlight != null) {
                this.drawDebugBox(this.overlayGui.midHighlight, eyePos, matrixStack, vertexBuilder, 0.0F, 1.0F, 0.0F, 0.5F);
            }
            if (this.overlayGui.downHighlight != null) {
                this.drawDebugBox(this.overlayGui.downHighlight, eyePos, matrixStack, vertexBuilder, 0.0F, 0.0F, 1.0F, 0.5F);
            }
            RenderSystem.lineWidth(1.0F);
        }
    }

    private void drawDebugBox(BlockPos blockPos, Vec3 eyePos, PoseStack matrixStack, VertexConsumer vertexBuilder, float red, float green, float blue, float alpha) {
        Vec3 pos = Vec3.atLowerCornerOf(blockPos).subtract(eyePos);
        AABB aabb = new AABB(pos, pos.add(1.0, 1.0, 1.0));
        LevelRenderer.renderLineBox(matrixStack, vertexBuilder, aabb.inflate(0.0030000000949949026), red, green, blue, alpha);
    }
}