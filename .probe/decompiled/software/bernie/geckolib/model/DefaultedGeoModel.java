package software.bernie.geckolib.model;

import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public abstract class DefaultedGeoModel<T extends GeoAnimatable> extends GeoModel<T> {

    private ResourceLocation modelPath;

    private ResourceLocation texturePath;

    private ResourceLocation animationsPath;

    public DefaultedGeoModel(ResourceLocation assetSubpath) {
        this.modelPath = this.buildFormattedModelPath(assetSubpath);
        this.texturePath = this.buildFormattedTexturePath(assetSubpath);
        this.animationsPath = this.buildFormattedAnimationPath(assetSubpath);
    }

    public DefaultedGeoModel<T> withAltModel(ResourceLocation altPath) {
        this.modelPath = this.buildFormattedModelPath(altPath);
        return this;
    }

    public DefaultedGeoModel<T> withAltAnimations(ResourceLocation altPath) {
        this.animationsPath = this.buildFormattedAnimationPath(altPath);
        return this;
    }

    public DefaultedGeoModel<T> withAltTexture(ResourceLocation altPath) {
        this.texturePath = this.buildFormattedTexturePath(altPath);
        return this;
    }

    public ResourceLocation buildFormattedModelPath(ResourceLocation basePath) {
        return new ResourceLocation(basePath.getNamespace(), "geo/" + this.subtype() + "/" + basePath.getPath() + ".geo.json");
    }

    public ResourceLocation buildFormattedAnimationPath(ResourceLocation basePath) {
        return new ResourceLocation(basePath.getNamespace(), "animations/" + this.subtype() + "/" + basePath.getPath() + ".animation.json");
    }

    public ResourceLocation buildFormattedTexturePath(ResourceLocation basePath) {
        return new ResourceLocation(basePath.getNamespace(), "textures/" + this.subtype() + "/" + basePath.getPath() + ".png");
    }

    protected abstract String subtype();

    @Override
    public ResourceLocation getModelResource(T animatable) {
        return this.modelPath;
    }

    @Override
    public ResourceLocation getTextureResource(T animatable) {
        return this.texturePath;
    }

    @Override
    public ResourceLocation getAnimationResource(T animatable) {
        return this.animationsPath;
    }
}