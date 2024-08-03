package net.blay09.mods.balm.api.client.rendering;

import com.mojang.datafixers.util.Either;
import com.mojang.math.Transformation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public interface BalmModels {

    DeferredObject<BakedModel> loadModel(ResourceLocation var1);

    DeferredObject<BakedModel> bakeModel(ResourceLocation var1, UnbakedModel var2);

    default DeferredObject<BakedModel> loadDynamicModel(ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction) {
        return this.loadDynamicModel(identifier, modelFunction, textureMapFunction, transformFunction, Collections.emptyList());
    }

    DeferredObject<BakedModel> loadDynamicModel(ResourceLocation var1, @Nullable Function<BlockState, ResourceLocation> var2, @Nullable Function<BlockState, Map<String, String>> var3, @Nullable BiConsumer<BlockState, Matrix4f> var4, List<RenderType> var5);

    DeferredObject<BakedModel> retexture(ResourceLocation var1, Map<String, String> var2);

    void overrideModel(Supplier<Block> var1, Supplier<BakedModel> var2);

    ModelState getModelState(Transformation var1);

    UnbakedModel getUnbakedModelOrMissing(ResourceLocation var1);

    UnbakedModel getUnbakedMissingModel();

    default UnbakedModel retexture(ModelBakery bakery, ResourceLocation identifier, Map<String, String> textureMap) {
        Map<String, Either<Material, String>> replacedTexturesMapped = new HashMap();
        for (Entry<String, String> entry : textureMap.entrySet()) {
            replacedTexturesMapped.put((String) entry.getKey(), Either.left(new Material(TextureAtlas.LOCATION_BLOCKS, new ResourceLocation((String) entry.getValue()))));
        }
        BlockModel blockModel = new BlockModel(identifier, Collections.emptyList(), replacedTexturesMapped, false, BlockModel.GuiLight.FRONT, ItemTransforms.NO_TRANSFORMS, Collections.emptyList());
        blockModel.resolveParents(bakery::m_119341_);
        return blockModel;
    }

    ModelBaker createBaker(ResourceLocation var1, BiFunction<ResourceLocation, Material, TextureAtlasSprite> var2);
}