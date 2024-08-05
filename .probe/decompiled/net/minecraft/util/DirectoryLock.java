package net.minecraft.util;

import com.google.common.base.Charsets;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.AccessDeniedException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import net.minecraft.FileUtil;

public class DirectoryLock implements AutoCloseable {

    public static final String LOCK_FILE = "session.lock";

    private final FileChannel lockFile;

    private final FileLock lock;

    private static final ByteBuffer DUMMY;

    public static DirectoryLock create(Path path0) throws IOException {
        Path $$1 = path0.resolve("session.lock");
        FileUtil.createDirectoriesSafe(path0);
        FileChannel $$2 = FileChannel.open($$1, StandardOpenOption.CREATE, StandardOpenOption.WRITE);
        try {
            $$2.write(DUMMY.duplicate());
            $$2.force(true);
            FileLock $$3 = $$2.tryLock();
            if ($$3 == null) {
                throw DirectoryLock.LockException.alreadyLocked($$1);
            } else {
                return new DirectoryLock($$2, $$3);
            }
        } catch (IOException var6) {
            try {
                $$2.close();
            } catch (IOException var5) {
                var6.addSuppressed(var5);
            }
            throw var6;
        }
    }

    private DirectoryLock(FileChannel fileChannel0, FileLock fileLock1) {
        this.lockFile = fileChannel0;
        this.lock = fileLock1;
    }

    public void close() throws IOException {
        try {
            if (this.lock.isValid()) {
                this.lock.release();
            }
        } finally {
            if (this.lockFile.isOpen()) {
                this.lockFile.close();
            }
        }
    }

    public boolean isValid() {
        return this.lock.isValid();
    }

    public static boolean isLocked(Path path0) throws IOException {
        Path $$1 = path0.resolve("session.lock");
        try {
            FileChannel $$2 = FileChannel.open($$1, StandardOpenOption.WRITE);
            boolean var4;
            try {
                FileLock $$3 = $$2.tryLock();
                try {
                    var4 = $$3 == null;
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
            } catch (Throwable var9) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var6) {
                        var9.addSuppressed(var6);
                    }
                }
                throw var9;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var4;
        } catch (AccessDeniedException var10) {
            return true;
        } catch (NoSuchFileException var11) {
            return false;
        }
    }

    static {
        byte[] $$0 = "☃".getBytes(Charsets.UTF_8);
        DUMMY = ByteBuffer.allocateDirect($$0.length);
        DUMMY.put($$0);
        DUMMY.flip();
    }

    public static class LockException extends IOException {

        private LockException(Path path0, String string1) {
            super(path0.toAbsolutePath() + ": " + string1);
        }

        public static DirectoryLock.LockException alreadyLocked(Path path0) {
            return new DirectoryLock.LockException(path0, "already locked (possibly by other Minecraft instance?)");
        }
    }
}