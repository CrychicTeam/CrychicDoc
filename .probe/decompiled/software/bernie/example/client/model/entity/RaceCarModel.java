package software.bernie.example.client.model.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.RaceCarEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class RaceCarModel extends DefaultedEntityGeoModel<RaceCarEntity> {

    public RaceCarModel() {
        super(new ResourceLocation("geckolib", "race_car"));
    }

    public RenderType getRenderType(RaceCarEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}