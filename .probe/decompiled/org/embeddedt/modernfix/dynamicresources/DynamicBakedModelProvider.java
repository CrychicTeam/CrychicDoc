package org.embeddedt.modernfix.dynamicresources;

import com.google.common.collect.ImmutableSet;
import com.mojang.math.Transformation;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.duck.IExtendedModelBakery;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicBakedModelProvider implements Map<ResourceLocation, BakedModel> {

    private static final ImmutableSet<ResourceLocation> BAKE_SKIPPED_TOPLEVEL = ImmutableSet.builder().add(new ResourceLocation("custommachinery", "block/custom_machine_block")).build();

    public static DynamicBakedModelProvider currentInstance = null;

    private final ModelBakery bakery;

    private final Map<ModelBakery.BakedCacheKey, BakedModel> bakedCache;

    private final Map<ResourceLocation, BakedModel> permanentOverrides;

    private BakedModel missingModel;

    private static final BakedModel SENTINEL = new BakedModel() {

        @Override
        public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction direction, RandomSource random) {
            return null;
        }

        @Override
        public boolean useAmbientOcclusion() {
            return false;
        }

        @Override
        public boolean isGui3d() {
            return false;
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
            return null;
        }

        @Override
        public ItemTransforms getTransforms() {
            return null;
        }

        @Override
        public ItemOverrides getOverrides() {
            return null;
        }
    };

    public DynamicBakedModelProvider(ModelBakery bakery, Map<ModelBakery.BakedCacheKey, BakedModel> cache) {
        this.bakery = bakery;
        this.bakedCache = cache;
        this.permanentOverrides = Collections.synchronizedMap(new Object2ObjectOpenHashMap());
        if (currentInstance == null) {
            currentInstance = this;
        }
    }

    private static ModelBakery.BakedCacheKey vanillaKey(Object o) {
        return new ModelBakery.BakedCacheKey((ResourceLocation) o, BlockModelRotation.X0_Y0.getRotation(), false);
    }

    public int size() {
        return this.bakedCache.size();
    }

    public boolean isEmpty() {
        return this.bakedCache.isEmpty();
    }

    public boolean containsKey(Object o) {
        return this.permanentOverrides.getOrDefault(o, SENTINEL) != null;
    }

    public boolean containsValue(Object o) {
        return this.permanentOverrides.containsValue(o) || this.bakedCache.containsValue(o);
    }

    private static boolean isVanillaTopLevelModel(ResourceLocation location) {
        if (location instanceof ModelResourceLocation mrl) {
            try {
                ResourceLocation registryKey = new ResourceLocation(mrl.m_135827_(), mrl.m_135815_());
                if (mrl.getVariant().equals("inventory") && BuiltInRegistries.ITEM.m_7804_(registryKey)) {
                    return true;
                }
                Optional<Block> blockOpt = BuiltInRegistries.BLOCK.m_6612_(registryKey);
                if (blockOpt.isPresent()) {
                    return ModelBakeryHelpers.getBlockStatesForMRL(((Block) blockOpt.get()).getStateDefinition(), mrl).size() > 0;
                }
            } catch (RuntimeException var4) {
            }
        }
        return location.getNamespace().equals("minecraft") && location.getPath().equals("builtin/missing");
    }

    private BakedModel getMissingModel() {
        BakedModel m = this.missingModel;
        if (m == null) {
            m = this.missingModel = ((IExtendedModelBakery) this.bakery).bakeDefault(ModelBakery.MISSING_MODEL_LOCATION, BlockModelRotation.X0_Y0);
        }
        return m;
    }

    public BakedModel get(Object o) {
        BakedModel model = (BakedModel) this.permanentOverrides.getOrDefault(o, SENTINEL);
        if (model != SENTINEL) {
            return model;
        } else {
            try {
                if (BAKE_SKIPPED_TOPLEVEL.contains((ResourceLocation) o)) {
                    model = this.getMissingModel();
                } else {
                    model = ((IExtendedModelBakery) this.bakery).bakeDefault((ResourceLocation) o, BlockModelRotation.X0_Y0);
                }
            } catch (RuntimeException var4) {
                ModernFix.LOGGER.error("Exception baking {}: {}", o, var4);
                model = this.getMissingModel();
            }
            if (model == this.getMissingModel()) {
                model = isVanillaTopLevelModel((ResourceLocation) o) ? model : null;
                this.permanentOverrides.put((ResourceLocation) o, model);
            }
            return model;
        }
    }

    public BakedModel put(ResourceLocation resourceLocation, BakedModel bakedModel) {
        BakedModel m = (BakedModel) this.permanentOverrides.put(resourceLocation, bakedModel);
        return m != null ? m : (BakedModel) this.bakedCache.get(vanillaKey(resourceLocation));
    }

    public BakedModel remove(Object o) {
        BakedModel m = (BakedModel) this.permanentOverrides.remove(o);
        return m != null ? m : (BakedModel) this.bakedCache.remove(vanillaKey(o));
    }

    public void putAll(@NotNull Map<? extends ResourceLocation, ? extends BakedModel> map) {
        this.permanentOverrides.putAll(map);
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<ResourceLocation> keySet() {
        return (Set<ResourceLocation>) this.bakedCache.keySet().stream().map(ModelBakery.BakedCacheKey::f_243934_).collect(Collectors.toSet());
    }

    @NotNull
    public Collection<BakedModel> values() {
        return this.bakedCache.values();
    }

    @NotNull
    public Set<Entry<ResourceLocation, BakedModel>> entrySet() {
        return (Set<Entry<ResourceLocation, BakedModel>>) this.bakedCache.entrySet().stream().map(entry -> new SimpleImmutableEntry(((ModelBakery.BakedCacheKey) entry.getKey()).id(), (BakedModel) entry.getValue())).collect(Collectors.toSet());
    }

    @Nullable
    public BakedModel replace(ResourceLocation key, BakedModel value) {
        BakedModel existingOverride = (BakedModel) this.permanentOverrides.get(key);
        return existingOverride == null ? this.put(key, value) : existingOverride;
    }

    public void replaceAll(BiFunction<? super ResourceLocation, ? super BakedModel, ? extends BakedModel> function) {
        Set<ResourceLocation> overridenLocations = this.permanentOverrides.keySet();
        this.permanentOverrides.replaceAll(function);
        boolean uvLock = BlockModelRotation.X0_Y0.m_7538_();
        Transformation rotation = BlockModelRotation.X0_Y0.getRotation();
        this.bakedCache.replaceAll((loc, oldModel) -> loc.transformation() == rotation && loc.isUvLocked() == uvLock && !overridenLocations.contains(loc.id()) ? (BakedModel) function.apply(loc.id(), oldModel) : oldModel);
    }
}