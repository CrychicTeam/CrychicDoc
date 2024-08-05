package me.jellysquid.mods.sodium.client.compatibility.environment;

import java.util.Locale;

public class OSInfo {

    public static OSInfo.OS getOS() {
        String s = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (s.contains("win")) {
            return OSInfo.OS.WINDOWS;
        } else {
            return !s.contains("linux") && !s.contains("unix") ? OSInfo.OS.UNKNOWN : OSInfo.OS.LINUX;
        }
    }

    public static enum OS {

        WINDOWS, LINUX, UNKNOWN
    }
}