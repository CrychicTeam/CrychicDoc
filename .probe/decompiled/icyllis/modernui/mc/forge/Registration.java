package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import icyllis.modernui.ModernUI;
import icyllis.modernui.core.Core;
import icyllis.modernui.core.Handler;
import icyllis.modernui.fragment.FragmentController;
import icyllis.modernui.graphics.ImageStore;
import icyllis.modernui.mc.FontResourceManager;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.mc.MuiModApi;
import icyllis.modernui.mc.OptiFineIntegration;
import icyllis.modernui.mc.TooltipRenderType;
import icyllis.modernui.mc.UIManager;
import icyllis.modernui.mc.mixin.AccessOptions;
import icyllis.modernui.mc.testforge.TestContainerMenu;
import icyllis.modernui.mc.testforge.TestPauseFragment;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.IntStream;
import javax.annotation.Nonnull;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.OptionInstance;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.CycleButton;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.ShaderInstance;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.util.Mth;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.RegisterShadersEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.CrashReportCallables;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.apache.commons.io.output.StringBuilderWriter;

@EventBusSubscriber(modid = "modernui", bus = Bus.MOD)
final class Registration {

    private Registration() {
    }

    @SubscribeEvent
    static void register(@Nonnull RegisterEvent event) {
        if (ModernUIMod.sDevelopment) {
            event.register(ForgeRegistries.MENU_TYPES.getRegistryKey(), Registration::registerMenus);
            event.register(ForgeRegistries.ITEMS.getRegistryKey(), Registration::registerItems);
        }
    }

    static void registerMenus(@Nonnull RegisterEvent.RegisterHelper<MenuType<?>> helper) {
        helper.register(MuiRegistries.TEST_MENU_KEY, IForgeMenuType.create(TestContainerMenu::new));
    }

    static void registerItems(@Nonnull RegisterEvent.RegisterHelper<Item> helper) {
        Item.Properties properties = new Item.Properties().stacksTo(1);
        helper.register(MuiRegistries.PROJECT_BUILDER_ITEM_KEY, new ProjectBuilderItem(properties));
    }

    @SubscribeEvent
    static void setupCommon(@Nonnull FMLCommonSetupEvent event) {
        if (ModList.get().getModContainerById(new String(new byte[] { 107, 73, 119, 105 }, StandardCharsets.UTF_8).toLowerCase(Locale.ROOT)).isPresent()) {
            event.enqueueWork(() -> ModernUI.LOGGER.fatal("OK"));
        }
        if (ModernUIMod.sDevelopment) {
            NetworkMessages.sNetwork = (NetworkHandler) DistExecutor.safeRunForDist(() -> NetworkMessages::client, () -> NetworkMessages::new);
        }
        MinecraftForge.EVENT_BUS.register(ServerHandler.INSTANCE);
    }

    @EventBusSubscriber(modid = "modernui", bus = Bus.MOD, value = { Dist.CLIENT })
    static class ModClient {

        private ModClient() {
        }

        @SubscribeEvent
        static void loadingClient(RegisterParticleProvidersEvent event) {
            UIManagerForge.initialize();
        }

        @SubscribeEvent
        static void registerResourceListener(@Nonnull RegisterClientReloadListenersEvent event) {
            event.registerReloadListener((ResourceManagerReloadListener) manager -> {
                ImageStore.getInstance().clear();
                Handler handler = Core.getUiHandlerAsync();
                if (handler != null) {
                    handler.post(() -> UIManager.getInstance().updateLayoutDir(Config.CLIENT.mForceRtl.get()));
                }
            });
            if (!ModernUIMod.isTextEngineEnabled()) {
                event.registerReloadListener(FontResourceManager.getInstance());
            }
            ModernUI.LOGGER.debug(ModernUI.MARKER, "Registered resource reload listener");
        }

        @SubscribeEvent
        static void registerKeyMapping(@Nonnull RegisterKeyMappingsEvent event) {
            event.register(UIManagerForge.OPEN_CENTER_KEY);
            event.register(UIManagerForge.ZOOM_KEY);
        }

