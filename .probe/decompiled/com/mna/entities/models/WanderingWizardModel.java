package com.mna.entities.models;

import com.mna.entities.utility.WanderingWizard;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WanderingWizardModel extends GeoModel<WanderingWizard> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/wandering_wizard.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/wandering_wizard.anim.json");

    private static final ResourceLocation texFile = new ResourceLocation("mna", "textures/entity/wandering_wizard.png");

    public ResourceLocation getAnimationResource(WanderingWizard arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(WanderingWizard arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(WanderingWizard arg0) {
        return texFile;
    }
}