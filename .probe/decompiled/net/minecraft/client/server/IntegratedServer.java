package net.minecraft.client.server;

import com.google.common.base.MoreObjects;
import com.google.common.collect.Lists;
import com.mojang.authlib.GameProfile;
import com.mojang.logging.LogUtils;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.CrashReport;
import net.minecraft.SharedConstants;
import net.minecraft.SystemReport;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.Services;
import net.minecraft.server.WorldStem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.level.progress.ChunkProgressListenerFactory;
import net.minecraft.server.packs.repository.PackRepository;
import net.minecraft.stats.Stats;
import net.minecraft.util.ModCheck;
import net.minecraft.util.profiling.ProfilerFiller;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.storage.LevelStorageSource;
import org.slf4j.Logger;

public class IntegratedServer extends MinecraftServer {

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final int MIN_SIM_DISTANCE = 2;

    private final Minecraft minecraft;

    private boolean paused = true;

    private int publishedPort = -1;

    @Nullable
    private GameType publishedGameType;

    @Nullable
    private LanServerPinger lanPinger;

    @Nullable
    private UUID uuid;

    private int previousSimulationDistance = 0;

    public IntegratedServer(Thread thread0, Minecraft minecraft1, LevelStorageSource.LevelStorageAccess levelStorageSourceLevelStorageAccess2, PackRepository packRepository3, WorldStem worldStem4, Services services5, ChunkProgressListenerFactory chunkProgressListenerFactory6) {
        super(thread0, levelStorageSourceLevelStorageAccess2, packRepository3, worldStem4, minecraft1.getProxy(), minecraft1.getFixerUpper(), services5, chunkProgressListenerFactory6);
        this.m_236740_(minecraft1.getUser().getGameProfile());
        this.m_129975_(minecraft1.isDemo());
        this.m_129823_(new IntegratedPlayerList(this, this.m_247573_(), this.f_129745_));
        this.minecraft = minecraft1;
    }

    @Override
    public boolean initServer() {
        LOGGER.info("Starting integrated minecraft server version {}", SharedConstants.getCurrentVersion().getName());
        this.m_129985_(true);
        this.m_129997_(true);
        this.m_129999_(true);
        this.m_129793_();
        this.m_130006_();
        GameProfile $$0 = this.m_236731_();
        String $$1 = this.m_129910_().getLevelName();
        this.m_129989_($$0 != null ? $$0.getName() + " - " + $$1 : $$1);
        return true;
    }

    @Override
    public void tickServer(BooleanSupplier booleanSupplier0) {
        boolean $$1 = this.paused;
        this.paused = Minecraft.getInstance().isPaused();
        ProfilerFiller $$2 = this.m_129905_();
        if (!$$1 && this.paused) {
            $$2.push("autoSave");
            LOGGER.info("Saving and pausing game...");
            this.m_195514_(false, false, false);
            $$2.pop();
        }
        boolean $$3 = Minecraft.getInstance().getConnection() != null;
        if ($$3 && this.paused) {
            this.tickPaused();
        } else {
            if ($$1 && !this.paused) {
                this.m_276350_();
            }
            super.tickServer(booleanSupplier0);
            int $$4 = Math.max(2, this.minecraft.options.renderDistance().get());
            if ($$4 != this.m_6846_().getViewDistance()) {
                LOGGER.info("Changing view distance to {}, from {}", $$4, this.m_6846_().getViewDistance());
                this.m_6846_().setViewDistance($$4);
            }
            int $$5 = Math.max(2, this.minecraft.options.simulationDistance().get());
            if ($$5 != this.previousSimulationDistance) {
                LOGGER.info("Changing simulation distance to {}, from {}", $$5, this.previousSimulationDistance);
                this.m_6846_().setSimulationDistance($$5);
                this.previousSimulationDistance = $$5;
            }
        }
    }

    private void tickPaused() {
        for (ServerPlayer $$0 : this.m_6846_().getPlayers()) {
            $$0.m_36220_(Stats.TOTAL_WORLD_TIME);
        }
    }

