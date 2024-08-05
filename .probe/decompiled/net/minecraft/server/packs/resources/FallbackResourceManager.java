package net.minecraft.server.packs.resources;

import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

public class FallbackResourceManager implements ResourceManager {

    static final Logger LOGGER = LogUtils.getLogger();

    protected final List<FallbackResourceManager.PackEntry> fallbacks = Lists.newArrayList();

    private final PackType type;

    private final String namespace;

    public FallbackResourceManager(PackType packType0, String string1) {
        this.type = packType0;
        this.namespace = string1;
    }

    public void push(PackResources packResources0) {
        this.pushInternal(packResources0.packId(), packResources0, null);
    }

    public void push(PackResources packResources0, Predicate<ResourceLocation> predicateResourceLocation1) {
        this.pushInternal(packResources0.packId(), packResources0, predicateResourceLocation1);
    }

    public void pushFilterOnly(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        this.pushInternal(string0, null, predicateResourceLocation1);
    }

    private void pushInternal(String string0, @Nullable PackResources packResources1, @Nullable Predicate<ResourceLocation> predicateResourceLocation2) {
        this.fallbacks.add(new FallbackResourceManager.PackEntry(string0, packResources1, predicateResourceLocation2));
    }

    @Override
    public Set<String> getNamespaces() {
        return ImmutableSet.of(this.namespace);
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation resourceLocation0) {
        for (int $$1 = this.fallbacks.size() - 1; $$1 >= 0; $$1--) {
            FallbackResourceManager.PackEntry $$2 = (FallbackResourceManager.PackEntry) this.fallbacks.get($$1);
            PackResources $$3 = $$2.resources;
            if ($$3 != null) {
                IoSupplier<InputStream> $$4 = $$3.getResource(this.type, resourceLocation0);
                if ($$4 != null) {
                    IoSupplier<ResourceMetadata> $$5 = this.createStackMetadataFinder(resourceLocation0, $$1);
                    return Optional.of(createResource($$3, resourceLocation0, $$4, $$5));
                }
            }
            if ($$2.isFiltered(resourceLocation0)) {
                LOGGER.warn("Resource {} not found, but was filtered by pack {}", resourceLocation0, $$2.name);
                return Optional.empty();
            }
        }
        return Optional.empty();
    }

    private static Resource createResource(PackResources packResources0, ResourceLocation resourceLocation1, IoSupplier<InputStream> ioSupplierInputStream2, IoSupplier<ResourceMetadata> ioSupplierResourceMetadata3) {
        return new Resource(packResources0, wrapForDebug(resourceLocation1, packResources0, ioSupplierInputStream2), ioSupplierResourceMetadata3);
    }

    private static IoSupplier<InputStream> wrapForDebug(ResourceLocation resourceLocation0, PackResources packResources1, IoSupplier<InputStream> ioSupplierInputStream2) {
        return LOGGER.isDebugEnabled() ? () -> new FallbackResourceManager.LeakedResourceWarningInputStream(ioSupplierInputStream2.get(), resourceLocation0, packResources1.packId()) : ioSupplierInputStream2;
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation0) {
        ResourceLocation $$1 = getMetadataLocation(resourceLocation0);
        List<Resource> $$2 = new ArrayList();
        boolean $$3 = false;
        String $$4 = null;
        for (int $$5 = this.fallbacks.size() - 1; $$5 >= 0; $$5--) {
            FallbackResourceManager.PackEntry $$6 = (FallbackResourceManager.PackEntry) this.fallbacks.get($$5);
            PackResources $$7 = $$6.resources;
            if ($$7 != null) {
                IoSupplier<InputStream> $$8 = $$7.getResource(this.type, resourceLocation0);
                if ($$8 != null) {
                    IoSupplier<ResourceMetadata> $$9;
                    if ($$3) {
                        $$9 = ResourceMetadata.EMPTY_SUPPLIER;
                    } else {
                        $$9 = () -> {
                            IoSupplier<InputStream> $$2x = $$7.getResource(this.type, $$1);
                            return $$2x != null ? parseMetadata($$2x) : ResourceMetadata.EMPTY;
                        };
                    }
                    $$2.add(new Resource($$7, $$8, $$9));
                }
            }
            if ($$6.isFiltered(resourceLocation0)) {
                $$4 = $$6.name;
                break;
            }
            if ($$6.isFiltered($$1)) {
                $$3 = true;
            }
        }
        if ($$2.isEmpty() && $$4 != null) {
            LOGGER.warn("Resource {} not found, but was filtered by pack {}", resourceLocation0, $$4);
        }
        return Lists.reverse($$2);
    }

