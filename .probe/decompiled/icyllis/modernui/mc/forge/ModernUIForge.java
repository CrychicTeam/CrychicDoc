package icyllis.modernui.mc.forge;

import com.mojang.blaze3d.systems.RenderSystem;
import icyllis.modernui.ModernUI;
import icyllis.modernui.mc.FontResourceManager;
import icyllis.modernui.mc.ModernUIClient;
import icyllis.modernui.mc.ModernUIMod;
import icyllis.modernui.util.DataSet;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.LanguageManager;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.ConfigScreenHandler;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.event.IModBusEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.javafmlmod.FMLModContainer;
import net.minecraftforge.fml.loading.FMLEnvironment;

@Mod("modernui")
public final class ModernUIForge extends ModernUIMod {

    private static final Map<String, IEventBus> sModEventBuses = new HashMap();

    public ModernUIForge() {
        if (!FMLEnvironment.production) {
            ModernUIMod.sDevelopment = true;
            ModernUI.LOGGER.debug(ModernUI.MARKER, "Auto detected in FML development environment");
        } else if (ModernUI.class.getSigners() == null) {
            ModernUI.LOGGER.warn(ModernUI.MARKER, "Signature is missing");
        }
        if (ModList.get().isLoaded("tipthescales") && !ModernUIMod.sOptiFineLoaded) {
            ModernUI.LOGGER.fatal(ModernUI.MARKER, "Detected TipTheScales without OptiFine");
            warnSetup("You should remove TipTheScales, Modern UI already includes its features, and Modern UI is also compatible with OptiFine");
        }
        if (ModList.get().isLoaded("reblured")) {
            ModernUI.LOGGER.fatal(ModernUI.MARKER, "Detected ReBlurred");
            warnSetup("You should remove ReBlurred, Modern UI already includes its features, and Modern UI has better performance than it");
        }
        sLegendaryTooltipsLoaded = ModList.get().isLoaded("legendarytooltips");
        sUntranslatedItemsLoaded = ModList.get().isLoaded("untranslateditems");
        Config.initCommonConfig(spec -> ModLoadingContext.get().registerConfig(Type.COMMON, spec, "ModernUI/common.toml"));
        FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> Config.reloadCommon(event.getConfig()));
        LocalStorage.init();
        DistExecutor.safeRunWhenOn(Dist.CLIENT, () -> ModernUIForge.Loader::init);
        if (ModernUIMod.sDevelopment) {
            ModList.get().forEachModContainer((modid, container) -> {
                if (container instanceof FMLModContainer) {
                    String namespace = container.getNamespace();
                    if (!namespace.equals("forge")) {
                        sModEventBuses.put(namespace, ((FMLModContainer) container).getEventBus());
                    }
                }
            });
        }
        ModernUI.LOGGER.info(ModernUI.MARKER, "Initialized Modern UI");
    }

    public static void warnSetup(String key, Object... args) {
        ModLoader.get().addWarning(new ModLoadingWarning(null, ModLoadingStage.SIDED_SETUP, key, args));
    }

    public static <E extends Event & IModBusEvent> boolean post(@Nullable String ns, @Nonnull E e) {
        if (ns == null) {
            boolean handled = false;
            for (IEventBus bus : sModEventBuses.values()) {
                handled |= bus.post(e);
            }
            return handled;
        } else {
            IEventBus bus = (IEventBus) sModEventBuses.get(ns);
            return bus != null && bus.post(e);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class Client extends ModernUIClient {

        private Client() {
            Config.initClientConfig(spec -> ModLoadingContext.get().registerConfig(Type.CLIENT, spec, "ModernUI/client.toml"));
            Config.initTextConfig(spec -> ModLoadingContext.get().registerConfig(Type.CLIENT, spec, "ModernUI/text.toml"));
            FontResourceManager.getInstance();
            if (ModernUIMod.isTextEngineEnabled()) {
                ModernUIText.init();
                LOGGER.info(MARKER, "Initialized Modern UI text engine");
            }
            FMLJavaModLoadingContext.get().getModEventBus().addListener(event -> Config.reloadAnyClient(event.getConfig()));
            ModLoadingContext.get().registerExtensionPoint(ConfigScreenHandler.ConfigScreenFactory.class, () -> new ConfigScreenHandler.ConfigScreenFactory((mc, modsScreen) -> {
                DataSet args = new DataSet();
                args.putBoolean("navigateToPreferences", true);
                CenterFragment2 fragment = new CenterFragment2();
                fragment.setArguments(args);
                return MuiForgeApi.get().createScreen(fragment, null, modsScreen);
            }));
            if (ModernUIMod.sDevelopment) {
                FMLJavaModLoadingContext.get().getModEventBus().register(Registration.ModClientDev.class);
            }
            LOGGER.info(MARKER, "Initialized Modern UI client");
        }

        @Override
        protected void checkFirstLoadTypeface() {
            if (RenderSystem.isOnRenderThread() || Minecraft.getInstance().m_18695_()) {
                LOGGER.error(MARKER, "Loading typeface on the render thread, but it should be on a worker thread.\nDon't report to Modern UI, but to other mods as displayed in stack trace.", new Exception("Loading typeface at the wrong mod loading stage").fillInStackTrace());
            }
        }

        @Nonnull
        @Override
        protected Locale onGetSelectedLocale() {
            Minecraft minecraft;
            LanguageManager languageManager;
            return (minecraft = Minecraft.getInstance()) != null && (languageManager = minecraft.getLanguageManager()) != null ? languageManager.getJavaLocale() : super.onGetSelectedLocale();
        }

        static {
            assert FMLEnvironment.dist.isClient();
        }
    }

    private static class Loader {

        public static void init() {
            new ModernUIForge.Client();
        }
    }
}