package com.mna.entities.renderers.sorcery;

import com.mna.ManaAndArtifice;
import com.mna.entities.sorcery.targeting.SpellSigil;
import com.mna.gui.GuiTextures;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.EntityHitResult;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class SpellRuneRenderer extends EntityRenderer<SpellSigil> {

    public SpellRuneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(SpellSigil entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float scaleFactor = (float) entityIn.getSize();
        Minecraft mc = Minecraft.getInstance();
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RenderType.entityTranslucent((ResourceLocation) GuiTextures.affinityBadges.get(entityIn.getAffinity())));
        float alpha = entityIn.isOnCooldown() ? 192.0F + (float) ((int) (64.0 * Math.sin((double) (((float) ManaAndArtifice.instance.proxy.getGameTicks() + partialTicks) / 10.0F)))) : (entityIn.getCharges() == 0 ? 128.0F : 255.0F);
        if (entityIn.m_20145_()) {
            alpha = 32.0F;
        }
        matrixStackIn.pushPose();
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(-90.0F));
        matrixStackIn.translate(0.0F, -0.25F, 0.0F);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = 1.0F;
        float nrmH = 1.0F;
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, alpha);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, alpha);
        matrixStackIn.popPose();
        if (entityIn.isPermanent()) {
            String text = String.format("%d", entityIn.getCharges());
            if (mc.hitResult instanceof EntityHitResult && ((EntityHitResult) mc.hitResult).getEntity() == entityIn && entityIn.isCaster(mc.player)) {
                Font font = mc.font;
                matrixStackIn.pushPose();
                matrixStackIn.translate(0.0F, 1.0F, 0.0F);
                matrixStackIn.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
                matrixStackIn.scale(-0.025F, -0.025F, 0.025F);
                Matrix4f matrix4f = matrixStackIn.last().pose();
                float opacity = 0.25F;
                int textColor = (int) (opacity * 255.0F) << 24;
                float hOffset = (float) (-font.width(text) / 2);
                font.drawInBatch(text, hOffset, 0.0F, 553648127, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, textColor, packedLightIn);
                font.drawInBatch(text, hOffset, 0.0F, -1, false, matrix4f, bufferIn, Font.DisplayMode.NORMAL, 0, packedLightIn);
                matrixStackIn.popPose();
            }
        }
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, float alpha) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(255, 255, 255, (int) alpha).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }

    public ResourceLocation getTextureLocation(SpellSigil entity) {
        return null;
    }
}