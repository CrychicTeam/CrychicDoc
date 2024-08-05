package net.minecraft.server.packs.resources;

import com.google.common.collect.Lists;
import com.mojang.logging.LogUtils;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.util.Unit;
import org.slf4j.Logger;

public class ReloadableResourceManager implements ResourceManager, AutoCloseable {

    private static final Logger LOGGER = LogUtils.getLogger();

    private CloseableResourceManager resources;

    private final List<PreparableReloadListener> listeners = Lists.newArrayList();

    private final PackType type;

    public ReloadableResourceManager(PackType packType0) {
        this.type = packType0;
        this.resources = new MultiPackResourceManager(packType0, List.of());
    }

    public void close() {
        this.resources.close();
    }

    public void registerReloadListener(PreparableReloadListener preparableReloadListener0) {
        this.listeners.add(preparableReloadListener0);
    }

    public ReloadInstance createReload(Executor executor0, Executor executor1, CompletableFuture<Unit> completableFutureUnit2, List<PackResources> listPackResources3) {
        LOGGER.info("Reloading ResourceManager: {}", LogUtils.defer(() -> listPackResources3.stream().map(PackResources::m_5542_).collect(Collectors.joining(", "))));
        this.resources.close();
        this.resources = new MultiPackResourceManager(this.type, listPackResources3);
        return SimpleReloadInstance.create(this.resources, this.listeners, executor0, executor1, completableFutureUnit2, LOGGER.isDebugEnabled());
    }

    @Override
    public Optional<Resource> getResource(ResourceLocation resourceLocation0) {
        return this.resources.m_213713_(resourceLocation0);
    }

    @Override
    public Set<String> getNamespaces() {
        return this.resources.m_7187_();
    }

    @Override
    public List<Resource> getResourceStack(ResourceLocation resourceLocation0) {
        return this.resources.m_213829_(resourceLocation0);
    }

    @Override
    public Map<ResourceLocation, Resource> listResources(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        return this.resources.m_214159_(string0, predicateResourceLocation1);
    }

    @Override
    public Map<ResourceLocation, List<Resource>> listResourceStacks(String string0, Predicate<ResourceLocation> predicateResourceLocation1) {
        return this.resources.m_214160_(string0, predicateResourceLocation1);
    }

    @Override
    public Stream<PackResources> listPacks() {
        return this.resources.m_7536_();
    }
}