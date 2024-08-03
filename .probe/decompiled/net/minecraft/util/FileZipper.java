package net.minecraft.util;

import com.google.common.collect.ImmutableMap;
import com.mojang.logging.LogUtils;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystem;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.Util;
import org.slf4j.Logger;

public class FileZipper implements Closeable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path outputFile;

    private final Path tempFile;

    private final FileSystem fs;

    public FileZipper(Path path0) {
        this.outputFile = path0;
        this.tempFile = path0.resolveSibling(path0.getFileName().toString() + "_tmp");
        try {
            this.fs = Util.ZIP_FILE_SYSTEM_PROVIDER.newFileSystem(this.tempFile, ImmutableMap.of("create", "true"));
        } catch (IOException var3) {
            throw new UncheckedIOException(var3);
        }
    }

    public void add(Path path0, String string1) {
        try {
            Path $$2 = this.fs.getPath(File.separator);
            Path $$3 = $$2.resolve(path0.toString());
            Files.createDirectories($$3.getParent());
            Files.write($$3, string1.getBytes(StandardCharsets.UTF_8), new OpenOption[0]);
        } catch (IOException var5) {
            throw new UncheckedIOException(var5);
        }
    }

    public void add(Path path0, File file1) {
        try {
            Path $$2 = this.fs.getPath(File.separator);
            Path $$3 = $$2.resolve(path0.toString());
            Files.createDirectories($$3.getParent());
            Files.copy(file1.toPath(), $$3);
        } catch (IOException var5) {
            throw new UncheckedIOException(var5);
        }
    }

    public void add(Path path0) {
        try {
            Path $$1 = this.fs.getPath(File.separator);
            if (Files.isRegularFile(path0, new LinkOption[0])) {
                Path $$2 = $$1.resolve(path0.getParent().relativize(path0).toString());
                Files.copy($$2, path0);
            } else {
                Stream<Path> $$3 = Files.find(path0, Integer.MAX_VALUE, (p_144707_, p_144708_) -> p_144708_.isRegularFile(), new FileVisitOption[0]);
                try {
                    for (Path $$4 : (List) $$3.collect(Collectors.toList())) {
                        Path $$5 = $$1.resolve(path0.relativize($$4).toString());
                        Files.createDirectories($$5.getParent());
                        Files.copy($$4, $$5);
                    }
                } catch (Throwable var8) {
                    if ($$3 != null) {
                        try {
                            $$3.close();
                        } catch (Throwable var7) {
                            var8.addSuppressed(var7);
                        }
                    }
                    throw var8;
                }
                if ($$3 != null) {
                    $$3.close();
                }
            }
        } catch (IOException var9) {
            throw new UncheckedIOException(var9);
        }
    }

    public void close() {
        try {
            this.fs.close();
            Files.move(this.tempFile, this.outputFile);
            LOGGER.info("Compressed to {}", this.outputFile);
        } catch (IOException var2) {
            throw new UncheckedIOException(var2);
        }
    }
}