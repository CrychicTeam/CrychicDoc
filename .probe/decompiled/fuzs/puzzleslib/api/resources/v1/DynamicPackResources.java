package fuzs.puzzleslib.api.resources.v1;

import com.google.common.base.Stopwatch;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import fuzs.puzzleslib.api.data.v2.core.DataProviderContext;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import net.minecraft.FileUtil;
import net.minecraft.data.DataProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackResources;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.IoSupplier;
import org.jetbrains.annotations.Nullable;

public class DynamicPackResources extends AbstractModPackResources {

    public static final Map<String, PackType> PATHS_FOR_TYPE = (Map<String, PackType>) Stream.of(PackType.values()).collect(ImmutableMap.toImmutableMap(PackType::m_10305_, Function.identity()));

    protected final DataProviderContext.Factory[] factories;

    protected Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> paths;

    protected DynamicPackResources(DataProviderContext.Factory... factories) {
        this.factories = factories;
    }

    public static Supplier<AbstractModPackResources> create(DataProviderContext.Factory... factories) {
        return () -> new DynamicPackResources(factories);
    }

    protected static Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> generatePathsFromProviders(String modId, DataProviderContext.Factory... factories) {
        PuzzlesLib.LOGGER.info("Running data generation for dynamic pack resources provided by '{}'", modId);
        Stopwatch stopwatch = Stopwatch.createStarted();
        DataProviderContext context = DataProviderContext.fromModId(modId);
        Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> paths;
        try {
            Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>> packTypes = (Map<PackType, Map<ResourceLocation, IoSupplier<InputStream>>>) Stream.of(PackType.values()).collect(Collectors.toMap(Function.identity(), $ -> Maps.newConcurrentMap()));
            for (DataProviderContext.Factory provider : factories) {
                ((DataProvider) provider.apply(context)).run((filePath, data, hashCode) -> {
                    List<String> strings = (List<String>) FileUtil.decomposePath(filePath.normalize().toString().replace(File.separator, "/")).get().left().filter(list -> list.size() >= 2).orElse(null);
                    if (strings != null) {
                        PackType packType = (PackType) PATHS_FOR_TYPE.get(strings.get(0));
                        Objects.requireNonNull(packType, "pack type for directory %s is null".formatted(strings.get(0)));
                        String path = (String) strings.stream().skip(2L).collect(Collectors.joining("/"));
                        ResourceLocation resourceLocation = ResourceLocation.tryBuild((String) strings.get(1), path);
                        if (resourceLocation != null) {
                            ((Map) packTypes.get(packType)).put(resourceLocation, (IoSupplier) () -> new ByteArrayInputStream(data));
                        }
                    }
                }).get();
            }
            packTypes.replaceAll((packType, map) -> ImmutableMap.copyOf(map));
            paths = Maps.immutableEnumMap(packTypes);
        } catch (Throwable var10) {
            PuzzlesLib.LOGGER.error("Unable to complete data generation for dynamic pack resources provided by '{}'", modId, var10);
            paths = Map.of();
        }
        PuzzlesLib.LOGGER.info("Data generation for dynamic pack resources provided by '{}' took {}ms", modId, stopwatch.stop().elapsed().toMillis());
        return paths;
    }

    @Override
    protected void setup() {
        this.paths = generatePathsFromProviders(this.getNamespace(), this.factories);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType, ResourceLocation location) {
        Objects.requireNonNull(this.paths, "paths is null");
        return (IoSupplier<InputStream>) ((Map) this.paths.get(packType)).get(location);
    }

    @Override
    public void listResources(PackType packType, String namespace, String path, PackResources.ResourceOutput resourceOutput) {
        Objects.requireNonNull(this.paths, "paths is null");
        ((Map) this.paths.get(packType)).entrySet().stream().filter(entry -> ((ResourceLocation) entry.getKey()).getNamespace().equals(namespace) && ((ResourceLocation) entry.getKey()).getPath().startsWith(path)).forEach(entry -> resourceOutput.accept((ResourceLocation) entry.getKey(), (IoSupplier) entry.getValue()));
    }

    @Override
    public Set<String> getNamespaces(PackType type) {
        Objects.requireNonNull(this.paths, "paths is null");
        return (Set<String>) ((Map) this.paths.get(type)).keySet().stream().map(ResourceLocation::m_135827_).collect(Collectors.toSet());
    }
}