package fuzs.puzzleslib.impl.client.core;

import com.google.common.collect.Lists;
import fuzs.puzzleslib.api.client.core.v1.ClientModConstructor;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicBakingCompletedContext;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicModifyBakingResultContext;
import fuzs.puzzleslib.api.client.particle.v1.ClientParticleTypes;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.ModContainerHelper;
import fuzs.puzzleslib.api.core.v1.resources.ForwardingReloadListenerHelper;
import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.client.core.context.AdditionalModelsContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.BlockColorProvidersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.BlockEntityRenderersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.BlockRenderTypesContextImpl;
import fuzs.puzzleslib.impl.client.core.context.BuiltinModelItemRendererContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.ClientTooltipComponentsContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.CoreShadersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.DynamicBakingCompletedContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.EntityRenderersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.EntitySpectatorShaderContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.FluidRenderTypesContextImpl;
import fuzs.puzzleslib.impl.client.core.context.ItemColorProvidersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.ItemDecorationContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.ItemModelPropertiesContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.KeyMappingsContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.LayerDefinitionsContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.LivingEntityRenderLayersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.ParticleProvidersContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.ResourcePackSourcesContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.SearchRegistryContextForgeImpl;
import fuzs.puzzleslib.impl.client.core.context.SkullRenderersContextForgeImpl;
import fuzs.puzzleslib.impl.client.particle.ClientParticleTypesImpl;
import fuzs.puzzleslib.impl.client.particle.ClientParticleTypesManager;
import fuzs.puzzleslib.impl.core.context.AddReloadListenersContextForgeImpl;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.eventbus.api.IEventBus;

public final class ForgeClientModConstructor {

    private ForgeClientModConstructor() {
    }

    public static void construct(ClientModConstructor constructor, String modId, Set<ContentRegistrationFlags> availableFlags, Set<ContentRegistrationFlags> flagsToHandle) {
        ModContainerHelper.getOptionalModEventBus(modId).ifPresent(eventBus -> {
            registerModHandlers(constructor, modId, eventBus, availableFlags, flagsToHandle);
            constructor.onConstructMod();
        });
    }

