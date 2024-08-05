package info.journeymap.shaded.org.eclipse.jetty.http;

import info.journeymap.shaded.org.eclipse.jetty.util.ArrayTernaryTrie;
import info.journeymap.shaded.org.eclipse.jetty.util.Trie;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.Map.Entry;
import java.util.function.Predicate;

@Deprecated
public class PathMap<O> extends HashMap<String, O> {

    private static String __pathSpecSeparators = ":,";

    Trie<PathMap.MappedEntry<O>> _prefixMap = new ArrayTernaryTrie<>(false);

    Trie<PathMap.MappedEntry<O>> _suffixMap = new ArrayTernaryTrie<>(false);

    final Map<String, PathMap.MappedEntry<O>> _exactMap = new HashMap();

    List<PathMap.MappedEntry<O>> _defaultSingletonList = null;

    PathMap.MappedEntry<O> _prefixDefault = null;

    PathMap.MappedEntry<O> _default = null;

    boolean _nodefault = false;

    public static void setPathSpecSeparators(String s) {
        __pathSpecSeparators = s;
    }

    public PathMap() {
        this(11);
    }

    public PathMap(boolean noDefault) {
        this(11, noDefault);
    }

    public PathMap(int capacity) {
        this(capacity, false);
    }

    private PathMap(int capacity, boolean noDefault) {
        super(capacity);
        this._nodefault = noDefault;
    }

    public PathMap(Map<String, ? extends O> dictMap) {
        this.putAll(dictMap);
    }

    public O put(String pathSpec, O object) {
        if ("".equals(pathSpec.trim())) {
            PathMap.MappedEntry<O> entry = new PathMap.MappedEntry<>("", object);
            entry.setMapped("");
            this._exactMap.put("", entry);
            return (O) super.put("", object);
        } else {
            StringTokenizer tok = new StringTokenizer(pathSpec, __pathSpecSeparators);
            O old = null;
            while (tok.hasMoreTokens()) {
                String spec = tok.nextToken();
                if (!spec.startsWith("/") && !spec.startsWith("*.")) {
                    throw new IllegalArgumentException("PathSpec " + spec + ". must start with '/' or '*.'");
                }
                old = (O) super.put(spec, object);
                PathMap.MappedEntry<O> entry = new PathMap.MappedEntry<>(spec, object);
                if (entry.getKey().equals(spec)) {
                    if (spec.equals("/*")) {
                        this._prefixDefault = entry;
                    } else if (spec.endsWith("/*")) {
                        String mapped = spec.substring(0, spec.length() - 2);
                        entry.setMapped(mapped);
                        while (!this._prefixMap.put(mapped, entry)) {
                            this._prefixMap = new ArrayTernaryTrie<>((ArrayTernaryTrie<PathMap.MappedEntry<O>>) this._prefixMap, 1.5);
                        }
                    } else if (spec.startsWith("*.")) {
                        String suffix = spec.substring(2);
                        while (!this._suffixMap.put(suffix, entry)) {
                            this._suffixMap = new ArrayTernaryTrie<>((ArrayTernaryTrie<PathMap.MappedEntry<O>>) this._suffixMap, 1.5);
                        }
                    } else if (spec.equals("/")) {
                        if (this._nodefault) {
                            this._exactMap.put(spec, entry);
                        } else {
                            this._default = entry;
                            this._defaultSingletonList = Collections.singletonList(this._default);
                        }
                    } else {
                        entry.setMapped(spec);
                        this._exactMap.put(spec, entry);
                    }
                }
            }
            return old;
        }
    }

    public O match(String path) {
        PathMap.MappedEntry<O> entry = this.getMatch(path);
        return entry != null ? entry.getValue() : null;
    }

