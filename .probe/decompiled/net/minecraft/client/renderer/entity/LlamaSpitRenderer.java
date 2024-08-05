package net.minecraft.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.LlamaSpitModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.LlamaSpit;

public class LlamaSpitRenderer extends EntityRenderer<LlamaSpit> {

    private static final ResourceLocation LLAMA_SPIT_LOCATION = new ResourceLocation("textures/entity/llama/spit.png");

    private final LlamaSpitModel<LlamaSpit> model;

    public LlamaSpitRenderer(EntityRendererProvider.Context entityRendererProviderContext0) {
        super(entityRendererProviderContext0);
        this.model = new LlamaSpitModel<>(entityRendererProviderContext0.bakeLayer(ModelLayers.LLAMA_SPIT));
    }

    public void render(LlamaSpit llamaSpit0, float float1, float float2, PoseStack poseStack3, MultiBufferSource multiBufferSource4, int int5) {
        poseStack3.pushPose();
        poseStack3.translate(0.0F, 0.15F, 0.0F);
        poseStack3.mulPose(Axis.YP.rotationDegrees(Mth.lerp(float2, llamaSpit0.f_19859_, llamaSpit0.m_146908_()) - 90.0F));
        poseStack3.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(float2, llamaSpit0.f_19860_, llamaSpit0.m_146909_())));
        this.model.setupAnim(llamaSpit0, float2, 0.0F, -0.1F, 0.0F, 0.0F);
        VertexConsumer $$6 = multiBufferSource4.getBuffer(this.model.m_103119_(LLAMA_SPIT_LOCATION));
        this.model.m_7695_(poseStack3, $$6, int5, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack3.popPose();
        super.render(llamaSpit0, float1, float2, poseStack3, multiBufferSource4, int5);
    }

    public ResourceLocation getTextureLocation(LlamaSpit llamaSpit0) {
        return LLAMA_SPIT_LOCATION;
    }
}