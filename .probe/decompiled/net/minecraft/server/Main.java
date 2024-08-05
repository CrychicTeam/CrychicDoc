package net.minecraft.server;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.yggdrasil.YggdrasilAuthenticationService;
import com.mojang.datafixers.DataFixer;
import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.Lifecycle;
import java.awt.GraphicsEnvironment;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.net.Proxy;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.OptionSpec;
import joptsimple.util.PathConverter;
import joptsimple.util.PathProperties;
import net.minecraft.CrashReport;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.commands.Commands;
import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.obfuscate.DontObfuscate;
import net.minecraft.resources.RegistryOps;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.server.dedicated.DedicatedServerProperties;
import net.minecraft.server.dedicated.DedicatedServerSettings;
import net.minecraft.server.level.progress.LoggerChunkProgressListener;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.packs.repository.ServerPacksSource;
import net.minecraft.util.Mth;
import net.minecraft.util.datafix.DataFixers;
import net.minecraft.util.profiling.jfr.Environment;
import net.minecraft.util.profiling.jfr.JvmProfiler;
import net.minecraft.util.worldupdate.WorldUpgrader;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.LevelSettings;
import net.minecraft.world.level.WorldDataConfiguration;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.WorldDimensions;
import net.minecraft.world.level.levelgen.WorldOptions;
import net.minecraft.world.level.levelgen.presets.WorldPresets;
import net.minecraft.world.level.storage.LevelResource;
import net.minecraft.world.level.storage.LevelStorageSource;
import net.minecraft.world.level.storage.LevelSummary;
import net.minecraft.world.level.storage.PrimaryLevelData;
import net.minecraft.world.level.storage.WorldData;
import org.slf4j.Logger;

public class Main {

    private static final Logger LOGGER = LogUtils.getLogger();

