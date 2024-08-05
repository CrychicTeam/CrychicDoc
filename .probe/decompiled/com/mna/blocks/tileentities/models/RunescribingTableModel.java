package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mojang.math.Axis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class RunescribingTableModel extends WizardLabModel<RunescribingTableTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_simple_rooted_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/none.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation recipe = RLoc.create("block/wizard_lab/special/runescribing_table_recipe");

    public static final ResourceLocation water = RLoc.create("block/wizard_lab/special/runescribing_table_water");

    public static final ResourceLocation hammer = RLoc.create("item/runesmith_hammer");

    public static final ResourceLocation chisel = RLoc.create("item/runesmith_chisel");

    public RunescribingTableModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(3, "ROOT", recipe));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(4, "ROOT", water));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "ROOT", hammer, pose -> {
            pose.translate(0.155, 1.025, 0.6);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(1, "ROOT", chisel, pose -> {
            pose.translate(0.345, 1.085, 0.6);
            pose.scale(0.5F, 0.5F, 0.5F);
        }));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(2, "ROOT", ItemStack.EMPTY, pose -> {
            pose.translate(0.625, 1.035, 0.275);
            pose.mulPose(Axis.XP.rotationDegrees(90.0F));
            pose.scale(0.8F, 0.8F, 0.8F);
        }));
    }

    public ResourceLocation getAnimationResource(RunescribingTableTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(RunescribingTableTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(RunescribingTableTile arg0) {
        return texFile;
    }
}