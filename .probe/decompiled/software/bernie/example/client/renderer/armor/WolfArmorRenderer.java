package software.bernie.example.client.renderer.armor;

import net.minecraft.resources.ResourceLocation;
import software.bernie.example.item.WolfArmorItem;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoArmorRenderer;

public final class WolfArmorRenderer extends GeoArmorRenderer<WolfArmorItem> {

    public WolfArmorRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation("geckolib", "armor/wolf_armor")));
    }
}