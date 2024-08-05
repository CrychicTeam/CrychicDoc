package net.minecraft.gametest.framework;

import com.google.common.base.Stopwatch;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.ServicesKeySet;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Lifecycle;
import java.net.Proxy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.BooleanSupplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.SystemReport;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.BlockPos;
import net.minecraft.core.LayeredRegistryAccess;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.RegistryLayer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldLoader;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.PlayerList;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.world.Difficulty;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.DataPackConfig;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.PrimaryLevelData;
import org.slf4j.Logger;

public class GameTestServer extends MinecraftServer {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int PROGRESS_REPORT_INTERVAL = 20;

    private static final Services NO_SERVICES = new Services(null, ServicesKeySet.EMPTY, null, null);

    private final List<GameTestBatch> testBatches;

    private final BlockPos spawnPos;

    private static final GameRules TEST_GAME_RULES = Util.make(new GameRules(), p_177615_ -> {
        p_177615_.getRule(GameRules.RULE_DOMOBSPAWNING).set(false, null);
        p_177615_.getRule(GameRules.RULE_WEATHER_CYCLE).set(false, null);
    });

    private static final WorldOptions WORLD_OPTIONS = new WorldOptions(0L, false, false);

    @Nullable
    private MultipleTestTracker testTracker;

    public static GameTestServer create(Thread thread0, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess1, PackRepository packRepository2, Collection<GameTestBatch> collectionGameTestBatch3, BlockPos blockPos4) {
        if (collectionGameTestBatch3.isEmpty()) {
            throw new IllegalArgumentException("No test batches were given!");
        } else {
            packRepository2.reload();
            WorldDataConfiguration $$5 = new WorldDataConfiguration(new DataPackConfig(new ArrayList(packRepository2.getAvailableIds()), List.of()), FeatureFlags.REGISTRY.allFlags());
            LevelSettings $$6 = new LevelSettings("Test Level", GameType.CREATIVE, false, Difficulty.NORMAL, true, TEST_GAME_RULES, $$5);
            WorldLoader.PackConfig $$7 = new WorldLoader.PackConfig(packRepository2, $$5, false, true);
            WorldLoader.InitConfig $$8 = new WorldLoader.InitConfig($$7, Commands.CommandSelection.DEDICATED, 4);
            try {
                LOGGER.debug("Starting resource loading");
                Stopwatch $$9 = Stopwatch.createStarted();
                WorldStem $$10 = (WorldStem) Util.blockUntilDone(p_248045_ -> WorldLoader.load($$8, p_258205_ -> {
                    Registry<LevelStem> $$2 = new MappedRegistry<>(Registries.LEVEL_STEM, Lifecycle.stable()).freeze();
                    WorldDimensions.Complete $$3 = p_258205_.datapackWorldgen().m_175515_(Registries.WORLD_PRESET).getHolderOrThrow(WorldPresets.FLAT).value().createWorldDimensions().bake($$2);
                    return new WorldLoader.DataLoadOutput<>(new PrimaryLevelData($$6, WORLD_OPTIONS, $$3.specialWorldProperty(), $$3.lifecycle()), $$3.dimensionsRegistryAccess());
                }, WorldStem::new, Util.backgroundExecutor(), p_248045_)).get();
                $$9.stop();
                LOGGER.debug("Finished resource loading after {} ms", $$9.elapsed(TimeUnit.MILLISECONDS));
                return new GameTestServer(thread0, levelStorageSourceLevelStorageAccess1, packRepository2, $$10, collectionGameTestBatch3, blockPos4);
            } catch (Exception var11) {
                LOGGER.warn("Failed to load vanilla datapack, bit oops", var11);
                System.exit(-1);
                throw new IllegalStateException();
            }
        }
    }

    private GameTestServer(Thread thread0, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess1, PackRepository packRepository2, WorldStem worldStem3, Collection<GameTestBatch> collectionGameTestBatch4, BlockPos blockPos5) {
        super(thread0, levelStorageSourceLevelStorageAccess1, packRepository2, worldStem3, Proxy.NO_PROXY, DataFixers.getDataFixer(), NO_SERVICES, LoggerChunkProgressListener::new);
        this.testBatches = Lists.newArrayList(collectionGameTestBatch4);
        this.spawnPos = blockPos5;
    }

