package com.mna.entities.models.boss;

import com.mna.api.tools.RLoc;
import com.mna.entities.boss.WitherLich;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LichModel extends GeoModel<WitherLich> {

    private static final ResourceLocation modelFile = RLoc.create("geo/bosses/wither_lich.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/bosses/wither_lich.animation.json");

    private static final ResourceLocation[] texFiles = new ResourceLocation[] { RLoc.create("textures/entity/boss/wither_lich_variant_1.png"), RLoc.create("textures/entity/boss/wither_lich_variant_2.png"), RLoc.create("textures/entity/boss/wither_lich_variant_3.png"), RLoc.create("textures/entity/boss/wither_lich_variant_4.png") };

    public ResourceLocation getAnimationResource(WitherLich arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(WitherLich arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(WitherLich arg0) {
        return texFiles[arg0.getTextureVariant()];
    }
}