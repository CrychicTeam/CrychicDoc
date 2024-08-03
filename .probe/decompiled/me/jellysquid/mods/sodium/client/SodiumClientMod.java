package me.jellysquid.mods.sodium.client;

import java.io.IOException;
import me.jellysquid.mods.sodium.client.data.fingerprint.FingerprintMeasure;
import me.jellysquid.mods.sodium.client.data.fingerprint.HashedFingerprint;
import me.jellysquid.mods.sodium.client.gui.SodiumGameOptions;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.IExtensionPoint.DisplayTest;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.embeddedt.embeddium.render.ShaderModBridge;
import org.embeddedt.embeddium.taint.incompats.IncompatibleModManager;
import org.embeddedt.embeddium.taint.scanning.TaintDetector;
import org.embeddedt.embeddium.util.sodium.FlawlessFrames;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod("embeddium")
public class SodiumClientMod {

    public static final String MODID = "embeddium";

    public static final String MODNAME = "Embeddium";

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium");

    private static SodiumGameOptions CONFIG = loadConfig();

    private static String MOD_VERSION;

    public SodiumClientMod() {
        MOD_VERSION = ((ModContainer) ModList.get().getModContainerById("embeddium").get()).getModInfo().getVersion().toString();
        ModLoadingContext.get().registerExtensionPoint(DisplayTest.class, () -> new DisplayTest(() -> "OHNOES\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31\ud83d\ude31", (a, b) -> true));
        TaintDetector.init();
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onClientSetup);
        try {
            updateFingerprint();
        } catch (Throwable var2) {
            LOGGER.error("Failed to update fingerprint", var2);
        }
    }

    public void onClientSetup(FMLClientSetupEvent event) {
        IncompatibleModManager.checkMods(event);
        FlawlessFrames.onClientInitialization();
    }

    public static SodiumGameOptions options() {
        if (CONFIG == null) {
            throw new IllegalStateException("Config not yet available");
        } else {
            return CONFIG;
        }
    }

    public static Logger logger() {
        if (LOGGER == null) {
            throw new IllegalStateException("Logger not yet available");
        } else {
            return LOGGER;
        }
    }

    private static SodiumGameOptions loadConfig() {
        try {
            return SodiumGameOptions.load();
        } catch (Exception var2) {
            LOGGER.error("Failed to load configuration file", var2);
            LOGGER.error("Using default configuration file in read-only mode");
            SodiumGameOptions config = new SodiumGameOptions();
            config.setReadOnly();
            return config;
        }
    }

    public static void restoreDefaultOptions() {
        CONFIG = SodiumGameOptions.defaults();
        try {
            CONFIG.writeChanges();
        } catch (IOException var1) {
            throw new RuntimeException("Failed to write config file", var1);
        }
    }

    public static String getVersion() {
        if (MOD_VERSION == null) {
            throw new NullPointerException("Mod version hasn't been populated yet");
        } else {
            return MOD_VERSION;
        }
    }

    private static void updateFingerprint() {
        FingerprintMeasure current = FingerprintMeasure.create();
        if (current != null) {
            HashedFingerprint saved = null;
            try {
                saved = HashedFingerprint.loadFromDisk();
            } catch (Throwable var4) {
                LOGGER.error("Failed to load existing fingerprint", var4);
            }
            if (saved == null || !current.looselyMatches(saved)) {
                HashedFingerprint.writeToDisk(current.hashed());
                CONFIG.notifications.hasSeenDonationPrompt = false;
                CONFIG.notifications.hasClearedDonationButton = false;
                try {
                    CONFIG.writeChanges();
                } catch (IOException var3) {
                    LOGGER.error("Failed to update config file", var3);
                }
            }
        }
    }

    public static boolean canUseVanillaVertices() {
        return !options().performance.useCompactVertexFormat && !ShaderModBridge.areShadersEnabled();
    }

    public static boolean canApplyTranslucencySorting() {
        return options().performance.useTranslucentFaceSorting && !ShaderModBridge.isNvidiumEnabled();
    }
}