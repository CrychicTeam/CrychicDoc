package com.mna.entities.renderers.ritual;

import com.mna.entities.models.DemonStoneModel;
import com.mna.entities.rituals.DemonStone;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class DemonStoneRenderer extends EntityRenderer<DemonStone> {

    private static final ResourceLocation DEMON_STONE_TEXTURE = new ResourceLocation("textures/block/nether_bricks.png");

    protected final DemonStoneModel<DemonStone> modelStone = new DemonStoneModel<>(Minecraft.getInstance().getEntityModels().bakeLayer(DemonStoneModel.LAYER_LOCATION));

    public DemonStoneRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(DemonStone entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        int age = entityIn.getAge();
        matrixStackIn.translate(0.0F, -1.0F, 0.0F);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
        float angleDegrees = 0.0F;
        int numPillars = 3;
        for (int i = 0; i < numPillars; i++) {
            matrixStackIn.pushPose();
            float riseAmount = Math.min((float) (age - i * 10) / 100.0F * 2.5F, 2.5F);
            matrixStackIn.translate(0.0F, -riseAmount, 0.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(angleDegrees));
            matrixStackIn.translate(-2.0F, 0.0F, 0.0F);
            matrixStackIn.mulPose(Axis.YP.rotationDegrees(90.0F));
            this.modelStone.setupAnim(entityIn, partialTicks, 0.0F, -0.1F, 0.0F, 0.0F);
            VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.modelStone.m_103119_(this.getTextureLocation(entityIn)));
            this.modelStone.renderToBuffer(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
            matrixStackIn.popPose();
            angleDegrees += 360.0F / (float) numPillars;
        }
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(DemonStone entity) {
        return DEMON_STONE_TEXTURE;
    }
}