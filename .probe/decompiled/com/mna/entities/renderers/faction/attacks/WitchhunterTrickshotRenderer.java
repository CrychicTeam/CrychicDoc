package com.mna.entities.renderers.faction.attacks;

import com.mna.entities.projectile.WitchhunterTrickshot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.entity.SpectralArrowRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class WitchhunterTrickshotRenderer extends EntityRenderer<WitchhunterTrickshot> {

    private final Minecraft mc;

    private final ItemRenderer itemRenderer;

    private final ItemStack potionItem = new ItemStack(Items.POTION);

    public WitchhunterTrickshotRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.mc = Minecraft.getInstance();
        this.itemRenderer = this.mc.getItemRenderer();
    }

    public void render(WitchhunterTrickshot entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Vec3 offset = entityIn.getPotionPosition(partialTicks).subtract(entityIn.m_20182_());
        matrixStackIn.pushPose();
        matrixStackIn.translate(offset.x, offset.y, offset.z);
        float ticks = ((float) entityIn.f_19797_ + partialTicks) * 10.0F;
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(ticks));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(ticks));
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(ticks));
        this.itemRenderer.renderStatic(this.potionItem, ItemDisplayContext.FIXED, packedLightIn, 0, matrixStackIn, bufferIn, this.mc.level, 0);
        matrixStackIn.popPose();
        if (entityIn.renderArrow()) {
            offset = entityIn.getArrowPosition(partialTicks).subtract(entityIn.m_20182_());
            matrixStackIn.pushPose();
            matrixStackIn.translate(offset.x, offset.y, offset.z);
            this.renderArrow(matrixStackIn, partialTicks, entityIn, bufferIn, packedLightIn);
            matrixStackIn.popPose();
        }
    }

    public ResourceLocation getTextureLocation(WitchhunterTrickshot entity) {
        return SpectralArrowRenderer.SPECTRAL_ARROW_LOCATION;
    }

    private void renderArrow(PoseStack matrixStackIn, float partialTicks, WitchhunterTrickshot entityIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(45.0F));
        matrixStackIn.scale(0.05625F, 0.05625F, 0.05625F);
        matrixStackIn.translate(-4.0, 0.0, 0.0);
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(RenderType.entityCutout(this.getTextureLocation(entityIn)));
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f matrix4f = matrixstack$entry.pose();
        Matrix3f matrix3f = matrixstack$entry.normal();
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.15625F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.15625F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.3125F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.3125F, -1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, -2, 0.0F, 0.15625F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, 2, 2, 0.15625F, 0.15625F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, 2, 0.15625F, 0.3125F, 1, 0, 0, packedLightIn);
        this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -7, -2, -2, 0.0F, 0.3125F, 1, 0, 0, packedLightIn);
        for (int j = 0; j < 4; j++) {
            matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, -2, 0, 0.0F, 0.0F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, -2, 0, 0.5F, 0.0F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, 8, 2, 0, 0.5F, 0.15625F, 0, 1, 0, packedLightIn);
            this.drawVertex(matrix4f, matrix3f, ivertexbuilder, -8, 2, 0, 0.0F, 0.15625F, 0, 1, 0, packedLightIn);
        }
        matrixStackIn.popPose();
    }

    public void drawVertex(Matrix4f matrix, Matrix3f normals, VertexConsumer vertexBuilder, int offsetX, int offsetY, int offsetZ, float textureX, float textureY, int p_229039_9_, int p_229039_10_, int p_229039_11_, int packedLightIn) {
        vertexBuilder.vertex(matrix, (float) offsetX, (float) offsetY, (float) offsetZ).color(255, 255, 255, 255).uv(textureX, textureY).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLightIn).normal(normals, (float) p_229039_9_, (float) p_229039_11_, (float) p_229039_10_).endVertex();
    }
}