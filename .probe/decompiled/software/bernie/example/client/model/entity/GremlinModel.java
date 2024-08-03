package software.bernie.example.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class GremlinModel extends DefaultedEntityGeoModel<DynamicExampleEntity> {

    public GremlinModel() {
        super(new ResourceLocation("geckolib", "gremlin"));
    }
}