    @Override
    public boolean initServer() {
        this.m_129823_(new PlayerList(this, this.m_247573_(), this.f_129745_, 1) {
        });
        this.m_130006_();
        ServerLevel $$0 = this.m_129783_();
        $$0.setDefaultSpawnPos(this.spawnPos, 0.0F);
        int $$1 = 20000000;
        $$0.setWeatherParameters(20000000, 20000000, false, false);
        LOGGER.info("Started game test server");
        return true;
    }

    @Override
    public void tickServer(BooleanSupplier booleanSupplier0) {
        super.tickServer(booleanSupplier0);
        ServerLevel $$1 = this.m_129783_();
        if (!this.haveTestsStarted()) {
            this.startTests($$1);
        }
        if ($$1.m_46467_() % 20L == 0L) {
            LOGGER.info(this.testTracker.getProgressBar());
        }
        if (this.testTracker.isDone()) {
            this.m_7570_(false);
            LOGGER.info(this.testTracker.getProgressBar());
            GlobalTestReporter.finish();
            LOGGER.info("========= {} GAME TESTS COMPLETE ======================", this.testTracker.getTotalCount());
            if (this.testTracker.hasFailedRequired()) {
                LOGGER.info("{} required tests failed :(", this.testTracker.getFailedRequiredCount());
                this.testTracker.getFailedRequired().forEach(p_206615_ -> LOGGER.info("   - {}", p_206615_.getTestName()));
            } else {
                LOGGER.info("All {} required tests passed :)", this.testTracker.getTotalCount());
            }
            if (this.testTracker.hasFailedOptional()) {
                LOGGER.info("{} optional tests failed", this.testTracker.getFailedOptionalCount());
                this.testTracker.getFailedOptional().forEach(p_206613_ -> LOGGER.info("   - {}", p_206613_.getTestName()));
            }
            LOGGER.info("====================================================");
        }
    }

    @Override
    public void waitUntilNextTick() {
        this.m_18699_();
    }

    @Override
    public SystemReport fillServerSystemReport(SystemReport systemReport0) {
        systemReport0.setDetail("Type", "Game test server");
        return systemReport0;
    }

    @Override
    public void onServerExit() {
        super.onServerExit();
        LOGGER.info("Game test server shutting down");
        System.exit(this.testTracker.getFailedRequiredCount());
    }

    @Override
    public void onServerCrash(CrashReport crashReport0) {
        super.onServerCrash(crashReport0);
        LOGGER.error("Game test server crashed\n{}", crashReport0.getFriendlyReport());
        System.exit(1);
    }

    private void startTests(ServerLevel serverLevel0) {
        Collection<GameTestInfo> $$1 = GameTestRunner.runTestBatches(this.testBatches, new BlockPos(0, -60, 0), Rotation.NONE, serverLevel0, GameTestTicker.SINGLETON, 8);
        this.testTracker = new MultipleTestTracker($$1);
        LOGGER.info("{} tests are now running!", this.testTracker.getTotalCount());
    }

    private boolean haveTestsStarted() {
        return this.testTracker != null;
    }

    @Override
    public boolean isHardcore() {
        return false;
    }

    @Override
    public int getOperatorUserPermissionLevel() {
        return 0;
    }

    @Override
    public int getFunctionCompilationLevel() {
        return 4;
    }

    @Override
    public boolean shouldRconBroadcast() {
        return false;
    }

    @Override
    public boolean isDedicatedServer() {
        return false;
    }

    @Override
    public int getRateLimitPacketsPerSecond() {
        return 0;
    }

    @Override
    public boolean isEpollEnabled() {
        return false;
    }

    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }

    @Override
    public boolean isPublished() {
        return false;
    }

    @Override
    public boolean shouldInformAdmins() {
        return false;
    }

    @Override
    public boolean isSingleplayerOwner(GameProfile gameProfile0) {
        return false;
    }
}