    private static boolean isMetadata(ResourceLocation resourceLocation0) {
        return resourceLocation0.getPath().endsWith(".mcmeta");
    }

    private static ResourceLocation getResourceLocationFromMetadata(ResourceLocation resourceLocation0) {
        String $$1 = resourceLocation0.getPath().substring(0, resourceLocation0.getPath().length() - ".mcmeta".length());
        return resourceLocation0.withPath($$1);
    }

    static ResourceLocation getMetadataLocation(ResourceLocation resourceLocation0) {
        return resourceLocation0.withPath(resourceLocation0.getPath() + ".mcmeta");
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        Map<ResourceLocation, ResourceWithSourceAndIndex> $$2 = new HashMap();
        Map<ResourceLocation, ResourceWithSourceAndIndex> $$3 = new HashMap();
        int $$4 = this.fallbacks.size();
        for (int $$5 = 0; $$5 < $$4; $$5++) {
            FallbackResourceManager.PackEntry $$6 = (FallbackResourceManager.PackEntry) this.fallbacks.get($$5);
            $$6.filterAll($$2.keySet());
            $$6.filterAll($$3.keySet());
            PackResources $$7 = $$6.resources;
            if ($$7 != null) {
                int $$8 = $$5;
                $$7.listResources(this.type, this.namespace, string0, (p_248254_, p_248255_) -> {
                    record ResourceWithSourceAndIndex(PackResources f_243853_, IoSupplier<InputStream> f_244005_, int f_244110_) {

                        private final PackResources packResources;

                        private final IoSupplier<InputStream> resource;

                        private final int packIndex;

                        ResourceWithSourceAndIndex(PackResources f_243853_, IoSupplier<InputStream> f_244005_, int f_244110_) {
                            this.packResources = f_243853_;
                            this.resource = f_244005_;
                            this.packIndex = f_244110_;
                        }
                    }
                    if (isMetadata(p_248254_)) {
                        if (predicateResourceLocation1.test(getResourceLocationFromMetadata(p_248254_))) {
                            $$3.put(p_248254_, new ResourceWithSourceAndIndex($$7, p_248255_, $$8));
                        }
                    } else if (predicateResourceLocation1.test(p_248254_)) {
                        $$2.put(p_248254_, new ResourceWithSourceAndIndex($$7, p_248255_, $$8));
                    }
                });
            }
        }
        Map<ResourceLocation, Resource> $$9 = Maps.newTreeMap();
        $$2.forEach((p_248258_, p_248259_) -> {
            ResourceLocation $$4x = getMetadataLocation(p_248258_);
            ResourceWithSourceAndIndex $$5x = (ResourceWithSourceAndIndex) $$3.get($$4x);
            IoSupplier<ResourceMetadata> $$6x;
            if ($$5x != null && $$5x.packIndex >= p_248259_.packIndex) {
                $$6x = convertToMetadata($$5x.resource);
            } else {
                $$6x = ResourceMetadata.EMPTY_SUPPLIER;
            }
            $$9.put(p_248258_, createResource(p_248259_.packResources, p_248258_, p_248259_.resource, $$6x));
        });
        return $$9;
    }

