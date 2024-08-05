package net.minecraftforge.client.loading;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Consumer;
import net.minecraft.SharedConstants;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.repository.RepositorySource;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.DataPackConfig;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.gui.LoadingErrorScreen;
import net.minecraftforge.common.ForgeConfig;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LogicalSidedProvider;
import net.minecraftforge.event.AddPackFindersEvent;
import net.minecraftforge.fml.LoadingFailedException;
import net.minecraftforge.fml.Logging;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoader;
import net.minecraftforge.fml.ModLoadingStage;
import net.minecraftforge.fml.ModLoadingWarning;
import net.minecraftforge.fml.ModWorkManager;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.ModWorkManager.DrivenExecutor;
import net.minecraftforge.fml.VersionChecker.Status;
import net.minecraftforge.fml.loading.ImmediateWindowHandler;
import net.minecraftforge.forgespi.language.IModInfo;
import net.minecraftforge.forgespi.locating.IModFile;
import net.minecraftforge.internal.BrandingControl;
import net.minecraftforge.logging.CrashReportExtender;
import net.minecraftforge.resource.DelegatingPackResources;
import net.minecraftforge.resource.PathPackResources;
import net.minecraftforge.resource.ResourcePackLoader;
import net.minecraftforge.server.LanguageHook;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@OnlyIn(Dist.CLIENT)
public class ClientModLoader {

    private static final Logger LOGGER = LogManager.getLogger();

    private static boolean loading;

    private static Minecraft mc;

    private static boolean loadingComplete;

    private static LoadingFailedException error;

    public static void begin(Minecraft minecraft, PackRepository defaultResourcePacks, ReloadableResourceManager mcResourceManager) {
        Runtime.getRuntime().addShutdownHook(new Thread(LogManager::shutdown));
        ImmediateWindowHandler.updateProgress("Loading mods");
        loading = true;
        mc = minecraft;
        LogicalSidedProvider.setClient(() -> minecraft);
        LanguageHook.loadForgeAndMCLangs();
        createRunnableWithCatch(() -> ModLoader.get().gatherAndInitializeMods(ModWorkManager.syncExecutor(), ModWorkManager.parallelExecutor(), ImmediateWindowHandler::renderTick)).run();
        if (error == null) {
            ResourcePackLoader.loadResourcePacks(defaultResourcePacks, ClientModLoader::buildPackFinder);
            ModLoader.get().postEvent(new AddPackFindersEvent(PackType.CLIENT_RESOURCES, defaultResourcePacks::addPackFinder));
            DataPackConfig.DEFAULT.addModPacks(ResourcePackLoader.getPackNames());
            mcResourceManager.registerReloadListener(ClientModLoader::onResourceReload);
            mcResourceManager.registerReloadListener(BrandingControl.resourceManagerReloadListener());
        }
    }

    private static CompletableFuture<Void> onResourceReload(PreparableReloadListener.PreparationBarrier stage, ResourceManager resourceManager, ProfilerFiller prepareProfiler, ProfilerFiller executeProfiler, Executor asyncExecutor, Executor syncExecutor) {
        return CompletableFuture.runAsync(createRunnableWithCatch(() -> startModLoading(ModWorkManager.wrappedExecutor(syncExecutor), asyncExecutor)), ModWorkManager.parallelExecutor()).thenCompose(stage::m_6769_).thenRunAsync(() -> finishModLoading(ModWorkManager.wrappedExecutor(syncExecutor), asyncExecutor), ModWorkManager.parallelExecutor());
    }

    private static Runnable createRunnableWithCatch(Runnable r) {
        return () -> {
            if (!loadingComplete) {
                try {
                    r.run();
                } catch (LoadingFailedException var2) {
                    if (error == null) {
                        error = var2;
                    }
                }
            }
        };
    }

    private static void startModLoading(DrivenExecutor syncExecutor, Executor parallelExecutor) {
        createRunnableWithCatch(() -> ModLoader.get().loadMods(syncExecutor, parallelExecutor, ImmediateWindowHandler::renderTick)).run();
    }

