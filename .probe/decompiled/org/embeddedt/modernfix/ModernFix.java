package org.embeddedt.modernfix;

import java.lang.management.ManagementFactory;
import java.util.concurrent.ExecutorService;
import net.minecraft.SharedConstants;
import net.minecraft.Util;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.embeddedt.modernfix.command.ModernFixCommands;
import org.embeddedt.modernfix.core.ModernFixMixinPlugin;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.embeddedt.modernfix.resources.ReloadExecutor;
import org.embeddedt.modernfix.util.ClassInfoManager;

public class ModernFix {

    public static final Logger LOGGER = LogManager.getLogger("ModernFix");

    public static final String MODID = "modernfix";

    public static String NAME = "ModernFix";

    public static ModernFix INSTANCE;

    public static boolean runningFirstInjection = false;

    private static ExecutorService resourceReloadService = null;

    public static ExecutorService resourceReloadExecutor() {
        return resourceReloadService;
    }

    public ModernFix() {
        INSTANCE = this;
        if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.snapshot_easter_egg.NameChange") && !SharedConstants.getCurrentVersion().isStable()) {
            NAME = "PreemptiveFix";
        }
        ModernFixPlatformHooks.INSTANCE.onServerCommandRegister(ModernFixCommands::register);
    }

    public void onServerStarted() {
        if (ModernFixPlatformHooks.INSTANCE.isDedicatedServer()) {
            float gameStartTime = (float) ManagementFactory.getRuntimeMXBean().getUptime() / 1000.0F;
            if (ModernFixMixinPlugin.instance.isOptionEnabled("feature.measure_time.ServerLoad")) {
                LOGGER.warn("Dedicated server took " + gameStartTime + " seconds to load");
            }
            ModernFixPlatformHooks.INSTANCE.onLaunchComplete();
        }
        ClassInfoManager.clear();
    }

    public void onServerDead(MinecraftServer server) {
        try {
            for (ServerLevel level : server.getAllLevels()) {
                ChunkMap chunkMap = level.getChunkSource().chunkMap;
                chunkMap.updatingChunkMap.clear();
                chunkMap.visibleChunkMap.clear();
                chunkMap.pendingUnloads.clear();
            }
        } catch (RuntimeException var5) {
            LOGGER.error("Couldn't clear chunk data", var5);
        }
    }

    static {
        if (ModernFixMixinPlugin.instance.isOptionEnabled("perf.dedicated_reload_executor.ReloadExecutor")) {
            resourceReloadService = ReloadExecutor.createCustomResourceReloadExecutor();
        } else {
            resourceReloadService = Util.backgroundExecutor();
        }
    }
}