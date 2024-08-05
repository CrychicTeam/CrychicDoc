package me.jellysquid.mods.sodium.client.compatibility.environment.probe;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import me.jellysquid.mods.sodium.client.compatibility.environment.OSInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import oshi.SystemInfo;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.util.ExecutingCommand;

public class GraphicsAdapterProbe {

    private static final Logger LOGGER = LoggerFactory.getLogger("Embeddium-GraphicsAdapterProbe");

    private static List<GraphicsAdapterInfo> ADAPTERS;

    public static void findAdapters() {
        LOGGER.info("Searching for graphics cards...");
        List<GraphicsAdapterInfo> results = OSInfo.getOS() == OSInfo.OS.LINUX ? findAdaptersLinux() : findAdaptersCrossPlatform();
        if (results.isEmpty()) {
            LOGGER.warn("No graphics cards were found. Either you have no hardware devices supporting 3D acceleration, or something has gone terribly wrong!");
        }
        ADAPTERS = results;
    }

    public static List<GraphicsAdapterInfo> findAdaptersCrossPlatform() {
        SystemInfo systemInfo = new SystemInfo();
        HardwareAbstractionLayer hardwareInfo = systemInfo.getHardware();
        ArrayList<GraphicsAdapterInfo> results = new ArrayList();
        for (GraphicsCard graphicsCard : hardwareInfo.getGraphicsCards()) {
            GraphicsAdapterVendor vendor = GraphicsAdapterVendor.identifyVendorFromString(graphicsCard.getVendor());
            String name = graphicsCard.getName();
            String versionInfo = graphicsCard.getVersionInfo();
            GraphicsAdapterInfo info = new GraphicsAdapterInfo(vendor, name, versionInfo);
            results.add(info);
            LOGGER.info("Found graphics card: {}", info);
        }
        return results;
    }

    private static List<GraphicsAdapterInfo> findAdaptersLinux() {
        ArrayList<GraphicsAdapterInfo> results = new ArrayList();
        try {
            Stream<Path> devices = Files.list(Path.of("/sys/bus/pci/devices/"));
            try {
                for (Path devicePath : devices::iterator) {
                    String deviceClass = Files.readString(devicePath.resolve("class")).trim();
                    if (deviceClass.equals("0x030000") || deviceClass.equals("0x030200")) {
                        String deviceVendor = Files.readString(devicePath.resolve("vendor")).trim();
                        GraphicsAdapterVendor vendor = GraphicsAdapterVendor.identifyVendorFromString(deviceVendor);
                        String deviceId = Files.readString(devicePath.resolve("device")).trim();
                        String name = (String) ExecutingCommand.runNative("lspci -vmm -d " + deviceVendor.substring(2) + ":" + deviceId.substring(2)).stream().filter(line -> line.startsWith("Device:")).map(line -> line.substring("Device:".length()).trim()).findFirst().orElse("unknown");
                        String versionInfo = "unknown";
                        try {
                            versionInfo = Files.readString(devicePath.resolve("driver/module/version")).trim();
                        } catch (IOException var13) {
                        }
                        GraphicsAdapterInfo info = new GraphicsAdapterInfo(vendor, name, versionInfo);
                        results.add(info);
                        LOGGER.info("Found graphics card: {}", info);
                    }
                }
            } catch (Throwable var14) {
                if (devices != null) {
                    try {
                        devices.close();
                    } catch (Throwable var12) {
                        var14.addSuppressed(var12);
                    }
                }
                throw var14;
            }
            if (devices != null) {
                devices.close();
            }
        } catch (IOException var15) {
        }
        return results;
    }

    public static Collection<GraphicsAdapterInfo> getAdapters() {
        if (ADAPTERS == null) {
            throw new RuntimeException("Graphics adapters not probed yet");
        } else {
            return ADAPTERS;
        }
    }
}