package net.mehvahdjukaar.moonlight.api.platform.forge;

import com.google.gson.JsonElement;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.core.Moonlight;
import net.minecraft.client.color.block.BlockColor;
import net.minecraft.client.color.item.ItemColor;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelManager;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.IItemDecorator;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterItemDecorationsEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.model.ExtendedBlockModelDeserializer;
import net.minecraftforge.client.model.geometry.IGeometryLoader;
import net.minecraftforge.data.loading.DatagenModLoader;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.resource.PathPackResources;
import org.jetbrains.annotations.Nullable;

public class ClientHelperImpl {

    public static void registerRenderType(Block block, RenderType... types) {
        if (types.length == 1) {
            ItemBlockRenderTypes.setRenderLayer(block, types[0]);
        } else {
            List<RenderType> l = List.of(types);
            ItemBlockRenderTypes.setRenderLayer(block, l::contains);
        }
    }

    public static void registerFluidRenderType(Fluid fluid, RenderType type) {
        ItemBlockRenderTypes.setRenderLayer(fluid, type);
    }

    public static void addParticleRegistration(Consumer<ClientHelper.ParticleEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterParticleProvidersEvent> eventConsumer = event -> eventListener.accept(new ClientHelperImpl.ParticleEventImpl(event));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addEntityRenderersRegistration(Consumer<ClientHelper.EntityRendererEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<EntityRenderersEvent.RegisterRenderers> eventConsumer = event -> eventListener.accept(event::registerEntityRenderer);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addBlockEntityRenderersRegistration(Consumer<ClientHelper.BlockEntityRendererEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<EntityRenderersEvent.RegisterRenderers> eventConsumer = event -> eventListener.accept(event::registerBlockEntityRenderer);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addBlockColorsRegistration(Consumer<ClientHelper.BlockColorEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterColorHandlersEvent.Block> eventConsumer = event -> eventListener.accept(new ClientHelper.BlockColorEvent() {

            @Override
            public void register(BlockColor color, Block... block) {
                event.register(color, block);
            }

            @Override
            public int getColor(BlockState block, BlockAndTintGetter level, BlockPos pos, int tint) {
                return event.getBlockColors().getColor(block, level, pos, tint);
            }
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addItemColorsRegistration(Consumer<ClientHelper.ItemColorEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterColorHandlersEvent.Item> eventConsumer = event -> eventListener.accept(new ClientHelper.ItemColorEvent() {

            @Override
            public void register(ItemColor color, ItemLike... items) {
                event.register(color, items);
            }

            @Override
            public int getColor(ItemStack stack, int tint) {
                return event.getItemColors().getColor(stack, tint);
            }
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addClientReloadListener(Supplier<PreparableReloadListener> listener, ResourceLocation location) {
        Moonlight.assertInitPhase();
        Consumer<RegisterClientReloadListenersEvent> eventConsumer = event -> event.registerReloadListener((PreparableReloadListener) listener.get());
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addModelLayerRegistration(Consumer<ClientHelper.ModelLayerEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<EntityRenderersEvent.RegisterLayerDefinitions> eventConsumer = event -> eventListener.accept(event::registerLayerDefinition);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addSpecialModelRegistration(Consumer<ClientHelper.SpecialModelEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<ModelEvent.RegisterAdditional> eventConsumer = event -> eventListener.accept(event::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addTooltipComponentRegistration(Consumer<ClientHelper.TooltipComponentEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterClientTooltipComponentFactoriesEvent> eventConsumer = event -> eventListener.accept(event::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addModelLoaderRegistration(Consumer<ClientHelper.ModelLoaderEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<ModelEvent.RegisterGeometryLoaders> eventConsumer = event -> eventListener.accept((ClientHelper.ModelLoaderEvent) (i, l) -> event.register(i.getPath(), (IGeometryLoader<?>) l));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addItemDecoratorsRegistration(Consumer<ClientHelper.ItemDecoratorEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterItemDecorationsEvent> eventConsumer = event -> eventListener.accept((ClientHelper.ItemDecoratorEvent) (i, l) -> {
            IItemDecorator deco = new IItemDecorator() {

                @Override
                public boolean render(GuiGraphics graphics, Font font, ItemStack stack, int xOffset, int yOffset) {
                    return l.render(graphics, font, stack, xOffset, yOffset);
                }
            };
            event.register(i, deco);
        });
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addKeyBindRegistration(Consumer<ClientHelper.KeyBindEvent> eventListener) {
        Moonlight.assertInitPhase();
        Consumer<RegisterKeyMappingsEvent> eventConsumer = event -> eventListener.accept(event::register);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static int getPixelRGBA(TextureAtlasSprite sprite, int frameIndex, int x, int y) {
        return sprite.getPixelRGBA(frameIndex, x, y);
    }

    public static BakedModel getModel(ModelManager modelManager, ResourceLocation modelLocation) {
        return modelManager.getModel(modelLocation);
    }

    @Nullable
    public static Path getModIcon(String modId) {
        Optional<? extends ModContainer> m = ModList.get().getModContainerById(modId);
        if (m.isPresent()) {
            IModInfo mod = ((ModContainer) m.get()).getModInfo();
            IModFile file = mod.getOwningFile().getFile();
            String logo = (String) mod.getLogoFile().orElse(null);
            if (logo != null && file != null) {
                Path logoPath = file.findResource(new String[] { logo });
                if (Files.exists(logoPath, new LinkOption[0])) {
                    return logoPath;
                }
            }
        }
        return null;
    }

    public static BlockModel parseBlockModel(JsonElement json) {
        return (BlockModel) ExtendedBlockModelDeserializer.INSTANCE.getAdapter(BlockModel.class).fromJsonTree(json);
    }

    public static void addClientSetup(Runnable clientSetup) {
        Moonlight.assertInitPhase();
        Consumer<FMLClientSetupEvent> eventConsumer = event -> event.enqueueWork(clientSetup);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void addClientSetupAsync(Runnable clientSetup) {
        Moonlight.assertInitPhase();
        Consumer<FMLClientSetupEvent> eventConsumer = event -> clientSetup.run();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(eventConsumer);
    }

    public static void registerOptionalTexturePack(ResourceLocation folderName, Component displayName, boolean defaultEnabled) {
        Moonlight.assertInitPhase();
        PlatHelper.registerResourcePack(PackType.CLIENT_RESOURCES, () -> {
            IModFile file = ModList.get().getModFileById(folderName.getNamespace()).getFile();
            try {
                Pack var6;
                try (PathPackResources pack = new PathPackResources(folderName.toString(), true, file.findResource(new String[] { "resourcepacks/" + folderName.getPath() }))) {
                    PackMetadataSection metadata = (PackMetadataSection) Objects.requireNonNull((PackMetadataSection) pack.m_5550_(PackMetadataSection.TYPE));
                    var6 = Pack.create(folderName.toString(), displayName, defaultEnabled, s -> pack, new Pack.Info(metadata.getDescription(), metadata.getPackFormat(), FeatureFlagSet.of()), PackType.CLIENT_RESOURCES, Pack.Position.TOP, false, PackSource.BUILT_IN);
                }
                return var6;
            } catch (Exception var9) {
                if (!DatagenModLoader.isRunningDataGen()) {
                    var9.printStackTrace();
                }
                return null;
            }
        });
    }

    public static UnbakedModel getUnbakedModel(ModelManager modelManager, ResourceLocation modelLocation) {
        return modelManager.getModelBakery().getModel(modelLocation);
    }

    private static record ParticleEventImpl(RegisterParticleProvidersEvent event) implements ClientHelper.ParticleEvent {

        @Override
        public <P extends ParticleType<T>, T extends ParticleOptions> void register(P type, ClientHelper.ParticleFactory<T> provider) {
            this.event.registerSpriteSet(type, provider::create);
        }
    }
}