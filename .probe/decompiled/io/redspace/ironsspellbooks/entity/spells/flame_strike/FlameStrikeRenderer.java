package io.redspace.ironsspellbooks.entity.spells.flame_strike;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class FlameStrikeRenderer extends EntityRenderer<FlameStrike> {

    private static final ResourceLocation[] TEXTURES = new ResourceLocation[] { IronsSpellbooks.id("textures/entity/flame_strike/flame_strike_1.png"), IronsSpellbooks.id("textures/entity/flame_strike/flame_strike_2.png"), IronsSpellbooks.id("textures/entity/flame_strike/flame_strike_3.png"), IronsSpellbooks.id("textures/entity/flame_strike/flame_strike_4.png") };

    public FlameStrikeRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(FlameStrike entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int light) {
        poseStack.pushPose();
        PoseStack.Pose pose = poseStack.last();
        poseStack.mulPose(Axis.YP.rotationDegrees(90.0F - entity.m_146908_()));
        poseStack.mulPose(Axis.XP.rotationDegrees(-entity.m_146909_()));
        this.drawSlash(pose, entity, bufferSource, entity.m_20205_() * 1.5F, entity.isMirrored());
        poseStack.popPose();
        super.render(entity, yaw, partialTicks, poseStack, bufferSource, light);
    }

    private void drawSlash(PoseStack.Pose pose, FlameStrike entity, MultiBufferSource bufferSource, float width, boolean mirrored) {
        Matrix4f poseMatrix = pose.pose();
        Matrix3f normalMatrix = pose.normal();
        VertexConsumer consumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        float halfWidth = width * 0.5F;
        float height = entity.m_20206_() * 0.5F;
        consumer.vertex(poseMatrix, -halfWidth, height, -halfWidth).color(255, 255, 255, 255).uv(0.0F, mirrored ? 0.0F : 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, -halfWidth).color(255, 255, 255, 255).uv(1.0F, mirrored ? 0.0F : 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, halfWidth, height, halfWidth).color(255, 255, 255, 255).uv(1.0F, mirrored ? 1.0F : 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
        consumer.vertex(poseMatrix, -halfWidth, height, halfWidth).color(255, 255, 255, 255).uv(0.0F, mirrored ? 1.0F : 0.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(15728880).normal(normalMatrix, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public ResourceLocation getTextureLocation(FlameStrike entity) {
        int frame = entity.f_19797_ / 2 % TEXTURES.length;
        return TEXTURES[frame];
    }
}