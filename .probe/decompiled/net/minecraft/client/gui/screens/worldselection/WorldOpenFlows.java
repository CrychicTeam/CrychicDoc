package net.minecraft.client.gui.screens.worldselection;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import it.unimi.dsi.fastutil.booleans.BooleanConsumer;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.toasts.SystemToast;
import net.minecraft.client.gui.screens.AlertScreen;
import net.minecraft.client.gui.screens.BackupConfirmScreen;
import net.minecraft.client.gui.screens.ConfirmScreen;
import net.minecraft.client.gui.screens.DatapackLoadFailureScreen;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.SymlinkWarningScreen;
import net.minecraft.commands.Commands;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.ReloadableServerResources;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import net.minecraft.world.level.validation.ContentValidationException;
import org.slf4j.Logger;

public class WorldOpenFlows {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Minecraft minecraft;

    private final LevelStorageSource levelSource;

    public WorldOpenFlows(Minecraft minecraft0, LevelStorageSource levelStorageSource1) {
        this.minecraft = minecraft0;
        this.levelSource = levelStorageSource1;
    }

    public void loadLevel(Screen screen0, String string1) {
        this.doLoadLevel(screen0, string1, false, true);
    }

    public void createFreshLevel(String string0, LevelSettings levelSettings1, WorldOptions worldOptions2, Function<RegistryAccess, WorldDimensions> functionRegistryAccessWorldDimensions3) {
        LevelStorageSource.LevelStorageAccess $$4 = this.createWorldAccess(string0);
        if ($$4 != null) {
            PackRepository $$5 = ServerPacksSource.createPackRepository($$4);
            WorldDataConfiguration $$6 = levelSettings1.getDataConfiguration();
            try {
                WorldLoader.PackConfig $$7 = new WorldLoader.PackConfig($$5, $$6, false, false);
                WorldStem $$8 = this.loadWorldDataBlocking($$7, p_258145_ -> {
                    WorldDimensions.Complete $$4x = ((WorldDimensions) functionRegistryAccessWorldDimensions3.apply(p_258145_.datapackWorldgen())).bake(p_258145_.datapackDimensions().m_175515_(Registries.LEVEL_STEM));
                    return new WorldLoader.DataLoadOutput<>(new PrimaryLevelData(levelSettings1, worldOptions2, $$4x.specialWorldProperty(), $$4x.lifecycle()), $$4x.dimensionsRegistryAccess());
                }, WorldStem::new);
                this.minecraft.doWorldLoad(string0, $$4, $$5, $$8, true);
            } catch (Exception var10) {
                LOGGER.warn("Failed to load datapacks, can't proceed with server load", var10);
                safeCloseAccess($$4, string0);
            }
        }
    }

    @Nullable
    private LevelStorageSource.LevelStorageAccess createWorldAccess(String string0) {
        try {
            return this.levelSource.validateAndCreateAccess(string0);
        } catch (IOException var3) {
            LOGGER.warn("Failed to read level {} data", string0, var3);
            SystemToast.onWorldAccessFailure(this.minecraft, string0);
            this.minecraft.setScreen(null);
            return null;
        } catch (ContentValidationException var4) {
            LOGGER.warn("{}", var4.getMessage());
            this.minecraft.setScreen(new SymlinkWarningScreen(null));
            return null;
        }
    }

    public void createLevelFromExistingSettings(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, ReloadableServerResources reloadableServerResources1, LayeredRegistryAccess<RegistryLayer> layeredRegistryAccessRegistryLayer2, WorldData worldData3) {
        PackRepository $$4 = ServerPacksSource.createPackRepository(levelStorageSourceLevelStorageAccess0);
        CloseableResourceManager $$5 = (CloseableResourceManager) new WorldLoader.PackConfig($$4, worldData3.getDataConfiguration(), false, false).createResourceManager().getSecond();
        this.minecraft.doWorldLoad(levelStorageSourceLevelStorageAccess0.getLevelId(), levelStorageSourceLevelStorageAccess0, $$4, new WorldStem($$5, reloadableServerResources1, layeredRegistryAccessRegistryLayer2, worldData3), true);
    }