    public PathMap.MappedEntry<O> getMatch(String path) {
        if (path == null) {
            return null;
        } else {
            int l = path.length();
            PathMap.MappedEntry<O> entry = null;
            if (l == 1 && path.charAt(0) == '/') {
                entry = (PathMap.MappedEntry<O>) this._exactMap.get("");
                if (entry != null) {
                    return entry;
                }
            }
            entry = (PathMap.MappedEntry<O>) this._exactMap.get(path);
            if (entry != null) {
                return entry;
            } else {
                int i = l;
                Trie<PathMap.MappedEntry<O>> prefix_map = this._prefixMap;
                while (i >= 0) {
                    entry = prefix_map.getBest(path, 0, i);
                    if (entry != null) {
                        String key = entry.getKey();
                        if (key.length() - 2 < path.length() && path.charAt(key.length() - 2) != '/') {
                            i = key.length() - 3;
                            continue;
                        }
                        return entry;
                    }
                    break;
                }
                if (this._prefixDefault != null) {
                    return this._prefixDefault;
                } else {
                    i = 0;
                    Trie<PathMap.MappedEntry<O>> suffix_map = this._suffixMap;
                    while ((i = path.indexOf(46, i + 1)) > 0) {
                        entry = suffix_map.get(path, i + 1, l - i - 1);
                        if (entry != null) {
                            return entry;
                        }
                    }
                    return this._default;
                }
            }
        }
    }

    public List<? extends Entry<String, O>> getMatches(String path) {
        List<PathMap.MappedEntry<O>> entries = new ArrayList();
        if (path == null) {
            return entries;
        } else if (path.length() == 0) {
            return this._defaultSingletonList;
        } else {
            PathMap.MappedEntry<O> entry = (PathMap.MappedEntry<O>) this._exactMap.get(path);
            if (entry != null) {
                entries.add(entry);
            }
            int l = path.length();
            int i = l;
            Trie<PathMap.MappedEntry<O>> prefix_map = this._prefixMap;
            while (i >= 0) {
                entry = prefix_map.getBest(path, 0, i);
                if (entry == null) {
                    break;
                }
                String key = entry.getKey();
                if (key.length() - 2 >= path.length() || path.charAt(key.length() - 2) == '/') {
                    entries.add(entry);
                }
                i = key.length() - 3;
            }
            if (this._prefixDefault != null) {
                entries.add(this._prefixDefault);
            }
            i = 0;
            Trie<PathMap.MappedEntry<O>> suffix_map = this._suffixMap;
            while ((i = path.indexOf(46, i + 1)) > 0) {
                entry = suffix_map.get(path, i + 1, l - i - 1);
                if (entry != null) {
                    entries.add(entry);
                }
            }
            if ("/".equals(path)) {
                entry = (PathMap.MappedEntry<O>) this._exactMap.get("");
                if (entry != null) {
                    entries.add(entry);
                }
            }
            if (this._default != null) {
                entries.add(this._default);
            }
            return entries;
        }
    }

    public boolean containsMatch(String path) {
        PathMap.MappedEntry<?> match = this.getMatch(path);
        return match != null && !match.equals(this._default);
    }

    public O remove(Object pathSpec) {
        if (pathSpec != null) {
            String spec = (String) pathSpec;
            if (spec.equals("/*")) {
                this._prefixDefault = null;
            } else if (spec.endsWith("/*")) {
                this._prefixMap.remove(spec.substring(0, spec.length() - 2));
            } else if (spec.startsWith("*.")) {
                this._suffixMap.remove(spec.substring(2));
            } else if (spec.equals("/")) {
                this._default = null;
                this._defaultSingletonList = null;
            } else {
                this._exactMap.remove(spec);
            }
        }
        return (O) super.remove(pathSpec);
    }

    public void clear() {
        this._exactMap.clear();
        this._prefixMap = new ArrayTernaryTrie<>(false);
        this._suffixMap = new ArrayTernaryTrie<>(false);
        this._default = null;
        this._defaultSingletonList = null;
        this._prefixDefault = null;
        super.clear();
    }

