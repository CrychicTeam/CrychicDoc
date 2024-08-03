package org.embeddedt.modernfix.resources;

import com.google.common.base.Joiner;
import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableMap.Builder;
import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.io.IOException;
import java.nio.file.FileVisitOption;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import org.embeddedt.modernfix.ModernFix;
import org.embeddedt.modernfix.platform.ModernFixPlatformHooks;
import org.embeddedt.modernfix.util.PackTypeHelper;

public class PackResourcesCacheEngine {

    private static final Joiner SLASH_JOINER = Joiner.on('/');

    private final Map<PackType, Set<String>> namespacesByType;

    private final Set<CachedResourcePath> containedPaths;

    private final EnumMap<PackType, Map<String, List<CachedResourcePath>>> resourceListings;

    private volatile boolean cacheGenerationFlag = false;

    private List<Runnable> cacheGenerationTasks = new ArrayList();

    private Path debugPath;

    private static final WeakHashMap<ICachingResourcePack, Boolean> cachingPacks = new WeakHashMap();

    public PackResourcesCacheEngine(Function<PackType, Set<String>> namespacesRetriever, BiFunction<PackType, String, Path> basePathRetriever) {
        this.namespacesByType = new EnumMap(PackType.class);
        for (PackType type : PackType.values()) {
            if (PackTypeHelper.isVanillaPackType(type)) {
                this.namespacesByType.put(type, (Set) namespacesRetriever.apply(type));
            }
        }
        this.containedPaths = new ObjectOpenHashSet();
        this.resourceListings = new EnumMap(PackType.class);
        this.debugPath = ((Path) basePathRetriever.apply(PackType.CLIENT_RESOURCES, "minecraft")).toAbsolutePath();
        for (PackType typex : PackType.values()) {
            Collection<String> namespaces = PackTypeHelper.isVanillaPackType(typex) ? (Collection) this.namespacesByType.get(typex) : (Collection) namespacesRetriever.apply(typex);
            Collection<Pair<String, Path>> namespacedRoots = (Collection<Pair<String, Path>>) namespaces.stream().map(s -> Pair.of(s, ((Path) basePathRetriever.apply(type, s)).toAbsolutePath())).collect(Collectors.toList());
            this.cacheGenerationTasks.add((Runnable) () -> {
                Builder<String, List<CachedResourcePath>> packTypedMap = ImmutableMap.builder();
                for (Pair<String, Path> pair : namespacedRoots) {
                    try {
                        com.google.common.collect.ImmutableList.Builder<CachedResourcePath> namespacedList = ImmutableList.builder();
                        String namespace = (String) pair.getFirst();
                        Path root = (Path) pair.getSecond();
                        String[] prefix = new String[] { type.getDirectory(), namespace };
                        Stream<Path> stream = Files.find(root, Integer.MAX_VALUE, (p, a) -> a.isRegularFile(), new FileVisitOption[0]);
                        try {
                            stream.map(path -> root.relativize(path.toAbsolutePath())).filter(PackResourcesCacheEngine::isValidCachedResourcePath).forEach(path -> {
                                CachedResourcePath cachedPath = new CachedResourcePath(prefix, path);
                                synchronized (this.containedPaths) {
                                    this.containedPaths.add(cachedPath);
                                }
                                namespacedList.add(cachedPath);
                            });
                        } catch (Throwable var16) {
                            if (stream != null) {
                                try {
                                    stream.close();
                                } catch (Throwable var15) {
                                    var16.addSuppressed(var15);
                                }
                            }
                            throw var16;
                        }
                        if (stream != null) {
                            stream.close();
                        }
                        packTypedMap.put(namespace, namespacedList.build());
                    } catch (IOException var17) {
                    }
                }
                synchronized (this.resourceListings) {
                    this.resourceListings.put(type, packTypedMap.build());
                }
            });
        }
        this.cacheGenerationTasks.add((Runnable) () -> ((ObjectOpenHashSet) this.containedPaths).trim());
    }

    private static boolean isValidCachedResourcePath(Path path) {
        if (path.getFileName() != null && path.getNameCount() != 0) {
            String str = SLASH_JOINER.join(path);
            if (str.length() == 0) {
                return false;
            } else {
                for (int i = 0; i < str.length(); i++) {
                    if (!ResourceLocation.validPathChar(str.charAt(i))) {
                        return false;
                    }
                }
                return true;
            }
        } else {
            return false;
        }
    }

    public Set<String> getNamespaces(PackType type) {
        return PackTypeHelper.isVanillaPackType(type) ? (Set) this.namespacesByType.get(type) : null;
    }

    private void doGenerateCache() {
        Stopwatch watch = Stopwatch.createStarted();
        for (Runnable r : this.cacheGenerationTasks) {
            r.run();
        }
        watch.stop();
        ModernFix.LOGGER.debug("Generated cache for {} in {}", this.debugPath, watch);
        this.debugPath = null;
        this.cacheGenerationTasks = ImmutableList.of();
    }

    private void awaitLoad() {
        if (!this.cacheGenerationFlag) {
            synchronized (this) {
                if (!this.cacheGenerationFlag) {
                    this.doGenerateCache();
                    this.cacheGenerationFlag = true;
                }
            }
        }
    }

    public boolean hasResource(String path) {
        this.awaitLoad();
        return this.containedPaths.contains(new CachedResourcePath(path));
    }

    public boolean hasResource(String[] paths) {
        this.awaitLoad();
        return this.containedPaths.contains(new CachedResourcePath(paths));
    }

    public Collection<ResourceLocation> getResources(PackType type, String resourceNamespace, String pathIn, int maxDepth, Predicate<ResourceLocation> filter) {
        if (!PackTypeHelper.isVanillaPackType(type)) {
            throw new IllegalArgumentException("Only vanilla PackTypes are supported");
        } else {
            this.awaitLoad();
            List<CachedResourcePath> paths = (List<CachedResourcePath>) ((Map) this.resourceListings.get(type)).getOrDefault(resourceNamespace, Collections.emptyList());
            if (paths.isEmpty()) {
                return Collections.emptyList();
            } else {
                String testPath = pathIn.endsWith("/") ? pathIn : pathIn + "/";
                ArrayList<ResourceLocation> resources = new ArrayList();
                for (CachedResourcePath cachePath : paths) {
                    if (cachePath.getNameCount() - 2 <= maxDepth) {
                        String fullPath = cachePath.getFullPath(2);
                        String fullTestPath = fullPath.endsWith("/") ? fullPath : fullPath + "/";
                        if (fullTestPath.startsWith(testPath)) {
                            ResourceLocation foundResource = new ResourceLocation(resourceNamespace, fullPath);
                            if (filter.test(foundResource)) {
                                resources.add(foundResource);
                            }
                        }
                    }
                }
                return resources;
            }
        }
    }

    public static void track(ICachingResourcePack pack) {
        synchronized (cachingPacks) {
            cachingPacks.put(pack, Boolean.TRUE);
        }
    }

    public static void invalidate() {
        if (ModernFixPlatformHooks.INSTANCE.isDevEnv()) {
            synchronized (cachingPacks) {
                cachingPacks.keySet().forEach(pack -> {
                    if (pack != null) {
                        pack.invalidateCache();
                    }
                });
            }
        }
    }
}