        @SubscribeEvent
        static void setupClient(@Nonnull FMLClientSetupEvent event) {
            event.enqueueWork(() -> {
                UIManagerForge.initializeRenderer();
                Config.CLIENT.mLastWindowMode.apply();
                if (ModernUIMod.sDevelopment) {
                    MenuScreens.register(MuiRegistries.TEST_MENU.get(), MenuScreenFactory.create(menu -> new TestPauseFragment()));
                }
            });
            CrashReportCallables.registerCrashCallable("Fragments", () -> {
                FragmentController fragments = UIManager.getInstance().getFragmentController();
                StringBuilder builder = new StringBuilder();
                if (fragments != null) {
                    PrintWriter pw = new PrintWriter(new StringBuilderWriter(builder));
                    try {
                        fragments.getFragmentManager().dump("", null, pw);
                    } catch (Throwable var6) {
                        try {
                            pw.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                        throw var6;
                    }
                    pw.close();
                }
                return builder.toString();
            }, () -> UIManager.getInstance().getFragmentController() != null);
            if (Config.CLIENT.mUseNewGuiScale.get()) {
                OptionInstance<Integer> newGuiScale = new OptionInstance<>("options.guiScale", OptionInstance.noTooltip(), (caption, value) -> {
                    int r = MuiModApi.calcGuiScales();
                    if (value == 0) {
                        int auto = r >> 4 & 15;
                        return Options.genericValueLabel(caption, Component.translatable("options.guiScale.auto").append(Component.literal(" (" + auto + ")")));
                    } else {
                        MutableComponent valueComponent = Component.literal(value.toString());
                        int min = r >> 8 & 15;
                        int max = r & 15;
                        if (value < min || value > max) {
                            MutableComponent hint;
                            if (value < min) {
                                hint = Component.literal(" (<" + min + ")");
                            } else {
                                hint = Component.literal(" (>" + max + ")");
                            }
                            valueComponent.append(hint);
                            valueComponent.withStyle(ChatFormatting.RED);
                        }
                        return Options.genericValueLabel(caption, valueComponent);
                    }
                }, new Registration.ModClient.GuiScaleValueSet(), 0, value -> Minecraft.getInstance().m_6937_(() -> {
                    Minecraft minecraft = Minecraft.getInstance();
                    if ((int) minecraft.getWindow().getGuiScale() != minecraft.getWindow().calculateScale(value, false)) {
                        minecraft.resizeDisplay();
                    }
                }));
                Options options = Minecraft.getInstance().options;
                newGuiScale.set(options.guiScale().get());
                ((AccessOptions) options).setGuiScale(newGuiScale);
                if (ModernUIMod.isOptiFineLoaded()) {
                    OptiFineIntegration.setGuiScale(newGuiScale);
                    ModernUI.LOGGER.debug(ModernUI.MARKER, "Override OptiFine Gui Scale");
                }
            }
        }

        @SubscribeEvent
        static void onMenuOpen(@Nonnull OpenMenuEvent event) {
            if (ModernUIMod.sDevelopment && event.getMenu() instanceof TestContainerMenu c) {
                event.set(new TestPauseFragment());
            }
        }

        @SubscribeEvent
        static void onRegisterShaders(@Nonnull RegisterShadersEvent event) {
            try {
                event.registerShader(new ShaderInstance(event.getResourceProvider(), ModernUIMod.location("rendertype_modern_tooltip"), DefaultVertexFormat.POSITION), TooltipRenderType::setShaderTooltip);
            } catch (IOException var2) {
                ModernUI.LOGGER.error(ModernUI.MARKER, "Bad tooltip shader", var2);
            }
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }

        static class GuiScaleValueSet implements OptionInstance.IntRangeBase, OptionInstance.SliderableOrCyclableValueSet<Integer> {

            @Override
            public int minInclusive() {
                return 0;
            }

            @Override
            public int maxInclusive() {
                return 8;
            }

            @Nonnull
            @Override
            public Integer fromSliderValue(double progress) {
                return Math.toIntExact(Math.round(Mth.map(progress, 0.0, 1.0, (double) this.minInclusive(), (double) this.maxInclusive())));
            }

            @Nonnull
            public Optional<Integer> validateValue(@Nonnull Integer value) {
                return Optional.of(Mth.clamp(value, this.minInclusive(), this.maxInclusive()));
            }

            @Nonnull
            @Override
            public Codec<Integer> codec() {
                return ExtraCodecs.validate(Codec.INT, value -> {
                    int max = this.maxInclusive() + 1;
                    return value.compareTo(this.minInclusive()) >= 0 && value.compareTo(max) <= 0 ? DataResult.success(value) : DataResult.error(() -> "Value " + value + " outside of range [" + this.minInclusive() + ":" + max + "]", value);
                });
            }

            @Nonnull
            @Override
            public CycleButton.ValueListSupplier<Integer> valueListSupplier() {
                return CycleButton.ValueListSupplier.create(IntStream.range(this.minInclusive(), this.maxInclusive() + 1).boxed().toList());
            }

            @Override
            public boolean createCycleButton() {
                return false;
            }
        }
    }

    static class ModClientDev {

        private ModClientDev() {
        }

        @SubscribeEvent
        static void onRegistryModel(@Nonnull ModelEvent.RegisterAdditional event) {
            event.register(ModernUIMod.location("item/project_builder_main"));
            event.register(ModernUIMod.location("item/project_builder_cube"));
        }

        @SubscribeEvent
        static void onBakeModel(@Nonnull ModelEvent.ModifyBakingResult event) {
            Map<ResourceLocation, BakedModel> registry = event.getModels();
            replaceModel(registry, new ModelResourceLocation("modernui", "project_builder", "inventory"), baseModel -> new ProjectBuilderModel(baseModel, event.getModels()));
        }

        private static void replaceModel(@Nonnull Map<ResourceLocation, BakedModel> modelRegistry, @Nonnull ModelResourceLocation location, @Nonnull Function<BakedModel, BakedModel> replacer) {
            modelRegistry.put(location, (BakedModel) replacer.apply((BakedModel) modelRegistry.get(location)));
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }
    }
}