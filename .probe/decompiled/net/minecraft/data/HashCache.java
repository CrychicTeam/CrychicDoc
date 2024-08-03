package net.minecraft.data;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.UnmodifiableIterator;
import com.google.common.collect.ImmutableMap.Builder;
import com.google.common.hash.HashCode;
import com.google.common.hash.Hashing;
import com.mojang.logging.LogUtils;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.WorldVersion;
import org.apache.commons.lang3.mutable.MutableInt;
import org.slf4j.Logger;

public class HashCache {

    static final Logger LOGGER = LogUtils.getLogger();

    private static final String HEADER_MARKER = "// ";

    private final Path rootDir;

    private final Path cacheDir;

    private final String versionId;

    private final Map<String, HashCache.ProviderCache> caches;

    private final Set<String> cachesToWrite = new HashSet();

    private final Set<Path> cachePaths = new HashSet();

    private final int initialCount;

    private int writes;

    private Path getProviderCachePath(String string0) {
        return this.cacheDir.resolve(Hashing.sha1().hashString(string0, StandardCharsets.UTF_8).toString());
    }

    public HashCache(Path path0, Collection<String> collectionString1, WorldVersion worldVersion2) throws IOException {
        this.versionId = worldVersion2.getName();
        this.rootDir = path0;
        this.cacheDir = path0.resolve(".cache");
        Files.createDirectories(this.cacheDir);
        Map<String, HashCache.ProviderCache> $$3 = new HashMap();
        int $$4 = 0;
        for (String $$5 : collectionString1) {
            Path $$6 = this.getProviderCachePath($$5);
            this.cachePaths.add($$6);
            HashCache.ProviderCache $$7 = readCache(path0, $$6);
            $$3.put($$5, $$7);
            $$4 += $$7.count();
        }
        this.caches = $$3;
        this.initialCount = $$4;
    }

    private static HashCache.ProviderCache readCache(Path path0, Path path1) {
        if (Files.isReadable(path1)) {
            try {
                return HashCache.ProviderCache.load(path0, path1);
            } catch (Exception var3) {
                LOGGER.warn("Failed to parse cache {}, discarding", path1, var3);
            }
        }
        return new HashCache.ProviderCache("unknown", ImmutableMap.of());
    }

    public boolean shouldRunInThisVersion(String string0) {
        HashCache.ProviderCache $$1 = (HashCache.ProviderCache) this.caches.get(string0);
        return $$1 == null || !$$1.version.equals(this.versionId);
    }

    public CompletableFuture<HashCache.UpdateResult> generateUpdate(String string0, HashCache.UpdateFunction hashCacheUpdateFunction1) {
        HashCache.ProviderCache $$2 = (HashCache.ProviderCache) this.caches.get(string0);
        if ($$2 == null) {
            throw new IllegalStateException("Provider not registered: " + string0);
        } else {
            HashCache.CacheUpdater $$3 = new HashCache.CacheUpdater(string0, this.versionId, $$2);
            return hashCacheUpdateFunction1.update($$3).thenApply(p_253376_ -> $$3.close());
        }
    }

    public void applyUpdate(HashCache.UpdateResult hashCacheUpdateResult0) {
        this.caches.put(hashCacheUpdateResult0.providerId(), hashCacheUpdateResult0.cache());
        this.cachesToWrite.add(hashCacheUpdateResult0.providerId());
        this.writes = this.writes + hashCacheUpdateResult0.writes();
    }

