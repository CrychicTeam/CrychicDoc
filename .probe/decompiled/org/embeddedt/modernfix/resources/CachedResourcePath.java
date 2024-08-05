package org.embeddedt.modernfix.resources;

import com.google.common.base.Splitter;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Collection;
import org.embeddedt.modernfix.util.FileUtil;

public class CachedResourcePath {

    private final String[] pathComponents;

    public static final Interner<String> PATH_COMPONENT_INTERNER = Interners.newStrongInterner();

    private static final Splitter SLASH_SPLITTER = Splitter.on('/');

    public static final String[] NO_PREFIX = new String[0];

    public CachedResourcePath(String[] prefix, Path path) {
        this(prefix, path, path.getNameCount(), true);
    }

    public CachedResourcePath(String s) {
        this(NO_PREFIX, SLASH_SPLITTER.splitToList(FileUtil.normalize(s)), false);
    }

    public <T> CachedResourcePath(String[] prefixElements, Collection<T> collection, boolean intern) {
        this(prefixElements, collection, collection.size(), intern);
    }

    public <T> CachedResourcePath(String[] prefixElements, Iterable<T> path, int count, boolean intern) {
        String[] components = new String[prefixElements.length + count];
        int i;
        for (i = 0; i < prefixElements.length; i++) {
            components[i] = intern ? (String) PATH_COMPONENT_INTERNER.intern(prefixElements[i]) : prefixElements[i];
        }
        for (Object component : path) {
            String s = component.toString();
            if (s.length() != 0) {
                components[i] = intern ? (String) PATH_COMPONENT_INTERNER.intern(s) : s;
                i++;
            }
        }
        this.pathComponents = components;
    }

    public CachedResourcePath(String[] prefixElements, CachedResourcePath other) {
        String[] components = new String[prefixElements.length + other.pathComponents.length];
        int i;
        for (i = 0; i < prefixElements.length; i++) {
            components[i] = (String) PATH_COMPONENT_INTERNER.intern(prefixElements[i]);
        }
        System.arraycopy(other.pathComponents, 0, components, i, other.pathComponents.length);
        this.pathComponents = components;
    }

    public CachedResourcePath(String[] pathComponents) {
        for (String s : pathComponents) {
            if (s.length() == 0) {
                pathComponents = (String[]) Arrays.stream(pathComponents).filter(comp -> comp.length() > 0).toArray(String[]::new);
                break;
            }
        }
        this.pathComponents = pathComponents;
    }

    public int hashCode() {
        return Arrays.hashCode(this.pathComponents);
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            CachedResourcePath that = (CachedResourcePath) o;
            return Arrays.equals(this.pathComponents, that.pathComponents);
        } else {
            return false;
        }
    }

    public String getFileName() {
        return this.pathComponents[this.pathComponents.length - 1];
    }

    public int getNameCount() {
        return this.pathComponents.length;
    }

    public String getNameAt(int i) {
        return this.pathComponents[i];
    }

    public String getFullPath(int startIndex) {
        StringBuilder sb = new StringBuilder();
        for (int i = startIndex; i < this.pathComponents.length; i++) {
            sb.append(this.pathComponents[i]);
            if (i != this.pathComponents.length - 1) {
                sb.append('/');
            }
        }
        return sb.toString();
    }
}