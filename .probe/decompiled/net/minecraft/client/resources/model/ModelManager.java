package net.minecraft.client.resources.model;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.gson.JsonObject;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import java.io.Reader;
import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.slf4j.Logger;

public class ModelManager implements PreparableReloadListener, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final Map<ResourceLocation, ResourceLocation> VANILLA_ATLASES = Map.of(Sheets.BANNER_SHEET, new ResourceLocation("banner_patterns"), Sheets.BED_SHEET, new ResourceLocation("beds"), Sheets.CHEST_SHEET, new ResourceLocation("chests"), Sheets.SHIELD_SHEET, new ResourceLocation("shield_patterns"), Sheets.SIGN_SHEET, new ResourceLocation("signs"), Sheets.SHULKER_SHEET, new ResourceLocation("shulker_boxes"), Sheets.ARMOR_TRIMS_SHEET, new ResourceLocation("armor_trims"), Sheets.DECORATED_POT_SHEET, new ResourceLocation("decorated_pot"), TextureAtlas.LOCATION_BLOCKS, new ResourceLocation("blocks"));

    private Map<ResourceLocation, BakedModel> bakedRegistry;

    private final AtlasSet atlases;

    private final BlockModelShaper blockModelShaper;

    private final BlockColors blockColors;

    private int maxMipmapLevels;

    private BakedModel missingModel;

    private Object2IntMap<BlockState> modelGroups;

    public ModelManager(TextureManager textureManager0, BlockColors blockColors1, int int2) {
        this.blockColors = blockColors1;
        this.maxMipmapLevels = int2;
        this.blockModelShaper = new BlockModelShaper(this);
        this.atlases = new AtlasSet(VANILLA_ATLASES, textureManager0);
    }

    public BakedModel getModel(ModelResourceLocation modelResourceLocation0) {
        return (BakedModel) this.bakedRegistry.getOrDefault(modelResourceLocation0, this.missingModel);
    }

    public BakedModel getMissingModel() {
        return this.missingModel;
    }

    public BlockModelShaper getBlockModelShaper() {
        return this.blockModelShaper;
    }

    @Override
    public final CompletableFuture<Void> reload(PreparableReloadListener.PreparationBarrier preparableReloadListenerPreparationBarrier0, ResourceManager resourceManager1, ProfilerFiller profilerFiller2, ProfilerFiller profilerFiller3, Executor executor4, Executor executor5) {
        profilerFiller2.startTick();
        CompletableFuture<Map<ResourceLocation, BlockModel>> $$6 = loadBlockModels(resourceManager1, executor4);
        CompletableFuture<Map<ResourceLocation, List<ModelBakery.LoadedJson>>> $$7 = loadBlockStates(resourceManager1, executor4);
        CompletableFuture<ModelBakery> $$8 = $$6.thenCombineAsync($$7, (p_251201_, p_251281_) -> new ModelBakery(this.blockColors, profilerFiller2, p_251201_, p_251281_), executor4);
        Map<ResourceLocation, CompletableFuture<AtlasSet.StitchResult>> $$9 = this.atlases.scheduleLoad(resourceManager1, this.maxMipmapLevels, executor4);
        return CompletableFuture.allOf((CompletableFuture[]) Stream.concat($$9.values().stream(), Stream.of($$8)).toArray(CompletableFuture[]::new)).thenApplyAsync(p_248624_ -> this.loadModels(profilerFiller2, (Map<ResourceLocation, AtlasSet.StitchResult>) $$9.entrySet().stream().collect(Collectors.toMap(Entry::getKey, p_248988_ -> (AtlasSet.StitchResult) ((CompletableFuture) p_248988_.getValue()).join())), (ModelBakery) $$8.join()), executor4).thenCompose(p_252255_ -> p_252255_.readyForUpload.thenApply(p_251581_ -> p_252255_)).thenCompose(preparableReloadListenerPreparationBarrier0::m_6769_).thenAcceptAsync(p_252252_ -> this.apply(p_252252_, profilerFiller3), executor5);
    }

    private static CompletableFuture<Map<ResourceLocation, BlockModel>> loadBlockModels(ResourceManager resourceManager0, Executor executor1) {
        return CompletableFuture.supplyAsync(() -> ModelBakery.MODEL_LISTER.listMatchingResources(resourceManager0), executor1).thenCompose(p_250597_ -> {
            List<CompletableFuture<Pair<ResourceLocation, BlockModel>>> $$2 = new ArrayList(p_250597_.size());
            for (Entry<ResourceLocation, Resource> $$3 : p_250597_.entrySet()) {
                $$2.add(CompletableFuture.supplyAsync(() -> {
                    try {
                        Reader $$1 = ((Resource) $$3.getValue()).openAsReader();
                        Pair var2x;
                        try {
                            var2x = Pair.of((ResourceLocation) $$3.getKey(), BlockModel.fromStream($$1));
                        } catch (Throwable var5) {
                            if ($$1 != null) {
                                try {
                                    $$1.close();
                                } catch (Throwable var4x) {
                                    var5.addSuppressed(var4x);
                                }
                            }
                            throw var5;
                        }
                        if ($$1 != null) {
                            $$1.close();
                        }
                        return var2x;
                    } catch (Exception var6) {
                        LOGGER.error("Failed to load model {}", $$3.getKey(), var6);
                        return null;
                    }
                }, executor1));
            }
            return Util.sequence($$2).thenApply(p_250813_ -> (Map) p_250813_.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond)));
        });
    }

    private static CompletableFuture<Map<ResourceLocation, List<ModelBakery.LoadedJson>>> loadBlockStates(ResourceManager resourceManager0, Executor executor1) {
        return CompletableFuture.supplyAsync(() -> ModelBakery.BLOCKSTATE_LISTER.listMatchingResourceStacks(resourceManager0), executor1).thenCompose(p_250744_ -> {
            List<CompletableFuture<Pair<ResourceLocation, List<ModelBakery.LoadedJson>>>> $$2 = new ArrayList(p_250744_.size());
            for (Entry<ResourceLocation, List<Resource>> $$3 : p_250744_.entrySet()) {
                $$2.add(CompletableFuture.supplyAsync(() -> {
                    List<Resource> $$1 = (List<Resource>) $$3.getValue();
                    List<ModelBakery.LoadedJson> $$2x = new ArrayList($$1.size());
                    for (Resource $$3x : $$1) {
                        try {
                            Reader $$4 = $$3x.openAsReader();
                            try {
                                JsonObject $$5 = GsonHelper.parse($$4);
                                $$2x.add(new ModelBakery.LoadedJson($$3x.sourcePackId(), $$5));
                            } catch (Throwable var9) {
                                if ($$4 != null) {
                                    try {
                                        $$4.close();
                                    } catch (Throwable var8) {
                                        var9.addSuppressed(var8);
                                    }
                                }
                                throw var9;
                            }
                            if ($$4 != null) {
                                $$4.close();
                            }
                        } catch (Exception var10) {
                            LOGGER.error("Failed to load blockstate {} from pack {}", new Object[] { $$3.getKey(), $$3x.sourcePackId(), var10 });
                        }
                    }
                    return Pair.of((ResourceLocation) $$3.getKey(), $$2x);
                }, executor1));
            }
            return Util.sequence($$2).thenApply(p_248966_ -> (Map) p_248966_.stream().filter(Objects::nonNull).collect(Collectors.toUnmodifiableMap(Pair::getFirst, Pair::getSecond)));
        });
    }

    private ModelManager.ReloadState loadModels(ProfilerFiller profilerFiller0, Map<ResourceLocation, AtlasSet.StitchResult> mapResourceLocationAtlasSetStitchResult1, ModelBakery modelBakery2) {
        profilerFiller0.push("load");
        profilerFiller0.popPush("baking");
        Multimap<ResourceLocation, Material> $$3 = HashMultimap.create();
        modelBakery2.bakeModels((p_251469_, p_251262_) -> {
            AtlasSet.StitchResult $$4x = (AtlasSet.StitchResult) mapResourceLocationAtlasSetStitchResult1.get(p_251262_.atlasLocation());
            TextureAtlasSprite $$5x = $$4x.getSprite(p_251262_.texture());
            if ($$5x != null) {
                return $$5x;
            } else {
                $$3.put(p_251469_, p_251262_);
                return $$4x.missing();
            }
        });
        $$3.asMap().forEach((p_250493_, p_252017_) -> LOGGER.warn("Missing textures in model {}:\n{}", p_250493_, p_252017_.stream().sorted(Material.COMPARATOR).map(p_248692_ -> "    " + p_248692_.atlasLocation() + ":" + p_248692_.texture()).collect(Collectors.joining("\n"))));
        profilerFiller0.popPush("dispatch");
        Map<ResourceLocation, BakedModel> $$4 = modelBakery2.getBakedTopLevelModels();
        BakedModel $$5 = (BakedModel) $$4.get(ModelBakery.MISSING_MODEL_LOCATION);
        Map<BlockState, BakedModel> $$6 = new IdentityHashMap();
        for (Block $$7 : BuiltInRegistries.BLOCK) {
            $$7.getStateDefinition().getPossibleStates().forEach(p_250633_ -> {
                ResourceLocation $$4x = p_250633_.m_60734_().builtInRegistryHolder().key().location();
                BakedModel $$5x = (BakedModel) $$4.getOrDefault(BlockModelShaper.stateToModelLocation($$4x, p_250633_), $$5);
                $$6.put(p_250633_, $$5x);
            });
        }
        CompletableFuture<Void> $$8 = CompletableFuture.allOf((CompletableFuture[]) mapResourceLocationAtlasSetStitchResult1.values().stream().map(AtlasSet.StitchResult::m_246362_).toArray(CompletableFuture[]::new));
        profilerFiller0.pop();
        profilerFiller0.endTick();
        return new ModelManager.ReloadState(modelBakery2, $$5, $$6, mapResourceLocationAtlasSetStitchResult1, $$8);
    }

    private void apply(ModelManager.ReloadState modelManagerReloadState0, ProfilerFiller profilerFiller1) {
        profilerFiller1.startTick();
        profilerFiller1.push("upload");
        modelManagerReloadState0.atlasPreparations.values().forEach(AtlasSet.StitchResult::m_246239_);
        ModelBakery $$2 = modelManagerReloadState0.modelBakery;
        this.bakedRegistry = $$2.getBakedTopLevelModels();
        this.modelGroups = $$2.getModelGroups();
        this.missingModel = modelManagerReloadState0.missingModel;
        profilerFiller1.popPush("cache");
        this.blockModelShaper.replaceCache(modelManagerReloadState0.modelCache);
        profilerFiller1.pop();
        profilerFiller1.endTick();
    }

    public boolean requiresRender(BlockState blockState0, BlockState blockState1) {
        if (blockState0 == blockState1) {
            return false;
        } else {
            int $$2 = this.modelGroups.getInt(blockState0);
            if ($$2 != -1) {
                int $$3 = this.modelGroups.getInt(blockState1);
                if ($$2 == $$3) {
                    FluidState $$4 = blockState0.m_60819_();
                    FluidState $$5 = blockState1.m_60819_();
                    return $$4 != $$5;
                }
            }
            return true;
        }
    }

    public TextureAtlas getAtlas(ResourceLocation resourceLocation0) {
        return this.atlases.getAtlas(resourceLocation0);
    }

    public void close() {
        this.atlases.close();
    }

    public void updateMaxMipLevel(int int0) {
        this.maxMipmapLevels = int0;
    }

    static record ReloadState(ModelBakery f_244394_, BakedModel f_244619_, Map<BlockState, BakedModel> f_244561_, Map<ResourceLocation, AtlasSet.StitchResult> f_244177_, CompletableFuture<Void> f_244037_) {

        private final ModelBakery modelBakery;

        private final BakedModel missingModel;

        private final Map<BlockState, BakedModel> modelCache;

        private final Map<ResourceLocation, AtlasSet.StitchResult> atlasPreparations;

        private final CompletableFuture<Void> readyForUpload;

        ReloadState(ModelBakery f_244394_, BakedModel f_244619_, Map<BlockState, BakedModel> f_244561_, Map<ResourceLocation, AtlasSet.StitchResult> f_244177_, CompletableFuture<Void> f_244037_) {
            this.modelBakery = f_244394_;
            this.missingModel = f_244619_;
            this.modelCache = f_244561_;
            this.atlasPreparations = f_244177_;
            this.readyForUpload = f_244037_;
        }
    }
}