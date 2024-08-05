package me.lucko.spark.common.util;

import com.google.common.collect.ImmutableList;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public final class TemporaryFiles {

    public static final FileAttribute<?>[] OWNER_ONLY_FILE_PERMISSIONS;

    private final Path tmpDirectory;

    private final Set<Path> files = Collections.synchronizedSet(new HashSet());

    public TemporaryFiles(Path tmpDirectory) {
        this.tmpDirectory = tmpDirectory;
    }

    public Path create(String prefix, String suffix) throws IOException {
        Path file;
        if (this.ensureDirectoryIsReady()) {
            String name = prefix + Long.toHexString(System.nanoTime()) + suffix;
            file = Files.createFile(this.tmpDirectory.resolve(name), OWNER_ONLY_FILE_PERMISSIONS);
        } else {
            file = Files.createTempFile(prefix, suffix);
        }
        return this.register(file);
    }

    public Path register(Path path) {
        path.toFile().deleteOnExit();
        this.files.add(path);
        return path;
    }

    public void deleteTemporaryFiles() {
        synchronized (this.files) {
            for (Iterator<Path> iterator = this.files.iterator(); iterator.hasNext(); iterator.remove()) {
                Path path = (Path) iterator.next();
                try {
                    Files.deleteIfExists(path);
                } catch (IOException var6) {
                }
            }
        }
    }

    private boolean ensureDirectoryIsReady() {
        if (Boolean.parseBoolean(System.getProperty("spark.useOsTmpDir", "false"))) {
            return false;
        } else if (Files.isDirectory(this.tmpDirectory, new LinkOption[0])) {
            return true;
        } else {
            try {
                Files.createDirectories(this.tmpDirectory);
                Files.write(this.tmpDirectory.resolve("about.txt"), ImmutableList.of("# What is this directory?", "", "* In order to perform certain functions, spark sometimes needs to write temporary data to the disk. ", "* Previously, a temporary directory provided by the operating system was used for this purpose. ", "* However, this proved to be unreliable in some circumstances, so spark now stores temporary data here instead!", "", "spark will automatically cleanup the contents of this directory. ", "(but if for some reason it doesn't, if the server is stopped, you can freely delete any files ending in .tmp)", "", "tl;dr: spark uses this folder to store some temporary data."), StandardCharsets.UTF_8);
                return true;
            } catch (IOException var2) {
                return false;
            }
        }
    }

    static {
        boolean isPosix = FileSystems.getDefault().supportedFileAttributeViews().contains("posix");
        if (isPosix) {
            OWNER_ONLY_FILE_PERMISSIONS = new FileAttribute[] { PosixFilePermissions.asFileAttribute(EnumSet.of(PosixFilePermission.OWNER_READ, PosixFilePermission.OWNER_WRITE)) };
        } else {
            OWNER_ONLY_FILE_PERMISSIONS = new FileAttribute[0];
        }
    }
}