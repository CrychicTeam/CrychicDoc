package fuzs.puzzleslib.api.client.core.v1;

import fuzs.puzzleslib.api.client.core.v1.context.AdditionalModelsContext;
import fuzs.puzzleslib.api.client.core.v1.context.BlockEntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.BuiltinModelItemRendererContext;
import fuzs.puzzleslib.api.client.core.v1.context.ClientTooltipComponentsContext;
import fuzs.puzzleslib.api.client.core.v1.context.ColorProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.CoreShadersContext;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicBakingCompletedContext;
import fuzs.puzzleslib.api.client.core.v1.context.DynamicModifyBakingResultContext;
import fuzs.puzzleslib.api.client.core.v1.context.EntityRenderersContext;
import fuzs.puzzleslib.api.client.core.v1.context.EntitySpectatorShaderContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemDecorationContext;
import fuzs.puzzleslib.api.client.core.v1.context.ItemModelPropertiesContext;
import fuzs.puzzleslib.api.client.core.v1.context.KeyMappingsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LayerDefinitionsContext;
import fuzs.puzzleslib.api.client.core.v1.context.LivingEntityRenderLayersContext;
import fuzs.puzzleslib.api.client.core.v1.context.ParticleProvidersContext;
import fuzs.puzzleslib.api.client.core.v1.context.RenderTypesContext;
import fuzs.puzzleslib.api.client.core.v1.context.SearchRegistryContext;
import fuzs.puzzleslib.api.client.core.v1.context.SkullRenderersContext;
import fuzs.puzzleslib.api.core.v1.BaseModConstructor;
import fuzs.puzzleslib.api.core.v1.ContentRegistrationFlags;
import fuzs.puzzleslib.api.core.v1.context.AddReloadListenersContext;
import fuzs.puzzleslib.api.core.v1.context.ModLifecycleContext;
import fuzs.puzzleslib.api.core.v1.context.PackRepositorySourcesContext;
import fuzs.puzzleslib.impl.PuzzlesLib;
import fuzs.puzzleslib.impl.client.core.ClientFactories;
import fuzs.puzzleslib.impl.core.ModContext;
import java.util.Set;
import java.util.function.Supplier;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;
import org.apache.logging.log4j.util.Strings;

public interface ClientModConstructor extends BaseModConstructor {

    static void construct(String modId, Supplier<ClientModConstructor> modConstructor) {
        construct(modId, modConstructor);
    }

    @Deprecated(forRemoval = true)
    static void construct(String modId, Supplier<ClientModConstructor> supplier, ContentRegistrationFlags... flags) {
        if (Strings.isBlank(modId)) {
            throw new IllegalArgumentException("mod id is empty");
        } else {
            ClientModConstructor instance = (ClientModConstructor) supplier.get();
            ModContext modContext = ModContext.get(modId);
            ResourceLocation identifier = ModContext.getPairingIdentifier(modId, instance);
            modContext.scheduleClientModConstruction(identifier, () -> {
                PuzzlesLib.LOGGER.info("Constructing client components for {}", identifier);
                ContentRegistrationFlags[] builtInFlags = instance.getContentRegistrationFlags();
                Set<ContentRegistrationFlags> availableFlags = Set.of(builtInFlags.length != 0 ? builtInFlags : flags);
                Set<ContentRegistrationFlags> flagsToHandle = modContext.getFlagsToHandle(availableFlags);
                ClientFactories.INSTANCE.constructClientMod(modId, instance, availableFlags, flagsToHandle);
            });
        }
    }

    default void onConstructMod() {
    }

    @Deprecated(forRemoval = true)
    default void onClientSetup(ModLifecycleContext context) {
    }

    default void onClientSetup() {
        this.onClientSetup(Runnable::run);
    }

    default void onRegisterEntityRenderers(EntityRenderersContext context) {
    }

    default void onRegisterBlockEntityRenderers(BlockEntityRenderersContext context) {
    }

    default void onRegisterClientTooltipComponents(ClientTooltipComponentsContext context) {
    }

    default void onRegisterParticleProviders(ParticleProvidersContext context) {
    }

    default void onRegisterLayerDefinitions(LayerDefinitionsContext context) {
    }

    @Deprecated(forRemoval = true)
    default void onRegisterSearchTrees(SearchRegistryContext context) {
    }

    @Deprecated(forRemoval = true)
    default void onModifyBakingResult(DynamicModifyBakingResultContext context) {
    }

    @Deprecated(forRemoval = true)
    default void onBakingCompleted(DynamicBakingCompletedContext context) {
    }

    default void onRegisterAdditionalModels(AdditionalModelsContext context) {
    }

    default void onRegisterItemModelProperties(ItemModelPropertiesContext context) {
    }

    default void onRegisterBuiltinModelItemRenderers(BuiltinModelItemRendererContext context) {
    }

    default void onRegisterItemDecorations(ItemDecorationContext context) {
    }

    default void onRegisterEntitySpectatorShaders(EntitySpectatorShaderContext context) {
    }

    default void onRegisterSkullRenderers(SkullRenderersContext context) {
    }

    default void onRegisterResourcePackReloadListeners(AddReloadListenersContext context) {
    }

    default void onRegisterLivingEntityRenderLayers(LivingEntityRenderLayersContext context) {
    }

    default void onRegisterKeyMappings(KeyMappingsContext context) {
    }

    default void onRegisterBlockRenderTypes(RenderTypesContext<Block> context) {
    }

    default void onRegisterFluidRenderTypes(RenderTypesContext<Fluid> context) {
    }

    default void onRegisterBlockColorProviders(ColorProvidersContext<Block, BlockColor> context) {
    }

    default void onRegisterItemColorProviders(ColorProvidersContext<Item, ItemColor> context) {
    }

    default void onAddResourcePackFinders(PackRepositorySourcesContext context) {
    }

    default void onRegisterCoreShaders(CoreShadersContext context) {
    }
}