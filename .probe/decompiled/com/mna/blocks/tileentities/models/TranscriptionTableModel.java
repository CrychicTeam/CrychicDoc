package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.TranscriptionTableTile;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class TranscriptionTableModel extends WizardLabModel<TranscriptionTableTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_transcription_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/laboratory_transcription_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/material/mandala_circle.png");

    public static final ResourceLocation ink = RLoc.create("block/wizard_lab/special/transcription_table_ink");

    public static final ResourceLocation lapis = RLoc.create("block/wizard_lab/special/transcription_table_lapis");

    public static final ResourceLocation book = RLoc.create("item/special/rote_book_open");

    public static final ResourceLocation book_2 = RLoc.create("item/special/rote_book_open_2");

    public TranscriptionTableModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "STATICS", ink));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "STATICS", lapis));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ROTE_BOOK", book, pose -> {
            pose.translate(0.25, 1.1, 0.47);
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.ZP.rotationDegrees(15.0F));
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ROTE_BOOK", book_2, pose -> {
            pose.translate(0.25, 1.1, 0.47);
            pose.mulPose(Axis.YP.rotationDegrees(90.0F));
            pose.scale(0.5F, 0.5F, 0.5F);
            pose.mulPose(Axis.ZP.rotationDegrees(15.0F));
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(3, "MANDALA_ROOT", ItemStack.EMPTY, pose -> {
            pose.translate(0.5, 0.0, 0.5);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
    }

    public ResourceLocation getAnimationResource(TranscriptionTableTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(TranscriptionTableTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(TranscriptionTableTile arg0) {
        return texFile;
    }
}