    private IoSupplier<ResourceMetadata> createStackMetadataFinder(ResourceLocation resourceLocation0, int int1) {
        return () -> {
            ResourceLocation $$2 = getMetadataLocation(resourceLocation0);
            for (int $$3 = this.fallbacks.size() - 1; $$3 >= int1; $$3--) {
                FallbackResourceManager.PackEntry $$4 = (FallbackResourceManager.PackEntry) this.fallbacks.get($$3);
                PackResources $$5 = $$4.resources;
                if ($$5 != null) {
                    IoSupplier<InputStream> $$6 = $$5.getResource(this.type, $$2);
                    if ($$6 != null) {
                        return parseMetadata($$6);
                    }
                }
                if ($$4.isFiltered($$2)) {
                    break;
                }
            }
            return ResourceMetadata.EMPTY;
        };
    }

    private static IoSupplier<ResourceMetadata> convertToMetadata(IoSupplier<InputStream> ioSupplierInputStream0) {
        return () -> parseMetadata(ioSupplierInputStream0);
    }

    private static ResourceMetadata parseMetadata(IoSupplier<InputStream> ioSupplierInputStream0) throws IOException {
        InputStream $$1 = ioSupplierInputStream0.get();
        ResourceMetadata var2;
        try {
            var2 = ResourceMetadata.fromJsonStream($$1);
        } catch (Throwable var5) {
            if ($$1 != null) {
                try {
                    $$1.close();
                } catch (Throwable var4) {
                    var5.addSuppressed(var4);
                }
            }
            throw var5;
        }
        if ($$1 != null) {
            $$1.close();
        }
        return var2;
    }

    private static void applyPackFiltersToExistingResources(FallbackResourceManager.PackEntry fallbackResourceManagerPackEntry0, Map<ResourceLocation, FallbackResourceManager.EntryStack> mapResourceLocationFallbackResourceManagerEntryStack1) {
        for (FallbackResourceManager.EntryStack $$2 : mapResourceLocationFallbackResourceManagerEntryStack1.values()) {
            if (fallbackResourceManagerPackEntry0.isFiltered($$2.fileLocation)) {
                $$2.fileSources.clear();
            } else if (fallbackResourceManagerPackEntry0.isFiltered($$2.metadataLocation())) {
                $$2.metaSources.clear();
            }
        }
    }

    private void listPackResources(FallbackResourceManager.PackEntry fallbackResourceManagerPackEntry0, String string1, Predicate<ResourceLocation> predicateResourceLocation2, Map<ResourceLocation, FallbackResourceManager.EntryStack> mapResourceLocationFallbackResourceManagerEntryStack3) {
        PackResources $$4 = fallbackResourceManagerPackEntry0.resources;
        if ($$4 != null) {
            $$4.listResources(this.type, this.namespace, string1, (p_248266_, p_248267_) -> {
                if (isMetadata(p_248266_)) {
                    ResourceLocation $$5 = getResourceLocationFromMetadata(p_248266_);
                    if (!predicateResourceLocation2.test($$5)) {
                        return;
                    }
                    ((FallbackResourceManager.EntryStack) mapResourceLocationFallbackResourceManagerEntryStack3.computeIfAbsent($$5, FallbackResourceManager.EntryStack::new)).metaSources.put($$4, p_248267_);
                } else {
                    if (!predicateResourceLocation2.test(p_248266_)) {
                        return;
                    }
                    ((FallbackResourceManager.EntryStack) mapResourceLocationFallbackResourceManagerEntryStack3.computeIfAbsent(p_248266_, FallbackResourceManager.EntryStack::new)).fileSources.add(new FallbackResourceManager.ResourceWithSource($$4, p_248267_));
                }
            });
        }
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        Map<ResourceLocation, FallbackResourceManager.EntryStack> $$2 = Maps.newHashMap();
        for (FallbackResourceManager.PackEntry $$3 : this.fallbacks) {
            applyPackFiltersToExistingResources($$3, $$2);
            this.listPackResources($$3, string0, predicateResourceLocation1, $$2);
        }
        TreeMap<ResourceLocation, List<Resource>> $$4 = Maps.newTreeMap();
        for (FallbackResourceManager.EntryStack $$5 : $$2.values()) {
            if (!$$5.fileSources.isEmpty()) {
                List<Resource> $$6 = new ArrayList();
                for (FallbackResourceManager.ResourceWithSource $$7 : $$5.fileSources) {
                    PackResources $$8 = $$7.source;
                    IoSupplier<InputStream> $$9 = (IoSupplier<InputStream>) $$5.metaSources.get($$8);
                    IoSupplier<ResourceMetadata> $$10 = $$9 != null ? convertToMetadata($$9) : ResourceMetadata.EMPTY_SUPPLIER;
                    $$6.add(createResource($$8, $$5.fileLocation, $$7.resource, $$10));
                }
                $$4.put($$5.fileLocation, $$6);
            }
        }
        return $$4;
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.fallbacks.stream().map(p_215386_ -> p_215386_.resources).filter(Objects::nonNull);
    }

