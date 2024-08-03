package snownee.kiwi.customization;

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
import net.minecraft.server.packs.resources.CloseableResourceManager;
import net.minecraft.server.packs.resources.FallbackResourceManager;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceFilterSection;
import net.minecraft.server.packs.resources.ResourceManager;
import org.slf4j.Logger;

public class KiwiPackResourceManager implements CloseableResourceManager {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Map<String, FallbackResourceManager> namespacedManagers;

    private final List<PackResources> packs;

    public KiwiPackResourceManager(List<PackResources> packs) {
        this.packs = List.copyOf(packs);
        Map<String, FallbackResourceManager> map = new HashMap();
        List<String> list = packs.stream().flatMap(p_215471_ -> p_215471_.getNamespaces(PackType.CLIENT_RESOURCES).stream()).distinct().toList();
        for (PackResources packresources : packs) {
            ResourceFilterSection resourcefiltersection = this.getPackFilterSection(packresources);
            Set<String> set = packresources.getNamespaces(PackType.CLIENT_RESOURCES);
            Predicate<ResourceLocation> predicate = resourcefiltersection != null ? p_215474_ -> resourcefiltersection.isPathFiltered(p_215474_.getPath()) : null;
            for (String s : list) {
                boolean flag = set.contains(s);
                boolean flag1 = resourcefiltersection != null && resourcefiltersection.isNamespaceFiltered(s);
                if (flag || flag1) {
                    FallbackResourceManager fallbackresourcemanager = (FallbackResourceManager) map.computeIfAbsent(s, s1 -> new FallbackResourceManager(PackType.CLIENT_RESOURCES, s1));
                    if (flag && flag1) {
                        fallbackresourcemanager.push(packresources, predicate);
                    } else if (flag) {
                        fallbackresourcemanager.push(packresources);
                    } else {
                        fallbackresourcemanager.pushFilterOnly(packresources.packId(), predicate);
                    }
                }
            }
        }
        this.namespacedManagers = map;
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
        ResourceManager resourcemanager = (ResourceManager) this.namespacedManagers.get(resourceLocation0.getNamespace());
        return resourcemanager != null ? resourcemanager.m_213713_(resourceLocation0) : Optional.empty();
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation0) {
        ResourceManager resourcemanager = (ResourceManager) this.namespacedManagers.get(resourceLocation0.getNamespace());
        return resourcemanager != null ? resourcemanager.getResourceStack(resourceLocation0) : List.of();
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        checkTrailingDirectoryPath(string0);
        Map<ResourceLocation, Resource> map = new TreeMap();
        for (FallbackResourceManager fallbackresourcemanager : this.namespacedManagers.values()) {
            map.putAll(fallbackresourcemanager.listResources(string0, predicateResourceLocation1));
        }
        return map;
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        checkTrailingDirectoryPath(string0);
        Map<ResourceLocation, List<Resource>> map = new TreeMap();
        for (FallbackResourceManager fallbackresourcemanager : this.namespacedManagers.values()) {
            map.putAll(fallbackresourcemanager.listResourceStacks(string0, predicateResourceLocation1));
        }
        return map;
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