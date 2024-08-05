package net.minecraftforge.client.model.geometry;

import java.util.function.Function;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.IModelBuilder;

public abstract class SimpleUnbakedGeometry<T extends SimpleUnbakedGeometry<T>> implements IUnbakedGeometry<T> {

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        TextureAtlasSprite particle = (TextureAtlasSprite) spriteGetter.apply(context.getMaterial("particle"));
        ResourceLocation renderTypeHint = context.getRenderTypeHint();
        RenderTypeGroup renderTypes = renderTypeHint != null ? context.getRenderType(renderTypeHint) : RenderTypeGroup.EMPTY;
        IModelBuilder<?> builder = IModelBuilder.of(context.useAmbientOcclusion(), context.useBlockLight(), context.isGui3d(), context.getTransforms(), overrides, particle, renderTypes);
        this.addQuads(context, builder, baker, spriteGetter, modelState, modelLocation);
        return builder.build();
    }

    protected abstract void addQuads(IGeometryBakingContext var1, IModelBuilder<?> var2, ModelBaker var3, Function<Material, TextureAtlasSprite> var4, ModelState var5, ResourceLocation var6);
}