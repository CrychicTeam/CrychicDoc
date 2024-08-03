package net.minecraft.server.dedicated;

import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.datafixers.DataFixer;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Writer;
import java.net.InetAddress;
import java.net.Proxy;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.DefaultUncaughtExceptionHandler;
import net.minecraft.DefaultUncaughtExceptionHandlerWithName;
import net.minecraft.SharedConstants;
import net.minecraft.SystemReport;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.BlockPos;
import net.minecraft.server.ConsoleInput;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerInterface;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.gui.MinecraftServerGui;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.network.TextFilter;
import net.minecraft.server.network.TextFilterClient;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.server.players.GameProfileCache;
import net.minecraft.server.players.OldUsersConverter;
import net.minecraft.server.rcon.RconConsoleSource;
import net.minecraft.server.rcon.thread.QueryThreadGs4;
import net.minecraft.server.rcon.thread.RconThread;
import net.minecraft.util.Mth;
import net.minecraft.util.monitoring.jmx.MinecraftServerStatistics;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.GameRules;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.SkullBlockEntity;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;

public class DedicatedServer extends MinecraftServer implements ServerInterface {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final int CONVERSION_RETRY_DELAY_MS = 5000;

    private static final int CONVERSION_RETRIES = 2;

    private final List<ConsoleInput> consoleInput = Collections.synchronizedList(Lists.newArrayList());

    @Nullable
    private QueryThreadGs4 queryThreadGs4;

    private final RconConsoleSource rconConsoleSource;

    @Nullable
    private RconThread rconThread;

    private final DedicatedServerSettings settings;

    @Nullable
    private MinecraftServerGui gui;

    @Nullable
    private final TextFilterClient textFilterClient;

    public DedicatedServer(Thread thread0, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess1, PackRepository packRepository2, WorldStem worldStem3, DedicatedServerSettings dedicatedServerSettings4, DataFixer dataFixer5, Services services6, ChunkProgressListenerFactory chunkProgressListenerFactory7) {
        super(thread0, levelStorageSourceLevelStorageAccess1, packRepository2, worldStem3, Proxy.NO_PROXY, dataFixer5, services6, chunkProgressListenerFactory7);
        this.settings = dedicatedServerSettings4;
        this.rconConsoleSource = new RconConsoleSource(this);
        this.textFilterClient = TextFilterClient.createFromConfig(dedicatedServerSettings4.getProperties().textFilteringConfig);
    }