    public void purgeStaleAndWrite() throws IOException {
        Set<Path> $$0 = new HashSet();
        this.caches.forEach((p_253378_, p_253379_) -> {
            if (this.cachesToWrite.contains(p_253378_)) {
                Path $$3x = this.getProviderCachePath(p_253378_);
                p_253379_.save(this.rootDir, $$3x, DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(LocalDateTime.now()) + "\t" + p_253378_);
            }
            $$0.addAll(p_253379_.data().keySet());
        });
        $$0.add(this.rootDir.resolve("version.json"));
        MutableInt $$1 = new MutableInt();
        MutableInt $$2 = new MutableInt();
        Stream<Path> $$3 = Files.walk(this.rootDir);
        try {
            $$3.forEach(p_236106_ -> {
                if (!Files.isDirectory(p_236106_, new LinkOption[0])) {
                    if (!this.cachePaths.contains(p_236106_)) {
                        $$1.increment();
                        if (!$$0.contains(p_236106_)) {
                            try {
                                Files.delete(p_236106_);
                            } catch (IOException var6) {
                                LOGGER.warn("Failed to delete file {}", p_236106_, var6);
                            }
                            $$2.increment();
                        }
                    }
                }
            });
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
        LOGGER.info("Caching: total files: {}, old count: {}, new count: {}, removed stale: {}, written: {}", new Object[] { $$1, this.initialCount, $$0.size(), $$2, this.writes });
    }

    class CacheUpdater implements CachedOutput {

        private final String provider;

        private final HashCache.ProviderCache oldCache;

        private final HashCache.ProviderCacheBuilder newCache;

        private final AtomicInteger writes = new AtomicInteger();

        private volatile boolean closed;

        CacheUpdater(String string0, String string1, HashCache.ProviderCache hashCacheProviderCache2) {
            this.provider = string0;
            this.oldCache = hashCacheProviderCache2;
            this.newCache = new HashCache.ProviderCacheBuilder(string1);
        }

        private boolean shouldWrite(Path path0, HashCode hashCode1) {
            return !Objects.equals(this.oldCache.get(path0), hashCode1) || !Files.exists(path0, new LinkOption[0]);
        }

        @Override
        public void writeIfNeeded(Path path0, byte[] byte1, HashCode hashCode2) throws IOException {
            if (this.closed) {
                throw new IllegalStateException("Cannot write to cache as it has already been closed");
            } else {
                if (this.shouldWrite(path0, hashCode2)) {
                    this.writes.incrementAndGet();
                    Files.createDirectories(path0.getParent());
                    Files.write(path0, byte1, new OpenOption[0]);
                }
                this.newCache.put(path0, hashCode2);
            }
        }

        public HashCache.UpdateResult close() {
            this.closed = true;
            return new HashCache.UpdateResult(this.provider, this.newCache.build(), this.writes.get());
        }
    }

    static record ProviderCache(String f_236126_, ImmutableMap<Path, HashCode> f_236127_) {

        private final String version;

        private final ImmutableMap<Path, HashCode> data;

        ProviderCache(String f_236126_, ImmutableMap<Path, HashCode> f_236127_) {
            this.version = f_236126_;
            this.data = f_236127_;
        }

        @Nullable
        public HashCode get(Path p_236135_) {
            return (HashCode) this.data.get(p_236135_);
        }

        public int count() {
            return this.data.size();
        }

        public static HashCache.ProviderCache load(Path p_236140_, Path p_236141_) throws IOException {
            BufferedReader $$2 = Files.newBufferedReader(p_236141_, StandardCharsets.UTF_8);
            HashCache.ProviderCache var7;
            try {
                String $$3 = $$2.readLine();
                if (!$$3.startsWith("// ")) {
                    throw new IllegalStateException("Missing cache file header");
                }
                String[] $$4 = $$3.substring("// ".length()).split("\t", 2);
                String $$5 = $$4[0];
                Builder<Path, HashCode> $$6 = ImmutableMap.builder();
                $$2.lines().forEach(p_253382_ -> {
                    int $$3x = p_253382_.indexOf(32);
                    $$6.put(p_236140_.resolve(p_253382_.substring($$3x + 1)), HashCode.fromString(p_253382_.substring(0, $$3x)));
                });
                var7 = new HashCache.ProviderCache($$5, $$6.build());
            } catch (Throwable var9) {
                if ($$2 != null) {
                    try {
                        $$2.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }
                throw var9;
            }
            if ($$2 != null) {
                $$2.close();
            }
            return var7;
        }

        public void save(Path p_236143_, Path p_236144_, String p_236145_) {
            try {
                BufferedWriter $$3 = Files.newBufferedWriter(p_236144_, StandardCharsets.UTF_8);
                try {
                    $$3.write("// ");
                    $$3.write(this.version);
                    $$3.write(9);
                    $$3.write(p_236145_);
                    $$3.newLine();
                    UnmodifiableIterator var5 = this.data.entrySet().iterator();
                    while (var5.hasNext()) {
                        Entry<Path, HashCode> $$4 = (Entry<Path, HashCode>) var5.next();
                        $$3.write(((HashCode) $$4.getValue()).toString());
                        $$3.write(32);
                        $$3.write(p_236143_.relativize((Path) $$4.getKey()).toString());
                        $$3.newLine();
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
            } catch (IOException var9) {
                HashCache.LOGGER.warn("Unable write cachefile {}: {}", p_236144_, var9);
            }
        }
    }

    static record ProviderCacheBuilder(String f_252424_, ConcurrentMap<Path, HashCode> f_252466_) {

        private final String version;

        private final ConcurrentMap<Path, HashCode> data;

        ProviderCacheBuilder(String p_254186_) {
            this(p_254186_, new ConcurrentHashMap());
        }

        private ProviderCacheBuilder(String f_252424_, ConcurrentMap<Path, HashCode> f_252466_) {
            this.version = f_252424_;
            this.data = f_252466_;
        }

        public void put(Path p_254121_, HashCode p_254288_) {
            this.data.put(p_254121_, p_254288_);
        }

        public HashCache.ProviderCache build() {
            return new HashCache.ProviderCache(this.version, ImmutableMap.copyOf(this.data));
        }
    }

    @FunctionalInterface
    public interface UpdateFunction {

        CompletableFuture<?> update(CachedOutput var1);
    }

    public static record UpdateResult(String f_252422_, HashCache.ProviderCache f_252528_, int f_252492_) {

        private final String providerId;

        private final HashCache.ProviderCache cache;

        private final int writes;

        public UpdateResult(String f_252422_, HashCache.ProviderCache f_252528_, int f_252492_) {
            this.providerId = f_252422_;
            this.cache = f_252528_;
            this.writes = f_252492_;
        }
    }
}