    @DontObfuscate
    public static void main(String[] string0) {
        SharedConstants.tryDetectVersion();
        OptionParser $$1 = new OptionParser();
        OptionSpec<Void> $$2 = $$1.accepts("nogui");
        OptionSpec<Void> $$3 = $$1.accepts("initSettings", "Initializes 'server.properties' and 'eula.txt', then quits");
        OptionSpec<Void> $$4 = $$1.accepts("demo");
        OptionSpec<Void> $$5 = $$1.accepts("bonusChest");
        OptionSpec<Void> $$6 = $$1.accepts("forceUpgrade");
        OptionSpec<Void> $$7 = $$1.accepts("eraseCache");
        OptionSpec<Void> $$8 = $$1.accepts("safeMode", "Loads level with vanilla datapack only");
        OptionSpec<Void> $$9 = $$1.accepts("help").forHelp();
        OptionSpec<String> $$10 = $$1.accepts("singleplayer").withRequiredArg();
        OptionSpec<String> $$11 = $$1.accepts("universe").withRequiredArg().defaultsTo(".", new String[0]);
        OptionSpec<String> $$12 = $$1.accepts("world").withRequiredArg();
        OptionSpec<Integer> $$13 = $$1.accepts("port").withRequiredArg().ofType(Integer.class).defaultsTo(-1, new Integer[0]);
        OptionSpec<String> $$14 = $$1.accepts("serverId").withRequiredArg();
        OptionSpec<Void> $$15 = $$1.accepts("jfrProfile");
        OptionSpec<Path> $$16 = $$1.accepts("pidFile").withRequiredArg().withValuesConvertedBy(new PathConverter(new PathProperties[0]));
        OptionSpec<String> $$17 = $$1.nonOptions();
        try {
            OptionSet $$18 = $$1.parse(string0);
            if ($$18.has($$9)) {
                $$1.printHelpOn(System.err);
                return;
            }
            Path $$19 = (Path) $$18.valueOf($$16);
            if ($$19 != null) {
                writePidFile($$19);
            }
            CrashReport.preload();
            if ($$18.has($$15)) {
                JvmProfiler.INSTANCE.start(Environment.SERVER);
            }
            Bootstrap.bootStrap();
            Bootstrap.validate();
            Util.startTimerHackThread();
            Path $$20 = Paths.get("server.properties");
            DedicatedServerSettings $$21 = new DedicatedServerSettings($$20);
            $$21.forceSave();
            Path $$22 = Paths.get("eula.txt");
            Eula $$23 = new Eula($$22);
            if ($$18.has($$3)) {
                LOGGER.info("Initialized '{}' and '{}'", $$20.toAbsolutePath(), $$22.toAbsolutePath());
                return;
            }
            if (!$$23.hasAgreedToEULA()) {
                LOGGER.info("You need to agree to the EULA in order to run the server. Go to eula.txt for more info.");
                return;
            }
            File $$24 = new File((String) $$18.valueOf($$11));
            Services $$25 = Services.create(new YggdrasilAuthenticationService(Proxy.NO_PROXY), $$24);
            String $$26 = (String) Optional.ofNullable((String) $$18.valueOf($$12)).orElse($$21.getProperties().levelName);
            LevelStorageSource $$27 = LevelStorageSource.createDefault($$24.toPath());
            LevelStorageSource.LevelStorageAccess $$28 = $$27.validateAndCreateAccess($$26);
            LevelSummary $$29 = $$28.getSummary();
            if ($$29 != null) {
                if ($$29.requiresManualConversion()) {
                    LOGGER.info("This world must be opened in an older version (like 1.6.4) to be safely converted");
                    return;
                }
                if (!$$29.isCompatible()) {
                    LOGGER.info("This world was created by an incompatible version.");
                    return;
                }
            }
            boolean $$30 = $$18.has($$8);
            if ($$30) {
                LOGGER.warn("Safe mode active, only vanilla datapack will be loaded");
            }
            PackRepository $$31 = ServerPacksSource.createPackRepository($$28.getLevelPath(LevelResource.DATAPACK_DIR));
            WorldStem $$33;
            try {
                WorldLoader.InitConfig $$32 = loadOrCreateConfig($$21.getProperties(), $$28, $$30, $$31);
                $$33 = (WorldStem) Util.blockUntilDone(p_248086_ -> WorldLoader.load($$32, p_248079_ -> {
                    Registry<LevelStem> $$6x = p_248079_.datapackDimensions().m_175515_(Registries.LEVEL_STEM);
                    DynamicOps<Tag> $$7x = RegistryOps.create(NbtOps.INSTANCE, p_248079_.datapackWorldgen());
                    Pair<WorldData, WorldDimensions.Complete> $$8x = $$28.getDataTag($$7x, p_248079_.dataConfiguration(), $$6x, p_248079_.datapackWorldgen().m_211816_());
                    if ($$8x != null) {
                        return new WorldLoader.DataLoadOutput<>((WorldData) $$8x.getFirst(), ((WorldDimensions.Complete) $$8x.getSecond()).dimensionsRegistryAccess());
                    } else {
                        LevelSettings $$9x;
                        WorldOptions $$10x;
                        WorldDimensions $$11x;
                        if ($$18.has($$4)) {
                            $$9x = MinecraftServer.DEMO_SETTINGS;
                            $$10x = WorldOptions.DEMO_OPTIONS;
                            $$11x = WorldPresets.createNormalWorldDimensions(p_248079_.datapackWorldgen());
                        } else {
                            DedicatedServerProperties $$12x = $$21.getProperties();
                            $$9x = new LevelSettings($$12x.levelName, $$12x.gamemode, $$12x.hardcore, $$12x.difficulty, false, new GameRules(), p_248079_.dataConfiguration());
                            $$10x = $$18.has($$5) ? $$12x.worldOptions.withBonusChest(true) : $$12x.worldOptions;
                            $$11x = $$12x.createDimensions(p_248079_.datapackWorldgen());
                        }
                        WorldDimensions.Complete $$16x = $$11x.bake($$6x);
                        Lifecycle $$17x = $$16x.lifecycle().add(p_248079_.datapackWorldgen().m_211816_());
                        return new WorldLoader.DataLoadOutput<>(new PrimaryLevelData($$9x, $$10x, $$16x.specialWorldProperty(), $$17x), $$16x.dimensionsRegistryAccess());
                    }
                }, WorldStem::new, Util.backgroundExecutor(), p_248086_)).get();
            } catch (Exception var37) {
                LOGGER.warn("Failed to load datapacks, can't proceed with server load. You can either fix your datapacks or reset to vanilla with --safeMode", var37);
                return;
            }
            RegistryAccess.Frozen $$36 = $$33.registries().compositeAccess();
            if ($$18.has($$6)) {
                forceUpgrade($$28, DataFixers.getDataFixer(), $$18.has($$7), () -> true, $$36.m_175515_(Registries.LEVEL_STEM));
            }
            WorldData $$37 = $$33.worldData();
            $$28.saveDataTag($$36, $$37);
            final DedicatedServer $$38 = MinecraftServer.spin(p_236710_ -> {
                DedicatedServer $$13x = new DedicatedServer(p_236710_, $$28, $$31, $$33, $$21, DataFixers.getDataFixer(), $$25, LoggerChunkProgressListener::new);
                $$13x.m_236740_($$18.has($$10) ? new GameProfile(null, (String) $$18.valueOf($$10)) : null);
                $$13x.m_129801_((Integer) $$18.valueOf($$13));
                $$13x.m_129975_($$18.has($$4));
                $$13x.m_129948_((String) $$18.valueOf($$14));
                boolean $$14x = !$$18.has($$2) && !$$18.valuesOf($$17).contains("nogui");
                if ($$14x && !GraphicsEnvironment.isHeadless()) {
                    $$13x.showGui();
                }
                return $$13x;
            });
            Thread $$39 = new Thread("Server Shutdown Thread") {

                public void run() {
                    $$38.m_7570_(true);
                }
            };
            $$39.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
            Runtime.getRuntime().addShutdownHook($$39);
        } catch (Exception var38) {
            LOGGER.error(LogUtils.FATAL_MARKER, "Failed to start the minecraft server", var38);
        }
    }

