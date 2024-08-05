package net.minecraft.server.packs.linkfs;

import com.google.common.base.Splitter;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.nio.file.FileStore;
import java.nio.file.FileSystem;
import java.nio.file.Path;
import java.nio.file.PathMatcher;
import java.nio.file.WatchService;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class LinkFileSystem extends FileSystem {

    private static final Set<String> VIEWS = Set.of("basic");

    public static final String PATH_SEPARATOR = "/";

    private static final Splitter PATH_SPLITTER = Splitter.on('/');

    private final FileStore store;

    private final FileSystemProvider provider = new LinkFSProvider();

    private final LinkFSPath root;

    LinkFileSystem(String string0, LinkFileSystem.DirectoryEntry linkFileSystemDirectoryEntry1) {
        this.store = new LinkFSFileStore(string0);
        this.root = buildPath(linkFileSystemDirectoryEntry1, this, "", null);
    }

    private static LinkFSPath buildPath(LinkFileSystem.DirectoryEntry linkFileSystemDirectoryEntry0, LinkFileSystem linkFileSystem1, String string2, @Nullable LinkFSPath linkFSPath3) {
        Object2ObjectOpenHashMap<String, LinkFSPath> $$4 = new Object2ObjectOpenHashMap();
        LinkFSPath $$5 = new LinkFSPath(linkFileSystem1, string2, linkFSPath3, new PathContents.DirectoryContents($$4));
        linkFileSystemDirectoryEntry0.files.forEach((p_249491_, p_250850_) -> $$4.put(p_249491_, new LinkFSPath(linkFileSystem1, p_249491_, $$5, new PathContents.FileContents(p_250850_))));
        linkFileSystemDirectoryEntry0.children.forEach((p_251592_, p_251728_) -> $$4.put(p_251592_, buildPath(p_251728_, linkFileSystem1, p_251592_, $$5)));
        $$4.trim();
        return $$5;
    }

    public FileSystemProvider provider() {
        return this.provider;
    }

    public void close() {
    }

    public boolean isOpen() {
        return true;
    }

    public boolean isReadOnly() {
        return true;
    }

    public String getSeparator() {
        return "/";
    }

    public Iterable<Path> getRootDirectories() {
        return List.of(this.root);
    }

    public Iterable<FileStore> getFileStores() {
        return List.of(this.store);
    }

    public Set<String> supportedFileAttributeViews() {
        return VIEWS;
    }

    public Path getPath(String string0, String... string1) {
        Stream<String> $$2 = Stream.of(string0);
        if (string1.length > 0) {
            $$2 = Stream.concat($$2, Stream.of(string1));
        }
        String $$3 = (String) $$2.collect(Collectors.joining("/"));
        if ($$3.equals("/")) {
            return this.root;
        } else if ($$3.startsWith("/")) {
            LinkFSPath $$4 = this.root;
            for (String $$5 : PATH_SPLITTER.split($$3.substring(1))) {
                if ($$5.isEmpty()) {
                    throw new IllegalArgumentException("Empty paths not allowed");
                }
                $$4 = $$4.resolveName($$5);
            }
            return $$4;
        } else {
            LinkFSPath $$6 = null;
            for (String $$7 : PATH_SPLITTER.split($$3)) {
                if ($$7.isEmpty()) {
                    throw new IllegalArgumentException("Empty paths not allowed");
                }
                $$6 = new LinkFSPath(this, $$7, $$6, PathContents.RELATIVE);
            }
            if ($$6 == null) {
                throw new IllegalArgumentException("Empty paths not allowed");
            } else {
                return $$6;
            }
        }
    }

    public PathMatcher getPathMatcher(String string0) {
        throw new UnsupportedOperationException();
    }

    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    public WatchService newWatchService() {
        throw new UnsupportedOperationException();
    }

    public FileStore store() {
        return this.store;
    }

    public LinkFSPath rootPath() {
        return this.root;
    }

    public static LinkFileSystem.Builder builder() {
        return new LinkFileSystem.Builder();
    }

    public static class Builder {

        private final LinkFileSystem.DirectoryEntry root = new LinkFileSystem.DirectoryEntry();

        public LinkFileSystem.Builder put(List<String> listString0, String string1, Path path2) {
            LinkFileSystem.DirectoryEntry $$3 = this.root;
            for (String $$4 : listString0) {
                $$3 = (LinkFileSystem.DirectoryEntry) $$3.children.computeIfAbsent($$4, p_249671_ -> new LinkFileSystem.DirectoryEntry());
            }
            $$3.files.put(string1, path2);
            return this;
        }

        public LinkFileSystem.Builder put(List<String> listString0, Path path1) {
            if (listString0.isEmpty()) {
                throw new IllegalArgumentException("Path can't be empty");
            } else {
                int $$2 = listString0.size() - 1;
                return this.put(listString0.subList(0, $$2), (String) listString0.get($$2), path1);
            }
        }

        public FileSystem build(String string0) {
            return new LinkFileSystem(string0, this.root);
        }
    }

    static record DirectoryEntry(Map<String, LinkFileSystem.DirectoryEntry> f_244268_, Map<String, Path> f_244526_) {

        private final Map<String, LinkFileSystem.DirectoryEntry> children;

        private final Map<String, Path> files;

        public DirectoryEntry() {
            this(new HashMap(), new HashMap());
        }

        private DirectoryEntry(Map<String, LinkFileSystem.DirectoryEntry> f_244268_, Map<String, Path> f_244526_) {
            this.children = f_244268_;
            this.files = f_244526_;
        }
    }
}