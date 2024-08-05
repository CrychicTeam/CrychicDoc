package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.ThesisDeskTile;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;

public class ThesisDeskModel extends WizardLabModel<ThesisDeskTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_thesis_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/laboratory_thesis_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation ink = RLoc.create("block/wizard_lab/special/thesis_desk_ink");

    public static final ResourceLocation vellum = RLoc.create("block/wizard_lab/special/thesis_desk_vellum");

    public static final ResourceLocation book = RLoc.create("item/special/rote_book_open");

    public static final ResourceLocation book_2 = RLoc.create("item/special/rote_book_open_2");

    public ThesisDeskModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "ROOT", ink));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "ROOT", vellum));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ROOT", book, pose -> {
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.translate(-0.85, 1.15, 0.25);
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.ZP.rotationDegrees(6.0F));
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ROOT", book_2, pose -> {
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.translate(-0.85, 1.15, 0.25);
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.ZP.rotationDegrees(6.0F));
        }));
    }

    public ResourceLocation getAnimationResource(ThesisDeskTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(ThesisDeskTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(ThesisDeskTile arg0) {
        return texFile;
    }
}