    @Override
    public boolean initServer() throws IOException {
        Thread $$0 = new Thread("Server console handler") {

            public void run() {
                BufferedReader $$0 = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8));
                String $$1;
                try {
                    while (!DedicatedServer.this.m_129918_() && DedicatedServer.this.m_130010_() && ($$1 = $$0.readLine()) != null) {
                        DedicatedServer.this.handleConsoleInput($$1, DedicatedServer.this.m_129893_());
                    }
                } catch (IOException var4) {
                    DedicatedServer.LOGGER.error("Exception handling console input", var4);
                }
            }
        };
        $$0.setDaemon(true);
        $$0.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandler(LOGGER));
        $$0.start();
        LOGGER.info("Starting minecraft server version {}", SharedConstants.getCurrentVersion().getName());
        if (Runtime.getRuntime().maxMemory() / 1024L / 1024L < 512L) {
            LOGGER.warn("To start the server with more ram, launch it as \"java -Xmx1024M -Xms1024M -jar minecraft_server.jar\"");
        }
        LOGGER.info("Loading properties");
        DedicatedServerProperties $$1 = this.settings.getProperties();
        if (this.m_129792_()) {
            this.m_129913_("127.0.0.1");
        } else {
            this.m_129985_($$1.onlineMode);
            this.m_129993_($$1.preventProxyConnections);
            this.m_129913_($$1.serverIp);
        }
        this.m_129997_($$1.pvp);
        this.m_129999_($$1.allowFlight);
        this.m_129989_($$1.motd);
        super.setPlayerIdleTimeout($$1.playerIdleTimeout.get());
        this.m_130004_($$1.enforceWhitelist);
        this.f_129749_.setGameType($$1.gamemode);
        LOGGER.info("Default game type: {}", $$1.gamemode);
        InetAddress $$2 = null;
        if (!this.m_130009_().isEmpty()) {
            $$2 = InetAddress.getByName(this.m_130009_());
        }
        if (this.m_7010_() < 0) {
            this.m_129801_($$1.serverPort);
        }
        this.m_129793_();
        LOGGER.info("Starting Minecraft server on {}:{}", this.m_130009_().isEmpty() ? "*" : this.m_130009_(), this.m_7010_());
        try {
            this.m_129919_().startTcpServerListener($$2, this.m_7010_());
        } catch (IOException var10) {
            LOGGER.warn("**** FAILED TO BIND TO PORT!");
            LOGGER.warn("The exception was: {}", var10.toString());
            LOGGER.warn("Perhaps a server is already running on that port?");
            return false;
        }
        if (!this.m_129797_()) {
            LOGGER.warn("**** SERVER IS RUNNING IN OFFLINE/INSECURE MODE!");
            LOGGER.warn("The server will make no attempt to authenticate usernames. Beware.");
            LOGGER.warn("While this makes the game possible to play without internet access, it also opens up the ability for hackers to connect with any username they choose.");
            LOGGER.warn("To change this, set \"online-mode\" to \"true\" in the server.properties file.");
        }
        if (this.convertOldUsers()) {
            this.m_129927_().save();
        }
        if (!OldUsersConverter.serverReadyAfterUserconversion(this)) {
            return false;
        } else {
            this.m_129823_(new DedicatedPlayerList(this, this.m_247573_(), this.f_129745_));
            long $$4 = Util.getNanos();
            SkullBlockEntity.setup(this.f_236721_, this);
            GameProfileCache.setUsesAuthentication(this.m_129797_());
            LOGGER.info("Preparing level \"{}\"", this.getLevelIdName());
            this.m_130006_();
            long $$5 = Util.getNanos() - $$4;
            String $$6 = String.format(Locale.ROOT, "%.3fs", (double) $$5 / 1.0E9);
            LOGGER.info("Done ({})! For help, type \"help\"", $$6);
            if ($$1.announcePlayerAchievements != null) {
                this.m_129900_().getRule(GameRules.RULE_ANNOUNCE_ADVANCEMENTS).set($$1.announcePlayerAchievements, this);
            }
            if ($$1.enableQuery) {
                LOGGER.info("Starting GS4 status listener");
                this.queryThreadGs4 = QueryThreadGs4.create(this);
            }
            if ($$1.enableRcon) {
                LOGGER.info("Starting remote control listener");
                this.rconThread = RconThread.create(this);
            }
            if (this.getMaxTickLength() > 0L) {
                Thread $$7 = new Thread(new ServerWatchdog(this));
                $$7.setUncaughtExceptionHandler(new DefaultUncaughtExceptionHandlerWithName(LOGGER));
                $$7.setName("Server Watchdog");
                $$7.setDaemon(true);
                $$7.start();
            }
            if ($$1.enableJmxMonitoring) {
                MinecraftServerStatistics.registerJmxMonitoring(this);
                LOGGER.info("JMX monitoring enabled");
            }
            return true;
        }
    }

    @Override
    public boolean isSpawningAnimals() {
        return this.getProperties().spawnAnimals && super.isSpawningAnimals();
    }

    @Override
    public boolean isSpawningMonsters() {
        return this.settings.getProperties().spawnMonsters && super.isSpawningMonsters();
    }

    @Override
    public boolean areNpcsEnabled() {
        return this.settings.getProperties().spawnNpcs && super.areNpcsEnabled();
    }

    @Override
    public DedicatedServerProperties getProperties() {
        return this.settings.getProperties();
    }

    @Override
    public void forceDifficulty() {
        this.m_129827_(this.getProperties().difficulty, true);
    }

    @Override
    public boolean isHardcore() {
        return this.getProperties().hardcore;
    }

    @Override
    public SystemReport fillServerSystemReport(SystemReport systemReport0) {
        systemReport0.setDetail("Is Modded", (Supplier<String>) (() -> this.m_183471_().fullDescription()));
        systemReport0.setDetail("Type", (Supplier<String>) (() -> "Dedicated Server (map_server.txt)"));
        return systemReport0;
    }

    @Override
    public void dumpServerProperties(Path path0) throws IOException {
        DedicatedServerProperties $$1 = this.getProperties();
        Writer $$2 = Files.newBufferedWriter(path0);
        try {
            $$2.write(String.format(Locale.ROOT, "sync-chunk-writes=%s%n", $$1.syncChunkWrites));
            $$2.write(String.format(Locale.ROOT, "gamemode=%s%n", $$1.gamemode));
            $$2.write(String.format(Locale.ROOT, "spawn-monsters=%s%n", $$1.spawnMonsters));
            $$2.write(String.format(Locale.ROOT, "entity-broadcast-range-percentage=%d%n", $$1.entityBroadcastRangePercentage));
            $$2.write(String.format(Locale.ROOT, "max-world-size=%d%n", $$1.maxWorldSize));
            $$2.write(String.format(Locale.ROOT, "spawn-npcs=%s%n", $$1.spawnNpcs));
            $$2.write(String.format(Locale.ROOT, "view-distance=%d%n", $$1.viewDistance));
            $$2.write(String.format(Locale.ROOT, "simulation-distance=%d%n", $$1.simulationDistance));
            $$2.write(String.format(Locale.ROOT, "spawn-animals=%s%n", $$1.spawnAnimals));
            $$2.write(String.format(Locale.ROOT, "generate-structures=%s%n", $$1.worldOptions.generateStructures()));
            $$2.write(String.format(Locale.ROOT, "use-native=%s%n", $$1.useNativeTransport));
            $$2.write(String.format(Locale.ROOT, "rate-limit=%d%n", $$1.rateLimitPacketsPerSecond));
        } catch (Throwable var7) {
            if ($$2 != null) {
                try {
                    $$2.close();
                } catch (Throwable var6) {
                    var7.addSuppressed(var6);
                }
            }
            throw var7;
        }
        if ($$2 != null) {
            $$2.close();
        }
    }

    @Override
    public void onServerExit() {
        if (this.textFilterClient != null) {
            this.textFilterClient.close();
        }
        if (this.gui != null) {
            this.gui.close();
        }
        if (this.rconThread != null) {
            this.rconThread.stop();
        }
        if (this.queryThreadGs4 != null) {
            this.queryThreadGs4.m_7530_();
        }
    }

    @Override
    public void tickChildren(BooleanSupplier booleanSupplier0) {
        super.tickChildren(booleanSupplier0);
        this.handleConsoleInputs();
    }

    @Override
    public boolean isNetherEnabled() {
        return this.getProperties().allowNether;
    }

    public void handleConsoleInput(String string0, CommandSourceStack commandSourceStack1) {
        this.consoleInput.add(new ConsoleInput(string0, commandSourceStack1));
    }

    public void handleConsoleInputs() {
        while (!this.consoleInput.isEmpty()) {
            ConsoleInput $$0 = (ConsoleInput) this.consoleInput.remove(0);
            this.m_129892_().performPrefixedCommand($$0.source, $$0.msg);
        }
    }

    @Override
    public boolean isDedicatedServer() {
        return true;
    }

    @Override
    public int getRateLimitPacketsPerSecond() {
        return this.getProperties().rateLimitPacketsPerSecond;
    }

    @Override
    public boolean isEpollEnabled() {
        return this.getProperties().useNativeTransport;
    }

    public DedicatedPlayerList getPlayerList() {
        return (DedicatedPlayerList) super.getPlayerList();
    }

    @Override
    public boolean isPublished() {
        return true;
    }

    @Override
    public String getServerIp() {
        return this.m_130009_();
    }

    @Override
    public int getServerPort() {
        return this.m_7010_();
    }

    @Override
    public String getServerName() {
        return this.m_129916_();
    }

    public void showGui() {
        if (this.gui == null) {
            this.gui = MinecraftServerGui.showFrameFor(this);
        }
    }

    @Override
    public boolean hasGui() {
        return this.gui != null;
    }

    @Override
    public boolean isCommandBlockEnabled() {
        return this.getProperties().enableCommandBlock;
    }

    @Override
    public int getSpawnProtectionRadius() {
        return this.getProperties().spawnProtection;
    }

    @Override
    public boolean isUnderSpawnProtection(ServerLevel serverLevel0, BlockPos blockPos1, Player player2) {
        if (serverLevel0.m_46472_() != Level.OVERWORLD) {
            return false;
        } else if (this.getPlayerList().m_11307_().m_11390_()) {
            return false;
        } else if (this.getPlayerList().m_11303_(player2.getGameProfile())) {
            return false;
        } else if (this.getSpawnProtectionRadius() <= 0) {
            return false;
        } else {
            BlockPos $$3 = serverLevel0.m_220360_();
            int $$4 = Mth.abs(blockPos1.m_123341_() - $$3.m_123341_());
            int $$5 = Mth.abs(blockPos1.m_123343_() - $$3.m_123343_());
            int $$6 = Math.max($$4, $$5);
            return $$6 <= this.getSpawnProtectionRadius();
        }
    }

    @Override
    public boolean repliesToStatus() {
        return this.getProperties().enableStatus;
    }

    @Override
    public boolean hidesOnlinePlayers() {
        return this.getProperties().hideOnlinePlayers;
    }

    @Override
    public int getOperatorUserPermissionLevel() {
        return this.getProperties().opPermissionLevel;
    }

    @Override
    public int getFunctionCompilationLevel() {
        return this.getProperties().functionPermissionLevel;
    }

    @Override
    public void setPlayerIdleTimeout(int int0) {
        super.setPlayerIdleTimeout(int0);
        this.settings.update(p_278927_ -> p_278927_.playerIdleTimeout.update(this.m_206579_(), int0));
    }

    @Override
    public boolean shouldRconBroadcast() {
        return this.getProperties().broadcastRconToOps;
    }

    @Override
    public boolean shouldInformAdmins() {
        return this.getProperties().broadcastConsoleToOps;
    }

    @Override
    public int getAbsoluteMaxWorldSize() {
        return this.getProperties().maxWorldSize;
    }

    @Override
    public int getCompressionThreshold() {
        return this.getProperties().networkCompressionThreshold;
    }

    @Override
    public boolean enforceSecureProfile() {
        DedicatedServerProperties $$0 = this.getProperties();
        return $$0.enforceSecureProfile && $$0.onlineMode && this.f_236721_.profileKeySignatureValidator() != null;
    }

    protected boolean convertOldUsers() {
        boolean $$0 = false;
        for (int $$1 = 0; !$$0 && $$1 <= 2; $$1++) {
            if ($$1 > 0) {
                LOGGER.warn("Encountered a problem while converting the user banlist, retrying in a few seconds");
                this.waitForRetry();
            }
            $$0 = OldUsersConverter.convertUserBanlist(this);
        }
        boolean $$2 = false;
        for (int var7 = 0; !$$2 && var7 <= 2; var7++) {
            if (var7 > 0) {
                LOGGER.warn("Encountered a problem while converting the ip banlist, retrying in a few seconds");
                this.waitForRetry();
            }
            $$2 = OldUsersConverter.convertIpBanlist(this);
        }
        boolean $$3 = false;
        for (int var8 = 0; !$$3 && var8 <= 2; var8++) {
            if (var8 > 0) {
                LOGGER.warn("Encountered a problem while converting the op list, retrying in a few seconds");
                this.waitForRetry();
            }
            $$3 = OldUsersConverter.convertOpsList(this);
        }
        boolean $$4 = false;
        for (int var9 = 0; !$$4 && var9 <= 2; var9++) {
            if (var9 > 0) {
                LOGGER.warn("Encountered a problem while converting the whitelist, retrying in a few seconds");
                this.waitForRetry();
            }
            $$4 = OldUsersConverter.convertWhiteList(this);
        }
        boolean $$5 = false;
        for (int var10 = 0; !$$5 && var10 <= 2; var10++) {
            if (var10 > 0) {
                LOGGER.warn("Encountered a problem while converting the player save files, retrying in a few seconds");
                this.waitForRetry();
            }
            $$5 = OldUsersConverter.convertPlayers(this);
        }
        return $$0 || $$2 || $$3 || $$4 || $$5;
    }

    private void waitForRetry() {
        try {
            Thread.sleep(5000L);
        } catch (InterruptedException var2) {
        }
    }

    public long getMaxTickLength() {
        return this.getProperties().maxTickTime;
    }

    @Override
    public int getMaxChainedNeighborUpdates() {
        return this.getProperties().maxChainedNeighborUpdates;
    }

    @Override
    public String getPluginNames() {
        return "";
    }

    @Override
    public String runCommand(String string0) {
        this.rconConsoleSource.prepareForCommand();
        this.m_18709_(() -> this.m_129892_().performPrefixedCommand(this.rconConsoleSource.createCommandSourceStack(), string0));
        return this.rconConsoleSource.getCommandResponse();
    }

    public void storeUsingWhiteList(boolean boolean0) {
        this.settings.update(p_278925_ -> p_278925_.whiteList.update(this.m_206579_(), boolean0));
    }

    @Override
    public void stopServer() {
        super.stopServer();
        Util.shutdownExecutors();
        SkullBlockEntity.clear();
    }

    @Override
    public boolean isSingleplayerOwner(GameProfile gameProfile0) {
        return false;
    }

    @Override
    public int getScaledTrackingDistance(int int0) {
        return this.getProperties().entityBroadcastRangePercentage * int0 / 100;
    }

    @Override
    public String getLevelIdName() {
        return this.f_129744_.getLevelId();
    }

    @Override
    public boolean forceSynchronousWrites() {
        return this.settings.getProperties().syncChunkWrites;
    }

    @Override
    public TextFilter createTextFilterForPlayer(ServerPlayer serverPlayer0) {
        return this.textFilterClient != null ? this.textFilterClient.createContext(serverPlayer0.m_36316_()) : TextFilter.DUMMY;
    }

    @Nullable
    @Override
    public GameType getForcedGameType() {
        return this.settings.getProperties().forceGameMode ? this.f_129749_.getGameType() : null;
    }

    @Override
    public Optional<MinecraftServer.ServerResourcePackInfo> getServerResourcePack() {
        return this.settings.getProperties().serverResourcePackInfo;
    }
}