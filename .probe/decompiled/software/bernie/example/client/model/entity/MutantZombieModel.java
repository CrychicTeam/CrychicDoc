package software.bernie.example.client.model.entity;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.entity.DynamicExampleEntity;
import software.bernie.geckolib.model.DefaultedEntityGeoModel;

public class MutantZombieModel extends DefaultedEntityGeoModel<DynamicExampleEntity> {

    public MutantZombieModel() {
        super(new ResourceLocation("geckolib", "mutant_zombie"));
    }
}