package net.minecraft.server.packs.linkfs;

import java.io.IOException;
import java.nio.file.FileStore;
import java.nio.file.attribute.BasicFileAttributeView;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.attribute.FileStoreAttributeView;
import javax.annotation.Nullable;

class LinkFSFileStore extends FileStore {

    private final String name;

    public LinkFSFileStore(String string0) {
        this.name = string0;
    }

    public String name() {
        return this.name;
    }

    public String type() {
        return "index";
    }

    public boolean isReadOnly() {
        return true;
    }

    public long getTotalSpace() {
        return 0L;
    }

    public long getUsableSpace() {
        return 0L;
    }

    public long getUnallocatedSpace() {
        return 0L;
    }

    public boolean supportsFileAttributeView(Class<? extends FileAttributeView> classExtendsFileAttributeView0) {
        return classExtendsFileAttributeView0 == BasicFileAttributeView.class;
    }

    public boolean supportsFileAttributeView(String string0) {
        return "basic".equals(string0);
    }

    @Nullable
    public <V extends FileStoreAttributeView> V getFileStoreAttributeView(Class<V> classV0) {
        return null;
    }

    public Object getAttribute(String string0) throws IOException {
        throw new UnsupportedOperationException();
    }
}