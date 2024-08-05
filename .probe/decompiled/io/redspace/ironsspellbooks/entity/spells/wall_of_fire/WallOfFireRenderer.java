package io.redspace.ironsspellbooks.entity.spells.wall_of_fire;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WallOfFireRenderer extends EntityRenderer<WallOfFireEntity> {

    private static ResourceLocation TEXTURE = new ResourceLocation("textures/block/fire_0.png");

    public WallOfFireRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(WallOfFireEntity entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutout(TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        float height = 3.0F;
        Vec3 origin = entity.m_20182_();
        for (int i = 0; i < entity.subEntities.length - 1; i++) {
            Vec3 start = entity.subEntities[i].m_20182_().subtract(origin);
            Vec3 end = entity.subEntities[i + 1].m_20182_().subtract(origin);
            int frameCount = 32;
            int frame = (entity.f_19797_ + i * 87) % frameCount;
            float uvPerFrame = 1.0F / (float) frameCount;
            float uvY = (float) frame * uvPerFrame;
            poseStack.pushPose();
            consumer.vertex(poseMatrix, (float) start.x, (float) start.y, (float) start.z).color(255, 255, 255, 255).uv(0.0F, uvY + uvPerFrame).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) start.x, (float) start.y + height, (float) start.z).color(255, 255, 255, 255).uv(0.0F, uvY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) end.x, (float) end.y + height, (float) end.z).color(255, 255, 255, 255).uv(1.0F, uvY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) end.x, (float) end.y, (float) end.z).color(255, 255, 255, 255).uv(1.0F, uvY + uvPerFrame).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            consumer.vertex(poseMatrix, (float) start.x, (float) start.y, (float) start.z).color(255, 255, 255, 255).uv(0.0F, uvY + uvPerFrame).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) start.x, (float) start.y + height, (float) start.z).color(255, 255, 255, 255).uv(0.0F, uvY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) end.x, (float) end.y + height, (float) end.z).color(255, 255, 255, 255).uv(1.0F, uvY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            consumer.vertex(poseMatrix, (float) end.x, (float) end.y, (float) end.z).color(255, 255, 255, 255).uv(1.0F, uvY + uvPerFrame).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
            poseStack.popPose();
        }
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    public ResourceLocation getTextureLocation(WallOfFireEntity entity) {
        return TEXTURE;
    }
}