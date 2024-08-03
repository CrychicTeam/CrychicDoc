package net.minecraft.server;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.RegistryAccess;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.MultiPackResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.WorldDataConfiguration;
import org.slf4j.Logger;

public class WorldLoader {

    private static final Logger LOGGER = LogUtils.getLogger();

    public static <D, R> CompletableFuture<R> load(WorldLoader.InitConfig worldLoaderInitConfig0, WorldLoader.WorldDataSupplier<D> worldLoaderWorldDataSupplierD1, WorldLoader.ResultFactory<D, R> worldLoaderResultFactoryDR2, Executor executor3, Executor executor4) {
        try {
            Pair<WorldDataConfiguration, CloseableResourceManager> $$5 = worldLoaderInitConfig0.packConfig.createResourceManager();
            CloseableResourceManager $$6 = (CloseableResourceManager) $$5.getSecond();
            LayeredRegistryAccess<RegistryLayer> $$7 = RegistryLayer.createRegistryAccess();
            LayeredRegistryAccess<RegistryLayer> $$8 = loadAndReplaceLayer($$6, $$7, RegistryLayer.WORLDGEN, RegistryDataLoader.WORLDGEN_REGISTRIES);
            RegistryAccess.Frozen $$9 = $$8.getAccessForLoading(RegistryLayer.DIMENSIONS);
            RegistryAccess.Frozen $$10 = RegistryDataLoader.load($$6, $$9, RegistryDataLoader.DIMENSION_REGISTRIES);
            WorldDataConfiguration $$11 = (WorldDataConfiguration) $$5.getFirst();
            WorldLoader.DataLoadOutput<D> $$12 = worldLoaderWorldDataSupplierD1.get(new WorldLoader.DataLoadContext($$6, $$11, $$9, $$10));
            LayeredRegistryAccess<RegistryLayer> $$13 = $$8.replaceFrom(RegistryLayer.DIMENSIONS, $$12.finalDimensions);
            RegistryAccess.Frozen $$14 = $$13.getAccessForLoading(RegistryLayer.RELOADABLE);
            return ReloadableServerResources.loadResources($$6, $$14, $$11.enabledFeatures(), worldLoaderInitConfig0.commandSelection(), worldLoaderInitConfig0.functionCompilationLevel(), executor3, executor4).whenComplete((p_214370_, p_214371_) -> {
                if (p_214371_ != null) {
                    $$6.close();
                }
            }).thenApplyAsync(p_248101_ -> {
                p_248101_.updateRegistryTags($$14);
                return worldLoaderResultFactoryDR2.create($$6, p_248101_, $$13, $$12.cookie);
            }, executor4);
        } catch (Exception var15) {
            return CompletableFuture.failedFuture(var15);
        }
    }

    private static RegistryAccess.Frozen loadLayer(ResourceManager resourceManager0, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer1, RegistryLayer registryLayer2, List<RegistryDataLoader.RegistryData<?>> listRegistryDataLoaderRegistryData3) {
        RegistryAccess.Frozen $$4 = layeredRegistryAccessRegistryLayer1.getAccessForLoading(registryLayer2);
        return RegistryDataLoader.load(resourceManager0, $$4, listRegistryDataLoaderRegistryData3);
    }

    private static LayeredRegistryAccess<RegistryLayer> loadAndReplaceLayer(ResourceManager resourceManager0, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer1, RegistryLayer registryLayer2, List<RegistryDataLoader.RegistryData<?>> listRegistryDataLoaderRegistryData3) {
        RegistryAccess.Frozen $$4 = loadLayer(resourceManager0, layeredRegistryAccessRegistryLayer1, registryLayer2, listRegistryDataLoaderRegistryData3);
        return layeredRegistryAccessRegistryLayer1.replaceFrom(registryLayer2, $$4);
    }

    public static record DataLoadContext(ResourceManager f_244187_, WorldDataConfiguration f_244127_, RegistryAccess.Frozen f_244104_, RegistryAccess.Frozen f_243759_) {

        private final ResourceManager resources;

        private final WorldDataConfiguration dataConfiguration;

        private final RegistryAccess.Frozen datapackWorldgen;

        private final RegistryAccess.Frozen datapackDimensions;

        public DataLoadContext(ResourceManager f_244187_, WorldDataConfiguration f_244127_, RegistryAccess.Frozen f_244104_, RegistryAccess.Frozen f_243759_) {
            this.resources = f_244187_;
            this.dataConfiguration = f_244127_;
            this.datapackWorldgen = f_244104_;
            this.datapackDimensions = f_243759_;
        }
    }

    public static record DataLoadOutput<D>(D f_244458_, RegistryAccess.Frozen f_244143_) {

        private final D cookie;

        private final RegistryAccess.Frozen finalDimensions;

        public DataLoadOutput(D f_244458_, RegistryAccess.Frozen f_244143_) {
            this.cookie = f_244458_;
            this.finalDimensions = f_244143_;
        }
    }

    public static record InitConfig(WorldLoader.PackConfig f_214378_, Commands.CommandSelection f_214379_, int f_214380_) {

        private final WorldLoader.PackConfig packConfig;

        private final Commands.CommandSelection commandSelection;

        private final int functionCompilationLevel;

        public InitConfig(WorldLoader.PackConfig f_214378_, Commands.CommandSelection f_214379_, int f_214380_) {
            this.packConfig = f_214378_;
            this.commandSelection = f_214379_;
            this.functionCompilationLevel = f_214380_;
        }
    }

    public static record PackConfig(PackRepository f_214392_, WorldDataConfiguration f_244366_, boolean f_214394_, boolean f_244133_) {

        private final PackRepository packRepository;

        private final WorldDataConfiguration initialDataConfig;

        private final boolean safeMode;

        private final boolean initMode;

        public PackConfig(PackRepository f_214392_, WorldDataConfiguration f_244366_, boolean f_214394_, boolean f_244133_) {
            this.packRepository = f_214392_;
            this.initialDataConfig = f_244366_;
            this.safeMode = f_214394_;
            this.initMode = f_244133_;
        }

        public Pair<WorldDataConfiguration, CloseableResourceManager> createResourceManager() {
            FeatureFlagSet $$0 = this.initMode ? FeatureFlags.REGISTRY.allFlags() : this.initialDataConfig.enabledFeatures();
            WorldDataConfiguration $$1 = MinecraftServer.configurePackRepository(this.packRepository, this.initialDataConfig.dataPacks(), this.safeMode, $$0);
            if (!this.initMode) {
                $$1 = $$1.expandFeatures(this.initialDataConfig.enabledFeatures());
            }
            List<PackResources> $$2 = this.packRepository.openAllSelected();
            CloseableResourceManager $$3 = new MultiPackResourceManager(PackType.SERVER_DATA, $$2);
            return Pair.of($$1, $$3);
        }
    }

    @FunctionalInterface
    public interface ResultFactory<D, R> {

        R create(CloseableResourceManager var1, ReloadableServerResources var2, LayeredRegistryAccess<RegistryLayer> var3, D var4);
    }

    @FunctionalInterface
    public interface WorldDataSupplier<D> {

        WorldLoader.DataLoadOutput<D> get(WorldLoader.DataLoadContext var1);
    }
}