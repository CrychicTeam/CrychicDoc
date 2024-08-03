package harmonised.pmmo.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import java.util.Set;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

public class VeinRenderer {

    public static void drawBoxHighlights(PoseStack stack, Set<BlockPos> vein) {
        Minecraft mc = Minecraft.getInstance();
        Vec3 cameraPos = mc.getEntityRenderDispatcher().camera.getPosition();
        stack.pushPose();
        stack.translate(-cameraPos.x(), -cameraPos.y(), -cameraPos.z());
        MultiBufferSource.BufferSource buffer = mc.renderBuffers().bufferSource();
        VertexConsumer builder = buffer.getBuffer(RenderType.lines());
        for (BlockPos pos : vein) {
            drawBoxHighlight(stack, builder, pos);
        }
        stack.popPose();
        RenderSystem.disableDepthTest();
        buffer.endBatch();
    }

    public static void drawBoxHighlight(PoseStack stack, VertexConsumer builder, BlockPos pos) {
        stack.pushPose();
        Matrix4f matrix4f = stack.last().pose();
        int red = 255;
        int green = 0;
        int blue = 255;
        int alpha = 255;
        for (int i = 0; i < 12; i++) {
            int mode = i / 4;
            int j = i % 4;
            float modulus = (float) (j % 2);
            float divide = (float) (j / 2);
            switch(mode) {
                case 0:
                    builder.vertex(matrix4f, (float) pos.m_123341_() + modulus, (float) pos.m_123342_() + divide, (float) pos.m_123343_()).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
                    builder.vertex(matrix4f, (float) pos.m_123341_() + modulus, (float) pos.m_123342_() + divide, (float) (pos.m_123343_() + 1)).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
                    break;
                case 1:
                    builder.vertex(matrix4f, (float) pos.m_123341_(), (float) pos.m_123342_() + modulus, (float) pos.m_123343_() + divide).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
                    builder.vertex(matrix4f, (float) (pos.m_123341_() + 1), (float) pos.m_123342_() + modulus, (float) pos.m_123343_() + divide).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
                    break;
                case 2:
                    builder.vertex(matrix4f, (float) pos.m_123341_() + divide, (float) pos.m_123342_(), (float) pos.m_123343_() + modulus).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
                    builder.vertex(matrix4f, (float) pos.m_123341_() + divide, (float) (pos.m_123342_() + 1), (float) pos.m_123343_() + modulus).color(red, green, blue, alpha).normal(0.0F, 0.0F, 0.0F).endVertex();
            }
        }
        stack.popPose();
    }
}