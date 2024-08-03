package software.bernie.example.client.renderer.block;

import software.bernie.example.block.entity.FertilizerBlockEntity;
import software.bernie.example.client.model.block.FertilizerModel;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FertilizerBlockRenderer extends GeoBlockRenderer<FertilizerBlockEntity> {

    public FertilizerBlockRenderer() {
        super(new FertilizerModel());
    }
}