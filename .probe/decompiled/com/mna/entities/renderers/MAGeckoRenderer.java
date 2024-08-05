package com.mna.entities.renderers;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.object.Color;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public abstract class MAGeckoRenderer<T extends LivingEntity & GeoAnimatable> extends GeoEntityRenderer<T> {

    protected boolean invisible;

    protected boolean hasEmissivePass = false;

    protected boolean emissivePass = false;

    protected MAGeckoRenderer(EntityRendererProvider.Context context, GeoModel<T> modelProvider) {
        super(context, modelProvider);
    }

    public MAGeckoRenderer<T> enableEmissive() {
        this.hasEmissivePass = true;
        return this;
    }

    public void render(T entity, float entityYaw, float partialTicks, PoseStack stack, MultiBufferSource bufferIn, int packedLightIn) {
        try {
            float curScale = entity.getPersistentData().getFloat("mna_entity_scale");
            float prevScale = entity.getPersistentData().getFloat("mna_entity_scale_prev");
            float scale = curScale + (curScale - prevScale) * partialTicks;
            if (!entity.isAddedToWorld()) {
                scale = 1.0F;
            }
            stack.pushPose();
            stack.scale(scale, scale, scale);
        } catch (Exception var11) {
            var11.printStackTrace();
        }
        this.invisible = entity.m_20145_();
        super.render(entity, entityYaw, partialTicks, stack, bufferIn, packedLightIn);
        try {
            stack.popPose();
        } catch (Exception var10) {
            var10.printStackTrace();
        }
    }

    public void defaultRender(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        Color renderColor = this.getRenderColor(animatable, partialTick, packedLight);
        float red = renderColor.getRedFloat();
        float green = renderColor.getGreenFloat();
        float blue = renderColor.getBlueFloat();
        float alpha = renderColor.getAlphaFloat();
        int packedOverlay = this.getPackedOverlay(animatable, 0.0F);
        BakedGeoModel model = this.getGeoModel().getBakedModel(this.getGeoModel().getModelResource(animatable));
        this.renderPass(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight, false, model, red, green, blue, alpha, packedOverlay);
        if (this.hasEmissivePass) {
            this.renderPass(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight, true, model, red, green, blue, alpha, packedOverlay);
        }
    }

    private void renderPass(PoseStack poseStack, T animatable, MultiBufferSource bufferSource, RenderType renderType, VertexConsumer buffer, float yaw, float partialTick, int packedLight, boolean emissive, BakedGeoModel model, float red, float green, float blue, float alpha, int packedOverlay) {
        this.emissivePass = emissive;
        if (renderType == null) {
            renderType = this.getRenderType(animatable, this.m_5478_(animatable), bufferSource, partialTick);
        }
        if (buffer == null) {
            buffer = bufferSource.getBuffer(renderType);
        }
        poseStack.pushPose();
        this.preRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        if (this.firePreRenderEvent(poseStack, model, bufferSource, partialTick, packedLight)) {
            this.preApplyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, (float) packedLight, packedLight, packedOverlay);
            this.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.applyRenderLayers(poseStack, animatable, model, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            this.postRender(poseStack, animatable, model, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
            this.firePostRenderEvent(poseStack, model, bufferSource, partialTick, packedLight);
        }
        poseStack.popPose();
        this.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
    }

    public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return this.emissivePass ? RenderType.eyes(texture) : RenderType.entityCutout(texture);
    }
}