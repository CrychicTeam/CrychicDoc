package com.mna.entities.renderers.boss.attacks;

import com.mna.entities.boss.attacks.OrangeChickenProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ChickenModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class OrangeChickenRenderer extends EntityRenderer<OrangeChickenProjectile> {

    private static final ResourceLocation CHICKEN_LOCATION = new ResourceLocation("textures/entity/chicken.png");

    private ChickenModel<OrangeChickenProjectile> model;

    public OrangeChickenRenderer(EntityRendererProvider.Context ctx) {
        super(ctx);
        this.model = new ChickenModel<>(ctx.bakeLayer(ModelLayers.CHICKEN));
    }

    public ResourceLocation getTextureLocation(OrangeChickenProjectile pEntity) {
        return CHICKEN_LOCATION;
    }

    public void render(OrangeChickenProjectile pEntity, float pEntityYaw, float pPartialTicks, PoseStack pMatrixStack, MultiBufferSource pBuffer, int pPackedLight) {
        pMatrixStack.pushPose();
        pMatrixStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        pMatrixStack.translate(0.0, -1.25, 0.0);
        float legAngle = (float) Math.sin((double) ((float) pEntity.f_19797_ + pPartialTicks)) / 3.0F;
        float wingAngle = (float) (Math.sin((double) ((float) pEntity.f_19797_ + pPartialTicks)) + (float) (Math.PI / 2));
        this.model.setupAnim(pEntity, 0.5F, legAngle, wingAngle, 0.0F, 0.0F);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.m_103119_(this.getTextureLocation(pEntity)));
        this.model.m_7695_(pMatrixStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 0.9607F, 0.5412F, 0.2588F, 1.0F);
        pMatrixStack.popPose();
    }
}