package software.bernie.example.client.model.entity;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.ParasiteEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ParasiteModel extends DefaultedEntityGeoModel<ParasiteEntity> {

    public ParasiteModel() {
        super(new ResourceLocation("geckolib", "parasite"));
    }

    public RenderType getRenderType(ParasiteEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}