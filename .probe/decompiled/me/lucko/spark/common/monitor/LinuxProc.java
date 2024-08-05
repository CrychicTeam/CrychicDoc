package me.lucko.spark.common.monitor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.checkerframework.checker.nullness.qual.Nullable;

public enum LinuxProc {

    CPUINFO("/proc/cpuinfo"), MEMINFO("/proc/meminfo"), NET_DEV("/proc/net/dev"), OSINFO("/etc/os-release");

    private final Path path;

    private LinuxProc(String path) {
        this.path = resolvePath(path);
    }

    @Nullable
    private static Path resolvePath(String path) {
        try {
            Path p = Paths.get(path);
            if (Files.isReadable(p)) {
                return p;
            }
        } catch (Exception var2) {
        }
        return null;
    }

    @NonNull
    public List<String> read() {
        if (this.path != null) {
            try {
                return Files.readAllLines(this.path, StandardCharsets.UTF_8);
            } catch (IOException var2) {
            }
        }
        return Collections.emptyList();
    }
}