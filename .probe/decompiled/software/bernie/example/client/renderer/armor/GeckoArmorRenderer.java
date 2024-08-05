package software.bernie.example.client.renderer.armor;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.item.GeckoArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public final class GeckoArmorRenderer extends GeoArmorRenderer<GeckoArmorItem> {

    public GeckoArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("geckolib", "armor/gecko_armor")));
        this.addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}