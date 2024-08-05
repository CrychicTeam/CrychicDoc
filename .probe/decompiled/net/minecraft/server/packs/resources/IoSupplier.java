package net.minecraft.server.packs.resources;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

@FunctionalInterface
public interface IoSupplier<T> {

    static IoSupplier<InputStream> create(Path path0) {
        return () -> Files.newInputStream(path0);
    }

    static IoSupplier<InputStream> create(ZipFile zipFile0, ZipEntry zipEntry1) {
        return () -> zipFile0.getInputStream(zipEntry1);
    }

    T get() throws IOException;
}