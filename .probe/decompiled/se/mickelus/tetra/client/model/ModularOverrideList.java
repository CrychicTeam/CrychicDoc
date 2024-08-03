package se.mickelus.tetra.client.model;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.collect.ImmutableList;
import it.unimi.dsi.fastutil.ints.Int2ObjectOpenHashMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.model.QuadTransformers;
import net.minecraftforge.client.model.geometry.IGeometryBakingContext;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.mickelus.tetra.items.modular.IModularItem;
import se.mickelus.tetra.module.data.ModuleModel;

@ParametersAreNonnullByDefault
public class ModularOverrideList extends ItemOverrides {

    private static final Logger logger = LogManager.getLogger();

    private final Cache<ModularOverrideList.CacheKey, BakedModel> bakedModelCache = CacheBuilder.newBuilder().maximumSize(1000L).expireAfterWrite(5L, TimeUnit.MINUTES).build();

    private final UnresolvedItemModel model;

    private final IGeometryBakingContext context;

    private final ModelBaker baker;

    private final Function<Material, TextureAtlasSprite> spriteGetter;

    private final ModelState modelState;

    private final ResourceLocation modelLocation;

    public ModularOverrideList(UnresolvedItemModel model, IGeometryBakingContext context, ModelBaker baker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState modelState, ResourceLocation modelLocation) {
        this.model = model;
        this.context = context;
        this.baker = baker;
        this.spriteGetter = spriteGetter;
        this.modelState = modelState;
        this.modelLocation = modelLocation;
    }

    public void clearCache() {
        logger.debug("Clearing item model cache for " + this.modelLocation);
        this.bakedModelCache.invalidateAll();
    }

    @Nullable
    @Override
    public BakedModel resolve(BakedModel originalModel, ItemStack stack, @Nullable ClientLevel world, @Nullable LivingEntity entity, int renderId) {
        CompoundTag baseTag = stack.getTag();
        BakedModel result = originalModel;
        if (baseTag != null && !baseTag.isEmpty()) {
            ModularOverrideList.CacheKey key = this.getCacheKey(stack, entity, originalModel);
            try {
                result = (BakedModel) this.bakedModelCache.get(key, () -> this.getOverrideModel(stack, world, entity));
            } catch (ExecutionException var10) {
                var10.printStackTrace();
            }
        }
        return result;
    }

    protected BakedModel getOverrideModel(ItemStack itemStack, @Nullable Level world, @Nullable LivingEntity entity) {
        IModularItem item = (IModularItem) itemStack.getItem();
        String transformVariant = item.getTransformVariant(itemStack, entity);
        ItemTransforms cameraTransforms = this.model.getCameraTransforms(transformVariant);
        BakingContextWrapper contextWrapper = new BakingContextWrapper(this.context, cameraTransforms);
        List<ModuleModel> models = item.getModels(itemStack, entity);
        Set<ItemDisplayContext> contexts = (Set<ItemDisplayContext>) models.stream().map(modelx -> modelx.contexts).filter(Objects::nonNull).flatMap(Arrays::stream).filter(Objects::nonNull).collect(Collectors.toSet());
        ItemLayerModel model = this.createLayerModel(this.filterModels(models, null));
        if (!contexts.isEmpty()) {
            Map<ItemDisplayContext, ItemLayerModel> perspectiveModels = (Map<ItemDisplayContext, ItemLayerModel>) contexts.stream().collect(Collectors.toUnmodifiableMap(p -> p, p -> this.createLayerModel(this.filterModels(models, p))));
            TetraSeparateTransformsModel transformsModel = new TetraSeparateTransformsModel(model, perspectiveModels);
            return transformsModel.bake(contextWrapper, this.baker, this.spriteGetter, this.modelState, ItemOverrides.EMPTY, this.modelLocation);
        } else {
            return model.bake(contextWrapper, this.baker, this.spriteGetter, this.modelState, ItemOverrides.EMPTY, this.modelLocation);
        }
    }

    protected ItemLayerModel createLayerModel(List<ModuleModel> models) {
        ImmutableList<Material> textures = (ImmutableList<Material>) models.stream().map(moduleModel -> new Material(TextureAtlas.LOCATION_BLOCKS, moduleModel.location)).collect(Collectors.collectingAndThen(Collectors.toList(), ImmutableList::copyOf));
        Int2ObjectOpenHashMap<ResourceLocation> renderTypes = new Int2ObjectOpenHashMap();
        QuadTransformerBuilder builder = new QuadTransformerBuilder();
        for (int i = 0; i < models.size(); i++) {
            ModuleModel model = (ModuleModel) models.get(i);
            if (model.tint != -1) {
                builder.add(i, new ColorQuadTransformer(model.tint));
            }
            if (model.emission >= 0 && model.emission < 16) {
                builder.add(i, QuadTransformers.settingEmissivity(model.emission));
            }
            if (model.transform != null) {
                builder.add(i, QuadTransformers.applying(model.transform));
            }
            if (model.renderType != null) {
                renderTypes.put(i, model.renderType);
            }
        }
        return new ItemLayerModel(textures, builder.get(), renderTypes);
    }

    protected List<ModuleModel> filterModels(List<ModuleModel> models, @Nullable ItemDisplayContext context) {
        return models.stream().filter(model -> model.contexts == null || ArrayUtils.contains(model.contexts, context) != model.invertPerspectives).toList();
    }

    protected ModularOverrideList.CacheKey getCacheKey(ItemStack itemStack, LivingEntity entity, BakedModel original) {
        return new ModularOverrideList.CacheKey(original, ((IModularItem) itemStack.getItem()).getModelCacheKey(itemStack, entity));
    }

    protected static class CacheKey {

        final BakedModel parent;

        final String data;

        protected CacheKey(BakedModel parent, String hash) {
            this.parent = parent;
            this.data = hash;
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            } else if (o != null && this.getClass() == o.getClass()) {
                ModularOverrideList.CacheKey cacheKey = (ModularOverrideList.CacheKey) o;
                return (this.parent != null ? this.parent == cacheKey.parent : cacheKey.parent == null) ? Objects.equals(this.data, cacheKey.data) : false;
            } else {
                return false;
            }
        }

        public int hashCode() {
            int result = this.parent != null ? this.parent.hashCode() : 0;
            return 31 * result + (this.data != null ? this.data.hashCode() : 0);
        }
    }
}