    private static void finishModLoading(DrivenExecutor syncExecutor, Executor parallelExecutor) {
        createRunnableWithCatch(() -> ModLoader.get().finishMods(syncExecutor, parallelExecutor, ImmediateWindowHandler::renderTick)).run();
        loading = false;
        loadingComplete = true;
        syncExecutor.execute(() -> mc.options.load(true));
    }

    public static Status checkForUpdates() {
        boolean anyOutdated = ModList.get().getMods().stream().map(VersionChecker::getResult).map(result -> result.status()).anyMatch(status -> status == Status.OUTDATED || status == Status.BETA_OUTDATED);
        return anyOutdated ? Status.OUTDATED : null;
    }

    public static boolean completeModLoading() {
        List<ModLoadingWarning> warnings = ModLoader.get().getWarnings();
        boolean showWarnings = true;
        try {
            showWarnings = ForgeConfig.CLIENT.showLoadWarnings.get();
        } catch (IllegalStateException | NullPointerException var3) {
        }
        if (!showWarnings) {
            if (!warnings.isEmpty()) {
                LOGGER.warn(Logging.LOADING, "Mods loaded with {} warning(s)", warnings.size());
                warnings.forEach(warning -> LOGGER.warn(Logging.LOADING, warning.formatToString()));
            }
            warnings = Collections.emptyList();
        }
        File dumpedLocation = null;
        if (error == null) {
            MinecraftForge.EVENT_BUS.start();
        } else {
            LanguageHook.loadForgeAndMCLangs();
            dumpedLocation = CrashReportExtender.dumpModLoadingCrashReport(LOGGER, error, mc.gameDirectory);
        }
        if (error == null && warnings.isEmpty()) {
            return false;
        } else {
            mc.setScreen(new LoadingErrorScreen(error, warnings, dumpedLocation));
            return true;
        }
    }

    public static boolean isLoading() {
        return loading;
    }

    private static RepositorySource buildPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks) {
        return packAcceptor -> clientPackFinder(modResourcePacks, packAcceptor);
    }

    private static void clientPackFinder(Map<IModFile, ? extends PathPackResources> modResourcePacks, Consumer<Pack> packAcceptor) {
        ArrayList<PathPackResources> hiddenPacks = new ArrayList();
        for (Entry<IModFile, ? extends PathPackResources> e : modResourcePacks.entrySet()) {
            IModInfo mod = (IModInfo) ((IModFile) e.getKey()).getModInfos().get(0);
            String name = "mod:" + mod.getModId();
            Pack modPack = Pack.readMetaAndCreate(name, Component.literal(((PathPackResources) e.getValue()).m_5542_()), false, id -> (PackResources) e.getValue(), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.DEFAULT);
            if (modPack == null) {
                ModLoader.get().addWarning(new ModLoadingWarning(mod, ModLoadingStage.ERROR, "fml.modloading.brokenresources", new Object[] { e.getKey() }));
            } else {
                LOGGER.debug(Logging.CORE, "Generating PackInfo named {} for mod file {}", name, ((IModFile) e.getKey()).getFilePath());
                if (mod.getOwningFile().showAsResourcePack()) {
                    packAcceptor.accept(modPack);
                } else {
                    hiddenPacks.add((PathPackResources) e.getValue());
                }
            }
        }
        Pack modResourcesPack = Pack.readMetaAndCreate("mod_resources", Component.literal("Mod Resources"), true, id -> new DelegatingPackResources(id, false, new PackMetadataSection(Component.translatable("fml.resources.modresources", hiddenPacks.size()), SharedConstants.getCurrentVersion().getPackVersion(PackType.CLIENT_RESOURCES)), hiddenPacks), PackType.CLIENT_RESOURCES, Pack.Position.BOTTOM, PackSource.DEFAULT);
        packAcceptor.accept(modResourcesPack);
    }
}