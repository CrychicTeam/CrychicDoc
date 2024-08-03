package org.embeddedt.modernfix.api.entrypoint;

import java.util.function.Function;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public interface ModernFixClientIntegration {

    default void onDynamicResourcesStatusChange(boolean enabled) {
    }

    default UnbakedModel onUnbakedModelLoad(ResourceLocation location, UnbakedModel originalModel, ModelBakery bakery) {
        return originalModel;
    }

    default UnbakedModel onUnbakedModelPreBake(ResourceLocation location, UnbakedModel originalModel, ModelBakery bakery) {
        return originalModel;
    }

    @Deprecated
    default BakedModel onBakedModelLoad(ResourceLocation location, UnbakedModel baseModel, BakedModel originalModel, ModelState state, ModelBakery bakery) {
        return originalModel;
    }

    default BakedModel onBakedModelLoad(ResourceLocation location, UnbakedModel baseModel, BakedModel originalModel, ModelState state, ModelBakery bakery, Function<Material, TextureAtlasSprite> textureGetter) {
        return this.onBakedModelLoad(location, baseModel, originalModel, state, bakery);
    }
}