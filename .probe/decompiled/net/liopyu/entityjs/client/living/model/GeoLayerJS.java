package net.liopyu.entityjs.client.living.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.client.living.KubeJSEntityRenderer;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class GeoLayerJS<T extends LivingEntity & IAnimatableJS> extends GeoRenderLayer<T> {

    public T entity;

    public final GeoLayerJSBuilder<T> geoBuilder;

    public final KubeJSEntityRenderer<T> renderer;

    public final BaseLivingEntityBuilder<T> builder;

    public GeoLayerJS(KubeJSEntityRenderer<T> entityRendererIn, GeoLayerJSBuilder<T> geoBuilder, BaseLivingEntityBuilder<T> builder) {
        super(entityRendererIn);
        this.geoBuilder = geoBuilder;
        this.renderer = entityRendererIn;
        this.builder = builder;
    }

    public String entityName() {
        return this.builder.get().toString();
    }

    protected ResourceLocation getTextureResource(T animatable) {
        if (this.geoBuilder.textureResource != null) {
            Object obj = this.geoBuilder.textureResource.apply(animatable);
            if (obj instanceof ResourceLocation) {
                return (ResourceLocation) obj;
            }
            EntityJSHelperClass.logErrorMessageOnce("[EntityJS]: Invalid return value for textureResource in newGeoLayer builder. Value: " + obj + ". Must be a ResourceLocation. Defaulting to " + super.getTextureResource(animatable));
        }
        return super.getTextureResource(animatable);
    }

    public void preRender(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if (this.geoBuilder.preRender != null && animatable != null) {
            ContextUtils.PreRenderContext<T> context = new ContextUtils.PreRenderContext<>(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
            EntityJSHelperClass.consumerCallback(this.geoBuilder.preRender, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: preRender");
            super.preRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        } else {
            super.preRender(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        }
    }

    public void render(PoseStack poseStack, T animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTicks, int packedLightIn, int packedOverlay) {
        if (this.geoBuilder.render != null && animatable != null) {
            ContextUtils.PreRenderContext<T> context = new ContextUtils.PreRenderContext<>(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTicks, packedLightIn, packedOverlay);
            EntityJSHelperClass.consumerCallback(this.geoBuilder.render, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: render");
            super.render(poseStack, animatable, bakedModel, renderType, bufferSource, buffer, partialTicks, packedLightIn, packedOverlay);
        } else {
            RenderType renderLayer = RenderType.entityCutoutNoCull(this.getTextureResource(animatable));
            this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, renderLayer, bufferSource.getBuffer(renderLayer), partialTicks, packedLightIn, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}