package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.DisenchanterTile;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DisenchanterModel extends WizardLabModel<DisenchanterTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_disenchanter_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/laboratory_disenchantment_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation crystal = RLoc.create("block/wizard_lab/special/disenchanter_crystal");

    public DisenchanterModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(-1, "CRYSTAL", crystal));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "STATICS", ItemStack.EMPTY, pose -> {
            pose.translate(0.5, 1.39, 0.4);
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(0.8F, 0.8F, 0.8F);
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "STATICS", ItemStack.EMPTY, pose -> {
            pose.translate(0.5, 1.04, 0.4);
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(0.8F, 0.8F, 0.8F);
        }));
    }

    public ResourceLocation getAnimationResource(DisenchanterTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(DisenchanterTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(DisenchanterTile arg0) {
        return texFile;
    }
}