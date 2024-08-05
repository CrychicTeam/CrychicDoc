package me.jellysquid.mods.sodium.client.compatibility.checks;

import com.mojang.blaze3d.platform.Window;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import me.jellysquid.mods.sodium.client.platform.MessageBox;
import me.jellysquid.mods.sodium.client.platform.windows.api.Kernel32;
import me.jellysquid.mods.sodium.client.platform.windows.api.version.LanguageCodePage;
import me.jellysquid.mods.sodium.client.platform.windows.api.version.Version;
import me.jellysquid.mods.sodium.client.platform.windows.api.version.VersionInfo;
import net.minecraft.client.Minecraft;
import net.minecraft.util.NativeModuleLister;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ModuleScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-Win32ModuleChecks");

    private static final String[] RTSS_HOOKS_MODULE_NAMES = new String[] { "RTSSHooks64.dll", "RTSSHooks.dll" };

    private static final Pattern RTSS_VERSION_PATTERN = Pattern.compile("^(?<x>\\d*)(, |\\.)(?<y>\\d*)(, |\\.)(?<z>\\d*)(, |\\.)(?<w>\\d*)$");

    public static void checkModules() {
        List<NativeModuleLister.NativeModuleInfo> modules;
        try {
            modules = NativeModuleLister.listModules();
        } catch (Throwable var2) {
            LOGGER.warn("Failed to scan the currently loaded modules", var2);
            return;
        }
        if (!modules.isEmpty()) {
            if (Configuration.WIN32_RTSS_HOOKS && isModuleLoaded(modules, RTSS_HOOKS_MODULE_NAMES)) {
                checkRTSSModules();
            }
        }
    }

    private static void checkRTSSModules() {
        LOGGER.warn("RivaTuner Statistics Server (RTSS) has injected into the process! Attempting to apply workarounds for compatibility...");
        String version = null;
        try {
            version = findRTSSModuleVersion();
        } catch (Throwable var2) {
            LOGGER.warn("Exception thrown while reading file version", var2);
        }
        if (version == null) {
            LOGGER.warn("Could not determine version of RivaTuner Statistics Server");
        } else {
            LOGGER.info("Detected RivaTuner Statistics Server version: {}", version);
        }
        if (version == null || !isRTSSCompatible(version)) {
            Window window = Minecraft.getInstance().getWindow();
            MessageBox.showMessageBox(window, MessageBox.IconType.ERROR, "Embeddium Renderer", "You appear to be using an older version of RivaTuner Statistics Server (RTSS) which is not compatible with Embeddium. You must either update to a newer version (7.3.4 and later) or close the RivaTuner Statistics Server application.\n\nFor more information on how to solve this problem, click the 'Help' button.", "https://github.com/CaffeineMC/sodium-fabric/wiki/Known-Issues#rtss-incompatible");
            throw new RuntimeException("RivaTuner Statistics Server (RTSS) is not compatible with Embeddium, see here for more details: https://github.com/CaffeineMC/sodium-fabric/wiki/Known-Issues#rtss-incompatible");
        }
    }

    private static boolean isRTSSCompatible(String version) {
        Matcher matcher = RTSS_VERSION_PATTERN.matcher(version);
        if (!matcher.matches()) {
            return false;
        } else {
            try {
                int x = Integer.parseInt(matcher.group("x"));
                int y = Integer.parseInt(matcher.group("y"));
                int z = Integer.parseInt(matcher.group("z"));
                return x > 7 || x == 7 && y > 3 || x == 7 && y == 3 && z >= 4;
            } catch (NumberFormatException var5) {
                LOGGER.warn("Invalid version string: {}", version);
                return false;
            }
        }
    }

    private static String findRTSSModuleVersion() {
        long module;
        try {
            module = Kernel32.getModuleHandleByNames(RTSS_HOOKS_MODULE_NAMES);
        } catch (Throwable var10) {
            LOGGER.warn("Failed to locate module", var10);
            return null;
        }
        String moduleFileName;
        try {
            moduleFileName = Kernel32.getModuleFileName(module);
        } catch (Throwable var9) {
            LOGGER.warn("Failed to get path of module", var9);
            return null;
        }
        Path modulePath = Path.of(moduleFileName);
        Path moduleDirectory = modulePath.getParent();
        LOGGER.info("Searching directory: {}", moduleDirectory);
        Path executablePath = moduleDirectory.resolve("RTSS.exe");
        if (!Files.exists(executablePath, new LinkOption[0])) {
            LOGGER.warn("Could not find executable: {}", executablePath);
            return null;
        } else {
            LOGGER.info("Parsing file: {}", executablePath);
            VersionInfo version = Version.getModuleFileVersion(executablePath.toAbsolutePath().toString());
            if (version == null) {
                LOGGER.warn("Couldn't find version structure");
                return null;
            } else {
                LanguageCodePage translation = version.queryEnglishTranslation();
                if (translation == null) {
                    LOGGER.warn("Couldn't find suitable translation");
                    return null;
                } else {
                    String fileVersion = version.queryValue("FileVersion", translation);
                    if (fileVersion == null) {
                        LOGGER.warn("Couldn't query file version");
                        return null;
                    } else {
                        return fileVersion;
                    }
                }
            }
        }
    }

    private static boolean isModuleLoaded(List<NativeModuleLister.NativeModuleInfo> modules, String[] names) {
        for (String name : names) {
            for (NativeModuleLister.NativeModuleInfo module : modules) {
                if (module.name.equalsIgnoreCase(name)) {
                    return true;
                }
            }
        }
        return false;
    }
}