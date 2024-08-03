package net.liopyu.entityjs.client.living;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.liopyu.entityjs.builders.living.BaseLivingEntityBuilder;
import net.liopyu.entityjs.client.living.model.EntityModelJS;
import net.liopyu.entityjs.client.living.model.GeoLayerJS;
import net.liopyu.entityjs.client.living.model.GeoLayerJSBuilder;
import net.liopyu.entityjs.entities.living.entityjs.IAnimatableJS;
import net.liopyu.entityjs.util.ContextUtils;
import net.liopyu.entityjs.util.EntityJSHelperClass;
import net.minecraft.ChatFormatting;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.PlayerModelPart;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class KubeJSEntityRenderer<T extends LivingEntity & IAnimatableJS> extends GeoEntityRenderer<T> {

    private final BaseLivingEntityBuilder<T> builder;

    public KubeJSEntityRenderer(EntityRendererProvider.Context renderManager, BaseLivingEntityBuilder<T> builder) {
        super(renderManager, new EntityModelJS<>(builder));
        this.builder = builder;
        this.scaleHeight = this.getScaleHeight();
        this.scaleWidth = this.getScaleWidth();
        for (GeoLayerJSBuilder<T> geoBuilder : builder.layerList) {
            GeoLayerJS<T> layerPart = geoBuilder.build(this, builder);
            this.addRenderLayer(layerPart);
        }
    }

    public String entityName() {
        return this.animatable.m_6095_().toString();
    }

    public float getScaleHeight() {
        return this.builder.scaleHeight;
    }

    public float getScaleWidth() {
        return this.builder.scaleWidth;
    }

    public void scaleModelForRender(float widthScale, float heightScale, PoseStack poseStack, T animatable, BakedGeoModel model, boolean isReRender, float partialTick, int packedLight, int packedOverlay) {
        if (this.builder.scaleModelForRender != null && this.animatable != null) {
            ContextUtils.ScaleModelRenderContext<T> context = new ContextUtils.ScaleModelRenderContext<>(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
            EntityJSHelperClass.consumerCallback(this.builder.scaleModelForRender, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: scaleModelForRender.");
            super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        } else {
            super.scaleModelForRender(widthScale, heightScale, poseStack, animatable, model, isReRender, partialTick, packedLight, packedOverlay);
        }
    }

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

    public void render(T animatable, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        if (this.builder.render != null && this.animatable != null) {
            ContextUtils.RenderContext<T> context = new ContextUtils.RenderContext<>(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
            EntityJSHelperClass.consumerCallback(this.builder.render, context, "[EntityJS]: Error in " + this.entityName() + "builder for field: render.");
            super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        } else {
            super.render(animatable, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        }
    }

    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        Pose pose = animatable.m_20089_();
        if (this.isShaking(animatable)) {
            rotationYaw += (float) (Math.cos((double) animatable.f_19797_ * 3.25) * Math.PI * 0.4);
        }
        if (pose != Pose.SLEEPING) {
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - rotationYaw));
        }
        if (animatable.deathTime > 0 && this.builder.defaultDeathPose) {
            float deathRotation = ((float) animatable.deathTime + partialTick - 1.0F) / 20.0F * 1.6F;
            poseStack.mulPose(Axis.ZP.rotationDegrees(Math.min(Mth.sqrt(deathRotation), 1.0F) * this.getDeathMaxRotation(animatable)));
        } else if (animatable.isAutoSpinAttack()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(-90.0F - animatable.m_146909_()));
            poseStack.mulPose(Axis.YP.rotationDegrees(((float) animatable.f_19797_ + partialTick) * -75.0F));
        } else if (pose == Pose.SLEEPING) {
            Direction bedOrientation = animatable.getBedOrientation();
            poseStack.mulPose(Axis.YP.rotationDegrees(bedOrientation != null ? RenderUtils.getDirectionAngle(bedOrientation) : rotationYaw));
            poseStack.mulPose(Axis.ZP.rotationDegrees(this.getDeathMaxRotation(animatable)));
            poseStack.mulPose(Axis.YP.rotationDegrees(270.0F));
        } else if (animatable.m_8077_() || animatable instanceof Player) {
            String name = animatable.m_7755_().getString();
            if (animatable instanceof Player player) {
                if (!player.isModelPartShown(PlayerModelPart.CAPE)) {
                    return;
                }
            } else {
                name = ChatFormatting.stripFormatting(name);
            }
            if (name != null && (name.equals("Dinnerbone") || name.equalsIgnoreCase("Grumm"))) {
                poseStack.translate(0.0F, animatable.m_20206_() + 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(180.0F));
            }
            if (name != null && (name.equalsIgnoreCase("liopyu") || name.equalsIgnoreCase("toomuchmail"))) {
                poseStack.translate(0.0F, animatable.m_20206_() + 0.1F, 0.0F);
                poseStack.mulPose(Axis.ZP.rotationDegrees(90.0F));
            }
        }
    }
}