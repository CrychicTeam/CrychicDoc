package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.SpellSpecializationTile;
import net.minecraft.resources.ResourceLocation;

public class SpecializationDeskModel extends WizardLabModel<SpellSpecializationTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_mastery_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/laboratory_mastery_armature.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation crystal_left = RLoc.create("block/wizard_lab/special/specialization_desk_crystal_left");

    public static final ResourceLocation crystal_right = RLoc.create("block/wizard_lab/special/specialization_desk_crystal_right");

    public static final ResourceLocation crystal_back = RLoc.create("block/wizard_lab/special/specialization_desk_crystal_back");

    public SpecializationDeskModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(-1, "CRYSTAL_REAR", crystal_back));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(-1, "CRYSTAL_FRONTRIGHT", crystal_right));
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(-1, "CRYSTAL_FRONTLEFT", crystal_left));
    }

    public ResourceLocation getAnimationResource(SpellSpecializationTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(SpellSpecializationTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(SpellSpecializationTile arg0) {
        return texFile;
    }
}