    private WorldStem loadWorldStem(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, boolean boolean1, PackRepository packRepository2) throws Exception {
        WorldLoader.PackConfig $$3 = this.getPackConfigFromLevelData(levelStorageSourceLevelStorageAccess0, boolean1, packRepository2);
        return this.loadWorldDataBlocking($$3, p_247851_ -> {
            DynamicOps<Tag> $$2 = RegistryOps.create(NbtOps.INSTANCE, p_247851_.datapackWorldgen());
            Registry<LevelStem> $$3x = p_247851_.datapackDimensions().m_175515_(Registries.LEVEL_STEM);
            Pair<WorldData, WorldDimensions.Complete> $$4 = levelStorageSourceLevelStorageAccess0.getDataTag($$2, p_247851_.dataConfiguration(), $$3x, p_247851_.datapackWorldgen().m_211816_());
            if ($$4 == null) {
                throw new IllegalStateException("Failed to load world");
            } else {
                return new WorldLoader.DataLoadOutput<>((WorldData) $$4.getFirst(), ((WorldDimensions.Complete) $$4.getSecond()).dimensionsRegistryAccess());
            }
        }, WorldStem::new);
    }

    public Pair<LevelSettings, WorldCreationContext> recreateWorldData(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0) throws Exception {
        PackRepository $$1 = ServerPacksSource.createPackRepository(levelStorageSourceLevelStorageAccess0);
        WorldLoader.PackConfig $$2 = this.getPackConfigFromLevelData(levelStorageSourceLevelStorageAccess0, false, $$1);
        return this.loadWorldDataBlocking($$2, p_247857_ -> {
            DynamicOps<Tag> $$2x = RegistryOps.create(NbtOps.INSTANCE, p_247857_.datapackWorldgen());
            Registry<LevelStem> $$3 = new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();
            Pair<WorldData, WorldDimensions.Complete> $$4 = levelStorageSourceLevelStorageAccess0.getDataTag($$2x, p_247857_.dataConfiguration(), $$3, p_247857_.datapackWorldgen().m_211816_());
            if ($$4 == null) {
                throw new IllegalStateException("Failed to load world");
            } else {
                record Data(LevelSettings f_244166_, WorldOptions f_244534_, Registry<LevelStem> f_244151_) {

                    private final LevelSettings levelSettings;

                    private final WorldOptions options;

                    private final Registry<LevelStem> existingDimensions;

                    Data(LevelSettings f_244166_, WorldOptions f_244534_, Registry<LevelStem> f_244151_) {
                        this.levelSettings = f_244166_;
                        this.options = f_244534_;
                        this.existingDimensions = f_244151_;
                    }
                }
                return new WorldLoader.DataLoadOutput<>(new Data(((WorldData) $$4.getFirst()).getLevelSettings(), ((WorldData) $$4.getFirst()).worldGenOptions(), ((WorldDimensions.Complete) $$4.getSecond()).dimensions()), p_247857_.datapackDimensions());
            }
        }, (p_247840_, p_247841_, p_247842_, p_247843_) -> {
            p_247840_.close();
            return Pair.of(p_247843_.levelSettings, new WorldCreationContext(p_247843_.options, new WorldDimensions(p_247843_.existingDimensions), p_247842_, p_247841_, p_247843_.levelSettings.getDataConfiguration()));
        });
    }

    private WorldLoader.PackConfig getPackConfigFromLevelData(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, boolean boolean1, PackRepository packRepository2) {
        WorldDataConfiguration $$3 = levelStorageSourceLevelStorageAccess0.getDataConfiguration();
        if ($$3 == null) {
            throw new IllegalStateException("Failed to load data pack config");
        } else {
            return new WorldLoader.PackConfig(packRepository2, $$3, boolean1, false);
        }
    }

    public WorldStem loadWorldStem(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, boolean boolean1) throws Exception {
        PackRepository $$2 = ServerPacksSource.createPackRepository(levelStorageSourceLevelStorageAccess0);
        return this.loadWorldStem(levelStorageSourceLevelStorageAccess0, boolean1, $$2);
    }

    private <D, R> R loadWorldDataBlocking(WorldLoader.PackConfig worldLoaderPackConfig0, WorldLoader.WorldDataSupplier<D> worldLoaderWorldDataSupplierD1, WorldLoader.ResultFactory<D, R> worldLoaderResultFactoryDR2) throws Exception {
        WorldLoader.InitConfig $$3 = new WorldLoader.InitConfig(worldLoaderPackConfig0, Commands.CommandSelection.INTEGRATED, 2);
        CompletableFuture<R> $$4 = WorldLoader.load($$3, worldLoaderWorldDataSupplierD1, worldLoaderResultFactoryDR2, Util.backgroundExecutor(), this.minecraft);
        this.minecraft.m_18701_($$4::isDone);
        return (R) $$4.get();
    }

