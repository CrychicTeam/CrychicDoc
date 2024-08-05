package me.lucko.spark.common.monitor.os;

import me.lucko.spark.common.monitor.LinuxProc;
import me.lucko.spark.common.monitor.WindowsWmic;

public final class OperatingSystemInfo {

    private final String name;

    private final String version;

    private final String arch;

    public OperatingSystemInfo(String name, String version, String arch) {
        this.name = name;
        this.version = version;
        this.arch = arch;
    }

    public String name() {
        return this.name;
    }

    public String version() {
        return this.version;
    }

    public String arch() {
        return this.arch;
    }

    public static OperatingSystemInfo poll() {
        String name = null;
        String version = null;
        for (String line : LinuxProc.OSINFO.read()) {
            if (line.startsWith("PRETTY_NAME") && line.length() > 13) {
                name = line.substring(13).replace('"', ' ').trim();
            }
        }
        for (String linex : WindowsWmic.OS_GET_CAPTION_AND_VERSION.read()) {
            if (linex.startsWith("Caption") && linex.length() > 18) {
                name = linex.substring(18).trim();
            } else if (linex.startsWith("Version")) {
                version = linex.substring(8).trim();
            }
        }
        if (name == null) {
            name = System.getProperty("os.name");
        }
        if (version == null) {
            version = System.getProperty("os.version");
        }
        String arch = System.getProperty("os.arch");
        return new OperatingSystemInfo(name, version, arch);
    }
}