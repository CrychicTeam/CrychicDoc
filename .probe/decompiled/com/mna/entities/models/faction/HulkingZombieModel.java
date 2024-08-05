package com.mna.entities.models.faction;

import com.mna.entities.faction.HulkingZombie;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class HulkingZombieModel extends GeoModel<HulkingZombie> {

    private static final ResourceLocation modelFile = new ResourceLocation("mna", "geo/hulking_zombie.geo.json");

    private static final ResourceLocation animFile = new ResourceLocation("mna", "animations/hulking_zombie.anim.json");

    private static final ResourceLocation texFile_t1 = new ResourceLocation("mna", "textures/entity/hulking_zombie_t1.png");

    private static final ResourceLocation texFile_t2 = new ResourceLocation("mna", "textures/entity/hulking_zombie_t2.png");

    private static final ResourceLocation texFile_t3 = new ResourceLocation("mna", "textures/entity/hulking_zombie_t3.png");

    public ResourceLocation getAnimationResource(HulkingZombie arg0) {
        return animFile;
    }

    public ResourceLocation getModelResource(HulkingZombie arg0) {
        return modelFile;
    }

    public ResourceLocation getTextureResource(HulkingZombie arg0) {
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