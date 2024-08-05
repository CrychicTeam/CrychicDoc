package com.mna.blocks.tileentities.renderers.wizard_lab;

import com.mna.blocks.tileentities.models.WizardLabModel;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoBlockRenderer;
import software.bernie.geckolib.util.RenderUtils;

public class WizardLabRenderer<T extends WizardLabTile> extends GeoBlockRenderer<T> {

    public WizardLabRenderer(BlockEntityRendererProvider.Context rendererDispatcherIn, WizardLabModel<T> model) {
        super(model);
    }

    public RenderType getRenderType(T animatable, ResourceLocation texture, MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entitySmoothCutout(texture);
    }

    public void renderRecursively(PoseStack stack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        stack.pushPose();
        RenderUtils.translateMatrixToBone(stack, bone);
        RenderUtils.translateToPivotPoint(stack, bone);
        RenderUtils.rotateMatrixAroundBone(stack, bone);
        RenderUtils.scaleMatrixForBone(stack, bone);
        if (!bone.isHidden()) {
            stack.pushPose();
            stack.translate(-0.5, 0.0, -0.5);
            ((WizardLabModel) this.getGeoModel()).renderBoneAdditions(animatable, bone.getName(), stack, bufferSource, renderType, packedLight, packedOverlay);
            stack.popPose();
            bufferSource.getBuffer(renderType);
            RenderUtils.translateAwayFromPivotPoint(stack, bone);
            this.renderCubesOfBone(stack, bone, buffer, packedLight, packedOverlay, red, green, blue, alpha);
            this.renderChildBones(stack, animatable, bone, renderType, bufferSource, buffer, false, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        } else {
            RenderUtils.translateAwayFromPivotPoint(stack, bone);
        }
        stack.popPose();
    }
}