package software.bernie.example.client.model.block;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import software.bernie.example.block.entity.GeckoHabitatBlockEntity;
import software.bernie.geckolib.model.DefaultedBlockGeoModel;

public class GeckoHabitatModel extends DefaultedBlockGeoModel<GeckoHabitatBlockEntity> {

    public GeckoHabitatModel() {
        super(new ResourceLocation("geckolib", "gecko_habitat"));
    }

    public RenderType getRenderType(GeckoHabitatBlockEntity animatable, ResourceLocation texture) {
        return RenderType.entityTranslucent(this.getTextureResource(animatable));
    }
}