package net.mehvahdjukaar.moonlight.api.client.model.forge;

import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.client.model.CustomGeometry;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;

public class GeometryWrapper implements IUnbakedGeometry<GeometryWrapper> {

    private final CustomGeometry owner;

    public GeometryWrapper(CustomGeometry owner) {
        this.owner = owner;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext iGeometryBakingContext, ModelBaker bakery, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides itemOverrides, ResourceLocation modelLocation) {
        return this.owner.bake(bakery, spriteGetter, modelState, modelLocation);
    }
}