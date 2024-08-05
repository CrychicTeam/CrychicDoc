package software.bernie.example.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.BatEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class BatModel extends DefaultedEntityGeoModel<BatEntity> {

    public BatModel() {
        super(new ResourceLocation("geckolib", "bat"), true);
    }
}