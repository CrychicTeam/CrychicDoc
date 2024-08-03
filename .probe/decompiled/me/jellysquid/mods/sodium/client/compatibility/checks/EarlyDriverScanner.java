package me.jellysquid.mods.sodium.client.compatibility.checks;

import me.jellysquid.mods.sodium.client.compatibility.environment.OSInfo;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterInfo;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterProbe;
import me.jellysquid.mods.sodium.client.compatibility.environment.probe.GraphicsAdapterVendor;
import me.jellysquid.mods.sodium.client.platform.MessageBox;
import me.jellysquid.mods.sodium.client.platform.windows.WindowsDriverStoreVersion;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class EarlyDriverScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-EarlyDriverScanner");

    private static final String CONSOLE_MESSAGE_TEMPLATE = "###ERROR_DESCRIPTION###\n\nFor more information, please see: ###HELP_URL###\n";

    private static final String INTEL_GEN7_DRIVER_MESSAGE = "The game failed to start because the currently installed Intel Graphics Driver is not compatible.\n\nInstalled version: ###CURRENT_DRIVER###\nRequired version: 15.33.53.5161 (or newer)\n\nYou must update your graphics card driver in order to continue.";

    private static final String INTEL_GEN7_DRIVER_HELP_URL = "https://github.com/CaffeineMC/sodium-fabric/wiki/Driver-Compatibility#windows-intel-gen7";

    public static void scanDrivers() {
        if (Configuration.WIN32_DRIVER_INTEL_GEN7) {
            WindowsDriverStoreVersion installedVersion = findBrokenIntelGen7GraphicsDriver();
            if (installedVersion != null) {
                showUnsupportedDriverMessageBox("The game failed to start because the currently installed Intel Graphics Driver is not compatible.\n\nInstalled version: ###CURRENT_DRIVER###\nRequired version: 15.33.53.5161 (or newer)\n\nYou must update your graphics card driver in order to continue.".replace("###CURRENT_DRIVER###", installedVersion.getFriendlyString()), "https://github.com/CaffeineMC/sodium-fabric/wiki/Driver-Compatibility#windows-intel-gen7");
            }
        }
    }

    private static void showUnsupportedDriverMessageBox(String message, String url) {
        LOGGER.error("###ERROR_DESCRIPTION###\n\nFor more information, please see: ###HELP_URL###\n".replace("###ERROR_DESCRIPTION###", message).replace("###HELP_URL###", url));
        MessageBox.showMessageBox(null, MessageBox.IconType.ERROR, "Embeddium Renderer - Unsupported Driver", message, url);
        System.exit(1);
    }

    @Nullable
    private static WindowsDriverStoreVersion findBrokenIntelGen7GraphicsDriver() {
        if (OSInfo.getOS() != OSInfo.OS.WINDOWS) {
            return null;
        } else {
            for (GraphicsAdapterInfo adapter : GraphicsAdapterProbe.getAdapters()) {
                if (adapter.vendor() == GraphicsAdapterVendor.INTEL) {
                    try {
                        WindowsDriverStoreVersion version = WindowsDriverStoreVersion.parse(adapter.version());
                        if (version.driverModel() == 10 && version.featureLevel() == 18 && version.major() == 10) {
                            if (version.minor() >= 5161) {
                                return null;
                            }
                            return version;
                        }
                    } catch (WindowsDriverStoreVersion.ParseException var3) {
                    }
                }
            }
            return null;
        }
    }
}