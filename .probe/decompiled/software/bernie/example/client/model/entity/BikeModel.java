package software.bernie.example.client.model.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.BikeEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BikeModel extends DefaultedEntityGeoModel<BikeEntity> {

    public BikeModel() {
        super(new ResourceLocation("geckolib", "bike"));
    }

    public RenderType getRenderType(BikeEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}