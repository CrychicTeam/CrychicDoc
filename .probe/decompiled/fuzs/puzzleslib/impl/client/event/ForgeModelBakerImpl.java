package fuzs.puzzleslib.impl.client.event;

import com.mojang.math.Transformation;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.mixin.client.accessor.ModelBakeryAccessor;
import java.util.Map;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.AtlasSet;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public record ForgeModelBakerImpl(Map<ForgeModelBakerImpl.BakedCacheKey, BakedModel> bakedCache, Function<ResourceLocation, UnbakedModel> unbakedModelGetter, Function<Material, TextureAtlasSprite> modelTextureGetter, BakedModel missingModel) implements ModelBaker {

    private static Map<ResourceLocation, AtlasSet.StitchResult> capturedAtlasPreparations;

    public ForgeModelBakerImpl(ResourceLocation modelLocation, Map<ForgeModelBakerImpl.BakedCacheKey, BakedModel> bakedCache, Function<ResourceLocation, UnbakedModel> modelGetter, BiConsumer<ResourceLocation, Material> missingTextureConsumer, BakedModel missingModel) {
        this(bakedCache, modelGetter, material -> {
            Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations = capturedAtlasPreparations;
            Objects.requireNonNull(atlasPreparations, "atlas preparations is null");
            AtlasSet.StitchResult stitchResult = (AtlasSet.StitchResult) atlasPreparations.get(material.atlasLocation());
            TextureAtlasSprite textureatlassprite = stitchResult.getSprite(material.texture());
            if (textureatlassprite != null) {
                return textureatlassprite;
            } else {
                missingTextureConsumer.accept(modelLocation, material);
                return stitchResult.missing();
            }
        }, missingModel);
    }

    public static void setAtlasPreparations(Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations) {
        capturedAtlasPreparations = atlasPreparations;
    }

    @Override
    public UnbakedModel getModel(ResourceLocation resourceLocation) {
        return (UnbakedModel) this.unbakedModelGetter.apply(resourceLocation);
    }

    @Nullable
    @Override
    public BakedModel bake(ResourceLocation resourceLocation, ModelState modelState) {
        return this.bake(resourceLocation, modelState, this.modelTextureGetter);
    }

    @Nullable
    public BakedModel bake(ResourceLocation resourceLocation, ModelState modelState, Function<Material, TextureAtlasSprite> modelTextureGetter) {
        return this.bake(this.getModel(resourceLocation), resourceLocation, modelState, modelTextureGetter);
    }

    public BakedModel bake(UnbakedModel unbakedModel, ResourceLocation resourceLocation) {
        return this.bake(unbakedModel, resourceLocation, BlockModelRotation.X0_Y0, this.modelTextureGetter);
    }

    private BakedModel bake(UnbakedModel unbakedModel, ResourceLocation resourceLocation, ModelState modelState, Function<Material, TextureAtlasSprite> modelTextureGetter) {
        ForgeModelBakerImpl.BakedCacheKey key = new ForgeModelBakerImpl.BakedCacheKey(resourceLocation, modelState.getRotation(), modelState.isUvLocked());
        BakedModel bakedModel = (BakedModel) this.bakedCache.get(key);
        if (bakedModel == null) {
            if (unbakedModel instanceof BlockModel blockModel && blockModel.getRootModel() == ModelBakery.GENERATION_MARKER) {
                return ModelBakeryAccessor.puzzleslib$getItemModelGenerator().generateBlockModel(modelTextureGetter, blockModel).bake(this, blockModel, modelTextureGetter, modelState, resourceLocation, false);
            }
            try {
                bakedModel = unbakedModel.bake(this, modelTextureGetter, modelState, resourceLocation);
            } catch (Exception var8) {
                PuzzlesLib.LOGGER.warn("Unable to bake model: '{}': {}", resourceLocation, var8);
                bakedModel = this.missingModel;
            }
            this.bakedCache.put(key, bakedModel);
        }
        return bakedModel;
    }

    public Function<Material, TextureAtlasSprite> getModelTextureGetter() {
        return this.modelTextureGetter;
    }

    static {
        ModContainerHelper.getOptionalModEventBus("puzzleslib").ifPresent(eventBus -> eventBus.addListener(evt -> capturedAtlasPreparations = null));
    }

    public static record BakedCacheKey(ResourceLocation resourceLocation, Transformation rotation, boolean isUvLocked) {
    }
}