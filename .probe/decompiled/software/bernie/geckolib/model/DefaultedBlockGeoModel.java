package software.bernie.geckolib.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class DefaultedBlockGeoModel<T extends GeoAnimatable> extends DefaultedGeoModel<T> {

    public DefaultedBlockGeoModel(ResourceLocation assetSubpath) {
        super(assetSubpath);
    }

    @Override
    protected String subtype() {
        return "block";
    }

    public DefaultedBlockGeoModel<T> withAltModel(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltModel(altPath);
    }

    public DefaultedBlockGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltAnimations(altPath);
    }

    public DefaultedBlockGeoModel<T> withAltTexture(ResourceLocation altPath) {
        return (DefaultedBlockGeoModel<T>) super.withAltTexture(altPath);
    }
}