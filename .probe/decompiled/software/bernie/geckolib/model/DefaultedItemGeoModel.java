package software.bernie.geckolib.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class DefaultedItemGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    public DefaultedItemGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "item";
    }

    public DefaultedItemGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltModel(altPath);
    }

    public DefaultedItemGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltAnimations(altPath);
    }

    public DefaultedItemGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedItemGeoModel<T>) super.withAltTexture(altPath);
    }
}