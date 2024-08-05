package com.mna.entities.models.boss;

import com.mna.api.tools.RLoc;
import com.mna.entities.boss.FaerieQueen;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class FaerieQueenModel extends GeoModel<FaerieQueen> {

    private static final ResourceLocation animFile = RLoc.create("animations/bosses/fey_queen.animation.json");

    private static final ResourceLocation modelFile_summer = RLoc.create("geo/bosses/fey_queen_summer.geo.json");

    private static final ResourceLocation texFile_summer = RLoc.create("textures/entity/boss/fey_queen_summer.png");

    private static final ResourceLocation modelFile_winter = RLoc.create("geo/bosses/fey_queen_winter.geo.json");

    private static final ResourceLocation texFile_winter = RLoc.create("textures/entity/boss/fey_queen_winter.png");

    private static final ResourceLocation texFile_illusion = RLoc.create("textures/block/illusion_block.png");

    public ResourceLocation getAnimationResource(FaerieQueen queen) {
        return animFile;
    }

    public ResourceLocation getModelResource(FaerieQueen queen) {
        return queen.isWinter() ? modelFile_winter : modelFile_summer;
    }

    public ResourceLocation getTextureResource(FaerieQueen queen) {
        if (queen.isExiting()) {
            return texFile_illusion;
        } else {
            return queen.isWinter() ? texFile_winter : texFile_summer;
        }
    }
}