    private static void writePidFile(Path path0) {
        try {
            long $$1 = ProcessHandle.current().pid();
            Files.writeString(path0, Long.toString($$1));
        } catch (IOException var3) {
            throw new UncheckedIOException(var3);
        }
    }

    private static WorldLoader.InitConfig loadOrCreateConfig(DedicatedServerProperties dedicatedServerProperties0, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess1, boolean boolean2, PackRepository packRepository3) {
        WorldDataConfiguration $$4 = levelStorageSourceLevelStorageAccess1.getDataConfiguration();
        WorldDataConfiguration $$6;
        boolean $$5;
        if ($$4 != null) {
            $$5 = false;
            $$6 = $$4;
        } else {
            $$5 = true;
            $$6 = new WorldDataConfiguration(dedicatedServerProperties0.initialDataPackConfiguration, FeatureFlags.DEFAULT_FLAGS);
        }
        WorldLoader.PackConfig $$9 = new WorldLoader.PackConfig(packRepository3, $$6, boolean2, $$5);
        return new WorldLoader.InitConfig($$9, Commands.CommandSelection.DEDICATED, dedicatedServerProperties0.functionPermissionLevel);
    }

    private static void forceUpgrade(LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess0, DataFixer dataFixer1, boolean boolean2, BooleanSupplier booleanSupplier3, Registry<LevelStem> registryLevelStem4) {
        LOGGER.info("Forcing world upgrade!");
        WorldUpgrader $$5 = new WorldUpgrader(levelStorageSourceLevelStorageAccess0, dataFixer1, registryLevelStem4, boolean2);
        Component $$6 = null;
        while (!$$5.isFinished()) {
            Component $$7 = $$5.getStatus();
            if ($$6 != $$7) {
                $$6 = $$7;
                LOGGER.info($$5.getStatus().getString());
            }
            int $$8 = $$5.getTotalChunks();
            if ($$8 > 0) {
                int $$9 = $$5.getConverted() + $$5.getSkipped();
                LOGGER.info("{}% completed ({} / {} chunks)...", new Object[] { Mth.floor((float) $$9 / (float) $$8 * 100.0F), $$9, $$8 });
            }
            if (!booleanSupplier3.getAsBoolean()) {
                $$5.cancel();
            } else {
                try {
                    Thread.sleep(1000L);
                } catch (InterruptedException var10) {
                }
            }
        }
    }
}