package net.blay09.mods.balm.common.client.rendering;

import com.mojang.math.Transformation;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.function.Predicate;
import net.blay09.mods.balm.api.client.BalmClient;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;

public abstract class AbstractCachedDynamicModel implements BakedModel {

    private final Map<String, BakedModel> cache = new HashMap();

    private final Map<ResourceLocation, BakedModel> baseModelCache = new HashMap();

    private final ModelBakery modelBakery;

    private final Function<BlockState, ResourceLocation> baseModelFunction;

    private final List<Pair<Predicate<BlockState>, BakedModel>> parts;

    private final Function<BlockState, Map<String, String>> textureMapFunction;

    private final BiConsumer<BlockState, Matrix4f> transformFunction;

    private final ResourceLocation location;

    private TextureAtlasSprite particleTexture;

    public AbstractCachedDynamicModel(ModelBakery modelBakery, Function<BlockState, ResourceLocation> baseModelFunction, @Nullable List<Pair<Predicate<BlockState>, BakedModel>> parts, @Nullable Function<BlockState, Map<String, String>> textureMapFunction, @Nullable BiConsumer<BlockState, Matrix4f> transformFunction, List<RenderType> renderTypes, ResourceLocation location) {
        this.modelBakery = modelBakery;
        this.baseModelFunction = baseModelFunction;
        this.parts = parts;
        this.textureMapFunction = textureMapFunction;
        this.transformFunction = transformFunction;
        this.location = location;
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, RandomSource rand) {
        if (state != null) {
            Matrix4f transform = BlockModelRotation.X0_Y0.getRotation().getMatrix();
            String stateString = state.toString();
            BakedModel bakedModel;
            synchronized (this.cache) {
                bakedModel = (BakedModel) this.cache.get(stateString);
                if (bakedModel == null) {
                    if (this.transformFunction != null) {
                        this.transformFunction.accept(state, transform);
                    }
                    BalmModels models = BalmClient.getModels();
                    ModelState modelTransform = models.getModelState(new Transformation(transform));
                    ResourceLocation baseModelLocation = (ResourceLocation) this.baseModelFunction.apply(state);
                    if (this.textureMapFunction != null && !this.baseModelCache.containsKey(baseModelLocation)) {
                        UnbakedModel baseModel = models.getUnbakedModelOrMissing(baseModelLocation);
                        BakedModel bakedBaseModel = baseModel.bake(models.createBaker(baseModelLocation, this::getSprite), Material::m_119204_, modelTransform, baseModelLocation);
                        this.baseModelCache.put(baseModelLocation, bakedBaseModel);
                    }
                    UnbakedModel retexturedBaseModel = this.textureMapFunction != null ? models.retexture(this.modelBakery, baseModelLocation, (Map<String, String>) this.textureMapFunction.apply(state)) : models.getUnbakedModelOrMissing(baseModelLocation);
                    bakedModel = retexturedBaseModel.bake(models.createBaker(this.location, this::getSprite), Material::m_119204_, modelTransform, this.location);
                    this.cache.put(stateString, bakedModel);
                    if (this.particleTexture == null && bakedModel != null) {
                        this.particleTexture = bakedModel.getParticleIcon();
                    }
                }
            }
            return bakedModel != null ? bakedModel.getQuads(state, side, rand) : Collections.emptyList();
        } else {
            return Collections.emptyList();
        }
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean usesBlockLight() {
        return false;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public TextureAtlasSprite getParticleIcon() {
        return this.particleTexture != null ? this.particleTexture : new Material(TextureAtlas.LOCATION_BLOCKS, MissingTextureAtlasSprite.getLocation()).sprite();
    }

    @Override
    public ItemTransforms getTransforms() {
        return ItemTransforms.NO_TRANSFORMS;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }

    public abstract List<RenderType> getBlockRenderTypes(BlockState var1, RandomSource var2);

    public abstract List<RenderType> getItemRenderTypes(ItemStack var1, boolean var2);

    private TextureAtlasSprite getSprite(ResourceLocation modelLocation, Material material) {
        return material.sprite();
    }
}