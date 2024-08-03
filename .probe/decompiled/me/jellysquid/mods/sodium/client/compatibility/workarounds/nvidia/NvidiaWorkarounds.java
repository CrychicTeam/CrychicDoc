package me.jellysquid.mods.sodium.client.compatibility.workarounds.nvidia;

import me.jellysquid.mods.sodium.client.compatibility.environment.OSInfo;
import me.jellysquid.mods.sodium.client.platform.unix.Libc;
import me.jellysquid.mods.sodium.client.platform.windows.WindowsCommandLine;
import me.jellysquid.mods.sodium.client.platform.windows.api.Kernel32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NvidiaWorkarounds {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-NvidiaWorkarounds");

    public static void install() {
        LOGGER.warn("Applying workaround: Prevent the NVIDIA OpenGL driver from using broken optimizations (NVIDIA_THREADED_OPTIMIZATIONS)");
        try {
            switch(OSInfo.getOS()) {
                case WINDOWS:
                    WindowsCommandLine.setCommandLine("net.caffeinemc.sodium");
                    Kernel32.setEnvironmentVariable("SHIM_MCCOMPAT", "0x800000001");
                    break;
                case LINUX:
                    Libc.setEnvironmentVariable("__GL_THREADED_OPTIMIZATIONS", "0");
            }
        } catch (Throwable var1) {
            LOGGER.error("Failure while applying workarounds", var1);
            LOGGER.error("READ ME! The workarounds for the NVIDIA Graphics Driver did not apply correctly!");
            LOGGER.error("READ ME! You are very likely going to run into unexplained crashes and severe performance issues!");
            LOGGER.error("READ ME! Please see this issue for more information: https://github.com/CaffeineMC/sodium-fabric/issues/1816");
        }
    }

    public static void uninstall() {
        switch(OSInfo.getOS()) {
            case WINDOWS:
                WindowsCommandLine.resetCommandLine();
            case LINUX:
        }
    }
}