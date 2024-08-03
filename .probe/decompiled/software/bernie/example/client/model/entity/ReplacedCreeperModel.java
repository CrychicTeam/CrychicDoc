package software.bernie.example.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.ReplacedCreeperEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class ReplacedCreeperModel extends DefaultedEntityGeoModel<ReplacedCreeperEntity> {

    public ReplacedCreeperModel() {
        super(new ResourceLocation("geckolib", "creeper"));
    }
}