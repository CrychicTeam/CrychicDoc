package com.mna.entities.models.faction;

import com.mna.api.tools.RLoc;
import com.mna.entities.faction.WitchHunter;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class WitchHunterModel extends GeoModel<WitchHunter> {

    private static final ResourceLocation modelFile = RLoc.create("geo/witchhunter.geo.json");

    private static final ResourceLocation animFile = RLoc.create("animations/witchhunter_anim.json");

    private static final ResourceLocation texFile_t1 = RLoc.create("textures/entity/witchhunter_t1.png");

    private static final ResourceLocation texFile_t2 = RLoc.create("textures/entity/witchhunter_t2.png");

    private static final ResourceLocation texFile_t3 = RLoc.create("textures/entity/witchhunter_t3.png");

    public ResourceLocation getAnimationResource(WitchHunter arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(WitchHunter arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(WitchHunter arg0) {
        switch(arg0.getTier()) {
            case 0:
                return texFile_t1;
            case 1:
                return texFile_t2;
            case 2:
                return texFile_t3;
            default:
                return texFile_t1;
        }
    }
}