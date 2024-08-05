package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.entity.EntityGuster;
import com.github.alexthe666.alexsmobs.entity.EntitySandShot;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.LlamaSpit;

public class RenderSandShot extends EntityRenderer<EntitySandShot> {

    private static final ResourceLocation SAND_SHOT = new ResourceLocation("alexsmobs:textures/entity/sand_shot.png");

    private final LlamaSpitModel<LlamaSpit> model;

    public RenderSandShot(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn);
        this.model = new LlamaSpitModel<>(renderManagerIn.bakeLayer(ModelLayers.LLAMA_SPIT));
    }

    public void render(EntitySandShot entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0, 0.15F, 0.0);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19859_, entityIn.m_146908_()) - 90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(partialTicks, entityIn.f_19860_, entityIn.m_146909_())));
        matrixStackIn.scale(1.2F, 1.2F, 1.2F);
        int i = EntityGuster.getColorForVariant(entityIn.getVariant());
        float r = (float) (i >> 16 & 0xFF) / 255.0F;
        float g = (float) (i >> 8 & 0xFF) / 255.0F;
        float b = (float) (i & 0xFF) / 255.0F;
        VertexConsumer ivertexbuilder = bufferIn.getBuffer(this.model.m_103119_(SAND_SHOT));
        this.model.m_7695_(matrixStackIn, ivertexbuilder, packedLightIn, OverlayTexture.NO_OVERLAY, r, g, b, 1.0F);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(EntitySandShot entity) {
        return SAND_SHOT;
    }
}