    private void doLoadLevel(Screen screen0, String string1, boolean boolean2, boolean boolean3) {
        LevelStorageSource.LevelStorageAccess $$4 = this.createWorldAccess(string1);
        if ($$4 != null) {
            PackRepository $$5 = ServerPacksSource.createPackRepository($$4);
            WorldStem $$6;
            try {
                $$6 = this.loadWorldStem($$4, boolean2, $$5);
            } catch (Exception var11) {
                LOGGER.warn("Failed to load level data or datapacks, can't proceed with server load", var11);
                if (!boolean2) {
                    this.minecraft.setScreen(new DatapackLoadFailureScreen(() -> this.doLoadLevel(screen0, string1, true, boolean3)));
                } else {
                    this.minecraft.setScreen(new AlertScreen(() -> this.minecraft.setScreen(null), Component.translatable("datapackFailure.safeMode.failed.title"), Component.translatable("datapackFailure.safeMode.failed.description"), CommonComponents.GUI_TO_TITLE, true));
                }
                safeCloseAccess($$4, string1);
                return;
            }
            WorldData $$9 = $$6.worldData();
            boolean $$10 = $$9.worldGenOptions().isOldCustomizedWorld();
            boolean $$11 = $$9.worldGenSettingsLifecycle() != Lifecycle.stable();
            if (!boolean3 || !$$10 && !$$11) {
                this.minecraft.getDownloadedPackSource().loadBundledResourcePack($$4).thenApply(p_233177_ -> true).exceptionallyComposeAsync(p_233183_ -> {
                    LOGGER.warn("Failed to load pack: ", p_233183_);
                    return this.promptBundledPackLoadFailure();
                }, this.minecraft).thenAcceptAsync(p_233168_ -> {
                    if (p_233168_) {
                        this.minecraft.doWorldLoad(string1, $$4, $$5, $$6, false);
                    } else {
                        $$6.close();
                        safeCloseAccess($$4, string1);
                        this.minecraft.getDownloadedPackSource().clearServerPack().thenRunAsync(() -> this.minecraft.setScreen(screen0), this.minecraft);
                    }
                }, this.minecraft).exceptionally(p_233175_ -> {
                    this.minecraft.delayCrash(CrashReport.forThrowable(p_233175_, "Load world"));
                    return null;
                });
            } else {
                this.askForBackup(screen0, string1, $$10, () -> this.doLoadLevel(screen0, string1, boolean2, false));
                $$6.close();
                safeCloseAccess($$4, string1);
            }
        }
    }

    private CompletableFuture<Boolean> promptBundledPackLoadFailure() {
        CompletableFuture<Boolean> $$0 = new CompletableFuture();
        this.minecraft.setScreen(new ConfirmScreen($$0::complete, Component.translatable("multiplayer.texturePrompt.failure.line1"), Component.translatable("multiplayer.texturePrompt.failure.line2"), CommonComponents.GUI_PROCEED, CommonComponents.GUI_CANCEL));
        return $$0;
    }

    private static void safeCloseAccess(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, String string1) {
        try {
            levelStorageSourceLevelStorageAccess0.close();
        } catch (IOException var3) {
            LOGGER.warn("Failed to unlock access to level {}", string1, var3);
        }
    }

    private void askForBackup(Screen screen0, String string1, boolean boolean2, Runnable runnable3) {
        Component $$4;
        Component $$5;
        if (boolean2) {
            $$4 = Component.translatable("selectWorld.backupQuestion.customized");
            $$5 = Component.translatable("selectWorld.backupWarning.customized");
        } else {
            $$4 = Component.translatable("selectWorld.backupQuestion.experimental");
            $$5 = Component.translatable("selectWorld.backupWarning.experimental");
        }
        this.minecraft.setScreen(new BackupConfirmScreen(screen0, (p_233172_, p_233173_) -> {
            if (p_233172_) {
                EditWorldScreen.makeBackupAndShowToast(this.levelSource, string1);
            }
            runnable3.run();
        }, $$4, $$5, false));
    }

    public static void confirmWorldCreation(Minecraft minecraft0, CreateWorldScreen createWorldScreen1, Lifecycle lifecycle2, Runnable runnable3, boolean boolean4) {
        BooleanConsumer $$5 = p_233154_ -> {
            if (p_233154_) {
                runnable3.run();
            } else {
                minecraft0.setScreen(createWorldScreen1);
            }
        };
        if (boolean4 || lifecycle2 == Lifecycle.stable()) {
            runnable3.run();
        } else if (lifecycle2 == Lifecycle.experimental()) {
            minecraft0.setScreen(new ConfirmScreen($$5, Component.translatable("selectWorld.warning.experimental.title"), Component.translatable("selectWorld.warning.experimental.question")));
        } else {
            minecraft0.setScreen(new ConfirmScreen($$5, Component.translatable("selectWorld.warning.deprecated.title"), Component.translatable("selectWorld.warning.deprecated.question")));
        }
    }
}