package com.mna.blocks.tileentities.renderers.wizard_lab;

import com.mna.blocks.tileentities.models.WizardLabModel;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class WizardLabBoneAdditionRenderer<T extends WizardLabTile> extends GeoRenderLayer<T> {

    public WizardLabBoneAdditionRenderer(GeoRenderer<T> entityRendererIn) {
        super(entityRendererIn);
    }

    public void renderForBone(PoseStack poseStack, T animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(-0.5, 0.0, -0.5);
        ((WizardLabModel) this.getGeoModel()).renderBoneAdditions(animatable, bone.getName(), poseStack, bufferSource, renderType, packedLight, packedOverlay);
        poseStack.popPose();
    }
}