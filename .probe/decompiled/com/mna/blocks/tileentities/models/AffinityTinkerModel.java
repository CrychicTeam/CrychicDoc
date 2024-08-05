package com.mna.blocks.tileentities.models;

import com.mna.api.tools.RLoc;
import com.mna.blocks.tileentities.wizard_lab.AffinityTinkerTile;
import net.minecraft.resources.ResourceLocation;

public class AffinityTinkerModel extends WizardLabModel<AffinityTinkerTile> {

    private static final ResourceLocation modelFile = RLoc.create("geo/block/laboratory_simple_rooted_armature.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/block/none.animation.json");

    private static final ResourceLocation texFile = RLoc.create("textures/block/inscription_table_deco.png");

    public static final ResourceLocation scroll = RLoc.create("block/wizard_lab/special/affinity_tinker_scroll");

    public AffinityTinkerModel() {
        this.boneOverrides.add(new WizardLabModel.GeoBoneRenderer(0, "ROOT", scroll));
    }

    public ResourceLocation getAnimationResource(AffinityTinkerTile arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(AffinityTinkerTile arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(AffinityTinkerTile arg0) {
        return texFile;
    }
}