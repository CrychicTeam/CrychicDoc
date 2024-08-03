package net.minecraftforge.client.model;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableList.Builder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.ints.Int2ObjectArrayMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.function.Function;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockElement;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.ForgeRenderTypes;
import net.minecraftforge.client.RenderTypeGroup;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.client.model.geometry.IUnbakedGeometry;
import net.minecraftforge.client.model.geometry.UnbakedGeometryHelper;
import org.jetbrains.annotations.Nullable;

public class ItemLayerModel implements IUnbakedGeometry<ItemLayerModel> {

    @Nullable
    private ImmutableList<Material> textures;

    private final Int2ObjectMap<ForgeFaceData> layerData;

    private final Int2ObjectMap<ResourceLocation> renderTypeNames;

    private ItemLayerModel(@Nullable ImmutableList<Material> textures, Int2ObjectMap<ForgeFaceData> layerData, Int2ObjectMap<ResourceLocation> renderTypeNames) {
        this.textures = textures;
        this.layerData = layerData;
        this.renderTypeNames = renderTypeNames;
    }

    @Override
    public BakedModel bake(IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ItemOverrides overrides, ResourceLocation modelLocation) {
        if (this.textures == null) {
            Builder<Material> builder = ImmutableList.builder();
            for (int i = 0; context.hasMaterial("layer" + i); i++) {
                builder.add(context.getMaterial("layer" + i));
            }
            this.textures = builder.build();
        }
        TextureAtlasSprite particle = (TextureAtlasSprite) spriteGetter.apply(context.hasMaterial("particle") ? context.getMaterial("particle") : (Material) this.textures.get(0));
        Transformation rootTransform = context.getRootTransform();
        if (!rootTransform.isIdentity()) {
            modelState = UnbakedGeometryHelper.composeRootTransformIntoModelState(modelState, rootTransform);
        }
        RenderTypeGroup normalRenderTypes = new RenderTypeGroup(RenderType.translucent(), ForgeRenderTypes.ITEM_UNSORTED_TRANSLUCENT.get());
        CompositeModel.Baked.Builder builder = CompositeModel.Baked.builder(context, particle, overrides, context.getTransforms());
        for (int i = 0; i < this.textures.size(); i++) {
            TextureAtlasSprite sprite = (TextureAtlasSprite) spriteGetter.apply((Material) this.textures.get(i));
            List<BlockElement> unbaked = UnbakedGeometryHelper.createUnbakedItemElements(i, sprite.contents(), (ForgeFaceData) this.layerData.get(i));
            List<BakedQuad> quads = UnbakedGeometryHelper.bakeElements(unbaked, $ -> sprite, modelState, modelLocation);
            ResourceLocation renderTypeName = (ResourceLocation) this.renderTypeNames.get(i);
            RenderTypeGroup renderTypes = renderTypeName != null ? context.getRenderType(renderTypeName) : null;
            builder.addQuads(renderTypes != null ? renderTypes : normalRenderTypes, quads);
        }
        return builder.build();
    }

    public static final class Loader implements IGeometryLoader<ItemLayerModel> {

        public static final ItemLayerModel.Loader INSTANCE = new ItemLayerModel.Loader();

        public ItemLayerModel read(JsonObject jsonObject, JsonDeserializationContext deserializationContext) {
            Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames = new Int2ObjectOpenHashMap();
            if (jsonObject.has("render_types")) {
                JsonObject renderTypes = jsonObject.getAsJsonObject("render_types");
                for (Entry<String, JsonElement> entry : renderTypes.entrySet()) {
                    ResourceLocation renderType = new ResourceLocation((String) entry.getKey());
                    for (JsonElement layer : ((JsonElement) entry.getValue()).getAsJsonArray()) {
                        if (renderTypeNames.put(layer.getAsInt(), renderType) != null) {
                            throw new JsonParseException("Registered duplicate render type for layer " + layer);
                        }
                    }
                }
            }
            Int2ObjectArrayMap<ForgeFaceData> emissiveLayers = new Int2ObjectArrayMap();
            if (jsonObject.has("forge_data")) {
                JsonObject forgeData = jsonObject.get("forge_data").getAsJsonObject();
                this.readLayerData(forgeData, "layers", renderTypeNames, emissiveLayers, false);
            }
            return new ItemLayerModel(null, emissiveLayers, renderTypeNames);
        }

        protected void readLayerData(JsonObject jsonObject, String name, Int2ObjectOpenHashMap<ResourceLocation> renderTypeNames, Int2ObjectMap<ForgeFaceData> layerData, boolean logWarning) {
            if (jsonObject.has(name)) {
                JsonObject fullbrightLayers = jsonObject.getAsJsonObject(name);
                for (Entry<String, JsonElement> entry : fullbrightLayers.entrySet()) {
                    int layer = Integer.parseInt((String) entry.getKey());
                    ForgeFaceData data = ForgeFaceData.read((JsonElement) entry.getValue(), ForgeFaceData.DEFAULT);
                    layerData.put(layer, data);
                }
            }
        }
    }
}