    private static void registerModHandlers(ClientModConstructor constructor, String modId, IEventBus eventBus, Set<ContentRegistrationFlags> availableFlags, Set<ContentRegistrationFlags> flagsToHandle) {
        List<ResourceManagerReloadListener> dynamicRenderers = Lists.newArrayList();
        eventBus.addListener(evt -> evt.enqueueWork(() -> {
            constructor.onClientSetup();
            constructor.onRegisterSearchTrees(new SearchRegistryContextForgeImpl());
            constructor.onRegisterItemModelProperties(new ItemModelPropertiesContextForgeImpl());
            constructor.onRegisterBuiltinModelItemRenderers(new BuiltinModelItemRendererContextForgeImpl(modId, dynamicRenderers));
            constructor.onRegisterBlockRenderTypes(new BlockRenderTypesContextImpl());
            constructor.onRegisterFluidRenderTypes(new FluidRenderTypesContextImpl());
        }));
        eventBus.addListener(evt -> {
            constructor.onRegisterEntityRenderers(new EntityRenderersContextForgeImpl(evt::registerEntityRenderer));
            constructor.onRegisterBlockEntityRenderers(new BlockEntityRenderersContextForgeImpl(evt::registerBlockEntityRenderer));
        });
        eventBus.addListener(evt -> constructor.onRegisterClientTooltipComponents(new ClientTooltipComponentsContextForgeImpl(evt::register)));
        eventBus.addListener(evt -> constructor.onRegisterParticleProviders(new ParticleProvidersContextForgeImpl(evt)));
        eventBus.addListener(evt -> constructor.onRegisterLayerDefinitions(new LayerDefinitionsContextForgeImpl(evt::registerLayerDefinition)));
        eventBus.addListener(evt -> onModifyBakingResult(constructor::onModifyBakingResult, modId, evt.getModels(), evt.getModelBakery()));
        eventBus.addListener(evt -> onBakingCompleted(constructor::onBakingCompleted, modId, evt.getModelManager(), evt.getModels(), evt.getModelBakery()));
        eventBus.addListener(evt -> constructor.onRegisterAdditionalModels(new AdditionalModelsContextForgeImpl(evt::register)));
        eventBus.addListener(evt -> constructor.onRegisterItemDecorations(new ItemDecorationContextForgeImpl(evt::register)));
        eventBus.addListener(evt -> constructor.onRegisterEntitySpectatorShaders(new EntitySpectatorShaderContextForgeImpl(evt::register)));
        eventBus.addListener(evt -> constructor.onRegisterSkullRenderers(new SkullRenderersContextForgeImpl(evt.getEntityModelSet(), evt::registerSkullModel)));
        eventBus.addListener(evt -> {
            constructor.onRegisterResourcePackReloadListeners(new AddReloadListenersContextForgeImpl(modId, evt::registerReloadListener));
            if (availableFlags.contains(ContentRegistrationFlags.DYNAMIC_RENDERERS)) {
                evt.registerReloadListener(ForwardingReloadListenerHelper.fromResourceManagerReloadListeners(new ResourceLocation(modId, "built_in_model_item_renderers"), dynamicRenderers));
            }
            if (flagsToHandle.contains(ContentRegistrationFlags.CLIENT_PARTICLE_TYPES)) {
                ClientParticleTypesManager particleTypesManager = ((ClientParticleTypesImpl) ClientParticleTypes.INSTANCE).getParticleTypesManager(modId);
                evt.registerReloadListener(ForwardingReloadListenerHelper.fromReloadListener(new ResourceLocation(modId, "client_particle_types"), particleTypesManager));
            }
        });
        eventBus.addListener(evt -> constructor.onRegisterLivingEntityRenderLayers(new LivingEntityRenderLayersContextForgeImpl(evt)));
        eventBus.addListener(evt -> constructor.onRegisterKeyMappings(new KeyMappingsContextForgeImpl(evt::register)));
        eventBus.addListener(evt -> constructor.onRegisterBlockColorProviders(new BlockColorProvidersContextForgeImpl((x$0, xva$1) -> evt.register(x$0, xva$1), evt.getBlockColors())));
        eventBus.addListener(evt -> constructor.onRegisterItemColorProviders(new ItemColorProvidersContextForgeImpl((x$0, xva$1) -> evt.register(x$0, xva$1), evt.getItemColors())));
        eventBus.addListener(evt -> {
            if (evt.getPackType() == PackType.CLIENT_RESOURCES) {
                constructor.onAddResourcePackFinders(new ResourcePackSourcesContextForgeImpl(evt::addRepositorySource));
            }
        });
        eventBus.addListener(evt -> constructor.onRegisterCoreShaders(new CoreShadersContextForgeImpl(evt::registerShader, evt.getResourceProvider())));
    }

    private static void onBakingCompleted(Consumer<DynamicBakingCompletedContext> consumer, String modId, ModelManager modelManager, Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery) {
        try {
            consumer.accept(new DynamicBakingCompletedContextForgeImpl(modelManager, models, modelBakery));
        } catch (Exception var6) {
            PuzzlesLib.LOGGER.error("Unable to execute additional resource pack model processing during baking completed phase provided by {}", modId, var6);
        }
    }

    private static void onModifyBakingResult(Consumer<DynamicModifyBakingResultContext> consumer, String modId, Map<ResourceLocation, BakedModel> models, ModelBakery modelBakery) {
        try {
            consumer.accept(new DynamicModifyBakingResultContextImpl(models, modelBakery));
        } catch (Exception var5) {
            PuzzlesLib.LOGGER.error("Unable to execute additional resource pack model processing during modify baking result phase provided by {}", modId, var5);
        }
    }
}