package net.minecraft.server.packs.linkfs;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.AccessDeniedException;
import java.nio.file.AccessMode;
import java.nio.file.CopyOption;
import java.nio.file.DirectoryIteratorException;
import java.nio.file.DirectoryStream;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.NotDirectoryException;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.ProviderMismatchException;
import java.nio.file.ReadOnlyFileSystemException;
import java.nio.file.StandardOpenOption;
import java.nio.file.DirectoryStream.Filter;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.annotation.Nullable;

class LinkFSProvider extends FileSystemProvider {

    public static final String SCHEME = "x-mc-link";

    public String getScheme() {
        return "x-mc-link";
    }

    public FileSystem newFileSystem(URI uRI0, Map<String, ?> mapString1) {
        throw new UnsupportedOperationException();
    }

    public FileSystem getFileSystem(URI uRI0) {
        throw new UnsupportedOperationException();
    }

    public Path getPath(URI uRI0) {
        throw new UnsupportedOperationException();
    }

    public SeekableByteChannel newByteChannel(Path path0, Set<? extends OpenOption> setExtendsOpenOption1, FileAttribute<?>... fileAttribute2) throws IOException {
        if (!setExtendsOpenOption1.contains(StandardOpenOption.CREATE_NEW) && !setExtendsOpenOption1.contains(StandardOpenOption.CREATE) && !setExtendsOpenOption1.contains(StandardOpenOption.APPEND) && !setExtendsOpenOption1.contains(StandardOpenOption.WRITE)) {
            Path $$3 = toLinkPath(path0).toAbsolutePath().getTargetPath();
            if ($$3 == null) {
                throw new NoSuchFileException(path0.toString());
            } else {
                return Files.newByteChannel($$3, setExtendsOpenOption1, fileAttribute2);
            }
        } else {
            throw new UnsupportedOperationException();
        }
    }

    public DirectoryStream<Path> newDirectoryStream(Path path0, final Filter<? super Path> filterSuperPath1) throws IOException {
        final PathContents.DirectoryContents $$2 = toLinkPath(path0).toAbsolutePath().getDirectoryContents();
        if ($$2 == null) {
            throw new NotDirectoryException(path0.toString());
        } else {
            return new DirectoryStream<Path>() {

                public Iterator<Path> iterator() {
                    return $$2.children().values().stream().filter(p_250987_ -> {
                        try {
                            return filterSuperPath1.accept(p_250987_);
                        } catch (IOException var3) {
                            throw new DirectoryIteratorException(var3);
                        }
                    }).map(p_249891_ -> p_249891_).iterator();
                }

                public void close() {
                }
            };
        }
    }

    public void createDirectory(Path path0, FileAttribute<?>... fileAttribute1) {
        throw new ReadOnlyFileSystemException();
    }

    public void delete(Path path0) {
        throw new ReadOnlyFileSystemException();
    }

    public void copy(Path path0, Path path1, CopyOption... copyOption2) {
        throw new ReadOnlyFileSystemException();
    }

    public void move(Path path0, Path path1, CopyOption... copyOption2) {
        throw new ReadOnlyFileSystemException();
    }

    public boolean isSameFile(Path path0, Path path1) {
        return path0 instanceof LinkFSPath && path1 instanceof LinkFSPath && path0.equals(path1);
    }

    public boolean isHidden(Path path0) {
        return false;
    }

    public FileStore getFileStore(Path path0) {
        return toLinkPath(path0).getFileSystem().store();
    }

    public void checkAccess(Path path0, AccessMode... accessMode1) throws IOException {
        if (accessMode1.length == 0 && !toLinkPath(path0).exists()) {
            throw new NoSuchFileException(path0.toString());
        } else {
            AccessMode[] var3 = accessMode1;
            int var4 = accessMode1.length;
            int var5 = 0;
            while (var5 < var4) {
                AccessMode $$2 = var3[var5];
                switch($$2) {
                    case READ:
                        if (!toLinkPath(path0).exists()) {
                            throw new NoSuchFileException(path0.toString());
                        }
                    default:
                        var5++;
                        break;
                    case EXECUTE:
                    case WRITE:
                        throw new AccessDeniedException($$2.toString());
                }
            }
        }
    }

    @Nullable
    public <V extends FileAttributeView> V getFileAttributeView(Path path0, Class<V> classV1, LinkOption... linkOption2) {
        LinkFSPath $$3 = toLinkPath(path0);
        return (V) (classV1 == BasicFileAttributeView.class ? $$3.getBasicAttributeView() : null);
    }

    public <A extends BasicFileAttributes> A readAttributes(Path path0, Class<A> classA1, LinkOption... linkOption2) throws IOException {
        LinkFSPath $$3 = toLinkPath(path0).toAbsolutePath();
        if (classA1 == BasicFileAttributes.class) {
            return (A) $$3.getBasicAttributes();
        } else {
            throw new UnsupportedOperationException("Attributes of type " + classA1.getName() + " not supported");
        }
    }

    public Map<String, Object> readAttributes(Path path0, String string1, LinkOption... linkOption2) {
        throw new UnsupportedOperationException();
    }

    public void setAttribute(Path path0, String string1, Object object2, LinkOption... linkOption3) {
        throw new ReadOnlyFileSystemException();
    }

    private static LinkFSPath toLinkPath(@Nullable Path path0) {
        if (path0 == null) {
            throw new NullPointerException();
        } else if (path0 instanceof LinkFSPath) {
            return (LinkFSPath) path0;
        } else {
            throw new ProviderMismatchException();
        }
    }
}