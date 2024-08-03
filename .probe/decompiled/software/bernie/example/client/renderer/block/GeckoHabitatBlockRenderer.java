package software.bernie.example.client.renderer.block;

import software.bernie.example.block.entity.GeckoHabitatBlockEntity;
import software.bernie.example.client.model.block.GeckoHabitatModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class GeckoHabitatBlockRenderer extends GeoBlockRenderer<GeckoHabitatBlockEntity> {

    public GeckoHabitatBlockRenderer() {
        super(new GeckoHabitatModel());
    }
}