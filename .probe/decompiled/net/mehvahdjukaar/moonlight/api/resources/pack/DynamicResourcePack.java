package net.mehvahdjukaar.moonlight.api.resources.pack;

import com.google.common.base.Suppliers;
import com.google.gson.JsonElement;
import dev.architectury.injectables.annotations.PlatformOnly;
import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Supplier;
import net.mehvahdjukaar.moonlight.api.integration.ModernFixCompat;
import net.mehvahdjukaar.moonlight.api.platform.PlatHelper;
import net.mehvahdjukaar.moonlight.api.resources.RPUtils;
import net.mehvahdjukaar.moonlight.api.resources.ResType;
import net.mehvahdjukaar.moonlight.api.resources.StaticResource;
import net.mehvahdjukaar.moonlight.api.resources.assets.LangBuilder;
import net.mehvahdjukaar.moonlight.core.CompatHandler;
import net.minecraft.SharedConstants;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.metadata.pack.PackMetadataSection;
import net.minecraft.server.packs.metadata.pack.PackMetadataSectionSerializer;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.world.flag.FeatureFlagSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public abstract class DynamicResourcePack implements PackResources {

    private static final List<DynamicResourcePack> INSTANCES = new ArrayList();

    protected static final Logger LOGGER = LogManager.getLogger();

    protected final boolean hidden;

    protected final boolean fixed;

    protected final Pack.Position position;

    protected final PackType packType;

    protected final Supplier<PackMetadataSection> metadata;

    protected final Component title;

    protected final ResourceLocation resourcePackName;

    protected final Set<String> namespaces = new HashSet();

    protected final Map<ResourceLocation, byte[]> resources = new ConcurrentHashMap();

    protected final Map<String, byte[]> rootResources = new ConcurrentHashMap();

    protected final String mainNamespace;

    protected boolean clearOnReload = true;

    protected Set<ResourceLocation> staticResources = new HashSet();

    protected boolean generateDebugResources;

    boolean addToStatic = false;

    private static final boolean MODERN_FIX = CompatHandler.MODERNFIX && ModernFixCompat.areLazyResourcesOn();

    @Internal
    public static void clearAfterReload(PackType targetType) {
        for (DynamicResourcePack p : INSTANCES) {
            if (p.packType == targetType) {
                p.clearNonStatic();
            }
        }
    }

    @Internal
    public static void clearBeforeReload(PackType targetType) {
        for (DynamicResourcePack p : INSTANCES) {
            if (p.packType == targetType) {
                p.clearAllContent();
            }
        }
    }

    protected DynamicResourcePack(ResourceLocation name, PackType type) {
        this(name, type, Pack.Position.TOP, false, false);
    }

    protected DynamicResourcePack(ResourceLocation name, PackType type, Pack.Position position, boolean fixed, boolean hidden) {
        this.packType = type;
        this.resourcePackName = name;
        this.mainNamespace = name.getNamespace();
        this.namespaces.add(name.getNamespace());
        this.title = Component.translatable(LangBuilder.getReadableName(name.toString()));
        this.position = position;
        this.fixed = fixed;
        this.hidden = hidden;
        this.metadata = Suppliers.memoize(() -> new PackMetadataSection(this.makeDescription(), SharedConstants.getCurrentVersion().getPackVersion(type)));
        this.generateDebugResources = PlatHelper.isDev();
    }

    public Component makeDescription() {
        return Component.translatable(LangBuilder.getReadableName(this.mainNamespace + "_dynamic_resources"));
    }

    public void setClearOnReload(boolean canBeCleared) {
        this.clearOnReload = canBeCleared;
    }

    @Deprecated(forRemoval = true)
    public void clearOnReload(boolean canBeCleared) {
        this.clearOnReload = canBeCleared;
    }

    public void markNotClearable(ResourceLocation staticResources) {
        this.staticResources.add(staticResources);
    }

    public void unMarkNotClearable(ResourceLocation staticResources) {
        this.staticResources.remove(staticResources);
    }

    public void setGenerateDebugResources(boolean generateDebugResources) {
        this.generateDebugResources = generateDebugResources;
    }

    public void addNamespaces(String... namespaces) {
        this.namespaces.addAll(Arrays.asList(namespaces));
    }

    public Component getTitle() {
        return this.title;
    }

    @Override
    public String packId() {
        return this.title.getString();
    }

    public ResourceLocation id() {
        return this.resourcePackName;
    }

    public String toString() {
        return this.packId();
    }

    public void registerPack() {
        if (!INSTANCES.contains(this)) {
            PlatHelper.registerResourcePack(this.packType, () -> Pack.create(this.packId(), this.getTitle(), true, s -> this, new Pack.Info(((PackMetadataSection) this.metadata.get()).getDescription(), ((PackMetadataSection) this.metadata.get()).getPackFormat(), FeatureFlagSet.of()), this.packType, Pack.Position.TOP, this.fixed, PackSource.BUILT_IN));
            INSTANCES.add(this);
        }
    }

    @PlatformOnly({ "forge" })
    public boolean isHidden() {
        return this.hidden;
    }

    @Override
    public Set<String> getNamespaces(PackType packType) {
        return this.namespaces;
    }

    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> serializer) {
        return (T) (serializer instanceof PackMetadataSectionSerializer ? this.metadata : null);
    }

    public void addRootResource(String name, byte[] resource) {
        this.rootResources.put(name, resource);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... strings) {
        String fileName = String.join("/", strings);
        byte[] resource = (byte[]) this.rootResources.get(fileName);
        return resource == null ? null : () -> new ByteArrayInputStream(resource);
    }

    @Override
    public void listResources(PackType packType, String namespace, String id, PackResources.ResourceOutput output) {
        if (packType == this.packType && this.namespaces.contains(namespace)) {
            this.resources.entrySet().stream().filter(r -> ((ResourceLocation) r.getKey()).getNamespace().equals(namespace) && ((ResourceLocation) r.getKey()).getPath().startsWith(id)).forEach(r -> output.accept((ResourceLocation) r.getKey(), (IoSupplier) () -> new ByteArrayInputStream((byte[]) r.getValue())));
        }
    }

    @Override
    public IoSupplier<InputStream> getResource(PackType type, ResourceLocation id) {
        byte[] res = (byte[]) this.resources.get(id);
        return res != null ? () -> {
            if (type != this.packType) {
                throw new IOException(String.format("Tried to access wrong type of resource on %s.", this.resourcePackName));
            } else {
                return new ByteArrayInputStream(res);
            }
        } : null;
    }

    @Override
    public void close() {
    }

    public FileNotFoundException makeFileNotFoundException(String path) {
        return new FileNotFoundException(String.format("'%s' in ResourcePack '%s'", path, this.resourcePackName));
    }

    protected void addBytes(ResourceLocation path, byte[] bytes) {
        this.namespaces.add(path.getNamespace());
        this.resources.put(path, bytes);
        if (this.addToStatic) {
            this.markNotClearable(path);
        }
        if (this.generateDebugResources) {
            try {
                Path p = Paths.get("debug", "generated_resource_pack").resolve(path.getNamespace() + "/" + path.getPath());
                Files.createDirectories(p.getParent());
                Files.write(p, bytes, new OpenOption[] { StandardOpenOption.CREATE, StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING });
            } catch (IOException var4) {
            }
        }
    }

    public void removeResource(ResourceLocation res) {
        this.resources.remove(res);
        this.staticResources.remove(res);
    }

    public void addResource(StaticResource resource) {
        this.addBytes(resource.location, resource.data);
    }

    private void addJson(ResourceLocation path, JsonElement json) {
        try {
            this.addBytes(path, RPUtils.serializeJson(json).getBytes());
        } catch (IOException var4) {
            LOGGER.error("Failed to write JSON {} to resource pack {}.", path, this.resourcePackName, var4);
        }
    }

    public void addJson(ResourceLocation location, JsonElement json, ResType resType) {
        this.addJson(resType.getPath(location), json);
    }

    public void addBytes(ResourceLocation location, byte[] bytes, ResType resType) {
        this.addBytes(resType.getPath(location), bytes);
    }

    public PackType getPackType() {
        return this.packType;
    }

    @Internal
    protected void clearNonStatic() {
        boolean mf = MODERN_FIX && this.getPackType() == PackType.CLIENT_RESOURCES;
        for (ResourceLocation r : this.resources.keySet()) {
            if ((!mf || !this.modernFixHack(r)) && !this.staticResources.contains(r)) {
                this.resources.remove(r);
            }
        }
    }

    @Internal
    protected void clearAllContent() {
        if (this.clearOnReload) {
            for (ResourceLocation r : this.resources.keySet()) {
                this.resources.remove(r);
            }
        }
    }

    private boolean modernFixHack(ResourceLocation r) {
        String s = r.getPath();
        return s.startsWith("model") || s.startsWith("blockstate");
    }
}