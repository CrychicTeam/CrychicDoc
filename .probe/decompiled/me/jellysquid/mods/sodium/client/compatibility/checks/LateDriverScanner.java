package me.jellysquid.mods.sodium.client.compatibility.checks;

import me.jellysquid.mods.sodium.client.compatibility.environment.GLContextInfo;
import me.jellysquid.mods.sodium.client.compatibility.workarounds.nvidia.NvidiaDriverVersion;
import me.jellysquid.mods.sodium.client.gui.console.Console;
import me.jellysquid.mods.sodium.client.gui.console.message.MessageLevel;
import net.minecraft.Util;
import net.minecraft.network.chat.Component;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LateDriverScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-PostlaunchChecks");

    public static void onContextInitialized() {
        checkContextImplementation();
        if (isUsingPojavLauncher()) {
            Console.instance().logMessage(MessageLevel.SEVERE, Component.translatable("sodium.console.pojav_launcher"), 30.0);
            LOGGER.error("It appears that PojavLauncher is being used with an OpenGL compatibility layer. This will likely cause severe performance issues, graphical issues, and crashes when used with Embeddium. This configuration is not supported -- you are on your own!");
        }
    }

    private static void checkContextImplementation() {
        GLContextInfo driver = GLContextInfo.create();
        if (driver == null) {
            LOGGER.warn("Could not retrieve identifying strings for OpenGL implementation");
        } else {
            LOGGER.info("OpenGL Vendor: {}", driver.vendor());
            LOGGER.info("OpenGL Renderer: {}", driver.renderer());
            LOGGER.info("OpenGL Version: {}", driver.version());
            if (!isSupportedNvidiaDriver(driver)) {
                Console.instance().logMessage(MessageLevel.SEVERE, Component.translatable("sodium.console.broken_nvidia_driver"), 30.0);
                LOGGER.error("The NVIDIA graphics driver appears to be out of date. This will likely cause severe performance issues and crashes when used with Sodium. The graphics driver should be updated to the latest version (version 536.23 or newer).");
            }
            if (driver.vendor() != null && driver.vendor().contains("NVIDIA")) {
                LOGGER.warn("Enabling secondary workaround for NVIDIA threaded optimizations");
                GL11.glEnable(33346);
            }
        }
    }

    private static boolean isSupportedNvidiaDriver(GLContextInfo driver) {
        if (Util.getPlatform() != Util.OS.WINDOWS) {
            return true;
        } else {
            NvidiaDriverVersion version = NvidiaDriverVersion.tryParse(driver);
            return version != null ? !version.isWithinRange(new NvidiaDriverVersion(526, 47), new NvidiaDriverVersion(536, 23)) : true;
        }
    }

    private static boolean isUsingPojavLauncher() {
        if (System.getenv("POJAV_RENDERER") != null) {
            LOGGER.warn("Detected presence of environment variable POJAV_LAUNCHER, which seems to indicate we are running on Android");
            return true;
        } else {
            String librarySearchPaths = System.getProperty("java.library.path", null);
            if (librarySearchPaths != null) {
                for (String path : librarySearchPaths.split(":")) {
                    if (isKnownAndroidPathFragment(path)) {
                        LOGGER.warn("Found a library search path which seems to be hosted in an Android filesystem: {}", path);
                        return true;
                    }
                }
            }
            String workingDirectory = System.getProperty("user.home", null);
            if (workingDirectory != null && isKnownAndroidPathFragment(workingDirectory)) {
                LOGGER.warn("Working directory seems to be hosted in an Android filesystem: {}", workingDirectory);
            }
            return false;
        }
    }

    private static boolean isKnownAndroidPathFragment(String path) {
        return path.matches("/data/user/[0-9]+/net\\.kdt\\.pojavlaunch");
    }
}