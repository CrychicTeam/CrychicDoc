package net.minecraft.server.packs;

import com.mojang.logging.LogUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.FileUtil;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.metadata.MetadataSectionSerializer;
import net.minecraft.server.packs.resources.IoSupplier;
import net.minecraft.server.packs.resources.Resource;
import net.minecraft.server.packs.resources.ResourceProvider;
import org.slf4j.Logger;

public class VanillaPackResources implements PackResources {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final BuiltInMetadata metadata;

    private final Set<String> namespaces;

    private final List<Path> rootPaths;

    private final Map<PackType, List<Path>> pathsForType;

    VanillaPackResources(BuiltInMetadata builtInMetadata0, Set<String> setString1, List<Path> listPath2, Map<PackType, List<Path>> mapPackTypeListPath3) {
        this.metadata = builtInMetadata0;
        this.namespaces = setString1;
        this.rootPaths = listPath2;
        this.pathsForType = mapPackTypeListPath3;
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getRootResource(String... string0) {
        FileUtil.validatePath(string0);
        List<String> $$1 = List.of(string0);
        for (Path $$2 : this.rootPaths) {
            Path $$3 = FileUtil.resolvePath($$2, $$1);
            if (Files.exists($$3, new LinkOption[0]) && PathPackResources.validatePath($$3)) {
                return IoSupplier.create($$3);
            }
        }
        return null;
    }

    public void listRawPaths(PackType packType0, ResourceLocation resourceLocation1, Consumer<Path> consumerPath2) {
        FileUtil.decomposePath(resourceLocation1.getPath()).get().ifLeft(p_248238_ -> {
            String $$4 = resourceLocation1.getNamespace();
            for (Path $$5 : (List) this.pathsForType.get(packType0)) {
                Path $$6 = $$5.resolve($$4);
                consumerPath2.accept(FileUtil.resolvePath($$6, p_248238_));
            }
        }).ifRight(p_248232_ -> LOGGER.error("Invalid path {}: {}", resourceLocation1, p_248232_.message()));
    }

    @Override
    public void listResources(PackType packType0, String string1, String string2, PackResources.ResourceOutput packResourcesResourceOutput3) {
        FileUtil.decomposePath(string2).get().ifLeft(p_248228_ -> {
            List<Path> $$4 = (List<Path>) this.pathsForType.get(packType0);
            int $$5 = $$4.size();
            if ($$5 == 1) {
                getResources(packResourcesResourceOutput3, string1, (Path) $$4.get(0), p_248228_);
            } else if ($$5 > 1) {
                Map<ResourceLocation, IoSupplier<InputStream>> $$6 = new HashMap();
                for (int $$7 = 0; $$7 < $$5 - 1; $$7++) {
                    getResources($$6::putIfAbsent, string1, (Path) $$4.get($$7), p_248228_);
                }
                Path $$8 = (Path) $$4.get($$5 - 1);
                if ($$6.isEmpty()) {
                    getResources(packResourcesResourceOutput3, string1, $$8, p_248228_);
                } else {
                    getResources($$6::putIfAbsent, string1, $$8, p_248228_);
                    $$6.forEach(packResourcesResourceOutput3);
                }
            }
        }).ifRight(p_248234_ -> LOGGER.error("Invalid path {}: {}", string2, p_248234_.message()));
    }

    private static void getResources(PackResources.ResourceOutput packResourcesResourceOutput0, String string1, Path path2, List<String> listString3) {
        Path $$4 = path2.resolve(string1);
        PathPackResources.listPath(string1, $$4, listString3, packResourcesResourceOutput0);
    }

    @Nullable
    @Override
    public IoSupplier<InputStream> getResource(PackType packType0, ResourceLocation resourceLocation1) {
        return (IoSupplier<InputStream>) FileUtil.decomposePath(resourceLocation1.getPath()).get().map(p_248224_ -> {
            String $$3 = resourceLocation1.getNamespace();
            for (Path $$4 : (List) this.pathsForType.get(packType0)) {
                Path $$5 = FileUtil.resolvePath($$4.resolve($$3), p_248224_);
                if (Files.exists($$5, new LinkOption[0]) && PathPackResources.validatePath($$5)) {
                    return IoSupplier.create($$5);
                }
            }
            return null;
        }, p_248230_ -> {
            LOGGER.error("Invalid path {}: {}", resourceLocation1, p_248230_.message());
            return null;
        });
    }

    @Override
    public Set<String> getNamespaces(PackType packType0) {
        return this.namespaces;
    }

    @Nullable
    @Override
    public <T> T getMetadataSection(MetadataSectionSerializer<T> metadataSectionSerializerT0) {
        IoSupplier<InputStream> $$1 = this.getRootResource("pack.mcmeta");
        if ($$1 != null) {
            try {
                InputStream $$2 = $$1.get();
                Object var5;
                label53: {
                    try {
                        T $$3 = AbstractPackResources.getMetadataFromStream(metadataSectionSerializerT0, $$2);
                        if ($$3 != null) {
                            var5 = $$3;
                            break label53;
                        }
                    } catch (Throwable var7) {
                        if ($$2 != null) {
                            try {
                                $$2.close();
                            } catch (Throwable var6) {
                                var7.addSuppressed(var6);
                            }
                        }
                        throw var7;
                    }
                    if ($$2 != null) {
                        $$2.close();
                    }
                    return this.metadata.get(metadataSectionSerializerT0);
                }
                if ($$2 != null) {
                    $$2.close();
                }
                return (T) var5;
            } catch (IOException var8) {
            }
        }
        return this.metadata.get(metadataSectionSerializerT0);
    }

    @Override
    public String packId() {
        return "vanilla";
    }

    @Override
    public boolean isBuiltin() {
        return true;
    }

    @Override
    public void close() {
    }

    public ResourceProvider asProvider() {
        return p_248239_ -> Optional.ofNullable(this.getResource(PackType.CLIENT_RESOURCES, p_248239_)).map(p_248221_ -> new Resource(this, p_248221_));
    }
}