    public static boolean match(String pathSpec, String path) {
        return match(pathSpec, path, false);
    }

    public static boolean match(String pathSpec, String path, boolean noDefault) {
        if (pathSpec.length() == 0) {
            return "/".equals(path);
        } else {
            char c = pathSpec.charAt(0);
            if (c == '/') {
                if (!noDefault && pathSpec.length() == 1 || pathSpec.equals(path)) {
                    return true;
                }
                if (isPathWildcardMatch(pathSpec, path)) {
                    return true;
                }
            } else if (c == '*') {
                return path.regionMatches(path.length() - pathSpec.length() + 1, pathSpec, 1, pathSpec.length() - 1);
            }
            return false;
        }
    }

    private static boolean isPathWildcardMatch(String pathSpec, String path) {
        int cpl = pathSpec.length() - 2;
        return pathSpec.endsWith("/*") && path.regionMatches(0, pathSpec, 0, cpl) && (path.length() == cpl || '/' == path.charAt(cpl));
    }

    public static String pathMatch(String pathSpec, String path) {
        char c = pathSpec.charAt(0);
        if (c == '/') {
            if (pathSpec.length() == 1) {
                return path;
            }
            if (pathSpec.equals(path)) {
                return path;
            }
            if (isPathWildcardMatch(pathSpec, path)) {
                return path.substring(0, pathSpec.length() - 2);
            }
        } else if (c == '*' && path.regionMatches(path.length() - (pathSpec.length() - 1), pathSpec, 1, pathSpec.length() - 1)) {
            return path;
        }
        return null;
    }

    public static String pathInfo(String pathSpec, String path) {
        if ("".equals(pathSpec)) {
            return path;
        } else {
            char c = pathSpec.charAt(0);
            if (c == '/') {
                if (pathSpec.length() == 1) {
                    return null;
                }
                boolean wildcard = isPathWildcardMatch(pathSpec, path);
                if (pathSpec.equals(path) && !wildcard) {
                    return null;
                }
                if (wildcard) {
                    if (path.length() == pathSpec.length() - 2) {
                        return null;
                    }
                    return path.substring(pathSpec.length() - 2);
                }
            }
            return null;
        }
    }

    public static String relativePath(String base, String pathSpec, String path) {
        String info = pathInfo(pathSpec, path);
        if (info == null) {
            info = path;
        }
        if (info.startsWith("./")) {
            info = info.substring(2);
        }
        if (base.endsWith("/")) {
            if (info.startsWith("/")) {
                path = base + info.substring(1);
            } else {
                path = base + info;
            }
        } else if (info.startsWith("/")) {
            path = base + info;
        } else {
            path = base + "/" + info;
        }
        return path;
    }

    public static class MappedEntry<O> implements Entry<String, O> {

        private final String key;

        private final O value;

        private String mapped;

        MappedEntry(String key, O value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return this.key;
        }

        public O getValue() {
            return this.value;
        }

        public O setValue(O o) {
            throw new UnsupportedOperationException();
        }

        public String toString() {
            return this.key + "=" + this.value;
        }

        public String getMapped() {
            return this.mapped;
        }

        void setMapped(String mapped) {
            this.mapped = mapped;
        }
    }

    public static class PathSet extends AbstractSet<String> implements Predicate<String> {

        private final PathMap<Boolean> _map = new PathMap<>();

        public Iterator<String> iterator() {
            return this._map.keySet().iterator();
        }

        public int size() {
            return this._map.size();
        }

        public boolean add(String item) {
            return this._map.put(item, Boolean.TRUE) == null;
        }

        public boolean remove(Object item) {
            return this._map.remove(item) != null;
        }

        public boolean contains(Object o) {
            return this._map.containsKey(o);
        }

        public boolean test(String s) {
            return this._map.containsMatch(s);
        }

        public boolean containsMatch(String s) {
            return this._map.containsMatch(s);
        }
    }
}