    @Override
    public boolean shouldRconBroadcast() {
        return true;
    }

    @Override
    public boolean shouldInformAdmins() {
        return true;
    }

    @Override
    public File getServerDirectory() {
        return this.minecraft.gameDirectory;
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
    public void onServerCrash(CrashReport crashReport0) {
        this.minecraft.delayCrashRaw(crashReport0);
    }

    @Override
    public SystemReport fillServerSystemReport(SystemReport systemReport0) {
        systemReport0.setDetail("Type", "Integrated Server (map_client.txt)");
        systemReport0.setDetail("Is Modded", (Supplier<String>) (() -> this.getModdedStatus().fullDescription()));
        systemReport0.setDetail("Launched Version", this.minecraft::m_91388_);
        return systemReport0;
    }

    @Override
    public ModCheck getModdedStatus() {
        return Minecraft.checkModStatus().merge(super.getModdedStatus());
    }

    @Override
    public boolean publishServer(@Nullable GameType gameType0, boolean boolean1, int int2) {
        try {
            this.minecraft.prepareForMultiplayer();
            this.minecraft.getProfileKeyPairManager().prepareKeyPair().thenAcceptAsync(p_263550_ -> p_263550_.ifPresent(p_263549_ -> {
                ClientPacketListener $$1 = this.minecraft.getConnection();
                if ($$1 != null) {
                    $$1.setKeyPair(p_263549_);
                }
            }), this.minecraft);
            this.m_129919_().startTcpServerListener(null, int2);
            LOGGER.info("Started serving on {}", int2);
            this.publishedPort = int2;
            this.lanPinger = new LanServerPinger(this.m_129916_(), int2 + "");
            this.lanPinger.start();
            this.publishedGameType = gameType0;
            this.m_6846_().setAllowCheatsForAllPlayers(boolean1);
            int $$3 = this.m_129944_(this.minecraft.player.m_36316_());
            this.minecraft.player.setPermissionLevel($$3);
            for (ServerPlayer $$4 : this.m_6846_().getPlayers()) {
                this.m_129892_().sendCommands($$4);
            }
            return true;
        } catch (IOException var7) {
            return false;
        }
    }

    @Override
    public void stopServer() {
        super.stopServer();
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }

    @Override
    public void halt(boolean boolean0) {
        this.m_18709_(() -> {
            for (ServerPlayer $$1 : Lists.newArrayList(this.m_6846_().getPlayers())) {
                if (!$$1.m_20148_().equals(this.uuid)) {
                    this.m_6846_().remove($$1);
                }
            }
        });
        super.halt(boolean0);
        if (this.lanPinger != null) {
            this.lanPinger.interrupt();
            this.lanPinger = null;
        }
    }

    @Override
    public boolean isPublished() {
        return this.publishedPort > -1;
    }

    @Override
    public int getPort() {
        return this.publishedPort;
    }

    @Override
    public void setDefaultGameType(GameType gameType0) {
        super.setDefaultGameType(gameType0);
        this.publishedGameType = null;
    }

    @Override
    public boolean isCommandBlockEnabled() {
        return true;
    }

    @Override
    public int getOperatorUserPermissionLevel() {
        return 2;
    }

    @Override
    public int getFunctionCompilationLevel() {
        return 2;
    }

    public void setUUID(UUID uUID0) {
        this.uuid = uUID0;
    }

    @Override
    public boolean isSingleplayerOwner(GameProfile gameProfile0) {
        return this.m_236731_() != null && gameProfile0.getName().equalsIgnoreCase(this.m_236731_().getName());
    }

    @Override
    public int getScaledTrackingDistance(int int0) {
        return (int) (this.minecraft.options.entityDistanceScaling().get() * (double) int0);
    }

    @Override
    public boolean forceSynchronousWrites() {
        return this.minecraft.options.syncWrites;
    }

    @Nullable
    @Override
    public GameType getForcedGameType() {
        return this.isPublished() ? (GameType) MoreObjects.firstNonNull(this.publishedGameType, this.f_129749_.getGameType()) : null;
    }
}