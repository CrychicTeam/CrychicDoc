package net.minecraft;

import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;
import oshi.hardware.GlobalMemory;
import oshi.hardware.GraphicsCard;
import oshi.hardware.HardwareAbstractionLayer;
import oshi.hardware.PhysicalMemory;
import oshi.hardware.VirtualMemory;
import oshi.hardware.CentralProcessor.ProcessorIdentifier;

public class SystemReport {

    public static final long BYTES_PER_MEBIBYTE = 1048576L;

    private static final long ONE_GIGA = 1000000000L;

    private static final Logger LOGGER = LogUtils.getLogger();

    private static final String OPERATING_SYSTEM = System.getProperty("os.name") + " (" + System.getProperty("os.arch") + ") version " + System.getProperty("os.version");

    private static final String JAVA_VERSION = System.getProperty("java.version") + ", " + System.getProperty("java.vendor");

    private static final String JAVA_VM_VERSION = System.getProperty("java.vm.name") + " (" + System.getProperty("java.vm.info") + "), " + System.getProperty("java.vm.vendor");

    private final Map<String, String> entries = Maps.newLinkedHashMap();

    public SystemReport() {
        this.setDetail("Minecraft Version", SharedConstants.getCurrentVersion().getName());
        this.setDetail("Minecraft Version ID", SharedConstants.getCurrentVersion().getId());
        this.setDetail("Operating System", OPERATING_SYSTEM);
        this.setDetail("Java Version", JAVA_VERSION);
        this.setDetail("Java VM Version", JAVA_VM_VERSION);
        this.setDetail("Memory", (Supplier<String>) (() -> {
            Runtime $$0 = Runtime.getRuntime();
            long $$1 = $$0.maxMemory();
            long $$2 = $$0.totalMemory();
            long $$3 = $$0.freeMemory();
            long $$4 = $$1 / 1048576L;
            long $$5 = $$2 / 1048576L;
            long $$6 = $$3 / 1048576L;
            return $$3 + " bytes (" + $$6 + " MiB) / " + $$2 + " bytes (" + $$5 + " MiB) up to " + $$1 + " bytes (" + $$4 + " MiB)";
        }));
        this.setDetail("CPUs", (Supplier<String>) (() -> String.valueOf(Runtime.getRuntime().availableProcessors())));
        this.ignoreErrors("hardware", () -> this.putHardware(new SystemInfo()));
        this.setDetail("JVM Flags", (Supplier<String>) (() -> {
            List<String> $$0 = (List<String>) Util.getVmArguments().collect(Collectors.toList());
            return String.format(Locale.ROOT, "%d total; %s", $$0.size(), String.join(" ", $$0));
        }));
    }

    public void setDetail(String string0, String string1) {
        this.entries.put(string0, string1);
    }

    public void setDetail(String string0, Supplier<String> supplierString1) {
        try {
            this.setDetail(string0, (String) supplierString1.get());
        } catch (Exception var4) {
            LOGGER.warn("Failed to get system info for {}", string0, var4);
            this.setDetail(string0, "ERR");
        }
    }

    private void putHardware(SystemInfo systemInfo0) {
        HardwareAbstractionLayer $$1 = systemInfo0.getHardware();
        this.ignoreErrors("processor", () -> this.putProcessor($$1.getProcessor()));
        this.ignoreErrors("graphics", () -> this.putGraphics($$1.getGraphicsCards()));
        this.ignoreErrors("memory", () -> this.putMemory($$1.getMemory()));
    }

    private void ignoreErrors(String string0, Runnable runnable1) {
        try {
            runnable1.run();
        } catch (Throwable var4) {
            LOGGER.warn("Failed retrieving info for group {}", string0, var4);
        }
    }

    private void putPhysicalMemory(List<PhysicalMemory> listPhysicalMemory0) {
        int $$1 = 0;
        for (PhysicalMemory $$2 : listPhysicalMemory0) {
            String $$3 = String.format(Locale.ROOT, "Memory slot #%d ", $$1++);
            this.setDetail($$3 + "capacity (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) $$2.getCapacity() / 1048576.0F)));
            this.setDetail($$3 + "clockSpeed (GHz)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) $$2.getClockSpeed() / 1.0E9F)));
            this.setDetail($$3 + "type", $$2::getMemoryType);
        }
    }

    private void putVirtualMemory(VirtualMemory virtualMemory0) {
        this.setDetail("Virtual memory max (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) virtualMemory0.getVirtualMax() / 1048576.0F)));
        this.setDetail("Virtual memory used (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) virtualMemory0.getVirtualInUse() / 1048576.0F)));
        this.setDetail("Swap memory total (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) virtualMemory0.getSwapTotal() / 1048576.0F)));
        this.setDetail("Swap memory used (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) virtualMemory0.getSwapUsed() / 1048576.0F)));
    }

    private void putMemory(GlobalMemory globalMemory0) {
        this.ignoreErrors("physical memory", () -> this.putPhysicalMemory(globalMemory0.getPhysicalMemory()));
        this.ignoreErrors("virtual memory", () -> this.putVirtualMemory(globalMemory0.getVirtualMemory()));
    }

    private void putGraphics(List<GraphicsCard> listGraphicsCard0) {
        int $$1 = 0;
        for (GraphicsCard $$2 : listGraphicsCard0) {
            String $$3 = String.format(Locale.ROOT, "Graphics card #%d ", $$1++);
            this.setDetail($$3 + "name", $$2::getName);
            this.setDetail($$3 + "vendor", $$2::getVendor);
            this.setDetail($$3 + "VRAM (MB)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) $$2.getVRam() / 1048576.0F)));
            this.setDetail($$3 + "deviceId", $$2::getDeviceId);
            this.setDetail($$3 + "versionInfo", $$2::getVersionInfo);
        }
    }

    private void putProcessor(CentralProcessor centralProcessor0) {
        ProcessorIdentifier $$1 = centralProcessor0.getProcessorIdentifier();
        this.setDetail("Processor Vendor", $$1::getVendor);
        this.setDetail("Processor Name", $$1::getName);
        this.setDetail("Identifier", $$1::getIdentifier);
        this.setDetail("Microarchitecture", $$1::getMicroarchitecture);
        this.setDetail("Frequency (GHz)", (Supplier<String>) (() -> String.format(Locale.ROOT, "%.2f", (float) $$1.getVendorFreq() / 1.0E9F)));
        this.setDetail("Number of physical packages", (Supplier<String>) (() -> String.valueOf(centralProcessor0.getPhysicalPackageCount())));
        this.setDetail("Number of physical CPUs", (Supplier<String>) (() -> String.valueOf(centralProcessor0.getPhysicalProcessorCount())));
        this.setDetail("Number of logical CPUs", (Supplier<String>) (() -> String.valueOf(centralProcessor0.getLogicalProcessorCount())));
    }

    public void appendToCrashReportString(StringBuilder stringBuilder0) {
        stringBuilder0.append("-- ").append("System Details").append(" --\n");
        stringBuilder0.append("Details:");
        this.entries.forEach((p_143529_, p_143530_) -> {
            stringBuilder0.append("\n\t");
            stringBuilder0.append(p_143529_);
            stringBuilder0.append(": ");
            stringBuilder0.append(p_143530_);
        });
    }

    public String toLineSeparatedString() {
        return (String) this.entries.entrySet().stream().map(p_143534_ -> (String) p_143534_.getKey() + ": " + (String) p_143534_.getValue()).collect(Collectors.joining(System.lineSeparator()));
    }
}