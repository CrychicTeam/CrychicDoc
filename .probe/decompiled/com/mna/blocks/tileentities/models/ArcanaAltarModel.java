package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.ArcanaAltarTile;
import com.mna.blocks.tileentities.wizard_lab.WizardLabTile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class ArcanaAltarModel extends WizardLabModel<ArcanaAltarTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/altar_of_arcana_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/altar_of_arcana_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/artifice/wizard_lab/mandala_ensorcellation.png");

    public static final ResourceLocation candle_short = RLoc.create("block/wizard_lab/special/altar_of_arcana_candle_short");

    public static final ResourceLocation candle_long = RLoc.create("block/wizard_lab/special/altar_of_arcana_candle_long");

    public ArcanaAltarModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "ITEM_FOCUS", ItemStack.EMPTY, pose -> {
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.translate(0.0, -0.05, -0.05);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "ITEM_RECIPE", ItemStack.EMPTY, pose -> {
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.translate(0.0, -0.05, -0.05);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ITEM_FOCUS", ItemStack.EMPTY, pose -> {
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.translate(0.0, -0.05, -0.05);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
    }

    public ResourceLocation getAnimationResource(ArcanaAltarTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(ArcanaAltarTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(ArcanaAltarTile arg0) {
        return texFile;
    }

    @Override
    public void renderBoneAdditions(WizardLabTile tile, String bone, PoseStack stack, MultiBufferSource bufferSource, RenderType renderType, int packedLightIn, int packedOverlayIn) {
        stack.translate(0.5, 0.0, 0.5);
        super.renderBoneAdditions(tile, bone, stack, bufferSource, renderType, packedLightIn, packedOverlayIn);
    }
}