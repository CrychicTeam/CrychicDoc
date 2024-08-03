package net.minecraft.server.packs.resources;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.TreeMap;
import java.util.function.Predicate;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import org.slf4j.Logger;

public class MultiPackResourceManager implements CloseableResourceManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, FallbackResourceManager> namespacedManagers;

    private final List<PackResources> packs;

    public MultiPackResourceManager(PackType packType0, List<PackResources> listPackResources1) {
        this.packs = List.copyOf(listPackResources1);
        Map<String, FallbackResourceManager> $$2 = new HashMap();
        List<String> $$3 = listPackResources1.stream().flatMap(p_215471_ -> p_215471_.getNamespaces(packType0).stream()).distinct().toList();
        for (PackResources $$4 : listPackResources1) {
            ResourceFilterSection $$5 = this.getPackFilterSection($$4);
            Set<String> $$6 = $$4.getNamespaces(packType0);
            Predicate<ResourceLocation> $$7 = $$5 != null ? p_215474_ -> $$5.isPathFiltered(p_215474_.getPath()) : null;
            for (String $$8 : $$3) {
                boolean $$9 = $$6.contains($$8);
                boolean $$10 = $$5 != null && $$5.isNamespaceFiltered($$8);
                if ($$9 || $$10) {
                    FallbackResourceManager $$11 = (FallbackResourceManager) $$2.get($$8);
                    if ($$11 == null) {
                        $$11 = new FallbackResourceManager(packType0, $$8);
                        $$2.put($$8, $$11);
                    }
                    if ($$9 && $$10) {
                        $$11.push($$4, $$7);
                    } else if ($$9) {
                        $$11.push($$4);
                    } else {
                        $$11.pushFilterOnly($$4.packId(), $$7);
                    }
                }
            }
        }
        this.namespacedManagers = $$2;
    }

    @Nullable
    private ResourceFilterSection getPackFilterSection(PackResources packResources0) {
        try {
            return packResources0.getMetadataSection(ResourceFilterSection.TYPE);
        } catch (IOException var3) {
            LOGGER.error("Failed to get filter section from pack {}", packResources0.packId());
            return null;
        }
    }

    @Override
    public Set<String> getNamespaces() {
        return this.namespacedManagers.keySet();
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation resourceLocation0) {
        ResourceManager $$1 = (ResourceManager) this.namespacedManagers.get(resourceLocation0.getNamespace());
        return $$1 != null ? $$1.m_213713_(resourceLocation0) : Optional.empty();
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation0) {
        ResourceManager $$1 = (ResourceManager) this.namespacedManagers.get(resourceLocation0.getNamespace());
        return $$1 != null ? $$1.getResourceStack(resourceLocation0) : List.of();
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        checkTrailingDirectoryPath(string0);
        Map<ResourceLocation, Resource> $$2 = new TreeMap();
        for (FallbackResourceManager $$3 : this.namespacedManagers.values()) {
            $$2.putAll($$3.listResources(string0, predicateResourceLocation1));
        }
        return $$2;
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        checkTrailingDirectoryPath(string0);
        Map<ResourceLocation, List<Resource>> $$2 = new TreeMap();
        for (FallbackResourceManager $$3 : this.namespacedManagers.values()) {
            $$2.putAll($$3.listResourceStacks(string0, predicateResourceLocation1));
        }
        return $$2;
    }

    private static void checkTrailingDirectoryPath(String string0) {
        if (string0.endsWith("/")) {
            throw new IllegalArgumentException("Trailing slash in path " + string0);
        }
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.packs.stream();
    }

    @Override
    public void close() {
        this.packs.forEach(PackResources::close);
    }
}