    static record EntryStack(ResourceLocation f_244439_, ResourceLocation f_215420_, List<FallbackResourceManager.ResourceWithSource> f_244329_, Map<PackResources, IoSupplier<InputStream>> f_243777_) {

        private final ResourceLocation fileLocation;

        private final ResourceLocation metadataLocation;

        private final List<FallbackResourceManager.ResourceWithSource> fileSources;

        private final Map<PackResources, IoSupplier<InputStream>> metaSources;

        EntryStack(ResourceLocation p_251350_) {
            this(p_251350_, FallbackResourceManager.getMetadataLocation(p_251350_), new ArrayList(), new Object2ObjectArrayMap());
        }

        private EntryStack(ResourceLocation f_244439_, ResourceLocation f_215420_, List<FallbackResourceManager.ResourceWithSource> f_244329_, Map<PackResources, IoSupplier<InputStream>> f_243777_) {
            this.fileLocation = f_244439_;
            this.metadataLocation = f_215420_;
            this.fileSources = f_244329_;
            this.metaSources = f_243777_;
        }
    }

    static class LeakedResourceWarningInputStream extends FilterInputStream {

        private final Supplier<String> message;

        private boolean closed;

        public LeakedResourceWarningInputStream(InputStream inputStream0, ResourceLocation resourceLocation1, String string2) {
            super(inputStream0);
            Exception $$3 = new Exception("Stacktrace");
            this.message = () -> {
                StringWriter $$3x = new StringWriter();
                $$3.printStackTrace(new PrintWriter($$3x));
                return "Leaked resource: '" + resourceLocation1 + "' loaded from pack: '" + string2 + "'\n" + $$3x;
            };
        }

        public void close() throws IOException {
            super.close();
            this.closed = true;
        }

        protected void finalize() throws Throwable {
            if (!this.closed) {
                FallbackResourceManager.LOGGER.warn("{}", this.message.get());
            }
            super.finalize();
        }
    }

    static record PackEntry(String f_215432_, @Nullable PackResources f_215433_, @Nullable Predicate<ResourceLocation> f_215434_) {

        private final String name;

        @Nullable
        private final PackResources resources;

        @Nullable
        private final Predicate<ResourceLocation> filter;

        PackEntry(String f_215432_, @Nullable PackResources f_215433_, @Nullable Predicate<ResourceLocation> f_215434_) {
            this.name = f_215432_;
            this.resources = f_215433_;
            this.filter = f_215434_;
        }

        public void filterAll(Collection<ResourceLocation> p_215443_) {
            if (this.filter != null) {
                p_215443_.removeIf(this.filter);
            }
        }

        public boolean isFiltered(ResourceLocation p_215441_) {
            return this.filter != null && this.filter.test(p_215441_);
        }
    }

    static record ResourceWithSource(PackResources f_244214_, IoSupplier<InputStream> f_244331_) {

        private final PackResources source;

        private final IoSupplier<InputStream> resource;

        ResourceWithSource(PackResources f_244214_, IoSupplier<InputStream> f_244331_) {
            this.source = f_244214_;
            this.resource = f_244331_;
        }
    }
}