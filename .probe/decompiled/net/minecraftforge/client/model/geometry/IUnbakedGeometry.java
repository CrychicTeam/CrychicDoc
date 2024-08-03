package net.minecraftforge.client.model.geometry;

import java.util.Set;
import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;

public interface IUnbakedGeometry<T extends IUnbakedGeometry<T>> {

    BakedModel bake(IGeometryBakingContext var1, ModelBaker var2, Function<Material, TextureAtlasSprite> var3, ModelState var4, ItemOverrides var5, ResourceLocation var6);

    default void resolveParents(Function<ResourceLocation, UnbakedModel> modelGetter, IGeometryBakingContext context) {
    }

    default Set<String> getConfigurableComponentNames() {
        return Set.of();
    }
}