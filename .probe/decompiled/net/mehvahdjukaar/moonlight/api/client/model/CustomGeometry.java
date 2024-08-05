package net.mehvahdjukaar.moonlight.api.client.model;

import java.util.function.Function;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

@FunctionalInterface
public interface CustomGeometry {

    CustomBakedModel bake(ModelBaker var1, Function<Material, TextureAtlasSprite> var2, ModelState var3, ResourceLocation var4);
}