package net.blay09.mods.balm.forge.client.rendering;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.math.Transformation;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;
import net.blay09.mods.balm.api.DeferredObject;
import net.blay09.mods.balm.api.client.rendering.BalmModels;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.model.SimpleModelState;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.slf4j.Logger;

public class ForgeBalmModels implements BalmModels {

    private static final Logger LOGGER = LogUtils.getLogger();

    public final List<ForgeBalmModels.DeferredModel> modelsToBake = Collections.synchronizedList(new ArrayList());

    private final Map<String, ForgeBalmModels.Registrations> registrations = new ConcurrentHashMap();

    private ModelBakery modelBakery;

    public void onBakeModels(ModelBakery modelBakery, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        this.modelBakery = modelBakery;
        this.registrations.values().forEach(it -> it.setSpriteBiFunction(spriteBiFunction));
        synchronized (this.modelsToBake) {
            for (ForgeBalmModels.DeferredModel deferredModel : this.modelsToBake) {
                deferredModel.resolveAndSet(modelBakery, modelBakery.getBakedTopLevelModels(), spriteBiFunction);
            }
        }
    }

    @Override
    public DeferredObject<BakedModel> loadModel(ResourceLocation identifier) {
        ForgeBalmModels.DeferredModel deferredModel = new ForgeBalmModels.DeferredModel(identifier) {

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                return (BakedModel) modelRegistry.get(this.getIdentifier());
            }
        };
        this.getActiveRegistrations().additionalModels.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> bakeModel(final ResourceLocation identifier, final UnbakedModel model) {
        ForgeBalmModels.DeferredModel deferredModel = new ForgeBalmModels.DeferredModel(identifier) {

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                ModelBaker baker = ForgeBalmModels.this.createBaker(identifier, spriteBiFunction);
                return model.bake(baker, baker.getModelTextureGetter(), ForgeBalmModels.this.getModelState(Transformation.identity()), identifier);
            }
        };
        this.modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> retexture(final ResourceLocation identifier, final Map<String, String> textureMap) {
        ForgeBalmModels.DeferredModel deferredModel = new ForgeBalmModels.DeferredModel(identifier) {

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                UnbakedModel model = ForgeBalmModels.this.retexture(bakery, identifier, textureMap);
                ModelBaker baker = ForgeBalmModels.this.createBaker(identifier, spriteBiFunction);
                return model.bake(baker, baker.getModelTextureGetter(), ForgeBalmModels.this.getModelState(Transformation.identity()), identifier);
            }
        };
        this.modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public DeferredObject<BakedModel> loadDynamicModel(final ResourceLocation identifier, @Nullable Function<BlockState, ResourceLocation> modelFunction, @Nullable final Function<BlockState, Map<String, String>> textureMapFunction, @Nullable final BiConsumer<BlockState, Matrix4f> transformFunction, final List<RenderType> renderTypes) {
        final Function<BlockState, ResourceLocation> effectiveModelFunction = modelFunction != null ? modelFunction : it -> identifier;
        ForgeBalmModels.DeferredModel deferredModel = new ForgeBalmModels.DeferredModel(identifier) {

            @Override
            public BakedModel resolve(ModelBakery bakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
                return new ForgeCachedDynamicModel(bakery, effectiveModelFunction, null, textureMapFunction, transformFunction, renderTypes, identifier);
            }
        };
        this.modelsToBake.add(deferredModel);
        return deferredModel;
    }

    @Override
    public void overrideModel(Supplier<Block> block, Supplier<BakedModel> model) {
        this.getActiveRegistrations().overrides.add(Pair.of(block, model));
    }

    @Override
    public ModelState getModelState(Transformation transformation) {
        return new SimpleModelState(transformation);
    }

    @Override
    public UnbakedModel getUnbakedModelOrMissing(ResourceLocation location) {
        return this.modelBakery.getModel(location);
    }

    @Override
    public UnbakedModel getUnbakedMissingModel() {
        return this.modelBakery.getModel(ModelBakery.MISSING_MODEL_LOCATION);
    }

    public void register() {
        FMLJavaModLoadingContext.get().getModEventBus().register(this.getActiveRegistrations());
    }

    private ForgeBalmModels.Registrations getActiveRegistrations() {
        return (ForgeBalmModels.Registrations) this.registrations.computeIfAbsent(ModLoadingContext.get().getActiveNamespace(), it -> new ForgeBalmModels.Registrations());
    }

    @Override
    public ModelBaker createBaker(ResourceLocation location, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
        try {
            Class<?> clazz = Class.forName("net.minecraft.client.resources.model.ModelBakery$ModelBakerImpl");
            Constructor<?> constructor = clazz.getDeclaredConstructor(ModelBakery.class, BiFunction.class, ResourceLocation.class);
            constructor.setAccessible(true);
            return (ModelBaker) constructor.newInstance(this.modelBakery, spriteBiFunction, location);
        } catch (NoSuchMethodException | InstantiationException | IllegalAccessException | InvocationTargetException | ClassNotFoundException var5) {
            throw new RuntimeException("Balm failed to create model baker", var5);
        }
    }

    private abstract static class DeferredModel extends DeferredObject<BakedModel> {

        public DeferredModel(ResourceLocation identifier) {
            super(identifier);
        }

        public void resolveAndSet(ModelBakery modelBakery, Map<ResourceLocation, BakedModel> modelRegistry, BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
            try {
                this.set(this.resolve(modelBakery, modelRegistry, spriteBiFunction));
            } catch (Exception var5) {
                ForgeBalmModels.LOGGER.warn("Unable to bake model: '{}':", this.getIdentifier(), var5);
                this.set((BakedModel) modelBakery.getBakedTopLevelModels().get(ModelBakery.MISSING_MODEL_LOCATION));
            }
        }

        public abstract BakedModel resolve(ModelBakery var1, Map<ResourceLocation, BakedModel> var2, BiFunction<ResourceLocation, Material, TextureAtlasSprite> var3);
    }

    private static class Registrations {

        public final List<ForgeBalmModels.DeferredModel> additionalModels = new ArrayList();

        public final List<Pair<Supplier<Block>, Supplier<BakedModel>>> overrides = new ArrayList();

        private BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction;

        public void setSpriteBiFunction(BiFunction<ResourceLocation, Material, TextureAtlasSprite> spriteBiFunction) {
            this.spriteBiFunction = spriteBiFunction;
        }

        @SubscribeEvent
        public void onRegisterAdditionalModels(ModelEvent.RegisterAdditional event) {
            this.additionalModels.forEach(it -> event.register(it.getIdentifier()));
        }

        @SubscribeEvent
        public void onModelBakingCompleted(ModelEvent.ModifyBakingResult event) {
            for (Pair<Supplier<Block>, Supplier<BakedModel>> override : this.overrides) {
                Block block = (Block) ((Supplier) override.getFirst()).get();
                BakedModel bakedModel = (BakedModel) ((Supplier) override.getSecond()).get();
                block.getStateDefinition().getPossibleStates().forEach(state -> {
                    ModelResourceLocation modelLocation = BlockModelShaper.stateToModelLocation(state);
                    event.getModels().put(modelLocation, bakedModel);
                });
            }
        }

        @SubscribeEvent
        public void onModelBakingCompleted(ModelEvent.BakingCompleted event) {
            for (ForgeBalmModels.DeferredModel deferredModel : this.additionalModels) {
                deferredModel.resolveAndSet(event.getModelBakery(), event.getModels(), this.spriteBiFunction);
            }
            this.spriteBiFunction = null;
        }
    }
}