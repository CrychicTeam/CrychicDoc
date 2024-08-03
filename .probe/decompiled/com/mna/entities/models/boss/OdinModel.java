package com.mna.entities.models.boss;

import com.mna.entities.boss.Odin;
import com.mna.events.seasonal.SeasonalHelper;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class OdinModel extends GeoModel<Odin> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/bosses/odin.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/bosses/odin.animation.json");

    private static final ResourceLocation texFile_xmas = new ResourceLocation("mna", "textures/entity/boss/odin_holiday.png");

    private static final ResourceLocation texFile = new ResourceLocation("mna", "textures/entity/boss/odin.png");

    public ResourceLocation getAnimationResource(Odin arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(Odin arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(Odin arg0) {
        return SeasonalHelper.isChristmas() ? texFile_xmas : texFile;
    }
}