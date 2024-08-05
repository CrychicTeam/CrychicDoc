package net.liopyu.entityjs.client.nonliving;

import com.mojang.blaze3d.vertex.PoseStack;
import javax.annotation.Nullable;
import net.liopyu.entityjs.builders.nonliving.BaseEntityBuilder;
import net.liopyu.entityjs.client.nonliving.model.NonLivingEntityModel;
import net.liopyu.entityjs.entities.nonliving.entityjs.IAnimatableJSNL;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class KubeJSNLEntityRenderer<T extends Entity & IAnimatableJSNL> extends GeoEntityRenderer<T> {

    private final BaseEntityBuilder<T> builder;

    public KubeJSNLEntityRenderer(EntityRendererProvider.Context renderManager, BaseEntityBuilder<T> builder) {
        super(renderManager, new NonLivingEntityModel<>(builder));
        this.builder = builder;
        this.scaleHeight = this.getScaleHeight();
        this.scaleWidth = this.getScaleWidth();
    }

    public String entityName() {
        return this.animatable.getType().toString();
    }

    public float getScaleHeight() {
        return this.builder.scaleHeight;
    }

    public float getScaleWidth() {
        return this.builder.scaleWidth;
    }

    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, T animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (this.builder.scaleModelForRender != null && this.animatable != null) {
            ContextUtils.ScaleModelRenderContextNL<T> context = new ContextUtils.ScaleModelRenderContextNL<>(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
            EntityJSHelperClass.consumerCallback(this.builder.scaleModelForRender, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: scaleModelForRender.");
            super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        } else {
            super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        }
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return (ResourceLocation) this.builder.textureResource.apply(entity);
    }

    public RenderType getRenderType(T animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return switch(animatable.getBuilder().renderType) {
            case SOLID ->
                RenderType.entitySolid(texture);
            case CUTOUT ->
                RenderType.entityCutout(texture);
            case TRANSLUCENT ->
                RenderType.entityTranslucent(texture);
        };
    }

    @Override
    public void render(T animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.builder.render != null && this.animatable != null) {
            ContextUtils.NLRenderContext<T> context = new ContextUtils.NLRenderContext<>(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            EntityJSHelperClass.consumerCallback(this.builder.render, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: render.");
            super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        } else {
            super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }
}